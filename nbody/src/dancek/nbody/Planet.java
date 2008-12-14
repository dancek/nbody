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

    private static int nextPlanetNumber = 0;

    private int size;
    private String name;

    private Color color;

    public Planet(double position, double position2, double velocity, double velocity2, double mass) {
        this(position, position2, velocity, velocity2, mass, "Planet" + ++nextPlanetNumber, new Color(Nbody.rand.nextInt(16777216)));
    }

    public Planet(double position, double position2, double velocity, double velocity2, double mass, String name,
            Color color) {
        super(position, position2, velocity, velocity2, mass);
        this.setMass(mass);
        this.setName(name);
        this.setColor(color);
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

    public int getSize() {
        return this.size;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected void setMass(double mass) {
        super.setMass(mass);
        this.size = (int) Math.log(mass);
        if (this.size < 3)
            this.size = 3;
    }
}
