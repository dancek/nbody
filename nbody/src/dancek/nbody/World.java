package dancek.nbody;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

import dancek.vecmath.SimpleVector;

/**
 * Luokka, joka sisältää maailmassa olevat asiat (käytännössä planeetat).
 * 
 * @author Hannu Hartikainen
 */
public class World {

    /**
     * Gravitaatiovakio (reaalimaailman arvo).
     */
    public final static double GRAVITATIONAL_CONSTANT = 6.6743e-11;

    /**
     * Antaa maailman, jossa on maa ja kuu oikeilla arvoilla. Käytin tätä
     * jossain vaiheessa fysiikan testaamiseen. Koska kuun kiertoaika on noin
     * kuukausi, ja simulaatio pyörii oletuksena reaaliajassa, tällä maailmalla
     * ei ole juuri käyttöä lopullisessa ohjelmassa.
     * 
     * @return maailma, jossa maa ja kuu
     */
    public static World earthAndMoon() {
        World world = new World();

        Planet earth = new Planet(0, 0, -0.04, -12.5, 5.9742e24);
        Planet moon = new Planet(3.844e8, 0, 0, 1023, 7.3477e22);

        world.addPlanet(earth);
        world.addPlanet(moon);

        return world;
    }

    /**
     * Lataa planeetan. Kutsuu load(Scanner)-metodia.
     * 
     * @param file tiedosto
     * @return maailma
     * @throws NumberFormatException
     * @throws FileNotFoundException
     */
    public static World load(File file) throws FileNotFoundException, NumberFormatException {
        return load(new Scanner(file));
    }

    /**
     * Lataa planeetan. Kutsuu load(Scanner)-metodia.
     * 
     * @param is stream-olio
     * @return maailma
     * @throws NumberFormatException
     */
    public static World load(InputStream is) throws NumberFormatException {
        return load(new Scanner(is));
    }

    /**
     * Lataa planeetan. Parsii tiedoston rivi kerrallaan ja kutsuu Planetin
     * konstruktoria saaduilla parametreilla. Palauttaa maailman, jossa on
     * kaikki luodut planeetat.
     * 
     * @param scanner lukijaolio
     * @return maailma
     * @throws NumberFormatException
     */
    public static World load(Scanner scanner) throws NumberFormatException {
        World world = new World();

        while (scanner.hasNextLine()) {
            // pilkotaan rivi paloiksi ja tulkitaan joka pala
            String line = scanner.nextLine();
            String[] parameters = line.split(";");

            double positionX = Double.parseDouble(parameters[0]);
            double positionY = Double.parseDouble(parameters[1]);
            double velocityX = Double.parseDouble(parameters[2]);
            double velocityY = Double.parseDouble(parameters[3]);
            double mass = Double.parseDouble(parameters[4]);
            String name = parameters[5];
            Color color = new Color(Integer.parseInt(parameters[6]));

            Planet planet = new Planet(positionX, positionY, velocityX, velocityY, mass, name, color);

            world.addPlanet(planet);
        }

        return world;
    }

    /**
     * Luo testimaailman, jota olen käyttänyt ohjelmaa kehittäessä. Tätä
     * käytetään edelleen, jos oletusmaailman lukeminen tiedostosta epäonnistuu.
     * 
     * @return maailma, jossa muutama planeetta
     */
    public static World quickTestWorld() {
        World world = new World();

        Planet sun = new Planet(0, 0, 0, 0, 1e40);
        Planet earth = new Planet(0, 200, 160, 0, 1e15);
        Planet moon = new Planet(200, 30, 40, -60, 1e9);

        // World.stabilize(earth, moon);
        World.stabilize(sun, earth);

        world.addPlanet(sun);
        world.addPlanet(earth);
        world.addPlanet(moon);

        return world;
    }

    /**
     * Yrittää tehdä mahdollisimman stabiilin järjestelmän kahdesta annetusta
     * kappaleesta muuttamalla massiivisemman massaa. Toimii vaatimalla, että
     * a=v^2/r, kun F=ma ja F=GmM/r^2, josta saadaan ehto M=rv^2/G.
     * 
     * Tätä ei käytetä lopullisessa ohjelmassa. Suunnittelin toisenkin
     * stabilointimetodin tekemistä (nopeutta säätämällä) ja näiden liittämistä
     * käyttöliittymään, mutta sille ei ollut aikaa.
     * 
     * @param bigger suurempi kappale (jonka massaa muutetaan)
     * @param smaller pienempi kappale
     */
    public static void stabilize(Planet bigger, Planet smaller) {
        // // huolehditaan että planeetat on oikein päin
        // if (bigger.getMass() < smaller.getMass()) {
        // Planet temp = smaller;
        // smaller = bigger;
        // bigger = temp;
        // }

        SimpleVector distanceVector = bigger.getPosition().clone();
        distanceVector.sub(smaller.getPosition());

        double r = distanceVector.length();
        double vel = smaller.getLinearVelocity();

        bigger.setMass(r * vel * vel / GRAVITATIONAL_CONSTANT);
    }

    /*
     * kaksi planeettalistaa, jotta osa planeetoista voi olla ilman fysiikkaa
     * (lisäysvaihe GUI:ssa); käytännössä tähän olisi riittänyt pelkkä
     * pendingPlanet-muuttuja, mutta tällä tavoin koodi on selkeämpää ja
     * tehokkaampaa
     */
    private ArrayList<Planet> planetsWithPhysics;
    private ArrayList<Planet> allPlanets;
    private boolean simulationRunning;
    private Planet pendingPlanet;

