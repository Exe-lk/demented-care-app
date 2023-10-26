import cv2
import time
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
from ultralytics import YOLO

# Initialize Firebase with your credentials
cred = credentials.Certificate("credentials.json")
firebase_admin.initialize_app(cred, {
  'databaseURL': 'https://mental-hospital-30f6c-default-rtdb.firebaseio.com'
})

# Load the YOLOv8 model
model = YOLO("best.pt")

# Start the webcam capture
cap = cv2.VideoCapture(0)

# Get a reference to the Firebase Realtime Database
ref = db.reference('/captures')

def send_results_to_db(results):
  """Sends the results of the YOLOv8 inference to the Firebase Realtime Database.

  Args:
    results: A list of YOLOv8 detections.
  """

  for detection in results:
    try:
      class_name = detection.names[detection.boxes.cls.item()]
    except:
      continue

    # Check if the class name is empty
    if class_name:
      detection_ref = ref.push()

      # Set the fields of the document
      detection_ref.set({
        'class_name': class_name,
      })
      
# Continuously capture frames and perform inference
while True:

  # Capture the next frame
  ret, frame = cap.read()

  # If the frame is not empty, perform inference
  if ret:

    # Perform inference on the frame
    results = model(frame)

    # Send the results to the database
    send_results_to_db(results)

    # Display the frame
    cv2.imshow('Frame', frame)

    # Wait for 1 second before capturing the next frame
    cv2.waitKey(1)

  # If the 'q' key is pressed, break out of the loop
  if cv2.waitKey(1) & 0xFF == ord('q'):
    break

# Release the webcam capture
cap.release()

# Close all open windows
cv2.destroyAllWindows()

# import cv2
# import torch
# import time

# from ultralytics import YOLO

# # Load the YOLOv8 model
# model = YOLO("best.pt")

# # Start the webcam capture
# cap = cv2.VideoCapture(0)

# # Continuously capture frames and perform inference
# while True:

#     # Capture the next frame
#     ret, frame = cap.read()

#     # If the frame is not empty, perform inference
#     if ret:

#         # Perform inference on the frame
#         results = model(frame)

#         # Skip the code that draws the bounding boxes on the frame

#         # Display the frame
#         cv2.imshow('Frame', frame)

#         # Wait for 1 second before capturing the next frame
#         cv2.waitKey(1)

#     # If the 'q' key is pressed, break out of the loop
#     if cv2.waitKey(1) & 0xFF == ord('q'):
#         break

# # Release the webcam capture
# cap.release()

# # Close all open windows
# cv2.destroyAllWindows()


# from ultralytics import YOLO

# # Load a model
# model = YOLO("best.pt") 

# # Use the model
# results = model("img2.JPG") 