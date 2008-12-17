/**
 * 
 */
package dancek.nbody;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.concurrent.ScheduledFuture;

import javax.swing.*;

/**
 * Ohjelman ikkuna (JFrame-kehys). Sisältää piirtopaneelin ja sivupaneelin ja
 * huolehtii piirtopaneelin eventeistä (sisäluokalla).
 * 
 * @author Hannu Hartikainen
 */
public class NbodyFrame extends JFrame {

    private World world;
    private NbodyPanel nbodyPanel;
    private ScheduledFuture<?> simulationHandle;
    private PlanetPanel planetPanel;
    public boolean waitingForVelocity;
    private JLabel statusLabel;
    private NbodyPanelMouseListener nbodyPanelMouseListener;

    private int mouseDragLastX;
    private int mouseDragLastY;

    public NbodyFrame(World world) {
        this.setJMenuBar(this.createMenuBar());

        this.world = world;
        this.nbodyPanel = new NbodyPanel(this.world);
        this.planetPanel = new PlanetPanel(this, this.world);
        this.statusLabel = new JLabel();

        this.statusLabel.setText("Double click to add a planet");

        this.setLayout(new BorderLayout());

        this.add(this.nbodyPanel, BorderLayout.CENTER);
        this.add(this.planetPanel, BorderLayout.EAST);
        this.add(this.statusLabel, BorderLayout.SOUTH);
        this.pack();

        this.setTitle("nbody");

        // laitetaan ohjelma päättymään, kun ikkuna suljetaan
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ikkuna keskelle ruutua
        this.setLocationRelativeTo(null);

        // näytetään ikkuna
        this.setVisible(true);

        this.nbodyPanelMouseListener = new NbodyPanelMouseListener();
        this.nbodyPanel.addMouseListener(this.nbodyPanelMouseListener);
        this.nbodyPanel.addMouseMotionListener(this.nbodyPanelMouseListener);
        this.nbodyPanel.addMouseWheelListener(this.nbodyPanelMouseListener);

        this.simulationHandle = RenderingThread.startRendering(this.world, this.nbodyPanel, this.planetPanel);
    }

    /**
     * Luo valikkopalkin sisältöineen.
     * 
     * @return JMenubar-olio, jossa on ohjelman valikot
     */
    private JMenuBar createMenuBar() {
        JMenuBar menubar = new JMenuBar();

        JMenu worldMenu = new JMenu("World");
        JMenu helpMenu = new JMenu(("Help"));

        JMenuItem loadWorld = new JMenuItem(("Load"));
        JMenuItem saveWorld = new JMenuItem(("Save"));
        JMenuItem newWorld = new JMenuItem(("New"));
        JMenuItem quit = new JMenuItem(("Quit"));

        JMenuItem about = new JMenuItem(("About..."));

        newWorld.setAccelerator(KeyStroke.getKeyStroke("F2"));

        saveWorld.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveWorld();
            }
        });

        menubar.add(worldMenu);
        menubar.add(helpMenu);

        worldMenu.add(newWorld);
        worldMenu.addSeparator();
        worldMenu.add(loadWorld);
        worldMenu.add(saveWorld);
        worldMenu.addSeparator();
        worldMenu.add(quit);

        helpMenu.add(about);

        return menubar;
    }

    /**
     * Metodi, jota kutsutaan tallennus-valikkoitemistä.
     */
    protected void saveWorld() {
        new JFileChooser();
    }

    protected NbodyPanel getNbodyPanel() {
        return this.nbodyPanel;
    }

    protected boolean toggleSimulation() {
        if (this.world.isSimulationRunning()) {
            this.world.setSimulationRunning(false);
            return false;
        } else {
            this.world.setSimulationRunning(true);
            return true;
        }
    }

    /**
     * Hiirikuuntelija, joka kerää klikkaukset, liikkeet ja rullan liikkeet.
     * Sisältää osan päheyslogiikasta (tm); NbodyPanel sisältää loput.
     * 
     * @author Hannu Hartikainen
     */
    private class NbodyPanelMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener {
        public void mouseClicked(MouseEvent e) {
            // jos planeetan lisäys kesken, klikkaamalla tehdään nopeusvektori
            if (world.hasPendingPlanet()) {
                nbodyPanel.setPendingPlanetVelocity(e.getX(), e.getY());
                return;
            }

            // tuplaklikkauksella lisätään planeetta
            if (e.getClickCount() == 2) {
                Planet pendingPlanet = nbodyPanel.addPendingPlanet(e.getX(), e.getY());
                planetPanel.updatePlanetList();
                planetPanel.setPlanet(pendingPlanet);
            }
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            // valmistaudutaan raahaukseen tallentamalla ekat koordinaatit
            mouseDragLastX = e.getX();
            mouseDragLastY = e.getY();
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
            // siirretään näkymää ja tallennetaan koordinaatit
            nbodyPanel.moveView(e.getX() - mouseDragLastX, e.getY() - mouseDragLastY);
            mouseDragLastX = e.getX();
            mouseDragLastY = e.getY();
        }

        public void mouseMoved(MouseEvent e) {
            // jos planeetan asettaminen on kesken, ilmoitetaan koordinaatit
            // vektorin piirtoa varten
            if (world.hasPendingPlanet())
                nbodyPanel.setMouse(e.getX(), e.getY());
        }

        public void mouseWheelMoved(MouseWheelEvent e) {
            // kutsutaan zoomausmetodia
            nbodyPanel.zoom(e.getX(), e.getY(), e.getWheelRotation());
            planetPanel.updateZoom();
        }
    }
}
