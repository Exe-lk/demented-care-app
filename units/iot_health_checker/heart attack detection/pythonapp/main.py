import joblib

# Load the saved model
model = joblib.load('linear_regression_model.pkl')

# Function to get user inputs and make predictions
def predict_heart_disease():
    print("Enter patient details:")
    age = float(input("Age: "))
    sex = float(input("Sex (0 for female, 1 for male): "))
    cp = float(input("Chest Pain Type (0-3): "))
    trtbps = float(input("Resting Blood Pressure: "))
    chol = float(input("Cholesterol Level: "))
    fbs = float(input("Fasting Blood Sugar (0 for <=120 mg/dl, 1 for >120 mg/dl): "))
    restecg = float(input("Resting ECG Results (0-2): "))
    thalachh = float(input("Maximum Heart Rate Achieved: "))
    exng = float(input("Exercise-Induced Angina (0 for no, 1 for yes): "))
    oldpeak = float(input("ST Depression Induced by Exercise Relative to Rest: "))
    slp = float(input("Slope of the Peak Exercise ST Segment (0-2): "))
    caa = float(input("Number of Major Vessels Colored by Fluoroscopy (0-3): "))
    thall = float(input("Thallium Stress Test Result (0-3): "))

    # Create a feature array for prediction
    user_input = [[age, sex, cp, trtbps, chol, fbs, restecg, thalachh, exng, oldpeak, slp, caa, thall]]

    # Make a prediction
    prediction = model.predict(user_input)

    # Get the predicted probability
    predicted_probability = prediction[0]

    # Define a threshold (50%)
    threshold = 0.5

    # Check if the predicted probability is greater than or equal to the threshold
    if predicted_probability >= threshold:
        print(f"Prediction: The patient may have heart disease ({predicted_probability:.2%} probability).")
    else:
        print(f"Prediction: The patient may not have heart disease ({predicted_probability:.2%} probability).")

# Run the prediction function
predict_heart_disease()
