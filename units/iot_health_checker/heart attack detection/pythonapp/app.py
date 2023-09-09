from flask import Flask, request, jsonify
import joblib
from flask_cors import CORS

# Load the saved model
model = joblib.load('linear_regression_model.pkl')

# Create a Flask app
app = Flask(__name__)

# Create a Flask app

# app = Flask(__name__)
# CORS(app)
# CORS(app, origins=['http://localhost:3000']

# Define a route for the prediction
@app.route('/predict', methods=['POST'])
def predict():
    try:
        # Get user input from the JSON request
        data = request.get_json()

        # Extract features from the JSON data
        features = [
            data['age'], data['sex'], data['cp'], data['trtbps'], data['chol'],
            data['fbs'], data['restecg'], data['thalachh'], data['exng'],
            data['oldpeak'], data['slp'], data['caa'], data['thall']
        ]

        # Make a prediction
        prediction = model.predict([features])

        # Get the predicted probability
        predicted_probability = prediction[0]

        # Define a threshold (50%)
        threshold = 0.5

        # Determine the prediction label
        if predicted_probability >= threshold:
            result = "The patient may have heart disease"
        else:
            result = "The patient may not have heart disease"

        # Return the prediction result as JSON
        return jsonify({"prediction": result, "probability": predicted_probability})

    except Exception as e:
        return jsonify({"error": str(e)})

if __name__ == '__main__':
    app.run(debug=True)
