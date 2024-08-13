package com.example.systemtaskmanagement.domain.dao.mapper;



import com.example.systemtaskmanagement.domain.dao.dto.BaseDTO;
import com.example.systemtaskmanagement.domain.dao.model.BaseModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface BaseMapper<E extends BaseModel, D extends BaseDTO> {

    D toDto(E entity);

    E toEntity(D dto);

    default Set<D> convertFromEntityList(Set<E> entityList) {
        if (entityList == null) {
            return new HashSet<>();
        }
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    default Set<E> convertFromDtoList(Set<D> dtoList) {
        if (dtoList == null) {
            return new HashSet<>();
        }
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }


}
