package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.exception.NotFoundException;
import com.company.prueba_tecnica.domain.model.Product;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

/**
 * Caso de uso alternativo para actualizar el stock de un producto.
 *
 * Versión simplificada de UpdateProductStockUseCase: retorna la entidad
 * de dominio Product directamente en lugar del ProductDTO.
 *
 * Nota: actualmente no está conectado a ningún controlador.
 * La versión activa en la API es UpdateProductStockUseCase.
 */
@Service
@RequiredArgsConstructor
public class UpdateStockUseCase {

    private final ProductRepository productRepository;

    /**
     * Ejecuta el caso de uso.
     *
     * Flujo reactivo:
     *   1. Busca el producto por ID → NotFoundException si no existe
     *   2. Actualiza el stock directamente con setStock() (sin validación de dominio)
     *   3. Persiste y retorna la entidad actualizada
     *
     * @param productId ID del producto a actualizar
     * @param stock     nuevo valor de stock
     * @return Mono con la entidad Product actualizada
     */
    public Mono<Product> execute(String productId, Integer stock) {
        return productRepository.findById(productId)
                // Si el producto no existe, propagar el error apropiado
                .switchIfEmpty(Mono.error(new NotFoundException("Product not found")))
                .flatMap(product -> {
                    // Actualiza el stock usando el setter generado por Lombok (@Data)
                    product.setStock(stock);
                    return productRepository.save(product);
                });
    }
}
