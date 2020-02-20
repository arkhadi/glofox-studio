package com.glofox.app.studio.repository;

import com.glofox.app.studio.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

}
