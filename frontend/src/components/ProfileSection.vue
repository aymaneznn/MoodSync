<template>
    <div class="profile-section">
      <div class="section-header">
        <strong>{{ label }} :</strong> {{ value }}
        <button @click="$emit('edit')" class="edit-button">Éditer</button>
      </div>
      <div class="section-content">
        <slot></slot>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      label: String,
      value: String
    }
  };
  </script>
  
  <style scoped>
  .profile-section {
    margin-bottom: 1.5rem;
  }
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 0.5rem;
  }
  
  .edit-button {
    padding: 0.25rem 0.5rem;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 0.9rem;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }
  
  .edit-button:hover {
    background-color: #0056b3;
  }
  </style>