package com.identa.catering.controller;

import com.identa.catering.model.OutputMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    public void setup() {
        doNothing().when(messagingTemplate).convertAndSendToUser(
                anyString(), anyString(), any(OutputMessage.class));
    }

    @Test
    public void testSendMessage() throws Exception {
        String username = "admin";
        String user = "testUser";

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.post("/hello")
                        .content(username))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that the messaging template's convertAndSendToUser was called
//        verify(messagingTemplate, times(1))
//                .convertAndSendToUser(eq(username), eq("/queue/messages"), any(OutputMessage.class));
    }
}


