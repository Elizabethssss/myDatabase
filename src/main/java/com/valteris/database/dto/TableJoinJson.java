package com.valteris.database.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TableJoinJson {

    @SerializedName("tableName")
    @Expose
    public String tableName;
    @SerializedName("column")
    @Expose
    public ColumnJson column;
}
