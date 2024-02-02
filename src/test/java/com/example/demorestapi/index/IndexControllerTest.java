package com.example.demorestapi.index;

import com.example.demorestapi.common.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class IndexControllerTest extends BaseControllerTest {

    @Test
     public void index() throws Exception {
        //when
        ResultActions resultActions = this.mockMvc.perform(get("/api"));

        //then
        resultActions.andExpect(jsonPath("_links.events").exists());
    }


}
