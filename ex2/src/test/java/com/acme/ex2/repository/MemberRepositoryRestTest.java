package com.acme.ex2.repository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.acme.ex2.ApplicationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import com.acme.ex2.model.component.Account;
import com.acme.ex2.model.entity.Member;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest(classes = ApplicationConfig.class)
@TestPropertySource(locations =  "classpath:application-for-tests.properties")
class MemberRepositoryRestTest {

	private MockMvc mockMvc;
	
    @Autowired
    private WebApplicationContext ctx;
	
    @BeforeEach
    void setup() {
    	this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }
    
    @Test
    public void testBooks200() throws Exception{
        mockMvc.perform(get("/members").accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().isOk())
        	.andDo(print());
    }

    @Test
    public void testBook200() throws Exception{
        mockMvc.perform(get("/members/1").accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().isOk())
        	.andDo(print());
    }

    @Test
    public void testBook404() throws Exception{
        mockMvc.perform(get("/members/999").accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().isNotFound());
    }
    
    private ObjectMapper jackson = new ObjectMapper();
    
    @Test
    public void testMembers201() throws Exception{
    	Member member = new Member();
    	member.setFirstname("Jane");
    	member.setLastname("Doe");
    	member.setAccount(new Account("jane.doe", "azerty"));
        mockMvc.perform(post("/members")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(jackson.writeValueAsString(member)))
        	.andExpect(status().isCreated())
        	.andDo(print());
    }
    
    @Test
    public void testMembers409() throws Exception{
    	Member member = new Member();
    	member.setFirstname("Jane");
    	member.setLastname("Doe");
    	member.setAccount(new Account("jdoe", "azerty"));
        mockMvc.perform(post("/members")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(jackson.writeValueAsString(member)))
        	.andExpect(status().isConflict())
        	.andDo(print());
    }
}

