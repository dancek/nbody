package dancek.nbody;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import dancek.vecmath.SimpleVector;

/**
 * Paneeli, joka piirtää avaruutta.
 * 
 * @author Hannu Hartikainen
 */
public class NbodyPanel extends JPanel {

    /**
     * Ruudukkoon liittyvät vakiot
     */
    private static final Color MINOR_GRIDLINE_COLOR = new Color(0, 15, 0);
    private static final Color MAJOR_GRIDLINE_COLOR = new Color(0, 30, 0);
    private static final Color MAJOR2_GRIDLINE_COLOR = new Color(0, 45, 0);
    private static final int MINOR_GRIDLINE_SPACING = 100;
    private static final int MAJOR_GRIDLINE_SPACING = 5 * MINOR_GRIDLINE_SPACING;
    private static final int MAJOR2_GRIDLINE_SPACING = 5 * MAJOR_GRIDLINE_SPACING;
    private static final int MINIMUM_GRIDLINE_DISTANCE = 10;
    /**
     * Zoomiin liittyvät vakiot
     */
    private static final double DEFAULT_SCALING_FACTOR = 0.5;
    private static final double MINIMUM_SCALING_FACTOR = 1e-5;
    private static final double MOUSE_WHEEL_FACTOR = 10.0;

    /**
     * Muita vakioita
     */
    private static final Dimension PANEL_SIZE = new Dimension(700, 700);
    private static final Color VECTOR_COLOR = Color.gray;

    private World world;
    private double xOffset;
    private double yOffset;
    private double scalingFactor;
    private int panelCenterXOffset;
    private int panelCenterYOffset;
    private int mouseX;
    private int mouseY;
    private NbodyFrame nbodyFrame;

    /**
     * Luo uuden paneelin.
     * 
     * @param nbodyFrame frame, jossa sijaitaan
     * @param world maailma, jota piirretään
     */
    public NbodyPanel(NbodyFrame nbodyFrame, World world) {
        this.nbodyFrame = nbodyFrame;
        this.setWorld(world);

        this.setPreferredSize(PANEL_SIZE);
        this.scalingFactor = DEFAULT_SCALING_FACTOR;
    }

    /**
     * Palauttaa yhden pikselin "leveyden" avaruuden koordinaateissa.
     * 
     * @return pikselin koko avaruuskoordinaateissa
     */
    public double getPixelDistance() {
        return 1.0d / this.getScalingFactor();
    }

    /**
     * Antaa planeetan sijainnin perusteella.
     * 
     * @param screenX näyttökoordinaatti
     * @param screenY näyttökoordinaatti
     * @return planeetta
     */
    public Planet getPlanetByLocation(int screenX, int screenY) {
        Planet planet = null;
        double planetDistance = 0;

        SimpleVector clickPosition = new SimpleVector(this.worldX(screenX), this.worldY(screenY));
        double margin = 3 * this.getPixelDistance();

        for (Planet p : this.world.getPlanets()) {
            // etäisyys pythagoraan lauseella
            double distance = Math.sqrt(Math.pow(p.getPosition().x - clickPosition.x, 2)
                    + Math.pow(p.getPosition().y - clickPosition.y, 2));

            if (distance < p.getRadius() + margin) {
                // jos käypiä planeettoja on monta, otetaan lähin
                if (planet == null || distance < planetDistance) {
                    planet = p;
                    planetDistance = distance;
                }
            }
        }

        return planet;
    }

    /**
     * Palauttaa skaalauskertoimen.
     * 
     * @return skaalauskerroin
     */
    public double getScalingFactor() {
        return this.scalingFactor;
    }

    /**
     * Antaa näkymän x-koordinaatin.
     * 
     * @return näkymän x-koordinaatti
     */
    public double getViewX() {
        return -this.xOffset;
    }

    /**
     * Antaa näkymän y-koordinaatin.
     * 
     * @return näkymän y-koordinaatti
     */
    public double getViewY() {
        return -this.yOffset;
    }

