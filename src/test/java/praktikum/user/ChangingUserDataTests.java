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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class ChangingUserDataTests {

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
    @DisplayName("Change user all data with login")
    @Description("Successfully change the user with all fields with login. Email address, password and name")
    public void successfulChangeDataUserEmailAndNameAndPasswordWithLogin() {

        //Создать пользователя
        ValidatableResponse responseCreatedUser = userClient.create(user);

        //Получить access токен
        accessToken = responseCreatedUser.extract().path("accessToken");

        //Сгенерировать случайные данные полей
        DataForCreateNewUser newUserData = DataForCreateNewUser.getRandom();
        //Изменить данные пользователя
        ValidatableResponse responseChangeData = userClient.changeData(newUserData, accessToken);
        //Получить значение ключа "email"
        String actualEmail = responseChangeData.extract().path("user.email");
        //Получить значение ключа "name"
        String actualName = responseChangeData.extract().path("user.name");

        //Авторизоваться пользователем c новыми данными
        ValidatableResponse responseLoginWithNewData = userClient.login(new UserCredentials(newUserData.email, newUserData.password));

        //Получить статус код запроса на изменение данных
        int statusCodeResponseChangeData = responseChangeData.extract().statusCode();
        //Получить статус код запроса на авторизацию с новыми данными
        int statusCodeResponseLoginWithNewData = responseLoginWithNewData.extract().statusCode();

        //Проверить статус код запроса на изменение данных
        assertThat(statusCodeResponseChangeData, equalTo(200));
        //Проверить значение ключа "email"
        assertThat("У пользователя не изменились данные email", actualEmail, equalTo(newUserData.email));
        //Проверить значение ключа "name"
        assertThat("У пользователя не изменились данные name", actualName, equalTo(newUserData.name));
        //Проверить статус код запроса на авторизацию с измененными данными
        assertThat(statusCodeResponseLoginWithNewData, equalTo(200));
    }

    @Test
    @DisplayName("Change user email")
    @Description("Successfully change the user with email field with login")
    public void successfulChangeDataUserEmailWithLogin() {

        //Создать пользователя
        ValidatableResponse responseCreatedUser = userClient.create(user);

        //Получить access токен
        accessToken = responseCreatedUser.extract().path("accessToken");

        //Сгенерировать данные поля "email"
        DataForCreateNewUser newUserData = DataForCreateNewUser.getEmail();
        //Изменить данные пользователя
        ValidatableResponse responseChangeData = userClient.changeData(newUserData, accessToken);
        //Получить значение ключа "email"
        String actualEmail = responseChangeData.extract().path("user.email");
        //Получить значение ключа "name"
        String actualName = responseChangeData.extract().path("user.name");

        //Авторизоваться пользователем c новыми данными
        ValidatableResponse responseLoginWithNewData = userClient.login(new UserCredentials(newUserData.email, user.password));

        //Получить статус код запроса на изменение данных
        int statusCodeResponseChangeData = responseChangeData.extract().statusCode();
        //Получить статус код запроса на авторизацию с новыми данными
        int statusCodeResponseLoginWithNewData = responseLoginWithNewData.extract().statusCode();

        //Проверить статус код запроса на изменение данных
        assertThat(statusCodeResponseChangeData, equalTo(200));
        //Проверить значение ключа "email"
        assertThat("У пользователя не изменились данные email", actualEmail, equalTo(newUserData.email));
        //Проверить значение ключа "name"
        assertThat("У пользователя не изменились данные name", actualName, equalTo(user.name));
        //Проверить статус код запроса на авторизацию с измененными данными
        assertThat(statusCodeResponseLoginWithNewData, equalTo(200));
    }

    @Test
    @DisplayName("Change user password")
    @Description("Successfully change the user with password field with login")
    public void successfulChangeDataUserPasswordWithLogin() {

        //Создать пользователя
        ValidatableResponse responseCreatedUser = userClient.create(user);

        //Получить access токен
        accessToken = responseCreatedUser.extract().path("accessToken");

        //Сгенерировать данные поля "password"
        DataForCreateNewUser newUserData =  DataForCreateNewUser.getPassword();
        //Изменить данные пользователя
        ValidatableResponse responseChangeData = userClient.changeData(newUserData, accessToken);
        //Получить значение ключа "email"
        String actualEmail = responseChangeData.extract().path("user.email");
        //Получить значение ключа "name"
        String actualName = responseChangeData.extract().path("user.name");

        //Авторизоваться пользователем c новыми данными
        ValidatableResponse responseLoginWithNewData = userClient.login(new UserCredentials(user.email, newUserData.password));

        //Получить статус код запроса на изменение данных
        int statusCodeResponseChangeData = responseChangeData.extract().statusCode();
        //Получить статус код запроса на авторизацию с новыми данными
        int statusCodeResponseLoginWithNewData = responseLoginWithNewData.extract().statusCode();

        //Проверить статус код запроса на изменение данных
        assertThat(statusCodeResponseChangeData, equalTo(200));
        //Проверить значение ключа "email"
        assertThat("У пользователя не изменились данные email", actualEmail, equalTo(user.email));
        //Проверить значение ключа "name"
        assertThat("У пользователя не изменились данные name", actualName, equalTo(user.name));
        //Проверить статус код запроса на авторизацию с измененными данными
        assertThat(statusCodeResponseLoginWithNewData, equalTo(200));
    }

    @Test
    @DisplayName("Change user name")
    @Description("Successfully change the user with name field with login")
    public void successfulChangeDataUserNameWithLogin() {

        //Создать пользователя
        ValidatableResponse responseCreatedUser = userClient.create(user);

        //Получить access токен
        accessToken = responseCreatedUser.extract().path("accessToken");

        //Сгенерировать данные поля "name"
        DataForCreateNewUser newUserData =  DataForCreateNewUser.getName();
        //Изменить данные пользователя
        ValidatableResponse responseChangeData = userClient.changeData(newUserData, accessToken);
        //Получить значение ключа "email"
        String actualEmail = responseChangeData.extract().path("user.email");
        //Получить значение ключа "name"
        String actualName = responseChangeData.extract().path("user.name");

        //Авторизоваться пользователем c новыми данными
        ValidatableResponse responseLoginWithNewData = userClient.login(new UserCredentials(user.email, user.password));

        //Получить статус код запроса на изменение данных
        int statusCodeResponseChangeData = responseChangeData.extract().statusCode();
        //Получить статус код запроса на авторизацию с новыми данными
        int statusCodeResponseLoginWithNewData = responseLoginWithNewData.extract().statusCode();

        //Проверить статус код запроса на изменение данных
        assertThat(statusCodeResponseChangeData, equalTo(200));
        //Проверить значение ключа "email"
        assertThat("У пользователя не изменились данные email", actualEmail, equalTo(user.email));
        //Проверить значение ключа "name"
        assertThat("У пользователя не изменились данные name", actualName, equalTo(newUserData.name));
        //Проверить статус код запроса на авторизацию с измененными данными
        assertThat(statusCodeResponseLoginWithNewData, equalTo(200));
    }

    @Test
    @DisplayName("Change user data without login")
    @Description("Unsuccessfully change the user with all fields without login. Email address, password and name")
    public void unsuccessfulChangeDataUserEmailAndNameAndPasswordWithoutLogin() {

        //Создать пользователя
        userClient.create(user);

        //Сгенерировать случайные данные полей
        DataForCreateNewUser newUserData = DataForCreateNewUser.getRandom();
        //Изменить данные пользователя
        ValidatableResponse responseChangeDataWithoutToken = userClient.changeDataWithoutToken(newUserData);
        //Получить значение ключа "success"
        boolean isEmail = responseChangeDataWithoutToken.extract().path("success");
        //Получить значение ключа "name"
        String message = responseChangeDataWithoutToken.extract().path("message");

        //Авторизоваться пользователем c новыми данными
        ValidatableResponse responseLoginWithNewData = userClient.login(new UserCredentials(newUserData.email, newUserData.password));

        //Получить статус код запроса на изменение данных
        int statusCodeResponseChangeData = responseChangeDataWithoutToken.extract().statusCode();
        //Получить статус код запроса на авторизацию с новыми данными
        int statusCodeResponseLoginWithNewData = responseLoginWithNewData.extract().statusCode();

        //Проверить статус код запроса на изменение данных
        assertThat(statusCodeResponseChangeData, equalTo(401));
        //Проверить значение ключа "email"
        assertFalse(isEmail);
        //Проверить значение ключа "message"
        assertThat(message, equalTo("You should be authorised"));
        //Проверить статус код запроса на авторизацию с измененными данными
        assertThat(statusCodeResponseLoginWithNewData, equalTo(401));
    }
}