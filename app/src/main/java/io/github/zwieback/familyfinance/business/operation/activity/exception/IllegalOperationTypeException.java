package io.github.zwieback.familyfinance.business.operation.activity.exception;

import io.github.zwieback.familyfinance.core.model.OperationView;

public class IllegalOperationTypeException extends IllegalArgumentException {

    private static final long serialVersionUID = 4171955715539750284L;

    public static IllegalOperationTypeException unsupportedOperationType(OperationView operation) {
        return new IllegalOperationTypeException("Operation with id " + operation.getId() +
                " has unsupported type: " + operation.getType());
    }

    public static IllegalOperationTypeException notTransferOperation(OperationView operation) {
        return new IllegalOperationTypeException("Transfer operation with id " + operation.getId() +
                " must be transfer operation, not " + operation.getType());
    }

    private IllegalOperationTypeException(String message) {
        super(message);
    }
}
