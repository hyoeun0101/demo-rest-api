package com.example.demorestapi.events;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {
        //id 값 세팅
        event.setId(10);

        //DB에 저장
        Event newEvent = eventRepository.save(event);

        URI uri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
//        URI uri = linkTo(methodOn(EventController.class).createEvent()).toUri();

        ResponseEntity<Object> res = ResponseEntity.created(uri).body(event);
        return res;
    }
}