    /*
     * (non-Javadoc) Piirtää komponentin eli käytännössä palan avaruutta.
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // otetaan paneelin mitat talteen; niitä tarvitaan
        int width = this.getWidth();
        int height = this.getHeight();

        // tallennetaan puolivälikoordinaatit (näitä käytetään, jotta saadaan
        // pidettyä ikkunan keskusta kohdallaan)
        this.panelCenterXOffset = width / 2;
        this.panelCenterYOffset = height / 2;

        // tyhjennetään tausta
        g2d.setBackground(Color.black);
        g2d.clearRect(0, 0, width, height);

        // piirretään ruudukko
        if (this.nbodyFrame.isGridEnabled()) {
            if (MINOR_GRIDLINE_SPACING / this.getPixelDistance() >= MINIMUM_GRIDLINE_DISTANCE) {
                g2d.setColor(MINOR_GRIDLINE_COLOR);
                drawGrid(g2d, MINOR_GRIDLINE_SPACING, width, height);
            }
            if (MAJOR_GRIDLINE_SPACING / this.getPixelDistance() >= MINIMUM_GRIDLINE_DISTANCE) {
                g2d.setColor(MAJOR_GRIDLINE_COLOR);
                drawGrid(g2d, MAJOR_GRIDLINE_SPACING, width, height);
            }
            if (MAJOR2_GRIDLINE_SPACING / this.getPixelDistance() >= MINIMUM_GRIDLINE_DISTANCE) {
                g2d.setColor(MAJOR2_GRIDLINE_COLOR);
                drawGrid(g2d, MAJOR2_GRIDLINE_SPACING, width, height);
            }
        }

        boolean drawNames = this.nbodyFrame.isPlanetNamesEnabled();
        // piirretään planeetat
        for (Planet planet : this.world.getPlanets()) {
            this.drawPlanet(g2d, planet, drawNames);
        }

        // piirretään mahdollinen nopeusvektori
        if (this.world.hasPendingPlanet()) {
            this.drawVelocityVector(g2d, this.world.getPendingPlanet());
        }
    }

    /**
     * Lisää planeetan, joka jää odottamaan nopeusvektoria.
     * 
     * @param x koordinaatti
     * @param y koordinaatti
     * @param mass massa
     * @return luotu planeetta
     */
    protected Planet addPendingPlanet(int x, int y, double mass) {
        double planetX = this.worldX(x);
        double planetY = this.worldY(y);
        this.world.addPendingPlanet(new Planet(planetX, planetY, mass));
        this.mouseX = x;
        this.mouseY = y;
        return this.world.getPendingPlanet();
    }

    /**
     * Siirtää näkymää annetun määrän pikseleitä.
     * 
     * @param x vaakasuuntainen liike
     * @param y pystysuuntainen liike
     */
    protected void moveView(int x, int y) {
        this.xOffset += x / this.scalingFactor;
        this.yOffset += y / this.scalingFactor;
    }

