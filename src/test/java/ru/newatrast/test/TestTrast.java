package ru.newatrast.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.apache.logging.log4j.util.PropertySource;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;

public class TestTrast {
    @Test
    public void test() throws IOException {
        open("https://new.a-trast.ru");
        $(By.xpath(".//div[contains(text(),'Вход')]")).click();
        $(By.xpath("//div[contains(@class,'login-form')]")).$(By.name("USER_LOGIN")).setValue("bayduganova");
        $(By.xpath("//div[contains(@class,'login-form')]")).$(By.name("USER_PASSWORD")).setValue("test_b2b");
        $(By.xpath(".//button/div[text()='Войти']")).click();
        $x(".//a[@href='/user/returns']").click();

        // Клик по элементу для скачивания
        $("span[data-tooltip='Скачать акт']").click();

        // Делаем паузу для ожидания открытия новой вкладки
        sleep(10000); // Увеличьте время, если это необходимо


        // Находим последний загруженный файл в папке build/downloads
        Path downloadsDir = Paths.get("build/downloads");
        Optional<Path> latestFile;

        try (Stream<Path> paths = Files.walk(downloadsDir)) {
            latestFile = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".docx"))
                    .max(Comparator.comparing(Path::getFileName));

        }

        if (latestFile.isEmpty()) {
            throw new IOException("Файл не был найден в папке загрузок.");
        }

        // Проверяем наличие изображений в скачанном файле
        try (FileInputStream fis = new FileInputStream(latestFile.get().toFile());
             XWPFDocument document = new XWPFDocument(fis)) {

            boolean hasImage = document.getAllPictures().size() > 0;

            if (hasImage) {
                System.out.println("Документ содержит изображение.");
            } else {
                System.out.println("В документе изображений нет.");
            }
        }
    }}
