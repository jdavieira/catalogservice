package com.critical.catalogservice.data.repository;

import com.critical.catalogservice.data.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {}