package com.example.systemtaskmanagement.domain.dao.mapper;

import com.example.systemtaskmanagement.domain.dao.dto.CommentDTO;
import com.example.systemtaskmanagement.domain.dao.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper extends BaseMapper<Comment, CommentDTO> {
}
