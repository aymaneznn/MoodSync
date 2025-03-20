import axios from 'axios';

// Base URL for my backend API
const API_BASE_URL = 'http://localhost:8080';

// Axios instance with default configuration
const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});

// Function to handle user signup
export const signup = async (userData) => {
    try {
        const response = await apiClient.post('/users', userData);
        return response.data; // Return the response data (e.g., user details, token)
    } catch (error) {
        console.error('Error during signup:', error.response?.data || error.message);
        throw error; // Re-throw the error to handle it in the component
    }
};

// Function to handle user login
export const login = async (credentials) => {
    try {
        const response = await apiClient.post('/auth/login', credentials);
        return response.data; // Return the response data (e.g., user details, token)
    } catch (error) {
        console.error('Error during login:', error.response?.data || error.message);
        throw error; // Re-throw the error to handle it in the component
    }
};

// Function to handle Google OAuth login/signup
export const googleAuth = async (code) => {
    try {
        const response = await apiClient.post('/auth/google', { code });
        return response.data; // Return the response data (e.g., user details, token)
    } catch (error) {
        console.error('Error during Google OAuth:', error.response?.data || error.message);
        throw error; // Re-throw the error to handle it in the component
    }
};

// Function to fetch user profile (protected route)
export const getUserProfile = async (userId, token) => {
    try {
        const response = await apiClient.get(`/users/${userId}`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        return response.data; // Return the user profile data
    } catch (error) {
        console.error('Error fetching user profile:', error.response?.data || error.message);
        throw error; // Re-throw the error to handle it in the component
    }
};

// Function to fetch user profile (protected route)
export const getAllPost = async () => {
    try {
        const response = await apiClient.get('/api/posts', {
        });
        return response.data; // Return the user profile data
    } catch (error) {
        console.error('Error fetching posts:', error.response?.data || error.message);
        throw error; // Re-throw the error to handle it in the component
    }
};

// Function to fetch user posts (protected route)
export const getUserPosts = async (userId) => {
    try {
        const response = await apiClient.get(`/api/posts/user/${userId}`, {
        });
        return response.data; // Return the user posts data
    } catch (error) {
        console.error('Error fetching user posts:', error.response?.data || error.message);
        throw error; // Re-throw the error to handle it in the component
    }
};

// Function to like a post
export const likePost = async (postId) => {
    try {
      const token = localStorage.getItem('token');
      const response = await apiClient.post(`/api/posts/like-post/${postId}`, null, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response.data;
    } catch (error) {
      console.error('Error liking post:', error.response?.data || error.message);
      throw error;
    }
  };
  
  // Function to unlike a post
  export const unlikePost = async (postId) => {
    try {
      const token = localStorage.getItem('token');
      const response = await apiClient.post(`/api/posts/unlike-post/${postId}`, null, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response.data;
    } catch (error) {
      console.error('Error unliking post:', error.response?.data || error.message);
      throw error;
    }
  };

  // Function to create a new post
export const createPost = async (userId, postData) => {
    try {
        const token = localStorage.getItem('token'); // Récupération du token
        const response = await apiClient.post(`/api/posts/user/${userId}`, postData, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data; // Retourne la réponse du backend
    } catch (error) {
        console.error('Error creating post:', error.response?.data || error.message);
        throw error;
    }
};

// Function to fetch recommendations for a user
export const getUserRecommendations = async (userId) => {
    try {
        const token = localStorage.getItem('token'); // Récupérer le token d'authentification
        const response = await apiClient.get(`/api/recommendations/user/${userId}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data; // Retourne les recommandations de l'utilisateur
    } catch (error) {
        console.error('Error fetching recommendations:', error.response?.data || error.message);
        throw error;
    }
};

// Function to add a new recommendation
export const addRecommendation = async (recommendationData) => {
    try {
        const token = localStorage.getItem('token'); // Récupérer le token d'authentification
        const response = await apiClient.post('/api/recommendations/add', recommendationData, {
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        });
        return response.data; // Retourne la recommandation ajoutée
    } catch (error) {
        console.error('Error adding recommendation:', error.response?.data || error.message);
        throw error;
    }
};

// Function to delete a recommendation
export const deleteRecommendation = async (recommendationId) => {
    try {
        const token = localStorage.getItem('token'); // Récupérer le token d'authentification
        const response = await apiClient.delete(`/api/recommendations/delete/${recommendationId}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data; // Retourne la réponse de suppression
    } catch (error) {
        console.error('Error deleting recommendation:', error.response?.data || error.message);
        throw error;
    }
};

export const addCommentToPost = async (postId, commentData) => {
    try {
        const token = localStorage.getItem('token');
        const response = await apiClient.post(`/api/posts/add-tags/${postId}`, commentData, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data;
    } catch (error) {
        console.error('Error adding comment to post:', error.response?.data || error.message);
        throw error;
    }
};

export const getCommentsByPostId = async (postId) => {
    try {
        const token = localStorage.getItem('token');
        const response = await apiClient.get(`/api/posts/comments/${postId}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching comments:', error.response?.data || error.message);
        throw error;
    }
};



// Export all functions
export default {
    signup,
    login,
    googleAuth,
    getUserProfile,
    getUserPosts,
    likePost,
    unlikePost,
    createPost,
    getUserRecommendations,
    addRecommendation,
    deleteRecommendation,
    addCommentToPost,
    getCommentsByPostId
};