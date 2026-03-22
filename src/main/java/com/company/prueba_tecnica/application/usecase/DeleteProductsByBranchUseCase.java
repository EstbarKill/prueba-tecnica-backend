package com.company.prueba_tecnica.application.usecase;


import com.company.prueba_tecnica.infrastructure.persistence.repository.ProductMongoRepository;
import com.company.prueba_tecnica.domain.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProductsByBranchUseCase {

private final ProductMongoRepository productRepository;

    public Mono<Void> execute(String branchId, String productId) {

        return productRepository.existsByIdAndBranchId(productId, branchId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new NotFoundException(
                                "Product " + productId + " not found in branch " + branchId
                        ));
                    }

                    return productRepository.deleteById(productId);
                });
    }
}