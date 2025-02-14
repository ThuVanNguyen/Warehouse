package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.OrderHistory;
import com.mycompany.myapp.repository.OrderHistoryRepository;
import com.mycompany.myapp.repository.OrderRepository;
import com.mycompany.myapp.service.dto.OrderHistoryDTO;
import com.mycompany.myapp.service.mapper.OrderHistoryMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderHistoryService.class);

    private final OrderHistoryRepository orderHistoryRepository;

    private final OrderHistoryMapper orderHistoryMapper;

    private final OrderRepository orderRepository;

    public OrderHistoryService(
        OrderHistoryRepository orderHistoryRepository,
        OrderHistoryMapper orderHistoryMapper,
        OrderRepository orderRepository
    ) {
        this.orderHistoryRepository = orderHistoryRepository;
        this.orderHistoryMapper = orderHistoryMapper;
        this.orderRepository = orderRepository;
    }

    /**
     * Save a orderHistory.
     *
     * @param orderHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderHistoryDTO save(OrderHistoryDTO orderHistoryDTO) {
        LOG.debug("Request to save OrderHistory : {}", orderHistoryDTO);
        OrderHistory orderHistory = orderHistoryMapper.toEntity(orderHistoryDTO);
        orderHistory = orderHistoryRepository.save(orderHistory);
        return orderHistoryMapper.toDto(orderHistory);
    }

    /**
     * Update a orderHistory.
     *
     * @param orderHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderHistoryDTO update(OrderHistoryDTO orderHistoryDTO) {
        LOG.debug("Request to update OrderHistory : {}", orderHistoryDTO);
        OrderHistory orderHistory = orderHistoryMapper.toEntity(orderHistoryDTO);
        orderHistory = orderHistoryRepository.save(orderHistory);
        return orderHistoryMapper.toDto(orderHistory);
    }

    /**
     * Partially update a orderHistory.
     *
     * @param orderHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderHistoryDTO> partialUpdate(OrderHistoryDTO orderHistoryDTO) {
        LOG.debug("Request to partially update OrderHistory : {}", orderHistoryDTO);

        return orderHistoryRepository
            .findById(orderHistoryDTO.getId())
            .map(existingOrderHistory -> {
                orderHistoryMapper.partialUpdate(existingOrderHistory, orderHistoryDTO);

                return existingOrderHistory;
            })
            .map(orderHistoryRepository::save)
            .map(orderHistoryMapper::toDto);
    }

    /**
     * Get all the ordersHistory.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderHistoryDTO> findAll(String keyword, Pageable pageable) {
        LOG.debug("Request to get all OrdersHistory");
        return orderHistoryRepository.findAll(keyword, pageable).map(orderHistoryMapper::toDto);
    }

    /**
     * Get one orderHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get OrderHistory: {}", id);
        return orderHistoryRepository.findById(id).map(orderHistoryMapper::toDto);
    }

    /**
     * Delete the orderHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderHistory: {}", id);
        orderHistoryRepository.deleteById(id);
    }

    @Transactional
    public void createOrderHistoryAndDeleteOrder() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<Order> ordersToProcess = orderRepository.findRecordsWithExactOneMonthAgo(oneMonthAgo);

        for (Order order : ordersToProcess) {
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setImportVehicleNumber(order.getImportVehicleNumber());
            orderHistory.setReceiptDate(order.getReceiptDate());
            orderHistory.setWarehouseReceiptCode(order.getWarehouseReceiptCode());
            orderHistory.setProductCn(order.getProductCn());
            orderHistory.setProductVn(order.getProductVn());
            orderHistory.setPiecesReceivedCount(order.getPiecesReceivedCount());
            orderHistory.setPiecesWareHouseCount(order.getPiecesWareHouseCount());
            orderHistory.setTotalWeight(order.getTotalWeight());
            orderHistory.setTotalCubicMeter(order.getTotalCubicMeter());
            orderHistory.setListCalculation(order.getListCalculation());
            orderHistory.setUnitPriceWeight(order.getUnitPriceWeight());
            orderHistory.setUnitPriceCubicMeter(order.getUnitPriceCubicMeter());
            orderHistory.setTotalPrice(order.getTotalPrice());
            orderHistory.setWaybill(order.getWaybill());
            orderHistory.setShippingAddressAndPhone(order.getShippingAddressAndPhone());
            orderHistory.setExportVehicleNumber(order.getExportVehicleNumber());
            orderHistory.setExportDate(order.getExportDate());
            orderHistory.setExportQuantity(order.getExportQuantity());
            orderHistory.setNote(order.getNote());
            orderHistory.setClient(order.getClient());
            orderHistory.setStatus(order.getStatus());
            orderHistory.setLoadedOnVehicleDate(order.getLoadedOnVehicleDate());
            orderHistory.setArrivedAtHNDate(order.getArrivedAtHNDate());
            orderHistory.setDeliveredDate(order.getDeliveredDate());
            orderHistory.setCustomerSignedDate(order.getCustomerSignedDate());
            orderHistory.setCancelDate(order.getCancelDate());
            orderHistory.setArrangeVehicle(order.getArrangeVehicle());
            orderHistory.setProductImages(order.getProductImages());

            orderHistoryRepository.save(orderHistory);

            orderRepository.delete(order);
        }
    }
}
