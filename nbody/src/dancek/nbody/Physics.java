/**
 * 
 */
package dancek.nbody;

import java.awt.Graphics2D;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

/**
 * @author Hannu Hartikainen
 * 
 */
public class Physics implements Runnable {

    private static final int PHYSICS_FPS = 50;
    protected static final double PHYSICS_TIMESTEP = 1.0 / PHYSICS_FPS;

    public void run() {

    }

    public static ScheduledFuture<?> startPhysics(final World world) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable gamePhysics = new Runnable() {
            public void run() {
                world.resetAllForces();
                world.gravitateAll();
                world.updateAll(PHYSICS_TIMESTEP);
            }
        };

        return scheduler.scheduleAtFixedRate(gamePhysics, 0, (long) (1000 * PHYSICS_TIMESTEP), TimeUnit.MILLISECONDS);
    }

    public static ScheduledFuture<?> startPhysics(final World world, final JPanel displayPanel, final JPanel planetPanel) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable gamePhysics = new Runnable() {
            public void run() {
                world.resetAllForces();
                world.gravitateAll();
                world.updateAll(PHYSICS_TIMESTEP);
                displayPanel.repaint();
                planetPanel.repaint();
            }
        };

        return scheduler.scheduleAtFixedRate(gamePhysics, 0, (long) (1000 * PHYSICS_TIMESTEP), TimeUnit.MILLISECONDS);
    }

}
