package com.glofox.app.studio.service;

import com.glofox.app.studio.entity.Booking;
import com.glofox.app.studio.repository.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookingService {

    private final BookingRepository bookingRepository;

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> findBookingBookingById(Integer id) {
        return bookingRepository.findById(id);
    }

    public Booking updateBooking(Booking booking) {
        if(bookingRepository.existsById(booking.getId())) {
            return createBooking(booking);
        }
        return null;
    }

    public boolean deleteBooking(Integer id) {
        try {
            bookingRepository.deleteById(id);
            return true;
        } catch (DataAccessException dae) {
            return false;
        }
    }
}
