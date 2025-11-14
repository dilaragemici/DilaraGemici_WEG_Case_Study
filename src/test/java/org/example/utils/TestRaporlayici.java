package org.example.utils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Test raporlaması ve ekran görüntüsü yönetimi
 */
public class TestRaporlayici {

    private static final String RAPOR_DIZINI = "test-raporlari";
    private static final String SCREENSHOT_DIZINI = "screenshots";

    static {
        // Dizinleri oluştur
        new File(RAPOR_DIZINI).mkdirs();
        new File(SCREENSHOT_DIZINI).mkdirs();
    }

    /**
     * Başarısız test için ekran görüntüsü yolunu döndürür
     */
    public static String ekranGoruntusyuKaydet(String testAdi) {
        String zaman = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String dosyaYolu = SCREENSHOT_DIZINI + "/" + testAdi + "_" + zaman + ".png";
        return dosyaYolu;
    }

    /**
     * Test log'u yaz
     */
    public static void logYaz(String testAdi, String mesaj) {
        String zaman = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.printf("[%s] %s: %s%n", zaman, testAdi, mesaj);
    }

    /**
     * Test başarılı mesajı
     */
    public static void testBasarili(String testAdi) {
        logYaz(testAdi, "✓ TEST BAŞARILI");
    }

    /**
     * Test başarısız mesajı
     */
    public static void testBasarisiz(String testAdi, String neden) {
        logYaz(testAdi, "✗ TEST BAŞARISIZ: " + neden);
    }
}

