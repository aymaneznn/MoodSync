<template>
    <Card>
        <template #content>
            <div class="p-fluid p-p-5 main-container">
                <!-- Analysis Zone -->
                <div class="analysis-section p-mb-6">
                    <div class="section-title p-mb-5">
                        <div class="title-decoration"></div>
                        <h2 class="p-text-center">
                            <i class="pi pi-comment p-mr-3"></i>
                            Emotional Analysis
                        </h2>
                        <div class="title-decoration"></div>
                    </div>

                    <!-- Button Container -->
                    <div class="button-container">
                        <!-- Start Analysis Button (Visible Initially) -->
                        <Button v-if="!result" label="Start Analysis" icon="pi pi-heart"
                            class="p-button-rounded p-button-lg p-button-primary" @click="handleAnalysis"
                            :disabled="loading" />

                        <!-- Refresh Icon (Visible After Analysis) -->
                        <Button v-else icon="pi pi-refresh" class="p-button-rounded p-button-text refresh-button"
                            @click="handleAnalysis" :disabled="loading" />
                    </div>
                </div>

                <!-- Loading Spinner -->
                <div v-if="loading" class="loading-section p-my-7">
                    <div class="loading-wrapper">
                        <ProgressSpinner style="width: 60px; height: 60px" strokeWidth="5" animationDuration=".6s" />
                        <p class="loading-text p-mt-3 p-text-xl">Analyzing...</p>
                    </div>
                </div>

                <!-- Results -->
                <div v-if="result" class="results-section">
                    <!-- Urgent Alert -->
                    <Card v-if="result.emotion === 'alert'" class="alert-section p-my-6 p-shadow-3">
                        <template #title>
                            <div class="alert-header p-d-flex p-ai-center">
                                <i class="pi pi-exclamation-triangle p-mr-3"></i>
                                <h3 class="p-m-0">Immediate Assistance Required</h3>
                            </div>
                        </template>
                        <template #content>
                            <div class="alert-content p-text-lg">
                                <p>It sounds like you're going through a tough time. Please reach out to a mental health
                                    professional or a trusted person in your life. Here are some resources that might
                                    help:</p>
                                <ul>
                                    <li><strong>Croix-Rouge écoute (association):</strong> <a href="tel:0800858858">0
                                            800 858
                                            858</a></li>
                                    <li><strong>SOS Amitié (association) 24/7:</strong> <a href="tel:0972394050">09 72
                                            39 40
                                            50</a> or in English: <a href="tel:0146214646">01 46 21 46 46</a></li>
                                    <li><strong>SOS crise (association Les transmetteurs):</strong> <a
                                            href="tel:0800190000">0800 19 00 00</a></li>
                                    <li><strong>More specific hotlines:</strong> <a
                                            href="https://www.psycom.org/sorienter/les-lignes-decoute/" target="_blank"
                                            rel="noopener noreferrer">Visit Psycom.org</a></li>
                                </ul>
                            </div>
                        </template>
                    </Card>

                    <!-- Normal Recommendations -->
                    <div v-else>
                        <!-- Suggestion Section -->
                        <div class="suggestion-section p-my-6">
                            <div class="section-title p-mb-5">
                                <h3 class="p-text-center">
                                    <i class="pi pi-lightbulb p-mr-3"></i>
                                    Your Personalized Recommendation
                                </h3>
                            </div>
                            <Card class="p-shadow-2">
                                <template #content>
                                    <div class="p-d-flex p-ai-center p-jc-center p-p-3">
                                        <blockquote class="suggestion-text">
                                            "{{ result.suggestion }}"
                                        </blockquote>
                                    </div>
                                </template>
                            </Card>
                        </div>

                        <!-- Movies Section -->
                        <div class="movies-section p-my-6">
                            <div class="section-title p-mb-5">
                                <h3 class="p-text-center">
                                    <i class="pi pi-ticket p-mr-3"></i>
                                    Special Picks For You
                                </h3>
                            </div>

                            <DataTable :value="result.movies" class="p-datatable-responsive p-shadow-2">
                                <Column field="title" header="Title">
                                    <template #body="{ data }">
                                        <div class="p-d-flex p-ai-center">
                                            <img :src="'https://image.tmdb.org/t/p/w500' + data.posterPath"
                                                :alt="data.title" class="p-mr-3"
                                                style="width: 70px; height: 105px; border-radius: 8px; object-fit: cover;" />
                                            <span>{{ data.title }}</span>
                                        </div>
                                    </template>
                                </Column>

                                <Column header="Description">
                                    <template #body="{ data }">
                                        <div class="p-text-sm" style="max-width: 400px; color: #4b5563">{{ data.overview
                                            }}
                                        </div>
                                    </template>
                                </Column>

                                <Column field="voteAverage" header="Rating">
                                    <template #body="{ data }">
                                        <Rating :modelValue="data.voteAverage / 2" readonly :cancel="false" />
                                    </template>
                                </Column>

                                <Column field="releaseDate" header="Release Date">
                                    <template #body="{ data }">
                                        {{ new Date(data.releaseDate).toLocaleDateString() }}
                                    </template>
                                </Column>
                            </DataTable>
                        </div>
                    </div>
                </div>
            </div>
        </template>
    </Card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import Button from 'primevue/button';
import Card from 'primevue/card';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Rating from 'primevue/rating';
import ProgressSpinner from 'primevue/progressspinner';
import emotionService from '@/service/emotionService.js';
import { getUserPosts } from '@/service/apiService';


