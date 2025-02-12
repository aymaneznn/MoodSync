<template>
    <div class="profile-container">
      <h1 class="profile-title">Profil</h1>
  
      <!-- Nom complet -->
      <ProfileSection
        label="Nom complet"
        :value="`${profile.firstName} ${profile.lastName}`"
        @edit="editField('name')"
      >
        <div v-if="editingField === 'name'" class="edit-form">
          <input
            type="text"
            v-model="editData.firstName"
            placeholder="Prénom"
            class="form-input"
          />
          <input
            type="text"
            v-model="editData.lastName"
            placeholder="Nom"
            class="form-input"
          />
          <div class="form-actions">
            <button @click="saveField('name')" class="submit-button">Enregistrer</button>
            <button @click="cancelEdit" class="cancel-button">Annuler</button>
          </div>
        </div>
      </ProfileSection>
  
      <!-- Pseudo -->
      <ProfileSection
        label="Pseudo / Nom d’utilisateur"
        :value="profile.username"
        @edit="editField('username')"
      >
        <div v-if="editingField === 'username'" class="edit-form">
          <input
            type="text"
            v-model="editData.username"
            placeholder="Pseudo"
            class="form-input"
          />
          <div class="form-actions">
            <button @click="saveField('username')" class="submit-button">Enregistrer</button>
            <button @click="cancelEdit" class="cancel-button">Annuler</button>
          </div>
        </div>
      </ProfileSection>
  
      <!-- Date de naissance -->
      <ProfileSection
        label="Date de naissance"
        :value="profile.birthDate"
        @edit="editField('birthDate')"
      >
        <div v-if="editingField === 'birthDate'" class="edit-form">
          <input
            type="date"
            v-model="editData.birthDate"
            class="form-input"
          />
          <div class="form-actions">
            <button @click="saveField('birthDate')" class="submit-button">Enregistrer</button>
            <button @click="cancelEdit" class="cancel-button">Annuler</button>
          </div>
        </div>
      </ProfileSection>
  
      <!-- Genre -->
      <ProfileSection
        label="Genre"
        :value="profile.gender"
        @edit="editField('gender')"
      >
        <div v-if="editingField === 'gender'" class="edit-form">
          <select v-model="editData.gender" class="form-input">
            <option value="male">Homme</option>
            <option value="female">Femme</option>
            <option value="other">Autre</option>
          </select>
          <div class="form-actions">
            <button @click="saveField('gender')" class="submit-button">Enregistrer</button>
            <button @click="cancelEdit" class="cancel-button">Annuler</button>
          </div>
        </div>
      </ProfileSection>
  
      <!-- Photo de profil -->
      <ProfileSection
        label="Photo de profil"
        :value="profile.profilePicture || 'Aucune photo'"
        @edit="editField('profilePicture')"
      >
        <div v-if="editingField === 'profilePicture'" class="edit-form">
          <input
            type="file"
            @change="handleFileUpload"
            class="form-input"
          />
          <div class="form-actions">
            <button @click="saveField('profilePicture')" class="submit-button">Enregistrer</button>
            <button @click="cancelEdit" class="cancel-button">Annuler</button>
          </div>
        </div>
      </ProfileSection>
  
      <!-- Biographie -->
      <ProfileSection
        label="Biographie"
        :value="profile.bio"
        @edit="editField('bio')"
      >
        <div v-if="editingField === 'bio'" class="edit-form">
          <textarea
            v-model="editData.bio"
            placeholder="Décrivez-vous en quelques mots..."
            class="form-textarea"
          ></textarea>
          <div class="form-actions">
            <button @click="saveField('bio')" class="submit-button">Enregistrer</button>
            <button @click="cancelEdit" class="cancel-button">Annuler</button>
          </div>
        </div>
      </ProfileSection>
  
      <!-- Adresse e-mail -->
      <ProfileSection
        label="Adresse e-mail"
        :value="profile.email"
        @edit="editField('email')"
      >
        <div v-if="editingField === 'email'" class="edit-form">
          <input
            type="email"
            v-model="editData.email"
            placeholder="Email"
            class="form-input"
          />
          <div class="form-actions">
            <button @click="saveField('email')" class="submit-button">Enregistrer</button>
            <button @click="cancelEdit" class="cancel-button">Annuler</button>
          </div>
        </div>
      </ProfileSection>
  
      <!-- Numéro de téléphone -->
      <ProfileSection
        label="Numéro de téléphone"
        :value="profile.phone"
        @edit="editField('phone')"
      >
        <div v-if="editingField === 'phone'" class="edit-form">
          <input
            type="tel"
            v-model="editData.phone"
            placeholder="Numéro de téléphone"
            class="form-input"
          />
          <div class="form-actions">
            <button @click="saveField('phone')" class="submit-button">Enregistrer</button>
            <button @click="cancelEdit" class="cancel-button">Annuler</button>
          </div>
        </div>
      </ProfileSection>
    </div>
  </template>
  
  <script>
  import ProfileSection from '@/components/ProfileSection.vue'; // Créez ce composant
  
  export default {
    components: {
      ProfileSection
    },
    data() {
      return {
        profile: {
          firstName: 'IZAN',
          lastName: 'IZAN',
          username: 'IZAN',
          birthDate: 'IZANE',
          gender: 'IZANE',
          profilePicture: '',
          bio: 'IZANE',
          email: 'IZANE',
          phone: '+213'
        },
        editingField: null, // Champ en cours d'édition
        editData: {} // Données temporaires pour l'édition
      };
    },
    methods: {
      editField(field) {
        this.editingField = field;
        this.editData = { ...this.profile }; // Copie des données actuelles
      },
      cancelEdit() {
        this.editingField = null;
        this.editData = {};
      },
      saveField(field) {
        this.profile = { ...this.profile, ...this.editData }; // Mise à jour des données
        this.cancelEdit();
        console.log('Profil mis à jour :', this.profile);
        // Ajoutez ici la logique pour sauvegarder les données (API, Vuex, etc.)
      },
      handleFileUpload(event) {
        const file = event.target.files[0];
        if (file) {
          this.editData.profilePicture = URL.createObjectURL(file); // Afficher l'image temporairement
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .profile-container {
    max-width: 800px;
    margin: 0 auto;
    padding: 2rem;
    background-color: #fff;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
  
  .profile-title {
    font-size: 2rem;
    color: #333;
    margin-bottom: 1.5rem;
  }
  
  .edit-form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }
  
  .form-input,
  .form-textarea {
    padding: 0.75rem;
    border: 1px solid #ddd;
    border-radius: 8px;
    font-size: 1rem;
    color: #333;
    transition: border-color 0.3s ease;
  }
  
  .form-input:focus,
  .form-textarea:focus {
    border-color: #007bff;
    outline: none;
    box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
  }
  
  .form-textarea {
    resize: vertical;
    min-height: 120px;
  }
  
  .form-actions {
    display: flex;
    gap: 1rem;
    justify-content: flex-end;
  }
  
  .submit-button,
  .cancel-button {
    padding: 0.5rem 1rem;
    border: none;
    border-radius: 8px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }
  
  .submit-button {
    background-color: #007bff;
    color: white;
  }
  
  .submit-button:hover {
    background-color: #0056b3;
  }
  
  .cancel-button {
    background-color: #6c757d;
    color: white;
  }
  
  .cancel-button:hover {
    background-color: #5a6268;
  }
  </style>