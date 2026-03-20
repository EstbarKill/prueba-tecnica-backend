package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.model.Product;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    public Mono<Product> execute(String name, Integer stock, String branchId) {

        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new RuntimeException("Branch not found")))
                .flatMap(branch -> {

                    Product product = Product.builder()
                            .name(name)
                            .stock(stock)
                            .branchId(branch.getId())
                            .build();

                    return productRepository.save(product);
                });
    }
}