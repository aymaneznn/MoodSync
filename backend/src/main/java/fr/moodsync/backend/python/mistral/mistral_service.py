from transformers import pipeline

pipe = pipeline("text-generation", model="google/gemma-7b-it")

# Contexte de conversation
messages = [
    {"role": "user", "content": "Who are you?"},
    {"role": "assistant", "content": "I am an AI assistant created by Google. How can I help you?"},
]

# Transformer le contexte en texte brut
formatted_input = "\n".join([f"{msg['role']}: {msg['content']}" for msg in messages])

# Génération de la réponse
response = pipe(formatted_input, max_length=200, do_sample=True)

print(response[0]["generated_text"])
