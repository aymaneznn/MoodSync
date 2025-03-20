<template>
    <div class="contact-us-container p-d-flex p-jc-center p-ai-center">
      <Card class="contact-card">
        <template #title>
          <div class="p-d-flex p-ai-center p-jc-center">
            <i class="pi pi-envelope p-mr-2"></i>
            <h2>Contact Us</h2>
          </div>
        </template>
  
        <template #content>
          <!-- Formulaire de contact -->
          <form @submit.prevent="submitForm" class="p-fluid">
            <!-- Champ Nom -->
            <div class="p-field p-mb-4">
              <label for="name">Name</label>
              <InputText id="name" v-model="form.name" placeholder="Enter your name" class="p-inputtext-lg" />
            </div>
  
            <!-- Champ E-mail -->
            <div class="p-field p-mb-4">
              <label for="email">Email</label>
              <InputText id="email" v-model="form.email" placeholder="Enter your email" class="p-inputtext-lg" />
            </div>
  
            <!-- Champ Sujet -->
            <div class="p-field p-mb-4">
              <label for="subject">Subject</label>
              <InputText id="subject" v-model="form.subject" placeholder="Enter the subject" class="p-inputtext-lg" />
            </div>
  
            <!-- Champ Message -->
            <div class="p-field p-mb-4">
              <label for="message">Message</label>
              <Textarea id="message" v-model="form.message" placeholder="Enter your message" rows="5" class="p-inputtext-lg" />
            </div>
  
            <!-- Bouton Soumettre -->
            <div class="p-d-flex p-jc-end">
              <Button type="submit" label="Submit" icon="pi pi-send" class="p-button-primary p-button-lg" :loading="loading" />
            </div>
          </form>
        </template>
      </Card>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import { useToast } from 'primevue/usetoast';
  import InputText from 'primevue/inputtext';
  import Textarea from 'primevue/textarea';
  import Button from 'primevue/button';
  import Card from 'primevue/card';
  
  const toast = useToast();
  const loading = ref(false);
  
  // Données du formulaire
  const form = ref({
    name: '',
    email: '',
    subject: '',
    message: '',
  });
  
  // Soumission du formulaire
  const submitForm = () => {
    loading.value = true;
  
    // Simuler une requête API
    setTimeout(() => {
      toast.add({
        severity: 'success',
        summary: 'Message Sent',
        detail: 'Thank you for contacting us! We will get back to you soon.',
        life: 3000,
      });
  
      // Réinitialiser le formulaire
      form.value = { name: '', email: '', subject: '', message: '' };
      loading.value = false;
    }, 1000);
  };
  </script>
  
  <style scoped>
  /* Conteneur principal */
  .contact-us-container {
    min-height: 100vh;
    background: linear-gradient(135deg, #f3f4f6, #e0e7ff);
    padding: 2rem;
  }
  
  /* Carte de contact */
  .contact-card {
    width: 100%;
    max-width: 600px;
    padding: 2rem;
    border-radius: 12px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    background: white;
  }
  
  /* Titre */
  .contact-card h2 {
    font-size: 1.75rem;
    font-weight: bold;
    color: #2c3e50;
  }
  
  /* Icône */
  .pi-envelope {
    font-size: 1.5rem;
    color: #6366f1;
  }
  
  /* Champs du formulaire */
  .p-field label {
    font-weight: bold;
    color: #374151;
    margin-bottom: 0.5rem;
  }
  
  .p-inputtext-lg {
    width: 100%;
    font-size: 1rem;
    padding: 0.75rem;
    border-radius: 8px;
    border: 1px solid #e5e7eb;
    transition: border-color 0.3s ease;
  }
  
  .p-inputtext-lg:focus {
    border-color: #6366f1;
    outline: none;
    box-shadow: 0 0 8px rgba(99, 102, 241, 0.3);
  }
  
  /* Bouton Soumettre */
  .p-button-primary {
    background-color: #6366f1;
    border: none;
    padding: 0.75rem 1.5rem;
    font-size: 1rem;
    font-weight: bold;
    color: white;
    border-radius: 8px;
    transition: background-color 0.3s ease;
  }
  
  .p-button-primary:hover {
    background-color: #4f46e5;
  }
  
  .p-button-primary:active {
    background-color: #4338ca;
  }
  </style>