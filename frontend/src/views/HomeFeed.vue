<script>
import { ref, onMounted } from 'vue';
import { getAllPost, likePost, unlikePost, createPost } from '@/service/apiService';

export default {
  setup() {
    const posts = ref([]);
    const newPostContent = ref(''); // Contenu du nouveau post
    const userId = localStorage.getItem('userId'); // Récupération de l'ID utilisateur

    // Fetch posts
    const fetchPosts = async () => {
      try {
        const response = await getAllPost();
        posts.value = response;
      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    };

    // Handle like/unlike
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
        console.error('Error toggling like:', error);
      }
    };

    // Handle post creation
    const handleCreatePost = async () => {
      if (!newPostContent.value.trim()) return;

      const newPost = {
        content: newPostContent.value,
        mediaUrl: null, 
        visibility: "public",
        tags: null,
        user: { id: userId },
      };

      try {
        const createdPost = await createPost(userId, newPost);
        posts.value.unshift(createdPost); // Ajouter le post en haut de la liste
        newPostContent.value = ''; // Réinitialiser le champ texte
      } catch (error) {
        console.error('Error creating post:', error);
      }
    };

    onMounted(() => {
      fetchPosts();
    });

    return {
      posts,
      toggleLike,
      newPostContent,
      handleCreatePost,
    };
  },
};
</script>


<template>
  <div class="home-feed">
    <!-- Section pour créer un post -->
    <div class="new-post-section">
      <textarea v-model="newPostContent" rows="3" placeholder="What's on your mind?" class="new-post-textarea p-inputtext p-d-block p-mb-2" autoResize />
      <Button label="Post" icon="pi pi-check" @click="handleCreatePost" class="new-post-button p-button-success p-button-outlined" />
    </div>

    <!-- Liste des posts -->
    <div v-for="post in posts" :key="post.id" class="post p-shadow-2">
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
</template>

<style scoped>
.home-feed {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.new-post-section {
  background-color: #fff;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.new-post-textarea {
  width: 100%;
  margin-bottom: 12px;
  resize: none;
}

.new-post-button {
  background-color: #28a745;
  color: white;
}

.new-post-button:hover {
  background-color: #218838;
}

.post {
  background-color: white;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
}

.author {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: inherit;
}

.profile-picture {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 12px;
}

.name {
  font-weight: bold;
}

.content {
  margin-top: 12px;
}

.post-image {
  width: 100%;
  border-radius: 8px;
  margin-top: 12px;
}

.like-section {
  display: flex;
  align-items: center;
  margin-top: 12px;
}

.like-button {
  background-color: transparent;
  color: #007bff;
  border: none;
  cursor: pointer;
  margin-right: 8px;
}

.like-button:hover {
  color: #0056b3;
}

.like-button.liked {
  color: #dc3545;
}

.like-button.liked:hover {
  color: #c82333;
}

.likes-count {
  font-size: 14px;
  color: #666;
}
</style>
