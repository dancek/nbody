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
    private static final double DEFAULT_SCALING_FACTOR = 0.5;
    /**
     * 
     */
    private static final double MOUSE_WHEEL_FACTOR = 10.0;
    private static final Dimension PANEL_SIZE = new Dimension(700, 700);
    private static final Color VECTOR_COLOR = Color.gray;

    private World world;
    private int xOffset;
    private int yOffset;
    private double scalingFactor;
    private int panelCenterXOffset;
    private int panelCenterYOffset;
    private int mouseX;
    private int mouseY;

    public NbodyPanel(World world) {
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

        this.panelCenterXOffset = this.getWidth() / 2;
        this.panelCenterYOffset = this.getHeight() / 2;

        g2d.setBackground(Color.black);
        g2d.clearRect(0, 0, this.getWidth(), this.getHeight());

        for (Planet planet : this.world.getPlanets()) {
            this.drawPlanet(g2d, planet);
        }

        if (this.world.hasPendingPlanet()) {
            //this.drawPlanet(g2d, this.pendingPlanet);
            this.drawVelocityVector(g2d, this.world.getPendingPlanet());
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

    private void drawPlanet(Graphics2D g2d, Planet planet) {
        int x = this.screenX(planet.getPosition().x);
        int y = this.screenY(planet.getPosition().y);
        int size = (int) Math.ceil(planet.getSize() * this.scalingFactor);

        g2d.setColor(planet.getColor());

        // vähennetään koordinaateista ympyrän säde, jotta piirros tulee
        // keskikohdan mukaan.
        g2d.fillOval(x - size / 2, y - size / 2, size, size);
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

    public Planet addPendingPlanet(int x, int y) {
        double planetX = this.worldX(x);
        double planetY = this.worldY(y);
        this.world.addPendingPlanet(new Planet(planetX, planetY));
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
}
