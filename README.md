# Marekoth Cleaner - Android APK (GitHub dağıtım için)

Bu proje senin Firefox eklentisinin direkt Android uygulaması. Play Store'a para vermeden GitHub Releases'tan dağıtabilirsin.

## Nasıl APK alınır (2 yol)

### Yol 1: Bilgisayarda Android Studio ile (en basit)
1. Android Studio'yu aç -> Open -> bu klasörü seç
2. Üstte Build -> Build Bundle(s) / APK(s) -> Build APK(s)
3. app/build/outputs/apk/debug/app-debug.apk oluşur
4. Bunu GitHub'a yükle

### Yol 2: GitHub Actions ile otomatik (önerilen)
1. GitHub'da yeni repo aç: `marekoth-cleaner-apk`
2. Bu klasörün içindekilerin hepsini push et
3. Repo'da Actions sekmesi otomatik build edecek
4. Actions bitince Artifacts'tan APK'yı indir -> Releases'e ekle

Workflow dosyası zaten eklendi: .github/workflows/build.yml

## Kullanıcı nasıl kurar?
APK'yı indiren kişi telefonunda:
Ayarlar -> Bilinmeyen kaynaklara izin ver -> APK'yı kur

Uygulama açılınca TikTok login ekranı gelir, giriş yapar, kendi profilindeki Reposts sekmesine gider, START'a basar.

## Reklam ekleme
app/src/main/res/layout/activity_main.xml içindeki adPlaceholder kısmı reklam alanı.
AdMob eklemek için:
1. AdMob hesabı aç
2. app/build.gradle'da play-services-ads satırını aç
3. MainActivity.kt'de adView kodunu aktif et (yorum satırında)

Temizlik çalışırken reklam otomatik gizleniyor, rahatsız etmiyor.

## Play Store'a geçmek istersen
Aynı proje, sadece app/build.gradle'da signingConfig ekleyip .aab build alman yeterli.