package com.company.prueba_tecnica.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra una sucursal por su ID.
 *
 * Especialización de BusinessException que incluye el branchId
 * en el mensaje para facilitar el diagnóstico del error.
 *
 * Código semántico: "BRANCH_NOT_FOUND"
 */
public class BranchNotFoundException extends BusinessException {

    /**
     * Construye la excepción con el ID de la sucursal no encontrada.
     *
     * @param branchId identificador de la sucursal buscada
     */
    public BranchNotFoundException(String branchId) {
        super("Branch not found with id: " + branchId, "BRANCH_NOT_FOUND");
    }
}
