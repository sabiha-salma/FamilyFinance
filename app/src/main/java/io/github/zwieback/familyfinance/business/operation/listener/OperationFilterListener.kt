package io.github.zwieback.familyfinance.business.operation.listener

import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter
import io.github.zwieback.familyfinance.core.listener.EntityFilterListener

interface OperationFilterListener<F : OperationFilter> : EntityFilterListener<F>
