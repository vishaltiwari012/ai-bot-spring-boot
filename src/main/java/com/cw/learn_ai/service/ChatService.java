package com.cw.learn_ai.service;

import com.cw.learn_ai.payload.CricketResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatModel chatModel;
    private final ImageModel imageModel;
    private final StreamingChatModel streamingChatModel;

    public String generateText(String inputText) {
        return chatModel.call(inputText);
    }

    public Flux<String> streamResponse(String inputText) {
        return chatModel.stream(inputText);
    }

    public CricketResponse generateCricketResponse(String inputText) throws IOException {
        String template = this.loadPromptFromTemplate("prompts/cricket_bot.txt");
        String prompt = this.putValuesInPromptTemplate(template, Map.of("inputText", inputText));
        ChatResponse cricketResponse = chatModel.call(
                new Prompt(prompt)
        );

//        get content as string
        String responseString = cricketResponse.getResult().getOutput().getContent();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(responseString, CricketResponse.class);
    }

//    load prompt from class path

    public List<String> generateImages(String description, int numbers) throws IOException {
        String template = this.loadPromptFromTemplate("prompts/image_bot.txt");
        String promptString = this.putValuesInPromptTemplate(template, Map.of(
                "description", description
        ));

        ImageResponse response = imageModel.call(new ImagePrompt(promptString,
                OpenAiImageOptions.builder()
                        .withModel("dall-e-2")
                        .withN(1)
                        .withWidth(512)
                        .withHeight(512)
                        .withQuality("standard").build()));
        List<String> imageUrls = response.getResults().stream().map(
                generation -> generation.getOutput().getUrl()
        ).collect(Collectors.toList());
        return imageUrls;
    }

    public String loadPromptFromTemplate(String filename) throws IOException {
        Path filePath = Path.of(new ClassPathResource(filename)
                .getFile().getPath());

        return Files.readString(filePath);
    }


//    put values to prompt
    public String putValuesInPromptTemplate(String template, Map<String, String> values) {
        for(Map.Entry<String, String> e : values.entrySet()) {
            template = template.replace("{"+e.getKey()+"}", e.getValue());
        }
        return template;
    }
}
