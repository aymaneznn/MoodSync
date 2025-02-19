from transformers import AutoModelForSequenceClassification, AutoTokenizer, pipeline
import random
from suggestions import suggestions_by_emotion
from movie_suggestions import get_movie_suggestions
from sensitive_words_handler import contains_sensitive_words, get_support_resources  # Import the new functions

# Load the model and tokenizer from local files
model = AutoModelForSequenceClassification.from_pretrained("./local_model")
tokenizer = AutoTokenizer.from_pretrained("./local_tokenizer")

# Create a pipeline for emotion analysis
emotion_analyzer = pipeline("text-classification", model=model, tokenizer=tokenizer)

# Analyze text
text = "I feel happy maybe I'll go the cinema today!"
print("User Input:", text)

# Check for sensitive words
if contains_sensitive_words(text):
    print(get_support_resources())
else:
    # Proceed with emotion analysis
    result = emotion_analyzer(text)
    print("Analysis Result:", result)  # Output: [{'label': 'sadness', 'score': 0.95}]

    # Extract the detected emotion
    detected_emotion = result[0]['label']  # e.g., "sadness"

    # Generate a suggestion
    suggestion = random.choice(suggestions_by_emotion.get(detected_emotion, ["I'm here to help. Let me know if you need anything!"]))
    print("Suggestion:", suggestion)

    # Get movie suggestions
    movies = get_movie_suggestions(detected_emotion)
    if movies:
        print("\nHere are some movie suggestions for you:")
        for movie in movies:
            print(f"Title: {movie['title']}, Overview: {movie['overview']}")
    else:
        print("No movie suggestions found.")