<template>
  <div class="profile-container p-d-flex p-jc-center p-ai-center">
    <Toast />
    <div class="p-grid p-d-flex p-flex-column p-ai-center p-shadow-3 profile-card">
      <!-- Chargement -->
      <Skeleton v-if="loading" width="100%" height="250px" class="p-mb-3" />

      <!-- Profil Utilisateur -->
      <div v-else class="p-d-flex p-flex-column p-ai-center p-text-center p-mb-4">
        <Avatar :image="user.profilePictureUrl || 'https://via.placeholder.com/150'" size="xlarge" shape="circle" class="p-mb-3 profile-avatar" />
        <h2 class="p-mb-1 profile-name">{{ user.name }}</h2>
        <p class="p-text-secondary profile-bio">{{ user.externalAccountId || "Aucune biographie disponible." }}</p>

        <!-- Bouton Modifier (si c'est le profil de l'utilisateur connecté) -->
        <Button v-if="isCurrentUser" label="Modifier le profil" icon="pi pi-user-edit" class="p-button-rounded p-button-outlined p-mt-3 edit-profile-button" @click="goToEditProfile" />
      </div>

      <!-- Section pour créer un post -->
      <div v-if="isCurrentUser" class="new-post-section p-w-100 p-mb-4">
        <textarea v-model="newPostContent" rows="3" placeholder="What's on your mind?" class="new-post-textarea p-inputtext p-d-block p-mb-2" autoResize />
        <Button label="Post" icon="pi pi-check" @click="handleCreatePost" class="new-post-button p-button-success p-button-outlined" />
      </div>

      <!-- Liste des posts -->
      <div class="p-mt-4 p-w-100">
        <h3 class="p-mb-3 section-title">Publications</h3>
        <div v-if="posts.length === 0" class="p-text-secondary p-mt-4 no-posts-message">Aucune publication disponible.</div>
        <div v-else>
          <div v-for="post in posts" :key="post.id" class="post p-shadow-2 p-mb-4">
            <!-- Profil de l'auteur -->
            <router-link :to="`/profile/${post.user.id}`" class="author">
              <img :src="post.user.profilePictureUrl" alt="Profile Picture" class="profile-picture" />
              <span class="name">{{ post.user.name }}</span>
            </router-link>

            <!-- Contenu du post -->
            <div class="content">
              <p>{{ post.content }}</p>
              <img v-if="post.mediaUrl" :src="post.mediaUrl" alt="Post Image" class="post-image p-shadow-4" />
            </div>

            <!-- Section Like -->
            <div class="like-section">
              <button @click="toggleLike(post)" :class="{ liked: post.isLiked }" class="like-button p-button p-button-text">
                <i :class="post.isLiked ? 'pi pi-heart-fill' : 'pi pi-heart'"></i>
                {{ post.isLiked ? 'Unlike' : 'Like' }}
              </button>
              <span class="likes-count">{{ post.likesCount }} likes</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useToast } from 'primevue/usetoast';
import { getUserProfile, getUserPosts, likePost, unlikePost, createPost } from '@/service/apiService';
import Avatar from 'primevue/avatar';
import Button from 'primevue/button';
import Skeleton from 'primevue/skeleton';
import Toast from 'primevue/toast';

const route = useRoute();
const router = useRouter();
const toast = useToast();

const user = ref({});
const posts = ref([]);
const loading = ref(true);
const isCurrentUser = ref(false);
const newPostContent = ref('');

// 🔹 Charger les informations de l'utilisateur et ses posts
const fetchUserProfile = async () => {
  try {
    const userId = route.params.userId;
    const token = localStorage.getItem('token');

    if (!token) {
      console.error('Utilisateur non authentifié.');
      return;
    }

    user.value = await getUserProfile(userId, token);
    posts.value = await getUserPosts(userId, token);

    // Vérifier si l'utilisateur connecté consulte son propre profil
    const currentUser = await getUserProfile(localStorage.getItem('userId'), token);
    isCurrentUser.value = currentUser.id === userId;

    loading.value = false;
  } catch (error) {
    console.error('Erreur lors du chargement du profil utilisateur:', error);
    toast.add({ severity: 'error', summary: 'Erreur', detail: 'Impossible de charger le profil.', life: 3000 });
  }
};

// 🔹 Redirection vers la page de modification du profil
const goToEditProfile = () => {
  router.push(`/profile/${user.value.id}/edit`);
};

// 🔹 Gérer les likes
const toggleLike = async (post) => {
  try {
    if (post.isLiked) {
      await unlikePost(post.id);
      post.likesCount -= 1;
    } else {
      await likePost(post.id);
      post.likesCount += 1;
    }
    post.isLiked = !post.isLiked;
  } catch (error) {
    console.error('Erreur lors du like:', error);
  }
};

