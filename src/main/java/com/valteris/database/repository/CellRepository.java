package com.valteris.database.repository;

import com.valteris.database.entity.CellEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CellRepository extends JpaRepository<CellEntity, Long> {
}