const loading = ref(false);
const result = ref(null);
const posts = ref([]);
const lastPost = ref();
const sampleText = ref('');

const fetchPosts = async () => {
    try {
        const response = await getUserPosts(localStorage.getItem('userId'));
        posts.value = response.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
        lastPost.value = posts.value[0];
        console.log('Posts fetched:', posts.value);
    } catch (error) {
        console.error('Error fetching posts:', error);
    }
};

onMounted(async () => {
    await fetchPosts();
    console.log('Last post:', lastPost.value.content);
    sampleText.value = lastPost.value?.content || 'vide';
});


const handleAnalysis = async () => {
    try {
        loading.value = true;
        result.value = await emotionService.analyzeText(sampleText.value);
    } catch (error) {
        console.error(error);
    } finally {
        loading.value = false;
    }
};


</script>

<style scoped>
/* Main Container */
.main-container {
    background: #f8fafc;
    min-height: 100vh;
}

/* Section Titles - Simplified Version */
.section-title {
    margin: 2rem 0;
    text-align: center;
}

.section-title h2 {
    color: #1e293b;
    font-size: 1.75rem;
    font-weight: 700;
    letter-spacing: -0.025em;
    position: relative;
    display: inline-block;
    padding: 0 1rem;
}

.section-title h2::after {
    content: "";
    position: absolute;
    bottom: -8px;
    left: 50%;
    transform: translateX(-50%);
    width: 50px;
    height: 3px;
    background: #3b82f6;
    border-radius: 2px;
}

.section-title h3 {
    font-size: 1.5rem;
    color: #334155;
    font-weight: 600;
    margin-bottom: 1.5rem;
}

/* Enhanced Suggestion Section */
.suggestion-section {
    background: linear-gradient(135deg, #f0f4ff 0%, #f8fafc 100%);
    border-radius: 16px;
    border: 2px solid #e0e7ff;
    margin: 2rem 0;
    position: relative;
    overflow: hidden;
}

.suggestion-section::before {
    content: "✨";
    position: absolute;
    top: -15px;
    right: -15px;
    font-size: 2rem;
    opacity: 0.3;
}

.suggestion-text {
    font-size: 1.3rem;
    color: #1e40af;
    font-style: italic;
    padding: 1.5rem;
    margin: 0;
    line-height: 1.6;
    position: relative;
    background: rgba(255, 255, 255, 0.7);
    border-radius: 12px;
}

/* Remove Separation Borders */
.analysis-section,
.loading-section,
.results-section {
    padding-bottom: 2rem;
    margin-bottom: 2rem;
    border-bottom: none;
}

/* Movies Section */
.movies-section {
    background: #fff;
    padding: 2rem;
    border-radius: 16px;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
}

/* Enhanced Alert Section */
.alert-section {
    background: #fff5f5;
    border: 2px solid #fee2e2;
    border-radius: 12px;
}

.alert-header {
    background: linear-gradient(135deg, #fee2e2 0%, #fff5f5 100%);
    padding: 1.5rem;
    border-radius: 12px 12px 0 0;
}

.alert-header h3 {
    color: #dc2626;
    font-size: 1.4rem;
}

.alert-content {
    color: #b91c1c;
    padding: 1.5rem;
    line-height: 1.7;
    font-size: 1.1rem;
}

/* Primary Button */
::v-deep(.p-button.p-button-primary) {
    background: #3b82f6;
    border-color: #3b82f6;
    padding: 1rem 3rem;
    font-size: 1.1rem;
    transition: all 0.3s ease;
    border-radius: 50px;
    box-shadow: 0 4px 6px -1px rgba(59, 130, 246, 0.2);
}

::v-deep(.p-button.p-button-primary:hover) {
    background: #2563eb;
    transform: translateY(-2px);
    box-shadow: 0 6px 8px -1px rgba(59, 130, 246, 0.3);
}

/* Centered Loader */
.loading-section {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 70vh;
}

.loading-wrapper {
    text-align: center;
}

.loading-text {
    color: #4b5563;
    font-weight: 500;
}

.button-container {
    display: flex;
    justify-content: center;
    align-items: center;
}

/* Enhanced Alert Section */
.alert-section {
    background: #fff5f5;
    border: 2px solid #fee2e2;
    border-radius: 12px;
}

.alert-header {
    background: linear-gradient(135deg, #fee2e2 0%, #fff5f5 100%);
    padding: 1.5rem;
    border-radius: 12px 12px 0 0;
}

.alert-header h3 {
    color: #dc2626;
    font-size: 1.4rem;
}

.alert-content {
    color: #b91c1c;
    padding: 1.5rem;
    line-height: 1.7;
    font-size: 1.1rem;
}

.alert-content ul {
    list-style-type: disc;
    padding-left: 2rem;
    margin-top: 1rem;
}

.alert-content li {
    margin-bottom: 0.75rem;
}

.alert-content a {
    color: #3b82f6;
    text-decoration: underline;
}

.alert-content a:hover {
    color: #2563eb;
}

/* Refresh Button */
.refresh-button {
    position: fixed;
    bottom: 79%;
    right: 3%;
    z-index: 1000;
    background-color: #3b82f6;
    color: white;
    border: none;
    box-shadow: 0 4px 6px -1px rgba(59, 130, 246, 0.2);
}

.refresh-button:hover {
    background-color: #2563eb;
    transform: translateY(-2px);
    box-shadow: 0 6px 8px -1px rgba(59, 130, 246, 0.3);
}
</style>