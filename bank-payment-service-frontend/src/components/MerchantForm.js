import React, {useState} from "react";
import { useFormik } from 'formik';
import * as Yup from "yup";
import { authService } from "../services/authentication-service";
import DatePicker from "react-datepicker";
import { useParams } from "react-router-dom";
import {Button} from "react-bootstrap";

const MerchantForm = () => {

    const { metchantId } = useParams();

    const formik = useFormik({
        initialValues: {
            pan: '',
            securityNumber: ''
        },
        validationSchema: Yup.object().shape({
            pan: Yup.string()
              .length(16, "PAN should be 16 numbers long.")
              .required("PAN is required."),
            securityNumber: Yup.string()
              .length(3, "Security number must be 3 numbers long.")
              .required("Security number is required."),
        }),
        onSubmit: values => {
            let expirationDate = new Intl.DateTimeFormat("en-GB", {
                year: "numeric",
                month: "numeric"
              }).format(startDate);
            let payload = {...values, expirationDate};
            const promise = authService.authenticateMerchant(payload, metchantId);
                promise.then((res) => {
                    console.log(res);
            });
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
        <h3>E-banking registration</h3>
        <form onSubmit={formik.handleSubmit}>
            <div className="form-group">
                <label className="formDivLabel" htmlFor="pan">PAN:</label>
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
                <label className="formDivLabel" htmlFor="securityNumber">Card Security Number:</label>
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
                <label className="formDivLabel" htmlFor="expirationDate">Expiration Date:</label>
                <div>
                    <DatePicker
                        className="form-control"
                        selected={startDate}
                        onChange={date => setStartDate(date)}
                        dateFormat="MM/yyyy"
                        minDate={new Date()}
                        showMonthYearPicker
                    />
                </div>
            </div>
            <div className="formFieldDiv">
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

export default MerchantForm;
