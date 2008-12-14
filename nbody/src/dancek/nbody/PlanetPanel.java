package dancek.nbody;

import java.awt.Graphics;

/**
 * Tämä luokka sisältää käyttöliittymän sivupaneelin.
 * HUOM: LUOKKA SISÄLTÄÄ AUTOMAATTISESTI GENEROITUA KOODIA.
 * Tarkemmin sanottuna initComponents-metodi ja siihen liittyvät
 * komponenttiattribuutit ovat NetBeans 6.1:n graafisen GUI-editorin
 * tuotosta. Olen toki käyttänyt editoria itse. Tapahtumakäsittelijät
 * ja muu luokan toimintalogiikka on kirjoitettu käsin.
 * 
 * @author Hannu Hartikainen
 */
public class PlanetPanel extends javax.swing.JPanel {

    private Planet planet;
    private boolean updateAllFields;

    public PlanetPanel() {
        initComponents();
    }

    public PlanetPanel(Planet planet) {
        this();
        this.planet = planet;
        this.updateAllFields = true;
        this.updateComponentValues();
    }

    /** 
     * Metodi, joka asettelee komponentit paikoilleen.
     * HUOM! TÄMÄ METODI EI OLE MINUN KIRJOITTAMANI, vaan NetBeansin
     * GUI-editorin generoima.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        massSlider = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        massExponentSlider = new javax.swing.JSlider();
        massTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        velocitySlider = new javax.swing.JSlider();
        directionSlider = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        xPositionTextField = new javax.swing.JTextField();
        yPositionTextField = new javax.swing.JTextField();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Planet"));
        setMinimumSize(new java.awt.Dimension(300, 400));
        setPreferredSize(new java.awt.Dimension(300, 700));

        massSlider.setMinimum(1);
        massSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                massSliderChanged(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setLabelFor(massTextField);
        jLabel1.setText("Mass (kg)");

        massExponentSlider.setMaximum(50);
        massExponentSlider.setValue(10);
        massExponentSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                massSliderChanged(evt);
            }
        });

        massTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        massTextField.setText("0.0e+00");
        massTextField.setMinimumSize(new java.awt.Dimension(70, 28));
        massTextField.setPreferredSize(new java.awt.Dimension(70, 28));
        massTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                massTextFieldChanged(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Velocity");

        velocitySlider.setMaximum(500);
        velocitySlider.setValue(0);
        velocitySlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                velocitySliderChanged(evt);
            }
        });

        directionSlider.setMaximum(6283);
        directionSlider.setValue(0);
        directionSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                velocitySliderChanged(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Direction");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setLabelFor(massTextField);
        jLabel4.setText("Name");

        nameTextField.setText("jTextField1");
        nameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nameTextFieldChanged(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setLabelFor(massTextField);
        jLabel5.setText("Position");

        xPositionTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        xPositionTextField.setText("0");

        yPositionTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        yPositionTextField.setText("0");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(massTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(massSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .add(massExponentSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(velocitySlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .add(directionSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)))
                    .add(layout.createSequentialGroup()
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(nameTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(xPositionTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 79, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(yPositionTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 79, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .add(nameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .add(xPositionTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(yPositionTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(74, 74, 74)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(massSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(massTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(massExponentSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(velocitySlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(directionSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                .add(389, 389, 389))
        );
    }// </editor-fold>//GEN-END:initComponents

private void massSliderChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_massSliderChanged
    double mass = 0.1 * this.massSlider.getValue() * Math.pow(10, this.massExponentSlider.getValue());
    this.massTextField.setText(String.format("%.1e", mass));
    this.planet.setMass(mass);
}//GEN-LAST:event_massSliderChanged

private void massTextFieldChanged(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_massTextFieldChanged
    // toimitaan vain, jos painettiin enteriä
    if (evt.getKeyChar() != '\n') {
        return;
    }
    try {
        double mass = Double.parseDouble(this.massTextField.getText());
        this.planet.setMass(mass);
        this.updateAllFields = true;
        this.updateComponentValues();
    } catch (NumberFormatException e) {
        // ei haittaa mitään, tällöin ei vain tehdä mitään arvoille.
        return;
    }
}//GEN-LAST:event_massTextFieldChanged

private void nameTextFieldChanged(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameTextFieldChanged
    // toimitaan vain, jos painettiin enteriä
    if (evt.getKeyChar() != '\n') {
        return;
    }
    
    this.planet.setName(this.nameTextField.getText());
}//GEN-LAST:event_nameTextFieldChanged

private void velocitySliderChanged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_velocitySliderChanged
    this.planet.setVelocityPolar(this.velocitySlider.getValue(), this.directionSlider.getValue() / 1000.0);
}//GEN-LAST:event_velocitySliderChanged

    private void updateComponentValues() {
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
        }
    }

    public void paintComponent(Graphics g) {
        this.updateComponentValues();
        super.paintComponent(g);
    }
    // HUOM! NÄMÄ ATTRIBUUTIT OVAT NETBEANSIN AUTOMAATTISESTI
    // GENEROIMIA EIVÄTKÄ MINUN KIRJOITTAMIANI.
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider directionSlider;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSlider massExponentSlider;
    private javax.swing.JSlider massSlider;
    private javax.swing.JTextField massTextField;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JSlider velocitySlider;
    private javax.swing.JTextField xPositionTextField;
    private javax.swing.JTextField yPositionTextField;
    // End of variables declaration//GEN-END:variables
}
