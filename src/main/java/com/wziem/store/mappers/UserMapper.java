package com.wziem.store.mappers;


import com.wziem.store.dtos.RegisterUserRequest;
import com.wziem.store.dtos.UpdateUserRequest;
import com.wziem.store.dtos.UserDto;
import com.wziem.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest request);

    void update(UpdateUserRequest request, @MappingTarget User user);
}
