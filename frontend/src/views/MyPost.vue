<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getUserPosts } from '@/service/apiService';

const posts = ref([]);
const userId = localStorage.getItem('userId');

const fetchPosts = async () => {
  try {
    const response = await getUserPosts(userId);
    posts.value = response.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
  } catch (error) {
    console.error('Error fetching posts:', error);
  }
};

onMounted(() => {
  fetchPosts();
});
</script>

<template>
  <div class="my-posts">
    <h1>My Posts</h1>
    <div v-if="posts.length === 0" class="no-posts-message">No posts available.</div>
    <div v-else>
      <div v-for="post in posts" :key="post.id" class="post">
        <h2>{{ post.title }}</h2>
        <p>{{ post.content }}</p>
        <span>{{ new Date(post.createdAt).toLocaleString() }}</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.my-posts {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.no-posts-message {
  text-align: center;
  color: #888;
}

.post {
  background-color: #fff;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.post h2 {
  margin: 0 0 10px;
}

.post p {
  margin: 0 0 10px;
}

.post span {
  color: #888;
  font-size: 0.9em;
}
</style>