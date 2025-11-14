package org.example.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import org.example.config.Surucu;
import org.example.pages.UcusSaramasiSayfasi;
import org.example.utils.TestRaporlayici;
import java.util.List;

/**
 * CASE 3: Kritik Yol Testi
 *
 * Gereksinimleri:
 * - En kritik ve sık kullanılan kullanıcı yolculuğu
 * - Başlangıç sayfası yükle
 * - Parametrize arama yap
 * - Filtreler uygula
 * - Sonuçları doğrula
 * - Uçuş seç
 * - Doğru sayfaya yönlendir
 *
 * Kritik Yol:
 * İstanbul -> Ankara Gidiş-Dönüş Arama ->
 * Saat ve Havayolu Filtresi -> Sonuçlar Görüntüleme
 */
public class UcunuSeç {

    private WebDriver surucu;
    private UcusSaramasiSayfasi sayfasi;
    private String testAdi = "Case3_CriticalPathTesting";

    @BeforeClass
    public void setup() {
        String tarayici = System.getProperty("browser", "chrome");
        surucu = Surucu.baslat(tarayici);
        sayfasi = new UcusSaramasiSayfasi(surucu);
        TestRaporlayici.logYaz(testAdi, "Test başlatılıyor...");
    }

    @AfterClass
    public void teardown() {
        if (surucu != null) {
            Surucu.kapat();
        }
    }

    @Test
    public void kritikYoluTestet() {
        try {
            // ADIM 1: Sayfayı yükle
            TestRaporlayici.logYaz(testAdi, "ADIM 1: Ana sayfa yükleniyor...");
            sayfasi.sayfayuKle();
            Assert.assertTrue(surucu.getTitle().length() > 0, "Sayfa başlığı boş");
            TestRaporlayici.logYaz(testAdi, "✓ Sayfa başarıyla yüklendi");

            // ADIM 2: Arama Parametreleri
            String kalkisSehri = "İstanbul";
            String varisSehri = "Ankara";
            String kalkisTarihi = "15.11.2024";
            String donusTarihi = "20.11.2024";

            // ADIM 3: Arama Kriterleri Gir
            TestRaporlayici.logYaz(testAdi, "ADIM 2: Arama kriterleri giriş yapılıyor...");
            sayfasi.kalkisSehriGir(kalkisSehri);
            TestRaporlayici.logYaz(testAdi, "  - Kalkış şehri: " + kalkisSehri);

            sayfasi.varisSehriGir(varisSehri);
            TestRaporlayici.logYaz(testAdi, "  - Varış şehri: " + varisSehri);

            sayfasi.kalkisTarihiGir(kalkisTarihi);
            TestRaporlayici.logYaz(testAdi, "  - Kalkış tarihi: " + kalkisTarihi);

            sayfasi.donusTarihiGir(donusTarihi);
            TestRaporlayici.logYaz(testAdi, "  - Dönüş tarihi: " + donusTarihi);

            // ADIM 4: Filtreler Uygula
            TestRaporlayici.logYaz(testAdi, "ADIM 3: Filtreler uygulanıyor...");
            sayfasi.saatFiltresiniUygula("10:00", "18:00");
            TestRaporlayici.logYaz(testAdi, "  - Saat filtresi: 10:00 - 18:00");

            sayfasi.havayoluSeç("Türk Hava Yolları");
            TestRaporlayici.logYaz(testAdi, "  - Havayolu: Türk Hava Yolları");

            // ADIM 5: Arama Yap
            TestRaporlayici.logYaz(testAdi, "ADIM 4: Arama gerçekleştiriliyor...");
            sayfasi.aramaYap();
            Thread.sleep(3000);

            // ADIM 6: Sonuçları Doğrula
            TestRaporlayici.logYaz(testAdi, "ADIM 5: Sonuçlar doğrulanıyor...");
            List<WebElement> ucuslar = sayfasi.ucuslariAl();

            Assert.assertTrue(ucuslar.size() > 0, "Arama sonucu bulunamamıştır");
            TestRaporlayici.logYaz(testAdi, "  ✓ Toplam sonuç: " + ucuslar.size() + " uçuş");

            // ADIM 7: Birinci Uçuş Bilgilerini Doğrula
            TestRaporlayici.logYaz(testAdi, "ADIM 6: İlk uçuş bilgileri kontrol ediliyor...");
            WebElement ilkUcus = ucuslar.get(0);

            String havayolu = sayfasi.ucusHavayoluAdiAl(ilkUcus);
            String fiyat = sayfasi.ucusFiyatiAl(ilkUcus);
            String kalkisSaati = sayfasi.ucusKalkisSaatiAl(ilkUcus);

            TestRaporlayici.logYaz(testAdi, "  - Havayolu: " + havayolu);
            TestRaporlayici.logYaz(testAdi, "  - Kalkış Saati: " + kalkisSaati);
            TestRaporlayici.logYaz(testAdi, "  - Fiyat: " + fiyat);

            // ADIM 8: Validasyonlar
            Assert.assertTrue(havayolu.toLowerCase().contains("türk"),
                    "Havayolu Türk Hava Yolları değildir");
            Assert.assertTrue(sayfasi.saatAraliktaMi(kalkisSaati, "10:00", "18:00"),
                    "Kalkış saati belirtilen aralık dışındadır");
            Assert.assertTrue(fiyat.length() > 0, "Fiyat bilgisi alınamadı");

            TestRaporlayici.logYaz(testAdi, "✓ Tüm validasyonlar başarılı");
            TestRaporlayici.testBasarili(testAdi);

        } catch (Exception e) {
            TestRaporlayici.testBasarisiz(testAdi, e.getMessage());
            throw new AssertionError(testAdi + " test başarısız oldu: " + e.getMessage());
        }
    }
}
