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
import team16.paymentserviceprovider.config.EndpointConfig;
import team16.paymentserviceprovider.config.RestConfig;
import team16.paymentserviceprovider.dto.*;
import team16.paymentserviceprovider.exceptions.InvalidDataException;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.Order;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestConfig configuration;

    @Autowired
    private MerchantServiceImpl merchantService;

    @Autowired
    private OrderServiceImpl orderService;

    private ValidationService validationService;

    Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public PaymentService() {
        validationService = new ValidationService();
    }

    public OrderResponseDTO createPaymentResponseToLA(OrderDTO dto) throws Exception {
        System.out.println("------------------------DTO from LA-------------------------------");
        System.out.println(dto.getAmount());
        System.out.println(dto.getCurrency());
        System.out.println(dto.getMerchantEmail());
        System.out.println(dto.getMerchantId());
        System.out.println(dto.getMerchantPassword());
        System.out.println(dto.getMerchantSuccessUrl());
        System.out.println(dto.getMerchantFailedUrl());
        System.out.println(dto.getMerchantErrorUrl());

        validateDTO(dto);

        Merchant merchant = merchantService.findByMerchantEmail(dto.getMerchantEmail());
        logger.info("Found merchant: " + merchant.getMerchantEmail() + " | " + merchant.getMerchantId());
        System.out.println("Found merchant: " + merchant.getMerchantEmail() + "|" + merchant.getMerchantId());

        Order order = new Order();
        order.setMerchant(merchant);
        order.setAmount(dto.getAmount());
        order.setCurrency(dto.getCurrency());
        order.setMerchantOrderTimestamp(LocalDateTime.now());
        Order newOrder = orderService.create(order);
        System.out.println("Create Order: " + newOrder.getMerchantOrderId());

        logger.info("New Order created: " + newOrder.getMerchantOrderId());

        return new OrderResponseDTO(newOrder.getMerchantOrderId(),
                "https://localhost:3001/" + newOrder.getMerchantOrderId(), merchant.getMerchantId());
    }

    public PaymentResponseInfoDTO createPaymentRequest(Long orderId) throws Exception {
        System.out.println("------------------------Order from PC front-------------------------------");
        Order order = orderService.getOne(orderId);
        System.out.println("Found Order: " + order.getMerchantOrderId());
        logger.info("Found Order: " + order.getMerchantOrderId());
        if(order == null) {
            System.out.println("Not Found Order");
            logger.error("Not Found Order");
            throw new InvalidDataException("Nonexistent order.");
        }
        Merchant merchant = order.getMerchant();
        Long merchantOrderId = order.getMerchantOrderId();
        System.out.println("Found Merchant Order: " + order.getMerchantOrderId());

        // kreiram novi PaymentRequestDTO koji cu da posaljem na servis banke
        System.out.println("kreiram novi PaymentRequestDTO koji cu da posaljem na servis banke");

        PaymentRequestDTO paymentRequestDTO =
                new PaymentRequestDTO(merchant.getMerchantId(), merchant.getMerchantEmail(), merchant.getPassword(), order.getAmount(),
                        merchantOrderId, order.getMerchantOrderTimestamp(), merchant.getMerchantSuccessUrl(),
                        merchant.getMerchantFailedUrl(), merchant.getMerchantErrorUrl());

        // saljem zahtev za dobijanje payment url i id na servis banke prodavca
        System.out.println("saljem zahtev za dobijanje payment url i id na servis banke prodavca");
        logger.info("Sending request to bank service");
        PaymentResponseInfoDTO response
                = restTemplate.postForObject(getEndpoint(),
                paymentRequestDTO, PaymentResponseInfoDTO.class);

        logger.info("Received response from bank service");

        System.out.println(response.getPaymentUrl());
        System.out.println(response.getPaymentId());

        return response;
    }

    private URI getEndpoint() throws URISyntaxException {
        return new URI(configuration.url() + EndpointConfig.BANK_PAYMENT_SERVICE_BASE_URL + "/api/payments/request");
    }

    private void validateDTO(OrderDTO dto) throws InvalidDataException {
        System.out.println("------------------------DTO from LA validation-------------------------------");
        if(!validationService.validateString(dto.getMerchantId()) ||
                !validationService.validateString(dto.getMerchantPassword()) ||
                !validationService.validateString(dto.getMerchantEmail())) {
            System.out.println("Id, pass or email null or empty");
            logger.debug("Invalid merchantId, password or email");
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
        Merchant merchant = merchantService.findByMerchantEmail(dto.getMerchantEmail());
        if(!merchant.getPassword().equals(dto.getMerchantPassword())) {
            System.out.println("passwords dont match");
            logger.debug("Invalid merchant password");
            logger.error("Failed to create Order due to invalid received data");
            throw new InvalidDataException("Invalid merchant info.");
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

    public String createGenericPaymentRequest(Order order, String paymentMethodName) throws URISyntaxException {
        Merchant merchant = order.getMerchant();
        if(merchant == null){
            logger.error("Failed to find Merchant");
            return null;
        }

        OrderInfoDTO orderDTO = new OrderInfoDTO(order.getMerchantOrderId(),merchant.getMerchantEmail(), order.getAmount(), order.getCurrency(),
                merchant.getMerchantSuccessUrl(), merchant.getMerchantErrorUrl(), merchant.getMerchantFailedUrl());


        HttpEntity<OrderInfoDTO> request = new HttpEntity<>(orderDTO);
        ResponseEntity<String> response = null;

        try {
            logger.info("Sending request to corresponding payment service");
            response = restTemplate.exchange(
                    getServiceEndpoint(paymentMethodName), HttpMethod.POST, request, String.class);
            logger.info("Received response from corresponding payment service");
        } catch (RestClientException e) {
            logger.error("RestTemplate error");
            e.printStackTrace();
        }

        return response.getBody();
    }

    private URI getServiceEndpoint(String paymentMethodName) throws URISyntaxException {
        return new URI(configuration.url() + "/" + paymentMethodName + "-payment-service/api/pay");
    }
}
