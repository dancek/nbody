/**
 * 
 */
package dancek.nbody;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Hannu Hartikainen
 *
 */
public class Physics implements Runnable {

    private static final int PHYSICS_FPS = 50;
    private static final double PHYSICS_TIMESTEP = 1.0 / PHYSICS_FPS;
    
    public void run() {
        
    }

    
    public static ScheduledFuture<?> startPhysics(final World world) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
        Runnable gamePhysics = new Runnable() {
            public void run() {
                System.out.println(System.currentTimeMillis());
                world.resetAllForces();
                world.gravitateAll();
                world.updateAll(PHYSICS_TIMESTEP);
            }
        };
        
        return scheduler.scheduleAtFixedRate(gamePhysics, 0, (long) (1000 * PHYSICS_TIMESTEP), TimeUnit.MILLISECONDS);
    }

}
