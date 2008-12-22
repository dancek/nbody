package dancek.nbody;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;

/**
 * Tämä luokka sisältää käyttöliittymän sivupaneelin. HUOM: LUOKKA SISÄLTÄÄ
 * AUTOMAATTISESTI GENEROITUA KOODIA. Tarkemmin sanottuna initComponents-metodi
 * ja siihen liittyvät komponenttiattribuutit ovat NetBeans 6.1:n graafisen
 * GUI-editorin tuotosta. Olen toki käyttänyt editoria itse (mikä vaatii kyllä
 * myöskin vaivannäköä). Tapahtumankäsittelijämetodit ja muu luokan logiikka on
 * kirjoitettu käsin. Event-olioita välitetään metodeille, vaikkei se olisikaan
 * tarpeen, koska sen muotoiset metodit oli helppo liittää generoituun koodiin
 * (ja voihan Event-olioista olla myöhemmin hyötyä muutenkin).
 * 
 * Automaattisesti generoitu koodi on sijoitettu luokan loppuun.
 * 
 * @author Hannu Hartikainen
 */
public class PlanetPanel extends javax.swing.JPanel {

    // planeetta, joka otetaan paneeliin jos ei ole planeettoja.
    private static Planet NULL_PLANET = new Planet(0, 0, 0, 0, 0, "(no planet)", Color.black);

    private Planet planet;
    private boolean updateAllFields;
    private NbodyFrame nbodyFrame;
    private World world;

    /**
     * Luo paneelin, eli kutsuu NetBeansin generoimaa initComponents()-metodia
     * ja asettaa tarvittavat attribuutit.
     * 
     * @param nbodyFrame frame, johon paneeli kytketään
     * @param world maailma, johon paneeli kytketään
     */
    public PlanetPanel(NbodyFrame nbodyFrame, World world) {
        this.initComponents();
        this.nbodyFrame = nbodyFrame;
        this.setWorld(world);
    }

    /**
     * Asettaa planeetan, joka on valittuna. Olettaa, että planeetta on
     * listassa. Huolehtii komponenttien päivittämisestä.
     * 
     * @param planet planeetta, joka valitaan
     */
    public void setPlanet(Planet planet) {
        this.planet = planet;
        this.updatePlanetList();
        this.planetListBox.getModel().setSelectedItem(planet);
        this.updateAllFields = true;
        // tätä kutsua ei tarvitsisi tehdä, jos renderöintisäie on käynnissä.
        this.updateComponentValues();
        this.updateZoomAndPosition();
    }

    /**
     * Kytkee paneelin maailmaan, jonka planeettoja katsotaan ja muokataan.
     * Huolehtii että "tyhjä planeetta" valitaan jos maailma on tyhjä.
     * 
     * @param world maailma, johon paneeli kytketään
     */
    public void setWorld(World world) {
        this.world = world;
        this.updatePlanetList();
        if (!this.world.getPlanets().isEmpty()) {
            this.setPlanet(this.world.getPlanets().get(0));
        } else {
            this.setPlanet(NULL_PLANET);
        }
    }

    /**
     * Kertoo halutaanko että ruudukko piirretään
     */
    protected boolean isGridEnabled() {
        return this.gridCheckBox.isSelected();
    }

    /**
     * Kertoo halutaanko planeettojen nimet näkyviin simulaationäkymässä.
     */
    protected boolean isPlanetNamesEnabled() {
        return this.planetNamesCheckBox.isSelected();
    }

    /**
     * Kertoo onko simulaation pyöritys paneelissa päällä.
     */
    protected boolean isSimulationPaused() {
        return this.startStopButton.isSelected();
    }

    /**
     * Päivittää ne komponenttien arvot, jotka muuttuvat jatkuvasti, sekä massan
     * ja nimen ja värin, jos this.updateAllFields == true.
     * 
     * Ei liikuta slidereitä jos käyttäjä on juuri säätämässä niitä.
     */
    protected void updateComponentValues() {
        this.xPositionTextField.setText(String.format("%.0f", this.planet.getPosition().x));
        this.yPositionTextField.setText(String.format("%.0f", this.planet.getPosition().y));

        if (!this.velocitySlider.getValueIsAdjusting()) {
            this.velocitySlider.setValue((int) this.planet.getLinearVelocity());
        }
        if (!this.directionSlider.getValueIsAdjusting()) {
            this.directionSlider.setValue((int) (this.planet.getDirection() * 1000));
        }

        if (this.updateAllFields) {
            this.updateAllFields = false;
            double mass = this.planet.getMass();
            int exp = (int) Math.floor(Math.log10(mass));
            double significand = mass / Math.pow(10, exp);
            this.massSlider.setValue((int) (significand * 10));
            this.massExponentSlider.setValue(exp);

            this.nameTextField.setText(this.planet.getName());
            this.planetColorButton.setBackground(this.planet.getColor());
        }
    }

