package com.example.demorestapi.events;

import com.example.demorestapi.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTest extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Test
    @DisplayName("정상적으로 이벤트를 생성")
    public void createEvent() throws Exception {
        //given
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2024,1,1,12,0,0))
                .closeEnrollmentDateTime(LocalDateTime.of(2024,1,2,12,0,0))
                .beginEventDateTime(LocalDateTime.of(2024,1,1,12,0,0))
                .endEventDateTime(LocalDateTime.of(2024,1,2,12,0,0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                //TODO[KHE] - 응답 시 location 한글 깨짐 현상 해결해야함.
                .location("강남역 D2 스타텁 팩토리")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(eventDto)));

        //then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.toString()))
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update events"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("name").description("이벤트 이름"),
                                fieldWithPath("description").description("이벤트 설명"),
                                fieldWithPath("beginEnrollmentDateTime").description("이벤트 등록 시작 시간,날짜"),
                                fieldWithPath("closeEnrollmentDateTime").description("이벤트 등록 종료 시간,날짜"),
                                fieldWithPath("beginEventDateTime").description("이벤트 시작 시간,날짜"),
                                fieldWithPath("endEventDateTime").description("이벤트 종료 시간,날짜"),
                                fieldWithPath("location").description("이벤트 위치"),
                                fieldWithPath("basePrice").description("이벤트 기본 가격"),
                                fieldWithPath("maxPrice").description("이벤트 최대 가격"),
                                fieldWithPath("limitOfEnrollment").description("등록 제한 수")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("loaction header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("id of new Event"),
                                fieldWithPath("name").description("이벤트 이름"),
                                fieldWithPath("description").description("이벤트 설명"),
                                fieldWithPath("beginEnrollmentDateTime").description("이벤트 등록 시작 시간,날짜"),
                                fieldWithPath("closeEnrollmentDateTime").description("이벤트 등록 종료 시간,날짜"),
                                fieldWithPath("beginEventDateTime").description("이벤트 시작 시간,날짜"),
                                fieldWithPath("endEventDateTime").description("이벤트 종료 시간,날짜"),
                                fieldWithPath("location").description("이벤트 위치"),
                                fieldWithPath("basePrice").description("이벤트 기본 가격"),
                                fieldWithPath("maxPrice").description("이벤트 최대 가격"),
                                fieldWithPath("limitOfEnrollment").description("등록 제한 수"),
                                fieldWithPath("offline").description("오프라인 유무, location이 없으면 오프라인"),
                                fieldWithPath("free").description("무료 유무, 가격이 0이면 무료"),
                                fieldWithPath("eventStatus").description("이벤트 상태, 기본값"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query events"),
                                fieldWithPath("_links.update-event.href").description("link to update event"),
                                fieldWithPath("_links.profile.href").description("link to profile")

                        )
                ));

    }