// 🔹 Créer un post
const handleCreatePost = async () => {
  if (!newPostContent.value.trim()) return;

  const newPost = {
    content: newPostContent.value,
    mediaUrl: null,
    visibility: "public",
    tags: null,
    user: { id: user.value.id },
  };

  try {
    const createdPost = await createPost(user.value.id, newPost);
    posts.value.unshift(createdPost); // Ajouter le post en haut de la liste
    newPostContent.value = ''; // Réinitialiser le champ texte
    toast.add({ severity: 'success', summary: 'Succès', detail: 'Post créé avec succès!', life: 3000 });
  } catch (error) {
    console.error('Erreur lors de la création du post:', error);
    toast.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la création du post.', life: 3000 });
  }
};

// 🔹 Charger les données utilisateur
onMounted(async () => {
  await fetchUserProfile();
});
</script>

<style scoped>
/* Conteneur principal */
.profile-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8fafc, #e0e7ff);
  padding: 2rem;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* Carte Profil */
.profile-card {
  width: 100%;
  max-width: 800px;
  background: #ffffff;
  padding: 2rem;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.profile-card:hover {
  transform: translateY(-4px);
}

.profile-avatar {
  border: 5px solid #6366f1;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.profile-avatar:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.profile-name {
  font-size: 2rem;
  font-weight: bold;
  color: #374151;
}

.profile-bio {
  font-size: 1.1rem;
  color: #6b7280;
  margin-bottom: 1rem;
}

/* Bouton Modifier le profil */
.edit-profile-button {
  background-color: #6366f1;
  color: white;
  border: none;
  border-radius: 50px;
  padding: 8px 16px;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.edit-profile-button:hover {
  background-color: #4f46e5;
  transform: scale(1.05);
}

.new-post-section {
  background-color: #ffffff;
  padding: 16px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 1.5rem;
}

.new-post-textarea {
  width: 100%;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 12px;
  font-size: 1.1rem;
  transition: border-color 0.3s ease;
}

.new-post-textarea:focus {
  border-color: #6366f1;
  outline: none;
  box-shadow: 0 0 10px rgba(99, 102, 241, 0.3);
}

.new-post-button {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 30px;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.new-post-button:hover {
  background-color: #218838;
  transform: scale(1.05);
}

/* Liste des posts */
.section-title {
  font-size: 1.6rem;
  font-weight: bold;
  color: #374151;
  margin-bottom: 1rem;
}

/* Chaque post */
.post {
  background-color: white;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.post:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

/* Profil de l'auteur */
.author {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: inherit;
  margin-bottom: 12px;
}

.profile-picture {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 12px;
  transition: transform 0.3s ease;
}

.profile-picture:hover {
  transform: scale(1.1);
}

.name {
  font-weight: bold;
  color: #374151;
  font-size: 1.1rem;
}

/* Contenu du post */
.content {
  margin-top: 12px;
}

.post-image {
  width: 100%;
  height: auto;
  border-radius: 12px;
  margin-top: 12px;
  object-fit: cover;
}

/* Section Like */
.like-section {
  display: flex;
  align-items: center;
  margin-top: 16px;
}

.like-button {
  background-color: transparent;
  color: #6366f1;
  border: none;
  cursor: pointer;
  margin-right: 12px;
  font-size: 1.1rem;
  transition: color 0.3s ease;
}

.like-button:hover {
  color: #4f46e5;
}

.like-button.liked {
  color: #dc3545;
}

.like-button.liked:hover {
  color: #c82333;
}

.likes-count {
  font-size: 1rem;
  color: #666;
}

/* Message "Aucune publication disponible" */
.no-posts-message {
  text-align: center;
  font-size: 1.2rem;
  color: #6b7280;
}

/* Media Queries */
@media (max-width: 768px) {
  .profile-card {
    padding: 1.5rem;
  }

  .profile-name {
    font-size: 1.5rem;
  }

  .profile-bio {
    font-size: 1rem;
  }

  .post {
    padding: 15px;
  }

  .post-image {
    max-width: 100%;
    height: auto;
  }

  .like-button {
    font-size: 1rem;
  }
}

/* Contenu du post */
.content {
  margin-top: 12px;
}

.post-image {
  max-width: 100%; /* L'image ne dépasse pas la largeur du conteneur */
  height: auto; /* La hauteur s'ajuste automatiquement */
  display: block; /* Centrer l'image */
  margin: 0 auto; /* Centrer l'image */
  border-radius: 8px;
  object-fit: cover; /* Remplir l'espace sans déformation */
  max-height: 500px; /* Hauteur maximale pour éviter les images trop grandes */
}
</style>