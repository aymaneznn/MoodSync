<script>
import { ref, onMounted } from 'vue';
import { getAllPost, likePost, unlikePost, createPost, addCommentToPost, getCommentsByPostId, getUserProfile } from '@/service/apiService';

export default {
    setup() {
        const posts = ref([]);
        const newPostContent = ref(''); // New post content
        const userId = localStorage.getItem('userId'); // Get user ID
        const newComments = ref({}); // To store new comments
        const expandedComments = ref({}); // To track which posts have expanded comments
        const INITIAL_COMMENTS_COUNT = 2; // Initial number of comments to display
        const userProfilePicture = ref(localStorage.getItem('profilePictureUrl')); // Get user profile picture

        const fetchUserProfile = async () => {
            try {
                const response = await getUserProfile(userId);
                console.log(response);
                userProfilePicture.value = response.profilePictureUrl;
            } catch (error) {
                console.error('Error fetching user profile:', error);
            }
        };

        onMounted(() => {
            fetchUserProfile();
            fetchPosts();
        });
        
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
                posts.value.unshift(createdPost); // Add post at the top of the list
                newPostContent.value = ''; // Reset text field
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
                // Update post with new comment
                const postIndex = posts.value.findIndex((p) => p.id === postId);
                if (postIndex !== -1) {
                    if (!posts.value[postIndex].comments) {
                        posts.value[postIndex].comments = [];
                    }
                    if (comments.length > 0) {
                        posts.value[postIndex].comments.push({ user: newComments.value[postId] });
                    } else {
                        posts.value[postIndex].comments = comments;
                    }
                }
                // Reset comment field
                newComments.value[postId] = '';
            } catch (error) {
                console.error('Error adding comment:', error);
            }
        };

        const toggleComments = (postId) => {
            expandedComments.value[postId] = !expandedComments.value[postId];
        };

        const getVisibleComments = (post) => {
            if (!post.comments) return [];
            if (expandedComments.value[post.id]) {
                return post.comments;
            }
            return post.comments.slice(0, INITIAL_COMMENTS_COUNT);
        };

        const hasMoreComments = (post) => {
            return post.comments && post.comments.length > INITIAL_COMMENTS_COUNT;
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
            handleAddComment,
            expandedComments,
            toggleComments,
            getVisibleComments,
            hasMoreComments,
            userProfilePicture
        };
    }
};
</script>

<template>
    <div class="home-feed">
        <!-- Create post section -->
        <div class="new-post-section">
            <div class="post-header">
                <img :src="userProfilePicture" alt="Profile Picture" class="profile-picture" />
                <div class="post-input-container">
                    <textarea 
                        v-model="newPostContent" 
                        placeholder="What's happening?" 
                        class="new-post-textarea" 
                        @input="autoResize"
                        maxlength="280"
                    ></textarea>
                    <div class="post-actions">
                        <div class="post-attachments">
                            <button class="attachment-button">
                                <i class="pi pi-image"></i>
                            </button>
                            <button class="attachment-button">
                                <i class="pi pi-video"></i>
                            </button>
                            <button class="attachment-button">
                                <i class="pi pi-smile"></i>
                            </button>
                        </div>
                        <div class="post-submit">
                            <span class="character-count" :class="{ 'near-limit': newPostContent.length > 250 }">
                                {{ 280 - newPostContent.length }}
                            </span>
                            <Button 
                                label="Post" 
                                @click="handleCreatePost" 
                                class="tweet-button"
                                :disabled="!newPostContent.trim()"
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Posts list -->
        <div v-for="post in posts" :key="post.id" class="post">
            <!-- Post header -->
            <div class="post-header">
                <router-link :to="`/profile/${post.user.id}`" class="author">
                    <img :src="post.user.profilePictureUrl" alt="Profile Picture" class="profile-picture" />
                    <span class="name">{{ post.user.name }}</span>
                </router-link>
            </div>

            <!-- Post image -->
            <div v-if="post.mediaUrl" class="post-image-container">
                <img :src="post.mediaUrl" alt="Post Image" class="post-image" />
            </div>

            <!-- Post content -->
            <div class="post-content">
                <div class="post-actions">
                    <div class="action-buttons">
                        <button @click="toggleLike(post)" :class="{ liked: post.isLiked }" class="action-button">
                            <i :class="post.isLiked ? 'pi pi-heart-fill' : 'pi pi-heart'"></i>
                        </button>
                        <button class="action-button">
                            <i class="pi pi-comment"></i>
                        </button>
                        <button class="action-button">
                            <i class="pi pi-share-alt"></i>
                        </button>
                    </div>
                    <button class="action-button bookmark">
                        <i class="pi pi-bookmark"></i>
                    </button>
                </div>
                <div class="likes-count">{{ post.likesCount }} likes</div>
                <div class="post-text">{{ post.content }}</div>
            </div>

            <!-- Comments section -->
            <div class="comments-section">
                <div class="comments-list" v-if="getVisibleComments(post).length > 0">
                    <div v-for="comment in getVisibleComments(post)" :key="comment.id" class="comment">
                        <div class="comment-content">
                            <span class="comment-username">{{ comment.user }}</span>
                            <span class="comment-text">{{ comment.content }}</span>
                        </div>
                    </div>
                    <div v-if="hasMoreComments(post)" class="comments-toggle" @click="toggleComments(post.id)">
                        {{ expandedComments[post.id] ? 'See less' : `View all ${post.comments.length - 2} comments` }}
                    </div>
                </div>
                <div class="add-comment">
                    <div class="comment-input-wrapper">
                        <textarea 
                            v-model="newComments[post.id]" 
                            placeholder="Add a comment..." 
                            class="comment-input"
                            rows="1"
                            @input="autoResize"
                        ></textarea>
                        <Button 
                            label="Post" 
                            @click="handleAddComment(post.id)" 
                            class="p-button-text comment-button"
                            :disabled="!newComments[post.id]?.trim()"
                        />
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.home-feed {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    background-color: #fafafa;
}

