package dancek.nbody;

import java.util.Random;

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
        new NbodyFrame(World.quickEarthAndMoon());
    }
}
