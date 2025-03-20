<template>
    <div class="admin-page">
        <div class="card">
            <TabView>
                <!-- Users Tab -->
                <TabPanel header="Users">
                    <div class="card">
                        <Toolbar class="mb-4">
                            <template #start>
                                <Button label="New User" icon="pi pi-plus" severity="success" class="mr-2" @click="openNewUserDialog" />
                            </template>
                        </Toolbar>

                        <DataTable :value="users" v-model:filters="filters" :paginator="true" :rows="10" dataKey="id" filterDisplay="menu" :loading="loading" :rowsPerPageOptions="[5, 10, 20]" responsiveLayout="scroll">
                            <Column field="name" header="Username" sortable style="min-width: 12rem" />
                            <Column field="email" header="Email" sortable style="min-width: 16rem" />
                            <Column field="role" header="Role" sortable style="min-width: 8rem">
                                <template #body="slotProps">
                                    <Tag :severity="slotProps.data.role === 'admin' ? 'danger' : 'info'">
                                        {{ slotProps.data.role }}
                                    </Tag>
                                </template>
                            </Column>
                            <Column field="createdAt" header="Creation Date" sortable style="min-width: 10rem">
                                <template #body="slotProps">
                                    {{ new Date(slotProps.data.createdAt).toLocaleDateString() }}
                                </template>
                            </Column>
                            <Column header="Actions" style="min-width: 8rem">
                                <template #body="slotProps">
                                    <Button icon="pi pi-pencil" rounded outlined class="mr-2" @click="editUser(slotProps.data)" />
                                    <Button icon="pi pi-trash" rounded outlined severity="danger" @click="confirmDeleteUser(slotProps.data)" />
                                </template>
                            </Column>
                        </DataTable>
                    </div>
                </TabPanel>

                <!-- Posts Tab -->
                <TabPanel header="Posts">
                    <div class="card">
                        <DataTable :value="posts" v-model:filters="postFilters" :paginator="true" :rows="10" dataKey="id" filterDisplay="menu" :loading="loadingPosts" :rowsPerPageOptions="[5, 10, 20]" responsiveLayout="scroll">
                            <Column field="id" header="ID" sortable style="min-width: 5rem" />
                            <Column field="content" header="Content" sortable style="min-width: 20rem">
                                <template #body="slotProps">
                                    <div class="post-content">
                                        <p>{{ slotProps.data.content }}</p>
                                        <img v-if="slotProps.data.mediaUrl" :src="slotProps.data.mediaUrl" alt="Post media" style="max-width: 100px; height: auto" />
                                    </div>
                                </template>
                            </Column>
                            <Column field="user.name" header="Author" sortable style="min-width: 12rem">
                                <template #body="slotProps">
                                    <div class="user-info">
                                        <img :src="slotProps.data.user.profilePictureUrl" alt="Profile" style="width: 32px; height: 32px; border-radius: 50%; margin-right: 8px" />
                                        {{ slotProps.data.user.name }}
                                    </div>
                                </template>
                            </Column>
                            <Column field="visibility" header="Visibility" sortable style="min-width: 8rem" />
                            <Column field="likesCount" header="Likes" sortable style="min-width: 8rem" />
                            <Column field="comments" header="Comments" style="min-width: 12rem">
                                <template #body="slotProps">
                                    {{ slotProps.data.comments.length }}
                                </template>
                            </Column>
                            <Column field="createdAt" header="Creation Date" sortable style="min-width: 10rem">
                                <template #body="slotProps">
                                    {{ new Date(slotProps.data.createdAt).toLocaleDateString() }}
                                </template>
                            </Column>
                            <Column header="Actions" style="min-width: 8rem">
                                <template #body="slotProps">
                                    <Button icon="pi pi-eye" rounded outlined class="mr-2" @click="viewPost(slotProps.data)" />
                                    <Button icon="pi pi-pencil" rounded outlined class="mr-2" @click="editPost(slotProps.data)" />
                                    <Button icon="pi pi-trash" rounded outlined severity="danger" @click="confirmDeletePost(slotProps.data)" />
                                </template>
                            </Column>
                        </DataTable>
                    </div>

                    <!-- Post Details Dialog -->
                    <Dialog v-model:visible="postViewDialog" :header="'Post Details'" modal class="p-fluid post-dialog">
                        <div v-if="selectedPost" class="post-details">
                            <div class="post-header">
                                <div class="user-info">
                                    <img :src="selectedPost.user.profilePictureUrl" alt="Profile" class="profile-pic" />
                                    <span class="username">{{ selectedPost.user.name }}</span>
                                </div>
                                <span class="date">{{ new Date(selectedPost.createdAt).toLocaleDateString() }}</span>
                            </div>
                            <div class="post-content">
                                <p>{{ selectedPost.content }}</p>
                                <img v-if="selectedPost.mediaUrl" :src="selectedPost.mediaUrl" alt="Post media" class="post-image" />
                            </div>
                            <div class="post-stats">
                                <span class="likes">❤️ {{ selectedPost.likesCount }} likes</span>
                                <span class="visibility">🔒 {{ selectedPost.visibility }}</span>
                            </div>
                            <div class="comments-section">
                                <h3>Comments ({{ selectedPost.comments.length }})</h3>
                                <ul class="comments-list">
                                    <li v-for="(comment, index) in selectedPost.comments" :key="index">
                                        {{ comment.user }}
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </Dialog>

                    <!-- Delete Confirmation Dialog -->
                    <Dialog v-model:visible="deletePostDialog" modal header="Confirmation" :style="{ width: '450px' }">
                        <div class="confirmation-content">
                            <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
                            <span>Are you sure you want to delete this post?</span>
                        </div>
                        <template #footer>
                            <Button label="No" icon="pi pi-times" outlined @click="deletePostDialog = false" />
                            <Button label="Yes" icon="pi pi-check" severity="danger" @click="deletePost" />
                        </template>
                    </Dialog>

                    <!-- Edit Post Dialog -->
                    <Dialog v-model:visible="editPostDialog" :header="'Edit Post'" modal class="p-fluid post-dialog">
                        <div v-if="selectedPost" class="post-edit-form">
                            <div class="field">
                                <label for="content">Content</label>
                                <Textarea id="content" v-model="selectedPost.content" rows="4" required autoResize />
                            </div>
                            <div class="field">
                                <label for="visibility">Visibility</label>
                                <Dropdown id="visibility" v-model="selectedPost.visibility" :options="['public', 'private', 'friends']" placeholder="Select visibility" />
                            </div>
                            <div class="field">
                                <label for="mediaUrl">Image URL</label>
                                <div class="p-inputgroup">
                                    <InputText id="mediaUrl" v-model="selectedPost.mediaUrl" />
                                    <Button icon="pi pi-eye" @click="previewImage = selectedPost.mediaUrl" />
                                </div>
                                <img v-if="selectedPost.mediaUrl" :src="selectedPost.mediaUrl" alt="Preview" class="media-preview" />
                            </div>
                        </div>
                        <template #footer>
                            <Button label="Cancel" icon="pi pi-times" outlined @click="editPostDialog = false" />
                            <Button label="Save" icon="pi pi-check" @click="savePost" />
                        </template>
                    </Dialog>
                </TabPanel>
            </TabView>
        </div>

        <!-- Add/Edit User Dialog -->
        <Dialog v-model:visible="userDialog" :header="userDialogTitle" modal class="p-fluid user-dialog">
            <div class="field">
                <label for="name">Username</label>
                <InputText id="name" v-model="editedUser.name" required autofocus />
            </div>
            <div class="field">
                <label for="email">Email</label>
                <InputText id="email" v-model="editedUser.email" required type="email" />
            </div>
            <div class="field">
                <label for="role">Role</label>
                <Dropdown
                    id="role"
                    v-model="editedUser.role"
                    :options="['user', 'admin']"
                    optionLabel=""
                    placeholder="Select role"
                    class="w-full"
                />
            </div>
            <div class="field">
                <label for="profilePictureUrl">Profile Picture URL</label>
                <InputText id="profilePictureUrl" v-model="editedUser.profilePictureUrl" />
            </div>
            <template #footer>
                <Button label="Cancel" icon="pi pi-times" outlined @click="hideDialog" />
                <Button label="Save" icon="pi pi-check" @click="saveUser" />
            </template>
        </Dialog>

        <!-- Delete User Confirmation Dialog -->
        <Dialog v-model:visible="deleteUserDialog" modal header="Confirmation" :style="{ width: '450px' }">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
                <span>Are you sure you want to delete this user?</span>
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" outlined @click="deleteUserDialog = false" />
                <Button label="Yes" icon="pi pi-check" severity="danger" @click="deleteUser" />
            </template>
        </Dialog>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useToast } from 'primevue/usetoast';
