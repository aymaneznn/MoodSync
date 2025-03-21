<template>
    <Card>
        <template #content>
            <div class="p-fluid p-p-5 main-container">
                <!-- Generation Zone -->
                <div class="generation-section p-mb-6">
                    <div class="section-title p-mb-5">
                        <div class="title-decoration"></div>
                        <h2 class="p-text-center">
                            <i class="pi pi-music p-mr-3"></i>
                            Personalized Lyrics Generator
                        </h2>
                        <div class="title-decoration"></div>
                    </div>

                    <!-- Button Container -->
                    <div class="button-container">
                        <!-- Start Generation Button (Visible Initially) -->
                        <Button v-if="!result" label="Generate Lyrics" icon="pi pi-music" style="margin-left: 40%" class="p-button-rounded p-button-lg p-button-primary" @click="handleGeneration" :disabled="loading" />

                        <!-- Refresh Icon (Visible After Generation) -->
                        <Button v-else icon="pi pi-refresh" class="p-button-rounded p-button-text refresh-button" @click="handleGeneration" :disabled="loading" />
                    </div>
                </div>

                <!-- Loading Spinner -->
                <div v-if="loading" class="loading-section p-my-7">
                    <div class="loading-wrapper">
                        <div class="music-loader">
                            <div class="note note-1">🎵</div>
                            <div class="note note-2">🎼</div>
                            <div class="note note-3">🎶</div>
                            <div class="note note-4">🎸</div>
                        </div>
                        <p class="loading-text p-mt-5 p-text-xl">Composing...</p>
                    </div>
                </div>

                <!-- Results -->
                <div v-if="result" class="results-section">
                    <!-- Lyrics Display -->
                    <div class="lyrics-section p-my-6">
                        <div class="section-title p-mb-5">
                            <h3 class="p-text-center">
                                <i class="pi pi-volume-up p-mr-3"></i>
                                Your Personalized Lyrics
                            </h3>
                        </div>
                        <Card class="p-shadow-2 lyrics-card">
                            <template #content>
                                <div class="lyrics-container">
                                    <div class="lyrics-content">
                                        <div v-for="(stanza, index) in formattedLyrics" :key="index" class="stanza">
                                            <p v-for="(line, lineIndex) in stanza" :key="lineIndex" class="lyrics-line">
                                                {{ line }}
                                            </p>
                                        </div>
                                    </div>
                                    <div class="song-info">
                                        <div class="info-item">
                                            <i class="pi pi-music"></i>
                                            <span>{{ songKey }}</span>
                                        </div>
                                        <div class="info-item">
                                            <i class="pi pi-tag"></i>
                                            <span>{{ songGenre }}</span>
                                        </div>
                                    </div>
                                </div>
                            </template>
                        </Card>
                    </div>

                    <!-- Save Lyrics Button -->
                    <div class="save-lyrics p-my-6">
                        <Button label="Save Lyrics" icon="pi pi-save" class="p-button-rounded p-button-success" @click="saveLyrics" />
                    </div>
                </div>

                <!-- Lyrics History -->
                <div class="lyrics-history p-my-6">
                    <h3 class="p-text-center">
                        <i class="pi pi-history p-mr-3"></i>
                        Lyrics History
                    </h3>
                    <Dropdown :options="savedLyrics" optionLabel="content" placeholder="Select lyrics" class="p-mt-3" />
                </div>
            </div>
        </template>
    </Card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import Button from 'primevue/button';
import Card from 'primevue/card';
import Dropdown from 'primevue/dropdown';
import { getUserProfile, getUserPosts, addRecommendation, getUserRecommendations } from '@/service/apiService';

const loading = ref(false);
const result = ref(null);
const posts = ref([]);
const lastPost = ref();
const sampleText = ref('');
const savedLyrics = ref([]);
const formattedLyrics = ref([]);
const songKey = ref('');
const songGenre = ref('');

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

const fetchSavedLyrics = async () => {
    try {
        const response = await getUserRecommendations(localStorage.getItem('userId'));
        savedLyrics.value = response.filter((rec) => rec.type === 'lyrics');
        console.log('Saved lyrics fetched:', savedLyrics.value);
    } catch (error) {
        console.error('Error fetching saved lyrics:', error);
    }
};

onMounted(async () => {
    await fetchPosts();
    await fetchSavedLyrics();
    console.log('Last post:', lastPost.value?.content);
    sampleText.value = lastPost.value?.content || 'vide';
});