    /**
     * Konstruktori. Ei mitään ihmeellistä.
     */
    public World() {
        this.planetsWithPhysics = new ArrayList<Planet>();
        this.allPlanets = new ArrayList<Planet>();
        this.simulationRunning = true;
    }

    /**
     * Lisää planeetan.
     * 
     * @param planet planeetta
     */
    public void addPlanet(Planet planet) {
        if (planet.equals(this.pendingPlanet)) {
            this.planetsWithPhysics.add(planet);
            this.pendingPlanet = null;
        } else {
            this.planetsWithPhysics.add(planet);
            this.allPlanets.add(planet);
        }
    }

    /**
     * Antaa parhaillaan lisättävän planeetan.
     * 
     * @return odottava planeetta
     */
    public Planet getPendingPlanet() {
        return this.pendingPlanet;
    }

    /**
     * Antaa kaikki maailmassa olevat planeetat (myös ne, joille ei lasketa
     * fysiikkaa).
     * 
     * @return lista planeetoista
     */
    public ArrayList<Planet> getPlanets() {
        return this.allPlanets;
    }

    /**
     * Laskee kahden planeetan välisen gravitaatiovoiman, ja lisää sen
     * kummallekin planeetalle (voiman ja vastavoiman laki).
     * 
     * @param planet1 planeetta
     * @param planet2 planeetta
     */
    public void gravitate(Planet planet1, Planet planet2) {
        SimpleVector a = planet1.getPosition().clone();
        a.sub(planet2.getPosition());

        double rSquared = a.lengthSquared();

        a.normalize();
        a.scale(World.GRAVITATIONAL_CONSTANT * planet1.getMass() * planet2.getMass() / rSquared);

        planet2.addForce(a);

        a.negate();
        planet1.addForce(a);
    }

    /**
     * Laskee kaikkien planeettojen väliset gravitaatiovoimat. Hyödyntää voiman
     * ja vastavoiman lakia, eli joka laskulla saadaan kaksi voimaa eikä lasketa
     * samoja planeettoja enää toisin päin.
     */
    public void gravitateAll() {
        ListIterator<Planet> itr1 = this.planetsWithPhysics.listIterator();
        ListIterator<Planet> itr2;
        Planet planet1 = null, planet2 = null;

        if (itr1.hasNext())
            planet1 = itr1.next();

        while (itr1.hasNext()) {
            itr2 = this.planetsWithPhysics.listIterator(itr1.nextIndex());

            while (itr2.hasNext()) {
                planet2 = itr2.next();

                this.gravitate(planet1, planet2);
            }

            planet1 = itr1.next();
        }
    }

    /**
     * Onko odottavaa planeettaa.
     * 
     * @return true, jos maailmassa on lisäystä odottava planeetta
     */
    public boolean hasPendingPlanet() {
        return (this.pendingPlanet != null);
    }

    /**
     * Kertoo, pitäisikö simulaation pyöriä.
     * 
     * @return true jos fysiikkaa lasketaan
     */
    public boolean isSimulationRunning() {
        return this.simulationRunning;
    }

    /**
     * Poistaa planeetan.
     * 
     * @param planet planeetta
     */
    public void removePlanet(Planet planet) {
        if (planet.equals(this.pendingPlanet)) {
            this.pendingPlanet = null;
            this.allPlanets.remove(planet);
        } else {
            this.planetsWithPhysics.remove(planet);
            this.allPlanets.remove(planet);
        }
    }

    /**
     * Nollaa planeetoihin vaikuttavat voimat.
     */
    public void resetAllForces() {
        ListIterator<Planet> itr = this.planetsWithPhysics.listIterator();
        Planet planet;

        while (itr.hasNext()) {
            planet = itr.next();
            planet.resetForce();
        }
    }

    /**
     * Tallentaa maailman tiedostoon. Heittää mahdolliset poikkeukset kutsuvalle
     * metodille.
     * 
     * @param file tiedosto
     * @throws IOException
     */
    public void save(File file) throws IOException {
        FileWriter writer = new FileWriter(file);

        for (Planet planet : this.getPlanets()) {
            String planetString = "";
            planetString += planet.getPosition().x + ";";
            planetString += planet.getPosition().y + ";";
            planetString += planet.getVelocity().x + ";";
            planetString += planet.getVelocity().y + ";";
            planetString += planet.getMass() + ";";
            planetString += planet.getName() + ";";
            planetString += planet.getColor().getRGB() + "\n";

            writer.write(planetString);
        }

        writer.close();
    }

    /**
     * Asettaa fysiikan simuloinnin päälle tai pois.
     * 
     * @param run true jos fysiikkaa lasketaan
     */
    public void setSimulationRunning(boolean run) {
        this.simulationRunning = run;
    }

    /**
     * Kutsuu kunkin planeetan update-metodia.
     * 
     * @param dt aika-askel
     */
    public synchronized void updateAll(double dt) {
        ListIterator<Planet> itr = this.planetsWithPhysics.listIterator();
        Planet planet;

        while (itr.hasNext()) {
            planet = itr.next();
            planet.update(dt);
        }
    }

    /**
     * Lisää planeetan, jolle ei vielä lasketa fysiikkaa.
     * 
     * @param pendingPlanet planeetta
     */
    protected void addPendingPlanet(Planet pendingPlanet) {
        this.allPlanets.add(pendingPlanet);
        this.pendingPlanet = pendingPlanet;
    }
}
