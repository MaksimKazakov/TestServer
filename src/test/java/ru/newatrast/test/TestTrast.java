package ru.newatrast.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.io.IOException;
import java.nio.file.Path;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class TestTrast {
    static {
        // Установка конфигурации Selenide для подключения к Selenoid
        Configuration.remote = "http://147.45.153.130:4444/wd/hub";
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.downloadsFolder = "build/downloads"; // Установите путь загрузки
    }

    @Test
    public void test() throws IOException {
        // Открытие сайта
        open("https://new.a-trast.ru");
        $(By.xpath(".//div[contains(text(),'Вход')]")).click();
        $(By.xpath("//div[contains(@class,'login-form')]")).$(By.name("USER_LOGIN")).setValue("bayduganova");
        $(By.xpath("//div[contains(@class,'login-form')]")).$(By.name("USER_PASSWORD")).setValue("test_b2b");
        $(By.xpath(".//button/div[text()='Войти']")).click();
        $x(".//a[@href='/user/returns']").click();

        // Логируем перед попыткой найти элемент
        System.out.println("Пробуем найти элемент для скачивания...");
        SelenideElement downloadElement = $("span[data-tooltip='Скачать акт']").shouldBe(visible);
        System.out.println("Элемент найден, пытаемся скачать файл...");

        // Клик по элементу для скачивания и ожидание загрузки файла
        Path downloadedFile = downloadElement.download().toPath();
        System.out.println("Файл скачан по пути: " + downloadedFile.toString());
    }
}
