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

/**
 * Caso de uso: Obtener la estructura completa de todas las franquicias.
 *
 * Retorna un árbol jerárquico: Franquicia → Sucursales → Productos.
 * Útil para obtener una vista general de todo el sistema en una sola llamada.
 */
@Service
@RequiredArgsConstructor
public class GetFranchiseStructureUseCase {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    /**
     * Ejecuta el caso de uso.
     *
     * Flujo reactivo con tres niveles anidados:
     *   1. findAll() sobre franquicias → Flux<Franchise>
     *   2. Para cada franquicia: findByFranchiseId() → Flux<Branch>
     *   3. Para cada sucursal: findByBranchId() → Flux<Product>
     *   4. Cada nivel se mapea a su DTO y se agrega con collectList()
     *
     * @return Flux con todas las franquicias estructuradas en DTOs
     */
    public Flux<FranchiseDTO> execute() {

        return franchiseRepository.findAll()
        .flatMap(franchise ->
                // Para cada franquicia, obtener sus sucursales
                branchRepository.findByFranchiseId(franchise.getId())
                        .flatMap(branch ->
                                // Para cada sucursal, obtener sus productos
                                productRepository.findByBranchId(branch.getId())
                                        .map(product -> new ProductDTO(
                                                product.getId(),
                                                product.getName(),
                                                product.getStock()
                                        ))
                                        .collectList()
                                        // Construir el DTO de sucursal con sus productos
                                        .map(products -> new BranchDTO(
                                                branch.getId(),
                                                branch.getName(),
                                                products
                                        ))
                        )
                        .collectList()
                        // Construir el DTO de franquicia con sus sucursales
                        .map(branches -> new FranchiseDTO(
                                franchise.getId(),
                                franchise.getName(),
                                branches
                        ))
        );
    }

    /**
     * Método auxiliar para mapear una franquicia a su DTO con sucursales y productos.
     * Actualmente no se utiliza en el flujo principal pero queda disponible
     * como helper reutilizable.
     *
     * @param franchise entidad de dominio a mapear
     * @return Mono con el DTO completo de la franquicia
     */
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

    /**
     * Método auxiliar para mapear una sucursal a su DTO con productos ordenados
     * de mayor a menor stock.
     *
     * @param branch entidad de dominio a mapear
     * @return Mono con el DTO de la sucursal y sus productos ordenados
     */
    private Mono<BranchDTO> mapBranch(Branch branch) {

        return productRepository.findByBranchId(branch.getId())
                // Ordenar productos de mayor a menor stock
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
