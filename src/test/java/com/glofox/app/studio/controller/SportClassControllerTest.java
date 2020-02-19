package com.glofox.app.studio.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glofox.app.studio.entity.SportClass;
import com.glofox.app.studio.service.SportClassService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SportClassController.class)
class SportClassControllerTest {

    @MockBean
    private SportClassService sportClassService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldCreateSportClass() throws Exception {
        //Given
        SportClass sportClass = new SportClass();
        sportClass.setName("name");

        given(sportClassService.saveSportClass(any(SportClass.class))).willReturn(sportClass);

        //When
        MvcResult result = mvc.perform(post("/classes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sportClass)))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        SportClass resultObject =  objectMapper.readValue(result.getResponse().getContentAsString(), SportClass.class);
        assertThat(resultObject.getName()).isEqualTo("name");
    }

    @Test
    void shouldGetSportClass() throws Exception {
        //Given
        SportClass sportClass = new SportClass();
        sportClass.setName("name");

        given(sportClassService.findAllSportClasses()).willReturn(Collections.singletonList(sportClass));

        //When
        MvcResult result = mvc.perform(get("/classes"))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        List<SportClass> resultObject = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<SportClass>>(){});
        assertThat(resultObject.get(0).getName()).isEqualTo("name");
    }

    @Test
    void shouldGetSportClassByNameStatusOk() throws Exception {
        //Given
        SportClass sportClass = new SportClass();
        sportClass.setName("name");

        given(sportClassService.findSportClassByName("name")).willReturn(Optional.of(sportClass));

        //When
        MvcResult result = mvc.perform(get("/classes/name"))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        SportClass resultObject =  objectMapper.readValue(result.getResponse().getContentAsString(), SportClass.class);
        assertThat(resultObject.getName()).isEqualTo("name");
    }

    @Test
    void shouldGetSportClassByNameStatusNotFound() throws Exception {
        //Given
        given(sportClassService.findSportClassByName("name")).willReturn(Optional.empty());

        //When
        //Then
        mvc.perform(get("/classes/name"))
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    void shouldUpdateSportClass() throws Exception {
        //Given
        SportClass sportClass = new SportClass();
        sportClass.setName("name");

        SportClass updatedSportClass = new SportClass();
        updatedSportClass.setName("updatedName");

        given(sportClassService.updateSportClass(any(SportClass.class))).willReturn(updatedSportClass);

        //When
        MvcResult result = mvc.perform(put("/classes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sportClass)))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        SportClass resultObject =  objectMapper.readValue(result.getResponse().getContentAsString(), SportClass.class);
        assertThat(resultObject.getName()).isEqualTo("updatedName");
    }

    @Test
    void shouldDeleteSportClassByNameStatusOk() throws Exception {
        //Given
        given(sportClassService.deleteSportClass("name")).willReturn(true);

        //When
        MvcResult result = mvc.perform(delete("/classes/name"))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        String resultObject =  result.getResponse().getContentAsString();
        assertThat(resultObject).isEqualTo("Sport Class with name: name successfully deleted");
    }

    @Test
    void shouldDeleteSportClassByNameStatusNotFound() throws Exception {
        //Given
        given(sportClassService.deleteSportClass("name")).willReturn(false);

        //When
        //Then
        mvc.perform(delete("/classes/name"))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}