package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.application.usecase.dto.BranchDTO;
import com.company.prueba_tecnica.application.usecase.dto.FranchiseDTO;
import com.company.prueba_tecnica.application.usecase.dto.ProductDTO;
import com.company.prueba_tecnica.domain.model.Branch;
import com.company.prueba_tecnica.domain.model.Franchise;
import com.company.prueba_tecnica.domain.model.Product;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class GetFranchiseStructureUseCase {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public Flux<FranchiseDTO> execute() {

        return franchiseRepository.findAll()
        .flatMap(franchise ->
                branchRepository.findByFranchiseId(franchise.getId())
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
                                franchise.getId(),
                                franchise.getName(),
                                branches
                        ))
        );
    }

    @SuppressWarnings("unused")
private Mono<FranchiseDTO> mapFranchise(Franchise franchise) {

        return branchRepository.findByFranchiseId(franchise.getId())
                .flatMap(branch -> mapBranch(branch))
                .collectList()
                .map(branches -> new FranchiseDTO(
                        franchise.getId(),
                        franchise.getName(),
                        branches
                ));
    }

    private Mono<BranchDTO> mapBranch(Branch branch) {

        return productRepository.findByBranchId(branch.getId())
                .sort(Comparator.comparing(Product::getStock).reversed())
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
                ));
    }
}