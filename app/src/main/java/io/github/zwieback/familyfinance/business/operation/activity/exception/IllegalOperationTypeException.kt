package io.github.zwieback.familyfinance.business.operation.activity.exception

import io.github.zwieback.familyfinance.core.model.OperationView

class IllegalOperationTypeException private constructor(message: String) :
    IllegalArgumentException(message) {

    companion object {
        private const val serialVersionUID = 4171955715539750284L

        fun notTransferOperation(operation: OperationView): IllegalOperationTypeException {
            return IllegalOperationTypeException(
                "Transfer operation with id ${operation.id} " +
                        "must be transfer operation, not ${operation.type}"
            )
        }
    }
}
