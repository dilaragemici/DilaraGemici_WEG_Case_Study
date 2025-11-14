# Enuygun.com QA Otomasyon Test Projesi

## 📋 Proje Genel Bakış

Bu proje, Enuygun.com uçuş arama platformu için kapsamlı otomatik test kodu içermektedir. Java + Selenium ile yazılmış, 4 ana test case'i ve teknik gereksinimlerin tüm maddelerine uygun olarak tasarlanmıştır.

## ✅ Kapsamlı Teste Tabi Tutulan Alanlar

### Case 1: Temel Uçuş Arama ve Zaman Filtresi
- **Amaç:** Uçuş arama işlevselliğini ve saat filtrelemesini doğrula
- **Senaryo:**
  - İstanbul → Ankara gidiş-dönüş arama
  - Parametrize kalkış ve dönüş tarihleri
  - 10:00 - 18:00 saat filtresi uygulanır
  - Tüm uçuşların saat aralığı içinde olup olmadığı kontrol edilir
  - Arama sonuçları doğru şehirleri gösterir

### Case 2: Türk Hava Yolları İçin Fiyat Sıralanması
- **Amaç:** Havayolu filtreleme ve fiyat sıralamasını doğrula
- **Senaryo:**
  - İstanbul → Ankara gidiş-dönüş arama
  - 10:00 - 18:00 saat filtresi
  - Türk Hava Yolları seçimi
  - Fiyatlar artan sırada sıralandığını doğrula
  - Tüm sonuçlar Türk Hava Yolları'na ait midir

### Case 3: Kritik Yol Testi
- **Amaç:** En kritik kullanıcı yolculuğunu test et
- **Senaryo:**
  - Sayfa yükleme doğrulaması
  - Parametrize arama kriterleri
  - Filtre uygulaması
  - Sonuçları doğrulama
  - Uçuş seçimi ve detay sayfasına yönlendir

### Case 4: Veri Çıkarma ve Analiz
- **Amaç:** Uçuş verilerini çıkar, analiz et ve görselleştir
- **Senaryo:**
  - İstanbul → Nicosia arasında uçuş arama
  - Verileri çıkar:
    - Kalkış/Varış saatleri
    - Havayolu adı
    - Fiyat
    - Bağlantı bilgisi
    - Uçuş süresi
  - CSV dosyasına kaydet
  - Havayolu bazında min/maks/ortalama fiyat hesapla
  - En uygun uçuşu bulur
  - Saat dilimine göre fiyat dağılımı (ısı haritası) oluştur

## 🛠 Teknoloji Yığını

### Programlama Dili & Framework
- **Java 24**
- **Selenium WebDriver 4.15.0**
- **TestNG 7.8.1**

### Ek Kütüphaneler
- **WebDriverManager 5.6.3** - WebDriver otomatik yönetimi
- **Apache Commons CSV 1.10.0** - CSV dosya işleme
- **Google Gson 2.10.1** - JSON işleme
- **Log4j 2.20.0** - Loglama
- **Extent Reports 5.1.1** - Test raporlaması

## 📁 Proje Yapısı

```
src/
├── main/
│   └── resources/
│       └── log4j2.xml          # Log4j yapılandırması
└── test/
    └── java/org/example/
        ├── base/
        │   └── TabanSayfa.java           # Tüm sayfalar için temel sınıf
        ├── pages/
        │   └── UcusSaramasiSayfasi.java # Page Object Model
        ├── config/
        │   └── Surucu.java              # WebDriver yapılandırması
        ├── utils/
        │   ├── UcusVerisiIsleyici.java  # Veri işleme ve analiz
        │   └── TestRaporlayici.java     # Test raporlaması
        └── tests/
            ├── DortyuzCinis.java            # Case 1
            ├── IkiArmak.java                # Case 2
            ├── UcunuSeç.java                # Case 3
            └── VeriCikarimaVeAnaliz.java   # Case 4

testng.xml                          # Test yürütme yapılandırması
pom.xml                             # Maven yapılandırması
```

## 📋 Uygulanan Teknik Gereksinimler

### ✓ Selenium WebDriver
- Tüm testlerde kullanılır
- Chrome ve Firefox tarayıcıları desteklenir

### ✓ Page Object Model (POM)
- `UcusSaramasiSayfasi` sınıfı POM ilkelerini uygular
- Sayfa öğeleri merkezileştirilmiş olarak tanımlanır

### ✓ OOP Prensipleri
- Kalıtım: `TabanSayfa` temel sınıfından kalıtım
- Encapsulation: private konum tanımları
- Polimorfizm: Farklı test sınıfları

