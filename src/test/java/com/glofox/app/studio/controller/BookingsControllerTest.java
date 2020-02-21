package com.glofox.app.studio.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glofox.app.studio.entity.Booking;
import com.glofox.app.studio.entity.SportClass;
import com.glofox.app.studio.service.BookingService;
import com.glofox.app.studio.service.SportClassService;
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
@WebMvcTest(BookingsController.class)
class BookingsControllerTest {

    @MockBean
    private BookingService bookingService;

    @MockBean
    private SportClassService sportClassService;

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        objectMapper = builder.modulesToInstall(new JavaTimeModule()).build();
    }

    @Test
    void shouldCreateBooking() throws Exception {
        //Given
        LocalDate date = LocalDate.of(2020, 2, 20);

        Booking booking = new Booking();
        booking.setName("name");
        booking.setDate(date);

        SportClass sportClass = new SportClass();
        sportClass.setStartDate(LocalDate.of(2020, 1, 20));
        sportClass.setEndDate(LocalDate.of(2020, 3, 20));

        given(bookingService.createBooking(any(Booking.class))).willReturn(booking);
        given(sportClassService.findAllSportClasses()).willReturn(Collections.singletonList(sportClass));
        //When
        MvcResult result = mvc.perform(post("/bookings")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        Booking resultObject =  objectMapper.readValue(result.getResponse().getContentAsString(), Booking.class);
        assertThat(resultObject.getName()).isEqualTo("name");
        assertThat(resultObject.getDate()).isEqualTo(date);
    }

    @Test
    void shouldReturnBadRequestWhenNoClassThatDay() throws Exception {
        //Given
        LocalDate date = LocalDate.of(2020, 2, 20);

        Booking booking = new Booking();
        booking.setName("name");
        booking.setDate(date);

        SportClass sportClass = new SportClass();
        sportClass.setStartDate(LocalDate.of(2020, 2, 25));
        sportClass.setEndDate(LocalDate.of(2020, 3, 20));

        given(bookingService.createBooking(any(Booking.class))).willReturn(booking);
        given(sportClassService.findAllSportClasses()).willReturn(Collections.singletonList(sportClass));
        //When
        MvcResult result = mvc.perform(post("/bookings")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isBadRequest())
                .andReturn();
        //Then
        assertThat(result).isNotNull();
        assertThat(result.getResponse().getContentAsString()).contains("No class available that day");
    }

    @Test
    void shouldReturnBadRequestWhenCreateBookingValidationFails() throws Exception {
        //Given
        Booking booking = new Booking();
        given(bookingService.createBooking(any(Booking.class))).willReturn(booking);

        //When
        MvcResult result = mvc.perform(post("/bookings")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isBadRequest())
                .andReturn();

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getResponse().getContentAsString()).contains("Name is mandatory", "Date is mandatory");
    }

    @Test
    void shouldGetBookings() throws Exception {
        //Given
        Booking booking = new Booking();
        booking.setName("name");

        given(bookingService.findAllBookings()).willReturn(Collections.singletonList(booking));

        //When
        MvcResult result = mvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        List<Booking> resultObject = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Booking>>(){});
        assertThat(resultObject.get(0).getName()).isEqualTo("name");
    }

    @Test
    void shouldGetBookingById() throws Exception {
        //Given
        Booking booking = new Booking();

        given(bookingService.findBookingBookingById(1)).willReturn(Optional.of(booking));

        //When
        MvcResult result = mvc.perform(get("/bookings/1"))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        Booking resultObject = objectMapper.readValue(result.getResponse().getContentAsString(), Booking.class);
    }

    @Test
    void shouldGetBookingByIDReturnNotFound() throws Exception {
        //Given

        given(bookingService.findBookingBookingById(1)).willReturn(Optional.empty());

        //When
        //Then
        MvcResult result = mvc.perform(get("/bookings/1"))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    @Test
    void shouldUpdateBooking() throws Exception {
        //Given
        LocalDate date = LocalDate.of(2020, 2, 20);

        Booking booking = new Booking();
        booking.setName("name");
        booking.setDate(date);

        Booking updatedBooking = new Booking();
        updatedBooking.setName("updatedName");
        updatedBooking.setDate(date);

        SportClass sportClass = new SportClass();
        sportClass.setStartDate(LocalDate.of(2020, 2, 18));
        sportClass.setEndDate(LocalDate.of(2020, 3, 20));

        given(bookingService.updateBooking(any(Booking.class))).willReturn(updatedBooking);
        given(sportClassService.findAllSportClasses()).willReturn(Collections.singletonList(sportClass));

        //When
        MvcResult result = mvc.perform(put("/bookings")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        Booking resultObject =  objectMapper.readValue(result.getResponse().getContentAsString(), Booking.class);
        assertThat(resultObject.getName()).isEqualTo("updatedName");
        assertThat(resultObject.getDate()).isEqualTo(date);
    }

    @Test
    void shouldReturnBadRequestWhenUpdateBookingValidationFails() throws Exception {
        //Given
        Booking booking = new Booking();
        given(bookingService.createBooking(any(Booking.class))).willReturn(booking);

        //When
        MvcResult result = mvc.perform(put("/bookings")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isBadRequest())
                .andReturn();

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getResponse().getContentAsString()).contains("Name is mandatory", "Date is mandatory");
    }

    @Test
    void shouldDeleteSportClassByNameStatusOk() throws Exception {
        //Given
        given(bookingService.deleteBooking(1)).willReturn(true);

        //When
        MvcResult result = mvc.perform(delete("/bookings/1"))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        String resultObject =  result.getResponse().getContentAsString();
        assertThat(resultObject).isEqualTo("Booking with id: 1 successfully deleted");
    }

    @Test
    void shouldDeleteSportClassByNameStatusNotFound() throws Exception {
        //Given
        given(bookingService.deleteBooking(1)).willReturn(false);

        //When
        //Then
        mvc.perform(delete("/bookings/1"))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}