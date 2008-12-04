package dancek.nbody;

import java.util.concurrent.ScheduledFuture;

import javax.vecmath.Vector2d;

import jgame.JGColor;
import jgame.JGFont;
import jgame.JGObject;
import jgame.platform.JGEngine;

/**
 * @author Hannu Hartikainen
 * 
 */
public class Nbody extends JGEngine {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private ScheduledFuture<?> physicsThreadHandle;
    private World world;
    private long lastFrame;

    /**
     * The constructor that is used for applets.
     */
    public Nbody() {

    }

    public Nbody(World world) {
        this.world = world;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        World world = World.generateWorld(100);
        Nbody nbody = new Nbody(world);

        nbody
                .setCanvasSettings(80, 60, 10, 10, JGColor.white, JGColor.black, new JGFont("Verdana", JGFont.PLAIN,
                        10.0));
        nbody.initEngine(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jgame.platform.JGEngine#initCanvas()
     */
    @Override
    public void initCanvas() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see jgame.platform.JGEngine#initGame()
     */
    @Override
    public void initGame() {
        for (PhysicalObject obj : this.world.getPhysicalObjects()) {
            if (obj instanceof Planet)
                new JGPlanet((Planet) obj);
        }
        this.physicsThreadHandle = Physics.startPhysics(this.world);
    }

    public void doFrame() {
        this.moveObjects();
    }

    private class JGPlanet extends JGObject {
        private double size;
        private Planet planet;
        private Vector2d position;

        public JGPlanet(Planet p) {
            super("", true, p.getPosition().x, p.getPosition().y, 0, null);
            this.size = 20;
            this.planet = p;
        }

        public void move() {
            this.position = this.planet.getPosition();
            this.x = this.position.x;
            this.y = this.position.y;
        }

        public void paint() {
            setColor(JGColor.white);
            drawOval((int) x, (int) y, size, size, false, true);
        }
    }
}
