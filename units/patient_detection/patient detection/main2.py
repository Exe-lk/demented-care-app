import cv2
import torch
from pathlib import Path

import firebase_admin
from firebase_admin import credentials, db

# Initialize Firebase with your credentials
cred = credentials.Certificate('a2-demented-care-firebase-adminsdk-lx569-7db497dbdb.json')

firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://a2-demented-care-default-rtdb.firebaseio.com/'
})

# Reference to the database root
root_ref = db.reference('/AI')

# Load YOLOv5 model
model = torch.hub.load('yolov5', 'custom', path='best.pt', source='local')

# Set the model to evaluation mode
model.eval()

# Confidence threshold
confidence_threshold = 0.5

# Open the camera (you can specify the camera index or video file path)
cap = cv2.VideoCapture(0)  # 0 for the default camera

while True:
    ret, frame = cap.read()
    if not ret:
        break

    # Perform object detection
    results = model(frame)

    # Filter detections above the confidence threshold
    detections = results.pred[0][results.pred[0][:, 4] > confidence_threshold]

    

    # Draw bounding boxes on the frame
    for detection in detections:
        xyxy = detection[:4].cpu().numpy()
        label = int(detection[5])
        confidence = detection[4]

        label_txt = "Normal Person"
        if label == 1:
            label_txt = "Patient"

        color = (0, 0, 255)  # Set rectangle color to blue (BGR format)
    
        # Draw the rectangle
        frame = cv2.rectangle(frame, (int(xyxy[0]), int(xyxy[1])), (int(xyxy[2]), int(xyxy[3])), color, 2)
    
        # Get the label text size
        label_text = f'Class {label_txt}, Conf: {confidence:.2f}'
        (label_width, label_height), _ = cv2.getTextSize(label_text, cv2.FONT_HERSHEY_SIMPLEX, 0.5, 1)
    
        # Draw label background
        frame = cv2.rectangle(frame, (int(xyxy[0]), int(xyxy[1])), (int(xyxy[0]) + label_width, int(xyxy[1]) - label_height - 10), color, -1)
    
        # Draw label text in white
        frame = cv2.putText(frame, label_text, (int(xyxy[0]), int(xyxy[1]) - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)


        is_parent = False
        if label == 1:
            is_patient = True
            # Update the Firebase database with the current result
            # result_ref = root_ref.child('AI').push()
            root_ref.update({'isDetected': is_patient})
            break


    
    

    # Render the results on the frame
    # results.render()

    # Display the frame
    cv2.imshow('YOLOv5 Real-time Object Detection', frame)

    # Exit the loop if the 'q' key is pressed
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# Release the camera and close OpenCV windows
cap.release()
cv2.destroyAllWindows()