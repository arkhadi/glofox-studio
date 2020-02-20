package com.glofox.app.studio.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glofox.app.studio.entity.Booking;
import com.glofox.app.studio.service.BookingService;
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
@WebMvcTest(BookingsController.class)
class BookingsControllerTest {
    @MockBean
    private BookingService bookingService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldCreateBooking() throws Exception {
        //Given
        Booking booking = new Booking();
        booking.setName("name");

        given(bookingService.saveBooking(any(Booking.class))).willReturn(booking);

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
        Booking booking = new Booking();
        booking.setName("name");

        Booking updatedBooking = new Booking();
        updatedBooking.setName("updatedName");

        given(bookingService.updateBooking(any(Booking.class))).willReturn(updatedBooking);

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