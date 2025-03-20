<template>
    <div class="admin-page">
        <div class="card">
            <TabView>
                <!-- Onglet Utilisateurs -->
                <TabPanel header="Utilisateurs">
                    <div class="card">
                        <Toolbar class="mb-4">
                            <template #start>
                                <Button label="Nouvel Utilisateur" icon="pi pi-plus" severity="success" class="mr-2" @click="openNewUserDialog" />
                            </template>
                        </Toolbar>

                        <DataTable :value="users" v-model:filters="filters" :paginator="true" :rows="10" dataKey="id" filterDisplay="menu" :loading="loading" :rowsPerPageOptions="[5, 10, 20]" responsiveLayout="scroll">
                            <Column field="username" header="Nom d'utilisateur" sortable style="min-width: 12rem" />
                            <Column field="email" header="Email" sortable style="min-width: 16rem" />
                            <Column field="createdAt" header="Date de création" sortable style="min-width: 10rem">
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

                <!-- Onglet Posts -->
                <TabPanel header="Posts">
                    <div class="card">
                        <DataTable :value="posts" v-model:filters="postFilters" :paginator="true" :rows="10" dataKey="id" filterDisplay="menu" :loading="loadingPosts" :rowsPerPageOptions="[5, 10, 20]" responsiveLayout="scroll">
                            <Column field="id" header="ID" sortable style="min-width: 5rem" />
                            <Column field="content" header="Contenu" sortable style="min-width: 20rem">
                                <template #body="slotProps">
                                    <div class="post-content">
                                        <p>{{ slotProps.data.content }}</p>
                                        <img v-if="slotProps.data.mediaUrl" :src="slotProps.data.mediaUrl" alt="Post media" style="max-width: 100px; height: auto" />
                                    </div>
                                </template>
                            </Column>
                            <Column field="user.name" header="Auteur" sortable style="min-width: 12rem">
                                <template #body="slotProps">
                                    <div class="user-info">
                                        <img :src="slotProps.data.user.profilePictureUrl" alt="Profile" style="width: 32px; height: 32px; border-radius: 50%; margin-right: 8px" />
                                        {{ slotProps.data.user.name }}
                                    </div>
                                </template>
                            </Column>
                            <Column field="visibility" header="Visibilité" sortable style="min-width: 8rem" />
                            <Column field="likesCount" header="Likes" sortable style="min-width: 8rem" />
                            <Column field="comments" header="Commentaires" style="min-width: 12rem">
                                <template #body="slotProps">
                                    {{ slotProps.data.comments.length }}
                                </template>
                            </Column>
                            <Column field="createdAt" header="Date de création" sortable style="min-width: 10rem">
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

                    <!-- Dialog pour voir les détails du post -->
                    <Dialog v-model:visible="postViewDialog" :header="'Détails du Post'" modal class="p-fluid post-dialog">
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
                                <h3>Commentaires ({{ selectedPost.comments.length }})</h3>
                                <ul class="comments-list">
                                    <li v-for="(comment, index) in selectedPost.comments" :key="index">
                                        {{ comment.user }}
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </Dialog>

                    <!-- Dialog de confirmation de suppression -->
                    <Dialog v-model:visible="deletePostDialog" modal header="Confirmation" :style="{ width: '450px' }">
                        <div class="confirmation-content">
                            <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
                            <span>Êtes-vous sûr de vouloir supprimer ce post ?</span>
                        </div>
                        <template #footer>
                            <Button label="Non" icon="pi pi-times" outlined @click="deletePostDialog = false" />
                            <Button label="Oui" icon="pi pi-check" severity="danger" @click="deletePost" />
                        </template>
                    </Dialog>

                    <!-- Dialog d'édition de post -->
                    <Dialog v-model:visible="editPostDialog" :header="'Modifier le Post'" modal class="p-fluid post-dialog">
                        <div v-if="selectedPost" class="post-edit-form">
                            <div class="field">
                                <label for="content">Contenu</label>
                                <Textarea id="content" v-model="selectedPost.content" rows="4" required autoResize />
                            </div>
                            <div class="field">
                                <label for="visibility">Visibilité</label>
                                <Dropdown id="visibility" v-model="selectedPost.visibility" :options="['public', 'private', 'friends']" placeholder="Sélectionnez la visibilité" />
                            </div>
                            <div class="field">
                                <label for="mediaUrl">URL de l'image</label>
                                <div class="p-inputgroup">
                                    <InputText id="mediaUrl" v-model="selectedPost.mediaUrl" />
                                    <Button icon="pi pi-eye" @click="previewImage = selectedPost.mediaUrl" />
                                </div>
                                <img v-if="selectedPost.mediaUrl" :src="selectedPost.mediaUrl" alt="Preview" class="media-preview" />
                            </div>
                        </div>
                        <template #footer>
                            <Button label="Annuler" icon="pi pi-times" outlined @click="editPostDialog = false" />
                            <Button label="Sauvegarder" icon="pi pi-check" @click="savePost" />
                        </template>
                    </Dialog>
                </TabPanel>
            </TabView>
        </div>

        <!-- Dialogs -->
        <Dialog v-model:visible="userDialog" :header="userDialogTitle" modal class="p-fluid">
            <div class="field">
                <label for="username">Nom d'utilisateur</label>
                <InputText id="username" v-model="editedUser.username" required autofocus />
            </div>
            <div class="field">
                <label for="email">Email</label>
                <InputText id="email" v-model="editedUser.email" required type="email" />
            </div>
            <template #footer>
                <Button label="Annuler" icon="pi pi-times" outlined @click="hideDialog" />
                <Button label="Sauvegarder" icon="pi pi-check" @click="saveUser" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteUserDialog" modal header="Confirmation" :style="{ width: '450px' }">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
                <span>Êtes-vous sûr de vouloir supprimer cet utilisateur ?</span>
            </div>
            <template #footer>
                <Button label="Non" icon="pi pi-times" outlined @click="deleteUserDialog = false" />
                <Button label="Oui" icon="pi pi-check" severity="danger" @click="deleteUser" />
            </template>
        </Dialog>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useToast } from 'primevue/usetoast';

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

