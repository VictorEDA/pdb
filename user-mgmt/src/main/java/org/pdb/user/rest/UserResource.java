package org.pdb.user.rest;

import java.util.Date;
import java.util.HashSet;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.NotEmpty;
import org.pdb.common.AppConfigurationException;
import org.pdb.common.Helper;
import org.pdb.common.rest.ErrorJson;
import org.pdb.db.entities.Group;
import org.pdb.db.entities.User;
import org.pdb.db.repo.GroupRepo;
import org.pdb.db.repo.UserRepo;
import org.springframework.data.cassandra.repository.support.BasicMapId;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * REST interface for users and groups.
 */
@Path("/api{version:(\\/v1)?}")
public class UserResource {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LogManager.getLogger(UserResource.class);

    /**
     * The Cassandra repo for Group.
     */
    @Inject
    private GroupRepo groupRepo;

    /**
     * The Cassandra repo for User.
     */
    @Inject
    private UserRepo userRepo;

    /**
     * Default constructor.
     */
    public UserResource() {
        // empty
    }

    /**
     * Check initialization.
     * @throws AppConfigurationException if initialization issue encountered
     */
    @PostConstruct
    public void checkInit() {
        Helper.checkNullConfig(groupRepo, "groupRepo");
        Helper.checkNullConfig(userRepo, "userRepo");
    }

    public static class CreateGroupJson {
        @JsonProperty("group_id")
        @NotNull
        @NotEmpty
        @Size(max = Group.GROUP_ID_MAX_SIZE)
        public String groupId;
    }

    /**
     * Create group.
     * @param json The JSON object specifying entity properties.
     * @return 201 and created organization<br>
     *         400 if bad request<br>
     *         401 if unauthorized<br>
     *         409 if entity already exists<br>
     *         500 if server error
     */
    @POST
    @Path("/group")
    @Consumes("application/json")
    @Produces("application/json")
    public Response createGroup(@Valid CreateGroupJson json) {
        final String methodName = "createGroup";

        Helper.logEntrance(LOGGER, methodName, "json", json);
        Response response = null;

        try {
            Group exists = groupRepo.findOne(BasicMapId.id().with("groupId", json.groupId));
            if (exists != null) {
                response =
                        Response.status(Status.CONFLICT)
                                .entity(new ErrorJson("error", "Entity already exists.")).build();
            } else {
                Group group = new Group();
                group.setGroupId(json.groupId);
                group.setAccessToken(RandomStringUtils.randomAlphanumeric(32));
                groupRepo.save(group);
                response = Response.status(Status.CREATED).entity(group).build();
            }
        } catch (Exception e) {
            Helper.logException(LOGGER, methodName, e);
            response = Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorJson("error", e.getMessage())).build();
        }

        return Helper.logExit(LOGGER, methodName, response);
    }

    /**
     * Get group.
     * @param groupId The group id.
     * @return 200 and organization<br>
     *         401 if unauthorized<br>
     *         404 if not found<br>
     *         500 if server error
     */
    @GET
    @Path("/group/{group_id}")
    @Produces("application/json")
    public Response getGroup(@PathParam("group_id") String groupId) {
        final String methodName = "getGroup";

        Helper.logEntrance(LOGGER, methodName, "groupId", groupId);

        Response response = null;
        try {
            Group group = groupRepo.findOne(BasicMapId.id().with("groupId", groupId));
            if (group == null) {
                response = Response.status(Status.NOT_FOUND).entity(new ErrorJson("error", "Not found.")).build();
            } else {
                response = Response.ok().entity(group).build();
            }
        } catch (Exception e) {
            Helper.logException(LOGGER, methodName, e);
            response = Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorJson("error", e.getMessage())).build();
        }
        return Helper.logExit(LOGGER, methodName, response);
    }

    public static class CreateUserJson {
        @JsonProperty("user_id")
        @NotNull
        @NotEmpty
        @Size(max = User.USER_ID_MAX_SIZE)
        public String userId;
    }

    /**
     * Create user.
     * @param json The JSON object specifying user properties.
     * @return 201 and created user<br>
     *         400 if bad request<br>
     *         401 if unauthorized<br>
     *         404 if group does not exist<br>
     *         409 if user already exists<br>
     *         500 if server error
     */
    @Path("/group/{group_id}/user")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response createUser(@PathParam("group_id") String groupId, @Valid CreateUserJson json) {
        final String methodName = "createUser";

        Helper.logEntrance(LOGGER, methodName, "groupId", groupId, "json", json);

        Response response = null;
        try {
            Group group = groupRepo.findOne(BasicMapId.id().with("groupId", groupId));
            if (group == null) {
                response =
                        Response.status(Status.NOT_FOUND).entity(new ErrorJson("error", "Not found."))
                                .build();
            } else {
                User exists =
                        userRepo.findOne(BasicMapId.id().with("userId", json.userId).with("groupId", groupId));
                if (exists != null) {
                    response =
                            Response.status(Status.CONFLICT)
                                    .entity(new ErrorJson("error", "Entity already exists.")).build();
                } else {
                    // Save user.
                    User user = new User();
                    user.setUserId(json.userId);
                    user.setGroupId(groupId);
                    user.setAccessToken(RandomStringUtils.randomAlphanumeric(32));
                    userRepo.save(user);
                    // Create a new copy of user ids set so it can be modified.
                    group.setUserIds(new HashSet<>(group.getUserIds()));
                    group.getUserIds().add(json.userId);
                    group.setUpdatedAt(new Date());
                    groupRepo.save(group);
                    response = Response.status(Status.CREATED).entity(user).build();
                }
            }
        } catch (Exception e) {
            Helper.logException(LOGGER, methodName, e);
            response =
                    Response.status(Status.INTERNAL_SERVER_ERROR)
                            .entity(new ErrorJson("error", e.getMessage())).build();
        }

        return Helper.logExit(LOGGER, methodName, response);
    }

    /**
     * Get user.
     * @param groupId The group id.
     * @param userId The user id.
     * @return 200 and user<br>
     *         401 if unauthorized<br>
     *         404 if group or user not found<br>
     *         500 if server error
     */
    @GET
    @Path("/group/{group_id}/user/{user_id}")
    @Produces("application/json")
    public Response getUser(@PathParam("group_id") String groupId, @PathParam("user_id") String userId) {
        final String methodName = "getUser";

        Helper.logEntrance(LOGGER, methodName, "groupId", groupId, "userId", userId);

        Response response = null;
        try {
            User user = userRepo.findOne(BasicMapId.id().with("userId", userId).with("groupId", groupId));
            if (user == null) {
                response = Response.status(Status.NOT_FOUND).entity(new ErrorJson("error", "Not found.")).build();
            } else {
                response = Response.ok().entity(user).build();
            }
        } catch (Exception e) {
            Helper.logException(LOGGER, methodName, e);
            response = Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorJson("error", e.getMessage())).build();
        }
        return Helper.logExit(LOGGER, methodName, response);
    }

    // TODO: Delete group, delete user.

    @Path("/test")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String sayPlainTextHello() {
        return "{\"status\":\"good\"}";
    }

}
