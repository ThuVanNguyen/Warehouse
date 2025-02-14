package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ProductImageAsserts.*;
import static com.mycompany.myapp.domain.ProductImageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductImageMapperTest {

    private ProductImageMapper productImageMapper;

    @BeforeEach
    void setUp() {
        productImageMapper = new ProductImageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductImageSample1();
        var actual = productImageMapper.toEntity(productImageMapper.toDto(expected));
        assertProductImageAllPropertiesEquals(expected, actual);
    }
}
