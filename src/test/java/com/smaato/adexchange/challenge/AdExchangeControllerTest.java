package com.smaato.adexchange.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AdExchangeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Value(value = "${local.server.port}")
    private int port;

    @Test
    public void givenOnlyId_whenProcessId_thenReturnOk() throws Exception {
        // given
        Integer id = new Integer(489349);
        // when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/smaato/accept?id=" + id)
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
        // when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/smaato/accept")
                .accept(MediaType.TEXT_PLAIN_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals(400, status);
    }

    @Test
    public void givenIdAndEndpoint_whenProcessId_thenReturnOk() throws Exception {
        // given
        Integer id = new Integer(111349);
        String endpoint = "http://localhost:" + port + "/api/v1/ping";
        // when
        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.get("/api/smaato/accept?id=" + id + "&endpoint=" + endpoint)
                        .accept(MediaType.TEXT_PLAIN_VALUE))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals(200, status);
        assertEquals(content, "ok");
    }

}
