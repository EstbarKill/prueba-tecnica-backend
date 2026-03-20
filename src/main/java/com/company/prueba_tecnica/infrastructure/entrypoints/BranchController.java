package com.company.prueba_tecnica.infrastructure.entrypoints;

import com.company.prueba_tecnica.application.usecase.CreateBranchUseCase;
import com.company.prueba_tecnica.domain.model.Branch;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
public class BranchController {

    private final CreateBranchUseCase createBranchUseCase;

    @PostMapping
    public Mono<Branch> create(@RequestBody Branch request) {
        return createBranchUseCase.execute(
                request.getName(),
                request.getFranchiseId()
        );
    }
}