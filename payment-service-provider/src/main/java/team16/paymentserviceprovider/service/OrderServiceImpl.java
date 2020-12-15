package team16.paymentserviceprovider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.paymentserviceprovider.model.Order;
import team16.paymentserviceprovider.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order create(Order order) {
        order.setMerchant_order_timestamp(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    public Order getOne(Long id) {
        return null;
    }

    @Override
    public List<Order> getAllMerchantsOrders(Long merchantId) {
        return null;
    }
}
