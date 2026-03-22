package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.infrastructure.persistence.repository.ProductMongoRepository;
import com.company.prueba_tecnica.domain.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

/**
 * Caso de uso: Eliminar un producto específico de una sucursal.
 *
 * Antes de eliminar, verifica que el producto exista Y que pertenezca
 * a la sucursal indicada. Esto evita eliminar productos de otras sucursales
 * si se envía un branchId incorrecto.
 *
 * Nota: este caso de uso inyecta directamente ProductMongoRepository
 * (infraestructura) en lugar del ProductRepository del dominio.
 * Es una dependencia directa a la capa de infraestructura que podría
 * refactorizarse en el futuro añadiendo deleteById() al contrato del dominio.
 */
@Service
@RequiredArgsConstructor
public class DeleteProductsByBranchUseCase {

    /**
     * Repositorio de MongoDB para productos.
     * Se usa directamente por la necesidad de deleteById() y existsByIdAndBranchId().
     */
    private final ProductMongoRepository productRepository;

    /**
     * Ejecuta el caso de uso: verifica existencia y elimina el producto.
     *
     * Flujo reactivo:
     *   1. Verifica que el producto exista y pertenezca a la sucursal
     *      → NotFoundException si no se cumple alguna condición
     *   2. Elimina el producto por su ID
     *
     * @param branchId  ID de la sucursal propietaria del producto
     * @param productId ID del producto a eliminar
     * @return Mono vacío al completarse la eliminación
     */
    public Mono<Void> execute(String branchId, String productId) {

        // Verifica que el producto existe en esa sucursal específica
        return productRepository.existsByIdAndBranchId(productId, branchId)
                .flatMap(exists -> {
                    if (!exists) {
                        // El producto no existe o no pertenece a esta sucursal
                        return Mono.error(new NotFoundException(
                                "Product " + productId + " not found in branch " + branchId
                        ));
                    }

                    // Validación superada: eliminar el producto
                    return productRepository.deleteById(productId);
                });
    }
}
