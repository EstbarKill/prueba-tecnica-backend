package com.company.prueba_tecnica.infrastructure.entrypoints;

import com.company.prueba_tecnica.application.usecase.CreateFranchiseUseCase;
import com.company.prueba_tecnica.application.usecase.GetAllFranchisesUseCase;
import com.company.prueba_tecnica.domain.model.Franchise;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final GetAllFranchisesUseCase getAllFranchisesUseCase;

    @PostMapping
    public Mono<Franchise> create(@RequestBody Franchise request) {
        return createFranchiseUseCase.execute(request.getId(),request.getName());
    }

    @GetMapping
    public Flux<Franchise> getAll() {
        return getAllFranchisesUseCase.execute();
    }
}