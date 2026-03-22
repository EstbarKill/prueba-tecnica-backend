package com.company.prueba_tecnica.domain.repository;

import com.company.prueba_tecnica.domain.model.Branch;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrato de persistencia para la entidad Branch (Sucursal).
 *
 * Define las operaciones que la capa de infraestructura debe implementar.
 * El dominio no conoce MongoDB ni ninguna tecnología de persistencia concreta.
 *
 * Implementado por: BranchRepositoryAdapter
 */
public interface BranchRepository {

    /**
     * Busca una sucursal por su identificador único.
     *
     * @param id identificador de la sucursal
     * @return Mono con la sucursal encontrada, o Mono vacío si no existe
     */
    Mono<Branch> findById(String id);

    /**
     * Persiste una sucursal nueva o actualiza una existente.
     *
     * @param branch entidad a guardar
     * @return Mono con la sucursal guardada
     */
    Mono<Branch> save(Branch branch);

    /**
     * Retorna todas las sucursales pertenecientes a una franquicia.
     *
     * @param franchiseId ID de la franquicia propietaria
     * @return Flux con las sucursales encontradas (puede estar vacío)
     */
    Flux<Branch> findByFranchiseId(String franchiseId);

    /**
     * Retorna todas las sucursales del sistema.
     *
     * @return Flux con todas las sucursales
     */
    Flux<Branch> findAll();

    /**
     * Elimina una sucursal junto con todos sus productos asociados.
     * Operación en cascada que garantiza consistencia de datos.
     *
     * @param branchId ID de la sucursal a eliminar
     * @return Mono vacío al completarse
     */
    Mono<Void> deleteBranchWithProducts(String branchId);

    /**
     * Verifica si existe una sucursal con el ID dado.
     *
     * @param id identificador a verificar
     * @return Mono con true si existe, false si no
     */
    Mono<Boolean> existsById(String id);

    /**
     * Verifica si ya existe una sucursal con ese nombre dentro de una franquicia.
     * Usado en CreateBranchUseCase para evitar nombres duplicados.
     *
     * @param name        nombre de la sucursal a verificar
     * @param franchiseId ID de la franquicia donde se busca
     * @return Mono con true si ya existe, false si está disponible
     */
    Mono<Boolean> existsByNameAndFranchiseId(String name, String franchiseId);

    /**
     * Verifica si otra sucursal tiene el mismo nombre dentro de la franquicia,
     * excluyendo un ID. Usado en UpdateBranchNameUseCase para validar
     * el nuevo nombre sin rechazar la sucursal que se está editando.
     *
     * @param name        nombre a verificar
     * @param franchiseId ID de la franquicia donde se busca
     * @param id          ID de la sucursal que se excluye de la búsqueda
     * @return Mono con true si otro documento ya tiene ese nombre
     */
    Mono<Boolean> existsByNameAndFranchiseIdAndIdNot(String name, String franchiseId, String id);
}
