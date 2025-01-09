package com.cw.learn_ai;

import com.cw.learn_ai.payload.CricketResponse;
import com.cw.learn_ai.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@SpringBootTest
class LearnAiApplicationTests {
	@Autowired
	private ChatService chatService;

	@Test
	void contextLoads() throws JsonProcessingException {

	}

	@Test
	void testTemplate() throws IOException {
		String s = chatService.loadPromptFromTemplate("prompts/cricket_bot.txt");
		String prompt = chatService.putValuesInPromptTemplate(s, Map.of("inputText", "what is cricket"));
		System.out.println(prompt);
	}

}
