package team16.bitcoinservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import team16.bitcoinservice.dto.PaymentRequestDTO;
import team16.bitcoinservice.dto.PaymentResponseDTO;

@RestController
@RequestMapping("/")
public class BitcoinPaymentController {



    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/createOrder")
    public ResponseEntity createOrder(){

        PaymentRequestDTO requestDTO = new PaymentRequestDTO("1",12321.9,"EUR","BTC","https://localhost:8762/api/bitcoin/callback",
                "https://localhost:8762/api/bitcoin/cancel","https://localhost:8762/api/bitcoin/success","asdasdasdasd");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + "jMdDBQMucnx-2wcbHfys6YtxA22yxQBzKVQzkVcv");
        HttpEntity<PaymentRequestDTO> request = new HttpEntity<>(requestDTO, headers);

        ResponseEntity<PaymentResponseDTO> response = null;
        response = restTemplate.exchange("https://api-sandbox.coingate.com/v2/orders/", HttpMethod.POST, request, PaymentResponseDTO.class);

        return ResponseEntity.ok().build();
    }
}