    /**
     * Metodi, jolla kerrotaan paneelille missä hiiri liikkuu.
     * 
     * @param x hiiren x-koordinaatti paneelissa
     * @param y hiiren y-koordinaatti paneelissa
     */
    protected void setMouse(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    /**
     * Asettaa odottavalle planeetalle nopeusvektorin.
     * 
     * @param x nopeuden x-komponentti
     * @param y nopeuden y-komponentti
     */
    protected void setPendingPlanetVelocity(int x, int y) {
        Planet pendingPlanet = this.world.getPendingPlanet();
        double xVelocity = this.worldX(x) - pendingPlanet.getPosition().x;
        double yVelocity = this.worldY(y) - pendingPlanet.getPosition().y;
        pendingPlanet.setVelocity(xVelocity, yVelocity);
        this.world.addPlanet(pendingPlanet);
    }

    /**
     * Asettaa paneelin skaalauskertoimen.
     * 
     * @param factor skaalauskerroin
     */
    protected void setScalingFactor(double factor) {
        if (factor < MINIMUM_SCALING_FACTOR)
            factor = MINIMUM_SCALING_FACTOR;
        this.scalingFactor = factor;
    }

    /**
     * Asettaa näkymän keskikohdan kohtaan (x,y).
     * 
     * @param x koordinaatti
     * @param y koordinaatti
     */
    protected void setViewCenter(double x, double y) {
        this.xOffset = -x;
        this.yOffset = -y;
    }

    /**
     * Asettaa maailman.
     * 
     * @param world maailma
     */
    protected void setWorld(World world) {
        this.world = world;
    }

    /**
     * Zoomaa annettuun pisteeseen.
     * 
     * @param x koordinaatti
     * @param y koordinaatti
     * @param wheelRotation pykälien määrä
     */
    protected void zoom(int x, int y, int wheelRotation) {
        double currentX = this.worldX(x);
        double currentY = this.worldY(y);

        double factor;

        if (wheelRotation > 0) {
            factor = this.scalingFactor * 1.0 / (1 + wheelRotation / MOUSE_WHEEL_FACTOR);
        } else {
            factor = this.scalingFactor * (1 - wheelRotation / MOUSE_WHEEL_FACTOR);
        }

        this.setScalingFactor(factor);

        this.xOffset += (int) (this.worldX(x) - currentX);
        this.yOffset += (int) (this.worldY(y) - currentY);
    }

    /**
     * Piirtää ruudukon annetulla viivavälillä annetun kokoiselle alueelle.
     */
    private void drawGrid(Graphics2D g2d, int spacing, int width, int height) {
        int x, y, drawX, drawY;
        for (x = (int) Math.floor(this.worldX(0) / spacing) * spacing; x <= this.worldX(width); x += spacing) {
            drawX = this.screenX(x);
            g2d.drawLine(drawX, 0, drawX, height);
        }
        for (y = (int) Math.floor(this.worldY(0) / spacing) * spacing; y <= this.worldY(height); y += spacing) {
            drawY = this.screenY(y);
            g2d.drawLine(0, drawY, width, drawY);
        }
    }

    /**
     * Piirtää planeetan.
     */
    private void drawPlanet(Graphics2D g2d, Planet planet, boolean drawNames) {
        int x = this.screenX(planet.getPosition().x);
        int y = this.screenY(planet.getPosition().y);
        int r = (int) Math.ceil(planet.getRadius() * this.scalingFactor);

        g2d.setColor(planet.getColor());

        // vähennetään koordinaateista ympyrän säde, jotta piirros tulee
        // keskikohdan mukaan.
        g2d.fillOval(x - r, y - r, 2 * r, 2 * r);

        if (drawNames) {
            g2d.setColor(Color.gray);
            g2d.drawString(planet.getName(), x + r / 2, y - r / 2);
        }
    }

    /**
     * Piirtää nopeusvektorin, jota ollaan asettamassa.
     */
    private void drawVelocityVector(Graphics2D g2d, Planet pendingPlanet) {
        int planetX = this.screenX(pendingPlanet.getPosition().x);
        int planetY = this.screenY(pendingPlanet.getPosition().y);

        g2d.setColor(VECTOR_COLOR);
        g2d.drawLine(planetX, planetY, this.mouseX, this.mouseY);

        SimpleVector helper = new SimpleVector(this.mouseX - planetX, this.mouseY - planetY);
        helper.scale(0.15);
        helper.rotate(Math.PI / 6);
    }

    /**
     * Muuttaa maailman koordinaatit paneelin koordinaateiksi.
     */
    private int screenX(double worldX) {
        return (int) ((worldX + this.xOffset) * this.scalingFactor) + this.panelCenterXOffset;
    }

    /**
     * Muuttaa maailman koordinaatit paneelin koordinaateiksi.
     */
    private int screenY(double worldY) {
        return (int) ((worldY + this.yOffset) * this.scalingFactor) + this.panelCenterYOffset;
    }

    /**
     * Muuttaa paneelin koordinaatit maailman koordinaateiksi.
     */
    private double worldX(int screenX) {
        return (screenX - this.panelCenterXOffset) / this.scalingFactor - this.xOffset;
    }

    /**
     * Muuttaa paneelin koordinaatit maailman koordinaateiksi.
     */
    private double worldY(int screenY) {
        return (screenY - this.panelCenterYOffset) / this.scalingFactor - this.yOffset;
    }
}
