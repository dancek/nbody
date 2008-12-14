/**
 * 
 */
package dancek.nbody;

/**
 * @author Hannu Hartikainen
 * 
 */
public class Planet extends PhysicalObject {

    private static int nextPlanetNumber = 0;
    
    private int size;
    private String name;

    public Planet(double position, double position2, double velocity, double velocity2, double mass) {
        super(position, position2, velocity, velocity2, mass);
        this.setMass(mass);
        this.setName("Planet" + ++nextPlanetNumber);
    }

    public Planet(double position, double position2, double velocity, double velocity2, double mass, String name) {
        this(position, position2, velocity, velocity2, mass);
        this.setName(name);
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
        if (this.size < 3) this.size = 3;
    }
}
