package team16.paymentserviceprovider.service;

public class ValidationService {

    public boolean validateString(String data) {
        return data != null && !data.equals("");
    }

    public boolean convertToNonNegativeDouble(String data) {
        try {
            double number = Double.parseDouble(data);
            if(number < 0) {
                return false;
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
