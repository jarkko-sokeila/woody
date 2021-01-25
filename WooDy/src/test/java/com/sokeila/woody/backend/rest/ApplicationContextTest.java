package com.sokeila.woody.backend.rest;

import com.sokeila.woody.backend.services.FeedRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationContextTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedRepository feedRepository;

    @Test
    public void unreadCountTest() throws Exception {
        // given
        String dateInMilliseconds = "1611595149303";
        Date date = new Date(Long.parseLong(dateInMilliseconds));
        when(feedRepository.countByCreatedGreaterThan(date)).thenReturn(10L);

        // when
        ResultActions resultActions = this.mockMvc.perform(get("/rest/unreadcount").param("datetime", dateInMilliseconds));

        // then
        resultActions.andDo(print()).andExpect(status().isOk()).andExpect(content().string(equalTo("10")));
    }
}
