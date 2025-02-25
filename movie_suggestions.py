import requests

API_KEY = "c56f7c3e672b8f8a8be262965ab2d648"

def get_movie_suggestions(emotion):
    # Map emotion to genres
    emotion_to_genre = {
        "joy": ["Comedy", "Animation", "Musical"],
        "sadness": ["Drama", "Romance"],
        "anger": ["Action", "Thriller"],
        "fear": ["Horror", "Mystery"],
        "surprise": ["Adventure", "Sci-Fi"],
        "love": ["Romance", "Comedy"],
    }

    # Get genres for the detected emotion
    genres = emotion_to_genre.get(emotion, ["Drama"])  # Default to Drama if emotion not found

    # Fetch movies from TMDb
    url = f"https://api.themoviedb.org/3/discover/movie"
    params = {
        "api_key": API_KEY,
        "with_genres": ",".join([str(get_genre_id(g)) for g in genres]),  # Convert genre names to IDs
        "sort_by": "popularity.desc",  # Sort by popularity
        "page": 1,  # Get the first page of results
    }

    response = requests.get(url, params=params)
    if response.status_code == 200:
        movies = response.json().get("results", [])
        return movies[:5]  # Return top 5 movies
    else:
        print("Error fetching movies from TMDb.")
        return []

def get_genre_id(genre_name):
    # Map genre names to TMDb genre IDs
    genre_mapping = {
        "Comedy": 35,
        "Animation": 16,
        "Musical": 10402,
        "Drama": 18,
        "Romance": 10749,
        "Action": 28,
        "Thriller": 53,
        "Horror": 27,
        "Mystery": 9648,
        "Adventure": 12,
        "Sci-Fi": 878,
    }
    return genre_mapping.get(genre_name, 18)  # Default to Drama if genre not found