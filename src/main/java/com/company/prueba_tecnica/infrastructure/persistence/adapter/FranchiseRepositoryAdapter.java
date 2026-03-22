package com.company.prueba_tecnica.infrastructure.persistence.adapter;

import com.company.prueba_tecnica.domain.model.Franchise;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;
import com.company.prueba_tecnica.infrastructure.persistence.document.FranchiseDocument;
import com.company.prueba_tecnica.infrastructure.persistence.repository.FranchiseMongoRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Repository;

/**
 * Adaptador que implementa el contrato FranchiseRepository del dominio
 * usando FranchiseMongoRepository de Spring Data.
 *
 * Responsabilidades:
 *   1. Traducir llamadas del dominio a operaciones de MongoDB
 *   2. Mapear FranchiseDocument (infraestructura) ↔ Franchise (dominio)
 *
 * Este patrón Adapter (también llamado Anti-Corruption Layer) garantiza
 * que el dominio no se "contamine" con detalles de MongoDB como @Document,
 * @Id o tipos específicos del driver reactivo.
 *
 * @Repository registra esta clase como bean de Spring para que pueda
 * ser inyectada donde se declare FranchiseRepository.
 */
@Repository
@RequiredArgsConstructor
public class FranchiseRepositoryAdapter implements FranchiseRepository {

    /** Repositorio de Spring Data que interactúa directamente con MongoDB. */
    private final FranchiseMongoRepository repository;

    /**
     * Persiste una franquicia convirtiéndola primero a Document.
     *
     * Flujo: Franchise (dominio) → FranchiseDocument → MongoDB → Franchise (dominio)
     *
     * @param franchise entidad de dominio a guardar
     * @return Mono con la franquicia guardada (con ID asignado si era nueva)
     */
    @Override
    public Mono<Franchise> save(Franchise franchise) {
        // Convertir entidad de dominio a documento de MongoDB
        FranchiseDocument doc = FranchiseDocument.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();

        // Guardar en MongoDB y convertir el resultado de vuelta a dominio
        return repository.save(doc)
                .map(saved -> new Franchise(
                        saved.getId(),
                        saved.getName()
                ));
    }

    /**
     * Busca una franquicia por ID y la convierte al modelo de dominio.
     *
     * @param id identificador de la franquicia
     * @return Mono con la franquicia, o Mono vacío si no existe
     */
    @Override
    public Mono<Franchise> findById(String id) {
        return repository.findById(id)
                .map(doc -> new Franchise(
                        doc.getId(),
                        doc.getName()
                ));
    }

    /**
     * Retorna todas las franquicias convertidas al modelo de dominio.
     *
     * @return Flux con todas las franquicias
     */
    @Override
    public Flux<Franchise> findAll() {
        return repository.findAll()
                .map(doc -> new Franchise(
                        doc.getId(),
                        doc.getName()
                ));
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Boolean> existsById(String id) {
        return repository.existsById(id);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Boolean> existsByName(String name) {
        return repository.existsByName(name);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Boolean> existsByNameAndIdNot(String name, String id) {
        return repository.existsByNameAndIdNot(name, id);
    }
}
