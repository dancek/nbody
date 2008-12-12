/**
 * 
 */
package dancek.nbody;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.ScheduledFuture;

import javax.swing.JFrame;

/**
 * Ohjelman ikkuna (JFrame-kehys).
 * 
 * @author Hannu Hartikainen
 */
public class NbodyFrame extends JFrame {

    private World world;
    private NbodyPanel panel;
    private ScheduledFuture<?> simulationHandle;
    private boolean simulationRunning;

    public NbodyFrame(World world) {
        this.world = world;
        this.panel = new NbodyPanel(this.world);

        this.add(this.panel);
        this.pack();

        // laitetaan ohjelma päättymään, kun ikkuna suljetaan
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ikkuna keskelle ruutua
        this.setLocationRelativeTo(null);

        // näytetään ikkuna
        this.setVisible(true);

        this.panel.addMouseListener(new NbodyPanelClickListener());

        this.simulationHandle = Physics.startPhysics(world, this.panel);
        this.simulationRunning = true;
    }

    private class NbodyPanelClickListener implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            if (NbodyFrame.this.simulationRunning) {
                NbodyFrame.this.simulationHandle.cancel(false);
                NbodyFrame.this.simulationRunning = false;
            } else {
                NbodyFrame.this.simulationHandle = Physics.startPhysics(world, NbodyFrame.this.panel);
                NbodyFrame.this.simulationRunning = true;
            }
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }
}
