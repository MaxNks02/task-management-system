package com.example.systemtaskmanagement.domain.dao.mapper;

import com.example.systemtaskmanagement.domain.dao.dto.UserDTO;
import com.example.systemtaskmanagement.domain.dao.model.Task;
import com.example.systemtaskmanagement.domain.dao.model.User;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper extends BaseMapper<User, UserDTO> {

}
