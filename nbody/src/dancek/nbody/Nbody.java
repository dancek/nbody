package dancek.nbody;

import java.util.Random;

import javax.swing.SwingUtilities;

/**
 * Pääluokka, joka sisältää main-metodin. Jos teen pelistä appletin, yritän
 * laittaa tämän luokan käynnistämään myös sen.
 * 
 * @author Hannu Hartikainen
 */
public class Nbody {

    public static final Random rand = new Random();

    /**
     * Main-metodi.
     * 
     * @param args komentoriviargumentit
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new NbodyFrame(World.quickTestWorld());
            }
        });
    }
}
