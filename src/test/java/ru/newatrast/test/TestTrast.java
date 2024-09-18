package ru.newatrast.test;
import com.codeborne.selenide.Configuration;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;

public class TestTrast {
    static {
        // Настройки ChromeOptions
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", new HashMap<String, Object>() {{
            put("download.default_directory", "/home/selenium/Downloads");
            put("download.prompt_for_download", false);
            put("download.directory_upgrade", true);
            put("safebrowsing.enabled", true);
        }});
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--headless");

        // Установка конфигурации Selenide для подключения к Selenoid
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        Configuration.remote = "http://147.45.153.130:4444/wd/hub";
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.browserCapabilities = capabilities;
    }
//
   private final String downloadFilepath = "/home/selenium/Downloads";

    @Test
    public void test() throws IOException {
        open("https://new.a-trast.ru");
        $(By.xpath(".//div[contains(text(),'Вход')]")).click();
        $(By.xpath("//div[contains(@class,'login-form')]")).$(By.name("USER_LOGIN")).setValue("bayduganova");
        $(By.xpath("//div[contains(@class,'login-form')]")).$(By.name("USER_PASSWORD")).setValue("test_b2b");
        $(By.xpath(".//button/div[text()='Войти']")).click();
        $x(".//a[@href='/user/returns']").click();
        $("span[data-tooltip='Скачать акт']").click();

        // Ожидание загрузки
        sleep(20000); // Увеличьте время, если это необходимо

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

            System.out.println(hasImage ? "Документ содержит изображение." : "В документе изображений нет.");
        }
    }
}
