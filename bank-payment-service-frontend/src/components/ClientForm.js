import React from "react";
import { useFormik } from 'formik';
import * as Yup from "yup";
import { authService } from "../services/authentication-service";

const ClientForm = () => {

    const formik = useFormik({
        initialValues: {
            pan: '',
            securityNumber: '',
            cardHolderName: '',
            expirationDate: ''
        },
        validationSchema: Yup.object().shape({
            pan: Yup.string()
              .length(10, "PAN should be 10 caracters long.")
              .required("PAN is required."),
            securityNumber: Yup.string()
              .min(10, "Password must be 10 characters at minimum.")
              .required("Password is required."),
            cardHolderName: Yup.string()
              .required("Card holder name is required."),
            expirationDate: Yup.string()
              .required("Expiration date is required.")
        }),
        onSubmit: values => {
          console.log(JSON.stringify(values, null, 2));
          console.log(values);
          const promise = authService.authenticate(values);
            promise.then((res) => {
                console.log(res);
            });
        },
      });


  return (
    <div className="formDiv">
        <form onSubmit={formik.handleSubmit}>
            <div className="formFieldDiv">
            <label className="formDivLabel" htmlFor="pan">PAN:</label>
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
            <label className="formDivLabel" htmlFor="securityNumber">Security Number</label>
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
            <label className="formDivLabel" htmlFor="cardHolderName">Card Holder Name</label>
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
            <input
                id="expirationDate"
                name="expirationDate"
                type="date"
                onChange={formik.handleChange}
                value={formik.values.expirationDate}
            />
            {formik.touched.expirationDate && formik.errors.expirationDate ? (
                <div style={{color:"red"}}>{formik.errors.expirationDate}</div>
            ) : null}
            </div>
            <div className="formFieldDiv">
                <button type="submit">Submit</button>
            </div>
     </form>
    </div>
  );
};

export default ClientForm;
