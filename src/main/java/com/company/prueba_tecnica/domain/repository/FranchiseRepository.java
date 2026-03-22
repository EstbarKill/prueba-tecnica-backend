package com.company.prueba_tecnica.domain.repository;

import com.company.prueba_tecnica.domain.model.Franchise;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrato de persistencia para la entidad Franchise.
 *
 * Define las operaciones que la capa de infraestructura debe implementar.
 * El dominio no conoce MongoDB ni ninguna tecnología de persistencia concreta;
 * solo trabaja con este contrato.
 *
 * Todos los métodos retornan tipos reactivos (Mono/Flux) para integrarse
 * de forma no bloqueante con Spring WebFlux.
 *
 * Implementado por: FranchiseRepositoryAdapter
 */
public interface FranchiseRepository {

    /**
     * Persiste una franquicia nueva o actualiza una existente.
     *
     * @param franchise entidad a guardar
     * @return Mono con la franquicia guardada (con ID asignado si era nueva)
     */
    Mono<Franchise> save(Franchise franchise);

    /**
     * Busca una franquicia por su identificador único.
     *
     * @param id identificador de la franquicia
     * @return Mono con la franquicia encontrada, o Mono vacío si no existe
     */
    Mono<Franchise> findById(String id);

    /**
     * Retorna todas las franquicias registradas en el sistema.
     *
     * @return Flux con todas las franquicias (puede estar vacío)
     */
    Flux<Franchise> findAll();

    /**
     * Verifica si existe una franquicia con el ID dado.
     * Usado en CreateFranchiseUseCase para evitar duplicados de ID.
     *
     * @param id identificador a verificar
     * @return Mono con true si existe, false si no
     */
    Mono<Boolean> existsById(String id);

    /**
     * Verifica si existe una franquicia con el nombre dado.
     * Usado en CreateFranchiseUseCase para garantizar unicidad de nombre.
     *
     * @param name nombre a verificar
     * @return Mono con true si ya existe, false si está disponible
     */
    Mono<Boolean> existsByName(String name);

    /**
     * Verifica si existe otra franquicia con el mismo nombre, excluyendo un ID.
     * Usado en UpdateFranchiseNameUseCase para validar el nuevo nombre
     * sin rechazar la franquicia que se está editando.
     *
     * @param name nombre a verificar
     * @param id   ID de la franquicia que se excluye de la búsqueda
     * @return Mono con true si otro documento ya tiene ese nombre
     */
    Mono<Boolean> existsByNameAndIdNot(String name, String id);
}
