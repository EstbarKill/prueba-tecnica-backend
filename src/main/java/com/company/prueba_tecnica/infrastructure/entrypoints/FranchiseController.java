package com.company.prueba_tecnica.infrastructure.entrypoints;

import com.company.prueba_tecnica.application.usecase.CreateFranchiseUseCase;
import com.company.prueba_tecnica.application.usecase.GetFranchiseStructureUseCase;
import com.company.prueba_tecnica.application.usecase.dto.FranchiseDTO;
import com.company.prueba_tecnica.domain.model.Franchise;
import com.company.prueba_tecnica.infrastructure.web.response.ApiResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final GetFranchiseStructureUseCase getFranchiseStructureUseCase;

    @PostMapping
    public Mono<Franchise> create(@RequestBody Franchise request) {
        return createFranchiseUseCase.execute(request.getId(),request.getName());
    }

@GetMapping("/structure")
public Flux<FranchiseDTO> getStructure() {
    return getFranchiseStructureUseCase.execute();
}
}