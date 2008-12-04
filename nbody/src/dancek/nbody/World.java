package dancek.nbody;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

import javax.vecmath.Vector2d;


/**
 * @author Hannu Hartikainen
 *
 */
public class World {
    public final static double GRAVITATIONAL_CONSTANT = 6.6743e-11;

    private static final int GENERATE_PLANET_COUNT = 1000;

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
        a.scale(World.GRAVITATIONAL_CONSTANT * planet1.getMass() * planet2.getMass() / rSquared);
        
        planet2.addForce(a);
        
        a.negate();
        planet1.addForce(a);
    }
    
    public ArrayList<Planet> getPhysicalObjects() {
        return this.planets;
    }
    
    public static World earthAndMoon() {
        World world = new World();
        
        Planet earth = new Planet(0,0,-0.04,-12.5,5.9742e24);
        Planet moon = new Planet(3.844e8,0,0,1023,7.3477e22);
        
        world.addPlanet(earth);
        world.addPlanet(moon);
        
        return world;
    }

    public static World generateWorld() {
        return generateWorld(GENERATE_PLANET_COUNT);
    }
    
    public static World generateWorld(int planetNumber) {
        World world = new World();
        Random rand = new Random();
        
//        world.addPlanet(new Planet(400,300,0,-10,1e17));
//        world.addPlanet(new Planet(200,300,0,150,4e16));
//        world.addPlanet(new Planet(300,100,30,0,2e15));

        for (int i = 0; i < planetNumber; i++) {
            double x = rand.nextInt(800);
            double y = rand.nextInt(600);
            double xvel = rand.nextDouble() * 100;
            double yvel = rand.nextDouble() * 100;
            double mass = rand.nextDouble() * 1e18;
            world.addPlanet(new Planet(x,y,xvel,yvel,mass));
        }
        return world;
    }
}
