package dancek.nbody;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

import dancek.vecmath.SimpleVector;

/**
 * Luokka, joka sisältää maailmassa olevat asiat (käytännössä planeetat).
 * 
 * @author Hannu Hartikainen
 */
public class World {
    // gravitaatiovakio (kopioitu reaalimaailmasta)
    public final static double GRAVITATIONAL_CONSTANT = 6.6743e-11;

    private static final int GENERATE_PLANET_COUNT = 1000;

    // kaksi planeettalistaa, jotta osa planeetoista voi olla ilman fysiikkaa
    // (lisäysvaihe GUI:ssa); käytännössä tähän olisi riittänyt pelkkä
    // pendingPlanet, mutta tällä tavoin koodi on selkeämpää ja tehokkaampaa
    private ArrayList<Planet> planetsWithPhysics;
    private ArrayList<Planet> allPlanets;

    private boolean simulationRunning;

    private Planet pendingPlanet;

    /**
     * Antaa parhaillaan lisättävän planeetan.
     * 
     * @return odottava planeetta
     */
    public Planet getPendingPlanet() {
        return this.pendingPlanet;
    }

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
     * Laskee kahden planeetan välisen gravitaatiovoiman, ja lisää sen
     * kummallekin planeetalle (voiman ja vastavoiman laki).
     * 
     * @param planet1 planeetta
     * @param planet2 planeetta
     */
    public void gravitate(Planet planet1, Planet planet2) {
        SimpleVector a = planet1.getPosition();
        a.sub(planet2.getPosition());

        double rSquared = a.lengthSquared();

        a.normalize();
        a.scale(World.GRAVITATIONAL_CONSTANT * planet1.getMass() * planet2.getMass() / rSquared);

        planet2.addForce(a);

        a.negate();
        planet1.addForce(a);
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
     * Kertoo, pitäisikö simulaation pyöriä.
     * 
     * @return true jos fysiikkaa lasketaan
     */
    public boolean isSimulationRunning() {
        return this.simulationRunning;
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
     * Antaa maailman, jossa on maa ja kuu oikeilla arvoilla. Käytin tätä
     * jossain vaiheessa fysiikan testaamiseen.
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
     * Luo testimaailman, jota olen käyttänyt ohjelmaa kehittäessä.
     * 
     * @return maailma, jossa muutama planeetta
     */
    public static World quickTestWorld() {
        World world = new World();

        Planet sun = new Planet(0, 0, 0, 0, 1e40);
        Planet earth = new Planet(0, 200, 160, 0, 1e15);
        Planet moon = new Planet(200, 30, 40, -40, 1e9);

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
     * @param bigger suurempi kappale
     * @param smaller pienempi kappale
     */
    public static void stabilize(Planet bigger, Planet smaller) {
        // // huolehditaan että planeetat on oikein päin
        // if (bigger.getMass() < smaller.getMass()) {
        // Planet temp = smaller;
        // smaller = bigger;
        // bigger = temp;
        // }

        SimpleVector distanceVector = bigger.getPosition();
        distanceVector.sub(smaller.getPosition());

        double r = distanceVector.length();
        double vel = smaller.getLinearVelocity();

        bigger.setMass(r * vel * vel / GRAVITATIONAL_CONSTANT);
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

    /**
     * Onko odottavaa planeettaa.
     * 
     * @return true, jos maailmassa on lisäystä odottava planeetta
     */
    public boolean hasPendingPlanet() {
        return (this.pendingPlanet != null);
    }
}
