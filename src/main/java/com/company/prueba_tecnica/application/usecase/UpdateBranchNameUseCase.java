package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.application.usecase.dto.BranchDTO;
import com.company.prueba_tecnica.application.usecase.dto.ProductDTO;
import com.company.prueba_tecnica.application.usecase.dto.UpdateNameDTO;
import com.company.prueba_tecnica.domain.exception.DuplicateResourceException;
import com.company.prueba_tecnica.domain.exception.NotFoundException;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

/**
 * Caso de uso: Actualizar el nombre de una sucursal existente.
 *
 * Valida que el nuevo nombre no esté en uso por otra sucursal
 * dentro de la misma franquicia y retorna la sucursal actualizada
 * con sus productos actuales.
 */
@Service
@RequiredArgsConstructor
public class UpdateBranchNameUseCase {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    /**
     * Ejecuta el caso de uso.
     *
     * Flujo reactivo:
     *   1. Busca la sucursal por ID → NotFoundException si no existe
     *   2. Verifica que el nuevo nombre no esté en uso por otra sucursal
     *      dentro de la misma franquicia → DuplicateResourceException si hay conflicto
     *   3. Llama a branch.rename() y persiste el cambio
     *   4. Construye la respuesta con la sucursal actualizada y sus productos
     *
     * @param id      ID de la sucursal a actualizar
     * @param request DTO con el nuevo nombre validado con @NotBlank
     * @return Mono con la sucursal actualizada y su lista de productos
     */
    public Mono<BranchDTO> execute(String id, UpdateNameDTO request) {

        return branchRepository.findById(id)
                // Si la sucursal no existe, propagar el error apropiado
                .switchIfEmpty(Mono.error(new NotFoundException("Branch not found")))

                // Validar que el nuevo nombre no esté ocupado en la misma franquicia
                .flatMap(branch ->
                        // existsByNameAndFranchiseIdAndIdNot excluye la sucursal actual
                        branchRepository.existsByNameAndFranchiseIdAndIdNot(
                                        request.getName(),
                                        branch.getFranchiseId(), // franquicia a la que pertenece
                                        id                       // excluir la propia sucursal
                                )
                                .flatMap(exists -> {
                                    if (exists) {
                                        // Otra sucursal ya tiene ese nombre en esta franquicia
                                        return Mono.error(new DuplicateResourceException(
                                                "Branch name already exists in this franchise"
                                        ));
                                    }

                                    // Aplicar el cambio de nombre en la entidad de dominio
                                    branch.rename(request.getName());
                                    // Persistir el cambio
                                    return branchRepository.save(branch);
                                })
                )

                // Construir la respuesta con los productos actuales de la sucursal
                .flatMap(saved ->
                        productRepository.findByBranchId(saved.getId())
                                .map(product -> new ProductDTO(
                                        product.getId(),
                                        product.getName(),
                                        product.getStock()
                                ))
                                .collectList()
                                // Armar el DTO final con el nuevo nombre y productos
                                .map(products -> new BranchDTO(
                                        saved.getId(),
                                        saved.getName(),
                                        products
                                ))
                );
    }
}
