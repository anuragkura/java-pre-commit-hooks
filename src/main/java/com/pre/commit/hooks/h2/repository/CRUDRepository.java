package com.pre.commit.hooks.h2.repository;

import com.pre.commit.hooks.h2.model.CRUDModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CRUDRepository extends JpaRepository<CRUDModel, Long> {
  List<CRUDModel> findByPublished(boolean published);

  List<CRUDModel> findByTitleContainingIgnoreCase(String title);
}
