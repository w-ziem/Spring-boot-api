package com.wziem.store.mappers;

import com.wziem.store.dtos.ProductSummaryDto;
import com.wziem.store.dtos.ProductDto;
import com.wziem.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);

    ProductSummaryDto toCartProductDto(Product product);

    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductDto request);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    void update(ProductDto request, @MappingTarget Product product);


}
