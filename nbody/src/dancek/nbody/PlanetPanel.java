package dancek.nbody;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;

/**
 * Tämä luokka sisältää käyttöliittymän sivupaneelin. HUOM: LUOKKA SISÄLTÄÄ
 * AUTOMAATTISESTI GENEROITUA KOODIA. Tarkemmin sanottuna initComponents-metodi
 * ja siihen liittyvät komponenttiattribuutit ovat NetBeans 6.1:n graafisen
 * GUI-editorin tuotosta. Olen toki käyttänyt editoria itse (mikä vaatii kyllä
 * myöskin vaivannäköä). Tapahtumakäsittelijät ja muu luokan toimintalogiikka on
 * kirjoitettu käsin.
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

    public PlanetPanel() {
        this.initComponents();
    }

    public PlanetPanel(NbodyFrame nbodyFrame, World world) {
        this();
        this.nbodyFrame = nbodyFrame;
        this.setWorld(world);
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
        this.updatePlanetList();
        this.planetListBox.getModel().setSelectedItem(planet);
        this.updateAllFields = true;
        this.updateComponentValues();
        this.updateZoom();
    }

    public void setWorld(World world) {
        this.world = world;
        this.updatePlanetList();
        if (!this.world.getPlanets().isEmpty()) {
            this.setPlanet(this.world.getPlanets().get(0));
        } else {
            this.setPlanet(NULL_PLANET);
        }
    }
    
    public boolean isGridEnabled() {
        return this.gridCheckBox.isSelected();
    }


    private void zoomBoxChanged(java.awt.event.ActionEvent evt) {// GEN-FIRST:
        // event_zoomBoxChanged
        try {
            double zoom = Double.parseDouble((String) this.zoomComboBox.getSelectedItem());
            this.nbodyFrame.getNbodyPanel().setScalingFactor(zoom);
        } catch (NumberFormatException e) {
            // ei haittaa mitään, tällöin ei vain tehdä mitään
            return;
        }

    }// GEN-LAST:event_zoomBoxChanged

    private void removePlanetButtonClicked(java.awt.event.ActionEvent evt) {
        Planet removedPlanet = (Planet) this.planetListBox.getSelectedItem();

        // katsotaan tyhjeneekö planeettalista
        if (this.planetListBox.getModel().getSize() == 1)
            this.setPlanet(NULL_PLANET);

        this.world.removePlanet(removedPlanet);
        this.planetListBox.removeItem(removedPlanet);
    }

    private void startStopButtonClicked(java.awt.event.ActionEvent evt) {
        this.nbodyFrame.toggleSimulation();
    }

    private void removeOtherPlanetsButtonClicked(java.awt.event.ActionEvent evt) {
        ArrayList<Planet> planetList = (ArrayList<Planet>) this.world.getPlanets().clone();
        for (Planet p : planetList) {
            if (!p.equals(this.planet)) {
                this.world.removePlanet(p);
            }
        }
        this.updatePlanetList();
    }

    private void massSliderChanged(javax.swing.event.ChangeEvent evt) {// GEN-
        // FIRST:
        // event_massSliderChanged

        double mass = 0.1 * this.massSlider.getValue() * Math.pow(10, this.massExponentSlider.getValue());
        this.massTextField.setText(String.format("%.1e", mass));
        this.planet.setMass(mass);
        // this.updateVisuals();
    }// GEN-LAST:event_massSliderChanged

    private void massTextFieldChanged(java.awt.event.KeyEvent evt) {
        // event_massTextFieldChanged
        // toimitaan vain, jos painettiin enteriä
        if (evt.getKeyChar() != '\n') {
            return;
        }
        try {
            double mass = Double.parseDouble(this.massTextField.getText());
            this.planet.setMass(mass);
            this.updateAllFields = true;
            // this.updateVisuals();
        } catch (NumberFormatException e) {
            // ei haittaa mitään, tällöin ei vain tehdä mitään arvoille.
            return;
        }
    }// GEN-LAST:event_massTextFieldChanged

    private void nameTextFieldChanged(java.awt.event.KeyEvent evt) {
        // event_nameTextFieldChanged
        // toimitaan vain, jos painettiin enteriä
        if (evt.getKeyChar() != '\n') {
            return;
        }

        this.planet.setName(this.nameTextField.getText());
        this.updateAllFields = true;
    }// GEN-LAST:event_nameTextFieldChanged

    private void velocitySliderChanged(java.awt.event.MouseEvent evt) {// GEN-
        // FIRST:
        // event_velocitySliderChanged

        this.planet.setVelocityPolar(this.velocitySlider.getValue(), this.directionSlider.getValue() / 1000.0);
    }// GEN-LAST:event_velocitySliderChanged

    private void planetColorButtonClicked(java.awt.event.ActionEvent evt) {//GEN-
        // FIRST
        // :
        // event_planetColorButtonClicked

        Color newColor = JColorChooser.showDialog(this, "Choose planet color", this.planet.getColor());

        if (newColor != null) {
            this.planet.setColor(newColor);
            this.updateAllFields = true;
            // this.updateVisuals();
        }
    }// GEN-LAST:event_planetColorButtonClicked

    private void planetChangedFromList(java.awt.event.ActionEvent evt) {// GEN-
        // FIRST
        // :
        // event_planetChangedFromList

        this.setPlanet((Planet) this.planetListBox.getSelectedItem());
    }// GEN-LAST:event_planetChangedFromList

    private void nextPlanet(java.awt.event.ActionEvent evt) {// GEN-FIRST:
        // event_nextPlanet

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
    }// GEN-LAST:event_nextPlanet

    protected void updateComponentValues() {
        this.xPositionTextField.setText(String.format("%.2f", this.planet.getPosition().x));
        this.yPositionTextField.setText(String.format("%.2f", this.planet.getPosition().y));

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

    protected void updatePlanetList() {
        Object[] planetArray = this.world.getPlanets().toArray();

        // jos ei ole planeettoja, laitetaan "tyhjä planeetta"
        if (planetArray.length == 0)
            planetArray = new Object[] { NULL_PLANET };

        DefaultComboBoxModel planetComboBoxModel = new DefaultComboBoxModel(planetArray);
        this.planetListBox.setModel(planetComboBoxModel);
    }

    protected void updateZoom() {
        this.zoomComboBox.setSelectedItem(String.format("%.4f", this.nbodyFrame.getNbodyPanel().getScalingFactor()));
    }

    // HUOM! NÄMÄ ATTRIBUUTIT OVAT NETBEANSIN AUTOMAATTISESTI
    // GENEROIMIA EIVÄTKÄ MINUN KIRJOITTAMIANI.
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider directionSlider;
    private javax.swing.JCheckBox gridCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
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
    private javax.swing.JButton removeOtherPlanetsButton;
    private javax.swing.JButton removePlanetButton;
    private javax.swing.JToggleButton startStopButton;
    private javax.swing.JSlider velocitySlider;
    private javax.swing.JTextField xPositionTextField;
    private javax.swing.JTextField yPositionTextField;
    private javax.swing.JComboBox zoomComboBox;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Metodi, joka asettelee komponentit paikoilleen. HUOM! TÄMÄ METODI EI OLE
     * MINUN KIRJOITTAMANI, vaan NetBeansin GUI-editorin generoima.
     */
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

        xPositionTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        xPositionTextField.setText("0");

        yPositionTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        yPositionTextField.setText("0");

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

        velocitySlider.setMaximum(500);
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

        startStopButton.setText("Pause simulation");
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

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(startStopButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(gridCheckBox)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSeparator4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel6)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(zoomComboBox, 0, 122, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(startStopButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(gridCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator4)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(zoomComboBox)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
