package com.wziem.store.mappers;

import com.wziem.store.dtos.ProductDto;
import com.wziem.store.entities.Product;
import com.wziem.store.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")

public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);

    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductDto request);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    void update(ProductDto request, @MappingTarget Product product);


}
