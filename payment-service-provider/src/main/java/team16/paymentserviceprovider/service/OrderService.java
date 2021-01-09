package team16.paymentserviceprovider.service;

import team16.paymentserviceprovider.dto.PaymentMethodDTO;
import team16.paymentserviceprovider.model.Order;
import team16.paymentserviceprovider.model.PaymentMethod;

import java.util.List;

public interface OrderService {

    Order create(Order order);
    Order getOne(Long id);
    List<Order> getAllMerchantsOrders(Long merchantId);
    Order findById(Long id);
    List<PaymentMethodDTO> getAvailablePaymentMethodsForOrder(Order order);
    String choosePaymentMethodForOrderAndSend(Order order, String paymentMethodName);
}
