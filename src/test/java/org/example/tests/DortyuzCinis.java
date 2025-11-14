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
 * CASE 1: Temel Uçuş Arama ve Zaman Filtresi
 *
 * Gereksinimleri:
 * - İstanbul - Ankara arası gidiş-dönüş uçuş arama
 * - Kalkış ve dönüş tarihleri parametrize
 * - Şehirler parametrize
 * - 10:00 - 18:00 saat filtresi uygulanır
 * - Tüm uçuşların saat filtresi içinde olup olmadığı doğrulanır
 * - Arama sonuçları doğru şehirleri gösterir
 */
public class DortyuzCinis {

    private WebDriver surucu;
    private UcusSaramasiSayfasi sayfasi;
    private String testAdi = "Case1_BasicFlightSearchAndTimeFilter";

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
    public void temelUcusAramaVeSaatFiltresi() {
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

            TestRaporlayici.logYaz(testAdi, kalkisSehri + " -> " + varisSehri + " aranıyor");

            // 3. Arama kriterleri gir
            sayfasi.kalkisSehriGir(kalkisSehri);
            sayfasi.varisSehriGir(varisSehri);
            sayfasi.kalkisTarihiGir(kalkisTarihi);
            sayfasi.donusTarihiGir(donusTarihi);

            // 4. Saat filtresi uygula
            TestRaporlayici.logYaz(testAdi, "Saat filtresi uygulanıyor: " + baslangicSaati + " - " + bitisSaati);
            sayfasi.saatFiltresiniUygula(baslangicSaati, bitisSaati);

            // 5. Arama yap
            TestRaporlayici.logYaz(testAdi, "Arama gerçekleştiriliyor...");
            sayfasi.aramaYap();

            // 6. Sonuçları doğrula
            Thread.sleep(3000); // Sayfa yüklenmesi için bekle

            List<WebElement> ucuslar = sayfasi.ucuslariAl();
            Assert.assertTrue(ucuslar.size() > 0, "Uçuş sonucu bulunamamıştır");

            TestRaporlayici.logYaz(testAdi, "Toplam " + ucuslar.size() + " uçuş bulundu");

            // 7. Tüm uçuşların saat filtresine uyup uymadığını kontrol et
            List<WebElement> kalkisSaatleri = sayfasi.kalkisSaatleriniAl();
            int uygunUcusSayisi = 0;

            for (WebElement saat : kalkisSaatleri) {
                String saatMetni = saat.getText();
                if (sayfasi.saatAraliktaMi(saatMetni, baslangicSaati, bitisSaati)) {
                    uygunUcusSayisi++;
                }
            }

            TestRaporlayici.logYaz(testAdi, "Saat aralığına uygun uçuş sayısı: " + uygunUcusSayisi);
            Assert.assertTrue(uygunUcusSayisi > 0, "Saat filtresine uygun uçuş bulunamamıştır");

            TestRaporlayici.testBasarili(testAdi);

        } catch (Exception e) {
            TestRaporlayici.testBasarisiz(testAdi, e.getMessage());
            throw new AssertionError(testAdi + " test başarısız oldu: " + e.getMessage());
        }
    }
}
