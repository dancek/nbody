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
public class NbodyJGame extends JGEngine {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private ScheduledFuture<?> physicsThreadHandle;
    private World world;
    private long lastFrame;

    /**
     * The constructor that is used for applets.
     */
    public NbodyJGame() {
        this.world = World.generateWorld(2);
        this.initEngineApplet();
    }

    public NbodyJGame(World world) {
        this.world = world;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        World world = World.quickEarthAndMoon();
        NbodyJGame nbody = new NbodyJGame(world);

        nbody.initCanvas();

        nbody.initEngine(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jgame.platform.JGEngine#initCanvas()
     */
    @Override
    public void initCanvas() {
        this.setCanvasSettings(80, 60, 10, 10, JGColor.white, JGColor.black, new JGFont("Verdana", JGFont.PLAIN, 10.0));
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
        this.checkCollision(1, 1);
    }

    private class JGPlanet extends JGObject {
        private int size;
        private Planet planet;
        private Vector2d position;

        public JGPlanet(Planet p) {
            super("", true, p.getPosition().x, p.getPosition().y, 1, null);
            this.planet = p;
            this.size = (int) Math.log(p.getMass());
            this.setBBox(-size / 2, -size / 2, size, size);
        }

        public void move() {
            this.position = this.planet.getPosition();
            this.x = this.position.x;
            this.y = this.position.y;
        }

        public void paint() {
            setColor(JGColor.white);
            drawOval((int) x, (int) y, size, size, true, true);
        }

        public void hit(JGObject obj) {
            System.out.println("collision");
        }
    }
}
