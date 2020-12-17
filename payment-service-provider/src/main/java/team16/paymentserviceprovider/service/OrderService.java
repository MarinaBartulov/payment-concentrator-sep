package team16.paymentserviceprovider.service;

import team16.paymentserviceprovider.model.Order;

import java.util.List;

public interface OrderService {

    Order create(Order order);
    Order getOne(Long id);
    List<Order> getAllMerchantsOrders(Long merchantId);
}
