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

        validateDTO(dto);

        Merchant merchant = merchantService.findByMerchantId(dto.getMerchantId());

        Order order = new Order();
        order.setMerchant(merchant);
        order.setAmount(dto.getAmount());
        order.setMerchantOrderTimestamp(LocalDateTime.now());
        Order newOrder = orderService.create(order);

        return new OrderResponseDTO(newOrder.getMerchantOrderId(),
                "https://localhost:3001/" + newOrder.getMerchantOrderId(), merchant.getMerchantId());
    }

    public PaymentResponseInfoDTO createPaymentRequest(Long orderId) throws Exception {

        Order order = orderService.getOne(orderId);
        if(order == null) {
            throw new InvalidDataException("Nonexistent order.");
        }
        Merchant merchant = order.getMerchant();
        Long merchantOrderId = order.getMerchantOrderId();

        // kreiram novi PaymentRequestDTO koji cu da posaljem na servis banke
        PaymentRequestDTO paymentRequestDTO =
                new PaymentRequestDTO(merchant.getMerchantId(), merchant.getPassword(), order.getAmount(),
                        merchantOrderId, order.getMerchantOrderTimestamp(), merchant.getMerchantSuccessUrl(),
                        merchant.getMerchantFailedUrl(), merchant.getMerchantErrorUrl());

        // saljem zahtev za dobijanje payment url i id na servis banke prodavca
        PaymentResponseInfoDTO response
                = restTemplate.postForObject(getEndpoint(),
                paymentRequestDTO, PaymentResponseInfoDTO.class);

        return response;
    }

    private URI getEndpoint() throws URISyntaxException {
        return new URI(configuration.url() + EndpointConfig.BANK_PAYMENT_SERVICE_BASE_URL + "/api/payments/request");
    }

    private void validateDTO(OrderDTO dto) throws InvalidDataException {
        if(!validationService.validateString(dto.getMerchantId()) ||
                !validationService.validateString(dto.getMerchantPassword()) ||
                !validationService.validateString(dto.getMerchantEmail())) {
            throw new InvalidDataException("Invalid merchant info.");
        }
        if(merchantService.findByMerchantId(dto.getMerchantId()) == null) {
            throw new InvalidDataException("Nonexistent merchant.");
        }
        if(!validationService.validateString(dto.getCurrency())) {
            throw new InvalidDataException("Invalid currency.");
        }
        if(dto.getAmount() < 0) {
            throw new InvalidDataException("Amount cannot be negative.");
        }
        Merchant merchant = merchantService.findByMerchantId(dto.getMerchantId());
        if(!merchant.getPassword().equals(dto.getMerchantPassword())) {
            throw new InvalidDataException("Invalid merchant info.");
        }
        if(!validationService.validateString(dto.getMerchantSuccessUrl()) ||
                !validationService.validateString(dto.getMerchantFailedUrl()) ||
                !validationService.validateString(dto.getMerchantErrorUrl())) {
            throw new InvalidDataException("Invalid URLs.");
        }
    }
}
