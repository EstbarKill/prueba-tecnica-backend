package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.model.Branch;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateBranchUseCase {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public Mono<Branch> execute(String name, String franchiseId) {

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))
                .flatMap(franchise -> {
                    Branch branch = Branch.builder()
                            .id("Br"+franchiseId)
                            .name(name)
                            .franchiseId(franchiseId)
                            .build();

                    return branchRepository.save(branch);
                });
    }
}