package com.valteris.database.service;

import com.valteris.database.domain.Table;
import com.valteris.database.dto.TableErrorValidation;

public interface CellValidationService {

    TableErrorValidation validateIfTableHasErrors(Table requestedTable);
}