import { getAllPost } from '@/service/apiService';
import Tag from 'primevue/tag';
import Dropdown from 'primevue/dropdown';

const toast = useToast();
const loading = ref(false);
const loadingPosts = ref(false);
const users = ref([]);
const posts = ref([]);
const filters = ref({});
const postFilters = ref({});
const userDialog = ref(false);
const deleteUserDialog = ref(false);
const editedUser = ref({});
const userDialogTitle = ref('');
const postViewDialog = ref(false);
const deletePostDialog = ref(false);
const selectedPost = ref(null);
const editPostDialog = ref(false);
const previewImage = ref('');

// Chargement des données
onMounted(async () => {
    await loadUsers();
    await loadPosts();
});

const loadUsers = async () => {
    loading.value = true;
    try {
        const response = await fetch('http://localhost:8080/users');
        const data = await response.json();
        users.value = data;
        console.log("users->>>", users.value);
    } catch (error) {
        console.error('Erreur lors de la récupération des utilisateurs', error);
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Unable to load users',
            life: 3000
        });
    } finally {
        loading.value = false;
    }
};

const loadPosts = async () => {
    loadingPosts.value = true;
    try {
        const response = await getAllPost();
        posts.value = response.map((post) => ({
            ...post,
            comments: post.comments ? post.comments : [], // Assure que comments est toujours un tableau
            showAllComments: false
        }));
    } catch (error) {
        console.error('Error fetching posts:', error);
    } finally {
        loadingPosts.value = false;
    }
};

