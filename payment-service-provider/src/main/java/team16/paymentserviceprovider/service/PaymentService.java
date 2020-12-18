package team16.paymentserviceprovider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public OrderResponseDTO createPaymentResponseToLA(OrderDTO dto) throws Exception {
        // proveravam da li su Merchant info okej - za sad imam samo id
        if(dto.getMerchantId().equals("") || dto.getMerchantId() == null) {
            throw new Exception("Invalid merchant info."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(merchantService.findByMerchantId(dto.getMerchantId()) == null) {
            throw new Exception("Nonexistent merchant.");
        }
        if(dto.getAmount() < 0) {
            throw new Exception("Amount cannot be negative.");
        }
        // amount se proverava tek kod banke, prilikom placanja
        Merchant merchant = merchantService.findByMerchantId(dto.getMerchantId());
        if(dto.getMerchantPassword().equals("") || dto.getMerchantPassword() == null) {
            throw new Exception("Invalid merchant info."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(!merchant.getPassword().equals(dto.getMerchantPassword())) {
            throw new Exception("Invalid merchant info.");
        }

        Order order = new Order();
        order.setMerchant(merchant);
        order.setAmount(dto.getAmount());
        order.setMerchantOrderTimestamp(LocalDateTime.now());
        Order newOrder = orderService.create(order);

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
        PaymentResponseInfoDTO response
                = restTemplate.postForObject(getEndpoint(),
                paymentRequestDTO, PaymentResponseInfoDTO.class);
        System.out.println(response.getPaymentUrl());
        return response;
    }

    private URI getEndpoint() throws URISyntaxException {
        return new URI(configuration.url() + EndpointConfig.BANK_PAYMENT_SERVICE_BASE_URL + "/api/payments/request");
    }
}
