package com.company.prueba_tecnica.infrastructure.persistence.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.company.prueba_tecnica.infrastructure.persistence.document.BranchDocument;

/**
 * Repositorio reactivo de MongoDB para la colección "branches".
 *
 * Extiende ReactiveMongoRepository, que provee operaciones CRUD estándar
 * de forma no bloqueante (save, findById, findAll, deleteById, etc.).
 *
 * Los métodos adicionales usan Query Derivation de Spring Data:
 * Spring genera automáticamente la consulta MongoDB a partir del nombre del método.
 *
 * Implementado internamente por Spring Data; no requiere clase concreta.
 */
public interface BranchMongoRepository extends ReactiveMongoRepository<BranchDocument, String> {

    /**
     * Retorna todas las sucursales que pertenecen a una franquicia.
     * Query derivada: db.branches.find({franchiseId: franchiseId})
     *
     * @param franchiseId ID de la franquicia propietaria
     * @return Flux con las sucursales encontradas
     */
    Flux<BranchDocument> findByFranchiseId(String franchiseId);

    /**
     * Verifica si existe una sucursal con ese nombre dentro de una franquicia.
     * Query derivada: db.branches.countDocuments({name: name, franchiseId: franchiseId}) > 0
     *
     * Usado en CreateBranchUseCase para evitar nombres duplicados.
     *
     * @param name        nombre a verificar
     * @param franchiseId ID de la franquicia donde se busca
     * @return Mono con true si ya existe, false si está disponible
     */
    Mono<Boolean> existsByNameAndFranchiseId(String name, String franchiseId);

    /**
     * Verifica si existe otra sucursal con ese nombre, excluyendo un ID.
     * Query derivada: db.branches.countDocuments({name: name, _id: {$ne: id}}) > 0
     *
     * Usado en UpdateBranchNameUseCase sin el contexto de franquicia.
     *
     * @param name nombre a verificar
     * @param id   ID a excluir de la búsqueda
     * @return Mono con true si otro documento ya tiene ese nombre
     */
    Mono<Boolean> existsByNameAndIdNot(String name, String id);

    /**
     * Verifica si existe una sucursal con el ID dado.
     * Query derivada: db.branches.countDocuments({_id: id}) > 0
     *
     * @param id identificador a buscar
     * @return Mono con true si existe, false si no
     */
    Mono<Boolean> existsById(String id);

    /**
     * Verifica si existe otra sucursal con ese nombre dentro de la misma franquicia,
     * excluyendo un ID concreto.
     * Query derivada: db.branches.countDocuments({name, franchiseId, _id: {$ne: id}}) > 0
     *
     * Usado en UpdateBranchNameUseCase para validar el nuevo nombre
     * sin rechazar la sucursal que se está editando.
     *
     * @param name        nombre a verificar
     * @param franchiseId ID de la franquicia donde se busca
     * @param id          ID de la sucursal a excluir
     * @return Mono con true si otro documento ya tiene ese nombre en esa franquicia
     */
    Mono<Boolean> existsByNameAndFranchiseIdAndIdNot(String name, String franchiseId, String id);
}
