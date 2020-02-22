package com.glofox.app.studio.controller;

import com.glofox.app.studio.entity.Booking;
import com.glofox.app.studio.service.BookingService;
import com.glofox.app.studio.validator.BookingValidator;
import com.sun.javafx.binding.StringFormatter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("bookings")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookingsController {

    private final BookingService bookingService;

    private final BookingValidator bookingValidator;

    @PostMapping
    public ResponseEntity  createBooking(@Valid @RequestBody Booking booking) {
        String validationErrors = bookingValidator.validate(booking);
        if(validationErrors != null) {
            return ResponseEntity.badRequest().body(validationErrors);
        }
        return ResponseEntity.ok(bookingService.createBooking(booking));
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
    public ResponseEntity<Booking> updateBooking(@Valid @RequestBody Booking booking) {
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
