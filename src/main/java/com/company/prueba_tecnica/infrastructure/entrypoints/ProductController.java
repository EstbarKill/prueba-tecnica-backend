package com.company.prueba_tecnica.infrastructure.entrypoints;

import com.company.prueba_tecnica.application.usecase.CreateProductUseCase;
import com.company.prueba_tecnica.application.usecase.dto.ProductDTO;
import com.company.prueba_tecnica.application.usecase.dto.UpdateNameDTO;
import com.company.prueba_tecnica.application.usecase.dto.UpdateStockDTO;
import com.company.prueba_tecnica.application.usecase.UpdateProductNameUseCase;
import com.company.prueba_tecnica.application.usecase.UpdateProductStockUseCase;
import com.company.prueba_tecnica.domain.model.Product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductNameUseCase updateProductNameUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;

    @PostMapping
    public Mono<Product> create(@RequestBody Product request) {
        return createProductUseCase.execute(request.getName(), request.getStock(), request.getBranchId());
    }

    @PutMapping("/{id}/name")
    public Mono<ProductDTO> updateName(
            @PathVariable String id,
            @RequestBody UpdateNameDTO request
            ){
        return updateProductNameUseCase.execute(id, request);
    }


    @PutMapping("/{id}/stock")
    public Mono<ProductDTO> updateStock(
            @PathVariable String id,
            @RequestBody UpdateStockDTO request
        ) {
        return updateProductStockUseCase.execute(id, request);
    }
}