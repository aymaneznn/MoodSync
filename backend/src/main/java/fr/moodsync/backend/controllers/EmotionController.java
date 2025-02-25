package fr.moodsync.backend.controllers;

import fr.moodsync.backend.models.EmotionResponse;
import fr.moodsync.backend.services.EmotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emotion")
public class EmotionController {

    @Autowired
    private EmotionService emotionService;

    @PostMapping("/analyze")
    public EmotionResponse analyzeText(@RequestBody String text) {
        return emotionService.analyzeEmotion(text);
    }
}