const formatLyrics = (content) => {
    // Nettoyer le contenu des guillemets et des émojis
    let cleanContent = content.replace(/^["']|["']$/g, '').trim();
    cleanContent = cleanContent.replace(/[^\w\s.,!?-]/g, ''); // Garder uniquement les caractères pertinents

    // Séparer les paroles en strophes
    formattedLyrics.value = cleanContent
        .split('\n\n')
        .map(stanza => stanza
            .split('\n')
            .filter(line => line.trim())
            .map(line => line.trim())
        )
        .filter(stanza => stanza.length > 0);

    // Extraire la clé et le genre si présents
    const lastStanza = formattedLyrics.value[formattedLyrics.value.length - 1];
    if (lastStanza && lastStanza[0].toLowerCase().includes('key of')) {
        const keyMatch = lastStanza[0].match(/key of (.*?)\./i);
        songKey.value = keyMatch ? keyMatch[1] : '';
        formattedLyrics.value.pop(); // Retirer la strophe des informations
    }

    if (lastStanza && lastStanza[0].toLowerCase().includes('genre')) {
        const genreMatch = lastStanza[0].match(/genre that (.*?)\./i);
        songGenre.value = genreMatch ? genreMatch[1] : '';
        formattedLyrics.value.pop(); // Retirer la strophe des informations
    }
};

const handleGeneration = async () => {
    try {
        loading.value = true;
        // const response = await fetch('http://localhost:8080/api/llm/generate-lyrics', {
        //     method: 'POST',
        //     headers: {
        //         'Content-Type': 'application/json'
        //     },
        //     body: JSON.stringify({ text: sampleText.value })
        // });

        // if (!response.ok) {
        //     throw new Error('Erreur lors de la génération des paroles');
        // }

        // const data = await response.json();
        const data = {
            id: 'chatcmpl-exn5vzbp7954iajfj22hp',
            object: 'chat.completion',
            created: 1742523597,
            model: 'mathstral-7b-v0.1',
            choices: [
                {
                    index: 0,
                    logprobs: null,
                    finish_reason: 'stop',
                    message: {
                        role: 'assistant',
                        content:
                            "\"Sunshine on your face,\nLife's like a sweet embrace.\nBask in the golden rays of joy,\nDance to the rhythm of happiness that blooms.\n\nIn the garden of life, you sway,\nLiving each moment as it may.\nThe world is full of color and song,\nSo sing out loud, let your voice belong.\n\nWith every beat of your heart,\nYou're painting the world with art.\nCelebrate the sunshine today,\nFor life's a beautiful way!\" 🌞"
                    }
                }
            ],
            usage: {
                prompt_tokens: 113,
                completion_tokens: 120,
                total_tokens: 233
            },
            stats: {},
            system_fingerprint: 'mathstral-7b-v0.1'
        };
        result.value = data.choices[0].message.content;
        formatLyrics(result.value);
    } catch (error) {
        console.error('Erreur:', error);
    } finally {
        loading.value = false;
    }
};

const saveLyrics = async () => {
    try {
        const lyricsData = {
            user: await getUserProfile(localStorage.getItem('userId'), localStorage.getItem('token')),
            type: 'lyrics',
            content: result.value.lyrics,
            generatedBy: 'algorithm',
            createdAt: new Date()
        };
        await addRecommendation(lyricsData);
        await fetchSavedLyrics();
        console.log('Lyrics saved:', lyricsData);
    } catch (error) {
        console.error('Error saving lyrics:', error);
    }
};
</script>

<style scoped>
/* Main Container */
.main-container {
    background: #f8fafc;
    min-height: 100vh;
}

/* Section Titles */
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
    content: '';
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

/* Lyrics Section */
.lyrics-section {
    background: linear-gradient(135deg, #f0f4ff 0%, #f8fafc 100%);
    border-radius: 16px;
    border: 2px solid #e0e7ff;
    margin: 2rem 0;
    position: relative;
    overflow: hidden;
}

.lyrics-text {
    font-size: 1.2rem;
    color: #1e40af;
    padding: 1.5rem;
    margin: 0;
    line-height: 1.8;
    white-space: pre-wrap;
    font-family: 'Courier New', Courier, monospace;
}

/* Loading Section */
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
    font-size: 1.5rem;
    margin-top: 2rem;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
}

/* Music Loader Animation */
.music-loader {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 20px;
    font-size: 3rem;
}

.note {
    opacity: 0;
    animation: floatNote 2s infinite;
}

.note-1 {
    animation-delay: 0s;
}
.note-2 {
    animation-delay: 0.5s;
}
.note-3 {
    animation-delay: 1s;
}
.note-4 {
    animation-delay: 1.5s;
}

@keyframes floatNote {
    0%,
    100% {
        transform: translateY(0) rotate(0deg);
        opacity: 0;
    }
    50% {
        transform: translateY(-20px) rotate(10deg);
        opacity: 1;
    }
}

/* Button Styles */
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

.lyrics-card {
    background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
    border: 2px solid #e0e7ff;
    border-radius: 16px;
    overflow: hidden;
}

.lyrics-container {
    padding: 2rem;
}

.lyrics-content {
    font-family: 'Georgia', serif;
    color: #1e293b;
    line-height: 1.8;
}

.stanza {
    margin-bottom: 2rem;
}

.stanza:last-child {
    margin-bottom: 0;
}

.lyrics-line {
    margin: 0.5rem 0;
    font-size: 1.1rem;
    text-align: center;
    color: #334155;
    transition: all 0.3s ease;
}

.lyrics-line:hover {
    color: #3b82f6;
    transform: scale(1.02);
}

.song-info {
    margin-top: 2rem;
    padding-top: 1.5rem;
    border-top: 1px solid #e0e7ff;
    display: flex;
    justify-content: center;
    gap: 2rem;
}

.info-item {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    color: #64748b;
    font-size: 0.9rem;
}

.info-item i {
    color: #3b82f6;
}

/* Animation pour l'apparition des strophes */
.stanza {
    opacity: 0;
    animation: fadeInUp 0.5s ease forwards;
}

@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* Délai d'animation pour chaque strophe */
.stanza:nth-child(1) {
    animation-delay: 0.1s;
}
.stanza:nth-child(2) {
    animation-delay: 0.2s;
}
.stanza:nth-child(3) {
    animation-delay: 0.3s;
}
.stanza:nth-child(4) {
    animation-delay: 0.4s;
}
</style>
