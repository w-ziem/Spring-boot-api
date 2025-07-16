package com.wziem.store.mappers;


import com.wziem.store.dtos.UserDto;
import com.wziem.store.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
