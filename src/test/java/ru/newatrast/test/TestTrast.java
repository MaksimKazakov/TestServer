package ru.newatrast.test;

import com.codeborne.selenide.Configuration;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;

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
        // Установка конфигурации Selenide для подключения к Selenoid
        Configuration.remote = "http://147.45.153.130:4444/wd/hub";
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.downloadsFolder = "build/downloads"; // Установите путь загрузки
    }

    @Test
    public void test() throws IOException {

            String downloadFilepath = "/root/downloads";
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadFilepath);
        chromePrefs.put("download.prompt_for_download", false);
        chromePrefs.put("download.directory_upgrade", true);
        chromePrefs.put("safebrowsing.enabled", true);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);

        // Открытие сайта
        open("https://new.a-trast.ru");
        $(By.xpath(".//div[contains(text(),'Вход')]")).click();
        $(By.xpath("//div[contains(@class,'login-form')]")).$(By.name("USER_LOGIN")).setValue("bayduganova");
        $(By.xpath("//div[contains(@class,'login-form')]")).$(By.name("USER_PASSWORD")).setValue("test_b2b");
        $(By.xpath(".//button/div[text()='Войти']")).click();
        $x(".//a[@href='/user/returns']").click();

        // Клик по элементу для скачивания
        $("span[data-tooltip='Скачать акт']").click();

        sleep(30000);
    }
}
