package team16.bankpaymentservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.dto.PCCRequestDTO;

@Service
public class IssuerService {

    Logger logger = LoggerFactory.getLogger(IssuerService.class);

    public String handlePCCPaymentRequest(PCCRequestDTO dto) {
        return "jeeeeeeeeeeeej";
    }
}
