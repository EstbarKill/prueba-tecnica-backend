package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.application.usecase.dto.TopProductByBranchDTO;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTopProductsByFranchiseUseCase {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public Flux<TopProductByBranchDTO> execute(String franchiseId) {

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("Franchise not found")
                ))
                .flatMapMany(franchise ->
                        branchRepository.findByFranchiseId(franchise.getId())
                )
                .flatMap(branch ->
                        productRepository.findByBranchId(branch.getId())
                                .sort((p1, p2) ->
                                        Integer.compare(p2.getStock(), p1.getStock())
                                )
                                .next()
                                .map(top -> new TopProductByBranchDTO(
                                        branch.getId(),
                                        branch.getName(),
                                        top.getId(),
                                        top.getName(),
                                        top.getStock()
                                ))
                );
    }
}