    /**
     * Päivittää planeettalistan. Huolehtii ettei lista koskaan ole ihan tyhjä,
     * vaan tarvittaessa on "tyhjä planeetta".
     */
    protected void updatePlanetList() {
        Object[] planetArray = this.world.getPlanets().toArray();

        // jos ei ole planeettoja, laitetaan "tyhjä planeetta"
        if (planetArray.length == 0) {
            planetArray = new Object[] { NULL_PLANET };
        }
        DefaultComboBoxModel planetComboBoxModel = new DefaultComboBoxModel(planetArray);
        this.planetListBox.setModel(planetComboBoxModel);
    }

    /**
     * Päivittää simulaationäkymän rajauksen (zoom ja sijainti).
     */
    protected void updateZoomAndPosition() {
        this.zoomComboBox.setSelectedItem(String.format("%.5f", this.nbodyFrame.getNbodyPanel().getScalingFactor()));
        this.xViewTextField.setText("" + (int) this.nbodyFrame.getNbodyPanel().getViewX());
        this.yViewTextField.setText("" + (int) this.nbodyFrame.getNbodyPanel().getViewY());
    }

    /**
     * Poistaa planeetan.
     * 
     * @param evt tapahtumaan liittyvä Event-olio
     */
    private void removePlanetButtonClicked(java.awt.event.ActionEvent evt) {
        Planet removedPlanet = (Planet) this.planetListBox.getSelectedItem();

        // katsotaan tyhjeneekö planeettalista
        if (this.planetListBox.getModel().getSize() == 1) {
            this.setPlanet(NULL_PLANET);
        }
        this.world.removePlanet(removedPlanet);
        this.planetListBox.removeItem(removedPlanet);
    }

    /**
     * Pysäyttää tai käynnistää fysiikkasimulaation.
     * 
     * @param evt tapahtumaan liittyvä Event-olio
     */
    private void startStopButtonClicked(java.awt.event.ActionEvent evt) {
        this.nbodyFrame.toggleSimulation();
    }

    /**
     * Poistaa kaikki muut planeetat.
     * 
     * @param evt tapahtumaan liittyvä Event-olio
     */
    @SuppressWarnings("unchecked")
    private void removeOtherPlanetsButtonClicked(java.awt.event.ActionEvent evt) {
        ArrayList<Planet> planetList = (ArrayList<Planet>) this.world.getPlanets().clone();
        for (Planet p : planetList) {
            if (!p.equals(this.planet)) {
                this.world.removePlanet(p);
            }
        }
        this.updatePlanetList();
    }

    /**
     * Muuttaa planeetan massaa.
     * 
     * @param evt tapahtumaan liittyvä Event-olio
     */
    private void massSliderChanged(javax.swing.event.ChangeEvent evt) {
        double mass = 0.1 * this.massSlider.getValue() * Math.pow(10, this.massExponentSlider.getValue());
        this.massTextField.setText(String.format("%.1e", mass));
        this.planet.setMass(mass);
    }

    /**
     * Muuttaa planeetan massaa ja asettaa oletusmassan. Pyytää päivittämään
     * myös massa-sliderit.
     * 
     * @param evt tapahtumaan liittyvä Event-olio
     */
    private void massTextFieldChanged(java.awt.event.KeyEvent evt) {
        // toimitaan vain, jos painettiin enteriä
        if (evt.getKeyChar() != '\n') {
            return;
        }
        try {
            double mass = Double.parseDouble(this.massTextField.getText());
            this.nbodyFrame.setDefaultMass(mass);
            this.planet.setMass(mass);
            this.updateAllFields = true;
        } catch (NumberFormatException e) {
            // ei haittaa mitään, tällöin ei vain tehdä mitään arvoille.
            return;
        }
    }

    /**
     * Vaihtaa planeetan nimen.
     * 
     * @param evt tapahtumaan liittyvä Event-olio
     */
    private void nameTextFieldChanged(java.awt.event.KeyEvent evt) {
        // toimitaan vain, jos painettiin enteriä
        if (evt.getKeyChar() != '\n') {
            return;
        }

        this.planet.setName(this.nameTextField.getText());
        this.updateAllFields = true;
        this.setPlanet(this.planet);
    }

    /**
     * Muuttaa planeetan nopeutta (tätä kutsutaan kun joko velocity- tai
     * direction-sliderin arvo muuttuu).
     * 
     * @param evt tapahtumaan liittyvä Event-olio
     */
    private void velocitySliderChanged(java.awt.event.MouseEvent evt) {
        this.planet.setVelocityPolar(this.velocitySlider.getValue(), this.directionSlider.getValue() / 1000.0);
    }

