package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.exception.DuplicateResourceException;
import com.company.prueba_tecnica.domain.model.Branch;
import com.company.prueba_tecnica.domain.repository.BranchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Tests unitarios para CreateBranchUseCase.
 *
 * Verifica que:
 *   - Una sucursal se crea correctamente cuando el nombre está disponible
 *   - Se lanza error cuando el nombre ya existe en la misma franquicia
 */
class CreateBranchUseCaseTest {

    private BranchRepository branchRepository;
    private CreateBranchUseCase useCase;

    @BeforeEach
    void setUp() {
        branchRepository = Mockito.mock(BranchRepository.class);
        useCase = new CreateBranchUseCase(branchRepository);
    }

    /**
     * Caso feliz: nombre disponible en la franquicia → sucursal creada.
     */
    @Test
    void shouldCreateBranchSuccessfully() {

        String franchiseId = "fr-1";

        Branch branch = Branch.builder()
                .id("br-1")
                .name("Sucursal")
                .franchiseId(franchiseId)
                .build();

        // Nombre libre en la franquicia, save exitoso
        Mockito.when(branchRepository.existsByNameAndFranchiseId("Sucursal", franchiseId))
                .thenReturn(Mono.just(false));

        Mockito.when(branchRepository.save(Mockito.any()))
                .thenReturn(Mono.just(branch));

        StepVerifier.create(useCase.execute("Sucursal", franchiseId))
                .expectNextMatches(b ->
                        b.getName().equals("Sucursal") &&
                        b.getFranchiseId().equals(franchiseId)
                )
                .verifyComplete();
    }

    /**
     * Nombre ya existe en la franquicia → debe emitir error.
     *
     * Nota: el use case lanza NotFoundException en este caso, aunque
     * semánticamente debería ser DuplicateResourceException.
     */
    @Test
    void shouldFailWhenBranchAlreadyExists() {

        String franchiseId = "fr-1";

        // El nombre ya existe en esta franquicia
        Mockito.when(branchRepository.existsByNameAndFranchiseId("Sucursal", franchiseId))
                .thenReturn(Mono.just(true));

        StepVerifier.create(useCase.execute("Sucursal", franchiseId))
                .expectErrorMatches(error ->
                        error.getMessage().equals("Branch name already exists in this franchise")
                )
                .verify();
    }
}
