package team16.paymentserviceprovider.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import team16.paymentserviceprovider.dto.*;
import team16.paymentserviceprovider.enums.OrderStatus;
import team16.paymentserviceprovider.exceptions.InvalidDataException;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.Order;
import team16.paymentserviceprovider.model.PaymentMethod;
import team16.paymentserviceprovider.repository.OrderRepository;
import team16.paymentserviceprovider.service.MerchantService;
import team16.paymentserviceprovider.service.OrderService;
import team16.paymentserviceprovider.service.PaymentMethodService;
import team16.paymentserviceprovider.service.ValidationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private ValidationService validationService;
    public OrderServiceImpl() {
        validationService = new ValidationService();
    }


    @Override
    public OrderResponseDTO createOrderFromClientApp(OrderDTO dto) throws InvalidDataException {
        System.out.println("------------------------DTO from LA-------------------------------");
        System.out.println(dto.getAmount());
        System.out.println(dto.getCurrency());
        System.out.println(dto.getMerchantEmail());
        System.out.println(dto.getMerchantSuccessUrl());
        System.out.println(dto.getMerchantFailedUrl());
        System.out.println(dto.getMerchantErrorUrl());

        validateDTO(dto);

        Merchant merchant = merchantService.findByMerchantEmail(dto.getMerchantEmail());
        logger.info("Found merchant: " + merchant.getEmail() + " | " + merchant.getMerchantId());
        System.out.println("Found merchant: " + merchant.getEmail() + "|" + merchant.getMerchantId());

        Order order = new Order();
        order.setMerchant(merchant);
        order.setAmount(dto.getAmount());
        order.setCurrency(dto.getCurrency());
        order.setMerchantOrderTimestamp(LocalDateTime.now());
        order.setMerchantOrderId(dto.getOrderId());
        order.setOrderStatus(OrderStatus.INITIATED);
        Order newOrder = orderRepository.save(order);
        System.out.println("Create Order: " + newOrder.getId());

        logger.info("New Order created: " + newOrder.getId());

        return new OrderResponseDTO(newOrder.getId(),
                "https://localhost:3001/order/" + newOrder.getId(), merchant.getMerchantId());
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

        PaymentMethod pm = this.paymentMethodService.findByName(paymentMethodName);

        if(paymentMethodName.equals("Bank")){ //ovo je za banku jer se ne moze iskombinovati sa ostalima

            Long merchantOrderId = order.getId();
            System.out.println("Found Merchant Order: " + order.getId());

            // kreiram novi PaymentRequestDTO koji cu da posaljem na servis banke
            System.out.println("kreiram novi PaymentRequestDTO koji cu da posaljem na servis banke");

            PaymentRequestDTO paymentRequestDTO =
                    new PaymentRequestDTO(merchant.getMerchantId(), merchant.getEmail(), merchant.getMerchantPassword(), order.getAmount(),
                            merchantOrderId, order.getMerchantOrderTimestamp(), merchant.getMerchantSuccessUrl(),
                            merchant.getMerchantFailedUrl(), merchant.getMerchantErrorUrl());

            // saljem zahtev za dobijanje payment url i id na servis banke prodavca
            System.out.println("saljem zahtev za dobijanje payment url i id na servis banke prodavca");
            logger.info("Sending request to bank service");
            PaymentResponseInfoDTO response = null;
            try {
                response = restTemplate.postForObject("https://localhost:8083/" + paymentMethodName.toLowerCase() + "-payment-service/api/payments/request",
                        paymentRequestDTO, PaymentResponseInfoDTO.class);
            }catch(RestClientException e){
                order.setOrderStatus(OrderStatus.INVALID);
                this.orderRepository.save(order);
                logger.error("RestTemplate error");
                e.printStackTrace();
            }

            logger.info("Received response from bank service");

            System.out.println(response.getPaymentUrl());
            System.out.println(response.getPaymentId());
            order.setPaymentMethod(pm);
            order.setOrderStatus(OrderStatus.CREATED);
            orderRepository.save(order);
            return response.getPaymentUrl();

        }else { //Ovo je za sve ostale nacine placanja

            OrderInfoDTO orderDTO = new OrderInfoDTO(order.getId(), merchant.getEmail(), order.getAmount(), order.getCurrency(),
                    merchant.getMerchantSuccessUrl(), merchant.getMerchantErrorUrl(), merchant.getMerchantFailedUrl());


            HttpEntity<OrderInfoDTO> request = new HttpEntity<>(orderDTO);
            ResponseEntity<String> response = null;

            try {
                logger.info("Sending request to corresponding payment service");
                response = restTemplate.exchange("https://localhost:8083/" + paymentMethodName.toLowerCase() + "-payment-service/api/pay", HttpMethod.POST, request, String.class);
                logger.info("Received response from corresponding payment service");
            } catch (RestClientException e) {
                order.setOrderStatus(OrderStatus.INVALID);
                this.orderRepository.save(order);
                logger.error("RestTemplate error");
                e.printStackTrace();
            }
            order.setPaymentMethod(pm);
            order.setOrderStatus(OrderStatus.CREATED);
            orderRepository.save(order);
            return response.getBody();
        }
    }

    @Override
    public OrderStatus findOrderStatus(String merchantEmail, Long orderId) {

        Order order = this.orderRepository.findOrderForMerchant(merchantEmail, orderId);
        if(order == null){
            return null;
        }
        return order.getOrderStatus();
    }

    @Scheduled(initialDelay = 10000, fixedRate = 300000) //na svakih 5 minuta
    public void updateOrdersStatus(){

        System.out.println("Updating orders status started...");
        List<Order> expiredOrders = this.orderRepository.findInitiatedOrders();

        for(Order o : expiredOrders){
            if(o.getMerchantOrderTimestamp().plusMinutes(30).isBefore(LocalDateTime.now())){
                System.out.println("Promenjen status sa " + o.getOrderStatus().toString() + " na EXPIRED");
                o.setOrderStatus(OrderStatus.EXPIRED);
                this.orderRepository.save(o);
            }
        }

        List<Order> unfinishedOrders = this.orderRepository.findCreatedOrders(); // traze se CREATED

        for(Order o : unfinishedOrders){

            ResponseEntity<OrderStatusDTO> response = null;
            try{
                response = restTemplate.getForEntity("https://localhost:8083/" + o.getPaymentMethod().getName().toLowerCase() + "-payment-service/api/status?orderId=" + o.getId(), OrderStatusDTO.class);

            }catch(Exception e){
                e.printStackTrace();
                return;
            }

            if(response.getBody().getStatus() != null) {
                OrderStatus status = OrderStatus.valueOf(response.getBody().getStatus());
                if (!status.equals(o.getOrderStatus())) {
                    System.out.println("Promenjen status sa " + o.getOrderStatus().toString() + " na " + status.toString());
                    o.setOrderStatus(status);
                    this.orderRepository.save(o);
                }
            }
        }
        System.out.println("Updating orders status finished...");
    }


    private void validateDTO(OrderDTO dto) throws InvalidDataException {
        System.out.println("------------------------DTO from LA validation-------------------------------");
        if(!validationService.validateString(dto.getMerchantEmail())) {
            System.out.println("Email null or empty");
            logger.debug("Invalid email");
            logger.error("Failed to create Order due to invalid received data");
            throw new InvalidDataException("Invalid merchant info.");
        }
        if(merchantService.findByMerchantEmail(dto.getMerchantEmail()) == null) {
            System.out.println("nonexistent merchant");
            logger.debug("Nonexistent merchant");
            logger.error("Failed to create Order due to invalid received data");
            throw new InvalidDataException("Nonexistent merchant.");
        }
        if(!validationService.validateString(dto.getCurrency())) {
            System.out.println("currency null or empty");
            logger.debug("Invalid currency");
            logger.error("Failed to create Order due to invalid received data");
            throw new InvalidDataException("Invalid currency.");
        }
        if(dto.getAmount() < 0) {
            System.out.println("negative amount");
            logger.debug("Invalid amount");
            logger.error("Failed to create Order due to invalid received data");
            throw new InvalidDataException("Amount cannot be negative.");
        }

        if(!validationService.validateString(dto.getMerchantSuccessUrl()) ||
                !validationService.validateString(dto.getMerchantFailedUrl()) ||
                !validationService.validateString(dto.getMerchantErrorUrl())) {
            System.out.println("invalid urls - null or empty");
            logger.debug("Invalid redirection URLs");
            logger.error("Failed to create Order due to invalid received data");
            throw new InvalidDataException("Invalid URLs.");
        }
    }
}
