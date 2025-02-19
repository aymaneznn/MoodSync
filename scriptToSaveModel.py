from transformers import AutoModelForSequenceClassification, AutoTokenizer

# Specify the model name
model_name = "bhadresh-savani/distilbert-base-uncased-emotion"

# Download and save the model and tokenizer
model = AutoModelForSequenceClassification.from_pretrained(model_name)
tokenizer = AutoTokenizer.from_pretrained(model_name)

model.save_pretrained("./local_model")
tokenizer.save_pretrained("./local_tokenizer")