package io.github.zwieback.familyfinance.business.operation.service.grouper;

import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.TemporalUnit;

abstract class BaseOperationGrouper implements OperationGrouper {

    private static final int INCLUDE_END_DATE = 1;

    abstract TemporalUnit getTemporalUnit();

    final long calculatePeriodBetween(LocalDate startDate, LocalDate endDate) {
        return getTemporalUnit().between(startDate, endDate) + INCLUDE_END_DATE;
    }
}