//    @Test
//    public void restrict_Input_value() throws Exception {
//        //given
//        Event event = Event.builder()
//                .id(100)
//                .name("Spring")
//                .description("REST API Development with Spring")
//                .beginEnrollmentDateTime(LocalDateTime.of(2024,1,1,12,0,0))
//                .closeEnrollmentDateTime(LocalDateTime.of(2024,1,2,12,0,0))
//                .beginEventDateTime(LocalDateTime.of(2024,1,1,12,0,0))
//                .endEventDateTime(LocalDateTime.of(2024,1,2,12,0,0))
//                .basePrice(100)
//                .maxPrice(200)
//                .limitOfEnrollment(100)
//                .location("강남역 D2 스타텁 팩토리")
//                .free(true)
//                .offline(false)
//                .eventStatus(EventStatus.PUBLISHED)
//                .build();
//
//        //when
//        ResultActions resultActions = mockMvc.perform(post("/api/events")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaTypes.HAL_JSON)
//                .content(objectMapper.writeValueAsString(event)));
//
//        //then
//        resultActions.andDo(print())
//                .andExpect(jsonPath("id").value(Matchers.not(100)))
//                .andExpect(jsonPath("free").value(false))
//                .andExpect(jsonPath("offline").value(false))
//                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));
//    }


    @Test
    @DisplayName("알 수 없는 입력 값이 있을 경우에 에러 발생")
    public void createEvent_BadRequest_When_unknowing_properties() throws Exception {
        //given
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2024,1,1,12,0,0))
                .closeEnrollmentDateTime(LocalDateTime.of(2024,1,2,12,0,0))
                .beginEventDateTime(LocalDateTime.of(2024,1,1,12,0,0))
                .endEventDateTime(LocalDateTime.of(2024,1,2,12,0,0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .offline(false)
                .free(true)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)));

        //then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("빈 입력 값인 경우에 에러 발생")
    public void createEvent_BadRequest_Empty_Input() throws Exception {
        //given
        EventDto eventDto = EventDto.builder().build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잘못된 입력 값인 경우에 에러 발생")
    public void createEvent_BadRequest_Wrong_Input() throws Exception {
        //given
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2024,1,10,12,0,0))
                .closeEnrollmentDateTime(LocalDateTime.of(2024,1,1,12,0,0))
                .beginEventDateTime(LocalDateTime.of(2024,1,2,12,0,0))
                .endEventDateTime(LocalDateTime.of(2024,1,1,12,0,0))
                .basePrice(3000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception {
        //given
        IntStream.range(0, 30).forEach(this::generateEvent);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,DESC")
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"));
        //TODO[KHE] - 문서화 필요

    }


    @Test
    @DisplayName("기존의 이벤트를 하나 조회하기")
    public void getEvent() throws Exception{
        //given
        Event event = this.generateEvent(100);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/events/{id}", event.getId()));

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"));
        //TODO[KHE] - 문서화 필요
    }

    @Test
    @DisplayName("없는 이벤트 조회한 경우 404 에러")
    public void getEvent_404() throws Exception{
        //when & then
        mockMvc.perform(get("/api/events/123123123"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("이벤트 수정 성공")
    public void updateEvent() throws Exception {
        //given
        Event event = this.generateEvent(1);
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        String eventName = "updated event name";
        eventDto.setName(eventName);

        //when & then
        mockMvc.perform(put("/api/events/{id}",event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists());
    }

    @Test
    @DisplayName("입력값이 빈 경우에 이벤트 수정 실패")
    public void updateEvent400_empty() throws Exception {
        //given
        Event event = this.generateEvent(1);
        EventDto eventDto = EventDto.builder().build();

        //when & then
        mockMvc.perform(put("/api/events/{id}",event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("_links.index").exists());
    }

    @Test
    @DisplayName("입력값이 잘못된 경우에 이벤트 수정 실패")
    public void updateEvent400_wrong() throws Exception {
        //given
        Event event = this.generateEvent(1);
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(2000);
        eventDto.setMaxPrice(20);

        //when & then
        mockMvc.perform(put("/api/events/{id}",event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("_links.index").exists());
    }

    @Test
    @DisplayName("존재하지 않는 이벤트를 수정하는 경우 실패")
    public void updateEvent404() throws Exception {
        //given
        Event event = this.generateEvent(1);
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        String eventName = "updated Event";
        eventDto.setName(eventName);

        //when & then
        mockMvc.perform(put("/api/events/9999",event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private Event generateEvent(int i) {
        Event event = Event.builder()
                .name("event" + i)
                .description("test event")
                .beginEnrollmentDateTime(LocalDateTime.of(2024,1,1,12,0,0))
                .closeEnrollmentDateTime(LocalDateTime.of(2024,1,2,12,0,0))
                .beginEventDateTime(LocalDateTime.of(2024,1,1,12,0,0))
                .endEventDateTime(LocalDateTime.of(2024,1,2,12,0,0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();

        return eventRepository.save(event);
    }
}
