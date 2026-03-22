package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.application.usecase.dto.*;
import com.company.prueba_tecnica.domain.exception.NotFoundException;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;

/**
 * Caso de uso: Obtener el producto con mayor stock por sucursal para una franquicia.
 *
 * Para cada sucursal de la franquicia, determina cuál de sus productos
 * tiene la mayor cantidad de stock. Retorna el resultado agrupado por sucursal.
 *
 * Este es el caso de uso principal requerido por la prueba técnica (Criterio 7).
 */
@Service
@RequiredArgsConstructor
public class GetTopProductByBranchInFranchiseUseCase {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    /**
     * Ejecuta el caso de uso.
     *
     * Flujo reactivo:
     *   1. Busca la franquicia por ID → NotFoundException si no existe
     *   2. Para cada sucursal de la franquicia (en orden, con flatMapSequential):
     *      a. Obtiene todos los productos de esa sucursal
     *      b. Determina el producto con mayor stock usando Java Streams
     *      c. Construye un BranchWithProductsAndTopDTO con el resultado
     *   3. Agrupa todos los resultados en un FranchiseTopProductsDTO
     *
     * Se usa flatMapSequential (en lugar de flatMap) para preservar
     * el orden de las sucursales en la respuesta.
     *
     * @param franchiseId ID de la franquicia a consultar
     * @return Mono con la franquicia y el top product de cada sucursal
     */
    public Mono<FranchiseTopProductsDTO> execute(String franchiseId) {

        return franchiseRepository.findById(franchiseId)
                // Si la franquicia no existe, propagar el error apropiado
                .switchIfEmpty(Mono.error(new NotFoundException("Franchise not found")))
                .flatMap(franchise ->

                        // Iterar sobre cada sucursal manteniendo el orden de llegada
                        branchRepository.findByFranchiseId(franchise.getId())
                                .flatMapSequential(branch ->

                                        // Obtener todos los productos de la sucursal actual
                                        productRepository.findByBranchId(branch.getId())
                                                .collectList()
                                                .map(products -> {

                                                    // Encontrar el producto con mayor stock usando Stream.max()
                                                    // Si la sucursal no tiene productos, topProduct será null
                                                    TopProductDTO topProduct = products.stream()
                                                            .max(Comparator.comparingInt(p -> p.getStock()))
                                                            .map(p -> new TopProductDTO(
                                                                    p.getId(),
                                                                    p.getName(),
                                                                    p.getStock()
                                                            ))
                                                            .orElse(null);

                                                    // Mapear la lista completa de productos a DTOs
                                                    var productDTOs = products.stream()
                                                            .map(p -> new ProductDTO(
                                                                    p.getId(),
                                                                    p.getName(),
                                                                    p.getStock()
                                                            ))
                                                            .toList();

                                                    // Construir el DTO de la sucursal con su top product
                                                    return new BranchWithProductsAndTopDTO(
                                                            branch.getId(),
                                                            branch.getName(),
                                                            topProduct,
                                                            productDTOs
                                                    );
                                                })
                                )
                                .collectList()
                                // Construir la respuesta final con la franquicia y todas sus sucursales
                                .map(branches -> new FranchiseTopProductsDTO(
                                        franchise.getId(),
                                        franchise.getName(),
                                        branches
                                ))
                );
    }
}
