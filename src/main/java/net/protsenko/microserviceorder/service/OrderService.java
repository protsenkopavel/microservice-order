package net.protsenko.microserviceorder.service;

import net.protsenko.microserviceorder.domain.Order;

public interface OrderService {

    void createOrder(Order order);

}
