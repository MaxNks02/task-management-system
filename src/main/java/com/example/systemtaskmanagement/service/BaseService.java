package com.example.systemtaskmanagement.service;


import com.example.systemtaskmanagement.domain.dao.dto.BaseDTO;
import com.example.systemtaskmanagement.domain.dao.mapper.BaseMapper;
import com.example.systemtaskmanagement.domain.dao.model.BaseModel;
import com.example.systemtaskmanagement.domain.dao.repo.BaseRepo;
import com.example.systemtaskmanagement.exeption.CustomNotFoundException;
import com.example.systemtaskmanagement.exeption.DatabaseException;
import com.example.systemtaskmanagement.exeption.handling.ApiMessages;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseService<
        E extends BaseModel,
        D extends BaseDTO,
        R extends BaseRepo<E>,
        M extends BaseMapper<E, D>
        > {

    protected static final String DATA_NOT_FOUND = "Data not found. Id: %s";

    private final R repository;
    private final M mapper;

    protected BaseService(R repository, M mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public R getRepository() {
        return repository;
    }

    public M getMapper() {
        return mapper;
    }

    @Transactional
    public D create(D dto) {
        E entity = mapper.toEntity(dto);
        E savedEntity;
        try {
            savedEntity = repository.save(entity);
        } catch (RuntimeException exception) {
            throw new DatabaseException(String.format(ApiMessages.INTERNAL_SERVER_ERROR + " %s", exception.getMessage()));
        }
        return mapper.toDto(savedEntity);
    }


    public D getById(Long id) {
        E entity = repository.findById(id).orElseThrow(() -> new CustomNotFoundException(String.format(ApiMessages.NOT_FOUND + DATA_NOT_FOUND, id)));
        return mapper.toDto(entity);
    }

    public E entityGetById(Long id) {
        return repository.findById(id).orElseThrow(() -> new CustomNotFoundException(String.format(ApiMessages.NOT_FOUND + DATA_NOT_FOUND, id)));
    }

    public List<D> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public Page<D> getAllWithPagination(Pageable pageable) {
        try {
            return repository.findAll(pageable).map(mapper::toDto);
        } catch (RuntimeException exception) {
            throw new DatabaseException(String.format(ApiMessages.INTERNAL_SERVER_ERROR + " %s", exception.getMessage()));
        }
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new CustomNotFoundException(String.format(ApiMessages.NOT_FOUND + DATA_NOT_FOUND, id));
        }
        try {
            repository.deleteById(id);
        } catch (RuntimeException exception) {
            throw new DatabaseException(String.format(ApiMessages.INTERNAL_SERVER_ERROR + " %s", exception.getMessage()));
        }
    }

    public abstract D update(D dto);

}
