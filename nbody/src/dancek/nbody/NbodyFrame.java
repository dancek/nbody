package dancek.nbody;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ScheduledFuture;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import dancek.io.RegexFileFilter;

/**
 * Ohjelman ikkuna (JFrame-kehys). Sisältää piirtopaneelin ja sivupaneelin ja
 * huolehtii piirtopaneelin eventeistä (sisäluokalla).
 * 
 * @author Hannu Hartikainen
 */
public class NbodyFrame extends JFrame {

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
                statusLabel.setText("Double click to add a planet");
                return;
            }

            // tuplaklikkauksella lisätään planeetta
            if (e.getClickCount() == 2) {
                Planet pendingPlanet = nbodyPanel.addPendingPlanet(e.getX(), e.getY(), defaultMass);
                planetPanel.updatePlanetList();
                planetPanel.setPlanet(pendingPlanet);
                statusLabel.setText("Click to set velocity");
                return;
            }

            // jos klikattiin planeettaa, valitaan se
            Planet planet = nbodyPanel.getPlanetByLocation(e.getX(), e.getY());
            if (planet != null)
                planetPanel.setPlanet(planet);
        }

        public void mouseDragged(MouseEvent e) {
            // siirretään näkymää ja tallennetaan koordinaatit
            nbodyPanel.moveView(e.getX() - mouseDragLastX, e.getY() - mouseDragLastY);
            mouseDragLastX = e.getX();
            mouseDragLastY = e.getY();
            planetPanel.updateZoomAndPosition();

            // jos planeetan asettaminen on kesken, ilmoitetaan koordinaatit
            // vektorin piirtoa varten
            if (world.hasPendingPlanet())
                nbodyPanel.setMouse(e.getX(), e.getY());
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
            // jos planeetan asettaminen on kesken, ilmoitetaan koordinaatit
            // vektorin piirtoa varten
            if (world.hasPendingPlanet())
                nbodyPanel.setMouse(e.getX(), e.getY());
        }

        public void mousePressed(MouseEvent e) {
            // valmistaudutaan raahaukseen tallentamalla ekat koordinaatit
            mouseDragLastX = e.getX();
            mouseDragLastY = e.getY();
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseWheelMoved(MouseWheelEvent e) {
            // kutsutaan zoomausmetodia
            nbodyPanel.zoom(e.getX(), e.getY(), e.getWheelRotation());
            planetPanel.updateZoomAndPosition();
        }
    }

    /**
     * Maailmatiedostoihin liittyvät vakiot (pääte .world).
     */
    private static final String WORLD_FILE_EXTENSION = "world";
    private static final String WORLD_FILE_REGEX = ".*\\." + WORLD_FILE_EXTENSION;
    private static final FileFilter WORLD_FILE_FILTER = new RegexFileFilter(WORLD_FILE_REGEX, "nbody (*."
            + WORLD_FILE_EXTENSION + ")");

    /**
     * Attribuutit
     */
    private World world;
    private NbodyPanel nbodyPanel;
    private ScheduledFuture<?> simulationHandle;
    private PlanetPanel planetPanel;
    private JLabel statusLabel;
    private NbodyPanelMouseListener nbodyPanelMouseListener;
    private int mouseDragLastX;
    private int mouseDragLastY;
    private JScrollPane scrollPane;
    private AboutDialog aboutDialog;
    private double defaultMass;

    /**
     * Konstruktori. Asettelee asiat paikoilleen ikkunaan, luo tarvittavat oliot
     * ja lisää hiirikuuntelijat.
     * 
     * @param world maailma
     */
    public NbodyFrame(World world) {
        this.defaultMass = Planet.DEFAULT_MASS;
        this.setJMenuBar(this.createMenuBar());

        this.nbodyPanel = new NbodyPanel(this, world);
        this.planetPanel = new PlanetPanel(this, world);
        this.scrollPane = new JScrollPane(this.planetPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.statusLabel = new JLabel();

        this.statusLabel.setText("Double click to add a planet");

        this.setLayout(new BorderLayout());

        this.add(this.nbodyPanel, BorderLayout.CENTER);
        this.add(this.scrollPane, BorderLayout.EAST);
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

        this.setWorld(world);
    }

    /**
     * Kertoo näytetäänkö näkymässä planeettojen nimet. Kysytään NbodyFramen
     * kautta, koska en ollut varma haluanko valinnan valikkoon vai
     * sivupaneeliin.
     * 
     * @return piirretäänkö planeettojen nimet
     */
    public boolean isPlanetNamesEnabled() {
        return this.planetPanel.isPlanetNamesEnabled();
    }

    /**
     * Asettaa oletusmassan uusille planeetoille.
     * 
     * @param mass massa
     */
    public void setDefaultMass(double mass) {
        this.defaultMass = mass;
    }

    /**
     * Näyttää viestin jossa on tärkeimpiä tietoja ohjelmasta.
     */
    protected void about() {
        if (this.aboutDialog == null)
            this.aboutDialog = new AboutDialog(this, false);

        this.aboutDialog.setVisible(true);
    }

    /**
     * Antaa käytössä olevan NbodyPanel-olion.
     * 
     * @return NbodyPanel-olio
     */
    protected NbodyPanel getNbodyPanel() {
        return this.nbodyPanel;
    }

    /**
     * Kertoo piirretäänkö ruudukko. Kysytään NbodyFramen kautta, koska en ollut
     * varma haluanko valinnan valikkoon vai sivupaneeliin.
     * 
     * @return piirretäänkö ruudukko
     */
    protected boolean isGridEnabled() {
        return this.planetPanel.isGridEnabled();
    }

    /**
     * Hoitaa maailman lataamisen.
     */
    protected void loadWorld() {
        JFileChooser fileChooser;
        
        try {
            fileChooser = new JFileChooser();
        } catch (Exception e) {
            this.setWorld(Nbody.getDefaultWorld());
            return;
        }
        
        fileChooser.setFileFilter(WORLD_FILE_FILTER);
        fileChooser.showOpenDialog(this);
        try {
            File file = fileChooser.getSelectedFile();
            if (file == null)
                return;

            this.setWorld(World.load(file));
            this.statusLabel.setText(file.getName() + " loaded.");
        } catch (Exception e) {
            // ongelmatilanteissa informoidaan käyttäjää
            JOptionPane.showMessageDialog(this, "The file is not a proper world file.", "Unsuitable file",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Tallentaa maailman tiedostoon.
     */
    protected void saveWorld() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(WORLD_FILE_FILTER);
        fileChooser.showSaveDialog(this);

        try {
            File file = fileChooser.getSelectedFile();
            if (file == null)
                return;

            // yritetään huolehtia, että tiedosto saa .world-päätteen
            if (!file.getName().matches(WORLD_FILE_REGEX))
                file = new File(file.getCanonicalPath() + "." + WORLD_FILE_EXTENSION);

            this.world.save(file);
            this.statusLabel.setText(file.getName() + " saved.");
        } catch (IOException e) {
            // ongelmatilanteissa informoidaan käyttäjää
            JOptionPane.showMessageDialog(this, "The file could not be written.", "Problem saving world",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Kytkee simulaation päälle tai pois.
     * 
     * @return onko simulaatio käynnissä
     */
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
     * Luo valikkopalkin sisältöineen.
     * 
     * @return JMenubar-olio, jossa on ohjelman valikot
     */
    private JMenuBar createMenuBar() {
        JMenuBar menubar = new JMenuBar();

        JMenu worldMenu = new JMenu("World");
        JMenu helpMenu = new JMenu(("Help"));

        JMenuItem newWorld = new JMenuItem(("New"));
        JMenuItem loadWorld = new JMenuItem(("Load"));
        JMenuItem saveWorld = new JMenuItem(("Save"));
        JMenuItem quit = new JMenuItem(("Quit"));

        JMenuItem about = new JMenuItem(("About..."));

        newWorld.setAccelerator(KeyStroke.getKeyStroke("F2"));

        newWorld.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setWorld(new World());
            }
        });

        loadWorld.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadWorld();
            }
        });

        saveWorld.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveWorld();
            }
        });

        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                about();
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
     * Asettaa käytössä olevan maailman. Huolehtii myös fysiikkamallinnuksen ja
     * piirtämisen käynnistämisestä.
     * 
     * @param world maailma
     */
    private void setWorld(World world) {
        this.world = world;
        this.nbodyPanel.setWorld(this.world);
        this.planetPanel.setWorld(this.world);

        if (this.simulationHandle != null)
            this.simulationHandle.cancel(false);

        this.world.setSimulationRunning(!this.planetPanel.isSimulationPaused());

        this.simulationHandle = RenderingThread.startRendering(this.world, this.nbodyPanel, this.planetPanel);
    }
}
