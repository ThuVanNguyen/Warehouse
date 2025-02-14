package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Client;
import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.ProductImage;
import com.mycompany.myapp.service.dto.ClientDTO;
import com.mycompany.myapp.service.dto.OrderDTO;
import com.mycompany.myapp.service.dto.ProductImageDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientMinimal")
    @Mapping(target = "productImages", source = "productImages", qualifiedByName = "productImages")
    OrderDTO toDto(Order order);

    @Named("clientMinimal")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    ClientDTO toDtoClientMinimal(Client client);

    @Named("productImages")
    default Set<ProductImageDTO> toDtoProductImages(Set<ProductImage> productImages) {
        if (productImages == null) {
            return null;
        }
        return productImages.stream().map(this::toDtoProductImage).collect(Collectors.toSet());
    }

    @Named("productImage")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "base64", source = "base64")
    ProductImageDTO toDtoProductImage(ProductImage productImage);
}
