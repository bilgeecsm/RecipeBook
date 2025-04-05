import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.*;
import java.util.List;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import javax.swing.table.TableRowSorter;

public class Tarifler extends JFrame {
    private JPanel filtrelePanel;
    private JCheckBox[] malzemeCheckboxlar;
    private JTextField[] miktarTextFieldlar;
    private JComboBox<String> kategoriComboBox;
    private JTextField minMalzemeField;
    private JTextField maxMalzemeField;
    private JScrollPane malzemeScrollPane;
    private JComboBox<String> sureComboBox;
    private JButton araButton;
    private JTable sonucTablosu;
    private DefaultTableModel tarifSonucTablo;
    private JTextArea tarifDetayiTextArea;
    private JPanel malzemeEklePanel;
    private JButton tariflerButton;
    private JButton malzemeEkleButton;
    private JButton tarifEkleButton;
    private JButton duzenleButton;
    private JButton silButton;
    private JPanel listePanel;
    private JPanel bulPanel;
    private JPanel checkboxPanel;
    private JComboBox siralaComboBox;
    private JLabel siralaLabel;
    private JButton bulButton;
    private JTextField bulTextField;


    private static final Color PANEL_BACKGROUND_COLOR = new Color(55, 52, 53);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color TEXTFIELD_BACKGROUND_COLOR =  new Color(237, 237, 237);
    private static final Color TABLE_BACKGROUND_COLOR =  new Color(164, 201, 176);
    private static final Color BUTTON_BACKGROUND_COLOR =  new Color(197, 70, 63);
    private static final Color BUTTON_ACCEPT_COLOR = new Color(28, 201, 93);

    private static final float minimumEslesmeYuzdesi = 100;

    public Tarifler() {
        setTitle("Tarifler");
        setSize(1950, 920);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        filtrelePanel = new JPanel();
        filtrelePanel.setLayout(new BoxLayout(filtrelePanel, BoxLayout.Y_AXIS));
        filtrelePanel.setBackground(PANEL_BACKGROUND_COLOR);
        filtrelePanel.setPreferredSize(new Dimension(255, getHeight()));

        // Kategori kutucuğu
        JLabel kategoriLabel = new JLabel("Kategori");
        kategoriLabel.setForeground(TEXT_COLOR);
        String[] kategoriler = {"Fark etmez", "Ana Yemek", "Kahvaltı", "Atıştırmalık",
                "Çorba", "Meze", "Hamur İşi", "Salata", "Tatlı", "İçecek"};

        kategoriComboBox = new JComboBox<>(kategoriler);
        Dimension kategoriFieldSize = new Dimension(290, 30);
        kategoriComboBox.setPreferredSize(kategoriFieldSize);
        kategoriComboBox.setMaximumSize(kategoriFieldSize);
        kategoriComboBox.setMinimumSize(kategoriFieldSize);
        kategoriComboBox.setBackground(TEXTFIELD_BACKGROUND_COLOR);
        kategoriComboBox.setForeground(Color.BLACK);
        filtrelePanel.add(kategoriLabel);
        filtrelePanel.add(kategoriComboBox);

        // Hazırlama Süresi
        JLabel sureLabel = new JLabel("Hazırlama Süresi");
        sureLabel.setForeground(TEXT_COLOR);
        String[] sureSecenekleri = {"Fark etmez", "10 dakika", "20 dakika", "30 dakika",
                "40 dakika", "50 dakika", "60 dakika", "60+ dakika"};
        sureComboBox = new JComboBox<>(sureSecenekleri);
        Dimension sureFieldSize = new Dimension(290, 30);
        sureComboBox.setPreferredSize(sureFieldSize);
        sureComboBox.setMaximumSize(sureFieldSize);
        sureComboBox.setMinimumSize(sureFieldSize);
        sureComboBox.setBackground(TEXTFIELD_BACKGROUND_COLOR);
        sureComboBox.setForeground(Color.BLACK);
        filtrelePanel.add(sureLabel);
        filtrelePanel.add(sureComboBox);

        // Malzeme min max
        JLabel malzemeCesidiLabel = new JLabel("Malzeme Çeşidi                         ");
        malzemeCesidiLabel.setForeground(TEXT_COLOR);
        malzemeCesidiLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel cesitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); // 5 piksel yatay boşluk, dikey boşluk yok

        minMalzemeField = new JTextField(5);
        maxMalzemeField = new JTextField(5);
        cesitPanel.add(malzemeCesidiLabel);

        cesitPanel.add(new JLabel("Min:"));
        cesitPanel.add(minMalzemeField);
        cesitPanel.add(new JLabel("Max:"));
        cesitPanel.add(maxMalzemeField);
        filtrelePanel.add(cesitPanel);

