package com.valteris.database.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.valteris.database.domain.Type;
import lombok.Builder;
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
}
