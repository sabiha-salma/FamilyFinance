package io.github.zwieback.familyfinance.core.activity.exception

class ReadOnlyException :
    IllegalStateException("The method can't be executed because the readOnly flag is set") {

    companion object {
        const val serialVersionUID = -8708939504644359255L
    }
}
