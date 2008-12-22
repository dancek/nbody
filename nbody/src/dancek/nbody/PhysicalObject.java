package dancek.nbody;

import dancek.vecmath.SimpleVector;

/**
 * PhysicalObject is an abstract class for any physical object (i.e. has mass).
 * 
 * @author Hannu Hartikainen
 */
public abstract class PhysicalObject {

    // kentät ovat volatileja varmuuden vuoksi, koska käytetään useampaa säiettä
    private volatile double mass;
    private volatile SimpleVector position;
    private volatile SimpleVector lastPosition;
    private volatile SimpleVector velocity;
    private volatile SimpleVector acceleration;
    private volatile SimpleVector forceSum;

    /**
     * Luo uuden kappaleen.
     * 
     * @param xPosition sijainnin x-komponentti
     * @param yPosition sijainnin y-komponentti
     * @param xVelocity nopeuden x-komponentti
     * @param yVelocity nopeuden y-komponentti
     * @param mass massa
     */
    public PhysicalObject(double xPosition, double yPosition, double xVelocity, double yVelocity, double mass) {
        this.position = new SimpleVector(xPosition, yPosition);
        this.acceleration = new SimpleVector();
        this.forceSum = new SimpleVector();
        this.mass = mass;

        this.setVelocity(xVelocity, yVelocity);
    }

    /**
     * Lisää kappaleeseen vaikuttavan voiman.
     * 
     * @param force voimavektori
     */
    public void addForce(SimpleVector force) {
        this.forceSum.add(force);
    }

    /**
     * Antaa liikesuunnan (nopeuden suuntakulma).
     * 
     * @return liikesuunta
     */
    public double getDirection() {
        double angle = -Math.atan2(this.velocity.y, this.velocity.x);
        return angle >= 0 ? angle : angle + 2 * Math.PI;
    }

    /**
     * Antaa vauhdin (nopeuden itseisarvo).
     * 
     * @return vauhti
     */
    public double getLinearVelocity() {
        return this.velocity.length();
    }

    /**
     * Antaa kappaleen massan.
     * 
     * @return massa
     */
    public double getMass() {
        return this.mass;
    }

    /**
     * Antaa kappaleen sijainnin.
     * 
     * @return sijainti
     */
    public SimpleVector getPosition() {
        return this.position;
    }

    /**
     * Antaa kappaleen nopeuden.
     * 
     * @return nopeus
     */
    public SimpleVector getVelocity() {
        return this.velocity;
    }

    /**
     * Nollaa kappaleeseen vaikuttavat voimat.
     */
    public synchronized void resetForce() {
        this.forceSum.set(0, 0);
    }

    /**
     * Päivittää kappaleen sijainnin ja nopeuden. Käyttää Verlet-integrointia,
     * joka on Euleria selvästi tarkempi (Euler on se intuitiivisin toteutus).
     * 
     * Kaavat ovat siis seuraavat:
     * 
     * <pre>
     *  Euler: x(t+dt) = x(t) + v(t)*dt
     * Verlet: x(t+dt) = 2*x(t) - x(t-dt) + a(t)*dt*dt
     * </pre>
     * 
     * @param dt aika, jolla simulaatiota viedään eteenpäin
     */
    public synchronized void update(double dt) {
        this.acceleration.set(this.forceSum);
        this.acceleration.scale(1.0d / mass);

        SimpleVector newLastPosition = this.position.clone();

        // on ehdottoman tärkeää laskea nopeus ennen kuin päivittää paikkaa
        // (hakkasin päätäni seinään tämän kanssa noin tunnin...)
        this.velocity = this.position.clone();
        this.velocity.sub(this.lastPosition);
        this.velocity.scale(1.0d / dt);

        // tässä siis Verlet-integrointi
        this.position.x = 2 * this.position.x - this.lastPosition.x + this.acceleration.x * dt * dt;
        this.position.y = 2 * this.position.y - this.lastPosition.y + this.acceleration.y * dt * dt;

        this.lastPosition = newLastPosition;
    }

    /**
     * Asettaa kappaleen massan.
     * 
     * @param mass massa
     */
    protected void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * Asettaa kappaleen nopeuden.
     * 
     * @param x nopeuden x-komponentti
     * @param y nopeuden y-komponentti
     */
    protected synchronized void setVelocity(double x, double y) {
        if (this.velocity != null)
            this.velocity.set(x, y);
        else
            this.velocity = new SimpleVector(x, y);

        /*
         * Verlet (käytössä oleva numeerinen integrointimenetelmä) tarvitsee
         * edellisen sijainnin, mikä on ensimmäisen askeleen aikana tai nopeuden
         * muutoksen jälkeen ongelma. Tämä ei ole ihan oikea tapa ratkaista
         * ongelmaa, mutta virheet jäävät kuitenkin aika pieneksi.
         */
        this.lastPosition = this.velocity.clone();
        this.lastPosition.scale(-RenderingThread.PHYSICS_TIMESTEP);
        this.lastPosition.add(this.position);
    }

    /**
     * Asettaa nopeuden polaarisissa koordinaateissa. Käytetään -thetaa, jotta
     * saadaan kasvu vastapäivään (standardi).
     * 
     * @param r vauhti
     * @param theta suunta
     */
    protected void setVelocityPolar(double r, double theta) {
        this.setVelocity(r * Math.cos(-theta), r * Math.sin(-theta));
    }
}
