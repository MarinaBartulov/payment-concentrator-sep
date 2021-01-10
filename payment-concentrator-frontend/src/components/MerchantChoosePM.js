import React, { useState, useEffect } from "react";
import Header from "./Header";
import { paymentMethodService } from "../services/payment-method-service";
import { merchantService } from "../services/merchant-service";
import { toast } from "react-toastify";
import { useHistory } from "react-router-dom";

const MerchantChoosePM = () => {
  const [step, setStep] = useState(0);
  const [steps, setSteps] = useState([]);
  const [chosenPM, setChosenPM] = useState(false);
  const [formsData, setFormsData] = useState([]);
  const [formValues, setFormValues] = useState({});
  const [showSuccessMsg, setShowSuccessMsg] = useState(false);
  const history = useHistory();

  const getFormsDataForAvailablePaymentMethods = async () => {
    const response = await merchantService.getMyInfo();
    if (response.pmChosen) {
      history.push("/merchantChangePM");
    } else {
      try {
        const response = await paymentMethodService.getFormsDataForAvailablePaymentMethods();
        setFormsData(response);
        let tempArr = [];
        tempArr[0] = true;
        for (let i = 1; i < formsData.length; i++) {
          tempArr[i] = false;
        }
        setSteps(tempArr);
        initValues(response);
      } catch (error) {
        if (error.response) {
          console.log("Error: " + JSON.stringify(error.response));
        }
        toast.error(error.response ? error.response.data : error.message, {
          hideProgressBar: true,
        });
      }
    }
  };

  useEffect(() => {
    getFormsDataForAvailablePaymentMethods();
  }, []);

  //inicijalizacija polja svih formi
  const initValues = (formsData) => {
    let tempFormValues = {};
    for (let i = 0; i < formsData.length; i++) {
      tempFormValues[`${formsData[i].paymentMethodName}`] = {};
      for (let j = 0; j < formsData[i].formFields.length; j++) {
        let formField = formsData[i].formFields[j];
        if (
          formField.type === "text" ||
          formField.type === "email" ||
          formField.type === "password"
        ) {
          tempFormValues[formsData[i].paymentMethodName][`${formField.name}`] =
            "";
        } else if (formField.type === "number") {
          tempFormValues[formsData[i].paymentMethodName][
            `${formField.name}`
          ] = 0;
        } else if (formField.type === "date") {
          tempFormValues[formsData[i].paymentMethodName][
            `${formField.name}`
          ] = new Date();
        } else if (formField.type === "checkbox") {
          tempFormValues[formsData[i].paymentMethodName][
            `${formField.name}`
          ] = false;
        } else {
          tempFormValues[formsData[i].paymentMethodName][
            `${formField.name}`
          ] = null;
        }
      }
    }
    console.log(tempFormValues);
    setFormValues(tempFormValues);
  };

  const handleSubmit = async (event, paymentMethodName) => {
    event.preventDefault();
    console.log(paymentMethodName);
    console.log(formValues[paymentMethodName]);
    try {
      const response = await merchantService.addPaymentMethodForMerchant(
        paymentMethodName,
        formValues[paymentMethodName]
      );
      toast.success("You have successfully added a new payment method.", {
        hideProgressBar: true,
      });
      setChosenPM(true);
      nextStep();
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const handleChange = (e, paymentMethodName) => {
    const { id, value } = e.target;
    //console.log(id);
    //console.log(value);
    //console.log(paymentMethodName);
    let tempFormValues = formValues;
    tempFormValues[paymentMethodName][id] = value;
    setFormValues(tempFormValues);
    //console.log(formValues);
  };

  const handleSkip = () => {
    nextStep();
  };

  const nextStep = () => {
    if (step === formsData.length - 1) {
      console.log("Kraj stepa");
      if (chosenPM) {
        let stepsTemp = [...steps];
        stepsTemp[step] = false;
        setSteps(stepsTemp);
        setShowSuccessMsg(true);
      } else {
        toast.error("You have to choose at least one payment method.", {
          hideProgressBar: true,
        });
        let stepsTemp = [...steps];
        stepsTemp[step] = false;
        stepsTemp[0] = true;
        setSteps(stepsTemp);
        setStep(0);
      }
    } else {
      let stepsTemp = [...steps];
      stepsTemp[step] = false;
      stepsTemp[step + 1] = true;
      setSteps(stepsTemp);
      setStep(step + 1);
      console.log(steps);
      console.log(step);
    }
  };
  return (
    <div>
      <Header />
      <h2>Choose your payment methods:</h2>
      {formsData.map((formData, index) => {
        const { paymentMethodName, formFields } = formData;
        return (
          <>
            {steps[index] && (
              <div className="pt-5">
                <div
                  className="card mr-auto ml-auto mt-5"
                  style={{
                    width: "30%",
                    backgroundColor: "#cdf2f7",
                  }}
                >
                  <h2 className="card-title">{paymentMethodName}</h2>
                  <form
                    onSubmit={(e) => handleSubmit(e, paymentMethodName)}
                    style={{ width: "90%", margin: "auto" }}
                  >
                    {formFields.map((formField) => {
                      return (
                        <div className="form-group">
                          <label htmlFor={formField.name}>
                            {formField.label}:
                          </label>
                          <input
                            className="form-control"
                            id={formField.name}
                            name={formField.name}
                            type={formField.type}
                            required={formField.required}
                            placeholder={"Enter " + formField.label}
                            onChange={(value) =>
                              handleChange(value, paymentMethodName)
                            }
                          />
                        </div>
                      );
                    })}
                    <button
                      onClick={handleSkip}
                      className="btn btn-dark mb-1 mr-1"
                      style={{ width: "6em" }}
                    >
                      Skip
                    </button>
                    <button
                      className="btn btn-dark mb-1 mr-1"
                      style={{ width: "6em" }}
                      type="submit"
                    >
                      Submit
                    </button>
                  </form>
                </div>
              </div>
            )}
          </>
        );
      })}
      {showSuccessMsg && (
        <h2 style={{ color: "green" }}>
          You have successfully chosen your payment methods.
        </h2>
      )}
    </div>
  );
};

export default MerchantChoosePM;
