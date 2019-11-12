package io.github.zwieback.familyfinance.business.template.exception

import io.github.zwieback.familyfinance.core.model.type.TemplateType

class UnsupportedTemplateTypeException(id: Int, type: TemplateType) :
    RuntimeException("Type $type of template with id $id is not supported") {

    companion object {
        private const val serialVersionUID = 3058114760405260932L
    }
}
