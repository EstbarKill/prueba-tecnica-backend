package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.exception.DuplicateResourceException;
import com.company.prueba_tecnica.domain.model.Franchise;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Caso de uso: Crear una nueva franquicia.
 *
 * Orquesta las validaciones de negocio y la persistencia de la entidad.
 * Sigue el principio de responsabilidad única: solo crea franquicias.
 *
 * @Service     → registra esta clase como bean de Spring
 * @RequiredArgsConstructor → Lombok genera el constructor con los campos final,
 *               permitiendo la inyección de dependencias sin @Autowired
 */
@Service
@RequiredArgsConstructor
public class CreateFranchiseUseCase {

    /** Repositorio del dominio para operar sobre franquicias. */
    private final FranchiseRepository repository;

    /**
     * Ejecuta el caso de uso: valida y crea la franquicia.
     *
     * Flujo reactivo:
     *   1. Verifica que el ID no esté en uso → DuplicateResourceException si existe
     *   2. Verifica que el nombre no esté en uso → DuplicateResourceException si existe
     *   3. Persiste la franquicia con save()
     *
     * Los pasos 1 y 2 se encadenan con flatMap para mantener el flujo no bloqueante.
     *
     * @param id   identificador deseado para la nueva franquicia
     * @param name nombre comercial de la nueva franquicia
     * @return Mono con la franquicia creada y persistida
     */
    public Mono<Franchise> execute(String id, String name) {

        // Construye la entidad con el patrón builder de Lombok
        Franchise franchise = Franchise.builder()
                .id(id)
                .name(name)
                .build();

        // Paso 1: verificar que el ID no exista
        return repository.existsById(franchise.getId())
        .flatMap(idExists -> {
            if (idExists) {
                // El ID ya está registrado → error de negocio
                return Mono.error(new DuplicateResourceException(
                        "Franchise id already exists"
                ));
            }

            // Paso 2: verificar que el nombre no exista
            return repository.existsByName(franchise.getName());
        })
        .flatMap(nameExists -> {
            if (nameExists) {
                // El nombre ya está registrado → error de negocio
                return Mono.error(new DuplicateResourceException(
                        "Franchise name already exists"
                ));
            }

            // Paso 3: todas las validaciones pasaron, persistir
            return repository.save(franchise);
        });
    }
}
