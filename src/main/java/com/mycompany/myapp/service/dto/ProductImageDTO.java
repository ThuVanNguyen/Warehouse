package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ProductImage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductImageDTO implements Serializable {

    private Long id;

    private String code;

    private Long len;

    private LocalDate createDate;

    private String type;

    private String base64;

    private OrderDTO order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getLen() {
        return len;
    }

    public void setLen(Long len) {
        this.len = len;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductImageDTO)) {
            return false;
        }

        ProductImageDTO productImageDTO = (ProductImageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productImageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductImageDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", len=" + getLen() +
            ", createDate='" + getCreateDate() + "'" +
            ", type='" + getType() + "'" +
            ", base64='" + getBase64() + "'" +
            ", order=" + getOrder() +
            "}";
    }
}
