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
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.Order;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

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

    Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public OrderResponseDTO createPaymentResponseToLA(OrderDTO dto) throws Exception {
        // proveravam da li su Merchant info okej - za sad imam samo id
        if(dto.getMerchantId().equals("") || dto.getMerchantId() == null) {
            logger.debug("Invalid merchantId");
            logger.error("Failed to create Order due to invalid received data");
            throw new Exception("Invalid merchant info."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(merchantService.findByMerchantId(dto.getMerchantId()) == null) {
            logger.debug("Nonexistent merchant");
            logger.error("Failed to create Order due to invalid received data");
            throw new Exception("Nonexistent merchant.");
        }
        if(dto.getAmount() < 0) {
            logger.debug("Invalid amount");
            logger.error("Failed to create Order due to invalid received data");
            throw new Exception("Amount cannot be negative.");
        }
        // amount se proverava tek kod banke, prilikom placanja
        Merchant merchant = merchantService.findByMerchantId(dto.getMerchantId());
        if(dto.getMerchantPassword().equals("") || dto.getMerchantPassword() == null) {
            logger.debug("Invalid merchant password");
            logger.error("Failed to create Order due to invalid received data");
            throw new Exception("Invalid merchant info."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(!merchant.getPassword().equals(dto.getMerchantPassword())) {
            logger.debug("Invalid merchant password");
            logger.error("Failed to create Order due to invalid received data");
            throw new Exception("Invalid merchant info.");
        }

        Order order = new Order();
        order.setMerchant(merchant);
        order.setAmount(dto.getAmount());
        order.setCurrency(dto.getCurrency());
        order.setMerchantOrderTimestamp(LocalDateTime.now());
        Order newOrder = orderService.create(order);

        logger.info("New Order created");

        return new OrderResponseDTO(newOrder.getMerchantOrderId(),
                "https://localhost:3001/" + newOrder.getMerchantOrderId(), merchant.getMerchantId());
    }

    public PaymentResponseInfoDTO createPaymentRequest(Long orderId) throws Exception {
        // amount se proverava tek kod banke, prilikom placanja
        // kreira se novi order, povezuje se sa ovm merchant-om i timestamp ce mu biti now
        Order order = orderService.getOne(orderId);
        Merchant merchant = order.getMerchant();
        Long merchantOrderId = order.getMerchantOrderId();
        // kreiram novi PaymentRequestDTO koji cu da posaljem na servis banke
        PaymentRequestDTO paymentRequestDTO =
                new PaymentRequestDTO(merchant.getMerchantId(), merchant.getPassword(), order.getAmount(),
                        merchantOrderId, order.getMerchantOrderTimestamp(), merchant.getMerchantSuccessUrl(),
                        merchant.getMerchantFailedUrl(), merchant.getMerchantErrorUrl());
        // saljem zahtev za dobijanje payment url i id na servis banke prodavca
        logger.info("Sending request to bank service");
        PaymentResponseInfoDTO response
                = restTemplate.postForObject(getEndpoint(),
                paymentRequestDTO, PaymentResponseInfoDTO.class);
        logger.info("Received response from bank service");

        System.out.println(response.getPaymentUrl());
        return response;
    }

    private URI getEndpoint() throws URISyntaxException {
        return new URI(configuration.url() + EndpointConfig.BANK_PAYMENT_SERVICE_BASE_URL + "/api/payments/request");
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
