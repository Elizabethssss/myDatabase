package com.valteris.database.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InnerJoinJson {

    @SerializedName("dbName")
    @Expose
    private String dbName;

    @SerializedName("table1")
    @Expose
    private TableJoinJson table1;

    @SerializedName("table2")
    @Expose
    private TableJoinJson table2;
}
