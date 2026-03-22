package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.application.usecase.dto.ProductDTO;
import com.company.prueba_tecnica.application.usecase.dto.UpdateNameDTO;
import com.company.prueba_tecnica.domain.exception.NotFoundException;
import com.company.prueba_tecnica.domain.exception.DuplicateResourceException;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

/**
 * Caso de uso: Actualizar el nombre de un producto existente.
 *
 * Valida que el nuevo nombre no esté en uso por otro producto
 * dentro de la misma sucursal y retorna el producto actualizado.
 */
@Service
@RequiredArgsConstructor
public class UpdateProductNameUseCase {

    private final ProductRepository productRepository;

    /**
     * Ejecuta el caso de uso.
     *
     * Flujo reactivo:
     *   1. Busca el producto por ID → NotFoundException si no existe
     *   2. Verifica que el nuevo nombre no esté en uso por otro producto
     *      dentro de la misma sucursal → DuplicateResourceException si hay conflicto
     *   3. Llama a product.rename() y persiste el cambio
     *   4. Retorna el ProductDTO con los datos actualizados
     *
     * @param id      ID del producto a actualizar
     * @param request DTO con el nuevo nombre validado con @NotBlank
     * @return Mono con el producto actualizado como ProductDTO
     */
    public Mono<ProductDTO> execute(String id, UpdateNameDTO request) {

        return productRepository.findById(id)
                // Si el producto no existe, propagar el error apropiado
                .switchIfEmpty(Mono.error(new NotFoundException("Product not found")))

                // Validar que el nuevo nombre no esté ocupado en la misma sucursal
                .flatMap(product ->
                        // existsByNameAndBranchIdAndIdNot excluye el producto actual
                        productRepository.existsByNameAndBranchIdAndIdNot(
                                        request.getName(),
                                        product.getBranchId(), // sucursal a la que pertenece
                                        id                     // excluir el propio producto
                                )
                                .flatMap(exists -> {
                                    if (exists) {
                                        // Otro producto ya tiene ese nombre en esta sucursal
                                        return Mono.error(new DuplicateResourceException(
                                                "Product name already exists in this branch"
                                        ));
                                    }

                                    // Aplicar el cambio de nombre en la entidad de dominio
                                    product.rename(request.getName());
                                    // Persistir el cambio
                                    return productRepository.save(product);
                                })
                )

                // Mapear la entidad guardada al DTO de respuesta
                .map(saved -> new ProductDTO(
                        saved.getId(),
                        saved.getName(),
                        saved.getStock()
                ));
    }
}
