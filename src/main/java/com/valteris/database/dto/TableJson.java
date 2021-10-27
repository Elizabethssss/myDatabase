package com.valteris.database.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.valteris.database.domain.Line;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TableJson {

    @SerializedName("dbName")
    @Expose
    private String dbName;
    @SerializedName("tableName")
    @Expose
    private String tableName;
    @SerializedName("columns")
    @Expose
    private List<ColumnJson> columns = new ArrayList<>();
    @SerializedName("lines")
    @Expose
    private List<Line> lines = new ArrayList<>();
}
