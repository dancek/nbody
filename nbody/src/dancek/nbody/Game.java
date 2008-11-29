/**
 * 
 */
package dancek.nbody;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.vecmath.Vector2d;

/**
 * @author Hannu Hartikainen
 *
 */
@SuppressWarnings("unused")
public class Game {

    private static final int physicsFPS = 50;
    private static final double physicsTimestep = 1.0 / physicsFPS;
    
    
    public static ScheduledFuture<?> startPhysics(final World world) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable gamePhysics = new Runnable() {
            public void run() {
                System.out.println(System.currentTimeMillis());
                world.resetAllForces();
                world.gravitateAll();
                world.updateAll(physicsTimestep);
            }
        };
        
        return scheduler.scheduleAtFixedRate(gamePhysics, 0, (long) (1000 * physicsTimestep), TimeUnit.MILLISECONDS);
    }
    
    /**
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        final World world = new World();
        
        Planet earth = new Planet(0,0,-0.04,-12.5,5.9742e24);
        Planet moon = new Planet(3.844e8,0,0,1023,7.3477e22);
        
        world.addPlanet(earth);
        world.addPlanet(moon);
        
        ScheduledFuture<?> physicsThreadHandle = startPhysics(world);
        
        for (int i = 0; i < 1000; i++) {
           System.out.println(i + " Moon: " + moon.getPosition() + "; Earth: " + earth.getPosition() + " ("+ System.currentTimeMillis() + ")");
        }
        
        physicsThreadHandle.cancel(false);
    }

}
