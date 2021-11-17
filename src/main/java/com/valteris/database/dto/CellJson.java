package com.valteris.database.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.valteris.database.domain.Type;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CellJson {

    @SerializedName("value")
    @Expose
    private String value = "";
    @SerializedName("type")
    @Expose
    private Type type;
    @SerializedName("columnName")
    @Expose
    private String columnName;
    @SerializedName("lineId")
    @Expose
    private Long lineId;
    @SerializedName("hasError")
    @Expose
    private boolean hasError;
}
