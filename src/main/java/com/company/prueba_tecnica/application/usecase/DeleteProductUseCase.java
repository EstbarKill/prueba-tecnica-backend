package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final ProductRepository productRepository;

    public Mono<Void> execute(String productId) {
        return productRepository.deleteById(productId);
    }
}