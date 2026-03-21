package com.company.prueba_tecnica.infrastructure.entrypoints;

import com.company.prueba_tecnica.application.usecase.CreateProductUseCase;
import com.company.prueba_tecnica.domain.model.Product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUseCase createProductUseCase;

    @PostMapping
    public Mono<Product> create(@RequestBody Product request) {
        return createProductUseCase.execute(request.getName(),request.getStock(),request.getBranchId());
    }

}