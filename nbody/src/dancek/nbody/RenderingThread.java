/**
 * 
 */
package dancek.nbody;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Luokka sisältää metodit fysiikan ja renderöinnin pyörittämiseen halutuin
 * väliajoin (omassa säikeessään). Swingissä on turvallista kutsua lähinnä
 * repaint()-metodeja tapahtumasäikeen ulkopuolelta.
 * PlanetPanel.updateComponentValues() tekee kyllä muutakin, mutta ilmeisesti se
 * on melko turvallista (siinä ei muokata komponentteja, joita käyttäjä on
 * näpelöimässä).
 * 
 * @author Hannu Hartikainen
 */
public class RenderingThread {

    // montako kertaa sekunnissa ajetaan (50-60 on kohtuullinen, enempi parempi
    // mutta vaatisi aika paljon tehoa)
    private static final int PHYSICS_FPS = 60;
    protected static final double PHYSICS_TIMESTEP = 1.0 / PHYSICS_FPS;

    /**
     * Laittaa fysiikan pyörimään muttei tee repaint()-kutsuja. Käytin tätä
     * metodia paljon kehitysvaiheessa, mutta lopullisessa ohjelmassa tätä ei
     * käytetä.
     * 
     * @param world World-olio
     * @return kahva säikeeseen
     */
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

    /**
     * Pyörittää fysiikkaa ja piirtoa (oikeastaan kutsuu repaint()-metodia ja
     * PlanetPanelin päivitysmetodia).
     * 
     * @param world maailma
     * @param nbodyPanel piirtopaneeli
     * @param planetPanel hallintapaneeli
     * @return kahva säikeeseen
     */
    public static ScheduledFuture<?> startRendering(final World world, final NbodyPanel nbodyPanel,
            final PlanetPanel planetPanel) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable gamePhysics = new Runnable() {
            public void run() {
                // ei päivitetä fysiikkaa aina, mutta piirretään aina
                if (world.isSimulationRunning()) {
                    world.resetAllForces();
                    world.gravitateAll();
                    world.updateAll(PHYSICS_TIMESTEP);
                }
                nbodyPanel.repaint();
                planetPanel.updateComponentValues();
            }
        };

        return scheduler.scheduleAtFixedRate(gamePhysics, 0, (long) (1000 * PHYSICS_TIMESTEP), TimeUnit.MILLISECONDS);
    }

}
