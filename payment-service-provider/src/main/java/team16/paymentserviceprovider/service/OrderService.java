package team16.paymentserviceprovider.service;

import team16.paymentserviceprovider.dto.OrderDTO;
import team16.paymentserviceprovider.dto.OrderResponseDTO;
import team16.paymentserviceprovider.dto.PaymentMethodDTO;
import team16.paymentserviceprovider.exceptions.InvalidDataException;
import team16.paymentserviceprovider.model.Order;
import team16.paymentserviceprovider.model.PaymentMethod;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrderFromClientApp(OrderDTO dto) throws InvalidDataException;
    Order getOne(Long id);
    List<Order> getAllMerchantsOrders(Long merchantId);
    Order findById(Long id);
    List<PaymentMethodDTO> getAvailablePaymentMethodsForOrder(Order order);
    String choosePaymentMethodForOrderAndSend(Order order, String paymentMethodName);
}
