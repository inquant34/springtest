package springbootfinal.dao;

import java.util.*;
import springbootfinal.model.*;

//also can rename the methods for userDao to match that of sql
public interface UserDao {

    List<User> selectAllUsers();

    Optional<User> selectUserByUserUid(UUID userUid);

    int updateUser(User user);

    int deleteUserByUserUid(UUID userUid);

    int insertUser(UUID userUid, User user);

}
