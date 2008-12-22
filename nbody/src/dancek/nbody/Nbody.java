package dancek.nbody;

import java.io.InputStream;
import java.util.Random;

import javax.swing.SwingUtilities;

/**
 * Pääluokka, joka sisältää main-metodin. Jos teen pelistä appletin, yritän
 * laittaa tämän luokan käynnistämään myös sen.
 * 
 * @author Hannu Hartikainen
 */
public class Nbody {

    protected static final String DEFAULT_WORLD_FILENAME = "/solarsystem.world";

    public static final Random rand = new Random();

    public static final String VERSION = "1.0";

    /**
     * Main-metodi. Yrittää ladata maailman tiedostosta; jos ei onnistu, käyttää
     * Worldin staattista metodia luomaan maailman.
     * 
     * @param args komentoriviargumentit
     */
    public static void main(String[] args) {
        // käytetään invokeLater-metodia, jotta ei törmätä säieongelmiin
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                World world = null;

                try {
                    InputStream is = this.getClass().getResourceAsStream(DEFAULT_WORLD_FILENAME);
                    world = World.load(is);
                } catch (Exception e) {
                    world = World.quickTestWorld();
                } finally {
                    new NbodyFrame(world);
                }
            }
        });
    }
}
