<script>
import { ref, onMounted } from 'vue';
import Button from 'primevue/button';
import { getUserProfile, getUserPosts } from '@/service/userService';

export default {
  components: {
    Button,
  },
  props: {
    userId: {
      type: String,
      required: true,
    },
  },
  setup(props) {
    const user = ref({
      id: null,
      name: '',
      bio: 'Welcome to my bio !',
      profile_picture: 'https://primefaces.org/cdn/primevue/images/organization/walter.jpg',
    });
    const posts = ref([{
        id: 1,
        content: "Ceci est mon premier post !",
        mediaUrl: "https://primefaces.org/cdn/primevue/images/product/blue-t-shirt.jpg",
        visibility: "public",
        tags: { mood: "happy", weather: "sunny" },
        likesCount: 0,
        isLiked: false, // Track if the current user has liked the post
        createdAt: "2024-02-01T14:30:00Z",
        updatedAt: "2024-02-01T14:30:00Z",
      }]);
    const isCurrentUser = ref(false);


/* A DECOMMENTER après avoir lié avec le backend

    // Fetch user profile and posts
    const fetchUserProfile = async () => {
      const token = localStorage.getItem('token'); // Get token from localStorage

      if (!token) {
        console.error('No token found. User is not authenticated.');
        return;
      }

      try {
        // Fetch user data using userService
        const userResponse = await getUserProfile(token);
        user.value = userResponse;

        // Fetch user posts using userService
        const postsResponse = await getUserPosts(props.userId, token);
        posts.value = postsResponse;

        // Check if the current user is viewing their own profile
        const currentUser = JSON.parse(localStorage.getItem('user'));
        isCurrentUser.value = currentUser && currentUser.id === props.userId;
      } catch (error) {
        console.error('Error fetching user profile:', error);
      }
    };
*/
    // Format date
    const formatDate = (dateString) => {
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' });
    };

    // Redirect to Edit Profile page
    const goToEditProfile = () => {
      window.location.href = `/profile/${user.value.id}/edit`;
    };
/*
    // Fetch data when the component is mounted
    onMounted(() => {
      fetchUserProfile();
    });
*/
    return {
      user,
      posts,
      isCurrentUser,
      formatDate,
      goToEditProfile,
    };
  },
};
</script>

<template>
    <div class="profile-page">
      <!-- Profile Header -->
      <div class="profile-header bg-white p-6 rounded-lg shadow-md">
        <div class="flex items-center space-x-6">
          <!-- Profile Picture -->
          <img
            :src="user.profile_picture || 'https://via.placeholder.com/150'"
            alt="Profile Picture"
            class="w-24 h-24 rounded-full object-cover"
          />
          <!-- User Info -->
          <div>
            <h1 class="text-2xl font-bold">{{ user.name }}</h1>
            <p class="text-gray-600">{{ user.bio || "No bio available." }}</p>
          </div>
        </div>
        <!-- Edit Profile Button (for current user) -->
        <Button
          v-if="isCurrentUser"
          label="Edit Profile"
          class="mt-4 bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded"
          @click="goToEditProfile"
        />
      </div>
  
      <!-- User Posts -->
      <div class="mt-8">
        <h2 class="text-xl font-bold mb-4">Posts</h2>
        <div v-if="posts.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div v-for="post in posts" :key="post.id" class="bg-white p-4 rounded-lg shadow-md">
            <img
              v-if="post.mediaUrl"
              :src="post.mediaUrl"
              alt="Post Image"
              class="w-full h-48 object-cover rounded-lg"
            />
            <p class="mt-2">{{ post.content }}</p>
            <p class="text-sm text-gray-500 mt-2">{{ formatDate(post.created_at) }}</p>
          </div>
        </div>
        <p v-else class="text-gray-600">No posts available.</p>
      </div>
    </div>
  </template>
  
  
  <style scoped>
  .profile-page {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
  }
  </style>