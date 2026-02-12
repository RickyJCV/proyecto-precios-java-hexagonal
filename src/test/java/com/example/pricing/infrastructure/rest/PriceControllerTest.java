package com.example.pricing.infrastructure.rest;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 2020-06-14 10:00
    @Test
    void test1_14June10h_brand1_product35455() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList", is(1)))
                .andExpect(jsonPath("$.price", is(35.50)));
    }

    // 2020-06-14 16:00
    @Test
    void test2_14June16h_brand1_product35455() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "2020-06-14T16:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList", is(2)))
                .andExpect(jsonPath("$.price", is(25.45)));
    }

    // 2020-06-14 21:00
    @Test
    void test3_14June21h_brand1_product35455() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "2020-06-14T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList", is(1)))
                .andExpect(jsonPath("$.price", is(35.50)));
    }

    // 2020-06-15 10:00
    @Test
    void test4_15June10h_brand1_product35455() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "2020-06-15T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList", is(3)))
                .andExpect(jsonPath("$.price", is(30.50)));
    }

    // 2020-06-16 21:00
    @Test
    void test5_16June21h_brand1_product35455() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "2020-06-16T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList", is(4)))
                .andExpect(jsonPath("$.price", is(38.95)));
    }

    @Test
    void shouldReturnNotFoundWhenPriceDoesNotExist() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "2019-01-01T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Price Not Found")))
                .andExpect(jsonPath("$.details", containsString("No applicable price found")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "99999")
                        .param("brandId", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Price Not Found")))
                .andExpect(jsonPath("$.details", containsString("No applicable price found")));
    }

    @Test
    void shouldReturnBadRequestWhenApplicationDateIsMissing() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Invalid Request")))
                .andExpect(jsonPath("$.details", containsString("applicationDate")));
    }

    @Test
    void shouldReturnBadRequestWhenProductIdIsMissing() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Invalid Request")))
                .andExpect(jsonPath("$.details", containsString("productId")));
    }

    @Test
    void shouldReturnBadRequestWhenBrandIdIsMissing() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Invalid Request")))
                .andExpect(jsonPath("$.details", containsString("brandId")));
    }

    @Test
    void shouldReturnBadRequestWhenDateFormatIsInvalid() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "2020-06-14")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Invalid Parameter Type")))
                .andExpect(jsonPath("$.details", containsString("applicationDate")));
    }

    @Test
    void shouldReturnBadRequestWhenProductIdIsNotNumeric() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "abc")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Invalid Parameter Type")))
                .andExpect(jsonPath("$.details", containsString("productId")));
    }

    @Test
    void shouldReturnBadRequestWhenBrandIdIsNotNumeric() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "xyz"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Invalid Parameter Type")))
                .andExpect(jsonPath("$.details", containsString("brandId")));
    }
}
