import face_recognition
import cv2
import os
import numpy as np

def detect_and_crop_faces(input_folder, output_folder, padding=50):
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)
    
    for filename in os.listdir(input_folder):
        if filename.lower().endswith(('png', 'jpg', 'jpeg')):
            image_path = os.path.join(input_folder, filename)
            image = face_recognition.load_image_file(image_path)
            face_locations = face_recognition.face_locations(image, model='hog')  # Use 'cnn' for higher accuracy (requires GPU)
            
            for i, (top, right, bottom, left) in enumerate(face_locations):
                top = max(top - padding, 0)
                left = max(left - padding, 0)
                bottom = min(bottom + padding, image.shape[0])
                right = min(right + padding, image.shape[1])
                
                face_crop = image[top:bottom, left:right]
                face_crop = cv2.cvtColor(face_crop, cv2.COLOR_RGB2BGR)  # Convert to BGR for saving
                output_path = os.path.join(output_folder, f"{os.path.splitext(filename)[0]}_face_{i}.jpg")
                cv2.imwrite(output_path, face_crop)
                print(f"Saved cropped face: {output_path}")

input_folder = os.path.join(os.getcwd(), "input_images")  # Change this to your folder containing images
output_folder = os.path.join(os.getcwd(), "output_images") # Change this to where you want to save cropped faces

detect_and_crop_faces(input_folder, output_folder)
