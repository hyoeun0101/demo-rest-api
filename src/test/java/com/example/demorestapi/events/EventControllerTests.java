package com.example.demorestapi.events;

import com.example.demorestapi.common.RestDocsConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class EventControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

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
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists());


    }


}
