package com.example.systemtaskmanagement.domain.dao.mapper;

import com.example.systemtaskmanagement.domain.dao.dto.TaskDTO;
import com.example.systemtaskmanagement.domain.dao.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper extends BaseMapper<Task, TaskDTO> {


}
