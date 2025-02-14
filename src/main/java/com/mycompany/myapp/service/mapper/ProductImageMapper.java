package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.ProductImage;
import com.mycompany.myapp.service.dto.OrderDTO;
import com.mycompany.myapp.service.dto.ProductImageDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link ProductImage} and its DTO {@link ProductImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductImageMapper extends EntityMapper<ProductImageDTO, ProductImage> {
    @Mapping(target = "order", source = "order", qualifiedByName = "orderMinimal")
    ProductImageDTO toDto(ProductImage productImage);

    @Named("orderMinimal")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "warehouseReceiptCode", source = "warehouseReceiptCode")
    OrderDTO toDtoOrderMinimal(Order order);
}
