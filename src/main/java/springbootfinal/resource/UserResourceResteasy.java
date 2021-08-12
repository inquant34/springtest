package springbootfinal.resource;

import java.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import springbootfinal.service.UserService;
import springbootfinal.model.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
// Now the information will only be returned if one specifies /api/v1/users
// behind localhost:8080
@Path("/api/v1/users")
public class UserResourceResteasy {

    private UserService userService;

    @Autowired
    public UserResourceResteasy(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<User> fetchUsers(@QueryParam("gender") Optional<String> gender) throws IllegalArgumentException {
        return userService.getAllUsers(gender);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Path("{userUid}")
    public Response fetchUser(@PathVariable("userUid") UUID userUid) {
        Optional<User> userOptional = userService.getUser(userUid);
        if (userOptional.isPresent()) {
            return Response.ok(userOptional.get()).build();
        }
        return Response.status(Status.NOT_FOUND).entity(new ErrorMessage("user " + userUid + " was not found."))
                .build();
    }

    class ErrorMessage {
        String errorMessage;

        public ErrorMessage(String message) {
            errorMessage = message;
        }

        public String getMessage() {
            return errorMessage;
        }

        public void setMessage(String message) {
            this.errorMessage = message;
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    public Response insertNewUser(@RequestBody User user) {
        int result = userService.insertUser(user);
        return getIntegerResponseEntity(result);
    }

    // Put method allows us to modify what we already have in our database
    @PUT
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    public Response updateUser(@RequestBody User user) {
        // updates the user if user is present
        int result = userService.updateUser(user);
        return getIntegerResponseEntity(result);
    }

    // All the methods may produce an application_json_value
    @DELETE
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Path("{userUid}")
    public Response deleteUser(@PathVariable("userUid") UUID userUid) {
        // remove user only if the user is present
        int result = userService.removeUser(userUid);
        return getIntegerResponseEntity(result);
    }

    private Response getIntegerResponseEntity(int result) {
        if (result == 1) {
            // .build() to convert it into an integer response code
            return Response.ok().build();
        }

        return Response.status(Status.BAD_REQUEST).build();
    }
}
