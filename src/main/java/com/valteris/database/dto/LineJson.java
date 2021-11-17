package com.valteris.database.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LineJson {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("cells")
    @Expose
    private List<CellJson> cells;
}
