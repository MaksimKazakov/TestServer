package ru.newatrast.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.io.IOException;
import java.nio.file.Path;

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

        // Клик по элементу для скачивания и ожидание загрузки файла
        Path downloadedFile = $("span[data-tooltip='Скачать акт']").download().toPath(); // Это автоматическое ожидание завершения загрузки

        // Проверка пути файла
        System.out.println("Файл скачан по пути: " + downloadedFile.toString());
    }
}
