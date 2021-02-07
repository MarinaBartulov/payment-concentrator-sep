import React, {useState} from "react";
import { useFormik } from 'formik';
import * as Yup from "yup";
import { authService } from "../services/authentication-service";
import DatePicker from "react-datepicker";
import 'react-datepicker/dist/react-datepicker.css';
import { useParams } from "react-router-dom";
import {Button} from "react-bootstrap";
import { toast } from "react-toastify";

const ClientForm = () => {

    const { paymentId } = useParams();

    const handleSubmit = async (payload, paymentId) => {
        try {
            const response = await authService.authenticate(payload, paymentId);
            toast.success("Your payment ended successfully.", {
                hideProgressBar: true,
              });
            window.location.replace(response.redirectionURL);
        } catch (error) {
            if (error.response) {
                console.log("Error: " + JSON.stringify(error.response));
            }
            toast.error(error.response ? error.response.data.responseMessage : error.message, {
                hideProgressBar: true,
            });
            window.location.replace(error.response.data.redirectionURL);
        }
    };

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
            let expirationDate = new Intl.DateTimeFormat("en-GB", {
                year: "numeric",
                month: "numeric"
              }).format(startDate);
            let payload = {...values, expirationDate};
            handleSubmit(payload, paymentId);
        },
      });

      const [startDate, setStartDate] = useState(new Date());


  return (
    <div 
        style={{
            height: "100%",
            margin: "auto",
            paddingTop: "10%",
            backgroundColor: "#999999",
          }}>
        <div 
            style={{
                margin: "auto",
                width: "60%",
                border: "5px solid #ff9933",
                paddingBottom: "2%",
            }}>
        <h3> Card authentication for online payment</h3>
        <form onSubmit={formik.handleSubmit}>
            <div className="form-group">
                <label htmlFor="pan">PAN:</label>
                <input
                    id="pan"
                    className="form-control"
                    name="pan"
                    type="text"
                    onChange={formik.handleChange}
                    value={formik.values.pan}
                    style={{margin:"auto", width:"200px"}}
                />
                {formik.touched.pan && formik.errors.pan ? (
                    <div style={{color:"red"}}>{formik.errors.pan}</div>
                ) : null}
            </div>
            <div className="form-group">
                <label htmlFor="securityNumber">Card Security Number:</label>
                <input
                    id="securityNumber"
                    className="form-control"
                    name="securityNumber"
                    type="password"
                    onChange={formik.handleChange}
                    value={formik.values.securityNumber}
                    style={{margin:"auto", width:"200px"}}
                />
                {formik.touched.securityNumber && formik.errors.securityNumber ? (
                    <div style={{color:"red"}}>{formik.errors.securityNumber}</div>
                ) : null}
            </div>
            <div className="form-group">
                <label htmlFor="cardHolderName">Card Holder Name:</label>
                <input
                    id="cardHolderName"
                    className="form-control"
                    name="cardHolderName"
                    type="text"
                    onChange={formik.handleChange}
                    value={formik.values.cardHolderName}
                    style={{margin:"auto", width:"200px"}}
                />
                {formik.touched.cardHolderName && formik.errors.cardHolderName ? (
                    <div style={{color:"red"}}>{formik.errors.cardHolderName}</div>
                ) : null}
            </div>
            <div className="form-group">
                <label htmlFor="expirationDate">Expiration Date:</label>
                <div>
                    <DatePicker
                        className="form-control"
                        type="input"
                        selected={startDate}
                        onChange={date => setStartDate(date)}
                        dateFormat="MM/yyyy"
                        minDate={new Date()}
                        showMonthYearPicker
                    />
                </div>
            </div>
            <div>
                <Button 
                    variant="success" 
                    type="submit"
                    style={{width:"100px"}}
                >Submit</Button>{' '}
            </div>
     </form>
     </div>
    </div>
  );
};

export default ClientForm;
