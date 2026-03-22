package com.company.prueba_tecnica.application.usecase.dto;

import java.util.List;
/**
 * DTO de respuesta para una Franquicia con su estructura completa.
 *
 * Desacopla la representación de la API del modelo de dominio.
 * Se usa como respuesta en:
 *   - CreateFranchiseUseCase (sin sucursales)
 *   - UpdateFranchiseNameUseCase (con sucursales y productos)
 *   - GetFranchiseStructureUseCase (con sucursales y productos)
 */
public class FranchiseDTO {
    /** Identificador único de la franquicia. */
    private String id;
     /** Nombre de la franquicia. */
    private String name;
    /** Lista de sucursales con sus productos. Puede estar vacía. */
    private List<BranchDTO> branches;

        /**
     * Constructor completo requerido por los use cases.
     *
     * @param id       identificador de la franquicia
     * @param name     nombre de la franquicia
     * @param branches lista de sucursales anidadas
     */
    public FranchiseDTO(String id, String name, List<BranchDTO> branches) {
        this.id = id;
        this.name = name;
        this.branches = branches;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<BranchDTO> getBranches() { return branches; }
}