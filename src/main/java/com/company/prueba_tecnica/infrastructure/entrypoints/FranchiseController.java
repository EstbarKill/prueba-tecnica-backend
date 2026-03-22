package com.company.prueba_tecnica.infrastructure.entrypoints;

import com.company.prueba_tecnica.application.usecase.CreateFranchiseUseCase;
import com.company.prueba_tecnica.application.usecase.GetFranchiseStructureUseCase;
import com.company.prueba_tecnica.application.usecase.GetTopProductsByFranchiseUseCase;
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

@RestController
@RequestMapping("/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final GetFranchiseStructureUseCase getFranchiseStructureUseCase;
    private final GetTopProductsByFranchiseUseCase getTopProductsByFranchiseUseCase;
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    /**
     * POST /franchises
     * Crea una nueva franquicia.
     */
    @PostMapping
    public Mono<Franchise> create(@RequestBody Franchise request) {
        return createFranchiseUseCase.execute(request.getId(), request.getName());
    }

    /**
     * GET /franchises/structure
     * Retorna todas las franquicias con sus sucursales y productos.
     */
    @GetMapping("/structure")
    public Flux<FranchiseDTO> getStructure() {
        return getFranchiseStructureUseCase.execute();
    }

    @GetMapping("/{franchiseId}/top-products")
    public Mono<FranchiseTopProductsDTO> getTopProducts(
            @PathVariable String franchiseId) {
        return getTopProductsByFranchiseUseCase.execute(franchiseId);
    }

    @PutMapping("/{id}/name")
    public Mono<FranchiseDTO> updateName(
            @PathVariable String id,
            @Valid @RequestBody UpdateNameDTO request) {
        return updateFranchiseNameUseCase.execute(id, request);
    }
}