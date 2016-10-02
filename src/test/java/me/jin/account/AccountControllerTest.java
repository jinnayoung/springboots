package me.jin.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jin.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by nayoung on 2016. 9. 23..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional //rollback
public class AccountControllerTest {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AccountService service;

    MockMvc mockMvn;

    @Before
    public void setUp() {
        mockMvn = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void createAccount() throws Exception {
        AccountDto.Create creatDto = new AccountDto.Create();
        creatDto.setUsername("soolbe");
        creatDto.setPassword("password");

        ResultActions result = mockMvn.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creatDto)));

        result.andDo(print());
        result.andExpect(status().isCreated());
        //Body = {"id":1,"username":"soolbe","fullName":null,"joined":1475241861485,"updated":1475241861485}
        result.andExpect(jsonPath("$.username", is("soolbe")));

        result = mockMvn.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creatDto)));

        result.andDo(print());
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.code", is("duplicated.username.exception")));
    }

    @Test
    @Rollback(false)
    public void createAccount_BadRequest() throws Exception {
        AccountDto.Create creatDto = new AccountDto.Create();
        creatDto.setUsername("  ");
        creatDto.setPassword("1234");

        ResultActions result = mockMvn.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creatDto)));

        result.andDo(print());
        result.andExpect(status().isBadRequest());

        result.andExpect(jsonPath("$.code", is("bad.request")));
    }

    // TODO: 2016. 10. 2. getAccount()
    @Test
    public void getAccount() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("soolbe");
        createDto.setPassword("password");
        service.createAccount(createDto);

        ResultActions result = mockMvn.perform(get("/accounts/"));

        //Body = {"content":[{"id":1,"username":"soolbe","fullName":null,"joined":1475400922119,"updated":1475400922119}]
        // ,"last":true
        // ,"totalElements":1
        // ,"totalPages":1
        // ,"size":20
        // ,"number":0
        // ,"sort":null
        // ,"first":true
        // ,"numberOfElements":1}
        result.andDo(print());
        result.andExpect(status().isOk());
    }

}