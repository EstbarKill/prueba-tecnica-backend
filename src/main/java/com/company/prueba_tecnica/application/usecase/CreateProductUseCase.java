package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.model.Product;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import com.company.prueba_tecnica.domain.exception.DuplicateResourceException;
import com.company.prueba_tecnica.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

/**
 * Caso de uso: Agregar un nuevo producto a una sucursal existente.
 *
 * Realiza dos validaciones de negocio antes de persistir:
 *   1. Que la sucursal de destino exista
 *   2. Que no haya otro producto con el mismo nombre en esa sucursal
 */
@Service
@RequiredArgsConstructor
public class CreateProductUseCase {

    /** Repositorio de sucursales: necesario para verificar que la sucursal exista. */
    private final BranchRepository branchRepository;

    /** Repositorio de productos: para verificar duplicados y persistir. */
    private final ProductRepository productRepository;

    /**
     * Ejecuta el caso de uso: valida y crea el producto.
     *
     * Flujo reactivo:
     *   1. Busca la sucursal por ID → NotFoundException si no existe
     *   2. Verifica que el nombre del producto no esté duplicado en esa sucursal
     *      → DuplicateResourceException si ya existe
     *   3. Construye y persiste el producto
     *
     * @param name     nombre del producto
     * @param stock    cantidad inicial de stock (debe ser >= 0)
     * @param branchId ID de la sucursal donde se registrará el producto
     * @return Mono con el producto creado y persistido
     */
    public Mono<Product> execute(String name, Integer stock, String branchId) {

        // Paso 1: confirmar que la sucursal existe antes de continuar
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new NotFoundException("Branch not found")))

                .flatMap(branch ->

                        // Paso 2: verificar unicidad del nombre en la sucursal
                        productRepository.existsByNameAndBranchId(name, branch.getId())
                                .flatMap(exists -> {
                                    if (exists) {
                                        // Ya existe un producto con ese nombre en esta sucursal
                                        return Mono.error(new DuplicateResourceException(
                                                "Product name already exists in this branch"
                                        ));
                                    }

                                    // Paso 3: construir y persistir el producto
                                    Product product = Product.builder()
                                            .name(name)
                                            .stock(stock)
                                            .branchId(branch.getId())
                                            .build();

                                    return productRepository.save(product);
                                })
                );
    }
}
