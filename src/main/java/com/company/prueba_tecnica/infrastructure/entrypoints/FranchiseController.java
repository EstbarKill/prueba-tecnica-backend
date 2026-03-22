package com.company.prueba_tecnica.infrastructure.entrypoints;

import com.company.prueba_tecnica.application.usecase.CreateFranchiseUseCase;
import com.company.prueba_tecnica.application.usecase.GetFranchiseStructureUseCase;
import com.company.prueba_tecnica.application.usecase.GetTopProductByBranchInFranchiseUseCase;
import com.company.prueba_tecnica.application.usecase.UpdateFranchiseNameUseCase;
import com.company.prueba_tecnica.application.usecase.dto.FranchiseDTO;
import com.company.prueba_tecnica.application.usecase.dto.FranchiseTopProductsDTO;
import com.company.prueba_tecnica.application.usecase.dto.UpdateNameDTO;
import com.company.prueba_tecnica.domain.model.Franchise;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador REST para el recurso Franquicia.
 *
 * Expone los endpoints HTTP relacionados con la gestión de franquicias.
 * Delega toda la lógica a los casos de uso correspondientes.
 * No contiene lógica de negocio propia.
 *
 * Base URL: /franchises
 */
@RestController
@RequestMapping("/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    /** Caso de uso para crear una nueva franquicia. */
    private final CreateFranchiseUseCase createFranchiseUseCase;

    /** Caso de uso para obtener la estructura completa de todas las franquicias. */
    private final GetFranchiseStructureUseCase getFranchiseStructureUseCase;

    /** Caso de uso para obtener el producto top por sucursal de una franquicia. */
    private final GetTopProductByBranchInFranchiseUseCase getTopProductByBranchInFranchiseUseCase;

    /** Caso de uso para actualizar el nombre de una franquicia. */
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    /**
     * POST /franchises
     * Crea una nueva franquicia en el sistema.
     *
     * El body debe contener id y name. Se valida unicidad de ambos
     * en CreateFranchiseUseCase.
     *
     * @param request cuerpo del request con los datos de la franquicia
     * @return Mono con la franquicia creada
     */
    @PostMapping
    public Mono<Franchise> create(@RequestBody Franchise request) {
        return createFranchiseUseCase.execute(request.getId(), request.getName());
    }

    /**
     * GET /franchises/structure
     * Retorna todas las franquicias con sus sucursales y productos anidados.
     *
     * Útil para obtener una vista completa del sistema en una sola llamada.
     *
     * @return Flux con todas las franquicias estructuradas
     */
    @GetMapping("/structure")
    public Flux<FranchiseDTO> getStructure() {
        return getFranchiseStructureUseCase.execute();
    }

    /**
     * GET /franchises/{franchiseId}/branches/top-products
     * Retorna el producto con mayor stock por sucursal para la franquicia dada.
     *
     * @param franchiseId ID de la franquicia a consultar
     * @return Mono con la franquicia y el top product de cada sucursal
     */
    @GetMapping("/{franchiseId}/branches/top-products")
    public Mono<FranchiseTopProductsDTO> getTopProductsByBranch(
            @PathVariable String franchiseId
    ) {
        return getTopProductByBranchInFranchiseUseCase.execute(franchiseId);
    }

    /**
     * PUT /franchises/{id}/name
     * Actualiza el nombre de una franquicia existente.
     *
     * @Valid activa la validación de UpdateNameDTO (campo name no puede ser vacío).
     *
     * @param id      ID de la franquicia a actualizar
     * @param request body con el nuevo nombre
     * @return Mono con la franquicia actualizada y su estructura completa
     */
    @PutMapping("/{id}/name")
    public Mono<FranchiseDTO> updateName(
            @PathVariable String id,
            @Valid @RequestBody UpdateNameDTO request) {
        return updateFranchiseNameUseCase.execute(id, request);
    }
}
