package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.OrderHistory;
import com.mycompany.myapp.service.dto.OrderHistoryDTO;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

@Mapper(componentModel = "spring")
public interface OrderHistoryMapper extends EntityMapper<OrderHistoryDTO, OrderHistory> {
    OrderHistoryDTO toDto(OrderHistory orderHistory);
}
