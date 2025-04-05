import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class DatabaseFonksiyon {
    private static final String DATABASE_URL = "jdbc:sqlite:tarif_database.db";

    public DatabaseFonksiyon() {
        // JDBC Sürücüsünü yükleme
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        // Veritabanı oluşturma fonksiyonları
        //createDatabase();  // Database oluşturur
        //System.out.println(connectDatabase());  // Database'e bağlanır
        //createTables();  // Tabloları oluşturur

        // Veritabanı veri oluşturma fonksiyonları
        //createMalzeme();  // Malzemeleri oluşturur
        //createTarif();

        // Databaseden veri çekmek için kullanıalcak fonksiyonlar
        //List<String> tarifler = tarifAraAd("Çorba");
        //List<String> malzemelerAd = malzemeAraAd("");
        //List<String> malzemelerAdID = malzemeAraID(49);
        //List<Integer> malzemelerID = malzemeIDAraAd("Ton Balığı");

        List<String> malzemeAdlar = new ArrayList<String>();
        malzemeAdlar.add("Bal");
        malzemeAdlar.add("Kahve");
        malzemeAdlar.add("Fasulye");
        malzemeAdlar.add("Patlıcan");
        //tarifAraMalzeme(malzemeAdlar);
        //tarifBilgiAra("Pide");

        //tarifAraMalzemeSayisi(1,2);
        //tarifAraID(13);
        //System.out.println(tarifAraKategori("Kahvaltı"));
        //System.out.println(tarifAraHazirlamaSure(10));
        float yuzde = malzemeToplamEslesmeYuzdesiHesapla("Zeytinyağlı Taze Fasulye");

    }

    // 1 - Veritabanına bağlanma ve JDBC sürücüsünü yükleme metodu
    // Bağlantıyı return eder
    // Not: JDBC yükleme main içerisinde de olduğundan gereksiz görünse de önlem amaçlı JDBC tekrar yükleniyor
    public static Connection connectDatabase() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:tarif_database.db");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return conn;
    }


    // 2 - Veritabanını oluşturma metodu
    public static void createDatabase() {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            if (conn != null) {
                System.out.println("Yerel veritabanı başarıyla oluşturuldu.");
            }
        } catch (SQLException e) {
            System.out.println("Veritabanı oluşturulurken bir hata oluştu: " + e.getMessage());
        }
    }


    // 3 - Tabloları oluşturma metodu
    public static void createTables() {
        // SQL sorguları
        String createTariflerTable = """
        CREATE TABLE IF NOT EXISTS Tarifler (
            TarifID INTEGER PRIMARY KEY AUTOINCREMENT,
            TarifAdi TEXT NOT NULL,
            Kategori TEXT NOT NULL,
            HazirlamaSuresi INTEGER,
            Talimatlar TEXT
        );
        """;

        String createMalzemelerTable = """
        CREATE TABLE IF NOT EXISTS Malzemeler (
            MalzemeID INTEGER PRIMARY KEY AUTOINCREMENT,
            MalzemeAdi TEXT NOT NULL,
            ToplamMiktar TEXT,
            MalzemeBirim TEXT,
            BirimFiyat REAL
        );
        """;

        String createTarifMalzemeTable = """
        CREATE TABLE IF NOT EXISTS TarifMalzeme (
            TarifID INTEGER,
            MalzemeID INTEGER,
            MalzemeMiktar FLOAT,
            PRIMARY KEY (TarifID, MalzemeID),
            FOREIGN KEY (TarifID) REFERENCES Tarifler(TarifID),
            FOREIGN KEY (MalzemeID) REFERENCES Malzemeler(MalzemeID)
        );
        """;

        // Veritabanı bağlantısını oluştur
        try (Connection conn = connectDatabase();
             Statement stmt = conn != null ? conn.createStatement() : null) {
            if (stmt != null) {
                stmt.execute(createTariflerTable);
                stmt.execute(createMalzemelerTable);
                stmt.execute(createTarifMalzemeTable);
            } else {
                System.out.println("Veritabanına bağlanılamadı, tablolar oluşturulamadı.");
            }
        } catch (SQLException e) {
            System.out.println("Tablolar oluşturulurken bir hata oluştu: " + e.getMessage());
        }
    }

    // 4 - Önceden belirlenmiş malzemeleri oluşturan fonksiyon
    public static void createMalzeme() {
        List<Malzeme> malzemeListesi = new ArrayList<>();  // Malzeme nesneleri için liste
        malzemeListesi.add(new Malzeme(1, "Yumurta", "0", "Adet", 6.0f));
        malzemeListesi.add(new Malzeme(2, "Süt", "0", "Litre", 25.0f));
        malzemeListesi.add(new Malzeme(3, "Un", "0", "Kilogram", 15.0f));
        malzemeListesi.add(new Malzeme(4, "Şeker", "0", "Gram", 18.0f));
        malzemeListesi.add(new Malzeme(5, "Tuz", "0", "Gram", 5.0f));
        malzemeListesi.add(new Malzeme(6, "Zeytinyağı", "0", "Litre", 70.0f));
        malzemeListesi.add(new Malzeme(7, "Pirinç", "0", "Kilogram", 22.0f));
        malzemeListesi.add(new Malzeme(8, "Makarna", "0", "Paket", 12.0f));
        malzemeListesi.add(new Malzeme(9, "Domates", "0", "Kilogram", 10.0f));
        malzemeListesi.add(new Malzeme(10, "Peynir", "0", "Kilogram", 60.0f));
        malzemeListesi.add(new Malzeme(11, "Tereyağı", "0", "Gram", 80.0f));
        malzemeListesi.add(new Malzeme(12, "Çikolata", "0", "Paket", 15.0f));
        malzemeListesi.add(new Malzeme(13, "Salça", "0", "Kilogram", 20.0f));
        malzemeListesi.add(new Malzeme(14, "Biber", "0", "Kilogram", 18.0f));
        malzemeListesi.add(new Malzeme(15, "Soğan", "0", "Kilogram", 12.0f));
        malzemeListesi.add(new Malzeme(16, "Sarımsak", "0", "Gram", 25.0f));
        malzemeListesi.add(new Malzeme(17, "Limon", "0", "Adet", 4.0f));
        malzemeListesi.add(new Malzeme(18, "Patates", "0", "Kilogram", 8.0f));
        malzemeListesi.add(new Malzeme(19, "Havuç", "0", "Kilogram", 7.0f));
        malzemeListesi.add(new Malzeme(20, "Bezelye", "0", "Kilogram", 30.0f));
        malzemeListesi.add(new Malzeme(21, "Mercimek", "0", "Kilogram", 18.0f));
        malzemeListesi.add(new Malzeme(22, "Nohut", "0", "Kilogram", 20.0f));
        malzemeListesi.add(new Malzeme(23, "Fasulye", "0", "Kilogram", 22.0f));
        malzemeListesi.add(new Malzeme(24, "Kekik", "0", "Paket", 8.0f));
        malzemeListesi.add(new Malzeme(25, "Nane", "0", "Paket", 7.0f));
        malzemeListesi.add(new Malzeme(26, "Pul Biber", "0", "Paket", 12.0f));
        malzemeListesi.add(new Malzeme(27, "Karabiber", "0", "Paket", 15.0f));
        malzemeListesi.add(new Malzeme(28, "Kabartma Tozu", "0", "Paket", 5.0f));
        malzemeListesi.add(new Malzeme(29, "Vanilin", "0", "Paket", 3.0f));
        malzemeListesi.add(new Malzeme(30, "Krema", "0", "Paket", 18.0f));
        malzemeListesi.add(new Malzeme(31, "Maydanoz", "0", "Demet", 2.5f));
        malzemeListesi.add(new Malzeme(32, "Dereotu", "0", "Demet", 2.5f));
        malzemeListesi.add(new Malzeme(33, "Su", "0", "Litre", 1.0f));
        malzemeListesi.add(new Malzeme(34, "Soda", "0", "Şişe", 2.0f));
        malzemeListesi.add(new Malzeme(35, "Maden Suyu", "0", "Şişe", 3.0f));
        malzemeListesi.add(new Malzeme(36, "Elma", "0", "Kilogram", 12.0f));
        malzemeListesi.add(new Malzeme(37, "Muz", "0", "Kilogram", 20.0f));
        malzemeListesi.add(new Malzeme(38, "Armut", "0", "Kilogram", 15.0f));
        malzemeListesi.add(new Malzeme(39, "Çilek", "0", "Kilogram", 35.0f));
        malzemeListesi.add(new Malzeme(40, "Kayısı", "0", "Kilogram", 25.0f));
        malzemeListesi.add(new Malzeme(41, "İncir", "0", "Gram", 40.0f));
        malzemeListesi.add(new Malzeme(42, "Hurma", "0", "Gram", 50.0f));
        malzemeListesi.add(new Malzeme(43, "Ceviz", "0", "Gram", 80.0f));
        malzemeListesi.add(new Malzeme(44, "Badem", "0", "Gram", 90.0f));
        malzemeListesi.add(new Malzeme(45, "Fıstık", "0", "Gram", 85.0f));
        malzemeListesi.add(new Malzeme(46, "Susam", "0", "Gram", 28.0f));
        malzemeListesi.add(new Malzeme(47, "Tahin", "0", "Kilogram", 45.0f));
        malzemeListesi.add(new Malzeme(48, "Pekmez", "0", "Kilogram", 32.0f));
        malzemeListesi.add(new Malzeme(49, "Bal", "0", "Kilogram", 100.0f));
        malzemeListesi.add(new Malzeme(50, "Yoğurt", "0", "Kilogram", 20.0f));
        malzemeListesi.add(new Malzeme(51, "Kaymak", "0", "Kilogram", 60.0f));
        malzemeListesi.add(new Malzeme(52, "Sucuk", "0", "Kilogram", 110.0f));
        malzemeListesi.add(new Malzeme(53, "Pastırma", "0", "Kilogram", 150.0f));
        malzemeListesi.add(new Malzeme(54, "Kavurma", "0", "Kilogram", 180.0f));
        malzemeListesi.add(new Malzeme(55, "Kıyma", "0", "Kilogram", 150.0f));
        malzemeListesi.add(new Malzeme(56, "Patlıcan", "0", "Kilogram", 60.0f));
        malzemeListesi.add(new Malzeme(57, "Et", "0", "Kilogram", 200.0f));
        malzemeListesi.add(new Malzeme(58, "Yeşil Biber", "0", "Kilogram", 50.0f));
        malzemeListesi.add(new Malzeme(59, "Ekmek", "0", "Adet", 5.0f));
        malzemeListesi.add(new Malzeme(60, "Avokado", "0", "Adet", 15.0f));
        malzemeListesi.add(new Malzeme(61, "Ayçiçek Yağı", "0", "Litre", 30.0f));
        malzemeListesi.add(new Malzeme(62, "Kraker", "0", "Paket", 10.0f));
        malzemeListesi.add(new Malzeme(63, "Humus", "0", "Gram", 100.0f));
        malzemeListesi.add(new Malzeme(64, "Yufka", "0", "Adet", 8.0f));
        malzemeListesi.add(new Malzeme(65, "Kabak", "0", "Kilogram", 40.0f));
        malzemeListesi.add(new Malzeme(66, "Tavuk", "0", "Kilogram", 80.0f));
        malzemeListesi.add(new Malzeme(67, "Tarhana", "0", "Kilogram", 25.0f));
        malzemeListesi.add(new Malzeme(68, "Biber Salçası", "0", "Kilogram", 35.0f));
        malzemeListesi.add(new Malzeme(69, "Salatalık", "0", "Kilogram", 20.0f));
        malzemeListesi.add(new Malzeme(70, "Marul", "0", "Adet", 10.0f));
        malzemeListesi.add(new Malzeme(71, "Kruton", "0", "Gram", 15.0f));
        malzemeListesi.add(new Malzeme(72, "Bulgur", "0", "Kilogram", 12.0f));
        malzemeListesi.add(new Malzeme(73, "Ton Balığı", "0", "Konserve", 18.0f));
        malzemeListesi.add(new Malzeme(74, "Vanilya", "0", "Paket", 5.0f));
        malzemeListesi.add(new Malzeme(75, "İrmik", "0", "Kilogram", 20.0f));
        malzemeListesi.add(new Malzeme(76, "Kadayıf", "0", "Kilogram", 45.0f));
        malzemeListesi.add(new Malzeme(77, "Nişasta", "0", "Kilogram", 22.0f));
        malzemeListesi.add(new Malzeme(78, "Çay", "0", "Gram", 50.0f));
        malzemeListesi.add(new Malzeme(79, "Kahve", "0", "Gram", 60.0f));
        malzemeListesi.add(new Malzeme(80, "Meyve", "0", "Kilogram", 25.0f));


        try (Connection conn = connectDatabase()) {
            if (conn != null) {
                // Listedeki her bir malzeme için bilgileri veritabanına ekleme
                for (Malzeme malzeme : malzemeListesi) {
                    insertMalzeme(malzeme);
                }
            }
        } catch (SQLException e) {
            System.out.println("Veritabanına bağlanırken hata oluştu: " + e.getMessage());
        }
    }

    // 5 - Tabloya Malzeme eklemeyi sağlayan fonksiyon
    public static boolean insertMalzeme(Malzeme malzeme) {
        try (Connection conn = connectDatabase()) {
            if (conn != null) {
                //System.out.println("Veritabanına başarıyla bağlanıldı!");

                // SQL insert komutu
                String sqlInsert = "INSERT INTO Malzemeler (MalzemeAdi, ToplamMiktar, MalzemeBirim, BirimFiyat) VALUES (?, ?, ?, ?)";
                String sqlCheckExistence = "SELECT COUNT(*) FROM Malzemeler WHERE MalzemeAdi = ?";

                try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert);
                     PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheckExistence)) {

                    // Önce malzemenin veritabanında olup olmadığını kontrol etme
                    pstmtCheck.setString(1, malzeme.getMalzemeAdi());
                    ResultSet rs = pstmtCheck.executeQuery();
                    rs.next();

                    int count = rs.getInt(1);
                    if (count == 0) { // Eğer malzeme yoksa, ekle
                        pstmtInsert.setString(1, malzeme.getMalzemeAdi());
                        pstmtInsert.setString(2, malzeme.getToplamMiktar());
                        pstmtInsert.setString(3, malzeme.getMalzemeBirim());
                        pstmtInsert.setFloat(4, malzeme.getBirimFiyat());
                        pstmtInsert.executeUpdate();
                        System.out.println(malzeme.getMalzemeAdi() + " başarıyla eklendi.");
                        return true;
                    } else {
                        System.out.println(malzeme.getMalzemeAdi() + " zaten veritabanında mevcut, eklenmedi.");
                    }
                } catch (SQLException e) {
                    System.out.println("Veritabanı işlemi sırasında hata oluştu: " + e.getMessage());
                } catch (Exception hata) {
                    System.out.println("Hata: " + hata);
                }
            }
        } catch (SQLException e) {
            System.out.println("Veritabanına bağlanırken hata oluştu: " + e.getMessage());
        } catch (Exception hata) {
            System.out.println("Hata: " + hata);
        }
        return false;
    }


    // 6 - Önceden hazırlanmış tarifleri oluşturan fonksiyon
    public static void createTarif() {
        // Her tarif için malzeme ve miktarlarını içeren bir Map yapısı
        Map<Integer, List<List<Object>>> tarifMalzemeleri = new HashMap<>();
        List<Tarif> tarifListesi = new ArrayList<>();  // Tarif nesneleri için liste

        // Ana Yemek Tarifleri
        tarifListesi.add(new Tarif(1, "Zeytinyağlı Taze Fasulye", "Ana Yemek", 45,
                "1. Taze fasulyeleri ayıklayıp ikiye bölün ve yıkayın. " +
                        "2. Tencereye zeytinyağı ekleyin ve soğanları kavurun. " +
                        "3. Fasulyeleri, domatesleri ve sarımsağı ekleyip karıştırın. " +
                        "4. Tuz ve şeker ekleyin, üzerini örtecek kadar su koyup pişirin. " +
                        "5. Sıcak veya soğuk olarak servis edin."));
        tarifMalzemeleri.put(1, List.of(List.of("Fasulye", "Zeytinyağı", "Soğan", "Domates", "Sarımsak", "Su", "Şeker", "Tuz"),
                List.of(10f, 0.5f, 3f, 2f, 50f, 0.6f, 20f, 10f)));

        tarifListesi.add(new Tarif(2, "Karnıyarık", "Ana Yemek", 60,
                "1. Patlıcanları ortadan kesin ve kızartın. " +
                        "2. İç harcı için soğan, kıyma ve baharatları kavurun. " +
                        "3. Harcı patlıcanların içine doldurup fırında pişirin."));
        tarifMalzemeleri.put(2, List.of(List.of("Patlıcan", "Kıyma", "Soğan", "Domates", "Biber", "Tuz", "Zeytinyağı"),
                List.of(4f, 200f, 2f, 1f, 2f, 5f, 0.5f)));

        tarifListesi.add(new Tarif(3, "Mantı", "Ana Yemek", 90,
                "1. Hamuru açın ve küçük kareler halinde kesin. " +
                        "2. İç harcı koyup kapatın ve kaynar suda haşlayın. " +
                        "3. Sarımsaklı yoğurt ve sos ile servis edin."));
        tarifMalzemeleri.put(3, List.of(List.of("Un", "Kıyma", "Soğan", "Yoğurt", "Tuz", "Su"),
                List.of(300f, 200f, 1f, 200f, 10f, 100f)));

        tarifListesi.add(new Tarif(4, "İmam Bayıldı", "Ana Yemek", 50,
                "1. Patlıcanları kızartın ve içini doldurun. " +
                        "2. Soğan, domates, sarımsak ve baharatlı harcı ekleyin."));
        tarifMalzemeleri.put(4, List.of(List.of("Patlıcan", "Soğan", "Domates", "Sarımsak", "Zeytinyağı", "Tuz"),
                List.of(3f, 2f, 2f, 2f, 0.3f, 5f)));

        tarifListesi.add(new Tarif(5, "Et Sote", "Ana Yemek", 40,
                "1. Eti doğrayıp kavurun. " +
                        "2. Soğan, biber ve baharatları ekleyip pişirin."));
        tarifMalzemeleri.put(5, List.of(List.of("Et", "Soğan", "Biber", "Domates", "Zeytinyağı", "Tuz"),
                List.of(300f, 1f, 2f, 1f, 0.2f, 5f)));

        // Kahvaltı Tarifleri
        tarifListesi.add(new Tarif(6, "Menemen", "Kahvaltı", 20,
                "1. Domatesleri doğrayın ve tencereye alın. " +
                        "2. Biberleri ekleyip soteleyin. " +
                        "3. Yumurtaları kırıp karıştırın ve pişirin."));
        tarifMalzemeleri.put(6, List.of(List.of("Domates", "Yeşil Biber", "Yumurta", "Zeytinyağı", "Tuz"),
                List.of(4f, 2f, 3f, 0.3f, 5f)));

        tarifListesi.add(new Tarif(7, "Peynirli Omlet", "Kahvaltı", 15,
                "1. Yumurtaları çırpın ve tavaya dökün. " +
                        "2. Peynir ekleyip pişirin ve sıcak servis yapın."));
        tarifMalzemeleri.put(7, List.of(List.of("Yumurta", "Peynir", "Tereyağı", "Tuz"),
                List.of(2f, 50f, 0.2f, 5f)));

        tarifListesi.add(new Tarif(8, "Krep", "Kahvaltı", 25,
                "1. Hamur malzemelerini karıştırın ve tavada ince ince pişirin."));
        tarifMalzemeleri.put(8, List.of(List.of("Un", "Süt", "Yumurta", "Tereyağı", "Tuz"),
                List.of(200f, 300f, 2f, 0.1f, 2f)));

        tarifListesi.add(new Tarif(9, "Simit", "Kahvaltı", 60,
                "1. Hamuru yoğurun ve halka şekli verin. " +
                        "2. Üzerine susam serpip fırında pişirin."));
        tarifMalzemeleri.put(9, List.of(List.of("Un", "Susam", "Zeytinyağı", "Su", "Tuz"),
                List.of(250f, 30f, 0.2f, 150f, 5f)));

        tarifListesi.add(new Tarif(10, "Avokado Tost", "Kahvaltı", 10,
                "1. Ekmeği kızartın. " +
                        "2. Avokadoyu ezip ekmeğin üzerine sürün."));
        tarifMalzemeleri.put(10, List.of(List.of("Ekmek", "Avokado", "Zeytinyağı", "Tuz", "Limon"),
                List.of(2f, 1f, 0.1f, 3f, 0.5f)));

        // Atıştırmalık Tarifleri
        tarifListesi.add(new Tarif(11, "Patates Cipsi", "Atıştırmalık", 15,
                "1. Patatesleri ince dilimler halinde kesin. " +
                        "2. Kızgın yağda kızartın ve üzerine tuz serpin."));
        tarifMalzemeleri.put(11, List.of(List.of("Patates", "Tuz", "Ayçiçek Yağı"),
                List.of(2f, 5f, 0.5f)));

        tarifListesi.add(new Tarif(12, "Humuslu Kraker", "Atıştırmalık", 20,
                "1. Krakerlerin üzerine humus sürün ve servis yapın."));
        tarifMalzemeleri.put(12, List.of(List.of("Kraker", "Humus"),
                List.of(10f, 50f)));

        tarifListesi.add(new Tarif(13, "Peynirli Börek", "Atıştırmalık", 30,
                "1. Yufkayı açın ve peynirle doldurun. " +
                        "2. Rulo yaparak fırında pişirin."));
        tarifMalzemeleri.put(13, List.of(List.of("Yufka", "Peynir", "Zeytinyağı"),
                List.of(3f, 200f, 0.2f)));

        tarifListesi.add(new Tarif(14, "Çıtır Sebzeler", "Atıştırmalık", 25,
                "1. Sebzeleri ince doğrayıp fırında çıtırlaşana kadar pişirin."));
        tarifMalzemeleri.put(14, List.of(List.of("Havuç", "Kabak", "Tuz", "Zeytinyağı"),
                List.of(1f, 1f, 3f, 0.1f)));

        tarifListesi.add(new Tarif(15, "Mini Sandviç", "Atıştırmalık", 10,
                "1. Küçük ekmeklerin arasına peynir ve domates koyup servis edin."));
        tarifMalzemeleri.put(15, List.of(List.of("Ekmek", "Peynir", "Domates", "Maydanoz"),
                List.of(4f, 50f, 1f, 0.1f)));

        // Çorba Tarifleri
        tarifListesi.add(new Tarif(16, "Mercimek Çorbası", "Çorba", 40,
                "1. Mercimekleri tencerede haşlayın ve blenderdan geçirin."));
        tarifMalzemeleri.put(16, List.of(List.of("Mercimek", "Soğan", "Tuz", "Zeytinyağı", "Su"),
                List.of(200f, 1f, 5f, 0.1f, 1f)));

        tarifListesi.add(new Tarif(17, "Domates Çorbası", "Çorba", 30,
                "1. Domatesleri rendeleyin ve suyla kaynatın."));
        tarifMalzemeleri.put(17, List.of(List.of("Domates", "Tuz", "Zeytinyağı", "Su"),
                List.of(5f, 5f, 0.1f, 1f)));

        tarifListesi.add(new Tarif(18, "Tavuk Suyu Çorbası", "Çorba", 60,
                "1. Tavukları haşlayıp suyunu kullanarak çorba yapın."));
        tarifMalzemeleri.put(18, List.of(List.of("Tavuk", "Havuç", "Soğan", "Tuz"),
                List.of(300f, 1f, 1f, 5f)));

        tarifListesi.add(new Tarif(19, "Sebze Çorbası", "Çorba", 35,
                "1. Sebzeleri doğrayın ve suda haşlayarak pişirin."));
        tarifMalzemeleri.put(19, List.of(List.of("Havuç", "Patates", "Kabak", "Tuz", "Su"),
                List.of(1f, 1f, 1f, 5f, 1f)));

        tarifListesi.add(new Tarif(20, "Tarhana Çorbası", "Çorba", 25,
                "1. Tarhanayı suyla açıp kaynatın ve pişirin."));
        tarifMalzemeleri.put(20, List.of(List.of("Tarhana", "Su", "Tuz"),
                List.of(100f, 1f, 5f)));

        // Meze Tarifleri
        tarifListesi.add(new Tarif(21, "Acılı Ezme", "Meze", 15,
                "1. Tüm malzemeleri doğrayın ve karıştırın."));
        tarifMalzemeleri.put(21, List.of(List.of("Domates", "Biber", "Soğan", "Zeytinyağı", "Tuz"),
                List.of(2f, 1f, 1f, 0.2f, 5f)));

        tarifListesi.add(new Tarif(22, "Haydari", "Meze", 10,
                "1. Yoğurt ve ezilmiş sarımsağı karıştırın."));
        tarifMalzemeleri.put(22, List.of(List.of("Yoğurt", "Sarımsak", "Tuz"),
                List.of(200f, 1f, 5f)));

        tarifListesi.add(new Tarif(23, "Muhammara", "Meze", 20,
                "1. Ceviz ve baharatları karıştırarak hazırlayın."));
        tarifMalzemeleri.put(23, List.of(List.of("Ceviz", "Biber Salçası", "Zeytinyağı", "Tuz"),
                List.of(50f, 30f, 0.2f, 5f)));

        tarifListesi.add(new Tarif(24, "Humus", "Meze", 25,
                "1. Nohutları ezip tahin ve limon suyu ekleyin."));
        tarifMalzemeleri.put(24, List.of(List.of("Nohut", "Tahin", "Limon", "Zeytinyağı", "Tuz"),
                List.of(150f, 50f, 0.5f, 0.2f, 5f)));

        tarifListesi.add(new Tarif(25, "Babagannuş", "Meze", 30,
                "1. Patlıcanları közleyip diğer malzemelerle karıştırın."));
        tarifMalzemeleri.put(25, List.of(List.of("Patlıcan", "Domates", "Biber", "Zeytinyağı", "Tuz"),
                List.of(3f, 1f, 1f, 0.2f, 5f)));

        // Hamur İşi Tarifleri
        tarifListesi.add(new Tarif(26, "Poğaça", "Hamur İşi", 45,
                "1. Hamuru yoğurun ve iç malzeme koyup fırında pişirin."));
        tarifMalzemeleri.put(26, List.of(List.of("Un", "Peynir", "Zeytinyağı", "Su", "Tuz"),
                List.of(250f, 100f, 0.1f, 100f, 5f)));

        tarifListesi.add(new Tarif(27, "Börek", "Hamur İşi", 60,
                "1. Yufkaları serip iç malzeme ile doldurup sarın ve pişirin."));
        tarifMalzemeleri.put(27, List.of(List.of("Yufka", "Peynir", "Maydanoz", "Zeytinyağı", "Tuz"),
                List.of(5f, 200f, 0.1f, 0.2f, 5f)));

        tarifListesi.add(new Tarif(28, "Lahmacun", "Hamur İşi", 50,
                "1. Hamuru açıp kıymalı harçla kaplayın ve fırında pişirin."));
        tarifMalzemeleri.put(28, List.of(List.of("Un", "Kıyma", "Domates", "Biber", "Soğan", "Tuz"),
                List.of(300f, 200f, 1f, 1f, 1f, 5f)));

        tarifListesi.add(new Tarif(29, "Gözleme", "Hamur İşi", 40,
                "1. Hamuru açın ve peynir veya kıymayla doldurup tavada pişirin."));
        tarifMalzemeleri.put(29, List.of(List.of("Un", "Peynir", "Zeytinyağı", "Tuz"),
                List.of(200f, 100f, 0.1f, 5f)));

        tarifListesi.add(new Tarif(30, "Pide", "Hamur İşi", 55,
                "1. Hamuru açın ve peynir veya kıymalı harçla kaplayıp fırında pişirin."));
        tarifMalzemeleri.put(30, List.of(List.of("Un", "Peynir", "Kıyma", "Domates", "Tuz"),
                List.of(300f, 100f, 200f, 1f, 5f)));

        // Salata Tarifleri
        tarifListesi.add(new Tarif(31, "Çoban Salata", "Salata", 15,
                "1. Tüm malzemeleri doğrayıp karıştırın."));
        tarifMalzemeleri.put(31, List.of(List.of("Domates", "Salatalık", "Soğan", "Biber", "Zeytinyağı", "Tuz"),
                List.of(2f, 1f, 1f, 1f, 0.1f, 5f)));

        tarifListesi.add(new Tarif(32, "Sezar Salata", "Salata", 20,
                "1. Tavukları pişirip diğer malzemelerle karıştırın."));
        tarifMalzemeleri.put(32, List.of(List.of("Marul", "Tavuk", "Kruton", "Peynir", "Zeytinyağı", "Tuz"),
                List.of(100f, 150f, 50f, 30f, 0.1f, 5f)));

        tarifListesi.add(new Tarif(33, "Bulgur Salatası", "Salata", 25,
                "1. Bulguru haşlayıp diğer malzemelerle karıştırın."));
        tarifMalzemeleri.put(33, List.of(List.of("Bulgur", "Domates", "Salatalık", "Biber", "Zeytinyağı", "Tuz"),
                List.of(150f, 1f, 1f, 1f, 0.1f, 5f)));

        tarifListesi.add(new Tarif(34, "Patates Salatası", "Salata", 30,
                "1. Patatesleri haşlayıp doğrayarak diğer malzemelerle karıştırın."));
        tarifMalzemeleri.put(34, List.of(List.of("Patates", "Soğan", "Maydanoz", "Zeytinyağı", "Tuz"),
                List.of(3f, 1f, 0.1f, 0.1f, 5f)));

        tarifListesi.add(new Tarif(35, "Ton Balıklı Salata", "Salata", 20,
                "1. Ton balığını diğer malzemelerle karıştırarak servis edin."));
        tarifMalzemeleri.put(35, List.of(List.of("Ton Balığı", "Marul", "Domates", "Zeytinyağı", "Tuz"),
                List.of(150f, 100f, 1f, 0.1f, 5f)));

        // Tatlı Tarifleri
        tarifListesi.add(new Tarif(36, "Baklava", "Tatlı", 120,
                "1. Yufkaları ince açıp cevizle doldurup şerbetle tatlandırın."));
        tarifMalzemeleri.put(36, List.of(List.of("Un", "Ceviz", "Şeker", "Tereyağı"),
                List.of(500f, 200f, 300f, 150f)));

        tarifListesi.add(new Tarif(37, "Sütlaç", "Tatlı", 60,
                "1. Pirinçleri haşlayıp sütle pişirin ve şeker ekleyin."));
        tarifMalzemeleri.put(37, List.of(List.of("Pirinç", "Süt", "Şeker", "Vanilya"),
                List.of(100f, 1f, 100f, 2f)));

        tarifListesi.add(new Tarif(38, "Revani", "Tatlı", 50,
                "1. İrmik ve yoğurtla hamur yapıp fırında pişirin."));
        tarifMalzemeleri.put(38, List.of(List.of("İrmik", "Yoğurt", "Şeker", "Un"),
                List.of(150f, 100f, 200f, 100f)));

        tarifListesi.add(new Tarif(39, "Kadayıf", "Tatlı", 40,
                "1. Kadayıfı tereyağı ile kızartıp şerbetle tatlandırın."));
        tarifMalzemeleri.put(39, List.of(List.of("Kadayıf", "Tereyağı", "Şeker", "Ceviz"),
                List.of(200f, 100f, 300f, 100f)));

        tarifListesi.add(new Tarif(40, "Muhallebi", "Tatlı", 30,
                "1. Süt ve nişastayı kaynatıp muhallebi kıvamına getirin."));
        tarifMalzemeleri.put(40, List.of(List.of("Süt", "Nişasta", "Şeker", "Vanilya"),
                List.of(1f, 50f, 100f, 2f)));

        // İçecek Tarifleri
        tarifListesi.add(new Tarif(41, "Limonata", "İçecek", 15,
                "1. Limon suyu, şeker ve suyu karıştırarak soğuk servis yapın."));
        tarifMalzemeleri.put(41, List.of(List.of("Limon", "Şeker", "Su"),
                List.of(3f, 100f, 1f)));

        tarifListesi.add(new Tarif(42, "Çay", "İçecek", 10,
                "1. Çay yapraklarını demleyin ve sıcak su ekleyin."));
        tarifMalzemeleri.put(42, List.of(List.of("Çay", "Su"),
                List.of(5f, 1f)));

        tarifListesi.add(new Tarif(43, "Ayran", "İçecek", 5,
                "1. Yoğurt ve suyu karıştırıp tuz ekleyin."));
        tarifMalzemeleri.put(43, List.of(List.of("Yoğurt", "Su", "Tuz"),
                List.of(200f, 1f, 5f)));

        tarifListesi.add(new Tarif(44, "Kahve", "İçecek", 10,
                "1. Kahve ve suyu karıştırıp kaynatın."));
        tarifMalzemeleri.put(44, List.of(List.of("Kahve", "Su"),
                List.of(10f, 1f)));

        tarifListesi.add(new Tarif(45, "Meyve Suyu", "İçecek", 5,
                "1. Meyve suyunu taze sıkıp servis edin."));
        tarifMalzemeleri.put(45, List.of(List.of("Meyve"),
                List.of(300f)));


        try (Connection conn = connectDatabase()) {
            if (conn != null) {
                // Listedeki her bir tarif için bilgileri veritabanına ekleme
                for (Tarif tarif : tarifListesi) {
                    List<List<Object>> malzemeBilgileri = tarifMalzemeleri.get(tarif.getTarifID());
                    if (malzemeBilgileri != null) {
                        ArrayList<String> malzemeler = new ArrayList(malzemeBilgileri.get(0));
                        ArrayList<Float> miktarlar = new ArrayList(malzemeBilgileri.get(1));
                        insertTarif(tarif, malzemeler, miktarlar);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Veritabanına bağlanırken hata oluştu: " + e.getMessage());
        }
    }

    // 7 - Tabloya tarif eklemeyi sağlayan fonksiyon
    public static boolean insertTarif(Tarif tarif, ArrayList<String> malzemeler, ArrayList<Float> malzeme_miktar) {

        try (Connection conn = connectDatabase()) {
            if (conn != null) {
                System.out.println("Veritabanına başarıyla bağlanıldı!");

                // SQL insert komutu
                String sqlInsert = "INSERT INTO Tarifler (TarifAdi, Kategori, HazirlamaSuresi, Talimatlar) VALUES (?, ?, ?, ?)";
                String sqlCheckExistence = "SELECT COUNT(*) FROM Tarifler WHERE TarifAdi = ?";

                try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert);
                     PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheckExistence)) {
                    // Önce tarifin veritabanında olup olmadığının kontrolü
                    pstmtCheck.setString(1, tarif.getTarifAdi());
                    ResultSet result = pstmtCheck.executeQuery();
                    result.next();

                    int count = result.getInt(1);
                    if (count == 0) { // Eğer tarif yoksa, ekle
                        pstmtInsert.setString(1, tarif.getTarifAdi());
                        pstmtInsert.setString(1, tarif.getTarifAdi());
                        pstmtInsert.setString(2, tarif.getKategori());
                        pstmtInsert.setFloat(3, tarif.getHazirlamaSuresi());
                        pstmtInsert.setString(4, tarif.getTalimatlar());
                        pstmtInsert.executeUpdate();

                        // Tarif-Malzeme tablosuna ekleme kısmı
                        String sqlGetMalzemeID = "SELECT MalzemeID FROM Malzemeler WHERE MalzemeAdi = ?";
                        String sqlInsertTarifMalzeme = "INSERT INTO TarifMalzeme (TarifID, MalzemeID, MalzemeMiktar) VALUES (?, ?, ?)";

                        try (PreparedStatement pstmtGetMalzemeID = conn.prepareStatement(sqlGetMalzemeID);
                             PreparedStatement pstmtInsertTarifMalzeme = conn.prepareStatement(sqlInsertTarifMalzeme)) {

                            System.out.println(malzemeler.size());
                            for (int i = 0; i < malzemeler.size(); i++) {
                                String malzemeAd = malzemeler.get(i);
                                Float malzemeMiktar = malzeme_miktar.get(i);

                                System.out.println(malzemeAd);
                                System.out.println(malzemeMiktar);

                                // MalzemeID'yi almak için sorgu
                                pstmtGetMalzemeID.setString(1, malzemeAd);
                                ResultSet rsMalzemeID = pstmtGetMalzemeID.executeQuery();

                                if (rsMalzemeID.next()) {
                                    int malzemeID = rsMalzemeID.getInt("MalzemeID");
                                    int tarifID = tarifIDAraIsim(tarif.getTarifAdi());
                                    // Tarif-Malzeme tablosuna ekleme
                                    pstmtInsertTarifMalzeme.setInt(1, tarifID);
                                    pstmtInsertTarifMalzeme.setInt(2, malzemeID);
                                    pstmtInsertTarifMalzeme.setFloat(3, malzemeMiktar);
                                    pstmtInsertTarifMalzeme.executeUpdate();
                                    System.out.println(tarif.getTarifAdi() + " başarıyla eklendi.");
                                } else {
                                    System.out.println("Malzeme bulunamadı: " + malzemeAd);
                                }
                            }
                            return true;
                        } catch (SQLException e) {
                            System.out.println("Malzeme eklenirken hata oluştu: " + e.getMessage());
                        }

                    } else {
                        System.out.println(tarif.getTarifAdi() + " zaten veritabanında mevcut, eklenmedi.");
                    }
                } catch (SQLException e) {
                    System.out.println("Veritabanı işlemi sırasında hata oluştu: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Veritabanına bağlanırken hata oluştu: " + e.getMessage());
        }
        return false;
    }

    // 8 - Tarif aratma - isim ile
    public static List<String> tarifAraAd(String aranacakKelime) {
        List<String> bulunanTarifler = new ArrayList<>();

        String sql = "SELECT TarifAdi FROM tarifler WHERE TarifAdi LIKE ?";

        try (Connection conn = connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Aranacak kelimeyi ayarlama
            pstmt.setString(1, "%" + aranacakKelime + "%");

            ResultSet rs = pstmt.executeQuery();

            // Eşleşen tarifleri listeye ekleme
            while (rs.next()) {
                bulunanTarifler.add(rs.getString("TarifAdi"));
            }

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return bulunanTarifler;
    }

    // 8.1 - Tarif Bilgisi aratma - isim ile
    public static List<String> tarifBilgiAra(String aranacakKelime) {
        List<String> bulunanTarif = new ArrayList<>();
        String sql = "SELECT * FROM Tarifler WHERE TarifAdi = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Aranacak kelimeyi ayarlama
            pstmt.setString(1, aranacakKelime);

            ResultSet rs = pstmt.executeQuery();

            // Eşleşen tarifleri listeye ekleme
            while (rs.next()) {
                bulunanTarif.add(Integer.toString(rs.getInt("TarifID")));
                bulunanTarif.add(rs.getString("TarifAdi"));
                bulunanTarif.add(rs.getString("Kategori"));
                bulunanTarif.add(Integer.toString(rs.getInt("HazirlamaSuresi")));
                bulunanTarif.add(rs.getString("Talimatlar"));
            }

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return bulunanTarif;
    }

    // 9 - Malzeme aratma - isim ile
    public static List<String> malzemeAraAd(String aranacakKelime) {
        List<String> bulunanMalzemeler = new ArrayList<>();

        String sql = "SELECT MalzemeAdi FROM malzemeler WHERE MalzemeAdi LIKE ?";

        try (Connection conn = connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Aranacak kelimeyi ayarlama
            pstmt.setString(1, "%" + aranacakKelime + "%");

            ResultSet rs = pstmt.executeQuery();

            // Eşleşen malzemeleri listeye ekleme
            while (rs.next()) {
                bulunanMalzemeler.add(rs.getString("MalzemeAdi"));
            }

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return bulunanMalzemeler;
    }

    // 10 - Malzeme aratma - ID ile
    public static List<String> malzemeAraID(Integer malzemeID) {
        List<String> bulunanMalzemeler = new ArrayList<>();

        String sql = "SELECT MalzemeAdi FROM malzemeler WHERE MalzemeID = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Aranacak kelimeyi ayarlama
            pstmt.setInt(1, malzemeID);

            ResultSet rs = pstmt.executeQuery();

            // Eşleşen malzemeleri listeye ekleme
            while (rs.next()) {
                bulunanMalzemeler.add(rs.getString("MalzemeAdi"));
            }

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return bulunanMalzemeler;
    }

    // 11 - Malzeme ID aratma - isim ile
    public static List<Integer> malzemeIDAraAd(String aranacakKelime) {
        List<Integer> bulunanMalzemeler = new ArrayList<>();

        String sql = "SELECT MalzemeID FROM malzemeler WHERE MalzemeAdi = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Aranacak kelimeyi ayarlama
            pstmt.setString(1, aranacakKelime);

            ResultSet rs = pstmt.executeQuery();

            // Eşleşen malzemeleri listeye ekleme
            while (rs.next()) {
                bulunanMalzemeler.add(rs.getInt("MalzemeID"));
            }

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return bulunanMalzemeler;
    }

    // 12 - Malzeme kullanarak tarif aratma
    public static List<String> tarifAraMalzeme(List<String> malzemeler) {
        List<String> bulunanTarifler = new ArrayList<>();
        List<Integer> malzemeIDler = new ArrayList<>();
        Set<Integer> tarifIDSet = new HashSet<>(); // Tekrarlı tarifleri önlemek için set kullanılacak set45623456

        String tarifAdsql = "SELECT TarifAdi FROM tarifler WHERE TarifID = ?";
        String tarifMalzemelerIDsql = "SELECT TarifID FROM TarifMalzeme WHERE MalzemeID = ?";

        try (Connection conn = connectDatabase()) {

            // Eşleşen malzemeleri listeye ekleme
            for (int i = 0; i < malzemeler.size(); i++) {
                if (!malzemeIDAraAd(malzemeler.get(i)).isEmpty()) {
                    // Her malzemenin malzemeID'si listeye ekleniyor
                    malzemeIDler.add(malzemeIDAraAd(malzemeler.get(i)).get(0));
                }
            }

            // malzemeIDler listesinde MalzemeID'lerini kullanarak TarifMalzeme tablosundan bu MalzemeID'ye sahip olan
            // bütün satırlardaki TarifID'yi çekecek kısım
            for (Integer malzemeID : malzemeIDler) {
                try (PreparedStatement pstmt = conn.prepareStatement(tarifMalzemelerIDsql)) {
                    pstmt.setInt(1, malzemeID);
                    ResultSet rs = pstmt.executeQuery();

                    // Tarif ID'leri tarifIDSet'e ekleniyor
                    while (rs.next()) {
                        tarifIDSet.add(rs.getInt("TarifID"));
                    }
                }
            }

            // Bulunan tarif ID'leri kullanarak tarif adlarını listeye ekleme
            for (Integer tarifID : tarifIDSet) {
                try (PreparedStatement pstmt = conn.prepareStatement(tarifAdsql)) {
                    pstmt.setInt(1, tarifID);
                    ResultSet rs = pstmt.executeQuery();

                    // Tarif adlarını bulunanTarifler listesine ekleme
                    while (rs.next()) {
                        bulunanTarifler.add(rs.getString("TarifAdi"));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return bulunanTarifler;
    }

    // 13 - Malzeme min max sayısını kullanarak tarif aratma
    public static List<String> tarifAraMalzemeSayisi(Integer malzemeSayisiMin, Integer malzemeSayisiMax) {
        List<String> bulunanTarifler = new ArrayList<>();
        String tarifMalzemelerCountSql = "SELECT TarifID, COUNT(MalzemeID) AS MalzemeSayisi FROM TarifMalzeme GROUP BY TarifID";

        // TarifMalzeme tablosunda malzeme sayısını hesaplayıp TarifID'leri bulma
        try (Connection conn = connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(tarifMalzemelerCountSql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int tarifID = rs.getInt("TarifID");
                int malzemeSayisi = rs.getInt("MalzemeSayisi");

                // Malzeme sayısı verilen aralıkta ise tarif ID'yi işleme alma
                if (malzemeSayisi >= malzemeSayisiMin && malzemeSayisi <= malzemeSayisiMax) {
                    // Tarif adını almak için tarifler tablosuna ID ile sorgu yapma
                    String tarifAdi = tarifAraID(tarifID).get(0);
                    bulunanTarifler.add(tarifAdi);
                }
            }
        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return bulunanTarifler;
    }

    // 14 - TarifID ile tarif arama
    public static List<String> tarifAraID(Integer tarifID) {
        List<String> bulunanTarifler = new ArrayList<>();

        String sql = "SELECT TarifAdi FROM tarifler WHERE TarifID = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Aranacak ID'yi ayarlama
            pstmt.setInt(1, tarifID);
            ResultSet rs = pstmt.executeQuery();

            // Eşleşen tarifleri listeye ekleme
            while (rs.next()) {
                bulunanTarifler.add(rs.getString("TarifAdi"));
            }

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return bulunanTarifler;
    }

    // 15 - Kategori ile tarif arama
    public static List<String> tarifAraKategori(String aranacakKategori) {
        List<String> bulunanTarifler = new ArrayList<>();

        String sql = "SELECT TarifAdi FROM tarifler WHERE Kategori = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Aranacak kategoriyi ayarlama
            pstmt.setString(1, aranacakKategori);
            ResultSet rs = pstmt.executeQuery();

            // Eşleşen tarifleri listeye ekleme
            while (rs.next()) {
                bulunanTarifler.add(rs.getString("TarifAdi"));
            }

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return bulunanTarifler;
    }

    // 16 - Hazırlanış süresi kullanarak tarif aratma
    public static List<String> tarifAraHazirlamaSure(Integer maxHazirlamaSure) {
        List<String> bulunanTarifler = new ArrayList<>();
        String tarifMalzemelerCountSql = "SELECT TarifAdi FROM Tarifler WHERE HazirlamaSuresi <= ?";

        // TarifMalzeme tablosunda malzeme sayısını hesaplayıp TarifID'leri bulma
        try (Connection conn = connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(tarifMalzemelerCountSql)) {

            // Aranacak kategoriyi ayarlama
            pstmt.setInt(1, maxHazirlamaSure);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String tarifAd = rs.getString("TarifAdi");
                bulunanTarifler.add(tarifAd);
            }
        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return bulunanTarifler;
    }

    // 17 - Tarif maliyetini hesaplama
    public static float tarifMaliyetiHesapla(int tarifID) {
        float toplamMaliyet = 0.0f;

        // TarifMalzeme tablosundan tarifteki her bir malzemenin miktarını ve Malzeme tablosundan birim fiyatını getirme
        String sql = "SELECT tm.MalzemeID, tm.MalzemeMiktar, m.BirimFiyat " +
                "FROM TarifMalzeme tm " +
                "JOIN Malzemeler m ON tm.MalzemeID = m.MalzemeID " +
                "WHERE tm.TarifID = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // TarifID'yi sorguya ekle
            pstmt.setInt(1, tarifID);

            ResultSet rs = pstmt.executeQuery();

            // Her malzeme için maliyet hesapla ve toplam maliyete ekle
            while (rs.next()) {
                float miktar = rs.getFloat("MalzemeMiktar");
                float birimFiyat = rs.getFloat("BirimFiyat");
                float malzemeMaliyeti = miktar * birimFiyat;
                toplamMaliyet += malzemeMaliyeti;
            }

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return toplamMaliyet;
    }

    // 17.1 - Depodaki malzemeleri çıkarıp tarif maliyetini hesaplama
    public static float tarifMaliyetiHesaplaDepoDahil(int tarifID) {
        float toplamMaliyet = 0.0f;

        // TarifMalzeme tablosundan tarifteki her bir malzemenin miktarını ve Malzeme tablosundan birim fiyatını getirme
        String sql = "SELECT tm.MalzemeID, m.ToplamMiktar, tm.MalzemeMiktar, m.BirimFiyat " +
                "FROM TarifMalzeme tm " +
                "JOIN Malzemeler m ON tm.MalzemeID = m.MalzemeID " +
                "WHERE tm.TarifID = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // TarifID'yi sorguya ekle
            pstmt.setInt(1, tarifID);

            ResultSet rs = pstmt.executeQuery();

            // Her malzeme için maliyet hesapla ve toplam maliyete ekle
            while (rs.next()) {
                float miktar =  rs.getFloat("ToplamMiktar") - rs.getFloat("MalzemeMiktar");
                if (miktar >= 0) {
                    miktar = 0;
                }
                else {
                    miktar = Math.abs(miktar);
                }
                float birimFiyat = rs.getFloat("BirimFiyat");
                float malzemeMaliyeti = miktar * birimFiyat;
                toplamMaliyet += malzemeMaliyeti;
            }

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return toplamMaliyet;
    }

    // 18 - Tarif ismi ile tarifID bulma
    public static int tarifIDAraIsim(String tarifAdi) {
        int tarifID = -1; // Eşleşme bulunamazsa -1 döneceğiz

        String sql = "SELECT TarifID FROM tarifler WHERE TarifAdi = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Aranacak tarif adını sorguya ekle
            pstmt.setString(1, tarifAdi);

            ResultSet rs = pstmt.executeQuery();

            // Eğer kayıt bulunursa, tarifID'yi al
            if (rs.next()) {
                tarifID = rs.getInt("TarifID");
            }

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return tarifID;
    }

    // 19 - Tarif ismi ile tarifID bulma
    public static String malzemeBirimBul(String malzemeAd) {
        String sql = "SELECT MalzemeBirim FROM Malzemeler WHERE MalzemeAdi = ?";
        String malzemeBirim = "";
        try (Connection conn = connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Aranacak tarif adını sorguya ekle
            pstmt.setString(1, malzemeAd);

            ResultSet rs = pstmt.executeQuery();

            // Eğer kayıt bulunursa, tarifID'yi al
            if (rs.next()) {
                malzemeBirim = rs.getString("MalzemeBirim");
            }

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return malzemeBirim;
    }

    // 20 - Malzemeler tablosundaki ToplamMiktar değerini değiştirme
    public static void toplamMiktarGuncelle(int malzemeID, float yeniToplamMiktar) {
        String sql = "UPDATE Malzemeler SET ToplamMiktar = ? WHERE MalzemeID = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Parametreleri ayarlama
            pstmt.setFloat(1, yeniToplamMiktar);
            pstmt.setInt(2, malzemeID);

            // Güncelleme sorgusunu çalıştır
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

    }

    // 21 - Tarif adı ile malzemelerin toplam yüzdesini hesaplama
    public static float malzemeToplamEslesmeYuzdesiHesapla(String tarifAdi) {
        String sqlTarifID = "SELECT TarifID FROM tarifler WHERE TarifAdi = ?";
        String sqlMalzemeler =
                "SELECT tm.MalzemeMiktar, m.ToplamMiktar " +
                        "FROM TarifMalzeme tm " +
                        "JOIN Malzemeler m ON tm.MalzemeID = m.MalzemeID " +
                        "WHERE tm.TarifID = ?";

        float toplamGerekenMiktar = 0.0f;
        float toplamMevcutMiktar = 0.0f;

        try (Connection conn = connectDatabase();
             PreparedStatement pstmtTarifID = conn.prepareStatement(sqlTarifID);
             PreparedStatement pstmtMalzemeler = conn.prepareStatement(sqlMalzemeler)) {

            // Tarif ID'sini bulma
            pstmtTarifID.setString(1, tarifAdi);
            ResultSet rsTarifID = pstmtTarifID.executeQuery();
            int tarifID = -1;

            if (rsTarifID.next()) {
                tarifID = rsTarifID.getInt("TarifID");
            } else {
                System.out.println("Tarif bulunamadı: " + tarifAdi);
                return 0.0f;
            }

            // Malzemeleri çekme
            pstmtMalzemeler.setInt(1, tarifID);
            ResultSet rsMalzemeler = pstmtMalzemeler.executeQuery();

            while (rsMalzemeler.next()) {
                float gerekliMiktar = rsMalzemeler.getFloat("MalzemeMiktar");
                float toplamMiktar = rsMalzemeler.getFloat("ToplamMiktar");

                // Toplam gereken ve mevcut miktarları güncelleme
                toplamGerekenMiktar += gerekliMiktar;
                toplamMevcutMiktar += Math.min(toplamMiktar, gerekliMiktar); // Gereken miktardan fazlası sayılmıyor
            }

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        // Tüm tarifin yüzdesini hesaplama
        if (toplamGerekenMiktar == 0) {
            return 0.0f;
        }

        float toplamYuzde = (toplamMevcutMiktar / toplamGerekenMiktar) * 100;

        // Yüzdeyi 2 ondalık basamağa yuvarlama ve noktaya çevirme
        String eslesmeYuzdesi = String.format("%.2f", toplamYuzde).replace(",", ".");
        return Float.parseFloat(eslesmeYuzdesi);
    }

    // 22 - Girilen tarife ait malzemeleri ve ne kadar kullanıldıklarını döndüren fonksiyon
    public static Map<String, Float> malzemeleriVeMiktarlariBul(String tarifAdi) {
        String sql = """
        SELECT m.MalzemeAdi, tm.MalzemeMiktar
        FROM TarifMalzeme tm
        INNER JOIN Malzemeler m ON tm.MalzemeID = m.MalzemeID
        INNER JOIN Tarifler t ON tm.TarifID = t.TarifID
        WHERE t.TarifAdi = ?
        """;

        Map<String, Float> malzemeVeMiktar = new HashMap<>();

        try (Connection conn = connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Aranacak tarif adını sorguya ekle
            pstmt.setString(1, tarifAdi);

            ResultSet rs = pstmt.executeQuery();

            // Sonuçları döngü ile al
            while (rs.next()) {
                String malzemeAdi = rs.getString("MalzemeAdi");
                float miktar = rs.getFloat("MalzemeMiktar");
                malzemeVeMiktar.put(malzemeAdi, miktar);
            }

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return malzemeVeMiktar;
    }

    // 23 - Tarif silme fonksiyonu (TarifID ile)
    public static boolean tarifSil(int tarifID) {
        // Veritabanı bağlantısı
        String deleteTarifMalzemeSQL = "DELETE FROM TarifMalzeme WHERE TarifID = ?";
        String deleteTarifSQL = "DELETE FROM tarifler WHERE TarifID = ?";

        try (Connection conn = connectDatabase()) {
            // Otomatik commit özelliğini kapatma (transaction yönetimi için)
            conn.setAutoCommit(false);

            // TarifMalzeme tablosundan ilgili tarif bilgilerini silme
            try (PreparedStatement pstmtDeleteTarifMalzeme = conn.prepareStatement(deleteTarifMalzemeSQL)) {
                pstmtDeleteTarifMalzeme.setInt(1, tarifID);
                pstmtDeleteTarifMalzeme.executeUpdate();
            }

            // Tarifler tablosundan ilgili tarifi silme
            try (PreparedStatement pstmtDeleteTarif = conn.prepareStatement(deleteTarifSQL)) {
                pstmtDeleteTarif.setInt(1, tarifID);
                pstmtDeleteTarif.executeUpdate();
            }

            // İşlemleri başarıyla tamamla
            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
            return false;
        }
    }

    // 24 - Tarif silme fonksiyonu (Tarif Adı ile)
    public static boolean tarifSil(String tarifAdi) {
        String sqlDeleteFromTarifMalzeme = "DELETE FROM TarifMalzeme WHERE TarifID = ?";
        String sqlDeleteFromTarifler = "DELETE FROM Tarifler WHERE TarifID = ?";

        try (Connection conn = connectDatabase()) {
            // TarifID'yi bulma
            int tarifID = tarifIDAraIsim(tarifAdi);

            // Silme işlemi için bağlantı ve prepared statement kullanımı
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlDeleteFromTarifMalzeme);
                 PreparedStatement pstmt2 = conn.prepareStatement(sqlDeleteFromTarifler)) {

                // İlk önce TarifMalzeme tablosundan sil
                pstmt1.setInt(1, tarifID);
                pstmt1.executeUpdate();

                // Sonra Tarifler tablosundan sil
                pstmt2.setInt(1, tarifID);
                pstmt2.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
            return false;
        }
    }

    // 25 - Tabloya tarif güncelleme fonksiyonu
    public static boolean updateTarif(int tarifID, Tarif tarif, ArrayList<String> malzemeler, ArrayList<Float> malzeme_miktar) {

        try (Connection conn = connectDatabase()) {
            if (conn != null) {
                System.out.println("Veritabanına başarıyla bağlanıldı!");

                // Güncelleme ve kontrol sorguları
                String sqlUpdateTarif = "UPDATE Tarifler SET TarifAdi = ?, Kategori = ?, HazirlamaSuresi = ?, Talimatlar = ? WHERE TarifID = ?";
                String sqlCheckExistence = "SELECT COUNT(*) FROM Tarifler WHERE TarifAdi = ? AND TarifID != ?";
                String sqlDeleteOldMalzemeler = "DELETE FROM TarifMalzeme WHERE TarifID = ?";

                try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdateTarif);
                     PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheckExistence);
                     PreparedStatement pstmtDeleteOldMalzemeler = conn.prepareStatement(sqlDeleteOldMalzemeler)) {

                    // Tarif adı çakışma kontrolü
                    pstmtCheck.setString(1, tarif.getTarifAdi().strip());
                    pstmtCheck.setInt(2, tarifID);
                    ResultSet result = pstmtCheck.executeQuery();
                    result.next();

                    int count = result.getInt(1);
                    if (count > 0) {
                        System.out.println("Bu tarif adı zaten başka bir tarifte mevcut.");
                        return false;
                    }

                    // Tarif tablosunu güncelle
                    pstmtUpdate.setString(1, tarif.getTarifAdi());
                    pstmtUpdate.setString(2, tarif.getKategori());
                    pstmtUpdate.setFloat(3, tarif.getHazirlamaSuresi());
                    pstmtUpdate.setString(4, tarif.getTalimatlar());
                    pstmtUpdate.setInt(5, tarifID);
                    pstmtUpdate.executeUpdate();

                    // Eski malzemeleri sil
                    pstmtDeleteOldMalzemeler.setInt(1, tarifID);
                    pstmtDeleteOldMalzemeler.executeUpdate();

                    // Yeni malzemeleri ekle
                    String sqlGetMalzemeID = "SELECT MalzemeID FROM Malzemeler WHERE MalzemeAdi = ?";
                    String sqlInsertTarifMalzeme = "INSERT INTO TarifMalzeme (TarifID, MalzemeID, MalzemeMiktar) VALUES (?, ?, ?)";

                    try (PreparedStatement pstmtGetMalzemeID = conn.prepareStatement(sqlGetMalzemeID);
                         PreparedStatement pstmtInsertTarifMalzeme = conn.prepareStatement(sqlInsertTarifMalzeme)) {

                        for (int i = 0; i < malzemeler.size(); i++) {
                            String malzemeAd = malzemeler.get(i);
                            Float malzemeMiktar = malzeme_miktar.get(i);

                            // MalzemeID'yi almak için sorgu
                            pstmtGetMalzemeID.setString(1, malzemeAd);
                            ResultSet rsMalzemeID = pstmtGetMalzemeID.executeQuery();

                            if (rsMalzemeID.next()) {
                                int malzemeID = rsMalzemeID.getInt("MalzemeID");
                                // Tarif-Malzeme tablosuna yeni değerleri ekle
                                pstmtInsertTarifMalzeme.setInt(1, tarifID);
                                pstmtInsertTarifMalzeme.setInt(2, malzemeID);
                                pstmtInsertTarifMalzeme.setFloat(3, malzemeMiktar);
                                pstmtInsertTarifMalzeme.executeUpdate();
                            } else {
                                System.out.println("Malzeme bulunamadı: " + malzemeAd);
                            }
                        }
                        System.out.println(tarif.getTarifAdi() + " başarıyla güncellendi.");
                        return true;
                    } catch (SQLException e) {
                        System.out.println("Malzeme güncellenirken hata oluştu: " + e.getMessage());
                    }

                } catch (SQLException e) {
                    System.out.println("Veritabanı işlemi sırasında hata oluştu: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Veritabanına bağlanırken hata oluştu: " + e.getMessage());
        }
        return false;
    }

}
