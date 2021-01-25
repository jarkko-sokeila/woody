package com.sokeila.woody.backend.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sokeila.woody.backend.entity.Feed;
import com.sokeila.woody.backend.services.FeedRepository;
import com.sokeila.woody.backend.services.IPHashRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.Optional;

@WebMvcTest(FeedController.class)
public class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EntityManager entityManager;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private FeedRepository feedRepository;

    @MockBean
    private IPHashRepository ipHashRepository;

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

    @Test
    public void getFeedTest() throws Exception {
        // given
        Feed feed = new Feed();
        feed.setId(5L);
        when(feedRepository.findById(5L)).thenReturn(Optional.of(feed));

        // when
        ResultActions resultActions = this.mockMvc.perform(get("/rest/getfeed").param("id", "5"));

        // then
        resultActions.andDo(print()).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("5"));
    }
}
