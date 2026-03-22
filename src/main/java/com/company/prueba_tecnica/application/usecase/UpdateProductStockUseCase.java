package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.application.usecase.dto.ProductDTO;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import com.company.prueba_tecnica.application.usecase.dto.UpdateStockDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProductStockUseCase {

    private final ProductRepository productRepository;

    public Mono<ProductDTO> execute(String productId, UpdateStockDTO request) {

        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found")))
                .flatMap(product -> {
                    product.updateStock(request.getStock()); // lógica en dominio
                    return productRepository.save(product);
                })
                .map(saved -> new ProductDTO(
                        saved.getId(),
                        saved.getName(),
                        saved.getStock()
                ));
    }
}