package com.valteris.database.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "line")
public class LineEntity {

    @Id
    @NonNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tbl_id")
    private TableEntity tableEntity;

    @OneToMany(mappedBy = "lineEntity",
            cascade = CascadeType.ALL)
    private List<CellEntity> cellEntities = new ArrayList<>();

    public void addCellEntity(CellEntity cellEntity) {
        cellEntities.add(cellEntity);
        cellEntity.setLineEntity(this);
    }

    public void removeCellEntity(CellEntity cellEntity) {
        cellEntities.remove(cellEntity);
        cellEntity.setLineEntity(null);
    }
}
