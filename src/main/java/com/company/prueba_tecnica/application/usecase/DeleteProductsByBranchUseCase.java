package com.company.prueba_tecnica.application.usecase;


import com.company.prueba_tecnica.domain.repository.BranchRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProductsByBranchUseCase {


    private final BranchRepository branchRepository;

    public Mono<Void> execute(String branchId) {
        return branchRepository.deleteBranchWithProducts(branchId);
    }
}