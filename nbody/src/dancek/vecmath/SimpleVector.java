/**
 * 
 */
package dancek.vecmath;

/**
 * Vektoriluokka. Käytin aluksi luokkaa javax.vecmath.Vector2d, mutta huomasin
 * sen olevan osa j3d-apia jota ilmeisesti ei ole läheskään kaikilla. Koska teen
 * vain hyvin yksinkertaisia laskutoimituksia, päätin että on helpointa
 * kirjoittaa itse oma luokka, jossa on tarvittavat metodit. Aikaa meni puolisen
 * tuntia.
 * 
 * @author Hannu Hartikainen
 * 
 */
public class SimpleVector implements Cloneable {

    public volatile double x;
    public volatile double y;

    public SimpleVector(double x, double y) {
        this.set(x, y);
    }

    public SimpleVector() {
        this.set(0, 0);
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Palauttaa vektorin pituuden neliön.
     * 
     * @return vektorin pituuden neliö
     */
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    /**
     * Skaalaa vektorin.
     * 
     * @param d kerroin, jolla vektorin pituus skaalataan
     */
    public void scale(double d) {
        this.x *= d;
        this.y *= d;
    }

    /**
     * Summaa kaksi vektoria.
     * 
     * @param vec toinen vektori
     */
    public void add(SimpleVector vec) {
        this.x += vec.x;
        this.y += vec.y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(SimpleVector vec) {
        this.set(vec.x, vec.y);
    }

    public void sub(SimpleVector vec) {
        this.x -= vec.x;
        this.y -= vec.y;
    }

    public SimpleVector clone() {
        return new SimpleVector(this.x, this.y);
    }

    public void negate() {
        this.x = -this.x;
        this.y = -this.y;
    }

    public void normalize() {
        this.scale(1.0d / this.length());
    }

    public void rotate(double d) {
        
    }
}
