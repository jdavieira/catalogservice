package com.critical.catalogservice.data.repository;

import com.critical.catalogservice.data.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {}