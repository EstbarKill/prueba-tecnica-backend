package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.exception.DuplicateResourceException;
import com.company.prueba_tecnica.domain.model.Franchise;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Tests unitarios para CreateFranchiseUseCase.
 *
 * Usa Mockito para aislar el caso de uso de la base de datos real.
 * Usa StepVerifier (reactor-test) para verificar el comportamiento
 * de los Mono retornados por el caso de uso.
 *
 * Patrón de cada test:
 *   1. Configurar el comportamiento de los mocks (Mockito.when)
 *   2. Ejecutar el caso de uso
 *   3. Verificar el resultado con StepVerifier
 */
class CreateFranchiseUseCaseTest {

    /** Mock del repositorio: no toca MongoDB real. */
    private FranchiseRepository repository;

    /** Instancia del caso de uso bajo prueba. */
    private CreateFranchiseUseCase useCase;

    /**
     * Se ejecuta antes de cada test.
     * Crea mocks frescos e inyecta en el caso de uso manualmente
     * (sin contexto de Spring).
     */
    @BeforeEach
    void setUp() {
        repository = Mockito.mock(FranchiseRepository.class);
        useCase = new CreateFranchiseUseCase(repository);
    }

    /**
     * Caso feliz: ID y nombre únicos → franquicia creada correctamente.
     */
    @Test
    void shouldCreateFranchiseSuccessfully() {

        Franchise franchise = Franchise.builder()
                .id("fr-1")
                .name("Franquicia A")
                .build();

        // Configurar mocks: ID libre, nombre libre, save exitoso
        Mockito.when(repository.existsById("fr-1"))
                .thenReturn(Mono.just(false));

        Mockito.when(repository.existsByName("Franquicia A"))
                .thenReturn(Mono.just(false));

        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(Mono.just(franchise));

        // Verificar que el Mono emite la franquicia con los datos correctos
        StepVerifier.create(useCase.execute("fr-1", "Franquicia A"))
                .expectNextMatches(f ->
                        f.getId().equals("fr-1") &&
                        f.getName().equals("Franquicia A")
                )
                .verifyComplete();
    }

    /**
     * El ID ya está en uso → debe emitir DuplicateResourceException.
     */
    @Test
    void shouldFailWhenIdAlreadyExists() {

        // Configurar mock: el ID ya existe
        Mockito.when(repository.existsById("fr-1"))
                .thenReturn(Mono.just(true));

        // Verificar que el Mono emite el error esperado
        StepVerifier.create(useCase.execute("fr-1", "Franquicia A"))
                .expectErrorMatches(error ->
                        error instanceof DuplicateResourceException &&
                        error.getMessage().equals("Franchise id already exists")
                )
                .verify();
    }

    /**
     * El nombre ya está en uso → debe emitir DuplicateResourceException.
     */
    @Test
    void shouldFailWhenNameAlreadyExists() {

        // Configurar mocks: ID libre, pero nombre ya existe
        Mockito.when(repository.existsById("fr-1"))
                .thenReturn(Mono.just(false));

        Mockito.when(repository.existsByName("Franquicia A"))
                .thenReturn(Mono.just(true));

        StepVerifier.create(useCase.execute("fr-1", "Franquicia A"))
                .expectErrorMatches(error ->
                        error instanceof DuplicateResourceException &&
                        error.getMessage().equals("Franchise name already exists")
                )
                .verify();
    }
}
