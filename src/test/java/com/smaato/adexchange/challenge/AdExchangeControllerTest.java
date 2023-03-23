package com.smaato.adexchange.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class AdExchangeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void givenId_whenProcessId_thenReturnOk() throws Exception {
        // given
        Integer id = new Integer(489349);
        String uri = "/api/smaato/accept?id=" + id;

        // when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.TEXT_PLAIN_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals(200, status);
        assertEquals(content, "ok");
    }

    @Test
    public void givenNoId_whenProcessId_thenReturnFailed() throws Exception {
        // given
        String uri = "/api/smaato/accept";

        // when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.TEXT_PLAIN_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals(400, status);
        assertEquals(content, "failed");
    }

    @Test
    public void givenIdAndEndpoint_whenProcessId_thenReturnOk() throws Exception {
        // given
        Integer id = new Integer(111349);
        String endpoint = "/api/v1/ping";
        String uri = "/api/smaato/accept?id=" + id + "&endpoint=" + endpoint;

        // when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.TEXT_PLAIN_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals(200, status);
        assertEquals(content, "ok");
    }

}
