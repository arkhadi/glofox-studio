package com.glofox.app.studio.service;

import com.glofox.app.studio.entity.Booking;
import com.glofox.app.studio.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    private BookingService classToTest;

    @BeforeEach
    public void setUp() {
        classToTest = new BookingService(bookingRepository);
    }

    @Test
    void shouldFindAllBookings() {
        //Given
        Booking booking = new Booking();
        List<Booking> bookings = Collections.singletonList(booking);
        given(bookingRepository.findAll()).willReturn(bookings);

        //When
        List<Booking> result = classToTest.findAllBookings();

        //Then
        assertThat(result).isNotNull();
        assertThat(result.get(0)).isEqualTo(booking);

    }

    @Test
    void shouldFindBookingById() {
        //Given
        Booking booking = new Booking();
        booking.setName("name");

        given(bookingRepository.findById(eq(1))).willReturn(Optional.of(booking));

        //When
        Optional<Booking> result = classToTest.findBookingBookingById(1);

        //Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(booking);
    }

    @Test
    void shouldFindBookingByIdReturnEmptyOptionalWhenNotFound() {
        //Given

        given(bookingRepository.findById(eq(1))).willReturn(Optional.empty());

        //When
        Optional<Booking> result = classToTest.findBookingBookingById(1);

        //Then
        assertThat(result).isNotPresent();
    }


    @Test
    void shouldSaveBooking() {
        //Given
        Booking booking = new Booking();

        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        given(bookingRepository.save(captor.capture())).willReturn(booking);

        //When
        Booking result = classToTest.saveBooking(booking);

        //Then
        verify(bookingRepository).save(booking);
        assertThat(result).isEqualTo(captor.getValue());
    }

    @Test
    void shouldUpdateBooking() {
        //Given
        Booking booking = new Booking();
        booking.setId(1);

        Booking updatedBooking = new Booking();
        updatedBooking.setId(1);

        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        given(bookingRepository.existsById(1)).willReturn(true);
        given(bookingRepository.save(captor.capture())).willReturn(updatedBooking);

        //When
        Booking result = classToTest.updateBooking(booking);

        //Then
        verify(bookingRepository).save(booking);
        assertThat(captor.getValue()).isEqualTo(booking);
        assertThat(result).isEqualTo(updatedBooking);
    }

    @Test
    void shouldNotUpdateBooking() {
        //Given
        Booking booking = new Booking();
        booking.setId(1);

        given(bookingRepository.existsById(1)).willReturn(false);

        //When
        Booking result = classToTest.updateBooking(booking);

        //Then
        verify(bookingRepository).existsById(1);
        verify(bookingRepository, times(0)).save(any());
    }

    @Test
    void shouldDeleteBooking() {
        //Given
        Booking booking = new Booking();
        booking.setId(1);

        Mockito.doNothing().when(bookingRepository).deleteById(1);

        //When
        boolean result = classToTest.deleteBooking(1);

        //Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldNotDeleteBooking() {
        //Given
        Booking booking = new Booking();
        booking.setId(1);

        Mockito.doThrow(EmptyResultDataAccessException.class).when(bookingRepository).deleteById(1);

        //When
        boolean result = classToTest.deleteBooking(1);

        //Then
        assertThat(result).isFalse();
    }

}