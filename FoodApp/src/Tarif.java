public class Tarif {
    private int tarifID;
    private String tarifAdi;
    private String kategori;
    private int hazirlamaSuresi;
    private String talimatlar;

    // Get metodları
    public int getTarifID() {
        return tarifID;
    }

    public String getTarifAdi() {
        return tarifAdi;
    }

    public String getKategori() {
        return kategori;
    }

    public int getHazirlamaSuresi() {
        return hazirlamaSuresi;
    }

    public String getTalimatlar() {
        return talimatlar;
    }

    // Set metodları
    public void setTarifID(int tarifID) {
        this.tarifID = tarifID;
    }

    public void setTarifAdi(String tarifAdi) {
        this.tarifAdi = tarifAdi;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setHazirlamaSuresi(int hazirlamaSuresi) {
        this.hazirlamaSuresi = hazirlamaSuresi;
    }

    public void setTalimatlar(String talimatlar) {
        this.talimatlar = talimatlar;
    }

    // Boş Constructor
    public Tarif() {
    }

    // Parametreli Constructor
    public Tarif(int tarifID, String tarifAdi, String kategori, int hazirlamaSuresi, String talimatlar) {
        setTarifID(tarifID);
        setTarifAdi(tarifAdi);
        setKategori(kategori);
        setHazirlamaSuresi(hazirlamaSuresi);
        setTalimatlar(talimatlar);
    }
}
