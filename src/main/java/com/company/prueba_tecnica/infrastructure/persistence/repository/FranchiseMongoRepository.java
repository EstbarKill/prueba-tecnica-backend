package com.company.prueba_tecnica.infrastructure.persistence.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.company.prueba_tecnica.infrastructure.persistence.document.FranchiseDocument;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo de MongoDB para la colección "franchises".
 *
 * Extiende ReactiveMongoRepository, que provee operaciones CRUD estándar
 * (save, findById, findAll, deleteById, etc.) de forma no bloqueante.
 *
 * Los métodos adicionales usan Query Derivation de Spring Data:
 * Spring genera automáticamente la consulta MongoDB a partir del nombre del método.
 *
 * Implementado internamente por Spring Data; no requiere clase concreta.
 */
public interface FranchiseMongoRepository extends ReactiveMongoRepository<FranchiseDocument, String> {

    /**
     * Verifica si existe una franquicia con el ID dado.
     * Query derivada: db.franchises.countDocuments({_id: id}) > 0
     *
     * @param id identificador a buscar
     * @return Mono con true si existe, false si no
     */
    Mono<Boolean> existsById(String id);

    /**
     * Verifica si existe una franquicia con el nombre dado.
     * Query derivada: db.franchises.countDocuments({name: name}) > 0
     *
     * @param name nombre a verificar
     * @return Mono con true si existe, false si no
     */
    Mono<Boolean> existsByName(String name);

    /**
     * Verifica si existe otra franquicia con ese nombre, excluyendo un ID.
     * Query derivada: db.franchises.countDocuments({name: name, _id: {$ne: id}}) > 0
     *
     * Usado para validar el nuevo nombre al actualizar sin rechazar
     * la franquicia que se está editando.
     *
     * @param name nombre a verificar
     * @param id   ID a excluir de la búsqueda
     * @return Mono con true si otro documento ya tiene ese nombre
     */
    Mono<Boolean> existsByNameAndIdNot(String name, String id);
}
