<script>
import { ref, onMounted } from 'vue';
import { getAllPost, likePost, unlikePost, createPost, addCommentToPost, getCommentsByPostId } from '@/service/apiService';

export default {
    setup() {
        const posts = ref([]);
        const newPostContent = ref(''); // Contenu du nouveau post
        const userId = localStorage.getItem('userId'); // Récupération de l'ID utilisateur
        const newComments = ref({}); // Pour stocker les nouveaux commentaires

        const fetchPosts = async () => {
            try {
                const response = await getAllPost();
                posts.value = response.map((post) => ({
                    ...post,
                    comments: post.comments ? post.comments : [], // Assure que comments est toujours un tableau
                    showAllComments: false
                }));
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
                visibility: 'public',
                tags: null,
                user: { id: userId }
            };

            try {
                const createdPost = await createPost(userId, newPost);
                posts.value.unshift(createdPost); // Ajouter le post en haut de la liste
                newPostContent.value = ''; // Réinitialiser le champ texte
            } catch (error) {
                console.error('Error creating post:', error);
            }
        };

        // Gestion des commentaires
        const handleAddComment = async (postId) => {
            if (!newComments.value[postId]?.trim()) return;

            let comments = await getCommentsByPostId(postId);

            if (comments.length > 0) {
                comments.push({
                    user: newComments.value[postId]
                });
            } else {
                comments = [
                    {
                        user: newComments.value[postId]
                    }
                ];
            }
            try {
                const response = await addCommentToPost(postId, comments);
                // Mettre à jour le post avec le nouveau commentaire
                const postIndex = posts.value.findIndex((p) => p.id === postId);
                if (postIndex !== -1) {
                    if (!posts.value[postIndex].comments) {
                        posts.value[postIndex].comments = [];
                    }
                    if (comments.length > 0) {
                        posts.value[postIndex].comments.push({ user: newComments.value[postId] });
                        console.log('responsehdeio');
                    } else {
                        posts.value[postIndex].comments = comments;
                        console.log('null');
                    }
                    console.log('response', newComments.value[postId]);
                }
                // Réinitialiser le champ de commentaire
                newComments.value[postId] = '';
                console.log(posts.value[postIndex].comments);
            } catch (error) {
                console.error('Error adding comment:', error);
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
            newComments,
            handleAddComment
        };
    }
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

            <!-- Section Commentaires -->
            <div class="comments-section">
                <div class="comments-list" v-if="post.comments && post.comments.length > 0">
                    <div v-for="comment in post.comments" class="comment"><strong>User </strong>: {{ comment.user }}</div>
                </div>
                <div class="add-comment">
                    <textarea v-model="newComments[post.id]" placeholder="Ajouter un commentaire..." class="p-inputtext p-d-block" rows="2"></textarea>
                    <Button label="Commenter" icon="pi pi-comment" @click="handleAddComment(post.id)" class="p-button-outlined p-button-secondary" />
                </div>
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

.comments-section {
    margin-top: 16px;
    border-top: 1px solid #eee;
    padding-top: 16px;
}

.comments-list {
    margin-bottom: 12px;
}

.comment {
    padding: 8px;
    background-color: #f8f9fa;
    border-radius: 4px;
    margin-bottom: 8px;
}

.add-comment {
    display: flex;
    gap: 8px;
    align-items: flex-start;
}

.add-comment textarea {
    flex-grow: 1;
    resize: none;
    margin-bottom: 8px;
}

.add-comment button {
    margin-top: 4px;
}
</style>
