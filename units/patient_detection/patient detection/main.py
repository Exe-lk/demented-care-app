import firebase_admin
from firebase_admin import credentials, db

# Initialize Firebase with your credentials
cred = credentials.Certificate('credentials.json')
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://exepatient-default-rtdb.firebaseio.com'
})

import cv2
import torch

# Load YOLOv5 model
model = torch.hub.load('yolov5', 'custom', path='best.pt', source='local')

# Load the COCO class names for reference
class_names = model.module.names if hasattr(model, 'module') else model.names

# Reference to the database root
root_ref = db.reference('/')

# OpenCV settings for webcam capture
cap = cv2.VideoCapture(0)  # 0 represents the default webcam

while cap.isOpened():
    ret, frame = cap.read()
    if not ret:
        break

    # Perform inference on the frame
    results = model(frame)

    # Get detected class IDs
    class_ids = results.pred[0][:, 5].cpu().numpy().astype(int)

    # Determine if it's a patient or normal
    is_patient = False  # Default to normal
    for class_id in class_ids:
        class_name = class_names[class_id]
        if class_name == 'patient':
            is_patient = True
            break

    # Update the Firebase database with the current result
    result_ref = root_ref.child('results').push()
    result_ref.set({'is_patient': is_patient})

    # Print the class names of detected objects
    for class_id in class_ids:
        class_name = class_names[class_id]
        print("Detected:", class_name)

    # Display the frame
    cv2.imshow('YOLOv5 Real-time Object Detection', frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()

if __name__ == '__main__':
    app.run()

# import firebase_admin
# from firebase_admin import credentials

# # Initialize Firebase with your credentials
# cred = credentials.Certificate('credentials.json')
# firebase_admin.initialize_app(cred, {
#     'databaseURL': 'https://exepatient-default-rtdb.firebaseio.com'
# })




# import cv2
# import torch

# # Load YOLOv5 model
# model = torch.hub.load('yolov5', 'custom', path='best.pt', source='local')

# # Load the COCO class names for reference
# class_names = model.module.names if hasattr(model, 'module') else model.names

# # OpenCV settings for webcam capture
# cap = cv2.VideoCapture(0)  # 0 represents the default webcam

# while cap.isOpened():
#     ret, frame = cap.read()
#     if not ret:
#         break

#     # Perform inference on the frame
#     results = model(frame)

#     # Get detected class IDs
#     class_ids = results.pred[0][:, 5].cpu().numpy().astype(int)

#     # Print the class names of detected objects
#     for class_id in class_ids:
#         class_name = class_names[class_id]
#         print("Detected:", class_name)

#     if cv2.waitKey(1) & 0xFF == ord('q'):
#         break

# cap.release()
# cv2.destroyAllWindows()