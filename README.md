# İftarDroid

İftarDroid, Türkiye'deki kullanıcılar için tasarlanmış, güncel namaz vakitlerini ve özellikle İftar (Akşam) / Sahur (İmsak) zamanlarını geri sayım aracı ile sunan bir Android uygulamasıdır.

## Özellikler

*   **Güncel Şehir ve İlçe Verileri:** [Türkiye API](https://turkiyeapi.dev/) kullanılarak il ve ilçeler listelenir.
*   **Doğru Namaz Vakitleri:** [Aladhan API](https://aladhan.com/) (Diyanet İşleri Başkanlığı yöntemi, method 13) üzerinden günlük vakitler alınır.
*   **Dinamik Geri Sayım:** Uygulama açıldığında, o anki saate göre İftar'a (Akşam) veya Sahur'a (İmsak) kalan süreyi saniye saniye hesaplar.
*   **Modern Tasarım:** Material Design 3 bileşenleriyle hazırlanmış şık ve sade tek sayfa (Single Screen) arayüz.
*   **Release Otomasyonu:** GitHub Actions entegrasyonu sayesinde projede kolayca sürüm APK'sı üretilebilir (`workflow_dispatch` üzerinden manuel versiyon girişi ile).

## Teknik Mimari

Proje, güncel Android geliştirme standartlarına uygun olarak inşa edilmiştir:
*   **Dil:** Kotlin
*   **Mimari:** MVVM (Model-View-ViewModel)
*   **Asenkron İşlemler & State:** Coroutines, Flow / StateFlow
*   **Ağ Katmanı:** Retrofit, OkHttp Logging Interceptor, Gson
*   **Arayüz:** ViewBinding, Material Components
*   **Gereksinim:** Min SDK 24, Target SDK 34

## GitHub Workflow (CI/CD)

Projenin kök dizininde bulunan `.github/workflows/release.yml` dosyası sayesinde kodun otomatik olarak derlenip APK sürümünün çıkarılması sağlanmaktadır.

1.  GitHub repozitory'nizde **Actions** sekmesine gidin.
2.  Sol menüden **Release IftarDroid APK** workflow'unu seçin.
3.  **Run workflow** butonuna tıklayın.
4.  Çıkartmak istediğiniz versiyon adını (örneğin: `v1.0.2`) yazıp çalıştırın.
5.  Derleme tamamlandıktan sonra oluşturulan APK dosyası, girilen versiyon etiketiyle otomatik olarak projenizin **Releases** sayfasına eklenecektir.

## Ekran Görüntüsü
*Logo ve ana sayfa bileşenleri Material tasarımlarına uygun olarak yapılandırılmıştır. (Geliştirme aşamasında yer tutucu görseller kullanılmıştır).*

## Lisans

Bu proje, açık kaynaklı ve serbestçe geliştirilebilir bir projedir. Geliştirmeler ve pull requestler için her zaman açıktır.
