package org.example.pages;

import org.example.base.TabanSayfa;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Enuygun.com uçuş arama sayfası için Page Object Model
 */
public class UcusSaramasiSayfasi extends TabanSayfa {

    private static final String BASE_URL = "https://www.enuygun.com";

    // Konum (Locator) tanımları
    private static final By KALKIS_SEHRI_INPUT = By.id("from");
    private static final By VARIS_SEHRI_INPUT = By.id("to");
    private static final By KALKIS_TARIHI_INPUT = By.id("depart");
    private static final By DONUS_TARIHI_INPUT = By.id("return");
    private static final By ARAMA_BUTONU = By.xpath("//button[contains(@class, 'search') or contains(@class,'btn-search')]");

    // Filtre bölümleri (genel locator'lar, site değişirse güncelleyin)
    private static final By SAAT_FILTRESI = By.xpath("//div[contains(@class, 'time-filter')]");
    private static final By HAVAYOLU_FILTRESI = By.xpath("//div[contains(@class, 'airline-filter')]");

    // Sonuçlar
    private static final By UCUS_LISTESI = By.xpath("//div[contains(@class,'flight-result') or contains(@class,'result-item')]");
    private static final By UCUS_SAATI = By.xpath(".//span[contains(@class,'departure-time') or contains(@class,'time-depart')]");
    private static final By HAVAYOLU_ADI = By.xpath(".//span[contains(@class,'airline-name') or contains(@class,'airline')]");
    private static final By UCUS_FIYATI = By.xpath(".//span[contains(@class,'price') or contains(@class,'fare')]");

    public UcusSaramasiSayfasi(WebDriver surucu) {
        super(surucu);
    }

    /** Sayfa yüklenir */
    public void sayfayuKle() {
        sayfayuKle(BASE_URL);
        goru(KALKIS_SEHRI_INPUT);
    }

    /** Kalkış şehrini girer */
    public void kalkisSehriGir(String sehir) {
        metinGir(KALKIS_SEHRI_INPUT, sehir);
    }

    /** Varış şehrini girer */
    public void varisSehriGir(String sehir) {
        metinGir(VARIS_SEHRI_INPUT, sehir);
    }

    /** Kalkış tarihini girer */
    public void kalkisTarihiGir(String tarih) {
        metinGir(KALKIS_TARIHI_INPUT, tarih);
    }

    /** Dönüş tarihini girer */
    public void donusTarihiGir(String tarih) {
        metinGir(DONUS_TARIHI_INPUT, tarih);
    }

    /** Arama butonuna tıklar */
    public void aramaYap() {
        elemanaTikla(ARAMA_BUTONU);
    }

    /** Saat filtresini uygular (örnek: 10:00 - 18:00) */
    public void saatFiltresiniUygula(String baslangicSaati, String bitisSaati) {
        try {
            // Bu locator'lar sitenin gerçek yapısına göre güncellenmelidir
            By baslangicFiltresi = By.xpath("//input[@data-start='" + baslangicSaati + "']");
            By bitisFiltresi = By.xpath("//input[@data-end='" + bitisSaati + "']");
            elemanaTikla(baslangicFiltresi);
            elemanaTikla(bitisFiltresi);
        } catch (Exception e) {
            System.out.println("Saat filtresi uygulanırken hata: " + e.getMessage());
        }
    }

    /** Belirli bir havayolunu seçer */
    public void havayoluSeç(String havayoluAdi) {
        try {
            By havayoluCheckbox = By.xpath("//label[contains(., '" + havayoluAdi + "')]/preceding-sibling::input | //label[contains(., '" + havayoluAdi + "')]/..//input[@type='checkbox']");
            elemanaTikla(havayoluCheckbox);
        } catch (Exception e) {
            System.out.println("Havayolu seçilirken hata: " + e.getMessage());
        }
    }

    /** Tüm uçuş sonuçlarını döndürür */
    public List<WebElement> ucuslariAl() {
        return surucu.findElements(UCUS_LISTESI);
    }

    /** Uçuş kalkış saatlerini alır */
    public List<WebElement> kalkisSaatleriniAl() {
        return surucu.findElements(By.xpath("//span[contains(@class,'departure-time') or contains(@class,'time-depart')]"));
    }

    /** Belirli bir uçuş için havayolu adını döndürür */
    public String ucusHavayoluAdiAl(WebElement ucus) {
        try {
            return ucus.findElement(HAVAYOLU_ADI).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /** Belirli bir uçuş için fiyatı döndürür */
    public String ucusFiyatiAl(WebElement ucus) {
        try {
            return ucus.findElement(UCUS_FIYATI).getText();
        } catch (Exception e) {
            return "0";
        }
    }

    /** Belirli bir uçuş için kalkış saatini döndürür */
    public String ucusKalkisSaatiAl(WebElement ucus) {
        try {
            return ucus.findElement(UCUS_SAATI).getText();
        } catch (Exception e) {
            return "00:00";
        }
    }

    /** Saatin belirtilen aralıkta olup olmadığını kontrol eder (HH:mm formatı) */
    public boolean saatAraliktaMi(String saat, String baslangic, String bitis) {
        try {
            if (saat == null || saat.isEmpty()) return false;
            String[] saatParcalari = saat.split(":");
            String[] baslangicParcalari = baslangic.split(":");
            String[] bitisParcalari = bitis.split(":");

            int saatDegeri = Integer.parseInt(saatParcalari[0]);
            int baslangicDegeri = Integer.parseInt(baslangicParcalari[0]);
            int bitisDegeri = Integer.parseInt(bitisParcalari[0]);

            return saatDegeri >= baslangicDegeri && saatDegeri < bitisDegeri;
        } catch (Exception e) {
            System.out.println("Saat karşılaştırması yapılırken hata: " + e.getMessage());
            return false;
        }
    }
}
