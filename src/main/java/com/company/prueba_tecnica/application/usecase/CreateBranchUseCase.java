package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.exception.NotFoundException;
import com.company.prueba_tecnica.domain.model.Branch;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateBranchUseCase {

    private final BranchRepository branchRepository;

    public Mono<Branch> execute(String name, String franchiseId) {

        Branch branch = Branch.builder()
                .name(name)
                .franchiseId(franchiseId)
                .build();

        return branchRepository.existsByNameAndFranchiseId(name, franchiseId)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new NotFoundException(
                                "Branch name already exists in this franchise"
                        ));
                    }

                    return branchRepository.save(branch);
                });
    }
}