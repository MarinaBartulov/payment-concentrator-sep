import React, { useState } from "react";
import { useFormik } from "formik";
import * as Yup from "yup";
import { authService } from "../services/auth-service";
import { useHistory } from "react-router-dom";

const Login = () => {
  const formik = useFormik({
    initialValues: {
      username: "",
      password: "",
    },
    validationSchema: Yup.object({
      username: Yup.string().required("Required"),
      password: Yup.string().required("Required"),
    }),
    onSubmit: async (values, { resetForm }) => {
      console.log(values);
      try {
        setShowErrorMsg(false);
        resetForm();
        const response = await authService.login(values);
        console.log(response);
        localStorage.setItem("token", response.accessToken);
        history.push("/home");
      } catch (error) {
        if (error.response) {
          console.log("Error: " + JSON.stringify(error.response));
        }
        setErrorMsg(error.response ? error.response.data : error.message);
        setShowErrorMsg(true);
      }
    },
  });

  const [errorMsg, setErrorMsg] = useState("");
  const [showErrorMsg, setShowErrorMsg] = useState(false);
  const history = useHistory();

  return (
    <div className="pt-5">
      <div
        className="card mr-auto ml-auto mt-5"
        style={{
          width: "30%",
          backgroundColor: "#cdf2f7",
        }}
      >
        <h2 className="card-title">Login</h2>
        <form
          onSubmit={formik.handleSubmit}
          style={{ width: "90%", margin: "auto" }}
        >
          <div className="form-group">
            <label htmlFor="username">Username:</label>
            <input
              className="form-control"
              id="username"
              name="username"
              type="text"
              placeholder="Enter username"
              onChange={formik.handleChange}
              value={formik.values.username}
            />
            {formik.touched.username && formik.errors.username ? (
              <p style={{ color: "red" }}>{formik.errors.username}</p>
            ) : null}
          </div>
          <div className="form-group">
            <label htmlFor="password">Password:</label>
            <input
              className="form-control"
              id="password"
              name="password"
              type="password"
              placeholder="Enter password"
              onChange={formik.handleChange}
              value={formik.values.password}
            />
            {formik.touched.password && formik.errors.password ? (
              <p style={{ color: "red" }}>{formik.errors.password}</p>
            ) : null}
          </div>
          <button type="submit" className="btn btn-primary mb-1">
            Submit
          </button>
        </form>
      </div>
      {showErrorMsg && (
        <div
          className="card mt-3 mr-auto ml-auto"
          style={{
            width: "40%",
            backgroundColor: "#ff7e75",
          }}
        >
          <h4>{errorMsg}</h4>
        </div>
      )}
    </div>
  );
};

export default Login;
