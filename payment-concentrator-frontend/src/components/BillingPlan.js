import React from "react";
import Header from "./Header";
import { useFormik } from "formik";
import * as Yup from "yup";
import { toast } from "react-toastify";
import { merchantService } from "../services/merchant-service";

const BillingPlan = () => {
  const formik = useFormik({
    initialValues: {
      price: 0,
      discount: 0,
      cyclesNumber: 0,
      frequency: "",
      type: "",
    },
    validationSchema: Yup.object().shape({
      price: Yup.number()
        .positive("Price must be positive number")
        .required("Price is required."),
      discount: Yup.number()
        .positive("Discount must be positive number")
        .required("Discount is required."),
      cyclesNumber: Yup.number()
        .positive("Number of cycles must be positive number")
        .required("Number of cycles number is required."),
      type: Yup.string().required("Type is required"),
      frequency: Yup.string().required("Frequency is required"),
    }),
    onSubmit: async (values, { resetForm }) => {
      try {
        const response = await merchantService.createBillingPlan(values);
        console.log(response);
        resetForm();
        toast.success("Billing plan created.", {
          hideProgressBar: true,
        });
      } catch (error) {
        if (error.response) {
          console.log("Error: " + JSON.stringify(error.response));
        }
        toast.error(error.response ? error.response.data : error.message, {
          hideProgressBar: true,
        });
      }
    },
  });

  return (
    <div>
      <Header />
      <h1>Create Your Custom Billing Plan for Subscription</h1>
      <div className="pt-2">
        <div
          className="card mr-auto ml-auto mt-3"
          style={{
            width: "30%",
            backgroundColor: "#bdbbbb",
          }}
        >
          <h2 className="card-title">Create Billing Plan</h2>
          <form
            onSubmit={formik.handleSubmit}
            style={{ width: "90%", margin: "auto" }}
          >
            <div className="form-group">
              <label htmlFor="type">Billing plan type:</label>
              <select
                value={formik.values.type}
                name="type"
                className="form-control"
                onChange={formik.handleChange}
              >
                <option value="" disabled hidden>
                  Choose billing plan type
                </option>
                <option value="FIXED"> Fixed </option>
                <option value="INFINITE"> Infinite </option>
              </select>
              {formik.touched.type && formik.errors.type ? (
                <p style={{ color: "red" }}>{formik.errors.type}</p>
              ) : null}
            </div>

            <div className="form-group">
              <label htmlFor="frequency">Billing plan frequency:</label>
              <select
                value={formik.values.frequency}
                className="form-control"
                name="frequency"
                onChange={formik.handleChange}
              >
                <option value="" disabled hidden>
                  Choose billing plan frequency
                </option>
                <option value="MONTH"> Month </option>
                <option value="YEAR"> Year </option>
              </select>
              {formik.touched.frequency && formik.errors.frequency ? (
                <p style={{ color: "red" }}>{formik.errors.frequency}</p>
              ) : null}
            </div>

            <div className="form-group">
              <label htmlFor="price">Price</label>
              <input
                className="form-control"
                id="price"
                name="price"
                type="number"
                placeholder="Enter price"
                onChange={formik.handleChange}
                value={formik.values.price}
              />
              {formik.touched.price && formik.errors.price ? (
                <p style={{ color: "red" }}>{formik.errors.price}</p>
              ) : null}
            </div>
            <div className="form-group">
              <label htmlFor="discount">Discount:</label>
              <input
                className="form-control"
                id="discount"
                name="discount"
                type="number"
                placeholder="Enter discount"
                onChange={formik.handleChange}
                value={formik.values.discount}
              />
              {formik.touched.discount && formik.errors.discount ? (
                <p style={{ color: "red" }}>{formik.errors.discount}</p>
              ) : null}
            </div>
            <div className="form-group">
              <label htmlFor="cyclesNumber">Cycles number</label>
              <input
                className="form-control"
                id="cyclesNumber"
                name="cyclesNumber"
                type="number"
                placeholder="Enter number of cycles"
                onChange={formik.handleChange}
                value={formik.values.cyclesNumber}
              />
              {formik.touched.cyclesNumber && formik.errors.cyclesNumber ? (
                <p style={{ color: "red" }}>{formik.errors.cyclesNumber}</p>
              ) : null}
            </div>

            <button type="submit" className="btn btn-primary mb-1">
              Submit
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default BillingPlan;
