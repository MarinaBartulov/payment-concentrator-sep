package team16.pccservice.service;

public class ValidationService {

    public boolean validateString(String data) {
        return data != null && !data.equals("");
    }
}
