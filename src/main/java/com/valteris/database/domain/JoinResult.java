package com.valteris.database.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinResult {

    private Line rightLine;
    private Line leftLine;
    private Cell joiningCell;
}
