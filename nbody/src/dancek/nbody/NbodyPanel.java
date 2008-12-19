/**
 * 
 */
package dancek.nbody;

import java.awt.*;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dancek.vecmath.SimpleVector;

/**
 * @author Hannu Hartikainen
 * 
 */
public class NbodyPanel extends JPanel {

    /**
     * 
     */
    private static final Color MINOR_GRIDLINE_COLOR = new Color(0, 15, 0);
    private static final Color MAJOR_GRIDLINE_COLOR = new Color(0, 30, 0);
    private static final Color MAJOR2_GRIDLINE_COLOR = new Color(0, 45, 0);
    private static final int MINOR_GRIDLINE_SPACING = 100;
    private static final int MAJOR_GRIDLINE_SPACING = 5 * MINOR_GRIDLINE_SPACING;
    private static final int MAJOR2_GRIDLINE_SPACING = 5 * MAJOR_GRIDLINE_SPACING;
    private static final int MINIMUM_GRIDLINE_DISTANCE = 10;
    /**
     * 
     */
    private static final double DEFAULT_SCALING_FACTOR = 0.5;
    /**
     * 
     */
    private static final double MOUSE_WHEEL_FACTOR = 10.0;
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

    public NbodyPanel(NbodyFrame nbodyFrame, World world) {
        this.nbodyFrame = nbodyFrame;
        this.setWorld(world);

        this.setPreferredSize(PANEL_SIZE);
        this.scalingFactor = DEFAULT_SCALING_FACTOR;
    }

    public double getScalingFactor() {
        return this.scalingFactor;
    }

    protected void setScalingFactor(double factor) {
        this.scalingFactor = factor;
    }

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
            if (this.getPixelDistance() * MINOR_GRIDLINE_SPACING >= MINIMUM_GRIDLINE_DISTANCE) {
                g2d.setColor(MINOR_GRIDLINE_COLOR);
                drawGrid(g2d, MINOR_GRIDLINE_SPACING, width, height);
            }
            if (this.getPixelDistance() * MAJOR_GRIDLINE_SPACING >= MINIMUM_GRIDLINE_DISTANCE) {
                g2d.setColor(MAJOR_GRIDLINE_COLOR);
                drawGrid(g2d, MAJOR_GRIDLINE_SPACING, width, height);
            }
            if (this.getPixelDistance() * MAJOR2_GRIDLINE_SPACING >= MINIMUM_GRIDLINE_DISTANCE) {
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
     * Palauttaa yhden pikselin "leveyden" avaruuden koordinaateissa. Ei käytetä
     * suoraan getScalingFactoria, koska voitaisiin haluta laittaa skaalaus
     * toimimaan eri asteikolla.
     */
    private double getPixelDistance() {
        return this.getScalingFactor();
    }

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

    private void drawVelocityVector(Graphics2D g2d, Planet pendingPlanet) {
        int planetX = this.screenX(pendingPlanet.getPosition().x);
        int planetY = this.screenY(pendingPlanet.getPosition().y);

        g2d.setColor(VECTOR_COLOR);
        g2d.drawLine(planetX, planetY, this.mouseX, this.mouseY);

        SimpleVector helper = new SimpleVector(this.mouseX - planetX, this.mouseY - planetY);
        helper.scale(0.15);
        helper.rotate(Math.PI / 6);
    }

    private void drawPlanet(Graphics2D g2d, Planet planet, boolean drawNames) {
        int x = this.screenX(planet.getPosition().x);
        int y = this.screenY(planet.getPosition().y);
        int size = (int) Math.ceil(planet.getSize() * this.scalingFactor);

        g2d.setColor(planet.getColor());

        // vähennetään koordinaateista ympyrän säde, jotta piirros tulee
        // keskikohdan mukaan.
        g2d.fillOval(x - size / 2, y - size / 2, size, size);

        if (drawNames) {
            g2d.setColor(Color.gray);
            g2d.drawString(planet.getName(), x + size / 2, y - size / 2);
        }
    }

    private int screenX(double x) {
        return (int) ((x + this.xOffset) * this.scalingFactor) + this.panelCenterXOffset;
    }

    private int screenY(double y) {
        return (int) ((y + this.yOffset) * this.scalingFactor) + this.panelCenterYOffset;
    }

    private double worldX(int x) {
        return (x - this.panelCenterXOffset) / this.scalingFactor - this.xOffset;
    }

    private double worldY(int y) {
        return (y - this.panelCenterYOffset) / this.scalingFactor - this.yOffset;
    }

    public Planet addPendingPlanet(int x, int y, double mass) {
        double planetX = this.worldX(x);
        double planetY = this.worldY(y);
        this.world.addPendingPlanet(new Planet(planetX, planetY, mass));
        this.mouseX = x;
        this.mouseY = y;
        return this.world.getPendingPlanet();
    }

    public void setPendingPlanetVelocity(int x, int y) {
        Planet pendingPlanet = this.world.getPendingPlanet();
        double xVelocity = this.worldX(x) - pendingPlanet.getPosition().x;
        double yVelocity = this.worldY(y) - pendingPlanet.getPosition().y;
        pendingPlanet.setVelocity(xVelocity, yVelocity);
        this.world.addPlanet(pendingPlanet);
    }

    protected void moveView(int x, int y) {
        this.xOffset += x / this.scalingFactor;
        this.yOffset += y / this.scalingFactor;
    }

    /**
     * @param x
     * @param y
     * @param wheelRotation
     */
    protected void zoom(int x, int y, int wheelRotation) {
        double currentX = this.worldX(x);
        double currentY = this.worldY(y);
        this.scalingFactor *= (wheelRotation > 0) ? 1.0 / (1 + wheelRotation / MOUSE_WHEEL_FACTOR) : 1 - wheelRotation
                / MOUSE_WHEEL_FACTOR;

        this.xOffset += (int) (this.worldX(x) - currentX);
        this.yOffset += (int) (this.worldY(y) - currentY);
    }

    /**
     * @param x
     * @param y
     */
    protected void setMouse(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    /**
     * Asettaa maailman.
     */
    protected void setWorld(World world) {
        this.world = world;
    }

    /**
     * Asettaa näkymän keskikohdan kohtaan (x,y).
     * 
     * @param x koordinaatti
     * @param y koordinaatti
     */
    public void setViewCenter(double x, double y) {
        this.xOffset = -x;
        this.yOffset = -y;
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
}
