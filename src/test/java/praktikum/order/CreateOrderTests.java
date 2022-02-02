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

public class CreateOrderTests {

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
    @DisplayName("Create order with ingredients and without login")
    @Description("Successfully create order with ingredients and without login")
    public void successfulCreateOrderWithIngredientsAndWithoutLogin() {

        //Без токена
        accessToken = "";

        //Создать заказ
        ValidatableResponse responseOrder = orderClient.createOrder(ingredientsForCreateNewBurger, accessToken);

        //Получить статус кода запроса
        int statusCodeResponseOrder = responseOrder.extract().statusCode();
        //Получить значение ключа "success"
        boolean isOrderCreated = responseOrder.extract().path("success");
        // Получение номера созданого заказа
        int orderNumber = responseOrder.extract().path("order.number");

        //Проверить статус код
        assertThat(statusCodeResponseOrder, equalTo(200));
        //Проверить создание заказа
        assertTrue("Order is not created", isOrderCreated);
        //Проверить наличие номера созданного заказа
        assertNotNull("Пустой номер заказа", orderNumber);
    }

    @Test
    @DisplayName("Create order with ingredients and login")
    @Description("Successfully create order with ingredients and login")
    public void successfulCreateOrderWithIngredientsAndWithLogin() {

        //Создать пользователя
        ValidatableResponse response = userClient.create(user);

        //Получить access токен
        accessToken = response.extract().path("accessToken");

        //Создать заказ
        ValidatableResponse responseOrder = orderClient.createOrder(ingredientsForCreateNewBurger, accessToken);

        //Получить статус кода запроса
        int statusCodeResponseOrder = responseOrder.extract().statusCode();
        //Получить значение ключа "success"
        boolean isOrderCreated = responseOrder.extract().path("success");
        // Получение номера созданого заказа
        int orderNumber = responseOrder.extract().path("order.number");

        //Проверить статус код
        assertThat(statusCodeResponseOrder, equalTo(200));
        //Проверить создание заказа
        assertTrue("Order is not created", isOrderCreated);
        //Проверить наличие номера созданного заказа
        assertNotNull("Пустой номе заказа", orderNumber);
    }

    @Test
    @DisplayName("Create order with not really ingredients and with login")
    @Description("Unsuccessfully create order with not really ingredients and with login")
    public void unsuccessfulCreateOrderWithNotReallyIngredientsAndWithLogin() {

        //Создать пользователя
        ValidatableResponse response = userClient.create(user);

        //Получить access токен
        accessToken = response.extract().path("accessToken");

        //Создать заказ
        ValidatableResponse responseOrder = orderClient.createOrder(IngredientsForCreateNewBurger.getNotReallyIngredients(), accessToken);

        //Получить статус кода запроса
        int statusCodeResponseOrder = responseOrder.extract().statusCode();

        //Проверить статус код
        assertThat(statusCodeResponseOrder, equalTo(500));
    }

    @Test
    @DisplayName("Create order without ingredients and with login")
    @Description("Unsuccessfully create order without ingredients and with login")
    public void unsuccessfulCreateOrderWithoutIngredientsAndWithLogin() {

        //Создать пользователя
        ValidatableResponse response = userClient.create(user);

        //Получить access токен
        accessToken = response.extract().path("accessToken");

        //Создать заказ
        ValidatableResponse responseOrder = orderClient.createOrder(IngredientsForCreateNewBurger.getWithoutIngredients(), accessToken);

        //Получить статус кода запроса
        int statusCodeResponseOrder = responseOrder.extract().statusCode();
        //Получить значение ключа "success"
        boolean isOrderCreated = responseOrder.extract().path("success");
        // Получение номера ключа "message"
        String orderMessage = responseOrder.extract().path("message");

        //Проверить статус код
        assertThat(statusCodeResponseOrder, equalTo(400));
        //Проверить создание заказа
        assertFalse("Order is created", isOrderCreated);
        //Проверить текст
        assertThat(orderMessage, equalTo("Ingredient ids must be provided"));
    }
}