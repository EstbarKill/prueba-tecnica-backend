package com.company.prueba_tecnica.infrastructure.entrypoints;

import com.company.prueba_tecnica.application.usecase.CreateBranchUseCase;
import com.company.prueba_tecnica.application.usecase.DeleteProductsByBranchUseCase;
import com.company.prueba_tecnica.application.usecase.dto.BranchDTO;
import com.company.prueba_tecnica.application.usecase.dto.UpdateNameDTO;
import com.company.prueba_tecnica.application.usecase.UpdateBranchNameUseCase;
import com.company.prueba_tecnica.domain.model.Branch;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Controlador REST para el recurso Sucursal.
 *
 * Expone los endpoints HTTP relacionados con la gestión de sucursales.
 * Delega toda la lógica a los casos de uso correspondientes.
 *
 * Base URL: /branches
 */
@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
public class BranchController {

    /** Caso de uso para crear una nueva sucursal. */
    private final CreateBranchUseCase createBranchUseCase;

    /** Caso de uso para actualizar el nombre de una sucursal. */
    private final UpdateBranchNameUseCase updateBranchNameUseCase;

    /** Caso de uso para eliminar un producto de una sucursal. */
    private final DeleteProductsByBranchUseCase deleteProductsByBranchUseCase;

    /**
     * POST /branches
     * Crea una nueva sucursal asociada a una franquicia existente.
     *
     * El body debe contener name y franchiseId.
     * Se valida unicidad del nombre dentro de la franquicia en CreateBranchUseCase.
     *
     * @param request cuerpo del request con los datos de la sucursal
     * @return Mono con la sucursal creada
     */
    @PostMapping
    public Mono<Branch> create(@RequestBody Branch request) {
        return createBranchUseCase.execute(
                request.getName(),
                request.getFranchiseId()
        );
    }

    /**
     * PUT /branches/{id}/name
     * Actualiza el nombre de una sucursal existente.
     *
     * @Valid activa la validación del DTO (campo name no puede ser vacío).
     *
     * @param id      ID de la sucursal a actualizar
     * @param request body con el nuevo nombre
     * @return Mono con la sucursal actualizada y sus productos actuales
     */
    @PutMapping("/{id}/name")
    public Mono<BranchDTO> updateName(
            @PathVariable String id,
            @Valid @RequestBody UpdateNameDTO request
    ) {
        return updateBranchNameUseCase.execute(id, request);
    }

    /**
     * DELETE /branches/{productId}/branches/{branchId}
     * Elimina un producto específico de una sucursal.
     *
     * Verifica que el producto pertenezca a la sucursal antes de eliminarlo.
     * Retorna HTTP 204 No Content al completarse correctamente.
     *
     * @param branchId  ID de la sucursal propietaria del producto
     * @param productId ID del producto a eliminar
     * @return Mono con ResponseEntity vacío (204)
     */
    @DeleteMapping("/{productId}/branches/{branchId}")
    public Mono<ResponseEntity<Void>> deleteProductFromBranch(
            @PathVariable String branchId,
            @PathVariable String productId
    ) {
        return deleteProductsByBranchUseCase
                .execute(branchId, productId)
                // thenReturn convierte el Mono<Void> en la respuesta HTTP apropiada
                .thenReturn(ResponseEntity.noContent().build());
    }
}
