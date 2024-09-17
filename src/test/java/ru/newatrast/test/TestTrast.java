package ru.newatrast.test;
import com.codeborne.selenide.Configuration;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.Comparator;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;

public class TestTrast {

    @Test
    public void test() throws IOException {
        // Настройки для автоматического скачивания
        String downloadFilepath = "/root/downloads";
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadFilepath);
        chromePrefs.put("download.prompt_for_download", false);
        chromePrefs.put("download.directory_upgrade", true);
        chromePrefs.put("safebrowsing.enabled", true);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);

        Configuration.browserCapabilities.setCapability(ChromeOptions.CAPABILITY, options);

        // Ваш тестовый код
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

        // Проверка файла
        Path downloadsPath = Paths.get(downloadFilepath);
        Optional<Path> latestFile;

        try (Stream<Path> paths = Files.walk(downloadsPath)) {
            latestFile = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".docx"))
                    .max(Comparator.comparing(Path::getFileName));
        }

        if (latestFile.isEmpty()) {
            throw new IOException("Файл не был найден в папке загрузок.");
        }

        // Проверка наличия изображений в скачанном файле
        try (FileInputStream fis = new FileInputStream(latestFile.get().toFile());
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
