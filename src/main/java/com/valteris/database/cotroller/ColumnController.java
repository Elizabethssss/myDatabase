package com.valteris.database.cotroller;

import com.valteris.database.convector.TableConvertor;
import com.valteris.database.domain.Table;
import com.valteris.database.dto.CreateTableJson;
import com.valteris.database.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
public class ColumnController {

    private final DatabaseService databaseService;
    private final TableConvertor tableConvertor;

    @PostMapping(value = "/column", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CreateTableJson addNewColumn(@RequestBody CreateTableJson createTableJson) {
        Table table = tableConvertor.convertCreateTableToDomain(createTableJson);
        databaseService.addNewColumn(table);

        return createTableJson;
    }

    @PostMapping(value = "/column/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CreateTableJson deleteColumn(@RequestBody CreateTableJson createTableJson) {
        final Table table = tableConvertor.convertCreateTableToDomain(createTableJson);
        databaseService.deleteColumn(table);

        return createTableJson;
    }
}
