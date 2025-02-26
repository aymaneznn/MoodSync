import axios from 'axios';

const API_URL = 'http://localhost:8080/api/emotion/analyze';
const token = localStorage.getItem("token"); // Récupère le token stocké

export default {
  /**
   * Envoie un texte au backend pour analyse émotionnelle
   * @param {string} text - Le texte à analyser
   * @returns {Promise<Object>} - La réponse contenant l'émotion, la suggestion et les films
   */
  analyzeText(text) {
    return axios.post(API_URL, { text: text }, {
      headers: {
        Authorization: `Bearer ${token}`, // Utiliser le format "Bearer <token>"
        'Content-Type': 'application/json'
      }
    })
    .then(response => response.data)
    .catch(error => {
      console.error('Erreur lors de l\'analyse émotionnelle:', error);
      throw error;
    });
  }
};
