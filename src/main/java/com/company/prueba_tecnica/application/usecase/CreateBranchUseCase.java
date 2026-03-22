package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.exception.NotFoundException;
import com.company.prueba_tecnica.domain.model.Branch;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

/**
 * Caso de uso: Crear una nueva sucursal dentro de una franquicia.
 *
 * Garantiza que no existan dos sucursales con el mismo nombre
 * dentro de la misma franquicia antes de persistir.
 */
@Service
@RequiredArgsConstructor
public class CreateBranchUseCase {

    /** Repositorio del dominio para operar sobre sucursales. */
    private final BranchRepository branchRepository;

    /**
     * Ejecuta el caso de uso: valida y crea la sucursal.
     *
     * Flujo reactivo:
     *   1. Verifica que no exista otra sucursal con el mismo nombre
     *      en la misma franquicia → error si ya existe
     *   2. Persiste la sucursal con save()
     *
     * @param name        nombre de la nueva sucursal
     * @param franchiseId ID de la franquicia a la que pertenecerá
     * @return Mono con la sucursal creada y persistida
     */
    public Mono<Branch> execute(String name, String franchiseId) {

        // Construye la entidad antes de las validaciones
        Branch branch = Branch.builder()
                .name(name)
                .franchiseId(franchiseId)
                .build();

        // Verifica unicidad del nombre dentro de la franquicia
        return branchRepository.existsByNameAndFranchiseId(name, franchiseId)
                .flatMap(exists -> {
                    if (exists) {
                        // Ya existe una sucursal con ese nombre en esta franquicia
                        return Mono.error(new NotFoundException(
                                "Branch name already exists in this franchise"
                        ));
                    }

                    // Validación superada: persistir la nueva sucursal
                    return branchRepository.save(branch);
                });
    }
}