### ✓ TestNG Framework
- @BeforeClass / @AfterClass ek malzemeleri
- @Test anotasyonu
- Assertions (Assert.assertTrue, assertEquals, etc.)

### ✓ Chrome ve Firefox Desteği
- `Surucu.baslat("chrome")` 
- `Surucu.baslat("firefox")`

### ✓ Ekran Görüntüsü ve Raporlama
- `TestRaporlayici` sınıfı başarısız testler için ekran görüntüsü kaydeder
- Test logları raporlanır

### ✓ CSV Dosyası Çıktısı
- `uçuş_verileri.csv` Case 4 tarafından oluşturulur
- Sütunlar: Kalkış Saati, Varış Saati, Havayolu, Fiyat, Bağlantı, Süresi

### ✓ İstatistiksel Analiz
- Minimum, maksimum, ortalama fiyat hesaplaması
- Havayolu bazında analiz
- En uygun uçuş bulma algoritması

### ✓ Isı Haritası Görselleştirmesi
- Saat dilimine göre fiyat dağılımı
- ASCII bar grafikleri

### ✓ Parametrize Test Verileri
- Şehirler parametrize
- Tarihler parametrize
- Filtreler dinamik
- Farklı test senaryoları için tekrarlanabilir

### ✓ Temiz Kod
- Anlaşılır fonksiyon isimleri
- İyi yapılandırılmış sınıflar
- Tekrar kullanılabilir yardımcı metodlar

### ✓ Hata İşleme
- Try-catch blokları
- Anlamlı hata mesajları
- AssertionError ile test başarısızlığı

### ✓ Loglama Mekanizması
- Log4j entegrasyon
- Test adımlarının loglanması

### ✓ Yapılandırma Dosyası
- `testng.xml` test yürütme kontrol
- Maven `pom.xml` yapılandırması

## 🚀 Testleri Çalıştırma

### Ön Koşullar
- Java 24+
- Maven 3.6+
- Chrome/Firefox tarayıcısı

### Adımlar

1. **Bağımlılıkları kur:**
```bash
mvn clean install
```

2. **Testleri çalıştır:**
```bash
mvn test
```

3. **Belirli bir test çalıştır:**
```bash
mvn test -Dtest=DortyuzCinis
```

4. **TestNG ile çalıştır:**
```bash
mvn test -DsuiteXmlFile=testng.xml
```

## 📊 Test Çıktıları

### Başarılı Test Çıktısı
```
[14:23:45] DortyuzCinis: Test başlatılıyor...
[14:23:46] DortyuzCinis: Enuygun.com yükleniyor...
[14:23:47] DortyuzCinis: İstanbul -> Ankara aranıyor
[14:23:48] DortyuzCinis: ✓ TEST BAŞARILI
```

### CSV Çıktısı (Case 4)
```
Kalkış Saati,Varış Saati,Havayolu Adı,Fiyat,Bağlantı Bilgisi,Uçuş Süresi (saat)
10:15,12:00,Türk Hava Yolları,750.0,Direkt,1.5
11:30,13:00,Pegasus,650.0,Direkt,1.5
```

### Isı Haritası (Case 4)
```
10:00 | [████████████░░░░░░░░] | 850.50 TL
11:00 | [██████░░░░░░░░░░░░░░] | 725.00 TL
12:00 | [███████████████░░░░░░] | 950.25 TL
```

## 🐛 Hata Ayıklama

### Ekran Görüntüsü Kontrol
```bash
ls -la screenshots/
```

### Test Logları
```bash
cat logs/test.log
```

### Chrome Options
Headless mod için:
```java
secenekler.addArguments("--headless");
```

## 📝 Açıklamalar

- **Tüm kod Türkçe:** Değişken adları, fonksiyon adları ve açıklamalar Türkçe
- **Real-time siteyi test etmek için:** URL'yi ve XPath'leri siteye göre güncelle
- **Mock Veri:** Case 4'te bazı veriler (varış saati, bağlantı, süre) mock'lanmıştır

## 🎯 Başarı Kriterleri

✅ Case 1: Minimum 3 uçuş bulunur ve saat filtresi doğru uygulanır
✅ Case 2: Türk Hava Yolları uçuşları bulunur ve fiyatlar artan sırada
✅ Case 3: Kritik yol tamamlanır ve tüm doğrulamalar geçer
✅ Case 4: Veri çıkarılır, CSV oluşturulur ve istatistikler hesaplanır

## 📞 İletişim

Proje Sahibi: Dilara Gemici
Repository: https://github.com/dilaragemici/DilaraGemici_WEG_Case_Study

---

**Son Güncelleme:** Kasım 2024