const openNewUserDialog = () => {
    editedUser.value = {
        role: 'user' // Default role for new users
    };
    userDialogTitle.value = 'New User';
    userDialog.value = true;
};

const editUser = (user) => {
    editedUser.value = { ...user };
    userDialogTitle.value = 'Edit User';
    userDialog.value = true;
};

const hideDialog = () => {
    userDialog.value = false;
};

const saveUser = async () => {
    try {
        if (editedUser.value.id) {
            await fetch(`http://localhost:8080/users/${editedUser.value.id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(editedUser.value),
            });
        } else {
            await fetch('http://localhost:8080/users', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(editedUser.value),
            });
        }
        await loadUsers();
        userDialog.value = false;
        toast.add({ severity: 'success', summary: 'Success', detail: 'User saved successfully', life: 3000 });
    } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Unable to save user', life: 3000 });
    }
};

const confirmDeleteUser = (user) => {
    editedUser.value = user;
    deleteUserDialog.value = true;
};

const deleteUser = async () => {
    try {
        await fetch(`http://localhost:8080/users/${editedUser.value.id}`, {
            method: 'DELETE',
        });
        await loadUsers();
        deleteUserDialog.value = false;
        toast.add({ severity: 'success', summary: 'Success', detail: 'User deleted successfully', life: 3000 });
    } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Unable to delete user', life: 3000 });
    }
};

// Fonctions pour la gestion des posts
const viewPost = (post) => {
    selectedPost.value = post;
    postViewDialog.value = true;
};

const editPost = (post) => {
    selectedPost.value = { ...post }; // Créer une copie pour ne pas modifier directement
    editPostDialog.value = true;
};

