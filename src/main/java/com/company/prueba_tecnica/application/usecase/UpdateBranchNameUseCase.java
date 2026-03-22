package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.application.usecase.dto.BranchDTO;
import com.company.prueba_tecnica.application.usecase.dto.ProductDTO;
import com.company.prueba_tecnica.application.usecase.dto.UpdateNameDTO;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateBranchNameUseCase {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public Mono<BranchDTO> execute(String id, UpdateNameDTO request) {

        return branchRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Branch not found")))

                // 🔥 VALIDACIÓN DE DUPLICADO
                .flatMap(branch ->
                        branchRepository.existsByNameAndFranchiseIdAndIdNot(
                                        request.getName(),
                                        branch.getFranchiseId(),
                                        id
                                )
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new RuntimeException(
                                                "Branch name already exists in this franchise"
                                        ));
                                    }

                                    branch.rename(request.getName());
                                    return branchRepository.save(branch);
                                })
                )

                // 🔥 RESPONSE
                .flatMap(saved ->
                        productRepository.findByBranchId(saved.getId())
                                .map(product -> new ProductDTO(
                                        product.getId(),
                                        product.getName(),
                                        product.getStock()
                                ))
                                .collectList()
                                .map(products -> new BranchDTO(
                                        saved.getId(),
                                        saved.getName(),
                                        products
                                ))
                );
    }
}