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

    public NbodyPanel(World world) {
        this.world = world;

        this.setPreferredSize(PANEL_SIZE);
        this.xOffset = PANEL_SIZE.width / 2;
        this.yOffset = PANEL_SIZE.height / 2;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setBackground(Color.black);
        g2d.clearRect(0, 0, this.getWidth(), this.getHeight());

        for (Planet planet : this.world.getPlanets()) {
            int x = (int) planet.getPosition().x + this.xOffset;
            int y = (int) planet.getPosition().y + this.yOffset;
            int size = planet.getSize();

            g2d.setColor(planet.getColor());

            // vähennetään koordinaateista ympyrän säde, jotta piirros tulee
            // keskikohdan mukaan.
            g2d.fillOval(x - size / 2, y - size / 2, size, size);
        }
    }
}
