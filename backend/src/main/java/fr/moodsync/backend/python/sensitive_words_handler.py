# Define sensitive words
sensitive_words = [
    "suicide", "kill myself", "end my life", "want to die", "depressed",
    "hopeless", "helpless", "no reason to live", "self-harm", "harm myself"
]

# Function to check for sensitive words
def contains_sensitive_words(text):
    text = text.lower()
    for word in sensitive_words:
        if word in text:
            return True
    return False

# Function to provide support resources
def get_support_resources():
    return (
        "It sounds like you're going through a tough time. Please reach out to a mental health professional or a trusted person in your life. "
        "Here are some resources that might help:\n"
        "1. Croix-Rouge écoute (association): 0 800 858 858\n"
        "2. SOS Amitié (association) 24/7: 09 72 39 40 50 or in english: 01 46 21 46 46\n"
        "3. SOS crise (association Les transmetteurs): 0800 19 00 00\n"
        "4. More specific hotlines: https://www.psycom.org/sorienter/les-lignes-decoute/\n"
    )