package com.vorova.todo.webapp.converter;

import com.vorova.todo.models.dto.UserRegDto;
import com.vorova.todo.models.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class UserConverter {

    public abstract User userRegDtoToUser(UserRegDto userRegDto);

}