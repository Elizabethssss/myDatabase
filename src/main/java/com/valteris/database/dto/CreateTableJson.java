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
public class CreateTableJson {

    @SerializedName("dbName")
    @Expose
    public String dbName;
    @SerializedName("tableName")
    @Expose
    public String tableName;
    @SerializedName("columns")
    @Expose
    public List<ColumnJson> columns = new ArrayList<>();
}
