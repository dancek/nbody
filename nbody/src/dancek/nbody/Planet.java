/**
 * 
 */
package dancek.nbody;

import java.awt.Color;

/**
 * @author Hannu Hartikainen
 * 
 */
public class Planet extends PhysicalObject {

    protected static final double DEFAULT_MASS = 1e10;

    private static int nextPlanetNumber = 0;

    private int radius;
    private String name;

    private Color color;

    public Planet(double x, double y, double xVelocity, double yVelocity, double mass) {
        this(x, y, xVelocity, yVelocity, mass, "Planet" + ++nextPlanetNumber, new Color(Nbody.rand.nextInt(16777216)));
    }

    public Planet(double x, double y, double xVelocity, double yVelocity, double mass, String name, Color color) {
        super(x, y, xVelocity, yVelocity, mass);
        this.setMass(mass);
        this.setName(name);
        this.setColor(color);
    }

    public Planet(double x, double y) {
        this(x, y, 0, 0, DEFAULT_MASS);
    }

    public Planet(double x, double y, double mass) {
        this(x, y, 0, 0, mass);
    }

    /**
     * Asettaa planeetan värin.
     * 
     * @param color väri
     */
    protected void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public int getRadius() {
        return this.radius;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected void setMass(double mass) {
        super.setMass(mass);
        this.radius = (int) Math.log10(mass);
        if (this.radius < 1)
            this.radius = 1;
    }

    public String toString() {
        return this.getName();
    }
}
