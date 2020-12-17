package team16.paymentserviceprovider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team16.paymentserviceprovider.config.EndpointConfig;
import team16.paymentserviceprovider.config.RestConfig;
import team16.paymentserviceprovider.dto.PaymentDetailsDTO;
import team16.paymentserviceprovider.dto.PaymentRequestDTO;
import team16.paymentserviceprovider.dto.PaymentResponseInfoDTO;
import team16.paymentserviceprovider.dto.ResponseForLADTO;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.Order;

import java.net.URI;
import java.net.URISyntaxException;

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

    public ResponseForLADTO createPaymentResponseToLA(PaymentDetailsDTO dto) throws Exception {
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
        order.setApproved(false);
        Order newOrder = orderService.create(order);

        return new ResponseForLADTO("https://localhost:3001", merchant.getMerchant_id());
    }

    public PaymentResponseInfoDTO createPaymentRequest() throws Exception {
        String merchantId = "46QfIZh9KGe62AMDAStgnRbsK1fcX4";
        // amount se proverava tek kod banke, prilikom placanja
        Merchant merchant = merchantService.findByMerchantId(merchantId);
        // kreira se novi order, povezuje se sa ovm merchant-om i timestamp ce mu biti now
        Order order = orderService.getOne(1L);
        Long merchantOrderId = order.getMerchant_order_id();
        // kreiram novi PaymentRequestDTO koji cu da posaljem na servis banke
        PaymentRequestDTO paymentRequestDTO =
                new PaymentRequestDTO(merchant.getMerchant_id(), merchant.getPassword(), order.getAmount(),
                        merchantOrderId, order.getMerchant_order_timestamp(), merchant.getMerchant_success_url(),
                        merchant.getMerchant_failed_url(), merchant.getMerchant_error_url());
        // saljem zahtev za dobijanje payment url i id na servis banke prodavca
        PaymentResponseInfoDTO response
                = restTemplate.postForObject(getEndpoint(),
                paymentRequestDTO, PaymentResponseInfoDTO.class);
        System.out.println(response.getPayment_url());
        return response;
    }

    private URI getEndpoint() throws URISyntaxException {
        return new URI(configuration.url() + EndpointConfig.BANK_PAYMENT_SERVICE_BASE_URL + "/api/payments/request");
    }
}
