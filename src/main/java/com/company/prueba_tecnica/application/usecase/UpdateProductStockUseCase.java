package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.application.usecase.dto.ProductDTO;
import com.company.prueba_tecnica.domain.exception.NotFoundException;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import com.company.prueba_tecnica.application.usecase.dto.UpdateStockDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

/**
 * Caso de uso: Actualizar el stock de un producto existente.
 *
 * La validación de que el stock no sea negativo está encapsulada
 * en la entidad de dominio Product.updateStock(), respetando
 * el principio de que las reglas de negocio viven en el dominio.
 */
@Service
@RequiredArgsConstructor
public class UpdateProductStockUseCase {

    private final ProductRepository productRepository;

    /**
     * Ejecuta el caso de uso.
     *
     * Flujo reactivo:
     *   1. Busca el producto por ID → NotFoundException si no existe
     *   2. Llama a product.updateStock() que valida stock >= 0
     *      → IllegalArgumentException si el valor es negativo
     *   3. Persiste el cambio y retorna el ProductDTO actualizado
     *
     * @param productId ID del producto a actualizar
     * @param request   DTO con el nuevo valor de stock
     * @return Mono con el producto actualizado como ProductDTO
     */
    public Mono<ProductDTO> execute(String productId, UpdateStockDTO request) {

        return productRepository.findById(productId)
                // Si el producto no existe, propagar el error apropiado
                .switchIfEmpty(Mono.error(new NotFoundException("Product not found")))
                .flatMap(product -> {
                    // La validación de negocio (stock >= 0) ocurre dentro de la entidad
                    product.updateStock(request.getStock());
                    // Persistir el nuevo valor de stock
                    return productRepository.save(product);
                })
                // Mapear la entidad guardada al DTO de respuesta
                .map(saved -> new ProductDTO(
                        saved.getId(),
                        saved.getName(),
                        saved.getStock()
                ));
    }
}
