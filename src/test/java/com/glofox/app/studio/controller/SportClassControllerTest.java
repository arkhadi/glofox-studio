package com.glofox.app.studio.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glofox.app.studio.entity.SportClass;
import com.glofox.app.studio.service.SportClassService;
import com.glofox.app.studio.validator.SportClassValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
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

    @MockBean
    private SportClassValidator sportClassValidator;


    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        objectMapper = builder.modulesToInstall(new JavaTimeModule()).build();
    }

    @Test
    void shouldCreateSportClass() throws Exception {
        //Given
        LocalDate date = LocalDate.of(2020, 2, 20);
        SportClass sportClass = new SportClass();
        sportClass.setName("name");
        sportClass.setCapacity(20);
        sportClass.setStartDate(date);
        sportClass.setEndDate(date);

        given(sportClassValidator.validate(sportClass)).willReturn(null);
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
        assertThat(resultObject.getCapacity()).isEqualTo(20);
        assertThat(resultObject.getStartDate()).isEqualTo(date);
        assertThat(resultObject.getEndDate()).isEqualTo(date);
    }

    @Test
    void shouldReturnBadRequestWhenCreateSportClassFailsToValidate() throws Exception {
        //Given
        SportClass sportClass = new SportClass();

        //When

        MvcResult result = mvc.perform(post("/classes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sportClass)))
                .andExpect(status().isBadRequest())
                .andReturn();
        //Then
        assertThat(result).isNotNull();
        assertThat(result.getResponse().getContentAsString()).contains("End Date is mandatory", "Name is mandatory", "Start Date is mandatory", "Capacity is mandatory");
    }

    @Test
    void shouldReturnBadRequestWhenCreateSportClassFailsToValidateDueToOverlappingDates() throws Exception {
        //Given
        LocalDate date = LocalDate.of(2020, 2, 20);
        SportClass sportClass = new SportClass();
        sportClass.setName("name");
        sportClass.setCapacity(20);
        sportClass.setStartDate(date);
        sportClass.setEndDate(date);

        given(sportClassValidator.validate(any(SportClass.class))).willReturn("Failed to validate Dates");

        //When
        MvcResult result = mvc.perform(post("/classes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sportClass)))
                .andExpect(status().isBadRequest())
                .andReturn();
        //Then
        assertThat(result).isNotNull();
        assertThat(result.getResponse().getContentAsString()).contains("Failed to validate Dates");
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
        sportClass.setId(1);
        sportClass.setName("name");

        given(sportClassService.findSportClassById(1)).willReturn(Optional.of(sportClass));

        //When
        MvcResult result = mvc.perform(get("/classes/1"))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        SportClass resultObject =  objectMapper.readValue(result.getResponse().getContentAsString(), SportClass.class);
        assertThat(resultObject.getId()).isEqualTo(1);
        assertThat(resultObject.getName()).isEqualTo("name");
    }

    @Test
    void shouldGetSportClassByNameStatusNotFound() throws Exception {
        //Given
        given(sportClassService.findSportClassById(1)).willReturn(Optional.empty());

        //When
        //Then
        mvc.perform(get("/classes/1"))
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    void shouldUpdateSportClass() throws Exception {
        //Given
        LocalDate date = LocalDate.of(2020, 2, 20);
        SportClass sportClass = new SportClass();
        sportClass.setName("name");
        sportClass.setCapacity(20);
        sportClass.setStartDate(date);
        sportClass.setEndDate(date);

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
    void shouldReturnBadRequestWhenUpdateSportClassFailsToValidate() throws Exception {
        //Given
        SportClass sportClass = new SportClass();

        //When

        MvcResult result = mvc.perform(put("/classes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sportClass)))
                .andExpect(status().isBadRequest())
                .andReturn();
        //Then
        assertThat(result).isNotNull();
        assertThat(result.getResponse().getContentAsString()).contains("End Date is mandatory", "Name is mandatory", "Start Date is mandatory", "Capacity is mandatory");

    }

    @Test
    void shouldDeleteSportClassByNameStatusOk() throws Exception {
        //Given
        given(sportClassService.deleteSportClass(1)).willReturn(true);

        //When
        MvcResult result = mvc.perform(delete("/classes/1"))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        String resultObject =  result.getResponse().getContentAsString();
        assertThat(resultObject).isEqualTo("Sport Class with id: 1 successfully deleted");
    }

    @Test
    void shouldDeleteSportClassByNameStatusNotFound() throws Exception {
        //Given
        given(sportClassService.deleteSportClass(1)).willReturn(false);

        //When
        //Then
        mvc.perform(delete("/classes/1"))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}