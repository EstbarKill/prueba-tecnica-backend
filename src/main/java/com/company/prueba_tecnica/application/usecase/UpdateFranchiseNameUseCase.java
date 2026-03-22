package com.company.prueba_tecnica.application.usecase;


import com.company.prueba_tecnica.application.usecase.dto.UpdateNameDTO;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;
import com.company.prueba_tecnica.domain.exception.DuplicateResourceException;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import com.company.prueba_tecnica.application.usecase.dto.BranchDTO;
import com.company.prueba_tecnica.application.usecase.dto.ProductDTO;
import com.company.prueba_tecnica.application.usecase.dto.FranchiseDTO;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateFranchiseNameUseCase {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public Mono<FranchiseDTO> execute(String id, UpdateNameDTO request) {

        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))

                // 🔥 VALIDACIÓN DE DUPLICADO DE NAME
                .flatMap(franchise ->
                        franchiseRepository.existsByNameAndIdNot(request.getName(), id)
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new DuplicateResourceException(
                                                "Franchise name already exists"
                                        ));
                                    }

                                    franchise.rename(request.getName());
                                    return franchiseRepository.save(franchise);
                                })
                )

                // 🔥 ARMADO DE RESPONSE
                .flatMap(saved ->
                        branchRepository.findByFranchiseId(saved.getId())
                                .flatMap(branch ->
                                        productRepository.findByBranchId(branch.getId())
                                                .map(product -> new ProductDTO(
                                                        product.getId(),
                                                        product.getName(),
                                                        product.getStock()
                                                ))
                                                .collectList()
                                                .map(products -> new BranchDTO(
                                                        branch.getId(),
                                                        branch.getName(),
                                                        products
                                                ))
                                )
                                .collectList()
                                .map(branches -> new FranchiseDTO(
                                        saved.getId(),
                                        saved.getName(),
                                        branches
                                ))
                );
    }
}