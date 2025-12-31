import cv2
import face_recognition
import os
import time
from datetime import datetime
import requests
import numpy as np

# Configuration
API_URL = "http://localhost:8080/api/presence-events"
KNOWN_FACES_DIR = "known_faces"
CAMERA_INDEX = 0  # Default webcam
CHECK_INTERVAL = 30 # Seconds between captures to reduce load (adjust as needed)

# Time Windows (Start, End) - 24H Format
SCHEDULE = [
    ("09:15", "11:00"), # Morning Slot
    ("11:25", "13:00"), # Mid Slot
    ("14:00", "16:30")  # Afternoon Slot
]

def load_known_faces():
    """Loads student face encodings from the known_faces directory."""
    known_encodings = []
    known_names = []
    
    if not os.path.exists(KNOWN_FACES_DIR):
        print(f"Error: Directory '{KNOWN_FACES_DIR}' not found.")
        return [], []

    print("Loading known faces...")
    for filename in os.listdir(KNOWN_FACES_DIR):
        if filename.endswith(".jpg") or filename.endswith(".png"):
            path = os.path.join(KNOWN_FACES_DIR, filename)
            try:
                image = face_recognition.load_image_file(path)
                encodings = face_recognition.face_encodings(image)
                if encodings:
                    known_encodings.append(encodings[0])
                    # Assuming filename is "studentId_name.jpg" or just "studentId.jpg"
                    # For simplicty, let's assume the filename *is* the student ID for now 
                    # OR we can parse it. Let's send the filename (stripped) as ID.
                    student_id = os.path.splitext(filename)[0]
                    known_names.append(student_id)
                    print(f"Loaded: {filename}")
                else:
                    print(f"Warning: No face found in {filename}")
            except Exception as e:
                print(f"Error loading {filename}: {e}")
                
    return known_encodings, known_names

def is_within_active_hours():
    """Checks if current time is within any active schedule window."""
    now = datetime.now()
    current_time_str = now.strftime("%H:%M")
    current_time = datetime.strptime(current_time_str, "%H:%M").time()

    for start_str, end_str in SCHEDULE:
        start_time = datetime.strptime(start_str, "%H:%M").time()
        end_time = datetime.strptime(end_str, "%H:%M").time()
        
        if start_time <= current_time <= end_time:
            return True, f"{start_str}-{end_str}"
            
    return False, None

def send_presence_event(student_id):
    """Sends presence data to the backend."""
    payload = {
        "studentId": student_id, # Ensure filename maps to ID or handle mapping here
        "cameraId": 1, # Hardcoded for this camera
        "timestamp": datetime.now().isoformat(),
        "confidenceScore": 1.0 # High confidence since we matched encoding
    }
    
    try:
        response = requests.post(API_URL, json=payload)
        if response.status_code == 200:
            print(f"Successfully sent presence for Student {student_id}")
        else:
            print(f"Failed to send presence: {response.status_code} - {response.text}")
    except Exception as e:
        print(f"Connection Error: {e}")

def main():
    known_encodings, known_names = load_known_faces()
    if not known_encodings:
        print("No known faces loaded. Exiting.")
        return

    print("Starting AI Capture Service...")
    
    # Initialize Camera
    video_capture = cv2.VideoCapture(CAMERA_INDEX)
    if not video_capture.isOpened():
        print("Error: Could not access camera.")
        return

    try:
        while True:
            active, slot = is_within_active_hours()
            
            if active:
                print(f"Status: ACTIVE ({slot}). Scanning...")
                
                # Grab a single frame
                ret, frame = video_capture.read()
                if not ret:
                    print("Error: Failed to capture frame.")
                    time.sleep(5)
                    continue

                # Resize for speed
                small_frame = cv2.resize(frame, (0, 0), fx=0.25, fy=0.25)
                # Convert BGR to RGB
                rgb_small_frame = np.ascontiguousarray(small_frame[:, :, ::-1])

                # Detect Faces
                face_locations = face_recognition.face_locations(rgb_small_frame)
                face_encodings = face_recognition.face_encodings(rgb_small_frame, face_locations)

                for face_encoding in face_encodings:
                    # Match
                    matches = face_recognition.compare_faces(known_encodings, face_encoding, tolerance=0.5)
                    
                    if True in matches:
                        first_match_index = matches.index(True)
                        student_id = known_names[first_match_index]
                        print(f"MATCH: Student {student_id}")
                        send_presence_event(student_id)
                
                # Wait before next scan to avoid flooding backend
                time.sleep(CHECK_INTERVAL) 
                
            else:
                print("Status: SLEEP (Break/Off-hours). Waiting...")
                time.sleep(60) # Check every minute
                
    except KeyboardInterrupt:
        print("\nStopping Service...")
    finally:
        video_capture.release()
        cv2.destroyAllWindows()

if __name__ == "__main__":
    main()
