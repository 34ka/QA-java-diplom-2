package praktikum.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.UserClient;
import praktikum.data.DataForCreateNewUser;
import praktikum.data.UserCredentials;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class CreateUserTests {

    private UserClient userClient;
    private DataForCreateNewUser user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        //Сгенерировать случайные данные полей
        user = DataForCreateNewUser.getRandom();
    }

    @After
    public void tearDown() {
        //Удалить пользователя
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Сheck the successful creation of a user")
    @Description("Creating a user with all fields. Email, password and firstname")
    public void successfulCreateUser() {

        //Создать пользователя
        ValidatableResponse response = userClient.create(user);

        //Получить статус кода запроса
        int statusCodePositiveResponseCreate = response.extract().statusCode();
        //Получить значение ключа "success"
        boolean isUserCreated = response.extract().path("success");

        //Авторизоваться пользователем
        ValidatableResponse responseUserLogged = userClient.login(new UserCredentials(user.email, user.password));

        //Получить refresh токен
        String refreshToken = responseUserLogged.extract().path("refreshToken");
        //Получить access токен
        accessToken = responseUserLogged.extract().path("accessToken");

        //Проверить статус код
        assertThat(statusCodePositiveResponseCreate, equalTo(200));
        //Проверить создание пользователя
        assertTrue("User is not created", isUserCreated);
        //Проверить access токен
        assertNotNull("Пустой accessToken", accessToken);
        //Проверить refresh токен
        assertNotNull("Пустой refreshToken", refreshToken);
    }

    @Test
    @DisplayName("Сheck the unsuccessful creation of a user")
    @Description("Creating a user with a email, password and firstname twice")
    public void unsuccessfulCreateTwoIdenticalUsers() {

        //Создать пользователя
        userClient.create(user);

        //Повторно cоздать пользователя
        ValidatableResponse response = userClient.create(user);

        //Получить статус кода запроса
        int statusCodeNegativeResponse = response.extract().statusCode();
        //Получить значение ключа "success"
        boolean isSuccess = response.extract().path("success");
        //Получить значение ключа "message"
        String message = response.extract().path("message");

        //Проверить статус код
        assertThat(statusCodeNegativeResponse, equalTo(403));
        //Проверить значение ключа "success"
        assertFalse(isSuccess);
        //Проверить значение ключа "message"
        assertThat("Создан ещё один пользователь с одинаковыми данными", message, (equalTo("User already exists")));
    }
}
