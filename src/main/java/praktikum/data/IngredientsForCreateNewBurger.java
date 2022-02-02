package praktikum.data;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.client.RestAssuredClient;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public class IngredientsForCreateNewBurger extends RestAssuredClient {

    private static final String INGREDIENTS_PATH = "/api/ingredients";
    public ArrayList<Object> ingredients;
    public static Faker faker = new Faker();


    public IngredientsForCreateNewBurger(ArrayList<Object> ingredients) {
        this.ingredients = ingredients;
    }

    @Step
    public static IngredientsForCreateNewBurger getRandom(){

        ValidatableResponse response = given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH)
                .then()
                .statusCode(200);

        ArrayList<Object> ingredients = new ArrayList<>();

        int bunIndex = nextInt(0,2);
        int mainIndex = nextInt(0,9);
        int sauceIndex = nextInt(0,4);

        List<Object> bunIngredients = response.extract().jsonPath().getList("data.findAll{it.type == 'bun'}._id");
        List<Object> mainIngredients = response.extract().jsonPath().getList("data.findAll{it.type == 'main'}._id");
        List<Object> sauceIngredients = response.extract().jsonPath().getList("data.findAll{it.type == 'sauce'}._id");

        ingredients.add(bunIngredients.get(bunIndex));
        ingredients.add(mainIngredients.get(mainIndex));
        ingredients.add(sauceIngredients.get(sauceIndex));

        return new IngredientsForCreateNewBurger(ingredients);
    }

    public static IngredientsForCreateNewBurger getWithoutIngredients() {
        ArrayList<Object> ingredients = new ArrayList<>();
        return new IngredientsForCreateNewBurger(ingredients);
    }

    public static IngredientsForCreateNewBurger getNotReallyIngredients() {
        ArrayList<Object> ingredients = new ArrayList<>();
        ingredients.add(faker.internet().uuid());
        ingredients.add(faker.internet().uuid());
        ingredients.add(faker.internet().uuid());
        ingredients.add(faker.internet().uuid());
        return new IngredientsForCreateNewBurger(ingredients);
    }

}
