package com.critical.catalogservice.data.repository;

import com.critical.catalogservice.data.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Integer> {}