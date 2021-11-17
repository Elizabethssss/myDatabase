package com.valteris.database.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
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
    private List<LineJson> lines = new ArrayList<>();
}
