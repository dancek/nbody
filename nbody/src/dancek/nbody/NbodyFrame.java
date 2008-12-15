/**
 * 
 */
package dancek.nbody;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
    private boolean simulationRunning;
    private PlanetPanel planetPanel;
    private JPanel sidePanel;
    public boolean waitingForVelocity;
    private JLabel statusLabel;

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

        this.nbodyPanel.addMouseListener(new NbodyPanelClickListener());

        this.simulationHandle = Physics.startPhysics(this.world, this.nbodyPanel, this.planetPanel);
        this.simulationRunning = true;
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

//        newGame.addActionListener(new NewGameListener());
//        selectMap.addActionListener(new SelectMapListener());
//        highScores.addActionListener(new HighScoreListener());
//        quit.addActionListener(new QuitListener());
//
//        changeLanguage.addActionListener(new ChangeLanguageListener());

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

    public void updateSimulationView() {
        this.nbodyPanel.repaint();
    }

    private class NbodyPanelClickListener implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            if (waitingForVelocity) {
                nbodyPanel.setPendingPlanetVelocity(e.getX(), e.getY());
                planetPanel.setPendingPlanet(null);
                waitingForVelocity = false;
                return;
            }

            if (e.getClickCount() == 2) {
                Planet pendingPlanet = nbodyPanel.addPendingPlanet(e.getX(), e.getY());
                planetPanel.setPendingPlanet(pendingPlanet);
                waitingForVelocity = true;
            }

//            if (simulationRunning) {
//                simulationHandle.cancel(false);
//                simulationRunning = false;
//            } else {
//                simulationHandle = Physics.startPhysics(world, nbodyPanel, planetPanel);
//                simulationRunning = true;
//            }
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
