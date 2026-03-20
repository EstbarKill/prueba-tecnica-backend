package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.model.Franchise;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GetAllFranchisesUseCase {

    private final FranchiseRepository franchiseRepository;

    public Flux<Franchise> execute() {
        return franchiseRepository.findAll();
    }
}