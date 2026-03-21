package com.company.prueba_tecnica.domain.exception;

public class BranchNotFoundException extends BusinessException {

    public BranchNotFoundException(String branchId) {
        super("Branch not found with id: " + branchId, "BRANCH_NOT_FOUND");
    }
}
