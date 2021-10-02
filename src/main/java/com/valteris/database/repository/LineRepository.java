package com.valteris.database.repository;

import com.valteris.database.entity.LineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineRepository extends JpaRepository<LineEntity, Long> {
}
