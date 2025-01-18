# **MoodSync - Analyse de Sentiments et Recommandations**

## **Table des matières**
1. [Introduction](#introduction)
2. [Fonctionnalités](#fonctionnalités)
3. [Architecture](#architecture)
4. [Technologies utilisées](#technologies-utilisées)
5. [Configuration et installation](#configuration-et-installation)
6. [Utilisation](#utilisation)
7. [Sécurité et conformité](#sécurité-et-conformité)
8. [Contribution](#contribution)

---

## **Introduction**

MoodSync est une application web innovante qui analyse les sentiments des utilisateurs à partir de leurs interactions sur les réseaux sociaux, leurs préférences musicales ou des données textuelles saisies manuellement. Basée sur ces analyses, MoodSync propose des recommandations personnalisées de musiques, films, séries ou activités, contribuant ainsi à améliorer le bien-être émotionnel de ses utilisateurs.

### **Objectifs :**
- **Analyse des sentiments** : Identifier les émotions dans les interactions sociales et historiques musicaux.
- **Recommandations adaptées** : Fournir des contenus personnalisés alignés avec l'état émotionnel.
- **Respect de la vie privée** : Assurer une gestion sécurisée et conforme au RGPD des données utilisateurs.
- **Accessibilité multiplateforme** : Proposer une expérience utilisateur fluide sur mobiles, tablettes et ordinateurs.

---

## **Fonctionnalités**

1. **Comptes utilisateurs :**
   - Connexion avec Facebook, Instagram, Spotify via OAuth 2.0.
   - Mode indépendant : Saisie manuelle des pensées et des émotions.

2. **Analyse des sentiments :**
   - Traitement des données textuelles, visuelles et musicales.
   - Génération de rapports émotionnels détaillés.

3. **Recommandations personnalisées :**
   - Suggestions de musiques, films et séries en fonction des émotions.
   - Génération de musiques inédites adaptées à l'humeur.

4. **Publication utilisateur :**
   - Partage d'expériences et recommandations au sein de la communauté.

5. **Tableau de bord interactif :**
   - Résumé des analyses, gestion des préférences, et visualisation des recommandations.

---

## **Architecture**

MoodSync est basé sur une architecture **MVC (Model-View-Controller)**, combinant modularité et scalabilité.

### **Composants principaux :**
1. **Frontend :** Développé avec Vue.js, il offre une interface utilisateur responsive et interactive.
2. **Backend :** Basé sur Spring Boot, il gère les services web sécurisés et les analyses des sentiments.
3. **Base de données :** PostgreSQL pour le stockage structuré et Amazon S3 pour les fichiers multimédias.
4. **API externes :**
   - Facebook API
   - Instagram API
   - Spotify API

### **Schéma simplifié de l'architecture :**
```
[Frontend (Vue.js)] <--> [API REST (Spring Boot)] <--> [Base de données (PostgreSQL)]
                         [Services tiers (Facebook, Spotify)]
```

---

## **Technologies utilisées**

- **Frontend :**
  - Vue.js
  - Vuex pour la gestion de l’état global
  - Primevue pour le design responsive
- **Backend :**
  - Spring Boot
  - Spring Security pour l’authentification
  - JPA/Hibernate pour la gestion des données
- **Base de données :**
  - PostgreSQL
  - Amazon S3 pour le stockage multimédia
- **Outils de CI/CD :**
  - Docker

---

## **Configuration et installation**

### **Prérequis**
- **Backend** :
  - JDK 11+
  - Maven 3.6+
- **Frontend** :
  - Node.js 14+ et npm
- **Base de données** :
  - PostgreSQL
  - Amazon S3 (optionnel pour le stockage)

### **Installation**
1. Clonez le dépôt :
   ```bash
   git clone https://github.com/aymaneznn/MoodSync.git
   cd MoodSync
   ```

2. **Backend** :
   - Accédez au dossier `backend/`.
   - Configurez les fichiers `application.yml` pour vos environnements.
   - Lancez le serveur :
     ```bash
     mvn spring-boot:run
     ```

3. **Frontend** :
   - Accédez au dossier `frontend/`.
   - Installez les dépendances :
     ```bash
     npm install
     ```
   - Démarrez le serveur de développement :
     ```bash
     npm run serve
     ```

---

## **Utilisation**

1. **Créer un compte :** Inscrivez-vous ou connectez-vous via des réseaux sociaux.
2. **Analyser les émotions :** Connectez vos comptes ou saisissez vos pensées.
3. **Consulter les recommandations :** Accédez aux suggestions personnalisées dans le tableau de bord.
4. **Partager des expériences :** Publiez et explorez les expériences des autres utilisateurs.

---

## **Sécurité et conformité**

- **Chiffrement :**
  - AES-256 pour les données au repos.
  - SSL/TLS pour les données en transit.
- **Authentification :**
  - OAuth 2.0 pour les connexions externes.
  - JWT pour les sessions utilisateur.
- **Conformité RGPD :**
  - Droit à l’oubli et portabilité des données.
  - Collecte minimale des informations nécessaires.

---

## **Contribution**

1. Forkez le dépôt.
2. Créez une branche pour vos modifications :
   ```bash
   git checkout -b feature/nom-fonctionnalité
   ```
3. Proposez une pull request avec une description détaillée.

---
