package com.company.prueba_tecnica.infrastructure.persistence.adapter;

import com.company.prueba_tecnica.domain.model.Branch;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.infrastructure.persistence.document.BranchDocument;
import com.company.prueba_tecnica.infrastructure.persistence.repository.BranchMongoRepository;
import com.company.prueba_tecnica.infrastructure.persistence.repository.ProductMongoRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Repository;

/**
 * Adaptador que implementa el contrato BranchRepository del dominio
 * usando BranchMongoRepository de Spring Data.
 *
 * Responsabilidades:
 *   1. Traducir llamadas del dominio a operaciones de MongoDB
 *   2. Mapear BranchDocument (infraestructura) ↔ Branch (dominio)
 *   3. Gestionar la eliminación en cascada (sucursal + sus productos)
 *
 * Inyecta también ProductMongoRepository para poder implementar
 * la eliminación en cascada sin salir de la capa de infraestructura.
 */
@Repository
@RequiredArgsConstructor
public class BranchRepositoryAdapter implements BranchRepository {

    /** Repositorio de Spring Data para la colección "branches". */
    private final BranchMongoRepository repository;

    /**
     * Repositorio de Spring Data para la colección "products".
     * Necesario para eliminar en cascada al borrar una sucursal.
     */
    private final ProductMongoRepository productRepository;

    /**
     * Busca una sucursal por ID y la convierte al modelo de dominio.
     *
     * @param id identificador de la sucursal
     * @return Mono con la sucursal, o Mono vacío si no existe
     */
    @Override
    public Mono<Branch> findById(String id) {
        return repository.findById(id)
                .map(doc -> new Branch(
                        doc.getId(),
                        doc.getName(),
                        doc.getFranchiseId()
                ));
    }

    /**
     * Persiste una sucursal convirtiéndola primero a Document.
     *
     * Flujo: Branch (dominio) → BranchDocument → MongoDB → Branch (dominio)
     *
     * @param branch entidad de dominio a guardar
     * @return Mono con la sucursal guardada
     */
    @Override
    public Mono<Branch> save(Branch branch) {
        // Convertir entidad de dominio a documento de MongoDB
        BranchDocument doc = BranchDocument.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .build();

        // Guardar en MongoDB y convertir el resultado de vuelta a dominio
        return repository.save(doc)
                .map(saved -> new Branch(
                        saved.getId(),
                        saved.getName(),
                        saved.getFranchiseId()
                ));
    }

    /**
     * Retorna todas las sucursales de una franquicia convertidas al modelo de dominio.
     *
     * @param franchiseId ID de la franquicia propietaria
     * @return Flux con las sucursales encontradas
     */
    @Override
    public Flux<Branch> findByFranchiseId(String franchiseId) {
        return repository.findByFranchiseId(franchiseId)
                .map(doc -> new Branch(
                        doc.getId(),
                        doc.getName(),
                        doc.getFranchiseId()
                ));
    }

    /**
     * Retorna todas las sucursales convertidas al modelo de dominio.
     *
     * @return Flux con todas las sucursales
     */
    @Override
    public Flux<Branch> findAll() {
        return repository.findAll()
                .map(doc -> new Branch(
                        doc.getId(),
                        doc.getName(),
                        doc.getFranchiseId()
                ));
    }

    /**
     * Elimina una sucursal junto con TODOS sus productos asociados.
     *
     * Flujo reactivo en cascada:
     *   1. Verifica que la sucursal exista → RuntimeException si no existe
     *   2. Elimina todos los productos de la sucursal con deleteByBranchId()
     *      (operación masiva en una sola query)
     *   3. Elimina la sucursal con deleteById()
     *
     * El uso de .then() encadena las operaciones garantizando que
     * cada paso se complete antes de iniciar el siguiente.
     *
     * @param branchId ID de la sucursal a eliminar
     * @return Mono vacío al completarse toda la operación
     */
    @Override
    public Mono<Void> deleteBranchWithProducts(String branchId) {
        return repository.findById(branchId)
                // Verificar que la sucursal existe antes de eliminar
                .switchIfEmpty(Mono.error(new RuntimeException("Branch not found")))
                // Primero eliminar todos los productos de la sucursal
                .then(productRepository.deleteByBranchId(branchId))
                // Luego eliminar la sucursal misma
                .then(repository.deleteById(branchId));
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Boolean> existsById(String id) {
        return repository.existsById(id);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Boolean> existsByNameAndFranchiseId(String name, String franchiseId) {
        return repository.existsByNameAndFranchiseId(name, franchiseId);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Boolean> existsByNameAndFranchiseIdAndIdNot(String name, String franchiseId, String id) {
        return repository.existsByNameAndFranchiseIdAndIdNot(name, franchiseId, id);
    }
}