import { getAllPost, likePost, unlikePost, createPost, addCommentToPost, getCommentsByPostId, getUserProfile } from '@/service/apiService';

// Chargement des données
onMounted(async () => {
    await loadUsers();
    await loadPosts();
});

const loadUsers = async () => {
    loading.value = true;
    try {
        const response = await getUserProfile(userId);
        console.log(response);
        userProfilePicture.value = response.profilePictureUrl;
    } catch (error) {
        console.error('Error fetching user profile:', error);
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
    editedUser.value = {};
    userDialogTitle.value = 'Nouvel Utilisateur';
    userDialog.value = true;
};

const editUser = (user) => {
    editedUser.value = { ...user };
    userDialogTitle.value = 'Modifier Utilisateur';
    userDialog.value = true;
};

const hideDialog = () => {
    userDialog.value = false;
};

const saveUser = async () => {
    try {
        if (editedUser.value.id) {
            await fetch(`http://localhost:8080/api/users/${editedUser.value.id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(editedUser.value)
            });
        } else {
            await fetch('http://localhost:8080/api/users', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(editedUser.value)
            });
        }
        await loadUsers();
        userDialog.value = false;
        toast.add({ severity: 'success', summary: 'Succès', detail: 'Utilisateur sauvegardé', life: 3000 });
    } catch (error) {
        toast.add({ severity: 'error', summary: 'Erreur', detail: 'Erreur lors de la sauvegarde', life: 3000 });
    }
};

const confirmDeleteUser = (user) => {
    editedUser.value = user;
    deleteUserDialog.value = true;
};

const deleteUser = async () => {
    try {
        await fetch(`http://localhost:8080/api/users/${editedUser.value.id}`, {
            method: 'DELETE'
        });
        await loadUsers();
        deleteUserDialog.value = false;
        toast.add({ severity: 'success', summary: 'Succès', detail: 'Utilisateur supprimé', life: 3000 });
    } catch (error) {
        toast.add({ severity: 'error', summary: 'Erreur', detail: 'Erreur lors de la suppression', life: 3000 });
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
            summary: 'Succès',
            detail: 'Post mis à jour avec succès',
            life: 3000
        });
    } catch (error) {
        console.error('Erreur lors de la mise à jour:', error);
        toast.add({
            severity: 'error',
            summary: 'Erreur',
            detail: 'Impossible de mettre à jour le post',
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
        toast.add({ severity: 'success', summary: 'Succès', detail: 'Post supprimé', life: 3000 });
    } catch (error) {
        toast.add({ severity: 'error', summary: 'Erreur', detail: 'Erreur lors de la suppression', life: 3000 });
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
    width: 600px;

    .post-details {
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
                max-width: 100%;
                height: auto;
                border-radius: 8px;
                margin-top: 0.5rem;
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
        max-width: 100%;
        height: auto;
        margin-top: 1rem;
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .p-inputgroup {
        .p-button {
            background-color: var(--primary-color);
            border-color: var(--primary-color);
        }
    }
}
</style>
