package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.application.usecase.dto.*;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class GetTopProductByBranchInFranchiseUseCase {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public Mono<FranchiseTopProductsDTO> execute(String franchiseId) {

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))
                .flatMap(franchise ->

                        branchRepository.findByFranchiseId(franchise.getId())
                                .flatMapSequential(branch ->

                                        productRepository.findByBranchId(branch.getId())
                                                .collectList()
                                                .map(products -> {

                                                    TopProductDTO topProduct = products.stream()
                                                            .max(Comparator.comparingInt(p -> p.getStock()))
                                                            .map(p -> new TopProductDTO(
                                                                    p.getId(),
                                                                    p.getName(),
                                                                    p.getStock()
                                                            ))
                                                            .orElse(null);

                                                    var productDTOs = products.stream()
                                                            .map(p -> new ProductDTO(
                                                                    p.getId(),
                                                                    p.getName(),
                                                                    p.getStock()
                                                            ))
                                                            .toList();

                                                    return new BranchWithProductsAndTopDTO(
                                                            branch.getId(),
                                                            branch.getName(),
                                                            topProduct,
                                                            productDTOs
                                                    );
                                                })
                                )
                                .collectList()
                                .map(branches -> new FranchiseTopProductsDTO(
                                        franchise.getId(),
                                        franchise.getName(),
                                        branches
                                ))
                );
    }
}