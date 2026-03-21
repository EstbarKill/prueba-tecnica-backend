package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.application.usecase.dto.BranchTopProductDTO;
import com.company.prueba_tecnica.application.usecase.dto.FranchiseTopProductsDTO;
import com.company.prueba_tecnica.application.usecase.dto.TopProductDTO;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTopProductsByFranchiseUseCase {

private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

   public Mono<FranchiseTopProductsDTO> execute(String franchiseId) {

    return franchiseRepository.findById(franchiseId)
            .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))

            .flatMap(franchise ->
                    branchRepository.findByFranchiseId(franchise.getId())

                            .flatMap(branch ->
                                    productRepository.findByBranchId(branch.getId())
                                            .collectList()
                                            .map(products -> {

                                                // si no hay productos
                                                if (products.isEmpty()) {
                                                    return new BranchTopProductDTO(
                                                            branch.getId(),
                                                            branch.getName(),
                                                            null
                                                    );
                                                }

                                                // encontrar el mayor stock
                                                var topProduct = products.stream()
                                                        .max((p1, p2) ->
                                                                Integer.compare(p1.getStock(), p2.getStock())
                                                        )
                                                        .orElse(null);

                                                return new BranchTopProductDTO(
                                                        branch.getId(),
                                                        branch.getName(),
                                                        topProduct == null ? null :
                                                                new TopProductDTO(
                                                                        topProduct.getId(),
                                                                        topProduct.getName(),
                                                                        topProduct.getStock()
                                                                )
                                                );
                                            })
                            )
                            .collectList()
                            .map(branches ->
                                    new FranchiseTopProductsDTO(
                                            franchise.getId(),
                                            franchise.getName(),
                                            branches
                                    )
                            )
            );
}
}