/**
 * 
 */
package dancek.nbody;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.ScheduledFuture;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Ohjelman ikkuna (JFrame-kehys).
 * 
 * @author Hannu Hartikainen
 */
public class NbodyFrame extends JFrame {

    private World world;
    private NbodyPanel nbodyPanel;
    private ScheduledFuture<?> simulationHandle;
    private boolean simulationRunning;
    private PlanetPanel planetPanel;
    private JPanel sidePanel;

    public NbodyFrame(World world) {
        Planet panelPlanet = new Planet(0,-100,-250,0,1e10);
        world.addPlanet(panelPlanet);
        this.world = world;
        this.nbodyPanel = new NbodyPanel(this.world);
        this.planetPanel = new PlanetPanel(this.nbodyPanel, this.world);

        this.sidePanel = new JPanel();
        this.sidePanel.setLayout(new BorderLayout());
        this.sidePanel.add(this.planetPanel, BorderLayout.SOUTH);
        this.sidePanel.add(new JPanel(), BorderLayout.NORTH);

        this.setLayout(new BorderLayout());

        this.add(this.nbodyPanel, BorderLayout.CENTER);
        this.add(this.sidePanel, BorderLayout.EAST);
        this.pack();

        this.setTitle("nbody");
        
        // laitetaan ohjelma päättymään, kun ikkuna suljetaan
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ikkuna keskelle ruutua
        this.setLocationRelativeTo(null);

        // näytetään ikkuna
        this.setVisible(true);

        this.nbodyPanel.addMouseListener(new NbodyPanelClickListener());

        this.simulationHandle = Physics.startPhysics(world, this.nbodyPanel, this.planetPanel);
        this.simulationRunning = true;
    }
    
    private class NbodyPanelClickListener implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            if (NbodyFrame.this.simulationRunning) {
                NbodyFrame.this.simulationHandle.cancel(false);
                NbodyFrame.this.simulationRunning = false;
            } else {
                NbodyFrame.this.simulationHandle = Physics.startPhysics(world, NbodyFrame.this.nbodyPanel, NbodyFrame.this.planetPanel);
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
