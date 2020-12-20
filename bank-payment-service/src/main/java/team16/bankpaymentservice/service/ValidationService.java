package team16.bankpaymentservice.service;

import java.time.YearMonth;

public class ValidationService {

    public boolean validateString(String data) {
        return data != null && !data.equals("");
    }

    public YearMonth convertToYearMonth(String date) throws Exception {
        try {
            String[] arrOfStr = date.split("/");
            String month = arrOfStr[0];
            String year = arrOfStr[1];
            YearMonth y = YearMonth.parse(year + "-" + month);
            return y;
        } catch (Exception e) {
            throw new Exception("Invalid date form.");
        }
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
