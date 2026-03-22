package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.application.usecase.dto.ProductDTO;
import com.company.prueba_tecnica.application.usecase.dto.UpdateNameDTO;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProductNameUseCase {

    private final ProductRepository productRepository;

    public Mono<ProductDTO> execute(String id, UpdateNameDTO request) {

        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found")))

                // 🔥 VALIDACIÓN DUPLICADO POR BRANCH
                .flatMap(product ->
                        productRepository.existsByNameAndBranchIdAndIdNot(
                                        request.getName(),
                                        product.getBranchId(),
                                        id
                                )
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new RuntimeException(
                                                "Product name already exists in this branch"
                                        ));
                                    }

                                    product.rename(request.getName());
                                    return productRepository.save(product);
                                })
                )

                // 🔥 RESPONSE
                .map(saved -> new ProductDTO(
                        saved.getId(),
                        saved.getName(),
                        saved.getStock()
                ));
    }
}