package praktikum.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.OrderClient;
import praktikum.client.UserClient;
import praktikum.data.DataForCreateNewUser;
import praktikum.data.IngredientsForCreateNewBurger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class GetOrderUserTests {

    private UserClient userClient;
    private DataForCreateNewUser user;
    String accessToken;
    private IngredientsForCreateNewBurger ingredientsForCreateNewBurger;
    private OrderClient orderClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
        //Сгенерировать случайные данные полей
        user = DataForCreateNewUser.getRandom();
        orderClient = new OrderClient();
        //Сгенерировать случайный бургер
        ingredientsForCreateNewBurger = IngredientsForCreateNewBurger.getRandom();
    }

    @After
    public void tearDown() {
        //Удалить пользователя
        if (accessToken != "") {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Get orders from a unique logged in user")
    @Description("Successfully get orders from a unique logged in user")
    public void successfulGetOrdersWithLogin() {

        //Создать пользователя
        ValidatableResponse response = userClient.create(user);

        //Получить access токен
        accessToken = response.extract().path("accessToken");

        //Получить заказ пользователя
        ValidatableResponse responseOrder = orderClient.userOrderInfo(accessToken);

        //Получить статус кода запроса
        int statusCodeResponseOrder = responseOrder.extract().statusCode();
        //Получить значение ключа "success"
        boolean isGetOrders = responseOrder.extract().path("success");

        //Проверить статус код
        assertThat(statusCodeResponseOrder, equalTo(200));
        //Проверить успешность получения заказов
        assertTrue("Заказы не получены", isGetOrders);
    }

    @Test
    @DisplayName("Get orders from a unique unregistered user")
    @Description("Unsuccessfully get orders from a unique unregistered user")
    public void unsuccessfulGetOrdersWithoutLogin() {

        //Получить заказ
        ValidatableResponse responseOrder = orderClient.userOrderInfoWithoutToken();

        //Получить статус кода запроса
        int statusCodeResponseOrder = responseOrder.extract().statusCode();
        //Получить значение ключа "success"
        boolean isGetOrders = responseOrder.extract().path("success");
        //Получить значение ключа "message"
        String message = responseOrder.extract().path("message");

        //Проверить статус код
        assertThat(statusCodeResponseOrder, equalTo(401));
        //Проверить успешность получения заказов
        assertFalse("Операция успешна", isGetOrders);
        //Проверить сообщение
        assertThat(message, equalTo("You should be authorised"));
    }
}