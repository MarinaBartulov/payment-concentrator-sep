package team16.paymentserviceprovider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team16.paymentserviceprovider.config.EndpointConfig;
import team16.paymentserviceprovider.config.RestConfig;
import team16.paymentserviceprovider.dto.*;
import team16.paymentserviceprovider.exceptions.InvalidDataException;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.Order;

import javax.xml.bind.ValidationException;
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

    private ValidationService validationService;

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

        Merchant merchant = merchantService.findByMerchantId(dto.getMerchantId());
        System.out.println("Found merchant: " + merchant.getMerchantEmail() + "|" + merchant.getMerchantId());

        Order order = new Order();
        order.setMerchant(merchant);
        order.setAmount(dto.getAmount());
        order.setMerchantOrderTimestamp(LocalDateTime.now());
        Order newOrder = orderService.create(order);
        System.out.println("Create Order: " + newOrder.getMerchantOrderId());

        return new OrderResponseDTO(newOrder.getMerchantOrderId(),
                "https://localhost:3001/" + newOrder.getMerchantOrderId(), merchant.getMerchantId());
    }

    public PaymentResponseInfoDTO createPaymentRequest(Long orderId) throws Exception {
        System.out.println("------------------------Order from PC front-------------------------------");
        Order order = orderService.getOne(orderId);
        System.out.println("Found Order: " + order.getMerchantOrderId());
        if(order == null) {
            System.out.println("Not Found Order");
            throw new InvalidDataException("Nonexistent order.");
        }
        Merchant merchant = order.getMerchant();
        Long merchantOrderId = order.getMerchantOrderId();
        System.out.println("Found Merchant Order: " + order.getMerchantOrderId());

        // kreiram novi PaymentRequestDTO koji cu da posaljem na servis banke
        System.out.println("kreiram novi PaymentRequestDTO koji cu da posaljem na servis banke");

        PaymentRequestDTO paymentRequestDTO =
                new PaymentRequestDTO(merchant.getMerchantId(), merchant.getPassword(), order.getAmount(),
                        merchantOrderId, order.getMerchantOrderTimestamp(), merchant.getMerchantSuccessUrl(),
                        merchant.getMerchantFailedUrl(), merchant.getMerchantErrorUrl());

        // saljem zahtev za dobijanje payment url i id na servis banke prodavca
        System.out.println("saljem zahtev za dobijanje payment url i id na servis banke prodavca");
        PaymentResponseInfoDTO response
                = restTemplate.postForObject(getEndpoint(),
                paymentRequestDTO, PaymentResponseInfoDTO.class);

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
            throw new InvalidDataException("Invalid merchant info.");
        }
        if(merchantService.findByMerchantId(dto.getMerchantId()) == null) {
            System.out.println("nonexistent merchant");
            throw new InvalidDataException("Nonexistent merchant.");
        }
        if(!validationService.validateString(dto.getCurrency())) {
            System.out.println("currency null or empty");
            throw new InvalidDataException("Invalid currency.");
        }
        if(dto.getAmount() < 0) {
            System.out.println("negative amount");
            throw new InvalidDataException("Amount cannot be negative.");
        }
        Merchant merchant = merchantService.findByMerchantId(dto.getMerchantId());
        if(!merchant.getPassword().equals(dto.getMerchantPassword())) {
            System.out.println("passwords dont match");
            throw new InvalidDataException("Invalid merchant info.");
        }
        if(!validationService.validateString(dto.getMerchantSuccessUrl()) ||
                !validationService.validateString(dto.getMerchantFailedUrl()) ||
                !validationService.validateString(dto.getMerchantErrorUrl())) {
            System.out.println("invalid urls - null or empty");
            throw new InvalidDataException("Invalid URLs.");
        }
    }
}
