import React from "react";
import { useFormik } from 'formik';

const ClientForm = () => {

    const formik = useFormik({
        initialValues: {
            pan: '',
            securityNumber: '',
            cardHolderName: '',
            expirationDate: ''
        },
        onSubmit: values => {
          alert(JSON.stringify(values, null, 2));
        },
      });


  return (
    <div className="formDiv">
        <form onSubmit={formik.handleSubmit}>
            <div>
            <label className="formDivLabel" htmlFor="pan">PAN:</label>
            <input
                id="pan"
                name="pan"
                type="text"
                onChange={formik.handleChange}
                value={formik.values.pan}
            />
            </div>
            <div>
            <label htmlFor="securityNumber">Security Number</label>
            <input
                id="securityNumber"
                name="securityNumber"
                type="password"
                onChange={formik.handleChange}
                value={formik.values.securityNumber}
            />
            </div>
            <div>
            <label htmlFor="cardHolderName">Card Holder Name</label>
            <input
                id="cardHolderName"
                name="cardHolderName"
                type="text"
                onChange={formik.handleChange}
                value={formik.values.cardHolderName}
            />
            </div>
            <div>
            <label htmlFor="expirationDate">Expiration Date:</label>
            <input
                id="expirationDate"
                name="expirationDate"
                type="date"
                onChange={formik.handleChange}
                value={formik.values.expirationDate}
            />
            </div>
            <div>
            <button type="submit">Submit</button>
            </div>
     </form>
    </div>
  );
};

export default ClientForm;
