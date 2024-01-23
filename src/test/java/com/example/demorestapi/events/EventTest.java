package com.example.demorestapi.events;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;



class EventTest {
    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        //given
        String name = "Spring";
        String description = "Event";

        //when
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        //then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }


//    @CsvSource({
//            "0,0,true",
//            "0,100,false",
//            "100,0,false"
//    })
    @ParameterizedTest
    @MethodSource("paramsForTestFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        //given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        //when
        event.update();
        //then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private static Object[] paramsForTestFree() {
        return new Object[] {
                new Object[] {0,0,true},
                new Object[] {0,100, false},
                new Object[] {100,0,false},
                new Object[] {100,200,false}
        };
    }


    @ParameterizedTest
    @MethodSource("parametersForTestOffline")
    public void testOffline(String location, boolean isOffline) {
        //given
        Event event = Event.builder()
                .location(location)
                .build();
        //when
        event.update();
        //then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private static Object[] parametersForTestOffline() {
        return new Object[] {
                new Object[] {"강남역",true},
                new Object[] {"   ",false},
                new Object[] {null, false}
        };
    }

}