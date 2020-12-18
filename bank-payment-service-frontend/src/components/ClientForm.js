import React, {useState} from "react";
import { useFormik } from 'formik';
import * as Yup from "yup";
import { authService } from "../services/authentication-service";
import DatePicker from "react-datepicker";

const ClientForm = () => {

    const formik = useFormik({
        initialValues: {
            pan: '',
            securityNumber: '',
            cardHolderName: ''
        },
        validationSchema: Yup.object().shape({
            pan: Yup.string()
              .length(16, "PAN should be 16 numbers long.")
              .required("PAN is required."),
            securityNumber: Yup.string()
              .length(3, "Security number must be 3 numbers long.")
              .required("Security number is required."),
            cardHolderName: Yup.string()
              .required("Card holder name is required."),
        }),
        onSubmit: values => {
            let date = new Intl.DateTimeFormat("en-GB", {
                year: "numeric",
                month: "numeric"
              }).format(startDate);
            let payload = {...values, date};
            const promise = authService.authenticate(payload);
                promise.then((res) => {
                    console.log(res);
            });
        },
      });

      const [startDate, setStartDate] = useState(new Date());


  return (
    <div className="formDiv">
        <form onSubmit={formik.handleSubmit}>
            <div className="formFieldDiv">
            <label className="formDivLabel" htmlFor="pan">PAN:</label>
            </div>
            <div className="formInputField">
            <input
                id="pan"
                name="pan"
                type="text"
                onChange={formik.handleChange}
                value={formik.values.pan}
            />
            {formik.touched.pan && formik.errors.pan ? (
                <div style={{color:"red"}}>{formik.errors.pan}</div>
            ) : null}
            </div>
            <div className="formFieldDiv">
            <label className="formDivLabel" htmlFor="securityNumber">Card Security Number:</label>
            </div>
            <div className="formInputField">
            <input
                id="securityNumber"
                name="securityNumber"
                type="password"
                onChange={formik.handleChange}
                value={formik.values.securityNumber}
            />
            {formik.touched.securityNumber && formik.errors.securityNumber ? (
                <div style={{color:"red"}}>{formik.errors.securityNumber}</div>
            ) : null}
            </div>
            <div className="formFieldDiv">
            <label className="formDivLabel" htmlFor="cardHolderName">Card Holder Name:</label>
            </div>
            <div className="formInputField">
            <input
                id="cardHolderName"
                name="cardHolderName"
                type="text"
                onChange={formik.handleChange}
                value={formik.values.cardHolderName}
            />
            {formik.touched.cardHolderName && formik.errors.cardHolderName ? (
                <div style={{color:"red"}}>{formik.errors.cardHolderName}</div>
            ) : null}
            </div>
            <div className="formFieldDiv">
            <label className="formDivLabel" htmlFor="expirationDate">Expiration Date:</label>
            </div>
            <div className="formInputFieldDatePicker">
            <DatePicker
                className="datePicker"
                selected={startDate}
                onChange={date => setStartDate(date)}
                dateFormat="MM/yyyy"
                minDate={new Date()}
                showMonthYearPicker
            />
            </div>
            <div className="formFieldDiv">
                <button type="submit">Submit</button>
            </div>
     </form>
    </div>
  );
};

export default ClientForm;
