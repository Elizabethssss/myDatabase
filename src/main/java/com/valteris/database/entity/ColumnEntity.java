package com.valteris.database.entity;

import com.valteris.database.domain.Type;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "clmn")
public class ColumnEntity {

    @Id
    @NonNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @NonNull
    @Column(name = "max_length")
    private Integer maxLength;

    @ManyToOne
    @JoinColumn(name = "tbl_id")
//    @JoinTable(name = "tbl")
    private TableEntity tableEntity;

    @OneToMany(mappedBy = "columnEntity")
    private List<CellEntity> cellEntities;

}
