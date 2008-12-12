/**
 * 
 */
package dancek.nbody;

/**
 * @author Hannu Hartikainen
 *
 */
public class Planet extends PhysicalObject {

    private int size;

    /**
     * @param position
     * @param position2
     * @param velocity
     * @param velocity2
     * @param mass
     */
    public Planet(double position, double position2, double velocity, double velocity2, double mass) {
        super(position, position2, velocity, velocity2, mass);
        this.size = (int) Math.log(mass);
   }
    
    public int getSize() {
        return this.size;
    }
    
    protected void setMass(double mass) {
        super.setMass(mass);
        this.size = (int) Math.log(mass);
    }
}
