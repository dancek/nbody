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
    private Vector2d lastPosition;
    private Vector2d velocity;
    private Vector2d acceleration;
    private Vector2d forceSum;

    public PhysicalObject(double xPosition, double yPosition, double xVelocity, double yVelocity, double mass) {
        this.position = new Vector2d(xPosition, yPosition);
        this.velocity = new Vector2d(xVelocity, yVelocity);
        this.acceleration = new Vector2d();
        this.forceSum = new Vector2d();
        this.mass = mass;

        // Verlet tarvitsee edellisen sijainnin, mikä on ensimmäisen askeleen
        // aikana ongelma. Tämä ei ole ihan oikea tapa ratkaista ongelmaa, mutta
        // virheet jää kuitenkin aika pieneksi.
        this.lastPosition = (Vector2d) this.velocity.clone();
        this.lastPosition.scale(-Physics.PHYSICS_TIMESTEP);
        this.lastPosition.add(this.position);
    }

    public PhysicalObject(Vector2d pos, Vector2d vel, double mass) {
        this.position = pos;
        this.velocity = vel;
        this.acceleration = new Vector2d();
        this.forceSum = new Vector2d();
        this.mass = mass;
    }

    public void resetForce() {
        this.forceSum.set(0, 0);
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

    protected void setMass(double mass) {
        this.mass = mass;
    }

    public double getLinearVelocity() {
        return this.velocity.length();
    }
    
    public double getDirection() {
        return Math.atan2(this.velocity.y, this.velocity.x) + Math.PI;
    }

    /**
     * Päivittää kappaleen sijainnin ja nopeuden. Käyttää Verlet-integrointia,
     * joka on Euleria selvästi tarkempi (Euler on se intuitiivisin toteutus).
     * Lisätiedot Wikipediasta, josta itsekin selvitin menetelmän
     * yksityiskohdat.
     * 
     * @param dt aika, jolla simulaatiota viedään eteenpäin
     */
    public void update(double dt) {
        this.acceleration.set(this.forceSum);
        this.acceleration.scale(1.0d / mass);
        
        Vector2d newLastPosition = (Vector2d) this.position.clone();

        this.position.x = 2 * this.position.x - this.lastPosition.x + this.acceleration.x * dt * dt;
        this.position.y = 2 * this.position.y - this.lastPosition.y + this.acceleration.y * dt * dt;

        this.velocity = (Vector2d) this.position.clone();
        this.velocity.sub(this.lastPosition);
        this.velocity.scale(1.0 / dt);

        this.lastPosition = newLastPosition;
    }
}
