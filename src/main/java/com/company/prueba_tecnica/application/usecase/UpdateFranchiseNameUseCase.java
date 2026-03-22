package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.application.usecase.dto.UpdateNameDTO;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;
import com.company.prueba_tecnica.domain.exception.DuplicateResourceException;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import com.company.prueba_tecnica.application.usecase.dto.BranchDTO;
import com.company.prueba_tecnica.application.usecase.dto.ProductDTO;
import com.company.prueba_tecnica.application.usecase.dto.FranchiseDTO;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

/**
 * Caso de uso: Actualizar el nombre de una franquicia existente.
 *
 * Valida que el nuevo nombre no esté en uso por otra franquicia
 * y retorna la franquicia actualizada con su estructura completa.
 */
@Service
@RequiredArgsConstructor
public class UpdateFranchiseNameUseCase {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    /**
     * Ejecuta el caso de uso.
     *
     * Flujo reactivo:
     *   1. Busca la franquicia por ID → RuntimeException si no existe
     *   2. Verifica que el nuevo nombre no esté usado por otra franquicia
     *      → DuplicateResourceException si hay conflicto
     *   3. Llama a franchise.rename() y persiste el cambio
     *   4. Construye la respuesta completa con sucursales y productos
     *
     * @param id      ID de la franquicia a actualizar
     * @param request DTO con el nuevo nombre validado con @NotBlank
     * @return Mono con la franquicia actualizada y su estructura completa
     */
    public Mono<FranchiseDTO> execute(String id, UpdateNameDTO request) {

        return franchiseRepository.findById(id)
                // Si no existe la franquicia, propagar el error
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))

                // Validar que el nuevo nombre no esté ocupado por otra franquicia
                .flatMap(franchise ->
                        // existsByNameAndIdNot excluye la franquicia actual de la búsqueda
                        franchiseRepository.existsByNameAndIdNot(request.getName(), id)
                                .flatMap(exists -> {
                                    if (exists) {
                                        // Otro documento ya tiene ese nombre
                                        return Mono.error(new DuplicateResourceException(
                                                "Franchise name already exists"
                                        ));
                                    }

                                    // Aplicar el cambio de nombre en la entidad de dominio
                                    franchise.rename(request.getName());
                                    // Persistir el cambio
                                    return franchiseRepository.save(franchise);
                                })
                )

                // Construir la respuesta completa con sucursales y productos
                .flatMap(saved ->
                        branchRepository.findByFranchiseId(saved.getId())
                                .flatMap(branch ->
                                        // Para cada sucursal, obtener sus productos
                                        productRepository.findByBranchId(branch.getId())
                                                .map(product -> new ProductDTO(
                                                        product.getId(),
                                                        product.getName(),
                                                        product.getStock()
                                                ))
                                                .collectList()
                                                .map(products -> new BranchDTO(
                                                        branch.getId(),
                                                        branch.getName(),
                                                        products
                                                ))
                                )
                                .collectList()
                                // Armar el DTO final con el nuevo nombre y la estructura
                                .map(branches -> new FranchiseDTO(
                                        saved.getId(),
                                        saved.getName(),
                                        branches
                                ))
                );
    }
}
