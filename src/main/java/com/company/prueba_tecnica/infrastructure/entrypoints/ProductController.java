package com.company.prueba_tecnica.infrastructure.entrypoints;

import com.company.prueba_tecnica.application.usecase.CreateProductUseCase;
import com.company.prueba_tecnica.application.usecase.dto.ProductDTO;
import com.company.prueba_tecnica.application.usecase.DeleteProductsByBranchUseCase;
import com.company.prueba_tecnica.application.usecase.dto.UpdateNameDTO;
import com.company.prueba_tecnica.application.usecase.dto.UpdateStockDTO;
import com.company.prueba_tecnica.application.usecase.UpdateProductNameUseCase;
import com.company.prueba_tecnica.application.usecase.UpdateProductStockUseCase;
import com.company.prueba_tecnica.domain.model.Product;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Controlador REST para el recurso Producto.
 *
 * Expone los endpoints HTTP relacionados con la gestión de productos.
 * Delega toda la lógica a los casos de uso correspondientes.
 *
 * Base URL: /products
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    /** Caso de uso para crear un nuevo producto en una sucursal. */
    private final CreateProductUseCase createProductUseCase;

    /** Caso de uso para actualizar el nombre de un producto. */
    private final UpdateProductNameUseCase updateProductNameUseCase;

    /** Caso de uso para actualizar el stock de un producto. */
    private final UpdateProductStockUseCase updateProductStockUseCase;

    /** Caso de uso para eliminar un producto de una sucursal. */
    private final DeleteProductsByBranchUseCase deleteProductsByBranchUseCase;

    /**
     * POST /products
     * Crea un nuevo producto en una sucursal existente.
     *
     * El body debe contener name, stock y branchId.
     * Se valida que la sucursal exista y que el nombre no esté duplicado.
     *
     * @param request cuerpo del request con los datos del producto
     * @return Mono con el producto creado
     */
    @PostMapping
    public Mono<Product> create(@RequestBody Product request) {
        return createProductUseCase.execute(
                request.getName(),
                request.getStock(),
                request.getBranchId()
        );
    }

    /**
     * PUT /products/{id}/name
     * Actualiza el nombre de un producto existente.
     *
     * Se valida unicidad del nuevo nombre dentro de la misma sucursal.
     *
     * @param id      ID del producto a actualizar
     * @param request body con el nuevo nombre
     * @return Mono con el producto actualizado como ProductDTO
     */
    @PutMapping("/{id}/name")
    public Mono<ProductDTO> updateName(
            @PathVariable String id,
            @RequestBody UpdateNameDTO request
    ) {
        return updateProductNameUseCase.execute(id, request);
    }

    /**
     * PUT /products/{id}/stock
     * Actualiza la cantidad de stock de un producto existente.
     *
     * La validación de stock >= 0 ocurre en la entidad de dominio Product.
     *
     * @param id      ID del producto a actualizar
     * @param request body con el nuevo valor de stock
     * @return Mono con el producto actualizado como ProductDTO
     */
    @PutMapping("/{id}/stock")
    public Mono<ProductDTO> updateStock(
            @PathVariable String id,
            @RequestBody UpdateStockDTO request
    ) {
        return updateProductStockUseCase.execute(id, request);
    }

    /**
     * DELETE /products/{productId}/branches/{branchId}
     * Elimina un producto específico de una sucursal.
     *
     * Verifica que el producto pertenezca a la sucursal antes de eliminarlo.
     * Retorna HTTP 204 No Content al completarse correctamente.
     *
     * @param branchId  ID de la sucursal propietaria del producto
     * @param productId ID del producto a eliminar
     * @return Mono con ResponseEntity vacío (204)
     */
    @DeleteMapping("/{productId}/branches/{branchId}")
    public Mono<ResponseEntity<Void>> deleteProductFromBranch(
            @PathVariable String branchId,
            @PathVariable String productId
    ) {
        return deleteProductsByBranchUseCase
                .execute(branchId, productId)
                // thenReturn convierte el Mono<Void> en la respuesta HTTP apropiada
                .thenReturn(ResponseEntity.noContent().build());
    }
}
