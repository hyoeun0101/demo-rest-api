package com.example.demorestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class EventValidator {
    public void validate(EventDto eventDto, Errors errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
            errors.rejectValue("basePrice", "wrongValue", "basePrice is wrong");
            errors.rejectValue("maxPrice", "wrongValue", "maxPrice is wrong");
        }

        if (eventDto.getEndEventDateTime().isBefore(eventDto.getBeginEventDateTime()) ||
                eventDto.getEndEventDateTime().isBefore(eventDto.getBeginEnrollmentDateTime()) ||
                eventDto.getEndEventDateTime().isBefore(eventDto.getCloseEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");
        }

        if (eventDto.getBeginEventDateTime().isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("beginEventDateTime","wrongValue","beginEventDateTime is wrong");
        }

    }

}
