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
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "cell")
public class CellEntity {

    @Id
    @NonNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "value")
    private String value;

    @NonNull
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private LineEntity lineEntity;

    @ManyToOne
    @JoinColumn(name = "clmn_id")
    private ColumnEntity columnEntity;
}
