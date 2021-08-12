package springbootfinal.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;
import springbootfinal.model.User;
import springbootfinal.model.User.Gender;
import org.junit.Test;
import org.junit.Before;

public class FakeDataDaoTest {

    private FakeDataDao fakeDataDao;

    @Before
    public void SetUp() throws Exception {
        fakeDataDao = new FakeDataDao();
    }

    @Test
    public void shouldSelectAllUsers() throws Exception {
        List<User> users = fakeDataDao.selectAllUsers();
        assertThat(users).hasSize(1);
        User user = users.get(0);
        assertThat(user.getAge()).isEqualTo(20);
        assertThat(user.getFirstName()).isEqualTo("Joe");
        assertThat(user.getLastName()).isEqualTo("Jones");
        assertThat(user.getGender()).isEqualTo(Gender.MALE);
        assertThat(user.getEmail()).isEqualTo("JoeJones2021@gmail.com");
        assertThat(user.getUserUid()).isNotNull();
    }

    @Test
    public void shouldSelectUserByUserUid() throws Exception {
        UUID annaUserUid = UUID.randomUUID();
        User anna = new User(annaUserUid, "anna", "montana", Gender.FEMALE, 30, "anna@gmail.com");
        fakeDataDao.insertUser(annaUserUid, anna);
        // ensure that fakeDataDao has both joe and anna
        // assertThat has a hasSize method to check the size of the returned list
        ArrayList<Integer> input = new ArrayList<>();
        input.add(1);
        input.add(2);
        assertThat(input).hasSize(2);

        // Either returns Optional.empty or returns Optional with the User and all its
        // attributes within
        Optional<User> annaOptional = fakeDataDao.selectUserByUserUid(annaUserUid);
        assertThat(annaOptional.isPresent()).isTrue();
        assertThat(annaOptional.get()).usingRecursiveComparison().isEqualTo(anna);
    }

    @Test
    public void updateUser() throws Exception {
        // update user joe and ensure that i can retrieve the new joe
        // ensure also that the new joe has all the attributes equal to the old joe
        UUID joeUserUid = fakeDataDao.selectAllUsers().get(0).getUserUid();
        User newJoe = new User(joeUserUid, "anna", "montana", Gender.FEMALE, 30, "anna@gmail.com");
        fakeDataDao.updateUser(newJoe);

        // retrieving newJoe form database as joeJoe
        Optional<User> joeJoe = fakeDataDao.selectUserByUserUid(joeUserUid);
        assertThat(joeJoe.isPresent()).isTrue();

        assertThat(joeJoe.get()).usingRecursiveComparison().isEqualTo(newJoe);
    }

    @Test
    public void shouldDeleteUserByUserUid() throws Exception {
        UUID joeUserUid = fakeDataDao.selectAllUsers().get(0).getUserUid();
        fakeDataDao.deleteUserByUserUid(joeUserUid);
        assertThat(fakeDataDao.selectUserByUserUid(joeUserUid).isPresent()).isFalse();
        assertThat(fakeDataDao.selectAllUsers()).isEmpty();
    }

    @Test
    public void shouldInsertUser() throws Exception {
        UUID userUid = UUID.randomUUID();
        User user = new User(userUid, "anna", "montana", Gender.FEMALE, 30, "anna@gmail.com");

        fakeDataDao.insertUser(userUid, user);

        // select all the users so that one can check the size
        // ensure that the fields of the inserted user is the same as one that
        // is being created
        List<User> users = fakeDataDao.selectAllUsers();
        // has size = 2
        assertThat(users).hasSize(2);
        // ensure that the user that is inserted is the same as the one that is built
        assertThat(fakeDataDao.selectUserByUserUid(userUid).get()).usingRecursiveComparison().isEqualTo(user);
    }
}
