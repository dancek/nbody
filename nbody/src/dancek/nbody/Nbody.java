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
     * Oletusmaailman pulauttava metodi. Yrittää ladata maailman tiedostosta;
     * jos ei onnistu, käyttää Worldin staattista metodia luomaan maailman.
     * 
     * @return oletusmaailma
     */
    public static World getDefaultWorld() {
        World world = null;

        try {
            InputStream is = Nbody.class.getResourceAsStream(DEFAULT_WORLD_FILENAME);
            world = World.load(is);
        } catch (Exception e) {
            world = World.quickTestWorld();
        }

        return world;
    }

    /**
     * Main-metodi. Luo NbodyFramen.
     * 
     * @param args komentoriviargumentit
     */
    public static void main(String[] args) {
        // käytetään invokeLater-metodia, jotta ei törmätä säieongelmiin
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new NbodyFrame(getDefaultWorld());
            }
        });
    }
}