const savePost = async () => {
    try {
        const response = await fetch(`http://localhost:8080/api/posts/put-post/${selectedPost.value.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(selectedPost.value)
        });

        if (!response.ok) {
            throw new Error('Erreur lors de la mise à jour du post');
        }

        await loadPosts();
        editPostDialog.value = false;
        toast.add({
            severity: 'success',
            summary: 'Success',
            detail: 'Post updated successfully',
            life: 3000
        });
    } catch (error) {
        console.error('Erreur lors de la mise à jour:', error);
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Unable to update post',
            life: 3000
        });
    }
};

const confirmDeletePost = (post) => {
    selectedPost.value = post;
    deletePostDialog.value = true;
};

const deletePost = async () => {
    try {
        await fetch(`http://localhost:8080/api/posts/${selectedPost.value.id}`, {
            method: 'DELETE'
        });
        await loadPosts();
        deletePostDialog.value = false;
        toast.add({ severity: 'success', summary: 'Success', detail: 'Post deleted successfully', life: 3000 });
    } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Unable to delete post', life: 3000 });
    }
};
</script>

<style lang="scss" scoped>
.admin-page {
    padding: 2rem;

    .p-toolbar {
        background: transparent;
        border: none;
        padding: 0;
    }

    .card {
        background: var(--surface-card);
        border-radius: var(--border-radius);
        padding: 1.5rem;
        margin-bottom: 1rem;
        box-shadow: var(--card-shadow);
    }

    .confirmation-content {
        display: flex;
        align-items: center;
        justify-content: center;
    }
}

.post-dialog {
    max-width: 90vw;
    width: 800px;
    min-height: 500px;

    .post-details {
        max-height: 70vh;
        overflow-y: auto;
        padding: 1rem;

        .post-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1rem;

            .user-info {
                display: flex;
                align-items: center;
                gap: 0.5rem;

                .profile-pic {
                    width: 40px;
                    height: 40px;
                    border-radius: 50%;
                }

                .username {
                    font-weight: bold;
                }
            }

            .date {
                color: var(--text-color-secondary);
            }
        }

        .post-content {
            margin-bottom: 1rem;

            .post-image {
                max-width: 400px;
                max-height: 300px;
                width: auto;
                height: auto;
                border-radius: 8px;
                margin-top: 0.5rem;
                object-fit: contain;
            }
        }

        .post-stats {
            display: flex;
            gap: 1rem;
            margin-bottom: 1rem;
            color: var(--text-color-secondary);
        }

        .comments-section {
            h3 {
                margin-bottom: 0.5rem;
            }

            .comments-list {
                list-style: none;
                padding: 0;
                margin: 0;

                li {
                    padding: 0.5rem 0;
                    border-bottom: 1px solid var(--surface-border);

                    &:last-child {
                        border-bottom: none;
                    }
                }
            }
        }
    }
}

.user-info {
    display: flex;
    align-items: center;
}

.post-content {
    img {
        border-radius: 4px;
        margin-top: 0.5rem;
    }
}

.post-edit-form {
    .field {
        margin-bottom: 1.5rem;

        label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 600;
        }
    }

    .media-preview {
        max-width: 400px;
        max-height: 300px;
        width: auto;
        height: auto;
        margin-top: 1rem;
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        object-fit: contain;
    }

    .p-inputgroup {
        .p-button {
            background-color: var(--primary-color);
            border-color: var(--primary-color);
        }
    }
}

.user-dialog {
    width: 700px;
    min-height: 400px;

    .field {
        margin-bottom: 1.5rem;

        label {
            font-weight: 600;
            display: block;
            margin-bottom: 0.5rem;
        }

        .p-dropdown {
            width: 100%;
        }
    }
}

/* Ajout des styles généraux pour tous les dialogues */
:deep(.p-dialog) {
    .p-dialog-content {
        min-height: 200px;
        padding: 2rem;
    }

    .p-dialog-header {
        padding: 1.5rem 2rem;
    }

    .p-dialog-footer {
        padding: 1.5rem 2rem;
    }
}

/* Style spécifique pour le dialogue de confirmation */
:deep(.p-dialog.confirmation-dialog) {
    width: 500px;
    min-height: 200px;
}
</style>
