package com.valteris.database.repository;

import com.valteris.database.entity.DatabaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseRepository extends JpaRepository<DatabaseEntity, Long> {
}
