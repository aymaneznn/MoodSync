import sys
import random
import json

from transformers import AutoModelForSequenceClassification, AutoTokenizer, pipeline
from suggestions import suggestions_by_emotion
from movie_suggestions import get_movie_suggestions
from sensitive_words_handler import contains_sensitive_words, get_support_resources

# Load the model and tokenizer from local files
model_path = "./src/main/java/fr/moodsync/backend/python/local_model"
tokenizer_path = "./src/main/java/fr/moodsync/backend/python/local_tokenizer"

try:
    model = AutoModelForSequenceClassification.from_pretrained(model_path)
    tokenizer = AutoTokenizer.from_pretrained(tokenizer_path)
    emotion_analyzer = pipeline("text-classification", model=model, tokenizer=tokenizer)
except Exception as e:
    print(json.dumps({"emotion": "error", "suggestion": f"Model loading error: {str(e)}", "movies": []}))
    sys.exit(1)

# Retrieve text from command-line arguments
if len(sys.argv) > 1:
    text = " ".join(sys.argv[1:])
else:
    print(json.dumps({"emotion": "error", "suggestion": "No text provided", "movies": []}))
    sys.exit(1)

# Check for sensitive words
if contains_sensitive_words(text):
    response = {
        "emotion": "alert",  # Emotion spéciale pour les cas sensibles
        "suggestion": get_support_resources(),  # Message de soutien
        "movies": []  # Pas de films dans ce cas
    }
else:
    # Analyse de l'émotion
    try:
        result = emotion_analyzer(text)
        detected_emotion = result[0]['label']  # Exemple : "sadness"

        # Génération d'une suggestion
        suggestion = random.choice(suggestions_by_emotion.get(detected_emotion, ["I'm here to help. Let me know if you need anything!"]))

        # Récupération des suggestions de films
        movies = get_movie_suggestions(detected_emotion)

        # Construction de la réponse JSON
        response = {
            "emotion": detected_emotion,
            "suggestion": suggestion,
            "movies": movies
        }
    except Exception as e:
        response = {
            "emotion": "error",
            "suggestion": f"Emotion analysis error: {str(e)}",
            "movies": []
        }

# Affichage du JSON pour que Java puisse le lire
print(json.dumps(response))
