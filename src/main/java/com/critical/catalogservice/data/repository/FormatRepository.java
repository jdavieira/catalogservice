package com.critical.catalogservice.data.repository;

import com.critical.catalogservice.data.entity.Format;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormatRepository extends JpaRepository<Format, Integer> {}