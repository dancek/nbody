/**
 * 
 */
package dancek.nbody;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.concurrent.ScheduledFuture;

import javax.swing.*;

/**
 * Ohjelman ikkuna (JFrame-kehys).
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

        JMenu gameMenu = new JMenu("gameMenu");
        JMenu settingsMenu = new JMenu(("settingsMenu"));

        JMenuItem newGame = new JMenuItem(("newGame"));
        JMenuItem selectMap = new JMenuItem(("selectMap"));
        JMenuItem highScores = new JMenuItem(("highScores"));
        JMenuItem quit = new JMenuItem(("quit"));

        JMenuItem changeLanguage = new JMenuItem(("changeLanguage"));

        // newGame.addActionListener(new NewGameListener());
        // selectMap.addActionListener(new SelectMapListener());
        // highScores.addActionListener(new HighScoreListener());
        // quit.addActionListener(new QuitListener());
        //
        // changeLanguage.addActionListener(new ChangeLanguageListener());

        newGame.setAccelerator(KeyStroke.getKeyStroke("F2"));

        menubar.add(gameMenu);
        menubar.add(settingsMenu);

        gameMenu.add(newGame);
        gameMenu.add(selectMap);
        gameMenu.addSeparator();
        gameMenu.add(highScores);
        gameMenu.addSeparator();
        gameMenu.add(quit);

        settingsMenu.add(changeLanguage);

        return menubar;
    }

    protected NbodyPanel getNbodyPanel() {
        return this.nbodyPanel;
    }

    protected void updateSimulationView() {
        this.nbodyPanel.repaint();
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

    private class NbodyPanelMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener {
        public void mouseClicked(MouseEvent e) {
            if (world.hasPendingPlanet()) {
                nbodyPanel.setPendingPlanetVelocity(e.getX(), e.getY());
                planetPanel.setPendingPlanet(null);
                return;
            }

            if (e.getClickCount() == 2) {
                Planet pendingPlanet = nbodyPanel.addPendingPlanet(e.getX(), e.getY());
                planetPanel.setPendingPlanet(pendingPlanet);
            }
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            mouseDragLastX = e.getX();
            mouseDragLastY = e.getY();
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
            nbodyPanel.moveView(e.getX() - mouseDragLastX, e.getY() - mouseDragLastY);
            mouseDragLastX = e.getX();
            mouseDragLastY = e.getY();
        }

        public void mouseMoved(MouseEvent e) {
            if (world.hasPendingPlanet())
                nbodyPanel.setMouse(e.getX(), e.getY());
        }

        public void mouseWheelMoved(MouseWheelEvent e) {
            nbodyPanel.zoom(e.getX(), e.getY(), e.getWheelRotation());
        }
    }
}
