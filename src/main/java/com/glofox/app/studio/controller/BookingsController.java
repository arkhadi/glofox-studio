package com.glofox.app.studio.controller;

import com.glofox.app.studio.entity.Booking;
import com.glofox.app.studio.service.BookingService;
import com.sun.javafx.binding.StringFormatter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("bookings")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookingsController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking>  createBooking(@RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.saveBooking(booking));
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBookings() {
        return ResponseEntity.ok(bookingService.findAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable  Integer id) {
         Optional<Booking> booking = bookingService.findBookingBookingById(id);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<Booking> updateBooking(@RequestBody Booking booking) {
        return  ResponseEntity.ok(bookingService.updateBooking(booking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Integer id) {
        if(bookingService.deleteBooking(id)) {
            return ResponseEntity.ok(StringFormatter.format("Booking with id: %s successfully deleted", id).getValue());
        }
        return ResponseEntity.notFound().build();
    }

}
