package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.model.Product;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTopProductsByFranchiseUseCase {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public Flux<Product> execute(String franchiseId) {

        return branchRepository.findByFranchiseId(franchiseId)
                .flatMap(branch ->
                        productRepository.findByBranchId(branch.getId())
                                .sort((p1, p2) -> Integer.compare(p2.getStock(), p1.getStock()))
                                .next()
                );
    }
}