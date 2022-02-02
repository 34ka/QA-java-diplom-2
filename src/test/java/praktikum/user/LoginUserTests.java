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

public class LoginUserTests {

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
    @DisplayName("Successful authorization by user")
    @Description("User authorization with all fields. Email and password")
    public void successfulAuthorization() {

        //Создать пользователя
        userClient.create(user);

        //Авторизоваться пользователем
        ValidatableResponse response = userClient.login(new UserCredentials(user.email, user.password));

        //Получить статус код запроса
        int statusCodeResponse = response.extract().statusCode();
        //Получить значение ключа "success"
        boolean isUserLogged = response.extract().path("success");
        //Получить access токен
        accessToken = response.extract().path("accessToken");
        //Получить refresh токен
        String refreshToken = response.extract().path("refreshToken");
        //Получить значение ключа "email"
        String actualEmail = response.extract().path("user.email");
        //Получить значение ключа "name"
        String actualName = response.extract().path("user.name");

        //Проверить статус код
        assertThat(statusCodeResponse, equalTo(200));
        //Проверить значение ключа "success"
        assertTrue("Пользователь не авторизовался", isUserLogged);
        //Проверить access токен
        assertNotNull(accessToken);
        //Проверить refresh токен
        assertNotNull(refreshToken);
        //Проверить значение ключа "email"
        assertThat("Пользователь авторизовался под другим email", actualEmail, equalTo(user.email));
        //Проверить значение ключа "name"
        assertThat("Пользователь авторизовался под другим name", actualName, equalTo(user.name));
    }

    @Test
    @DisplayName("Unsuccessful authorization by user")
    @Description("User authorization with do not really email and password fields")
    public void unsuccessfulAuthorizationWithDoNotReallyEmailAndPassword() {

        //Авторизоваться с данными незарегистрированного пользователя
        ValidatableResponse response = userClient.login(UserCredentials.getWithDoNotReallyEmailAndPassword(user));

        //Получить статус код запроса
        int statusCodeResponse = response.extract().statusCode();
        //Получить значение ключа "success"
        boolean isUserUnLogged = response.extract().path("success");
        //Получить значение ключа "message"
        String message = response.extract().path("message");

        //Проверить статус код
        assertThat(statusCodeResponse, equalTo(401));
        //Проверить значение ключа "success"
        assertFalse(isUserUnLogged);
        //Проверить значение ключа "message"
        assertThat(message, equalTo("email or password are incorrect"));
    }
}