    /**
     * Vaihtaa planeetan väriä (näyttää värinvaihtoruudun).
     * 
     * @param evt tapahtumaan liittyvä Event-olio
     */
    private void planetColorButtonClicked(java.awt.event.ActionEvent evt) {
        Color newColor = JColorChooser.showDialog(this, "Choose planet color", this.planet.getColor());

        if (newColor != null) {
            this.planet.setColor(newColor);
            this.updateAllFields = true;
        }
    }

    /**
     * Vaihtaa planeettaa.
     * 
     * @param evt tapahtumaan liittyvä Event-olio
     */
    private void planetChangedFromList(java.awt.event.ActionEvent evt) {
        this.setPlanet((Planet) this.planetListBox.getSelectedItem());
    }

    /**
     * Vaihtaa planeettaa seuraavaan listassa olevaan.
     * 
     * @param evt tapahtumaan liittyvä Event-olio
     */
    private void nextPlanet(java.awt.event.ActionEvent evt) {
        int index = this.planetListBox.getSelectedIndex() + 1; // halutaan
        // seuraava

        int count = this.planetListBox.getItemCount();

        // valitaan ensimmäinen jos ollaan viimeisessä
        // ja poistutaan jos lista on tyhjä
        if (count == 0) {
            return;
        } else if (count < index + 1) {
            index = 0;
        }
        this.planetListBox.setSelectedIndex(index);
        this.setPlanet((Planet) this.planetListBox.getSelectedItem());
    }

    /**
     * Muuttaa näkymän koordinaatteja.
     * 
     * @param evt tapahtumaan liittyvä Event-olio
     */
    private void viewTextFieldKeyTyped(java.awt.event.KeyEvent evt) {
        // toimitaan vain, jos painettiin enteriä
        if (evt.getKeyChar() == '\n') {
            try {
                double x = Double.parseDouble(this.xViewTextField.getText());
                double y = Double.parseDouble(this.yViewTextField.getText());

                this.nbodyFrame.getNbodyPanel().setViewCenter(x, y);
            } catch (NumberFormatException e) {
                // ei voi mitään... poikkeus tulee ennen kuin näkymää
                // siirretään, se siis
                // jää siirtämättä.
            }
        }
    }

    /**
     * Säätää zoomia.
     * 
     * @param evt tapahtumaan liittyvä Event-olio
     */
    private void zoomBoxChanged(java.awt.event.ActionEvent evt) {
        try {
            double zoom = Double.parseDouble((String) this.zoomComboBox.getSelectedItem());
            this.nbodyFrame.getNbodyPanel().setScalingFactor(zoom);
        } catch (NumberFormatException e) {
            // ei haittaa mitään, tällöin ei vain tehdä mitään
            return;
        }
    }


    /*
     * HUOM! NÄMÄ ATTRIBUUTIT OVAT NETBEANSIN AUTOMAATTISESTI GENEROIMIA EIVÄTKÄ
     * MINUN KIRJOITTAMIANI.
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider directionSlider;
    private javax.swing.JCheckBox gridCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSlider massExponentSlider;
    private javax.swing.JSlider massSlider;
    private javax.swing.JTextField massTextField;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton nextPlanetButton;
    private javax.swing.JButton planetColorButton;
    private javax.swing.JComboBox planetListBox;
    private javax.swing.JCheckBox planetNamesCheckBox;
    private javax.swing.JButton removeOtherPlanetsButton;
    private javax.swing.JButton removePlanetButton;
    private javax.swing.JToggleButton startStopButton;
    private javax.swing.JSlider velocitySlider;
    private javax.swing.JTextField xPositionTextField;
    private javax.swing.JTextField xViewTextField;
    private javax.swing.JTextField yPositionTextField;
    private javax.swing.JTextField yViewTextField;
    private javax.swing.JComboBox zoomComboBox;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodi, joka asettelee komponentit paikoilleen. HUOM! TÄMÄ METODI EI OLE
     * MINUN KIRJOITTAMANI, vaan NetBeansin GUI-editorin generoima.
     */
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        massTextField = new javax.swing.JTextField();
        massSlider = new javax.swing.JSlider();
        massExponentSlider = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        planetColorButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        xPositionTextField = new javax.swing.JTextField();
        yPositionTextField = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        directionSlider = new javax.swing.JSlider();
        velocitySlider = new javax.swing.JSlider();
        planetListBox = new javax.swing.JComboBox();
        nextPlanetButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        removePlanetButton = new javax.swing.JButton();
        removeOtherPlanetsButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        startStopButton = new javax.swing.JToggleButton();
        zoomComboBox = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        gridCheckBox = new javax.swing.JCheckBox();
        planetNamesCheckBox = new javax.swing.JCheckBox();
        xViewTextField = new javax.swing.JTextField();
        yViewTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        setAutoscrolls(true);
        setMinimumSize(new java.awt.Dimension(300, 400));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Planet"));
        jPanel1.setMinimumSize(new java.awt.Dimension(300, 400));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setLabelFor(massTextField);
        jLabel1.setText("Mass (kg)");

        massTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        massTextField.setText("0.0e+00");
        massTextField.setMinimumSize(new java.awt.Dimension(70, 28));
        massTextField.setPreferredSize(new java.awt.Dimension(70, 28));
        massTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                massTextFieldChanged(evt);
            }
        });

        massSlider.setMinimum(1);
        massSlider.setValue(10);
        massSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                massSliderChanged(evt);
            }
        });

        massExponentSlider.setMaximum(25);
        massExponentSlider.setValue(10);
        massExponentSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                massSliderChanged(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setLabelFor(massTextField);
        jLabel4.setText("Name");

        nameTextField.setText("jTextField1");
        nameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nameTextFieldChanged(evt);
            }
        });

        planetColorButton.setBackground(new java.awt.Color(153, 153, 153));
        planetColorButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        planetColorButton.setOpaque(true);
        planetColorButton.setPreferredSize(new java.awt.Dimension(20, 20));
        planetColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                planetColorButtonClicked(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setLabelFor(massTextField);
        jLabel5.setText("Position");

        xPositionTextField.setEditable(false);
        xPositionTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        xPositionTextField.setText("0");
        xPositionTextField.setFocusable(false);
        xPositionTextField.setRequestFocusEnabled(false);

        yPositionTextField.setEditable(false);
        yPositionTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        yPositionTextField.setText("0");
        yPositionTextField.setFocusable(false);
        yPositionTextField.setRequestFocusEnabled(false);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Velocity");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Direction");

        directionSlider.setMaximum(6283);
        directionSlider.setValue(0);
        directionSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                velocitySliderChanged(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                velocitySliderChanged(evt);
            }
        });

        velocitySlider.setMaximum(1000);
        velocitySlider.setValue(0);
        velocitySlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                velocitySliderChanged(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                velocitySliderChanged(evt);
            }
        });

        planetListBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        planetListBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                planetChangedFromList(evt);
            }
        });

        nextPlanetButton.setText("Next planet");
        nextPlanetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextPlanet(evt);
            }
        });

        removePlanetButton.setText("Remove planet");
        removePlanetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removePlanetButtonClicked(evt);
            }
        });

        removeOtherPlanetsButton.setText("Remove all other planets");
        removeOtherPlanetsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeOtherPlanetsButtonClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(massTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(massSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .add(massExponentSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(nameTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(planetColorButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                        .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(xPositionTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(yPositionTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(directionSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .add(velocitySlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)))
                    .add(jSeparator3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                    .add(removePlanetButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(planetListBox, 0, 125, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(nextPlanetButton))
                    .add(removeOtherPlanetsButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nextPlanetButton)
                    .add(planetListBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(13, 13, 13)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .add(planetColorButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(nameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .add(xPositionTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(yPositionTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(massSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(massTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(massExponentSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(velocitySlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(directionSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(removePlanetButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(removeOtherPlanetsButton)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("View"));

        startStopButton.setText("Pause");
        startStopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startStopButtonClicked(evt);
            }
        });

        zoomComboBox.setEditable(true);
        zoomComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.05", "0.1", "0.2", "0.3", "0.5", "0.75", "1.0", "1.5", "2.0" }));
        zoomComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomBoxChanged(evt);
            }
        });

        jLabel6.setText("Zoom:");

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        gridCheckBox.setSelected(true);
        gridCheckBox.setText("Grid");

        planetNamesCheckBox.setSelected(true);
        planetNamesCheckBox.setText("Names");

        xViewTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        xViewTextField.setText("0");
        xViewTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                viewTextFieldKeyTyped(evt);
            }
        });

        yViewTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        yViewTextField.setText("0");
        yViewTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                viewTextFieldKeyTyped(evt);
            }
        });

        jLabel7.setText("Position:");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(startStopButton)
                    .add(gridCheckBox)
                    .add(planetNamesCheckBox))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel6)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(zoomComboBox, 0, 97, Short.MAX_VALUE))
                    .add(jLabel7)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(xViewTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(yViewTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(startStopButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(gridCheckBox)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(planetNamesCheckBox))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(zoomComboBox))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jLabel7)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(yViewTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(xViewTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(10, 10, 10))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
}
