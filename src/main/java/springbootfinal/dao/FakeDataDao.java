package springbootfinal.dao;

import java.util.*;

import org.springframework.stereotype.Repository;

import springbootfinal.model.User;
import springbootfinal.model.User.Gender;

@Repository
public class FakeDataDao implements UserDao {

    private Map<UUID, User> database;

    // static initializer block that is used to initialize static members of the
    // class
    public FakeDataDao() {
        database = new HashMap<>();
        // where UUID.randomUUID() generates a random UUID
        UUID joeUserUid = UUID.randomUUID();
        // randomUUID is joes user uuid cannot have two uuid that dont match
        database.put(joeUserUid, new User(joeUserUid, "Joe", "Jones", Gender.MALE, 20, "JoeJones2021@gmail.com"));
    }

    @Override
    public List<User> selectAllUsers() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Optional<User> selectUserByUserUid(UUID userUid) {
        // Putting in null to Optional.ofNullable will return
        // Optional.empty
        return Optional.ofNullable(database.get(userUid));
    }

    @Override
    public int updateUser(User user) {
        // user.getUserUid
        database.put(user.getUserUid(), user);
        return 1;
    }

    @Override
    public int deleteUserByUserUid(UUID userUid) {
        database.remove(userUid);
        return 1;
    }

    @Override
    public int insertUser(UUID userUid, User user) {
        database.put(userUid, user);
        return 1;
    }

    public Set<UUID> getKeySet() {
        return database.keySet();
    }

}
