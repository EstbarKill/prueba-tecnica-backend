package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.application.usecase.dto.BranchDTO;
import com.company.prueba_tecnica.application.usecase.dto.ProductDTO;
import com.company.prueba_tecnica.application.usecase.dto.UpdateNameDTO;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateBranchNameUseCase {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

public Mono<BranchDTO> execute(String id, UpdateNameDTO request) {

    return branchRepository.findById(id)
            .switchIfEmpty(Mono.error(new RuntimeException("Branch not found")))
            .flatMap(branch -> {
                branch.rename(request.getName());
                return branchRepository.save(branch);
            })
            .flatMap(saved -> 
                productRepository.findByBranchId(saved.getId())
                .map(products -> new ProductDTO(
                    products.getId(),
                    products.getName(),
                    products.getStock()
                ))
                .collectList()
                .map(products -> new BranchDTO(
                    saved.getId(),
                    saved.getName(),
                    products
                ))
            );
}
}