.new-post-section {
    background-color: #fff;
    padding: 16px;
    border-radius: 16px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;
    border: 1px solid #e1e8ed;
}

.post-header {
    padding: 14px;
    display: flex;
    align-items: center;
}

.post-input-container {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.new-post-textarea {
    width: 100%;
    border: none;
    outline: none;
    resize: none;
    font-size: 16px;
    line-height: 1.5;
    min-height: 100px;
    padding: 8px 0;
    color: #14171a;
}

.new-post-textarea::placeholder {
    color: #657786;
}

.post-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px solid #e1e8ed;
}

.post-attachments {
    display: flex;
    gap: 16px;
}

.attachment-button {
    background: none;
    border: none;
    padding: 8px;
    color: #1da1f2;
    cursor: pointer;
    border-radius: 50%;
    transition: background-color 0.2s;
}

.attachment-button:hover {
    background-color: rgba(29, 161, 242, 0.1);
}

.post-submit {
    display: flex;
    align-items: center;
    gap: 12px;
}

.character-count {
    font-size: 14px;
    color: #657786;
}

.character-count.near-limit {
    color: #f5a623;
}

.tweet-button {
    background-color: #1da1f2;
    color: white;
    border: none;
    border-radius: 9999px;
    padding: 8px 16px;
    font-weight: 600;
    font-size: 14px;
    transition: background-color 0.2s;
}

.tweet-button:hover:not(:disabled) {
    background-color: #1a91da;
}

.tweet-button:disabled {
    background-color: #98d7f7;
    cursor: not-allowed;
}

.profile-picture {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    margin-right: 12px;
}

.post {
    background-color: white;
    border-radius: 3px;
    margin-bottom: 24px;
    border: 1px solid #dbdbdb;
    max-width: 600px;
    margin-left: auto;
    margin-right: auto;
}

.author {
    display: flex;
    align-items: center;
    text-decoration: none;
    color: inherit;
}

.name {
    font-weight: 600;
    color: #262626;
}

.post-image-container {
    width: 100%;
    aspect-ratio: 1;
    overflow: hidden;
}

.post-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.post-content {
    padding: 12px;
}

.post-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
}

.action-buttons {
    display: flex;
    gap: 16px;
}

.action-button {
    background: none;
    border: none;
    padding: 8px;
    cursor: pointer;
    color: #262626;
    font-size: 24px;
    transition: color 0.2s;
}

.action-button:hover {
    color: #8e8e8e;
}

.action-button.liked {
    color: #ed4956;
}

.action-button.bookmark {
    margin-left: auto;
}

.likes-count {
    font-weight: 600;
    color: #262626;
    margin-bottom: 8px;
}

.post-text {
    color: #262626;
    margin-bottom: 8px;
    line-height: 1.4;
}

.comments-section {
    padding: 0 12px 12px;
    border-top: 1px solid #efefef;
}

.comment {
    margin-bottom: 8px;
    font-size: 14px;
}

.comment-content {
    display: flex;
    flex-wrap: wrap;
    gap: 4px;
}

.comment-username {
    font-weight: 600;
    color: #262626;
}

.comment-text {
    color: #262626;
}

.comments-toggle {
    color: #8e8e8e;
    font-size: 14px;
    cursor: pointer;
    margin-top: 4px;
    font-weight: 500;
}

.comments-toggle:hover {
    color: #262626;
}

.add-comment {
    margin-top: 12px;
    border-top: 1px solid #efefef;
    padding-top: 12px;
}

.comment-input-wrapper {
    display: flex;
    align-items: flex-start;
    gap: 8px;
}

.comment-input {
    flex-grow: 1;
    border: none;
    outline: none;
    resize: none;
    font-size: 14px;
    line-height: 18px;
    padding: 0;
    min-height: 18px;
    max-height: 80px;
}

.comment-button {
    color: #0095f6;
    font-weight: 600;
    padding: 0;
    height: auto;
}

.comment-button:disabled {
    color: #b2dffc;
    cursor: not-allowed;
}

.comment-button:not(:disabled):hover {
    color: #00376b;
}
</style>
