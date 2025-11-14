package org.example.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * Uçuş verilerini çıkar, analiz et ve kaydet
 */
public class UcusVerisiIsleyici {

    private List<UcusBilgisi> ucusListesi;

    public UcusVerisiIsleyici() {
        this.ucusListesi = new ArrayList<>();
    }

    /**
     * Uçuş bilgisi ekle
     */
    public void ucusBilgisiEkle(String kalkisSaati, String varisSaati, String havayolu,
                                double fiyat, String baglanti, double suresi) {
        ucusListesi.add(new UcusBilgisi(kalkisSaati, varisSaati, havayolu, fiyat, baglanti, suresi));
    }

    /**
     * Verileri CSV dosyasına kaydet
     */
    public void csvDosyasiBakal(String dosyaYolu) throws IOException {
        try (FileWriter writer = new FileWriter(dosyaYolu);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("Kalkış Saati", "Varış Saati", "Havayolu Adı",
                                "Fiyat", "Bağlantı Bilgisi", "Uçuş Süresi (saat)"))) {

            for (UcusBilgisi ucus : ucusListesi) {
                csvPrinter.printRecord(
                        ucus.getKalkisSaati(),
                        ucus.getVarisSaati(),
                        ucus.getHavayolu(),
                        ucus.getFiyat(),
                        ucus.getBaglanti(),
                        ucus.getSuresi()
                );
            }
            csvPrinter.flush();
            System.out.println("CSV dosyası başarıyla kaydedildi: " + dosyaYolu);
        }
    }

    /**
     * Havayolu bazında minimum, maksimum ve ortalama fiyatları hesapla
     */
    public Map<String, FiyatIstatistikleri> havayoluBazindaFiyatlariniHesapla() {
        Map<String, List<Double>> havayoluFiyatlari = new HashMap<>();

        // Havayoluna göre grupla
        for (UcusBilgisi ucus : ucusListesi) {
            havayoluFiyatlari.computeIfAbsent(ucus.getHavayolu(), k -> new ArrayList<>())
                    .add(ucus.getFiyat());
        }

        // İstatistikleri hesapla
        Map<String, FiyatIstatistikleri> sonuclar = new LinkedHashMap<>();
        for (Map.Entry<String, List<Double>> entry : havayoluFiyatlari.entrySet()) {
            List<Double> fiyatlar = entry.getValue();
            double minimum = fiyatlar.stream().mapToDouble(Double::doubleValue).min().orElse(0);
            double maksimum = fiyatlar.stream().mapToDouble(Double::doubleValue).max().orElse(0);
            double ortalama = fiyatlar.stream().mapToDouble(Double::doubleValue).average().orElse(0);

            sonuclar.put(entry.getKey(), new FiyatIstatistikleri(minimum, maksimum, ortalama));
        }

        return sonuclar;
    }

    /**
     * En uygun fiyatlı uçuşu bulur
     */
    public UcusBilgisi enUygunUcusuBul() {
        return ucusListesi.stream()
                .min(Comparator.comparingDouble(UcusBilgisi::getFiyat))
                .orElse(null);
    }

    /**
     * Saat dilimine göre uçuşları grupla (ısı haritası için)
     */
    public Map<String, Integer> saatDilimineSaatBazindaGrupla() {
        Map<String, Integer> saatGruplari = new LinkedHashMap<>();

        for (UcusBilgisi ucus : ucusListesi) {
            String saat = ucus.getKalkisSaati().substring(0, 2);
            saatGruplari.put(saat, saatGruplari.getOrDefault(saat, 0) + 1);
        }

        return saatGruplari;
    }

    /**
     * Saat dilimine göre ortalama fiyatları hesapla (ısı haritası için)
     */
    public Map<String, Double> saatDilimineSaatBazindaOrtalamaFiyat() {
        Map<String, List<Double>> saatFiyatlari = new HashMap<>();

        for (UcusBilgisi ucus : ucusListesi) {
            String saat = ucus.getKalkisSaati().substring(0, 2);
            saatFiyatlari.computeIfAbsent(saat, k -> new ArrayList<>())
                    .add(ucus.getFiyat());
        }

        Map<String, Double> ortalamaFiyatlar = new LinkedHashMap<>();
        for (Map.Entry<String, List<Double>> entry : saatFiyatlari.entrySet()) {
            double ortalama = entry.getValue().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0);
            ortalamaFiyatlar.put(entry.getKey(), ortalama);
        }

        return ortalamaFiyatlar;
    }

    /**
     * İstatistikleri ekrana yazdır
     */
    public void istatistikleriYazdir() {
        Map<String, FiyatIstatistikleri> istatistikler = havayoluBazindaFiyatlariniHesapla();

        System.out.println("\n========== HAVAYOLU BAZINDA FİYAT İSTATİSTİKLERİ ==========");
        for (Map.Entry<String, FiyatIstatistikleri> entry : istatistikler.entrySet()) {
            FiyatIstatistikleri stat = entry.getValue();
            System.out.printf("%s: Min: %.2f TL, Maks: %.2f TL, Ortalama: %.2f TL%n",
                    entry.getKey(), stat.getMinimum(), stat.getMaksimum(), stat.getOrtalama());
        }

        UcusBilgisi enUygun = enUygunUcusuBul();
        if (enUygun != null) {
            System.out.printf("\nEn Uygun Uçuş: %s uçak, %s, Fiyat: %.2f TL%n",
                    enUygun.getHavayolu(), enUygun.getKalkisSaati(), enUygun.getFiyat());
        }
    }

    /**
     * Uçuş bilgisi inner class
     */
    public static class UcusBilgisi {
        private String kalkisSaati;
        private String varisSaati;
        private String havayolu;
        private double fiyat;
        private String baglanti;
        private double suresi;

        public UcusBilgisi(String kalkisSaati, String varisSaati, String havayolu,
                          double fiyat, String baglanti, double suresi) {
            this.kalkisSaati = kalkisSaati;
            this.varisSaati = varisSaati;
            this.havayolu = havayolu;
            this.fiyat = fiyat;
            this.baglanti = baglanti;
            this.suresi = suresi;
        }

        // Getter'lar
        public String getKalkisSaati() { return kalkisSaati; }
        public String getVarisSaati() { return varisSaati; }
        public String getHavayolu() { return havayolu; }
        public double getFiyat() { return fiyat; }
        public String getBaglanti() { return baglanti; }
        public double getSuresi() { return suresi; }
    }

    /**
     * Fiyat İstatistikleri inner class
     */
    public static class FiyatIstatistikleri {
        private double minimum;
        private double maksimum;
        private double ortalama;

        public FiyatIstatistikleri(double minimum, double maksimum, double ortalama) {
            this.minimum = minimum;
            this.maksimum = maksimum;
            this.ortalama = ortalama;
        }

        public double getMinimum() { return minimum; }
        public double getMaksimum() { return maksimum; }
        public double getOrtalama() { return ortalama; }
    }
}

