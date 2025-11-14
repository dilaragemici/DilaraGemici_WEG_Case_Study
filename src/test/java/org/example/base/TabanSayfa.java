package org.example.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;
import java.time.Duration;

/**
 * Tüm sayfa nesneleri için temel sınıf
 * Ortak web sürücü işlemleri burada tanımlanır
 */
public class TabanSayfa {

    protected WebDriver surucu;
    protected WebDriverWait bekle;
    private static final int VARSAYILAN_BEKLEME_SURESI = 10;

    public TabanSayfa(WebDriver surucu) {
        this.surucu = surucu;
        this.bekle = new WebDriverWait(surucu, Duration.ofSeconds(VARSAYILAN_BEKLEME_SURESI));
    }

    /**
     * Elemanı bulur ve tıklar
     */
    protected void elemanaTikla(By konum) {
        WebElement eleman = bekle.until(ExpectedConditions.elementToBeClickable(konum));
        eleman.click();
    }

    /**
     * Elemanı bulur ve metin girer
     */
    protected void metinGir(By konum, String metin) {
        WebElement eleman = bekle.until(ExpectedConditions.visibilityOfElementLocated(konum));
        eleman.clear();
        eleman.sendKeys(metin);
    }

    /**
     * Elemanı bulur ve metnini döndürür
     */
    protected String metniAl(By konum) {
        WebElement eleman = bekle.until(ExpectedConditions.visibilityOfElementLocated(konum));
        return eleman.getText();
    }

    /**
     * Açılır listeyi seçer
     */
    protected void acilarListeSecimi(By konum, String deger) {
        WebElement eleman = bekle.until(ExpectedConditions.visibilityOfElementLocated(konum));
        Select secim = new Select(eleman);
        secim.selectByValue(deger);
    }

    /**
     * Açılır listeyi metin ile seçer
     */
    protected void acilarListeSecimiMetin(By konum, String metin) {
        WebElement eleman = bekle.until(ExpectedConditions.visibilityOfElementLocated(konum));
        Select secim = new Select(eleman);
        secim.selectByVisibleText(metin);
    }

    /**
     * Sayfayı belirtilen URL'ye yükler
     */
    protected void sayfayuKle(String url) {
        surucu.get(url);
    }

    /**
     * Elemanın görünür olmasını bekler
     */
    protected WebElement goru(By konum) {
        return bekle.until(ExpectedConditions.visibilityOfElementLocated(konum));
    }

    /**
     * Sayfayı yeniler
     */
    protected void sayfayiYenile() {
        surucu.navigate().refresh();
    }
}

