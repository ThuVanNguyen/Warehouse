package com.mycompany.myapp.schedulers;

import com.mycompany.myapp.service.OrderHistoryService;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(OrderScheduler.class);

    @Autowired
    private OrderHistoryService orderHistoryService;

    @Scheduled(fixedDelay = 86400000)
    public void runCreateOrderHistoryAndDeleteOrder() {
        LOG.info(
            "========== Start scheduler : {} ==========",
            ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"))
        );

        orderHistoryService.createOrderHistoryAndDeleteOrder();

        LOG.info(
            "========== End scheduler : {} ==========",
            ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"))
        );
    }
}
