package ru.newatrast.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

        // Ссылка на скачивание
        String downloadUrl = "https://new.a-trast.ru/user/get-download-statement/28421";
        String expectedFileName = "a-trast_ru_statement_28421.docx";
        Path downloadsPath = Paths.get("build/downloads");

        // Убедитесь, что папка для загрузок существует
        if (!Files.exists(downloadsPath)) {
            Files.createDirectories(downloadsPath);
        }

        Path downloadedFile = downloadsPath.resolve(expectedFileName);

        // Скачивание файла
        try (InputStream in = new URL(downloadUrl).openStream()) {
            Files.copy(in, downloadedFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Файл скачан по пути: " + downloadedFile.toString());
        } catch (IOException e) {
            System.err.println("Ошибка при скачивании файла: " + e.getMessage());
            e.printStackTrace();
        }

        // Проверка, что файл скачан
        if (Files.exists(downloadedFile)) {
            System.out.println("Файл успешно скачан.");
        } else {
            throw new IOException("Файл не был найден в папке загрузок.");
        }
    }
}
