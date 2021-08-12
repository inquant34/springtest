package springbootfinal.resource;

import java.util.*;

import javax.ws.rs.QueryParam;

import springbootfinal.service.UserService;
import springbootfinal.model.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;

// @RestController
// // Now the information will only be returned if one specifies /api/v1/users
// // behind localhost:8080
// @RequestMapping(path = "/api/v1/users")
public class UserResource {

    private UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    // We will instantiate the query parameter
    public List<User> fetchUsers(@QueryParam("gender") Optional<String> gender) throws IllegalArgumentException {
        return userService.getAllUsers(gender);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,
            // Note that the path is userUid where userUid is an input into the url
            path = "{userUid}")
    public ResponseEntity<?> fetchUser(@PathVariable("userUid") UUID userUid) {
        Optional<User> userOptional = userService.getUser(userUid);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage("user " + userUid + " was not found."));
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

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    // the RequestBody will be automatically mapped to the user and all the fields
    // that are present will be inputted correspondingly
    // the fields that are not being specified such as userUid will be inputted as
    // null
    public ResponseEntity<Integer> insertNewUser(@RequestBody User user) {
        int result = userService.insertUser(user);
        return getIntegerResponseEntity(result);
    }

    // Put method allows us to modify what we already have in our database
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> updateUser(@RequestBody User user) {
        // updates the user if user is present
        int result = userService.updateUser(user);
        return getIntegerResponseEntity(result);
    }

    // All the methods may produce an application_json_value
    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, path = "{userUid}")
    public ResponseEntity<?> deleteUser(@PathVariable("userUid") UUID userUid) {
        // remove user only if the user is present
        int result = userService.removeUser(userUid);
        return getIntegerResponseEntity(result);
    }

    private ResponseEntity<Integer> getIntegerResponseEntity(int result) {
        if (result == 1) {
            // .build() to convert it into an integer response code
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }
}
