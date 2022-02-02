package praktikum.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.client.UserClient;
import praktikum.data.DataForCreateNewUser;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class CreateUserRequestValidationTests {

    private UserClient userClient;
    DataForCreateNewUser user;
    private int expectedStatus;
    private boolean expectedSuccess;
    private String expectedErrorTextMessage;

    public CreateUserRequestValidationTests(DataForCreateNewUser user, int expectedStatus, boolean expectedSuccess, String expectedErrorTextMessage) {
        this.user = user;
        this.expectedStatus = expectedStatus;
        this.expectedSuccess = expectedSuccess;
        this.expectedErrorTextMessage = expectedErrorTextMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                {DataForCreateNewUser.getWithEmailAndPassword(), 403, false,"Email, password and name are required fields"},
                {DataForCreateNewUser.getWithPasswordAndName(), 403, false,"Email, password and name are required fields"},
                {DataForCreateNewUser.getWithEmailAndName() , 403, false,"Email, password and name are required fields"}
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Сheck the creation of a user without one field")
    @Description("Creating a user only with the: " +
            "1. Email and password " +
            "2. Password and name " +
            "3. Email and mame fields")
    public void createUsersWithoutField() {

        //Создать пользователя
        ValidatableResponse response = userClient.create(user);

        //Получить статус кода
        int statusCode = response.extract().statusCode();
        //Получить значение ключа "success"
        boolean actualSuccess = response.extract().path("success");
        //Получить значение ключа "message"
        String errorTextMessage = response.extract().path("message");

        //Проверить статус код
        assertThat(statusCode, equalTo(expectedStatus));
        //Проверить значение ключа "success"
        assertThat(actualSuccess, equalTo(expectedSuccess));
        //Проверить значение ключа "message"
        assertThat(errorTextMessage, equalTo(expectedErrorTextMessage));
    }
}
