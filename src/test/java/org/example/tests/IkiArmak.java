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
 * CASE 2: Türk Hava Yolları İçin Fiyat Sıralanması
 *
 * Gereksinimleri:
 * - İstanbul - Ankara arası gidiş-dönüş arama
 * - 10:00 - 18:00 saatinde kalkış filtresi
 * - Türk Hava Yolları seçimi
 * - Fiyatlar artan sırada sıralanır
 * - Tüm sonuçlar Türk Hava Yolları uçuşları
 * - Fiyat sıralamsı doğru
 */
public class IkiArmak {

    private WebDriver surucu;
    private UcusSaramasiSayfasi sayfasi;
    private String testAdi = "Case2_PriceSortingTurkishAirlines";

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
    public void turkHavaYolluFiyatSiralamasi() {
        try {
            // 1. Sayfayı yükle
            TestRaporlayici.logYaz(testAdi, "Enuygun.com yükleniyor...");
            sayfasi.sayfayuKle();

            // 2. Parametrize değerler
            String kalkisSehri = "İstanbul";
            String varisSehri = "Ankara";
            String kalkisTarihi = "15.11.2024";
            String donusTarihi = "20.11.2024";
            String baslangicSaati = "10:00";
            String bitisSaati = "18:00";
            String havayoluAdi = "Türk Hava Yolları";

            TestRaporlayici.logYaz(testAdi, kalkisSehri + " -> " + varisSehri + " aranıyor");

            // 3. Arama kriterleri gir
            sayfasi.kalkisSehriGir(kalkisSehri);
            sayfasi.varisSehriGir(varisSehri);
            sayfasi.kalkisTarihiGir(kalkisTarihi);
            sayfasi.donusTarihiGir(donusTarihi);

            // 4. Saat filtresi uygula
            TestRaporlayici.logYaz(testAdi, "Saat filtresi uygulanıyor: " + baslangicSaati + " - " + bitisSaati);
            sayfasi.saatFiltresiniUygula(baslangicSaati, bitisSaati);

            // 5. Havayolu seç (Türk Hava Yolları)
            TestRaporlayici.logYaz(testAdi, "Havayolu seçiliyor: " + havayoluAdi);
            sayfasi.havayoluSeç(havayoluAdi);

            // 6. Arama yap
            TestRaporlayici.logYaz(testAdi, "Arama gerçekleştiriliyor...");
            sayfasi.aramaYap();

            // 7. Sonuçları doğrula
            Thread.sleep(3000);

            List<WebElement> ucuslar = sayfasi.ucuslariAl();
            Assert.assertTrue(ucuslar.size() > 0, "Türk Hava Yolları sonucu bulunamamıştır");

            TestRaporlayici.logYaz(testAdi, "Toplam " + ucuslar.size() + " Türk Hava Yolları uçuşu bulundu");

            // 8. Tüm sonuçlar Türk Hava Yolları'na ait mi?
            int turkHavaYolluSayisi = 0;
            for (WebElement ucus : ucuslar) {
                String havayolu = sayfasi.ucusHavayoluAdiAl(ucus);
                if (havayolu.toLowerCase().contains("türk")) {
                    turkHavaYolluSayisi++;
                }
            }

            TestRaporlayici.logYaz(testAdi, "Türk Hava Yolları uçuş doğrulandı: " + turkHavaYolluSayisi + " / " + ucuslar.size());
            Assert.assertEquals(turkHavaYolluSayisi, ucuslar.size(),
                    "Tüm sonuçlar Türk Hava Yolları'na ait değildir");

            // 9. Fiyatlar artan sırada mı?
            double oncekiFiyat = 0;
            boolean siralamaDogru = true;

            for (WebElement ucus : ucuslar) {
                String fiyatMetni = sayfasi.ucusFiyatiAl(ucus);
                // Fiyatı parse et (örn: "500 TL" -> 500)
                double fiyat = Double.parseDouble(fiyatMetni.replaceAll("[^0-9.]", ""));

                if (fiyat < oncekiFiyat) {
                    siralamaDogru = false;
                    break;
                }
                oncekiFiyat = fiyat;
            }

            TestRaporlayici.logYaz(testAdi, "Fiyat sıralaması doğru: " + siralamaDogru);
            Assert.assertTrue(siralamaDogru, "Fiyatlar artan sırada değildir");

            TestRaporlayici.testBasarili(testAdi);

        } catch (Exception e) {
            TestRaporlayici.testBasarisiz(testAdi, e.getMessage());
            throw new AssertionError(testAdi + " test başarısız oldu: " + e.getMessage());
        }
    }
}
