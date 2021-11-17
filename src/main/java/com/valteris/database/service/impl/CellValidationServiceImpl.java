package com.valteris.database.service.impl;

import com.valteris.database.domain.Cell;
import com.valteris.database.domain.Table;
import com.valteris.database.dto.TableErrorValidation;
import com.valteris.database.service.CellValidationService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class CellValidationServiceImpl implements CellValidationService {

    private static final String CHAR_MATCHER = ".";

    @Override
    public TableErrorValidation validateIfTableHasErrors(Table requestedTable) {
        List<Cell> cells = requestedTable.getAllCells();
        for (Cell cell : cells) {
            cell.setHasError(validateIfCellHasError(cell));
        }

        return TableErrorValidation.builder()
                .table(requestedTable)
                .hasError(cells.stream().anyMatch(Cell::isHasError))
                .build();
    }

    private boolean validateIfCellHasError(Cell cell) {
        String value = cell.getValue();

        switch (cell.getType()) {
            case STRING:
                return false;
            case CHAR:
                return !value.matches(CHAR_MATCHER);
            case INTEGER:
                try {
                    Integer.parseInt(value);
                    return false;
                } catch (NumberFormatException e) {
                    System.out.println("Input String cannot be parsed to Integer.");
                }
                return true;
            case REAL:
                try {
                    Double.parseDouble(value);
                    return false;
                } catch (NumberFormatException e) {
                    System.out.println("Input String cannot be parsed to Double.");
                }
                return true;
            case TIME:
                try {
                    LocalTime.parse(value);
                    return false;
                } catch (DateTimeParseException e) {
                    System.out.println("Input String cannot be parsed to Time.");
                }
                return true;
            case TIME_INTERVAL:
                try {
                    String firstTime = value.split("-")[0].trim();
                    String secondTime = value.split("-")[1].trim();
                    LocalTime time1 = LocalTime.parse(firstTime);
                    LocalTime time2 = LocalTime.parse(secondTime);
                    return time1.isAfter(time2);
                } catch (DateTimeParseException e) {
                    System.out.println("Input String cannot be parsed to Time.");
                }
                return true;
            default:
        }
        return true;
    }
}
