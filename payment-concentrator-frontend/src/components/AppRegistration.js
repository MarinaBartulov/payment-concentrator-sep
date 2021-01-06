import React, { useState } from "react";
import { useFormik } from "formik";
import * as Yup from "yup";
import { appService } from "../services/app-service";
import { toast } from "react-toastify";

const AppRegistration = () => {
  const formik = useFormik({
    initialValues: {
      appName: "",
      webAddress: "",
      officialEmail: "",
    },
    validationSchema: Yup.object({
      appName: Yup.string()
        .max(20, "Must be 20 characters or less")
        .required("Required"),
      webAddress: Yup.string()
        .url("Must be in url format")
        .required("Required"),
      officialEmail: Yup.string()
        .email("Invalid email address")
        .required("Required"),
    }),
    onSubmit: async (values, { resetForm }) => {
      console.log(values);
      try {
        setShowNewApp(false);
        resetForm();
        const response = await appService.addNewApp(values);
        setNewApp(response);
        setShowNewApp(true);
        toast.success("You have successfully registered a new application.", {
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

  const [newApp, setNewApp] = useState({});
  const [showNewApp, setShowNewApp] = useState(false);

  return (
    <div>
      <div
        className="card mr-auto ml-auto mt-5 pb-2"
        style={{
          width: "30%",
          backgroundColor: "#cdf2f7",
        }}
      >
        <h2 className="card-title">Register a new application</h2>
        <form
          onSubmit={formik.handleSubmit}
          style={{ width: "90%", margin: "auto" }}
        >
          <div className="form-group">
            <label for="appName">Application name:</label>
            <input
              className="form-control"
              id="appName"
              name="appName"
              type="text"
              placeholder="Enter application name"
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.appName}
            />
            {formik.touched.appName && formik.errors.appName ? (
              <p style={{ color: "red" }}>{formik.errors.appName}</p>
            ) : null}
          </div>
          <div className="form-group">
            <label for="webAddress">Web address:</label>
            <input
              className="form-control"
              id="webAddress"
              name="webAddress"
              type="text"
              placeholder="http://www.example.com/index.html"
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.webAddress}
            />
            {formik.touched.webAddress && formik.errors.webAddress ? (
              <p style={{ color: "red" }}>{formik.errors.webAddress}</p>
            ) : null}
          </div>
          <div className="form-group">
            <label for="officialEmail">Official email:</label>
            <input
              className="form-control"
              id="officialEmail"
              name="officialEmail"
              type="email"
              placeholder="Enter official email"
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.officialEmail}
            />
            {formik.touched.officialEmail && formik.errors.officialEmail ? (
              <p style={{ color: "red" }}>{formik.errors.officialEmail}</p>
            ) : null}
          </div>
          <button type="submit" className="btn btn-primary">
            Submit
          </button>
        </form>
      </div>
      {showNewApp && (
        <div
          className="card mt-3 mr-auto ml-auto"
          style={{
            width: "40%",
            backgroundColor: "#cdf2f7",
          }}
        >
          <h3>Application ID:</h3>
          <h4>{newApp.appId}</h4>
          <h6 className="text-left ml-5">Application name: {newApp.appName}</h6>
          <h6 className="text-left ml-5">Web address: {newApp.webAddress}</h6>
          <h6 className="text-left ml-5">
            Official email: {newApp.officialEmail}
          </h6>
        </div>
      )}
    </div>
  );
};

export default AppRegistration;
