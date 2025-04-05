# RecipeBook
 Leziz Yemek Tarifleri
Kullanıcının yemek tariflerini saklayabileceği ve eldeki mevcut malzemelerle hangi yemeklerin yapılabileceğini gösteren bir masaüstü uygulaması geliştirilmiştir. 

![image](https://github.com/user-attachments/assets/bc903cb7-0409-4629-be85-ff699045016f)

Fig.1 : Giriş Ekranı
Giriş ekranında yer alan tarifler butonuna tıklanınca tarifler sınıfının çalıştırdığı “tarifler ekranı” gösterilir.

![image](https://github.com/user-attachments/assets/4ca64145-e539-498b-a6a0-e220cf5aa21b)

Fig.2: Tarifler ekranı

Bu ekrandaki filtrele panelinde aranan tarifin kategori, hazırlanma süresi, malzeme çeşidi sayısı ve malzemeler bilgisi alınarak ara butonuna basılır ve sağ taraftaki listede bu kriterlere uyan tarifler listelenir.


![image](https://github.com/user-attachments/assets/7f0841da-76c8-4f7b-b011-83228089688b)

Fig.3: Filtrelenmiş tablo
Yukarıdaki tabloda, 10 adet yumurtamız olduğunda yapabileceğimiz kahvaltı kategorisindeki yemekler listelenmiştir. En sağdaki eşleşme yüzdesi değeri, tarifin filtrelerele uyumunu gösterir. Size maliyeti kısmı ise eksik malzemelerin tutarını ifade eder. 

![image](https://github.com/user-attachments/assets/a90f4c1f-b5d4-4590-b2f3-e7d3246b659c)

Fig.4: İsme göre arama
Burada tablonun üzerindeki arama çubuğuna patates yazılarak patatesli tarifler listelenmiştir. Ayrıca bu tarifler maliyetinin aşağıya doğru artacağı şekilde gösterilmiştir.

![image](https://github.com/user-attachments/assets/9575f392-c3bc-4195-a4e6-3e779b83e016)

Fig.5: Tarif detayları
Ekranın sağ kısmında yer alan bu bölümde seçilen tarifin tarif adı, tarif kategorisi, tarih hazırlanma süresi, malzemeler ve tarih talimatları bilgileri gösterimiştir.

![image](https://github.com/user-attachments/assets/cd6d082b-ee18-475c-bfdf-073165de6f3e)
Fig.6: Buton Paneli
Bu buton panelinde tarif işlemleri için gerekli olan tarifler, malzeme ekle, tarif ekle, tarif düzenle ve tarif sil butonları yer alır. Bu butonlar aşağıdaki panelleri ekranın sağ altındaki panelde gösterir:


![image](https://github.com/user-attachments/assets/14f52493-2db0-4055-926a-20fadfe3397b)
![image](https://github.com/user-attachments/assets/29d14008-7b1c-4c02-8ea6-cc8970620fea)
![image](https://github.com/user-attachments/assets/528dd7bf-87e0-46ae-9277-ab5be79aa497)
![image](https://github.com/user-attachments/assets/9b82bac1-c958-4c36-b537-2b985996bb40)
Fig.7-8-9-10. Tarif işlemleri

![image](https://github.com/user-attachments/assets/56a42958-f499-486a-8f6a-8f1974c385ab)

Veritabanı:
Veritabanı yapısında üç temel tablo bulunmaktadır: Tarifler, Malzemeler ve Tarif-Malzeme İlişkisi. 

Tarifler tablosunda her tarif için benzersiz bir kimlik numarası (TarifID), tarifin adı (TarifAdi), kategorisi (Kategori), dakika cinsinden hazırlanma süresi (HazırlamaSuresi) ve tarifin yapılış talimatlarını içeren bir açıklama (Talimatlar) yer almaktadır. 

Malzemeler tablosunda ise her malzeme için benzersiz bir kimlik numarası (MalzemeID), malzemenin adı (MalzemeAdi), toplam miktarı (ToplamMiktar), ölçü birimi (MalzemeBirim) ve birim fiyatı (BirimFiyat) bilgileri bulunmaktadır. 

Tarif-Malzeme İlişkisi tablosu ise tariflerle malzemeler arasında çoktan çoğa ilişki kurmak için kullanılmaktadır. Bu tabloda, ilgili tarifin kimlik numarası (TarifID) ve ilgili malzemenin kimlik numarası (MalzemeID) dış anahtar (foreign key) olarak yer almakta, ayrıca her bir tarif için gerekli malzeme miktarı (MalzemeMiktar) bilgisi tutulmaktadır. 

Bu yapı sayesinde tariflerin detayları, kullanılan malzemeler ve her bir tarifteki malzeme miktarları düzenli bir şekilde yönetilebilmektedir.

