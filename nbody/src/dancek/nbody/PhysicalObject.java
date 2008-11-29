package dancek.nbody;

import javax.vecmath.Vector2d;

/**
 * PhysicalObject is an abstract class for any physical object (i.e. has mass).
 * 
 * @author Hannu Hartikainen
 */
public abstract class PhysicalObject {
    private double mass;
    private Vector2d position;
    private Vector2d velocity;
    private Vector2d acceleration;
    private Vector2d forceSum;
    
    public PhysicalObject(double xPosition, double yPosition, double xVelocity, double yVelocity, double mass) {
        this.position = new Vector2d(xPosition, yPosition);
        this.velocity = new Vector2d(xVelocity, yVelocity);
        this.acceleration = new Vector2d();
        this.forceSum = new Vector2d();
        this.mass = mass;
    }

    public PhysicalObject(Vector2d pos, Vector2d vel, double mass) {
        this.position = pos;
        this.velocity = vel;
        this.acceleration = new Vector2d();
        this.forceSum = new Vector2d();
        this.mass = mass;
    }
    
    public void resetForce() {
        this.forceSum.set(0,0);
    }
    
    public void addForce(Vector2d force) {
        this.forceSum.add(force);
    }
    
    public Vector2d getPosition() {
        return (Vector2d) this.position.clone();
    }
    
    public double getMass() {
        return this.mass;
    }
    
    public void update(double dt) {
        this.acceleration.set(this.forceSum);
        this.acceleration.scale( 1.0d / mass );
        this.velocity.x += this.acceleration.x * dt;
        this.velocity.y += this.acceleration.y * dt;
        this.position.x += this.velocity.x * dt;
        this.position.y += this.velocity.y * dt;
    }
}
