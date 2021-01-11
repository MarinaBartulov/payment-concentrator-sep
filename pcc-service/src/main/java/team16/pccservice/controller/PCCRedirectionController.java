package team16.pccservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team16.pccservice.dto.PCCRequestDTO;
import team16.pccservice.dto.PCCResponseDTO;
import team16.pccservice.service.PCCService;

@RestController
@RequestMapping(value = "/api")
public class PCCRedirectionController {

    @Autowired
    private PCCService pccService;

    Logger logger = LoggerFactory.getLogger(PCCRedirectionController.class);

    @PostMapping(value = "/redirect", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PCCResponseDTO> findBankAndRedirect(@RequestBody PCCRequestDTO dto) {
        try {
            PCCResponseDTO response = pccService.cratePaymentRequest(dto);
            logger.info("Transaction from Issuer Bank successfully completed. Sending redirection URL");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch(Exception e) {

            logger.error("Exception while completing Payment Request");
            logger.error(e.getMessage());

            return new ResponseEntity<>(pccService.makeFailureResponse(dto.getMerchantOrderId()), HttpStatus.OK);
        }
    }
}
