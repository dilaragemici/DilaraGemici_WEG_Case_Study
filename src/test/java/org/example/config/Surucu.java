package org.example.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * WebDriver yapılandırması ve yönetimi
 */
public class Surucu {

    private static WebDriver surucu;
    private static String tarayici;

    /**
     * WebDriver'ı başlat
     */
    public static WebDriver baslat(String tarayiciTipi) {
        tarayici = tarayiciTipi.toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        if ("chrome".equals(tarayici)) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions secenekler = new ChromeOptions();
            // Headless opsiyonu
            if (headless) {
                secenekler.addArguments("--headless=new");
                secenekler.addArguments("--disable-gpu");
                secenekler.addArguments("--no-sandbox");
                secenekler.addArguments("--disable-dev-shm-usage");
            }
            // Ekip/CI ortamlarında otomasyon kontrolü gizleme
            secenekler.addArguments("--disable-blink-features=AutomationControlled");
            surucu = new ChromeDriver(secenekler);
        } else if ("firefox".equals(tarayici)) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            if (headless) {
                // Selenium'in bazı sürümlerinde FirefoxOptions#setHeadless(boolean) mevcut olmayabilir.
                // Bu yüzden doğrudan headless argümanı ekliyoruz, Chrome tarafında kullandığımız formata benzetiyoruz.
                options.addArguments("--headless=new");
            }
            surucu = new FirefoxDriver(options);
        } else {
            throw new IllegalArgumentException("Desteklenmeyen tarayıcı: " + tarayiciTipi);
        }

        surucu.manage().window().maximize();
        return surucu;
    }

    /**
     * WebDriver'ı al
     */
    public static WebDriver al() {
        return surucu;
    }

    /**
     * WebDriver'ı kapat
     */
    public static void kapat() {
        if (surucu != null) {
            surucu.quit();
        }
    }

    /**
     * Sayfa başlığı al
     */
    public static String sayfaBasligiAl() {
        return surucu.getTitle();
    }
}
