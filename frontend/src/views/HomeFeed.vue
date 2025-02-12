<script>
import { ref, onMounted } from 'vue';
import { getUserPosts, likePost, unlikePost } from '@/service/userService';

export default {
  setup() {
    const posts = ref([
      {
        id: 1,
        content: "new shirt, new start, let the advanture begins !",
        mediaUrl: "https://primefaces.org/cdn/primevue/images/product/blue-t-shirt.jpg",
        visibility: "public",
        tags: { mood: "happy", weather: "sunny" },
        likesCount: 0,
        isLiked: false, // Track if the current user has liked the post
        createdAt: "2024-02-01T14:30:00Z",
        updatedAt: "2024-02-01T14:30:00Z",
        user: {
          id: 3,
          name: "John Doe",
          profile_picture_url: "https://primefaces.org/cdn/primevue/images/organization/walter.jpg",
        },
      },
    ]);

    // Fetch posts
    const fetchPosts = async () => {
      try {
        const response = await getUserPosts();
        posts.value = response.data;
      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    };

    // Handle like/unlike
    const toggleLike = async (post) => {
      try {
        if (post.isLiked) {
          // Unlike the post
          await unlikePost(post.id);
          post.likesCount -= 1;
        } else {
          // Like the post
          await likePost(post.id);
          post.likesCount += 1;
        }
        post.isLiked = !post.isLiked; // Toggle the like state
      } catch (error) {
        console.error('Error toggling like:', error);
      }
    };

    onMounted(() => {
      // fetchPosts(); // Uncomment this to fetch posts from the backend
    });

    return {
      posts,
      toggleLike,
    };
  },
};
</script>

<template>
  <div class="home-feed">
    <div v-for="post in posts" :key="post.id" class="post">
      <!-- Link to the author's profile -->
      <router-link :to="`/profile/${post.user.id}`" class="author">
        <img :src="post.user.profile_picture_url" alt="Profile Picture" class="profile-picture" />
        <span class="name">{{ post.user.name }}</span>
      </router-link>
      <!-- Post content -->
      <div class="content">
        <p>{{ post.content }}</p>
        <img v-if="post.mediaUrl" :src="post.mediaUrl" alt="Post Image" class="post-image" />
      </div>
      <!-- Like button and count -->
      <div class="like-section">
        <button @click="toggleLike(post)" :class="{ liked: post.isLiked }" class="like-button">
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

.post {
  background-color: white;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
  background-color: #007bff;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 8px;
}

.like-button:hover {
  background-color: #0056b3;
}

.like-button.liked {
  background-color: #dc3545;
}

.like-button.liked:hover {
  background-color: #c82333;
}

.likes-count {
  font-size: 14px;
  color: #666;
}
</style>