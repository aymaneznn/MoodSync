import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

// Fonction pour la connexion
export const login = async (credentials) => {
    const response = await axios.post(`${API_URL}/login`, credentials);
    return response.data; // Contient le token
};

// Fonction pour l'inscription
export const signup = async (userData) => {
    const response = await axios.post(`${API_URL}/register`, userData);
    return response.data; // Contient le token
};

// Fonction pour vérifier si l'utilisateur est connecté
export const isAuthenticated = () => {
    return !!localStorage.getItem('token'); // Vérifie si le token est stocké
};

// Fonction pour déconnecter l'utilisateur
export const logout = () => {
    console.log('Déconnexion' + localStorage.getItem('token'));
    localStorage.removeItem('token');
};
