package com.example.systemtaskmanagement.domain.dao.repo;


import com.example.systemtaskmanagement.domain.dao.model.BaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BaseRepo<E extends BaseModel> extends JpaRepository<E, Long>, JpaSpecificationExecutor<E> {

    Page<E> findAll(Specification<E> specs, Pageable pageable);

    List<E> findAll(Specification<E> specs);
    List<E> findAll(Specification<E> spec, Sort sort);
}
