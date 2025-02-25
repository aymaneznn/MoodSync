import axios from 'axios';

const API_URL = 'http://localhost:8080/api/emotion/analyze'; // Remplace par l'URL de ton backend

export default {
  /**
   * Envoie un texte au backend pour analyse émotionnelle
   * @param {string} text - Le texte à analyser
   * @returns {Promise<Object>} - La réponse contenant l'émotion, la suggestion et les films
   */
  analyzeText(text) {
    return axios.post(API_URL, text, {
      headers: {
        'Content-Type': 'text/plain'
      }
    }).then(response => response.data)
      .catch(error => {
        console.error('Erreur lors de l\'analyse émotionnelle:', error);
        throw error;
      });
  }
};
