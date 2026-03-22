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

@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
public class BranchController {

    private final CreateBranchUseCase createBranchUseCase;
    private final UpdateBranchNameUseCase updateBranchNameUseCase;
    private final DeleteProductsByBranchUseCase deleteProductsByBranchUseCase;


    @PostMapping
    public Mono<Branch> create(@RequestBody Branch request) {
        return createBranchUseCase.execute(
                request.getName(),
                request.getFranchiseId()
        );
    }

    @PutMapping("/{id}/name")
    public Mono<BranchDTO> updateName(
            @PathVariable String id,
            @Valid @RequestBody UpdateNameDTO request
    ) {
        return updateBranchNameUseCase.execute(id, request);
    }

@DeleteMapping("/{productId}/branches/{branchId}")
public Mono<ResponseEntity<Void>> deleteProductFromBranch(
        @PathVariable String branchId,
        @PathVariable String productId
) {
    return deleteProductsByBranchUseCase
            .execute(branchId, productId)
            .thenReturn(ResponseEntity.noContent().build());
}
}