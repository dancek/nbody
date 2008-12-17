/**
 * 
 */
package dancek.nbody;

import java.util.concurrent.ScheduledFuture;

/**
 * @author Hannu Hartikainen
 *
 */
public class Game {

    
    /**
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        World world = World.generateWorld(200);
        
        ScheduledFuture<?> physicsThreadHandle = RenderingThread.startPhysics(world);
        
        Thread.sleep(5000);
        
        physicsThreadHandle.cancel(false);
    }

}
