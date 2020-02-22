package com.glofox.app.studio.validator;

import com.glofox.app.studio.entity.Booking;
import com.glofox.app.studio.entity.SportClass;
import com.glofox.app.studio.service.SportClassService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookingValidator {

    private final SportClassService sportClassService;

    public String validate(Booking booking) {
        List<SportClass> sportClasses = sportClassService.findAllSportClasses();
        boolean existsClass = false;
        for(SportClass sportClass : sportClasses) {
            if(isDateInRange(booking.getDate(), sportClass.getStartDate(), sportClass.getEndDate())) {
                existsClass = true;
                break;
            }
        }
        if(!existsClass){
            return "No class available that day";
        }
        return null;
    }

    private boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}


