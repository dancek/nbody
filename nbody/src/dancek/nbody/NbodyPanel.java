/**
 * 
 */
package dancek.nbody;

import java.awt.*;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author Hannu Hartikainen
 * 
 */
public class NbodyPanel extends JPanel {

    private static final Dimension PANEL_SIZE = new Dimension(700, 700);
    private World world;
    private int xOffset;
    private int yOffset;
    private double scalingFactor;
    private Planet pendingPlanet;

    public NbodyPanel(World world) {
        this.world = world;

        this.setPreferredSize(PANEL_SIZE);
        this.xOffset = PANEL_SIZE.width / 2;
        this.yOffset = PANEL_SIZE.height / 2;
        this.scalingFactor = 1.0;
    }

    public double getScalingFactor() {
        return this.scalingFactor;
    }

    protected void setScalingFactor(double factor) {
        this.scalingFactor = factor;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setBackground(Color.black);
        g2d.clearRect(0, 0, this.getWidth(), this.getHeight());

        for (Planet planet : this.world.getPlanets()) {
            this.drawPlanet(g2d, planet);
        }

        if (this.pendingPlanet != null)
            this.drawPlanet(g2d, this.pendingPlanet);
    }

    private void drawPlanet(Graphics2D g2d, Planet planet) {
        int x = (int) (planet.getPosition().x * this.scalingFactor) + this.xOffset;
        int y = (int) (planet.getPosition().y * this.scalingFactor) + this.yOffset;
        int size = (int) Math.ceil(planet.getSize() * this.scalingFactor);

        g2d.setColor(planet.getColor());

        // vähennetään koordinaateista ympyrän säde, jotta piirros tulee
        // keskikohdan mukaan.
        g2d.fillOval(x - size / 2, y - size / 2, size, size);
    }

    public Planet addPendingPlanet(int x, int y) {
        double planetX = (x - this.xOffset) / this.scalingFactor;
        double planetY = (y - this.yOffset) / this.scalingFactor;
        this.pendingPlanet = new Planet(planetX, planetY);
        return this.pendingPlanet;
    }

    public void setPendingPlanetVelocity(int x, int y) {
        double xVelocity = (x - this.xOffset) / this.scalingFactor - this.pendingPlanet.getPosition().x;
        double yVelocity = (y - this.yOffset) / this.scalingFactor - this.pendingPlanet.getPosition().y;
        this.pendingPlanet.setVelocity(xVelocity, yVelocity);
        this.world.addPlanet(pendingPlanet);
        this.pendingPlanet = null;
    }
}
