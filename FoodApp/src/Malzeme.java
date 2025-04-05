public class Malzeme {
    private int malzemeID;
    private String malzemeAdi;
    private String toplamMiktar;
    private String malzemeBirim;
    private float birimFiyat;

    // Getter metodları
    public int getMalzemeID() {
        return malzemeID;
    }

    public String getMalzemeAdi() {
        return malzemeAdi;
    }

    public String getToplamMiktar() {
        return toplamMiktar;
    }

    public String getMalzemeBirim() {
        return malzemeBirim;
    }

    public float getBirimFiyat() {
        return birimFiyat;
    }

    // Setter metodları
    public void setMalzemeID(int malzemeID) {
        this.malzemeID = malzemeID;
    }

    public void setMalzemeAdi(String malzemeAdi) {
        this.malzemeAdi = malzemeAdi;
    }

    public void setToplamMiktar(String toplamMiktar) {
        this.toplamMiktar = toplamMiktar;
    }

    public void setMalzemeBirim(String malzemeBirim) {
        this.malzemeBirim = malzemeBirim;
    }

    public void setBirimFiyat(float birimFiyat) {
        this.birimFiyat = birimFiyat;
    }

    // Boş Constructor
    public Malzeme() {
    }

    // Parametreli Constructor
    public Malzeme(int malzemeID, String malzemeAdi, String toplamMiktar, String malzemeBirim, float birimFiyat) {
        setMalzemeID(malzemeID);
        setMalzemeAdi(malzemeAdi);
        setToplamMiktar(toplamMiktar);
        setMalzemeBirim(malzemeBirim);
        setBirimFiyat(birimFiyat);
    }
}
