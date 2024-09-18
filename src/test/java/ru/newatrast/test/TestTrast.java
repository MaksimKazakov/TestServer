package ru.newatrast.test;

import com.codeborne.selenide.Configuration;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;

public class TestTrast {

    @Test
    public void test() throws IOException {
        // Устанавливаем папку для загрузок
        String downloadFilepath = "/root/downloads";
        Configuration.downloadsFolder = downloadFilepath;

        // Открытие сайта
        open("https://new.a-trast.ru");
        $(By.xpath(".//div[contains(text(),'Вход')]")).click();
        $(By.xpath("//div[contains(@class,'login-form')]")).$(By.name("USER_LOGIN")).setValue("bayduganova");
        $(By.xpath("//div[contains(@class,'login-form')]")).$(By.name("USER_PASSWORD")).setValue("test_b2b");
        $(By.xpath(".//button/div[text()='Войти']")).click();
        $x(".//a[@href='/user/returns']").click();

        // Клик по элементу для скачивания
        $("span[data-tooltip='Скачать акт']").click();

        // Ожидание загрузки файла
        Path downloadedFile = $(By.cssSelector("span[data-tooltip='Скачать акт']"))
                .download().toPath();  // Этот метод дождётся завершения загрузки

        // Проверка, что файл скачан
        if (downloadedFile == null || !Files.exists(downloadedFile)) {
            throw new IOException("Файл не был найден в папке загрузок.");
        }

        // Проверка наличия изображений в скачанном файле
        try (FileInputStream fis = new FileInputStream(downloadedFile.toFile());
             XWPFDocument document = new XWPFDocument(fis)) {

            boolean hasImage = document.getAllPictures().size() > 0;

            if (hasImage) {
                System.out.println("Документ содержит изображение.");
            } else {
                System.out.println("В документе изображений нет.");
            }
        }
    }
}
