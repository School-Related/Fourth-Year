import os
import cv2
import numpy as np
import dlib
import face_recognition
from deepface import DeepFace
from facenet_pytorch import InceptionResnetV1
from mtcnn import MTCNN
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.metrics import accuracy_score

# Paths
TRAIN_DIR = "train_db"
TEST_DIR = "test_db"

# Store embeddings
dlib_encodings = {}
facenet_encodings = {}
lbph_recognizer = cv2.face.LBPHFaceRecognizer_create()

# ---------- 1. Load Training Data ----------

def load_images_from_folder(folder):
    images = []
    labels = []
    label_dict = {}
    label_count = 0

    for person in os.listdir(folder):
        person_path = os.path.join(folder, person)
        if os.path.isdir(person_path):
            if person not in label_dict:
                label_dict[person] = label_count
                label_count += 1

            for image_name in os.listdir(person_path):
                img_path = os.path.join(person_path, image_name)
                img = cv2.imread(img_path)
                if img is not None:
                    images.append(img)
                    labels.append(label_dict[person])

    return images, labels, label_dict

train_images, train_labels, label_map = load_images_from_folder(TRAIN_DIR)

# ---------- 2. Train Dlib & FaceNet Embeddings ----------

def get_dlib_embedding(image):
    rgb_img = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    faces = face_recognition.face_encodings(rgb_img)
    return faces[0] if faces else None

def get_facenet_embedding(image):
    mtcnn = MTCNN()
    resnet = InceptionResnetV1(pretrained="vggface2").eval()
    face = mtcnn(image)
    if face is not None:
        face = face.permute(1, 2, 0).numpy()
        face = cv2.resize(face, (160, 160))
        face = np.transpose(face, (2, 0, 1)) / 255.0
        embedding = resnet(torch.tensor(face).unsqueeze(0).float()).detach().numpy()
        return embedding[0]
    return None

for img, label in zip(train_images, train_labels):
    person_name = list(label_map.keys())[list(label_map.values()).index(label)]
    
    dlib_embed = get_dlib_embedding(img)
    if dlib_embed is not None:
        dlib_encodings[person_name] = dlib_embed

    facenet_embed = get_facenet_embedding(img)
    if facenet_embed is not None:
        facenet_encodings[person_name] = facenet_embed

# ---------- 3. Train LBPH ----------

gray_images = [cv2.cvtColor(img, cv2.COLOR_BGR2GRAY) for img in train_images]
lbph_recognizer.train(gray_images, np.array(train_labels))

# ---------- 4. Test on Unlabeled Images ----------

def recognize_face_dlib(image):
    embedding = get_dlib_embedding(image)
    if embedding is None:
        return None
    best_match = None
    best_score = float("inf")

    for person, db_embed in dlib_encodings.items():
        score = np.linalg.norm(embedding - db_embed)
        if score < best_score:
            best_score = score
            best_match = person

    return best_match if best_score < 0.6 else "Unknown"

def recognize_face_facenet(image):
    embedding = get_facenet_embedding(image)
    if embedding is None:
        return None
    best_match = None
    best_score = -1

    for person, db_embed in facenet_encodings.items():
        score = cosine_similarity([embedding], [db_embed])[0][0]
        if score > best_score:
            best_score = score
            best_match = person

    return best_match if best_score > 0.5 else "Unknown"

def recognize_face_lbph(image):
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    label, confidence = lbph_recognizer.predict(gray)
    if confidence < 100:
        return list(label_map.keys())[list(label_map.values()).index(label)]
    return "Unknown"

test_images, _, _ = load_images_from_folder(TEST_DIR)

dlib_results = []
facenet_results = []
lbph_results = []

for test_img in test_images:
    dlib_results.append(recognize_face_dlib(test_img))
    facenet_results.append(recognize_face_facenet(test_img))
    lbph_results.append(recognize_face_lbph(test_img))

# ---------- 5. Compare Results ----------

ground_truth = [folder for folder in os.listdir(TEST_DIR) if os.path.isdir(os.path.join(TEST_DIR, folder))]

dlib_accuracy = accuracy_score(ground_truth, dlib_results)
facenet_accuracy = accuracy_score(ground_truth, facenet_results)
lbph_accuracy = accuracy_score(ground_truth, lbph_results)

print(f"Dlib Accuracy: {dlib_accuracy:.2f}")
print(f"FaceNet Accuracy: {facenet_accuracy:.2f}")
print(f"LBPH Accuracy: {lbph_accuracy:.2f}")
