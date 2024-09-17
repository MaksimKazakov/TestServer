package ru.newatrast.test;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class TestTrast {

    int result;
    private int getResult() {
        return 3;
    }
    static {
        // Установка конфигурации Selenide для подключения к Selenoid
        Configuration.remote = "http://147.45.153.130:4444/wd/hub"; // Обратите внимание на http://
        Configuration.browser = "chrome"; // Укажите нужный браузер
        Configuration.browserSize = "1920x1080"; // Размер окна браузера

    }

    @Test
    @DisplayName("Open main page new.a-trast.ru")
    @Step("Open page https://new.a-trast.ru")
    public void test() {
        open("https://new.a-trast.ru");
        sleep(15000);
    }

    @Test
    @DisplayName("Secondary test")
    @Step("Test 2")
    void firstTest() {
        int result = getResult();
        System.out.println("###      firstTest()");
        Assertions.assertTrue(result > 2);
    }

    @Test
    @DisplayName("Test false")
    @Step("Test 3")
    void secondTest() {
        int result = getResult();
        System.out.println("###      secondTest()");
        Assertions.assertTrue(result> 2);
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver(); // Закрытие браузера после выполнения теста
    }
}
