package com.glofox.app.studio.validator;

import com.glofox.app.studio.entity.Booking;
import com.glofox.app.studio.entity.SportClass;
import com.glofox.app.studio.service.SportClassService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.List;

@Component("beforeSaveBookingValidator")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookingValidator implements Validator {

    private final SportClassService sportClassService;


    @Override
    public boolean supports(Class<?> aClass) {
        return Booking.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Booking booking = (Booking) o;
        List<SportClass> sportClasses = sportClassService.findAllSportClasses();
        boolean existsClass = false;
        for(SportClass sportClass : sportClasses) {
            if(isDateInRange(booking.getDate(), sportClass.getStartDate(), sportClass.getEndDate())) {
                existsClass = true;
                break;
            }
        }
        if(!existsClass) {
            errors.rejectValue("date", "No class available the requested day");
        }
    }

    private boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
