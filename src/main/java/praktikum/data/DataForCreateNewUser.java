package praktikum.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.javafaker.Faker;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataForCreateNewUser {

    public String email;
    public String password;
    public String name;

    public DataForCreateNewUser() {

    }

    public DataForCreateNewUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static DataForCreateNewUser getRandom() {
        Faker faker = new Faker();

        final String emailUser = faker.internet().emailAddress();
        final String passwordUser = faker.internet().password(6, 10);
        final String nameUser = faker.name().firstName();
        return new DataForCreateNewUser(emailUser, passwordUser, nameUser);
    }

    public static DataForCreateNewUser getWithPasswordAndName() {
        Faker faker = new Faker();

        return new DataForCreateNewUser().setPassword(faker.internet().password(6, 10))
                .setName(faker.name().firstName());
    }

    public static DataForCreateNewUser getWithEmailAndName() {
        Faker faker = new Faker();
        return new DataForCreateNewUser().setEmail(faker.internet().emailAddress())
                .setName(faker.name().firstName());
    }

    public static DataForCreateNewUser getWithEmailAndPassword() {
        Faker faker = new Faker();
        return new DataForCreateNewUser().setEmail(faker.internet().emailAddress())
                .setPassword(faker.internet().password(6, 10));
    }

    public static DataForCreateNewUser getEmail() {
        Faker faker = new Faker();
        return new DataForCreateNewUser().setEmail(faker.internet().emailAddress());
    }

    public static DataForCreateNewUser getPassword() {
        Faker faker = new Faker();
        return new DataForCreateNewUser().setPassword(faker.internet().password(6,10));
    }

    public static DataForCreateNewUser getName() {
        Faker faker = new Faker();
        return new DataForCreateNewUser().setName(faker.name().firstName());
    }

    public DataForCreateNewUser setEmail (String email) {
        this.email = email;
        return this;
    }

    public DataForCreateNewUser setPassword (String password) {
        this.password = password;
        return this;
    }

    public DataForCreateNewUser setName (String name) {
        this.name = name;
        return this;
    }
}
