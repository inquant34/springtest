package springbootfinal.service;

import java.util.*;

import javax.validation.constraints.Null;

import springbootfinal.model.User;
import springbootfinal.model.User.Gender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import org.junit.Before;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import springbootfinal.dao.FakeDataDao;

public class UserServiceTest {

    // Mockito is a framework used so that one can Mock objects
    // That is to create objects that has been tested and do not
    // need to be tested again. Isolating the class tested against
    // external dependencies since mock objects returns a dummy data
    // instead
    @Mock
    private FakeDataDao fakeDataDao;

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        // instantiate all the mocks in the class and replaces initMocks
        MockitoAnnotations.openMocks(this);
        userService = new UserService(fakeDataDao);
    }

    @Test
    public void shouldGetAllUsers() throws Exception {
        UUID annaUserUid = UUID.randomUUID();

        User anna = new User(annaUserUid, "anna", "montana", Gender.FEMALE, 30, "Anna@gmail.com");

        // since the mock object has a method that will return an arraylist but we do
        // not want to depend
        // on the arraylist that it generates or take from an external database, we will
        // decide what object is returned
        // using mockito given keyword

        ArrayList<User> users = new ArrayList<>();
        users.add(anna);

        given(fakeDataDao.selectAllUsers()).willReturn(users);

        // Optional.empty() is when no parameters is actually passed into the method
        List<User> allUsers = userService.getAllUsers(Optional.empty());

        assertThat(allUsers).hasSize(1);
    }

    @Test
    public void shouldGetUser() throws Exception {
        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "Anna", "Montana", Gender.FEMALE, 30, "Anna@gmail.com");

        // Return Anna in terms of an optional data
        given(fakeDataDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));

        // That we will actually get back an Optional<User> object
        Optional<User> userOptional = userService.getUser(annaUid);

        assertThat(userOptional.isPresent()).isTrue();
        User user = userOptional.get();

        // where asserUserFields is a private method
        assertAnnaFields(user);
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "Anna", "Montana", Gender.FEMALE, 30, "Anna@gmail.com");

        // Return Anna in terms of an optional data
        // return smth from all the methods
        given(fakeDataDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));
        given(fakeDataDao.updateUser(anna)).willReturn(1);

        int updateResult = userService.updateUser(anna);

        // we can capture the anna that is passed into updateUser
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        // we will make sure that we got call the fakeDataDao method within the method
        // also ensure that the object being called is annaUid
        // captor needs to be used for complex objects
        verify(fakeDataDao).selectUserByUserUid(annaUid);
        // need to pass in captor.capture where the captor is passed in
        // this is because anna is a complex ovvject a reference, we need to get the
        // captor.capture()
        // so that we can perform more tests later such as check all the values passed
        // into it
        verify(fakeDataDao).updateUser(captor.capture());

        // update user will pass in Ana which can be captured and checked for field
        assertAnnaFields(captor.getValue());
        assertThat(updateResult).isEqualTo(1);
    }

    @Test
    public void shouldRemoveUser() throws Exception {
        // check whether we got invoked getUser and also delete
        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "Anna", "Montana", Gender.FEMALE, 30, "Anna@gmail.com");

        given(fakeDataDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));
        given(fakeDataDao.deleteUserByUserUid(annaUid)).willReturn(1);

        int deleteResult = userService.removeUser(annaUid);

        // need to make sure we ran the select UserByuserUid
        verify(fakeDataDao).selectUserByUserUid(annaUid);
        // if annaUid is not passed into deleteUserByUserUid
        verify(fakeDataDao).deleteUserByUserUid(annaUid);

        assertThat(deleteResult).isEqualTo(1);
    }

    @Test
    public void shouldInsertUser() throws Exception {
        // note that the UUID is null for insert
        User anna = new User(null, "Anna", "Montana", Gender.FEMALE, 30, "Anna@gmail.com");

        // when mixed with raw values we need to use eq which means that we want a
        // specific value instead of any
        given(fakeDataDao.insertUser(any(UUID.class), eq(anna))).willReturn(1);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        // run the method
        int insertResult = userService.insertUser(anna);

        // check whether we ran the method
        verify(fakeDataDao).insertUser(any(UUID.class), captor.capture());

        // whatever that is inserted has to be anna
        // the anna uuid will be set by this point
        assertAnnaFields(anna);

        // final method runs
        assertThat(insertResult).isEqualTo(1);
    }

    @Test
    public void shouldGetAllUsersByGender() throws Exception {
        UUID annaUserUid = UUID.randomUUID();

        User anna = new User(annaUserUid, "Anna", "Montana", Gender.FEMALE, 30, "Anna@gmail.com");

        UUID joeUserUid = UUID.randomUUID();

        User joe = new User(joeUserUid, "Joe", "Jones", Gender.MALE, 30, "joeJones@gmail.com");

        ArrayList<User> users = new ArrayList<>();
        users.add(anna);
        users.add(joe);

        given(fakeDataDao.selectAllUsers()).willReturn(users);

        // Get list of filtered users for both male and female
        // Remember I can include something into the gender or not so it has to be
        // optional
        List<User> filteredMaleUsers = userService.getAllUsers(Optional.of("MALE"));
        // note that the returned list can only have one male user
        assertThat(filteredMaleUsers).hasSize(1);
        assertJoeFields(filteredMaleUsers.get(0));

        List<User> filteredFemaleUsers = userService.getAllUsers(Optional.of("female"));
        assertThat(filteredFemaleUsers).hasSize(1);
        assertAnnaFields(filteredFemaleUsers.get(0));
    }

    private void assertAnnaFields(User user) {
        assertThat(user.getAge()).isEqualTo(30);
        assertThat(user.getFirstName()).isEqualTo("Anna");
        assertThat(user.getLastName()).isEqualTo("Montana");
        assertThat(user.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(user.getEmail()).isEqualTo("Anna@gmail.com");
        assertThat(user.getUserUid()).isNotNull();
    }

    private void assertJoeFields(User user) {
        assertThat(user.getAge()).isEqualTo(30);
        assertThat(user.getFirstName()).isEqualTo("Joe");
        assertThat(user.getLastName()).isEqualTo("Jones");
        assertThat(user.getGender()).isEqualTo(Gender.MALE);
        assertThat(user.getEmail()).isEqualTo("joeJones@gmail.com");
        assertThat(user.getUserUid()).isNotNull();
    }

    @Test
    public void shouldThrowExceptionWhenGenderIsInvalid() throws Exception{
        assertThatThrownBy(() -> userService.getAllUsers(Optional.of("asdfase")))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Invalid gender");
    }
}
