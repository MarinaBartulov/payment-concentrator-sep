import React, { useState } from "react";
import { useFormik } from "formik";
import * as Yup from "yup";
import { authService } from "../services/auth-service";
import { merchantService } from "../services/merchant-service";
import { useHistory } from "react-router-dom";
import Header from "./Header";
import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";

const Login = () => {
  const history = useHistory();
  const [errorMsg, setErrorMsg] = useState("");
  const [showErrorMsg, setShowErrorMsg] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [errorMsgModal, setErrorMsgModal] = useState("");
  const [showErrorMsgModal, setShowErrorMsgModal] = useState(false);

  //forma za login
  const formik = useFormik({
    initialValues: {
      email: "",
      password: "",
    },
    validationSchema: Yup.object({
      email: Yup.string().email("Invalid email address").required("Required"),
      password: Yup.string().required("Required"),
    }),
    onSubmit: async (values, { resetForm }) => {
      console.log(values);
      try {
        setShowErrorMsg(false);
        resetForm();
        const response = await authService.login(values);
        console.log(response);
        sessionStorage.setItem("token", response.accessToken);
        sessionStorage.setItem("role", response.role);
        if (response.role === "ROLE_ADMIN") {
          history.push("/adminPanel");
        } else if (response.role === "ROLE_MERCHANT") {
          const response = await merchantService.getMyInfo();
          if (!response.passwordChanged) {
            setShowModal(true);
          } else {
            if (!response.pmChosen) {
              history.push("/merchantChoosePM");
            } else {
              history.push("/merchantChangePM");
            }
          }
        }
      } catch (error) {
        if (error.response) {
          console.log("Error: " + JSON.stringify(error.response));
        }
        setErrorMsg(error.response ? error.response.data : error.message);
        setShowErrorMsg(true);
      }
    },
  });

  //forma za promenu sifre
  const formik2 = useFormik({
    initialValues: {
      oldPassword: "",
      newPassword: "",
      confirmNewPassword: "",
    },
    validationSchema: Yup.object({
      oldPassword: Yup.string().required("Required"),
      newPassword: Yup.string()
        .required("Required")
        .min(10, "At least 10 charachters required"),
      confirmNewPassword: Yup.string()
        .required("Required")
        .oneOf([Yup.ref("newPassword"), null], "Passwords must match"),
    }),
    onSubmit: async (values, { resetForm }) => {
      console.log(values);
      try {
        setShowErrorMsgModal(false);
        resetForm();
        const response = await authService.changePassword(values);
        sessionStorage.setItem("token", null);
        sessionStorage.setItem("role", null);
        setShowModal(false);
        toast.success(
          "You have successfully changed your password. Now you can login again.",
          {
            hideProgressBar: true,
          }
        );
      } catch (error) {
        if (error.response) {
          console.log("Error: " + JSON.stringify(error.response));
        }
        setErrorMsgModal(error.response ? error.response.data : error.message);
        setShowErrorMsgModal(true);
      }
    },
  });

  const handleCancel = () => {
    sessionStorage.setItem("token", null);
    sessionStorage.setItem("role", null);
    setShowModal(false);
  };

  return (
    <div>
      <Header />
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
              <label htmlFor="email">Email:</label>
              <input
                className="form-control"
                id="email"
                name="email"
                type="email"
                placeholder="Enter email"
                onChange={formik.handleChange}
                value={formik.values.email}
              />
              {formik.touched.email && formik.errors.email ? (
                <p style={{ color: "red" }}>{formik.errors.email}</p>
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
      <Modal
        show={showModal}
        onHide={handleCancel}
        backdrop="static"
        keyboard={false}
        centered
      >
        <Modal.Header>
          <Modal.Title>Change your initial password</Modal.Title>
        </Modal.Header>
        <form
          onSubmit={formik2.handleSubmit}
          style={{ width: "90%", margin: "auto" }}
        >
          <Modal.Body>
            <div className="form-group">
              <label htmlFor="oldPassword">Old password:</label>
              <input
                className="form-control"
                id="oldPassword"
                name="oldPassword"
                type="password"
                placeholder="Enter old password"
                onChange={formik2.handleChange}
                value={formik2.values.oldPassword}
              />
              {formik2.touched.oldPassword && formik2.errors.oldPassword ? (
                <p style={{ color: "red" }}>{formik2.errors.oldPassword}</p>
              ) : null}
            </div>
            <div className="form-group">
              <label htmlFor="newPassword">New password:</label>
              <input
                className="form-control"
                id="newPassword"
                name="newPassword"
                type="password"
                placeholder="Enter new password"
                onChange={formik2.handleChange}
                value={formik2.values.newPassword}
              />
              {formik2.touched.newPassword && formik2.errors.newPassword ? (
                <p style={{ color: "red" }}>{formik2.errors.newPassword}</p>
              ) : null}
            </div>
            <div className="form-group">
              <label htmlFor="confirmNewPassword">Confirm new password:</label>
              <input
                className="form-control"
                id="confirmNewPassword"
                name="confirmNewPassword"
                type="password"
                placeholder="Confirm new password"
                onChange={formik2.handleChange}
                value={formik2.values.confirmNewPassword}
              />
              {formik2.touched.confirmNewPassword &&
              formik2.errors.confirmNewPassword ? (
                <p style={{ color: "red" }}>
                  {formik2.errors.confirmNewPassword}
                </p>
              ) : null}
            </div>
            {showErrorMsgModal && (
              <div
                className="card mt-3 mr-auto ml-auto"
                style={{
                  width: "40%",
                  backgroundColor: "#ff7e75",
                }}
              >
                <h4>{errorMsgModal}</h4>
              </div>
            )}
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCancel}>
              Cancel
            </Button>
            <Button variant="primary" type="submit">
              Change
            </Button>
          </Modal.Footer>
        </form>
      </Modal>
    </div>
  );
};

export default Login;
