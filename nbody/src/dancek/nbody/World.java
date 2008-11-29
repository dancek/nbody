package dancek.nbody;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.vecmath.Vector2d;


/**
 * @author Hannu Hartikainen
 *
 */
public class World {
    public final static double gravitationalConstant = 6.6743e-11;

    private ArrayList<Planet> planets;
    
    public World() {
        this.planets = new ArrayList<Planet>();
    }
    
    public void addPlanet(Planet planet) {
        this.planets.add(planet);
    }
    
    public void resetAllForces() {
        ListIterator<Planet> itr = this.planets.listIterator();
        Planet planet;
        
        while (itr.hasNext()) {
            planet = itr.next();
            planet.resetForce();
        }
    }
    
    public synchronized void updateAll(double dt) {
        ListIterator<Planet> itr = this.planets.listIterator();
        Planet planet;

        while (itr.hasNext()) {
            planet = itr.next();
            planet.update(dt);
        }
    }
    
    public void gravitateAll() {
        ListIterator<Planet> itr1 = this.planets.listIterator();
        ListIterator<Planet> itr2;
        Planet planet1 = null, planet2 = null;

        if (itr1.hasNext())
            planet1 = itr1.next();

        while (itr1.hasNext()) {
            itr2 = this.planets.listIterator(itr1.nextIndex());

            while (itr2.hasNext()) {
                planet2 = itr2.next();
                
                this.gravitate(planet1, planet2);
            }

            planet1 = itr1.next();
        }
    }
    
    public void gravitate(Planet planet1, Planet planet2) {
        Vector2d a = planet1.getPosition();
        a.sub(planet2.getPosition());
        
        double rSquared = a.lengthSquared();
        
        a.normalize();
        a.scale(World.gravitationalConstant * planet1.getMass() * planet2.getMass() / rSquared);
        
        planet2.addForce(a);
        
        a.negate();
        planet1.addForce(a);
    }
}
