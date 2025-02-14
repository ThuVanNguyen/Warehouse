package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.enums.Status;
import com.mycompany.myapp.repository.OrderRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.dto.OrderDTO;
import com.mycompany.myapp.service.mapper.OrderMapper;
import com.mycompany.myapp.utils.WarehouseDateUtils;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service Implementation for managing {@link com.mycompany.myapp.domain.Order}. */
@Service
@Transactional
public class OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final UserService userService;

    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, UserService userService, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.orderMapper = orderMapper;
    }

    /**
     * Save a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderDTO save(OrderDTO orderDTO) {
        LOG.debug("Request to save Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Update a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderDTO update(OrderDTO orderDTO) {
        LOG.debug("Request to update Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Partially update a order.
     *
     * @param orderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderDTO> partialUpdate(OrderDTO orderDTO) {
        LOG.debug("Request to partially update Order : {}", orderDTO);

        return orderRepository
            .findById(orderDTO.getId())
            .map(existingOrder -> {
                orderMapper.partialUpdate(existingOrder, orderDTO);

                return existingOrder;
            })
            .map(orderRepository::save)
            .map(orderMapper::toDto);
    }

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<OrderDTO> searchOrders(
        String warehouseReceiptCode,
        Status status,
        LocalDate date,
        String clientCode,
        String productName,
        Pageable pageable
    ) {
        String dateStr = WarehouseDateUtils.parse(date);
        Optional<User> currentUserOpt = userService.getUserWithAuthorities();

        boolean hasAdminOrEditorRole = currentUserOpt
            .map(user ->
                user
                    .getAuthorities()
                    .stream()
                    .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getName()) || "ROLE_EDITOR".equals(authority.getName()))
            )
            .orElse(false);

        String userCode = currentUserOpt.map(User::getClientCode).filter(StringUtils::isNotBlank).orElse(clientCode);

        Page<OrderDTO> orders = orderRepository
            .findAll(
                warehouseReceiptCode,
                status,
                dateStr,
                hasAdminOrEditorRole ? clientCode : null,
                hasAdminOrEditorRole,
                userCode,
                productName,
                pageable
            )
            .map(orderMapper::toDto);

        if (!hasAdminOrEditorRole) {
            orders.forEach(orderDTO -> {
                orderDTO.setUnitPriceWeight(null);
                orderDTO.setUnitPriceCubicMeter(null);
                orderDTO.setTotalPrice(null);
            });
        }

        return orders;
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderDTO> findOne(Long id) {
        Optional<User> currentUserOpt = userService.getUserWithAuthorities();

        boolean hasAdminOrEditorRole = currentUserOpt
            .map(user ->
                user
                    .getAuthorities()
                    .stream()
                    .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getName()) || "ROLE_EDITOR".equals(authority.getName()))
            )
            .orElse(false);
        LOG.debug("Request to get Order : {}", id);
        OrderDTO orderDTO = orderRepository.findById(id).map(orderMapper::toDto).orElse(null);

        if (!hasAdminOrEditorRole) {
            orderDTO.setUnitPriceWeight(null);
            orderDTO.setUnitPriceCubicMeter(null);
            orderDTO.setTotalPrice(null);
        }

        return Optional.ofNullable(orderDTO);
    }

    /**
     * Delete the order by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }

    @Transactional
    public Page<OrderDTO> updateOrderStatus(List<Long> orderIds, Status newStatus, Date date, Boolean selectAll, Pageable pageable) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<Order> orders;

        // TODO use query to update intead of object
        if (Boolean.TRUE.equals(selectAll)) {
            orders = orderRepository.findAll();
        } else {
            orders = orderRepository.findAllById(orderIds);
        }

        orders.forEach(order -> {
            order.setStatus(newStatus);

            if (Status.LOADED_ON_VEHICLE.equals(newStatus)) {
                order.setLoadedOnVehicleDate(localDate);
            } else if (Status.ARRIVED_AT_HN.equals(newStatus)) {
                order.setArrivedAtHNDate(localDate);
            } else if (Status.DELIVERED.equals(newStatus)) {
                order.setDeliveredDate(localDate);
            } else if (Status.CANCELED.equals(newStatus)) {
                order.setCancelDate(localDate);
            } else if (Status.CUSTOMER_SIGNED.equals(newStatus)) {
                order.setCustomerSignedDate(localDate);
            }
        });

        orderRepository.saveAll(orders);

        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }
}