        // Malzemeler
        // Arama alanı ekle
        JTextField aramaTextField = new JTextField(25); // 10 karakter genişliğinde
        aramaTextField.setBackground(PANEL_BACKGROUND_COLOR);
        aramaTextField.setForeground(TEXT_COLOR);
        JPanel malzemeAramaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); // 5 piksel yatay boşluk, dikey boşluk yok
        malzemeAramaPanel.setBackground(PANEL_BACKGROUND_COLOR);
        JLabel malzemeArabaslikLabel = new JLabel("Malzeme Ara");
        malzemeArabaslikLabel.setForeground(TEXT_COLOR);
        malzemeArabaslikLabel.setFont(new Font("Arial", Font.BOLD, 16));
        malzemeAramaPanel.add(malzemeArabaslikLabel);
        malzemeAramaPanel.add(aramaTextField);

        filtrelePanel.add(malzemeAramaPanel);

        JLabel malzemelerLabel = new JLabel();
        malzemelerLabel.setForeground(TEXT_COLOR);
        List<String> malzemelerAd = DatabaseFonksiyon.malzemeAraAd("");
        malzemeCheckboxlar = new JCheckBox[malzemelerAd.size()]; // Temsili olarak 20 malzeme checkbox
        miktarTextFieldlar = new JTextField[malzemeCheckboxlar.length]; // Her malzeme için bir JTextField

        // Başlıklar
        JPanel baslikPanel = new JPanel(new GridLayout(1, 2)); // 1 satır, 2 sütun
        JLabel malzemelerBaslikLabel = new JLabel(" Malzemeler");
        malzemelerBaslikLabel.setForeground(TEXT_COLOR);
        malzemelerBaslikLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Yazı tipi, stil ve boyut ayarı

        JLabel miktarBaslikLabel = new JLabel();
        malzemelerBaslikLabel.setForeground(TEXT_COLOR);
        miktarBaslikLabel.setForeground(TEXT_COLOR);

        malzemelerBaslikLabel.setHorizontalAlignment(SwingConstants.LEFT);
        miktarBaslikLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        baslikPanel.setBackground(PANEL_BACKGROUND_COLOR);
        baslikPanel.add(malzemelerBaslikLabel);

        filtrelePanel.add(baslikPanel);

        // Malzemelerin gösterildiği yer
        checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        checkboxPanel.setBackground(PANEL_BACKGROUND_COLOR);

        malzemeScrollPane = new JScrollPane(checkboxPanel);
        malzemeScrollPane.setPreferredSize(new Dimension(150, 350));
        filtrelePanel.add(malzemelerLabel);
        filtrelePanel.add(malzemeScrollPane);
        for (int i = 0; i < malzemeCheckboxlar.length; i++) {
            JPanel malzemeSatiri = new JPanel(new BorderLayout());
            malzemeSatiri.setBackground(PANEL_BACKGROUND_COLOR);

            malzemeCheckboxlar[i] = new JCheckBox(malzemelerAd.get(i));
            malzemeCheckboxlar[i].setBackground(PANEL_BACKGROUND_COLOR);
            malzemeCheckboxlar[i].setForeground(TEXT_COLOR);

            // Miktar için Label ve TextField
            JLabel miktarLabel = new JLabel("Miktar: ");
            miktarLabel.setForeground(TEXT_COLOR);
            miktarLabel.setVisible(false); // Başlangıçta görünmesin

            miktarTextFieldlar[i] = new JTextField(6); // Daha kısa TextField
            miktarTextFieldlar[i].setVisible(false);

            // Miktar Label ve TextField'ı tek bir panelde toplama
            JPanel miktarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            miktarPanel.setBackground(PANEL_BACKGROUND_COLOR);
            miktarPanel.add(miktarLabel);
            miktarPanel.add(miktarTextFieldlar[i]);

            malzemeSatiri.add(malzemeCheckboxlar[i], BorderLayout.WEST);
            malzemeSatiri.add(miktarPanel, BorderLayout.EAST);

            // Satırların arasına boşluk eklemek için
            malzemeSatiri.setBorder(new EmptyBorder(5, 0, 5, 0)); // Üst ve alt boşluk 5 piksel

            malzemeCheckboxlar[i].addItemListener(e -> {
                JCheckBox checkbox = (JCheckBox) e.getSource();
                for (int j = 0; j < malzemeCheckboxlar.length; j++) {
                    if (checkbox == malzemeCheckboxlar[j]) {
                        boolean isSelected = checkbox.isSelected();
                        miktarLabel.setVisible(isSelected);
                        miktarTextFieldlar[j].setVisible(isSelected);

                        final JTextField textField = miktarTextFieldlar[j];
                        final String malzemeBirim = DatabaseFonksiyon.malzemeBirimBul(checkbox.getText());

                        // TextField’a başlangıçta "Search" yazısını ve rengi gri olarak ayarla
                        if (textField.getText().isEmpty()) {
                            textField.setForeground(Color.GRAY);
                            textField.setText(malzemeBirim);
                        }

                        // FocusListener ekle
                        textField.addFocusListener(new FocusListener() {
                            @Override
                            public void focusGained(FocusEvent e) {
                                // Focus kazanıldığında sadece placeholder metni varsa temizle
                                if (textField.getText().equals(malzemeBirim)) {
                                    textField.setText("");
                                    textField.setForeground(Color.BLACK);
                                }
                            }

                            @Override
                            public void focusLost(FocusEvent e) {
                                // Focus kaybedildiğinde TextField boşsa placeholder metnini geri getir
                                if (textField.getText().isEmpty()) {
                                    textField.setForeground(Color.GRAY);
                                    textField.setText(malzemeBirim);
                                }
                            }
                        });

                        // DocumentListener ekle (Gri placeholder rengini kontrol etmek için)
                        textField.getDocument().addDocumentListener(new DocumentListener() {
                            @Override
                            public void insertUpdate(DocumentEvent e) {
                                updateTextColor();
                            }

                            @Override
                            public void removeUpdate(DocumentEvent e) {
                                updateTextColor();
                            }

                            @Override
                            public void changedUpdate(DocumentEvent e) {
                                updateTextColor();
                            }

                            private void updateTextColor() {
                                // Eğer placeholder yazısı yoksa yazıyı siyah yap
                                if (!textField.getText().equals(malzemeBirim)) {
                                    textField.setForeground(Color.BLACK);
                                }
                            }
                        });

                        break;
                    }
                }


                checkboxPanel.revalidate();
                checkboxPanel.repaint();
            });

            checkboxPanel.add(malzemeSatiri);
        }

        // Arama alanı için DocumentListener ekle
        aramaTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {filtreMalzemeler();}
            @Override
            public void removeUpdate(DocumentEvent e) {filtreMalzemeler();}
            @Override
            public void changedUpdate(DocumentEvent e) {filtreMalzemeler();}

            private void filtreMalzemeler() {
                String aramaMetni = aramaTextField.getText().toLowerCase();
                for (int i = 0; i < malzemeCheckboxlar.length; i++) {
                    String malzemeAdi = malzemeCheckboxlar[i].getText().toLowerCase();
                    boolean eslesme = malzemeAdi.contains(aramaMetni);
                    boolean secili = malzemeCheckboxlar[i].isSelected();
                    // Eğer malzeme arama metnine uyuyorsa veya seçiliyse görünür tut
                    malzemeCheckboxlar[i].getParent().setVisible(eslesme || secili);
                }
                checkboxPanel.revalidate();
                checkboxPanel.repaint();
            }
        });

        // Ara Butonu
        araButton = new JButton("Ara");
        araButton.setBackground(BUTTON_BACKGROUND_COLOR);
        filtrelePanel.add(araButton);

        // Listeleme paneli oluşturma
        listePanel = new JPanel(new BorderLayout());
        listePanel.setBackground(PANEL_BACKGROUND_COLOR);
        listePanel.setPreferredSize(new Dimension(300, getHeight()));

        // İsme göre aratma kısmı
        bulPanel = new JPanel();
        bulPanel.setBackground(PANEL_BACKGROUND_COLOR);
        bulButton = new JButton("Bul");
        bulButton.setBackground(BUTTON_BACKGROUND_COLOR);
        bulButton.setForeground(TEXT_COLOR);
        bulTextField = new JTextField();
        bulTextField.setPreferredSize(new Dimension(250, 30));
        siralaLabel = new JLabel("Sırala");
        siralaLabel.setForeground(TEXT_COLOR);
        JComboBox<String> siralaComboBox = new JComboBox<>(new String[]{"Maliyet Azalan","Maliyet Artan","Süre Azalan","Süre Artan"});
        bulPanel.add(siralaLabel);
        bulPanel.add((siralaComboBox));
        bulPanel.add(bulTextField);
        bulPanel.add(bulButton);
        listePanel.add(bulPanel, BorderLayout.NORTH);

        // Tarif isimlerini kullanarak tarif bilgilerini çekme
        List<String[]> veriListesi = new ArrayList<>();
        List<String> arananTarifler = DatabaseFonksiyon.tarifAraAd("");
        for(int i = 0; i < arananTarifler.size(); i++) {
            List<String> bulunanTarif = DatabaseFonksiyon.tarifBilgiAra(arananTarifler.get(i));
            String bulunanTarifAd = bulunanTarif.get(1);
            String bulunanKategori = bulunanTarif.get(2);
            String bulunanSure = bulunanTarif.get(3) + " Dakika";
            Float maliyet = DatabaseFonksiyon.tarifMaliyetiHesapla(DatabaseFonksiyon.tarifIDAraIsim(bulunanTarifAd));
            Float maliyetDepoDahil = DatabaseFonksiyon.tarifMaliyetiHesaplaDepoDahil(DatabaseFonksiyon.tarifIDAraIsim(bulunanTarifAd));
            Float eslesmeYuzdesi = DatabaseFonksiyon.malzemeToplamEslesmeYuzdesiHesapla(bulunanTarifAd);

            String[] tarifBilgi = {
                    bulunanTarifAd,
                    bulunanKategori,
                    bulunanSure,
                    maliyet + " TL",
                    maliyetDepoDahil + " TL",
                    eslesmeYuzdesi + "%"};
            veriListesi.add(tarifBilgi);
        }

        // Eşleşme yüzdesine göre azalan sıralama
        veriListesi.sort((a, b) -> {
            Float yuzdeA = Float.parseFloat(a[5].replace("%", "").replace(",", "."));
            Float yuzdeB = Float.parseFloat(b[5].replace("%", "").replace(",", "."));
            return yuzdeB.compareTo(yuzdeA); // Azalan sıralama
        });

        // ArrayList'i tabloya uygun hale getirmek için diziye çevirme
        String[][] veri = veriListesi.toArray(new String[0][]);
        String[] sutunIsimleri = {"Yemek Adı", "Kategori", "Hazırlanma Süresi", "Maliyet", "Size Maliyeti", "Eşleşme Yüzdesi",};
        // Tarif tablosunun editlenebilirliğini kapatmaya yarar
        tarifSonucTablo = new DefaultTableModel(veri, sutunIsimleri) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tüm hücrelerin düzenlenemez olmasını sağlar
            }
        };
        // Tarif tablosu
        sonucTablosu = new JTable(tarifSonucTablo);

        // TableRowSorter kullanarak tabloyu sıralamak için ayarlıyoruz
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tarifSonucTablo) {
            @Override
            public void toggleSortOrder(int column) {
                // Boş bırakılarak tablo başlıklarına tıklama ile sıralamayı devre dışı bırakır
            }
        };
        sonucTablosu.setRowSorter(sorter);

        // Süre sütunu (2. sütun) için Comparator (Dakikayı ayırarak sayısal sıralama)
        sorter.setComparator(2, (o1, o2) -> {
            // "30 Dakika" -> ["30", "Dakika"] şeklinde ayırır ve ilk kısmı int'e dönüştürür
            int sure1 = Integer.parseInt(o1.toString().split(" ")[0]);
            int sure2 = Integer.parseInt(o2.toString().split(" ")[0]);
            return Integer.compare(sure1, sure2);
        });

        // Size Maliyeti sütunu (4. sütun) için Comparator (Sayı olmayan karakterleri kaldırarak sayısal sıralama)
        sorter.setComparator(4, (o1, o2) -> {
            // "50.5 TL" gibi bir değeri "50.5" haline getirir ve float olarak karşılaştırır
            Float maliyet1 = Float.parseFloat(o1.toString().replaceAll("[^\\d.]", ""));
            Float maliyet2 = Float.parseFloat(o2.toString().replaceAll("[^\\d.]", ""));
            return Float.compare(maliyet1, maliyet2);
        });

        // JComboBox için bir dinleyici ekleyerek her seçimde tabloyu sıralayın
        siralaComboBox.addActionListener(e -> {
            String selectedOption = (String) siralaComboBox.getSelectedItem();

            // Sıralama ayarları
            List<RowSorter.SortKey> sortKeys = new ArrayList<>();
            switch (selectedOption) {
                case "Maliyet Azalan":
                    sortKeys.add(new RowSorter.SortKey(4, SortOrder.DESCENDING)); // Size Maliyeti sütunu azalan
                    break;
                case "Maliyet Artan":
                    sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING)); // Size Maliyeti sütunu artan
                    break;
                case "Süre Azalan":
                    sortKeys.add(new RowSorter.SortKey(2, SortOrder.DESCENDING)); // Süre sütunu azalan
                    break;
                case "Süre Artan":
                    sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING)); // Süre sütunu artan
                    break;
            }
            sorter.setSortKeys(sortKeys); // Seçilen sıralama ayarını tabloya uygula
            sorter.sort(); // Tabloyu yeniden sıralayın
        });

        // Tarif tablosu renklendirme işlemleri
        sonucTablosu.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component hucre = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Seçili satır için arka plan rengi ve kenarlık
                if (isSelected) {
                    hucre.setBackground(new Color(173, 216, 230)); // Açık mavi arka plan
                    ((JComponent) hucre).setBorder(BorderFactory.createLineBorder(Color.BLUE, 2)); // Mavi kenarlık
                } else {
                    // Diğer hücreler için renklendirme (5. sütuna göre)
                    String yuzdeStr = (String) table.getValueAt(row, 5);
                    float yuzde = Float.parseFloat(yuzdeStr.replace("%", "").replace(",", "."));

                    if (yuzde >= minimumEslesmeYuzdesi) {
                        hucre.setBackground(new Color(144, 238, 144)); // Açık yeşil
                    } else {
                        hucre.setBackground(new Color(255, 182, 193)); // Açık kırmızı
                    }
                    ((JComponent) hucre).setBorder(null); // Seçili olmayan hücrelerin kenarlığını kaldırma
                }

                return hucre;
            }
        });

        // Tablodan bir satır seçildiğinde bilgileri yazdırmak için listener ekleme
        sonucTablosu.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && sonucTablosu.getSelectedRow() != -1) {
                int selectedRow = sonucTablosu.getSelectedRow();

                // sütün 0 tarifAdi
                // sütün 1 Kategorisi
                // sütün 2 hazirlanma süresi
                // sütün 3 maliyet
                // sütün 4 maliyet bize
                // sütün 5 eşleşme yüzdesi
                String tarifIsim = (String) sonucTablosu.getValueAt(selectedRow, 0);
                String tarifKategori = (String) sonucTablosu.getValueAt(selectedRow, 1);
                String tarifSure = (String) sonucTablosu.getValueAt(selectedRow, 2);
                Map<String, Float> tarifMalzemeMiktarlar = DatabaseFonksiyon.malzemeleriVeMiktarlariBul(tarifIsim);
                // Her bir malzemeyi ve miktarını stringe ekleme
                StringBuilder malzemeListesi = new StringBuilder();
                int sayac = 1;
                for (Map.Entry<String, Float> entry : tarifMalzemeMiktarlar.entrySet()) {
                    malzemeListesi.append(sayac + ". ")
                            .append(entry.getKey())
                            .append("\t\tMiktar: ")
                            .append(entry.getValue() + " ")
                            .append(DatabaseFonksiyon.malzemeBirimBul(entry.getKey()))
                            .append("\n");
                    sayac++;
                }
                String tarifTalimatlar = DatabaseFonksiyon.tarifBilgiAra(tarifIsim).get(4);
                tarifDetayiTextArea.setText(
                        "Tarif Adı: \t\t" + tarifIsim + "\n" +
                        "Tarif Kategorisi: \t" + tarifKategori + "\n" +
                        "Tarif Hazırlanma Süresi: \t" + tarifSure + "\n\n" +
                        "Malzemeler:\n" + malzemeListesi + "\n" +
                        "Tarif Talimatları: \n" + tarifTalimatlar + "\n"

                );
            }
        });
        sonucTablosu.setBackground(TABLE_BACKGROUND_COLOR);
        sonucTablosu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Sadece 1 tane tarifin seçilebilir olmasını sağlar
        listePanel.add(new JScrollPane(sonucTablosu), BorderLayout.CENTER);

        // Sağ Panel - tarif detayı + ekle, düzenle
        JPanel sagPanel = new JPanel();
        sagPanel.setLayout(new BoxLayout(sagPanel, BoxLayout.Y_AXIS)); // BoxLayout kullanımı
        sagPanel.setPreferredSize(new Dimension(370, getHeight()));
        sagPanel.setBackground(PANEL_BACKGROUND_COLOR);

        // Tarif Detayı Paneli
        JPanel tarifDetayiPanel = new JPanel(new BorderLayout());
        tarifDetayiPanel.add(new JLabel("Tarif Detayı"), BorderLayout.NORTH);
        tarifDetayiPanel.setBackground(PANEL_BACKGROUND_COLOR);
        tarifDetayiPanel.setPreferredSize(new Dimension(350, 200)); // Detay panelinin yüksekliği ayarlanabilir

        // Tarif Detayı TextArea
        tarifDetayiTextArea = new JTextArea();
        tarifDetayiTextArea.setEditable(false);
        tarifDetayiTextArea.setText("Tarif detayları burada görüntülenecek.");
        tarifDetayiTextArea.setBackground(TEXTFIELD_BACKGROUND_COLOR);
        tarifDetayiTextArea.setLineWrap(true); // Satır sarmalama ayarları 1
        tarifDetayiTextArea.setWrapStyleWord(true); // Satır sarmalama ayarları 2
        tarifDetayiPanel.add(new JScrollPane(tarifDetayiTextArea), BorderLayout.CENTER);

        // Malzeme Ekle Paneli , Ekle, Düzenle
        malzemeEklePanel = new JPanel(new GridLayout(6, 2));
        malzemeEklePanel.setBackground(PANEL_BACKGROUND_COLOR);
        malzemeEklePanel.setPreferredSize(new Dimension(380, 350)); // Yükseklik arttırıldı

        // Sağ panel bileşenlerini ekleme
        sagPanel.add(tarifDetayiPanel);
        sagPanel.add(malzemeEklePanel);


        // Buton Paneli
        JPanel butonPanel = new JPanel();
        butonPanel.setBackground(BUTTON_BACKGROUND_COLOR);

        tariflerButton = new JButton(("Tarifler"));
        malzemeEkleButton = new JButton("Malzeme Ekle");
        tarifEkleButton = new JButton("Tarif Ekle");
        duzenleButton = new JButton("Tarif Düzenle");
        silButton = new JButton("Tarif Sil");

        tariflerButton.setBackground(BUTTON_BACKGROUND_COLOR);
        malzemeEkleButton.setBackground(BUTTON_BACKGROUND_COLOR);
        tarifEkleButton.setBackground(BUTTON_BACKGROUND_COLOR);
        duzenleButton.setBackground(BUTTON_BACKGROUND_COLOR);
        silButton.setBackground(BUTTON_BACKGROUND_COLOR);

        tariflerButton.setOpaque(true);
        tariflerButton.setBorderPainted(false);

        malzemeEkleButton.setOpaque(true);
        malzemeEkleButton.setBorderPainted(false);

        tarifEkleButton.setOpaque(true);
        tarifEkleButton.setBorderPainted(false);

        silButton.setOpaque(true);
        silButton.setBorderPainted(false);

        duzenleButton.setOpaque(true);
        duzenleButton.setBorderPainted(false);

        butonPanel.add(tariflerButton);
        butonPanel.add(malzemeEkleButton);
        butonPanel.add(tarifEkleButton);
        butonPanel.add(duzenleButton);
        butonPanel.add(silButton);

        add(filtrelePanel, BorderLayout.WEST);
        add(listePanel, BorderLayout.CENTER);
        add(sagPanel, BorderLayout.EAST);
        add(butonPanel, BorderLayout.SOUTH);

        setComponentColors(filtrelePanel);
        setComponentColors(tarifDetayiPanel);
        setComponentColors(butonPanel);

        // "Ara" butonu
        araButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kategori Bilgisi
                String secilenKategori = kategoriComboBox.getItemAt(kategoriComboBox.getSelectedIndex());

                // Süre Bilgisi
                String secilenSureText = sureComboBox.getItemAt(sureComboBox.getSelectedIndex());
                Integer secilenSure = 999;
                if (secilenSureText.equals("Fark etmez") || secilenSureText.equals("60+ dakika")) {
                    secilenSure = 999;
                }
                else {
                    secilenSure = Integer.parseInt(secilenSureText.split(" ")[0]) ;
                }

                // Malzeme min-max Bilgisi
                Integer secilenMinMalzeme = 0;
                Integer secilenMaxMalzeme = 999;

                if (!minMalzemeField.getText().equals("")) {
                    secilenMinMalzeme =  Integer.parseInt(minMalzemeField.getText());
                }
                if (!maxMalzemeField.getText().equals("")) {
                    secilenMaxMalzeme =  Integer.parseInt(maxMalzemeField.getText());
                }

                // Malzeme miktar bilgisi
                List<String> secilenMalzemeler = new ArrayList<>();
                for(int i = 0; i < malzemeCheckboxlar.length; i++) {
                    if (malzemeCheckboxlar[i].isSelected()) {
                        // Depodaki Malzeme Miktarının belirlenmesi
                        float malzemeMiktar = 0;
                        if(!miktarTextFieldlar[i].getText().equals("")) {
                            malzemeMiktar = Float.parseFloat(miktarTextFieldlar[i].getText());
                        }

                        // Malzeme bilgilerini kaydetme
                        secilenMalzemeler.add(malzemeCheckboxlar[i].getText());

                        // Sql içindeki verileri güncelleme
                        DatabaseFonksiyon.toplamMiktarGuncelle(
                                DatabaseFonksiyon.malzemeIDAraAd(malzemeCheckboxlar[i].getText()).get(0),
                                malzemeMiktar
                        );
                    }
                }
                // Her bir filtrleme için tarif listelenmesi
                List<String> secilenKategoriTarifler = DatabaseFonksiyon.tarifAraKategori(secilenKategori);
                List<String> secilenMixMaxTarifler = DatabaseFonksiyon.tarifAraMalzemeSayisi(secilenMinMalzeme, secilenMaxMalzeme);
                List<String> secilenMalzemelerTarifler = DatabaseFonksiyon.tarifAraMalzeme(secilenMalzemeler);
                List<String> secilenSureTarifler = DatabaseFonksiyon.tarifAraHazirlamaSure(secilenSure);

                // Ortak tariflerin bulunması
                List<String> ortakTarifler = new ArrayList<>(DatabaseFonksiyon.tarifAraAd("")); // İlk listeyi başlangıç olarak alıyoruz
                if (!secilenKategori.equals("Fark etmez")) {
                    ortakTarifler.retainAll(secilenKategoriTarifler);
                }
                if (!secilenMalzemelerTarifler.isEmpty()) {
                    ortakTarifler.retainAll(secilenMalzemelerTarifler);
                }
                else if(!secilenMalzemeler.isEmpty()) {
                    ortakTarifler.retainAll(secilenMalzemelerTarifler);
                }
                ortakTarifler.retainAll(secilenMixMaxTarifler);
                ortakTarifler.retainAll(secilenSureTarifler);

                gosterListeTarifPanel(ortakTarifler);
            }
        });

        // "Bul" butonu
        bulButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kategori Bilgisi
                String secilenKategori = kategoriComboBox.getItemAt(kategoriComboBox.getSelectedIndex());

                // Süre Bilgisi
                String secilenSureText = sureComboBox.getItemAt(sureComboBox.getSelectedIndex());
                Integer secilenSure = 999;
                if (secilenSureText.equals("Fark etmez") || secilenSureText.equals("60+ dakika")) {
                    secilenSure = 999;
                }
                else {
                    secilenSure = Integer.parseInt(secilenSureText.split(" ")[0]) ;
                }

                // Malzeme min-max Bilgisi
                Integer secilenMinMalzeme = 1;
                Integer secilenMaxMalzeme = 999;

                if (!minMalzemeField.getText().equals("")) {
                    secilenMinMalzeme =  Integer.parseInt(minMalzemeField.getText());
                }
                if (!maxMalzemeField.getText().equals("")) {
                    secilenMaxMalzeme =  Integer.parseInt(maxMalzemeField.getText());
                }

                String secilenTarifAd = bulTextField.getText();

                // Her bir filtrleme için tarif listelenmesi
                List<String> secilenKategoriTarifler = DatabaseFonksiyon.tarifAraKategori(secilenKategori);
                List<String> secilenMixMaxTarifler = DatabaseFonksiyon.tarifAraMalzemeSayisi(secilenMinMalzeme, secilenMaxMalzeme);
                List<String> secilenSureTarifler = DatabaseFonksiyon.tarifAraHazirlamaSure(secilenSure);
                List<String> secilenTarifAdTarifler = DatabaseFonksiyon.tarifAraAd(secilenTarifAd);

                // Ortak tariflerin bulunması
                List<String> ortakTarifler = new ArrayList<>(DatabaseFonksiyon.tarifAraAd("")); // İlk listeyi başlangıç olarak alıyoruz
                if (!secilenKategori.equals("Fark etmez")) {
                    ortakTarifler.retainAll(secilenKategoriTarifler);
                }
                ortakTarifler.retainAll(secilenMixMaxTarifler);
                ortakTarifler.retainAll(secilenSureTarifler);
                ortakTarifler.retainAll(secilenTarifAdTarifler);

                gosterListeTarifPanel(ortakTarifler);
            }
        });

        // "Tarifler" butonu
        tariflerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kategori Bilgisi
                String secilenKategori = kategoriComboBox.getItemAt(kategoriComboBox.getSelectedIndex());

                // Süre Bilgisi
                String secilenSureText = sureComboBox.getItemAt(sureComboBox.getSelectedIndex());
                Integer secilenSure = 999;
                if (secilenSureText.equals("Fark etmez") || secilenSureText.equals("60+ dakika")) {
                    secilenSure = 999;
                }
                else {
                    secilenSure = Integer.parseInt(secilenSureText.split(" ")[0]) ;
                }

                // Malzeme min-max Bilgisi
                Integer secilenMinMalzeme = 0;
                Integer secilenMaxMalzeme = 999;

                if (!minMalzemeField.getText().equals("")) {
                    secilenMinMalzeme =  Integer.parseInt(minMalzemeField.getText());
                }
                if (!maxMalzemeField.getText().equals("")) {
                    secilenMaxMalzeme =  Integer.parseInt(maxMalzemeField.getText());
                }

                // Her bir filtrleme için tarif listelenmesi
                List<String> secilenKategoriTarifler = DatabaseFonksiyon.tarifAraKategori(secilenKategori);
                List<String> secilenMixMaxTarifler = DatabaseFonksiyon.tarifAraMalzemeSayisi(secilenMinMalzeme, secilenMaxMalzeme);
                List<String> secilenSureTarifler = DatabaseFonksiyon.tarifAraHazirlamaSure(secilenSure);
                List<String> secilenTarifAdTarifler = DatabaseFonksiyon.tarifAraAd("");

                // Ortak tariflerin bulunması
                List<String> ortakTarifler = new ArrayList<>(DatabaseFonksiyon.tarifAraAd("")); // İlk listeyi başlangıç olarak alıyoruz
                if (!secilenKategori.equals("Fark etmez")) {
                    ortakTarifler.retainAll(secilenKategoriTarifler);
                }
                ortakTarifler.retainAll(secilenMixMaxTarifler);
                ortakTarifler.retainAll(secilenSureTarifler);
                ortakTarifler.retainAll(secilenTarifAdTarifler);

                gosterListeTarifPanel(ortakTarifler);
            }
        });

        // "Tarif Ekle" butonu
        tarifEkleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gosterTarifEklePanel();
                butonKapat();
            }
        });

        malzemeEkleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gosterMalzemeEklePanel();
                butonKapat();

            }
        });

        duzenleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sonucTablosu.getSelectedRow() != -1) {
                    gosterTarifDuzenlePanel();
                    butonKapat();
                }
                else {
                    JOptionPane.showMessageDialog(null, "Lütfen düzenlemek istediğiniz tarifi seçin.");
                }

            }
        });

        // "Tarif Sil" butonu
        silButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Seçilen satıra ait bilgiyi alma
                int selectedRow = sonucTablosu.getSelectedRow();

                // Herhangi bir satır seçildiyse
                if (selectedRow != -1) {
                    // Seçilen satırdaki tarif adını alma
                    String tarifAdi = sonucTablosu.getValueAt(selectedRow, 0).toString();

                    // Silme işlemini onaylamak için kullanıcıdan onay alma
                    int onay = JOptionPane.showConfirmDialog(
                            null,
                            "Bu tarifi silmek istediğinizden emin misiniz?",
                            "Tarif Silme Onayı",
                            JOptionPane.YES_NO_OPTION
                    );

                    // Eğer kullanıcı onay verirse silme işlemini yapma
                    if (onay == JOptionPane.YES_OPTION) {
                        // Tarif silme fonksiyonunu çağırma
                        boolean silindi = DatabaseFonksiyon.tarifSil(tarifAdi);

                        if (silindi) {
                            // Tabloyu güncelleme
                            ((DefaultTableModel) sonucTablosu.getModel()).removeRow(selectedRow);
                            JOptionPane.showMessageDialog(null, "Tarif silindi.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Tarif silme işlemi başarısız oldu.");
                        }
                    }
                } else {
                    // Eğer hiçbir satır seçilmediyse çalışacak kısım
                    JOptionPane.showMessageDialog(null, "Lütfen silmek istediğiniz tarifi seçin.");
                }
            }
        });
    }

    // Ara butonu tıklandığında tarifleri güncelleyecek liste
    private void gosterListeTarifPanel(List<String> ortakTariflerListe) {
        listePanel.removeAll();
        listePanel.setLayout(new BorderLayout());
        listePanel.setBackground(PANEL_BACKGROUND_COLOR);
        // İsme göre aratma kısmı
        bulPanel = new JPanel();
        bulPanel.setBackground(PANEL_BACKGROUND_COLOR);
        bulButton = new JButton("Bul");
        bulButton.setBackground(BUTTON_BACKGROUND_COLOR);
        bulButton.setForeground(TEXT_COLOR);
        bulTextField = new JTextField();
        bulTextField.setPreferredSize(new Dimension(250, 30));
        siralaLabel = new JLabel("Sırala");
        siralaLabel.setForeground(TEXT_COLOR);
        JComboBox<String> siralaComboBox = new JComboBox<>(new String[]{"Maliyet Azalan","Maliyet Artan","Süre Azalan","Süre Artan"});
        bulPanel.add(siralaLabel);
        bulPanel.add((siralaComboBox));
        bulPanel.add(bulTextField);
        bulPanel.add(bulButton);
        listePanel.add(bulPanel, BorderLayout.NORTH);

        // Tarif isimlerini kullanarak tarif bilgilerini çekme
        List<String[]> veriListesi = new ArrayList<>();
        for(int i = 0; i < ortakTariflerListe.size(); i++) {
            List<String> bulunanTarif = DatabaseFonksiyon.tarifBilgiAra(ortakTariflerListe.get(i));
            String bulunanTarifAd = bulunanTarif.get(1);
            String bulunanKategori = bulunanTarif.get(2);
            String bulunanSure = bulunanTarif.get(3) + " Dakika";
            Float maliyet = DatabaseFonksiyon.tarifMaliyetiHesapla(DatabaseFonksiyon.tarifIDAraIsim(bulunanTarifAd));
            Float maliyetDepoDahil = DatabaseFonksiyon.tarifMaliyetiHesaplaDepoDahil(DatabaseFonksiyon.tarifIDAraIsim(bulunanTarifAd));
            Float eslesmeYuzdesi = DatabaseFonksiyon.malzemeToplamEslesmeYuzdesiHesapla(bulunanTarifAd);

            String[] tarifBilgi = {
                    bulunanTarifAd,
                    bulunanKategori,
                    bulunanSure,
                    maliyet + " TL",
                    maliyetDepoDahil + " TL",
                    eslesmeYuzdesi + "%"};
            veriListesi.add(tarifBilgi);
        }
        // Eşleşme yüzdesine göre azalan sıralama
        veriListesi.sort((a, b) -> {
            // Eşleşme yüzdesini float olarak karşılaştırıyoruz
            Float yuzdeA = Float.parseFloat(a[5].replace("%", "").replace(",", "."));
            Float yuzdeB = Float.parseFloat(b[5].replace("%", "").replace(",", "."));
            return yuzdeB.compareTo(yuzdeA); // Azalan sıralama
        });

        // ArrayList'i tabloya uygun hale getirmek için diziye çevirme
        String[][] veri = veriListesi.toArray(new String[0][]);
        String[] sutunIsimleri = {"Yemek Adı", "Kategori", "Hazırlanma Süresi", "Maliyet", "Size Maliyeti", "Eşleşme Yüzdesi",};
        // Tarif tablosunun editlenebilirliğini kapatmaya yarar
        tarifSonucTablo = new DefaultTableModel(veri, sutunIsimleri) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tüm hücrelerin düzenlenemez olmasını sağlar
            }
        };
        // Tarif tablosu
        sonucTablosu = new JTable(tarifSonucTablo);

        // TableRowSorter kullanarak tabloyu sıralamak için ayarlıyoruz
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tarifSonucTablo) {
            @Override
            public void toggleSortOrder(int column) {
                // Boş bırakılarak tablo başlıklarına tıklama ile sıralamayı devre dışı bırakır
            }
        };
        sonucTablosu.setRowSorter(sorter);

        // Süre sütunu (2. sütun) için Comparator (Dakikayı ayırarak sayısal sıralama)
        sorter.setComparator(2, (o1, o2) -> {
            // "30 Dakika" -> ["30", "Dakika"] şeklinde ayırır ve ilk kısmı int'e dönüştürür
            int sure1 = Integer.parseInt(o1.toString().split(" ")[0]);
            int sure2 = Integer.parseInt(o2.toString().split(" ")[0]);
            return Integer.compare(sure1, sure2);
        });

        // Size Maliyeti sütunu (4. sütun) için Comparator (Sayı olmayan karakterleri kaldırarak sayısal sıralama)
        sorter.setComparator(4, (o1, o2) -> {
            // "50.5 TL" gibi bir değeri "50.5" haline getirir ve float olarak karşılaştırır
            Float maliyet1 = Float.parseFloat(o1.toString().replaceAll("[^\\d.]", ""));
            Float maliyet2 = Float.parseFloat(o2.toString().replaceAll("[^\\d.]", ""));
            return Float.compare(maliyet1, maliyet2);
        });

        // JComboBox için bir dinleyici ekleyerek her seçimde tabloyu sıralayın
        siralaComboBox.addActionListener(e -> {
            String selectedOption = (String) siralaComboBox.getSelectedItem();

            // Sıralama ayarları
            List<RowSorter.SortKey> sortKeys = new ArrayList<>();
            switch (selectedOption) {
                case "Maliyet Azalan":
                    sortKeys.add(new RowSorter.SortKey(4, SortOrder.DESCENDING)); // Size Maliyeti sütunu azalan
                    break;
                case "Maliyet Artan":
                    sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING)); // Size Maliyeti sütunu artan
                    break;
                case "Süre Azalan":
                    sortKeys.add(new RowSorter.SortKey(2, SortOrder.DESCENDING)); // Süre sütunu azalan
                    break;
                case "Süre Artan":
                    sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING)); // Süre sütunu artan
                    break;
            }
            sorter.setSortKeys(sortKeys); // Seçilen sıralama ayarını tabloya uygula
            sorter.sort(); // Tabloyu yeniden sıralayın
        });

        // Tarif tablosu renklendirme işlemleri
        sonucTablosu.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component hucre = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Seçili satır için arka plan rengi ve kenarlık
                if (isSelected) {
                    hucre.setBackground(new Color(173, 216, 230)); // Açık mavi arka plan
                    ((JComponent) hucre).setBorder(BorderFactory.createLineBorder(Color.BLUE, 2)); // Mavi kenarlık
                } else {
                    // Diğer hücreler için renklendirme (5. sütuna göre)
                    String yuzdeStr = (String) table.getValueAt(row, 5);
                    float yuzde = Float.parseFloat(yuzdeStr.replace("%", "").replace(",", "."));

                    if (yuzde >= minimumEslesmeYuzdesi) {
                        hucre.setBackground(new Color(144, 238, 144)); // Açık yeşil
                    } else {
                        hucre.setBackground(new Color(255, 182, 193)); // Açık kırmızı
                    }
                    ((JComponent) hucre).setBorder(null); // Seçili olmayan hücrelerin kenarlığını kaldırma
                }

                return hucre;
            }
        });

        // Tablodan bir satır seçildiğinde bilgileri yazdırmak için listener ekleme
        sonucTablosu.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && sonucTablosu.getSelectedRow() != -1) {
                int selectedRow = sonucTablosu.getSelectedRow();

                // sütün 0 tarifAdi
                // sütün 1 Kategorisi
                // sütün 2 hazirlanma süresi
                // sütün 3 maliyet
                // sütün 4 maliyet bize
                // sütün 5 eşleşme yüzdesi
                String tarifIsim = (String) sonucTablosu.getValueAt(selectedRow, 0);
                String tarifKategori = (String) sonucTablosu.getValueAt(selectedRow, 1);
                String tarifSure = (String) sonucTablosu.getValueAt(selectedRow, 2);
                Map<String, Float> tarifMalzemeMiktarlar = DatabaseFonksiyon.malzemeleriVeMiktarlariBul(tarifIsim);
                // Her bir malzemeyi ve miktarını stringe ekleme
                StringBuilder malzemeListesi = new StringBuilder();
                int sayac = 1;
                for (Map.Entry<String, Float> entry : tarifMalzemeMiktarlar.entrySet()) {
                    malzemeListesi.append(sayac + ". ")
                            .append(entry.getKey())
                            .append("\t\tMiktar: ")
                            .append(entry.getValue() + " ")
                            .append(DatabaseFonksiyon.malzemeBirimBul(entry.getKey()))
                            .append("\n");
                    sayac++;
                }
                String tarifTalimatlar = DatabaseFonksiyon.tarifBilgiAra(tarifIsim).get(4);
                tarifDetayiTextArea.setText(
                        "Tarif Adı: \t\t" + tarifIsim + "\n" +
                                "Tarif Kategorisi: \t" + tarifKategori + "\n" +
                                "Tarif Hazırlanma Süresi: \t" + tarifSure + "\n\n" +
                                "Malzemeler:\n" + malzemeListesi + "\n" +
                                "Tarif Talimatları: \n" + tarifTalimatlar + "\n"

                );
            }
        });
        sonucTablosu.setBackground(TABLE_BACKGROUND_COLOR);
        sonucTablosu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Sadece 1 tane tarifin seçilebilir olmasını sağlar
        listePanel.add(new JScrollPane(sonucTablosu), BorderLayout.CENTER);
        listePanel.revalidate();
        listePanel.repaint();

        // "Bul" butonu
        bulButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {// Kategori Bilgisi
                String secilenKategori = kategoriComboBox.getItemAt(kategoriComboBox.getSelectedIndex());

                // Süre Bilgisi
                String secilenSureText = sureComboBox.getItemAt(sureComboBox.getSelectedIndex());
                Integer secilenSure = 999;
                if (secilenSureText.equals("Fark etmez") || secilenSureText.equals("60+ dakika")) {
                    secilenSure = 999;
                }
                else {
                    secilenSure = Integer.parseInt(secilenSureText.split(" ")[0]) ;
                }

                // Malzeme min-max Bilgisi
                Integer secilenMinMalzeme = 0;
                Integer secilenMaxMalzeme = 999;

                // 230201127 Selim Eren Kaya
                // 220201045 Bilge Çeşme
                if (!minMalzemeField.getText().equals("")) {
                    secilenMinMalzeme =  Integer.parseInt(minMalzemeField.getText());
                }
                if (!maxMalzemeField.getText().equals("")) {
                    secilenMaxMalzeme =  Integer.parseInt(maxMalzemeField.getText());
                }

                String secilenTarifAd = bulTextField.getText();

                // Her bir filtrleme için tarif listelenmesi
                List<String> secilenKategoriTarifler = DatabaseFonksiyon.tarifAraKategori(secilenKategori);
                List<String> secilenMixMaxTarifler = DatabaseFonksiyon.tarifAraMalzemeSayisi(secilenMinMalzeme, secilenMaxMalzeme);
                List<String> secilenSureTarifler = DatabaseFonksiyon.tarifAraHazirlamaSure(secilenSure);
                List<String> secilenTarifAdTarifler = DatabaseFonksiyon.tarifAraAd(secilenTarifAd);

                // Ortak tariflerin bulunması
                List<String> ortakTarifler = new ArrayList<>(DatabaseFonksiyon.tarifAraAd("")); // İlk listeyi başlangıç olarak alıyoruz
                if (!secilenKategori.equals("Fark etmez")) {
                    ortakTarifler.retainAll(secilenKategoriTarifler);
                }
                ortakTarifler.retainAll(secilenMixMaxTarifler);
                ortakTarifler.retainAll(secilenSureTarifler);
                ortakTarifler.retainAll(secilenTarifAdTarifler);

                gosterListeTarifPanel(ortakTarifler);
            }
        });
    }

    private void gosterMalzemeEklePanel() {
        malzemeEklePanel.setLayout(new GridBagLayout());

        malzemeEklePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Panel Başlığı
        TitledBorder border1 = BorderFactory.createTitledBorder("Malzeme Ekle Paneli");
        border1.setTitleColor(TEXT_COLOR);
        malzemeEklePanel.setBorder(border1);

        // Malzeme Ad
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel isimLabel = new JLabel("Malzeme Ad:");
        isimLabel.setForeground(TEXT_COLOR);
        malzemeEklePanel.add(isimLabel, gbc);

        gbc.gridx = 1;
        JTextField isimField = new JTextField(15);
        malzemeEklePanel.add(isimField, gbc);

        // Malzeme Birim
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel birimLabel = new JLabel("Malzeme Birim:");
        birimLabel.setForeground(TEXT_COLOR);
        malzemeEklePanel.add(birimLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> birimComboBox = new JComboBox<>(new String[]
                {"Adet", "Demet", "Gram", "Kilogram",
                "Konserve", " Litre", "Paket", "Şişe"});
        malzemeEklePanel.add(birimComboBox, gbc);

        // Malzeme Birim Fiyat
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel birimFiyatLabel = new JLabel("Malzeme Birim Fiyat:");
        birimFiyatLabel.setForeground(TEXT_COLOR);
        malzemeEklePanel.add(birimFiyatLabel, gbc);

        gbc.gridx = 1;
        JTextField birimFiyatField = new JTextField(15);
        malzemeEklePanel.add(birimFiyatField, gbc);

        // Kaydet Butonu
        gbc.gridy = 4; // Bir alt satıra geç
        JButton kaydetButton = new JButton("Malzemeyi Ekle");
        kaydetButton.setBackground(BUTTON_ACCEPT_COLOR);
        kaydetButton.setForeground(TEXTFIELD_BACKGROUND_COLOR);
        malzemeEklePanel.add(kaydetButton, gbc);

        // Kaydet Butonu İşlevi
        kaydetButton.addActionListener(e -> {
            try {
                if (isimField.getText().isEmpty()) {
                    throw new Exception("Malzeme ismi boş bırakılamaz.");
                }
                else if (birimFiyatField.getText().isEmpty())
                {
                    throw new Exception("Malzeme birim fiyatı boş bırakılamaz.");
                }
                Malzeme yeniMalzeme = new Malzeme(
                        999,
                        isimField.getText(),
                        "0",
                        birimComboBox.getItemAt(birimComboBox.getSelectedIndex()),
                        Integer.parseInt(birimFiyatField.getText()));
                boolean basarili =  DatabaseFonksiyon.insertMalzeme(yeniMalzeme);
                if(!basarili) {
                    throw new Exception("Malzeme zaten mevcut.");
                }
                butonAktiflestir();
                listeyiGuncelle();

                // Paneli temizleyip yeniden çizdirme
                malzemeEklePanel.removeAll();
                malzemeEklePanel.revalidate();
                malzemeEklePanel.repaint();
                JOptionPane.showMessageDialog(null, "Malzeme başarıyla eklendi.");
            } catch (Exception hata) {
                JOptionPane.showMessageDialog(null, "Malzeme eklenemedi. Hata: " + hata.getMessage());
            }

        });

        // İptal Butonu
        gbc.gridy = 5;
        JButton iptalButton = new JButton("İptal");
        iptalButton.setBackground(BUTTON_BACKGROUND_COLOR);
        iptalButton.setForeground(TEXTFIELD_BACKGROUND_COLOR);
        malzemeEklePanel.add(iptalButton, gbc);

        // İptal Butonu İşlevi
        iptalButton.addActionListener(e -> {
            butonAktiflestir();
            listeyiGuncelle();

            malzemeEklePanel.removeAll();
            malzemeEklePanel.revalidate();
            malzemeEklePanel.repaint();
        });

        malzemeEklePanel.revalidate();
        malzemeEklePanel.repaint();
    }

    private void gosterTarifEklePanel() {
        malzemeEklePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Panel Başlığı
        TitledBorder border1 = BorderFactory.createTitledBorder("Tarif Ekle");
        border1.setTitleColor(TEXT_COLOR);
        malzemeEklePanel.setBorder(border1);

        // İsim
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel isimLabel = new JLabel("İsim:");
        isimLabel.setForeground(TEXT_COLOR);
        malzemeEklePanel.add(isimLabel, gbc);

        gbc.gridx = 1;
        JTextField isimField = new JTextField(25);
        malzemeEklePanel.add(isimField, gbc);

        // Kategori
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel kategoriLabel = new JLabel("Kategori:");
        kategoriLabel.setForeground(TEXT_COLOR);
        malzemeEklePanel.add(kategoriLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> kategoriComboBox = new JComboBox<>(new String[]{"Ana Yemek", "Kahvaltı", "Atıştırmalık",
                "Çorba", "Meze", "Hamur İşi", "Salata", "Tatlı", "İçecek"});
        malzemeEklePanel.add(kategoriComboBox, gbc);

        // Hazırlama Süresi
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel sureLabel = new JLabel("Süre:");
        sureLabel.setForeground(TEXT_COLOR);
        malzemeEklePanel.add(sureLabel, gbc);

        gbc.gridx = 1;
        JTextField sureField = new JTextField(10);
        malzemeEklePanel.add(sureField, gbc);

        // Malzeme Arama
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel malzemeAramaLabel = new JLabel("Malzeme Ara:");
        malzemeAramaLabel.setForeground(TEXT_COLOR);
        malzemeEklePanel.add(malzemeAramaLabel, gbc);

        gbc.gridx = 1;
        JTextField malzemeAramaField = new JTextField(15);
        malzemeEklePanel.add(malzemeAramaField, gbc);

        // Malzemeler Listesi
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel malzemelerLabel = new JLabel("Malzemeler:");
        malzemelerLabel.setForeground(TEXT_COLOR);
        malzemeEklePanel.add(malzemelerLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        checkboxPanel.setBackground(PANEL_BACKGROUND_COLOR);

        JScrollPane malzemeScrollPane = new JScrollPane(checkboxPanel);
        malzemeScrollPane.setPreferredSize(new Dimension(250, 100));
        malzemeEklePanel.add(malzemeScrollPane, gbc);

        List<String> malzemelerAd = DatabaseFonksiyon.malzemeAraAd("");
        malzemeCheckboxlar = new JCheckBox[malzemelerAd.size()];
        miktarTextFieldlar = new JTextField[malzemelerAd.size()];

        // Malzeme checkboxlarını ve miktar textField'larını ekle
        for (int i = 0; i < malzemelerAd.size(); i++) {
            JPanel malzemeSatiri = new JPanel(new BorderLayout());
            malzemeSatiri.setBackground(PANEL_BACKGROUND_COLOR);

            malzemeCheckboxlar[i] = new JCheckBox(malzemelerAd.get(i));
            malzemeCheckboxlar[i].setBackground(PANEL_BACKGROUND_COLOR);
            malzemeCheckboxlar[i].setForeground(TEXT_COLOR);

            miktarTextFieldlar[i] = new JTextField(6);
            miktarTextFieldlar[i].setVisible(false);
            malzemeSatiri.add(malzemeCheckboxlar[i], BorderLayout.WEST);
            malzemeSatiri.add(miktarTextFieldlar[i], BorderLayout.EAST);

            int finalI = i;
            malzemeCheckboxlar[i].addItemListener(e -> {
                miktarTextFieldlar[finalI].setVisible(malzemeCheckboxlar[finalI].isSelected());

                final JTextField textField = miktarTextFieldlar[finalI];
                final String malzemeBirim = DatabaseFonksiyon.malzemeBirimBul(malzemeCheckboxlar[finalI].getText());

                // TextField’a başlangıçta "Search" yazısını ve rengi gri olarak ayarla
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(malzemeBirim);
                }

                // FocusListener ekle
                textField.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        // Focus kazanıldığında sadece placeholder metni varsa temizle
                        if (textField.getText().equals(malzemeBirim)) {
                            textField.setText("");
                            textField.setForeground(Color.BLACK);
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        // Focus kaybedildiğinde TextField boşsa placeholder metnini geri getir
                        if (textField.getText().isEmpty()) {
                            textField.setForeground(Color.GRAY);
                            textField.setText(malzemeBirim);
                        }
                    }
                });

                // DocumentListener ekle (Gri placeholder rengini kontrol etmek için)
                textField.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        updateTextColor();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        updateTextColor();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        updateTextColor();
                    }

                    private void updateTextColor() {
                        // Eğer placeholder yazısı yoksa yazıyı siyah yap
                        if (!textField.getText().equals(malzemeBirim)) {
                            textField.setForeground(Color.BLACK);
                        }
                    }
                });

                checkboxPanel.revalidate();
                checkboxPanel.repaint();
            });

            checkboxPanel.add(malzemeSatiri);
        }

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Arama alanı için DocumentListener ekle
        malzemeAramaField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtreMalzemeler(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtreMalzemeler(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtreMalzemeler(); }

            private void filtreMalzemeler() {
                String aramaMetni = malzemeAramaField.getText().toLowerCase();
                for (int i = 0; i < malzemeCheckboxlar.length; i++) {
                    String malzemeAdi = malzemeCheckboxlar[i].getText().toLowerCase();
                    boolean eslesme = malzemeAdi.contains(aramaMetni);
                    boolean secili = malzemeCheckboxlar[i].isSelected();
                    // Eğer malzeme arama metnine uyuyorsa veya seçiliyse görünür tut
                    malzemeCheckboxlar[i].getParent().setVisible(eslesme || secili);
                }
                checkboxPanel.revalidate();
                checkboxPanel.repaint();
            }
        });

        // Talimatlar
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel talimatLabel = new JLabel("Talimatlar:");
        talimatLabel.setForeground(TEXT_COLOR);
        malzemeEklePanel.add(talimatLabel, gbc);

        gbc.gridx = 1;
        JTextArea talimatField = new JTextArea();
        talimatField.setLineWrap(true); // Satır sarmalama ayarları 1
        talimatField.setWrapStyleWord(true); // Satır sarmalama ayarları 2

        // JTextArea'yı JScrollPane içine alıyoruz
        JScrollPane talimatScrollPane = new JScrollPane(talimatField);
        talimatScrollPane.setPreferredSize(new Dimension(talimatField.getPreferredSize().width, 100));
        malzemeEklePanel.add(talimatScrollPane, gbc);

        // Güncelle Butonu
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton guncelleButton = new JButton("Tarifi Ekle");
        guncelleButton.setBackground(BUTTON_ACCEPT_COLOR);
        guncelleButton.setForeground(TEXTFIELD_BACKGROUND_COLOR);
        malzemeEklePanel.add(guncelleButton, gbc);

        // Güncelle Butonu İşlevi
        guncelleButton.addActionListener(e -> {
            // Database'e yeni tarifi ekleme
            // Bilgileri alma
            try {
                String tarifAd = isimField.getText();
                String tarifKategori = kategoriComboBox.getItemAt(kategoriComboBox.getSelectedIndex());
                String tarifSure = sureField.getText();
                String tarifTalimat = talimatField.getText();

                ArrayList<String> seciliMalzemeIsimleri = new ArrayList<>();
                ArrayList<Float> seciliMalzemeMiktarlari = new ArrayList<>();
                for (int i = 0; i < malzemeCheckboxlar.length; i++) {
                    if (malzemeCheckboxlar[i].isSelected()) {
                        if (!miktarTextFieldlar[i].getText().matches("[0-9]+(\\.[0-9]+)?") ){
                            throw new Exception("Malzeme miktarları sayı olmalı.");
                        }

                        seciliMalzemeIsimleri.add(malzemeCheckboxlar[i].getText());
                        seciliMalzemeMiktarlari.add(Float.parseFloat(miktarTextFieldlar[i].getText()));
                    }
                }
                if (tarifAd.isEmpty()) {
                    throw new Exception("Tarif adı boş.");
                }
                else if (tarifSure.isEmpty()) {
                    throw new Exception("Tarif süresi boş.");
                }
                else if (!tarifSure.matches("[0-9]+") ){
                    throw new Exception("Tarif süresi sayı olmalı.");
                }
                else if (seciliMalzemeIsimleri.isEmpty()) {
                    throw new Exception("En az 1 malzeme seçilmeli.");
                }

                Tarif yeniTarif = new Tarif(999, tarifAd, tarifKategori, Integer.parseInt(tarifSure), tarifTalimat);
                boolean basarili = DatabaseFonksiyon.insertTarif(yeniTarif, seciliMalzemeIsimleri, seciliMalzemeMiktarlari);

                if (!basarili) {
                    throw new Exception("Tarif zaten mevcut.");
                }
                butonAktiflestir();
                listeyiGuncelle();

                malzemeEklePanel.removeAll();
                malzemeEklePanel.revalidate();
                malzemeEklePanel.repaint();
                JOptionPane.showMessageDialog(null, "Tarif başarıyla eklendi.");

            } catch (Exception hata) {
                JOptionPane.showMessageDialog(null, "Tarif eklenemedi. Hata: " + hata.getMessage());
            }
        });

        // İptal Butonu
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton iptalButton = new JButton("İptal");
        iptalButton.setBackground(BUTTON_BACKGROUND_COLOR);
        iptalButton.setForeground(TEXTFIELD_BACKGROUND_COLOR);
        malzemeEklePanel.add(iptalButton, gbc);

        // İptal Butonu İşlevi
        iptalButton.addActionListener(e -> {
            butonAktiflestir();
            listeyiGuncelle();

            malzemeEklePanel.removeAll();
            malzemeEklePanel.revalidate();
            malzemeEklePanel.repaint();
        });

        malzemeEklePanel.revalidate();
        malzemeEklePanel.repaint();
    }

    // Tarif Düzenleme panelini yöneten fonksiyon
    private void gosterTarifDuzenlePanel() {
        // Arayüzü oluşturma
        malzemeEklePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Panel Başlığı
        TitledBorder border1 = BorderFactory.createTitledBorder("Tarif Düzenle");
        border1.setTitleColor(TEXT_COLOR);
        malzemeEklePanel.setBorder(border1);

        // İsim
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel isimLabel = new JLabel("İsim:");
        isimLabel.setForeground(TEXT_COLOR);
        malzemeEklePanel.add(isimLabel, gbc);

        gbc.gridx = 1;
        JTextField isimField = new JTextField(25);
        malzemeEklePanel.add(isimField, gbc);

        // Kategori
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel kategoriLabel = new JLabel("Kategori:");
        // 230201127 Selim Eren Kaya
        // 220201045 Bilge Çeşme
        kategoriLabel.setForeground(TEXT_COLOR);
        malzemeEklePanel.add(kategoriLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> kategoriComboBox = new JComboBox<>(new String[]{"Fark etmez", "Ana Yemek", "Kahvaltı", "Atıştırmalık",
                "Çorba", "Meze", "Hamur İşi", "Salata", "Tatlı", "İçecek"});
        malzemeEklePanel.add(kategoriComboBox, gbc);

        // Hazırlama Süresi
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel sureLabel = new JLabel("Süre:");
        sureLabel.setForeground(TEXT_COLOR);
        malzemeEklePanel.add(sureLabel, gbc);

        gbc.gridx = 1;
        JTextField sureField = new JTextField(10);
        malzemeEklePanel.add(sureField, gbc);

        // Malzeme Arama
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel malzemeAramaLabel = new JLabel("Malzeme Ara:");
        malzemeAramaLabel.setForeground(TEXT_COLOR);
        malzemeEklePanel.add(malzemeAramaLabel, gbc);

        gbc.gridx = 1;
        JTextField malzemeAramaField = new JTextField(15);
        malzemeEklePanel.add(malzemeAramaField, gbc);

        // Malzemeler Listesi
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel malzemelerLabel = new JLabel("Malzemeler:");
        malzemelerLabel.setForeground(TEXT_COLOR);
        malzemeEklePanel.add(malzemelerLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        checkboxPanel.setBackground(PANEL_BACKGROUND_COLOR);

        JScrollPane malzemeScrollPane = new JScrollPane(checkboxPanel);
        malzemeScrollPane.setPreferredSize(new Dimension(250, 100));
        malzemeEklePanel.add(malzemeScrollPane, gbc);

        List<String> malzemelerAd = DatabaseFonksiyon.malzemeAraAd("");
        malzemeCheckboxlar = new JCheckBox[malzemelerAd.size()];
        miktarTextFieldlar = new JTextField[malzemelerAd.size()];

        // Malzeme checkboxlarını ve miktar textField'larını ekle
        for (int i = 0; i < malzemelerAd.size(); i++) {
            JPanel malzemeSatiri = new JPanel(new BorderLayout());
            malzemeSatiri.setBackground(PANEL_BACKGROUND_COLOR);

            malzemeCheckboxlar[i] = new JCheckBox(malzemelerAd.get(i));
            malzemeCheckboxlar[i].setBackground(PANEL_BACKGROUND_COLOR);
            malzemeCheckboxlar[i].setForeground(TEXT_COLOR);

            miktarTextFieldlar[i] = new JTextField(6);
            miktarTextFieldlar[i].setVisible(false);
            malzemeSatiri.add(malzemeCheckboxlar[i], BorderLayout.WEST);
            malzemeSatiri.add(miktarTextFieldlar[i], BorderLayout.EAST);

            int finalI = i;
            malzemeCheckboxlar[i].addItemListener(e -> {
                miktarTextFieldlar[finalI].setVisible(malzemeCheckboxlar[finalI].isSelected());

                final JTextField textField = miktarTextFieldlar[finalI];
                final String malzemeBirim = DatabaseFonksiyon.malzemeBirimBul(malzemeCheckboxlar[finalI].getText());

                // TextField’a başlangıçta "Search" yazısını ve rengi gri olarak ayarla
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(malzemeBirim);
                }

                // FocusListener ekle
                textField.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        // Focus kazanıldığında sadece placeholder metni varsa temizle
                        if (textField.getText().equals(malzemeBirim)) {
                            textField.setText("");
                            textField.setForeground(Color.BLACK);
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        // Focus kaybedildiğinde TextField boşsa placeholder metnini geri getir
                        if (textField.getText().isEmpty()) {
                            textField.setForeground(Color.GRAY);
                            textField.setText(malzemeBirim);
                        }
                    }
                });

                // DocumentListener ekle (Gri placeholder rengini kontrol etmek için)
                textField.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        updateTextColor();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        updateTextColor();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        updateTextColor();
                    }

                    private void updateTextColor() {
                        // Eğer placeholder yazısı yoksa yazıyı siyah yap
                        if (!textField.getText().equals(malzemeBirim)) {
                            textField.setForeground(Color.BLACK);
                        }
                    }
                });

                checkboxPanel.revalidate();
                checkboxPanel.repaint();
            });

            checkboxPanel.add(malzemeSatiri);

        }

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Arama alanı için DocumentListener ekle
        malzemeAramaField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtreMalzemeler(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtreMalzemeler(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtreMalzemeler(); }

            private void filtreMalzemeler() {
                String aramaMetni = malzemeAramaField.getText().toLowerCase();
                for (int i = 0; i < malzemeCheckboxlar.length; i++) {
                    String malzemeAdi = malzemeCheckboxlar[i].getText().toLowerCase();
                    boolean eslesme = malzemeAdi.contains(aramaMetni);
                    boolean secili = malzemeCheckboxlar[i].isSelected();
                    // Eğer malzeme arama metnine uyuyorsa veya seçiliyse görünür tut
                    malzemeCheckboxlar[i].getParent().setVisible(eslesme || secili);
                }
                checkboxPanel.revalidate();
                checkboxPanel.repaint();
            }
        });

        // Talimatlar
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel talimatLabel = new JLabel("Talimatlar:");
        talimatLabel.setForeground(TEXT_COLOR);
        malzemeEklePanel.add(talimatLabel, gbc);

        gbc.gridx = 1;
        JTextArea talimatField = new JTextArea();
        talimatField.setLineWrap(true); // Satır sarmalama ayarları 1
        talimatField.setWrapStyleWord(true); // Satır sarmalama ayarları 2

        // JTextArea'yı JScrollPane içine alıyoruz
        JScrollPane talimatScrollPane = new JScrollPane(talimatField);
        talimatScrollPane.setPreferredSize(new Dimension(talimatField.getPreferredSize().width, 100));
        malzemeEklePanel.add(talimatScrollPane, gbc);

        // Seçilen tarife ait bilgileri çekme
        int selectedRow = sonucTablosu.getSelectedRow();
        // sütün 0 tarifAdi
        // sütün 1 Kategorisi
        // sütün 2 hazirlanma süresi
        // sütün 3 maliyet
        // sütün 4 maliyet bize
        // sütün 5 eşleşme yüzdesi
        String tabloTarifIsim = (String) sonucTablosu.getValueAt(selectedRow, 0);
        String tabloTarifKategori = (String) sonucTablosu.getValueAt(selectedRow, 1);
        String tabloTarifSure = (String) sonucTablosu.getValueAt(selectedRow, 2);
        Map<String, Float> tabloTarifMalzemeMiktarlar = DatabaseFonksiyon.malzemeleriVeMiktarlariBul(tabloTarifIsim);
        String tabloTarifTalimatlar = DatabaseFonksiyon.tarifBilgiAra(tabloTarifIsim).get(4);
        int tarifID = DatabaseFonksiyon.tarifIDAraIsim(tabloTarifIsim);

        // Bilgilerin yerleştirilmesi
        isimField.setText(tabloTarifIsim);
        kategoriComboBox.setSelectedItem(tabloTarifKategori);
        sureField.setText(tabloTarifSure.split(" ")[0]);
        talimatField.setText(tabloTarifTalimatlar);
        for (Map.Entry<String, Float> entry : tabloTarifMalzemeMiktarlar.entrySet()) {
            String malzemeAdi = entry.getKey();
            Float miktar = entry.getValue();

            for (int i = 0; i < malzemeCheckboxlar.length; i++) {
                if (malzemeCheckboxlar[i].getText().equals(malzemeAdi)) {
                    // Malzeme checkbox'ını işaretle
                    malzemeCheckboxlar[i].setSelected(true);

                    // İlgili miktarı textField'a yazdır ve görünür hale getir
                    miktarTextFieldlar[i].setText(miktar.toString());
                    miktarTextFieldlar[i].setVisible(true);
                    break; // Malzeme bulunduğunda döngüyü kırma
                }
            }
        }


        // Güncelle Butonu
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton guncelleButton = new JButton("Tarifi Güncelle");
        guncelleButton.setBackground(BUTTON_ACCEPT_COLOR);
        guncelleButton.setForeground(TEXTFIELD_BACKGROUND_COLOR);
        malzemeEklePanel.add(guncelleButton, gbc);

        // Güncelle Butonu İşlevi
        guncelleButton.addActionListener(e -> {
            try {
                String tarifAd = isimField.getText();
                String tarifKategori = kategoriComboBox.getItemAt(kategoriComboBox.getSelectedIndex());
                String tarifSure = sureField.getText();
                String tarifTalimat = talimatField.getText();

                ArrayList<String> seciliMalzemeIsimleri = new ArrayList<>();
                ArrayList<Float> seciliMalzemeMiktarlari = new ArrayList<>();
                for (int i = 0; i < malzemeCheckboxlar.length; i++) {
                    if (malzemeCheckboxlar[i].isSelected()) {
                        if (!miktarTextFieldlar[i].getText().matches("[0-9]+(\\.[0-9]+)?") ){
                            throw new Exception("Malzeme miktarları sayı olmalı.");
                        }

                        seciliMalzemeIsimleri.add(malzemeCheckboxlar[i].getText());
                        seciliMalzemeMiktarlari.add(Float.parseFloat(miktarTextFieldlar[i].getText()));
                    }
                }
                if (tarifAd.isEmpty()) {
                    throw new Exception("Tarif adı boş.");
                }
                else if (tarifSure.isEmpty()) {
                    throw new Exception("Tarif süresi boş.");
                }
                else if (!tarifSure.matches("[0-9]+") ){
                    throw new Exception("Tarif süresi sayı olmalı.");
                }
                else if (seciliMalzemeIsimleri.isEmpty()) {
                    throw new Exception("En az 1 malzeme seçilmeli.");
                }

                Tarif yeniTarif = new Tarif(999, tarifAd, tarifKategori, Integer.parseInt(tarifSure), tarifTalimat);
                boolean basarili = DatabaseFonksiyon.updateTarif(tarifID, yeniTarif, seciliMalzemeIsimleri, seciliMalzemeMiktarlari);

                if (!basarili) {
                    throw new Exception("Aynı isme sahip tarif zaten mevcut.");
                }
                butonAktiflestir();
                listeyiGuncelle();

                malzemeEklePanel.removeAll();
                malzemeEklePanel.revalidate();
                malzemeEklePanel.repaint();
                JOptionPane.showMessageDialog(null, "Tarif başarıyla güncellendi.");

            } catch (Exception hata) {
                JOptionPane.showMessageDialog(null, "Tarif güncellenemedi. Hata: " + hata.getMessage());
            }
        });

        // İptal Butonu
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton iptalButton = new JButton("İptal");
        iptalButton.setBackground(BUTTON_BACKGROUND_COLOR);
        iptalButton.setForeground(TEXTFIELD_BACKGROUND_COLOR);
        malzemeEklePanel.add(iptalButton, gbc);

        // İptal Butonu İşlevi
        iptalButton.addActionListener(e -> {
            butonAktiflestir();
            listeyiGuncelle();

            malzemeEklePanel.removeAll();
            malzemeEklePanel.revalidate();
            malzemeEklePanel.repaint();
        });

        malzemeEklePanel.revalidate();
        malzemeEklePanel.repaint();
    }

    // Malzemeleri Yenileme Fonksiyonu
    private void malzemeleriYenile() {
        // Mevcut malzeme bileşenlerini kaldır
        checkboxPanel.removeAll();

        // Güncel malzeme listesi ve checkbox bileşenlerini oluştur
        List<String> guncelMalzemeListesi = DatabaseFonksiyon.malzemeAraAd(""); // Veritabanından güncel malzeme listesini al
        malzemeCheckboxlar = new JCheckBox[guncelMalzemeListesi.size()];
        miktarTextFieldlar = new JTextField[guncelMalzemeListesi.size()];

        for (int i = 0; i < guncelMalzemeListesi.size(); i++) {
            JPanel malzemeSatiri = new JPanel(new BorderLayout());
            malzemeSatiri.setBackground(PANEL_BACKGROUND_COLOR);

            malzemeCheckboxlar[i] = new JCheckBox(guncelMalzemeListesi.get(i));
            malzemeCheckboxlar[i].setBackground(PANEL_BACKGROUND_COLOR);
            malzemeCheckboxlar[i].setForeground(TEXT_COLOR);

            // Miktar için Label ve TextField
            JLabel miktarLabel = new JLabel("Miktar: ");
            miktarLabel.setForeground(TEXT_COLOR);
            miktarLabel.setVisible(false); // Başlangıçta görünmesin

            miktarTextFieldlar[i] = new JTextField(6); // Daha kısa TextField
            miktarTextFieldlar[i].setVisible(false);

            // Miktar Label ve TextField'ı tek bir panelde toplama
            JPanel miktarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            miktarPanel.setBackground(PANEL_BACKGROUND_COLOR);
            miktarPanel.add(miktarLabel);
            miktarPanel.add(miktarTextFieldlar[i]);

            malzemeSatiri.add(malzemeCheckboxlar[i], BorderLayout.WEST);
            malzemeSatiri.add(miktarPanel, BorderLayout.EAST);

            // Satırların arasına boşluk eklemek için
            malzemeSatiri.setBorder(new EmptyBorder(5, 0, 5, 0)); // Üst ve alt boşluk 5 piksel

            malzemeCheckboxlar[i].addItemListener(e -> {
                JCheckBox checkbox = (JCheckBox) e.getSource();
                for (int j = 0; j < malzemeCheckboxlar.length; j++) {
                    if (checkbox == malzemeCheckboxlar[j]) {
                        boolean isSelected = checkbox.isSelected();
                        miktarLabel.setVisible(isSelected);       // Checkbox seçiliyse göster
                        miktarTextFieldlar[j].setVisible(isSelected);

                        final JTextField textField = miktarTextFieldlar[j];
                        final String malzemeBirim = DatabaseFonksiyon.malzemeBirimBul(checkbox.getText());

                        // TextField’a başlangıçta "Search" yazısını ve rengi gri olarak ayarla
                        if (textField.getText().isEmpty()) {
                            textField.setForeground(Color.GRAY);
                            textField.setText(malzemeBirim);
                        }

                        // FocusListener ekle
                        textField.addFocusListener(new FocusListener() {
                            @Override
                            public void focusGained(FocusEvent e) {
                                // Focus kazanıldığında sadece placeholder metni varsa temizle
                                if (textField.getText().equals(malzemeBirim)) {
                                    textField.setText("");
                                    textField.setForeground(Color.BLACK);
                                }
                            }

                            @Override
                            public void focusLost(FocusEvent e) {
                                // Focus kaybedildiğinde TextField boşsa placeholder metnini geri getir
                                if (textField.getText().isEmpty()) {
                                    textField.setForeground(Color.GRAY);
                                    textField.setText(malzemeBirim);
                                }
                            }
                        });

                        // DocumentListener ekle (Gri placeholder rengini kontrol etmek için)
                        textField.getDocument().addDocumentListener(new DocumentListener() {
                            @Override
                            public void insertUpdate(DocumentEvent e) {
                                updateTextColor();
                            }

                            @Override
                            public void removeUpdate(DocumentEvent e) {
                                updateTextColor();
                            }

                            @Override
                            public void changedUpdate(DocumentEvent e) {
                                updateTextColor();
                            }

                            private void updateTextColor() {
                                // Eğer placeholder yazısı yoksa yazıyı siyah yap
                                if (!textField.getText().equals(malzemeBirim)) {
                                    textField.setForeground(Color.BLACK);
                                }
                            }
                        });
                        break;
                    }
                }
                checkboxPanel.revalidate();
                checkboxPanel.repaint();
            });

            checkboxPanel.add(malzemeSatiri);
        }

        // Paneli yeniden çiz
        checkboxPanel.revalidate();
        checkboxPanel.repaint();
    }


    // Butonları aktifleştirmeye yarayan fonksiyon
    private void butonAktiflestir() {
        tariflerButton.setEnabled(true);
        duzenleButton.setEnabled(true);
        silButton.setEnabled(true);
        tarifEkleButton.setEnabled(true);
        malzemeEkleButton.setEnabled(true);
    }

    // Butonları kapatmaya yarayan fonksiyon
    private void butonKapat() {
        tariflerButton.setEnabled(false);
        duzenleButton.setEnabled(false);
        silButton.setEnabled(false);
        tarifEkleButton.setEnabled(false);
        malzemeEkleButton.setEnabled(false);
    }

    // Tarif listesini güncelleyen fonksiyon
    private void listeyiGuncelle() {
        malzemeleriYenile();
        gosterListeTarifPanel(DatabaseFonksiyon.tarifAraAd(""));
    }

    private void setComponentColors(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JPanel) {
                component.setBackground(PANEL_BACKGROUND_COLOR);
                setComponentColors((Container) component);
            } else if (component instanceof JLabel || component instanceof JButton || component instanceof JTextField || component instanceof JCheckBox) {
                component.setForeground(TEXT_COLOR);
                component.setBackground(PANEL_BACKGROUND_COLOR);
            }
        }
    }

}
