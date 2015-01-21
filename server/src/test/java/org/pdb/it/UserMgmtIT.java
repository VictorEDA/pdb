package org.pdb.it;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pdb.common.BaseTest;
import org.pdb.common.rest.ObjectMapperProvider;
import org.pdb.db.entities.Group;
import org.pdb.db.entities.User;

/**
 * Integration test for group and user resources.
 */
public class UserMgmtIT extends BaseTest {

    /**
     * The API endpoint.
     */
    private static final String ENDPOINT = "http://localhost:8080/api/";

    /**
     * The REST client.
     */
    private static Client client;

    /**
     * Set up test environment for class.
     */
    @BeforeClass
    public static void setUpClass() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(ObjectMapperProvider.class);
        client = ClientBuilder.newClient(clientConfig);
    }

    /**
     * Basic test for create and get API methods.
     */
    @Test
    public void test_create_get() {
        // Create group
        Group group = createGroup("group1");

        // Get group
        WebTarget webTarget = client.target(ENDPOINT + "group/" + group.getGroupId());
        Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
        assertEquals("Response should be OK.", Status.OK.getStatusCode(), response.getStatus());
        Group copy = response.readEntity(Group.class);
        assertTrue("Group should match the one created.", EqualsBuilder.reflectionEquals(group, copy));
    }

    /**
     * Create group.
     * @param groupName The group name.
     * @return the group
     */
    private Group createGroup(final String groupId) {
        WebTarget webTarget = client.target(ENDPOINT + "group");
        Map<String, String> json = new HashMap<>();
        json.put("group_id", groupId);
        Response response =
                webTarget.request(MediaType.APPLICATION_JSON).post(
                        Entity.entity(json, MediaType.APPLICATION_JSON));

        assertEquals("Group should be created.", Status.CREATED.getStatusCode(), response.getStatus());
        Group group = response.readEntity(Group.class);
        checkGroupCreate(groupId, group);
        return group;
    }

    /**
     * Check to make sure group was created correctly.
     * @param groupId The group id.
     * @param group The group to check.
     */
    private void checkGroupCreate(final String groupId, Group group) {
        assertEquals("Group id should be correct.", groupId, group.getGroupId());
        assertThat("Access token must be valid.", group.getAccessToken(), not(isEmptyOrNullString()));
        assertEquals("Created and updated dates must be the same.", group.getCreatedAt(),
                group.getUpdatedAt());
        Date date = new Date();
        assertThat("Creation date must be correct.", group.getCreatedAt().getTime(),
                lessThanOrEqualTo(date.getTime()));
        assertEquals("No users should exist.", 0, group.getUserIds().size());
    }

    /**
     * Basic test for create and get API methods with one user.
     */
    @Test
    public void test_create_get_user() {
        // Create group
        final String groupName = "Org";
        Group group = createGroup(groupName);
        String userId = "myUser";
        User user = createUser(userId, group);

        // Get user
        WebTarget webTarget = client.target(ENDPOINT + "group/" + group.getGroupId() + "/user/" + user.getUserId());
        Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
        assertEquals("Response should be OK.", Status.OK.getStatusCode(), response.getStatus());
        User copy = response.readEntity(User.class);
        assertTrue("User should match the one created.", EqualsBuilder.reflectionEquals(user, copy));

    }

    /**
     * Basic test for create and get API methods with users.
     */
    @Test
    public void test_create_get_users() {
        // Create group
        final String groupName = "Org With Users";
        Group group = createGroup(groupName);

        // Create a couple users.
        Set<String> userIds = new HashSet<>(Arrays.asList("abc", "def"));
        for (String userId : userIds) {
            createUser(userId, group);
        }

        // Get group
        WebTarget webTarget = client.target(ENDPOINT + "group/" + group.getGroupId());
        Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
        assertEquals("Response should be OK.", Status.OK.getStatusCode(), response.getStatus());
        Group copy = response.readEntity(Group.class);
        assertEquals("Users should be correct.", userIds, copy.getUserIds());
    }

    /**
     * Create a user.
     * @param userId The user id.
     * @param group The group for the user.
     * @return the created user
     */
    private User createUser(String userId, Group group) {
        WebTarget webTarget = client.target(ENDPOINT + "group/" + group.getGroupId() + "/user");
        Map<String, String> json = new HashMap<>();
        json.put("user_id", userId);
        Response response =
                webTarget.request(MediaType.APPLICATION_JSON).post(
                        Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals("User should be created.", Status.CREATED.getStatusCode(), response.getStatus());
        User user = response.readEntity(User.class);
        checkUserCreate(userId, user);
        return user;
    }

    /**
     * Check that user was created correctly.
     * @param userId The user id.
     * @param user The user to check.
     */
    private void checkUserCreate(String userId, User user) {
        assertEquals("Customer user id should be correct.", userId, user.getUserId());
        assertThat("Access token must be valid.", user.getAccessToken(), not(isEmptyOrNullString()));
        assertThat("Group id must be valid.", user.getGroupId(), not(isEmptyOrNullString()));
        assertEquals("Created and updated dates must be the same.", user.getCreatedAt(), user.getUpdatedAt());
        Date date = new Date();
        assertThat("Creation date must be correct.", user.getCreatedAt().getTime(),
                lessThanOrEqualTo(date.getTime()));
    }

    /**
     * Failure test for bad create request.
     */
    @Test
    public void test_create_bad_request() {
        WebTarget webTarget = client.target(ENDPOINT + "group");
        Map<String, String> json = new HashMap<>();
        // Set empty group id.
        json.put("group_id", "");
        Response response =
                webTarget.request(MediaType.APPLICATION_JSON).post(
                        Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    /**
     * Failure test when group already exists.
     */
    @Test
    public void test_create_exists() {
        final String groupName = "Double Org";
        createGroup(groupName);

        // Send the request in again.
        WebTarget webTarget = client.target(ENDPOINT + "group");
        Map<String, String> json = new HashMap<>();
        json.put("group_id", groupName);
        Response response =
                webTarget.request(MediaType.APPLICATION_JSON).post(
                        Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(Status.CONFLICT.getStatusCode(), response.getStatus());
    }

    /**
     * Failure test when group cannot be found or bad request is given.
     */
    @Test
    public void test_get_not_found() {
        WebTarget webTarget = client.target(ENDPOINT + "group/bad");
        Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
        assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());

        webTarget = client.target(ENDPOINT + "group/-1");
        response = webTarget.request(MediaType.APPLICATION_JSON).get();
        assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());

        webTarget = client.target(ENDPOINT + "group/1234566");
        response = webTarget.request(MediaType.APPLICATION_JSON).get();
        assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    /**
     * Failure test for bad create user request.
     */
    @Test
    public void test_create_user_bad_request() {
        Group group = createGroup("test_create_user_bad_request");
        WebTarget webTarget = client.target(ENDPOINT + "group/" + group.getGroupId() + "/user");
        Map<String, String> json = new HashMap<>();
        // Null user id
        json.put("user_id", null);
        Response response =
                webTarget.request(MediaType.APPLICATION_JSON).post(
                        Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    /**
     * Failure test for create user when group does not exist.
     */
    @Test
    public void test_create_user_bad_group() {
        WebTarget webTarget = client.target(ENDPOINT + "group/123456/user");
        Map<String, String> json = new HashMap<>();
        json.put("user_id", "test_create_user_bad_group");
        Response response =
                webTarget.request(MediaType.APPLICATION_JSON).post(
                        Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    /**
     * Failure test for create user when user already exists.
     */
    @Test
    public void test_create_user_exists() {
        // Create group
        final String groupName = "test_create_user_exists";
        Group group = createGroup(groupName);

        // Create a users.
        String userId = "test_create_user_exists";
        createUser(userId, group);

        // Try to create another user with the same id.
        WebTarget webTarget = client.target(ENDPOINT + "group/" + group.getGroupId() + "/user");
        Map<String, String> json = new HashMap<>();
        json.put("user_id", userId);
        Response response =
                webTarget.request(MediaType.APPLICATION_JSON).post(
                        Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(Status.CONFLICT.getStatusCode(), response.getStatus());
    }

}
