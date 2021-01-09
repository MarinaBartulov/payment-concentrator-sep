package team16.paymentserviceprovider.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import team16.paymentserviceprovider.dto.OrderInfoDTO;
import team16.paymentserviceprovider.dto.PaymentMethodDTO;
import team16.paymentserviceprovider.dto.PaymentRequestDTO;
import team16.paymentserviceprovider.dto.PaymentResponseInfoDTO;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.Order;
import team16.paymentserviceprovider.model.PaymentMethod;
import team16.paymentserviceprovider.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    @Override
    public Order create(Order order) {
        order.setMerchantOrderTimestamp(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    public Order getOne(Long id) {
        return orderRepository.getOne(id);
    }

    @Override
    public List<Order> getAllMerchantsOrders(Long merchantId) {
        return null;
    }

    @Override
    public Order findById(Long id) {
        return this.orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<PaymentMethodDTO> getAvailablePaymentMethodsForOrder(Order order) {

        List<PaymentMethodDTO> pmDTOs = order.getMerchant().getPaymentMethods().stream().
                map(pm -> new PaymentMethodDTO(pm)).collect(Collectors.toList());
        return pmDTOs;
    }

    @Override
    public String choosePaymentMethodForOrderAndSend(Order order, String paymentMethodName) {

        Merchant merchant = order.getMerchant();
        if(merchant == null){
            logger.error("Failed to find Merchant");
            return null;
        }

        if(paymentMethodName.equals("Bank")){ //ovo je za banku jer se ne moze iskombinovati sa ostalima

            Long merchantOrderId = order.getMerchantOrderId();
            System.out.println("Found Merchant Order: " + order.getMerchantOrderId());

            // kreiram novi PaymentRequestDTO koji cu da posaljem na servis banke
            System.out.println("kreiram novi PaymentRequestDTO koji cu da posaljem na servis banke");

            PaymentRequestDTO paymentRequestDTO =
                    new PaymentRequestDTO(merchant.getMerchantId(), merchant.getEmail(), merchant.getPassword(), order.getAmount(),
                            merchantOrderId, order.getMerchantOrderTimestamp(), merchant.getMerchantSuccessUrl(),
                            merchant.getMerchantFailedUrl(), merchant.getMerchantErrorUrl());

            // saljem zahtev za dobijanje payment url i id na servis banke prodavca
            System.out.println("saljem zahtev za dobijanje payment url i id na servis banke prodavca");
            logger.info("Sending request to bank service");
            PaymentResponseInfoDTO response
                    = restTemplate.postForObject("https://localhost:8083/" + paymentMethodName.toLowerCase() + "-payment-service/api/payments/request",
                    paymentRequestDTO, PaymentResponseInfoDTO.class);

            logger.info("Received response from bank service");

            System.out.println(response.getPaymentUrl());
            System.out.println(response.getPaymentId());

            return response.getPaymentUrl();

        }else { //Ovo je za sve ostale nacine placanja

            OrderInfoDTO orderDTO = new OrderInfoDTO(order.getMerchantOrderId(), merchant.getEmail(), order.getAmount(), order.getCurrency(),
                    merchant.getMerchantSuccessUrl(), merchant.getMerchantErrorUrl(), merchant.getMerchantFailedUrl());


            HttpEntity<OrderInfoDTO> request = new HttpEntity<>(orderDTO);
            ResponseEntity<String> response = null;

            try {
                logger.info("Sending request to corresponding payment service");
                response = restTemplate.exchange("https://localhost:8083/" + paymentMethodName.toLowerCase() + "-payment-service/api/pay", HttpMethod.POST, request, String.class);
                logger.info("Received response from corresponding payment service");
            } catch (RestClientException e) {
                logger.error("RestTemplate error");
                e.printStackTrace();
            }

            return response.getBody();
        }
    }
}
