package com.graduatepj.enol.repository;

import com.graduatepj.enol.entity.SearchDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<SearchDocument, Long> {
}

