package com.james;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.james.entities.User;
import com.james.services.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RegistrationFormSpringApplication.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegistrationFormSpringApplicationTests {

	@Autowired
	WebApplicationContext wac;

	MockMvc mockMvc;

	@Autowired
	UserRepository users;

	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void atestAddUser() throws Exception {
		User user = new User();
		user.setUsername("James");
		user.setAddress("101 Address");
		user.setEmail("email@email.com");

		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(user);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/user")
						.content(json)
						.contentType("application/json")
		);

		Assert.assertTrue(users.count() == 1);
	}

	@Test
	public void bTestEdit() throws Exception {
		User user = users.findOne(1);
		user.setUsername("Bob");

		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(user);

		mockMvc.perform(
				MockMvcRequestBuilders.put("/user")
						.content(json)
						.contentType("application/json")
		);

		User editUser = users.findOne(1);
		Assert.assertTrue(editUser.getUsername().equals(user.getUsername()));
	}

	@Test
	public void cTestGet() throws Exception {
		ResultActions ra = mockMvc.perform(
				MockMvcRequestBuilders.get("/user")
		);

		MvcResult result = ra.andReturn();
		MockHttpServletResponse response = result.getResponse();
		String json = response.getContentAsString();

		ObjectMapper om = new ObjectMapper();
		ArrayList<HashMap<String, String>> userMaps = om.readValue(json, ArrayList.class);

		Assert.assertTrue(userMaps.size() == 1 && userMaps.get(0).get("username").equals("Bob"));
	}

	@Test
	public void dTestDeleteUser() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/user/1")
		);

		Assert.assertTrue(users.count() == 0);
	}

}
