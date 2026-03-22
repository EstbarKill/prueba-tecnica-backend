package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.exception.NotFoundException;
import com.company.prueba_tecnica.domain.model.Product;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateStockUseCase {

    private final ProductRepository productRepository;

    public Mono<Product> execute(String productId, Integer stock) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new NotFoundException("Product not found")))
                .flatMap(product -> {
                    product.setStock(stock);
                    return productRepository.save(product);
                });
    }
}