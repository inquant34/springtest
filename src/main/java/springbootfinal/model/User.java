package springbootfinal.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class User {

    public enum Gender {
        MALE, FEMALE
    }

    private UUID userUid;
    private final String firstName;
    private final String lastName;
    private final Gender gender;
    private final Integer age;
    private final String email;

    public User(UUID userUid, String firstName, String lastName, Gender gender, Integer age, String email) {
        this.userUid = userUid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.email = email;
    }

    @JsonProperty("id")
    public UUID getUserUid() {
        return this.userUid;
    }

    public void setUserUid(UUID input) {
        this.userUid = input;
    }

    // @JsonIgnore is used to ensure that the json inputs are ignored when one asks
    // for the json object in postman

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Integer getAge() {
        return this.age;
    }

    public String getEmail() {
        return this.email;
    }

    public LocalDate getDateOfBirth() {
        // getting the current localdate
        return LocalDate.now().minusYears(age);
    }

    @Override
    public String toString() {
        return "User{" + "userUid= " + userUid + ", firstName " + firstName + '\'' + ", lastName " + lastName + '\''
                + ", gender= " + gender + ", age= " + age + ", email= " + email + '\'' + "}";
    }

}
