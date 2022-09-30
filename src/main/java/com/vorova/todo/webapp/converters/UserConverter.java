package com.vorova.todo.webapp.converters;

import com.vorova.todo.models.dto.UserRegDto;
import com.vorova.todo.models.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserConverter {

    public abstract User userRegDtoToUser(UserRegDto userRegDto);

}
