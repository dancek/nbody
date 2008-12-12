package dancek.nbody;

/**
 * Pääluokka, joka sisältää main-metodin. Jos teen pelistä appletin, yritän
 * laittaa tämän luokan käynnistämään myös sen.
 * 
 * @author Hannu Hartikainen
 */
public class Nbody {

    /**
     * Main-metodi.
     * 
     * @param args komentoriviargumentit
     */
    public static void main(String[] args) {
        new NbodyFrame(World.quickEarthAndMoon());
    }
}
