package dancek.vecmath;

/**
 * Vektoriluokka. Käytin aluksi luokkaa javax.vecmath.Vector2d, mutta huomasin
 * sen olevan osa j3d-apia jota ilmeisesti ei ole läheskään kaikilla. Koska teen
 * vain hyvin yksinkertaisia laskutoimituksia, päätin että on helpointa
 * kirjoittaa itse oma luokka, jossa on tarvittavat metodit. Aikaa meni puolisen
 * tuntia.
 * 
 * @author Hannu Hartikainen
 */
public class SimpleVector implements Cloneable {

    /**
     * Vektorin x-komponentti.
     */
    public volatile double x;
    
    /**
     * Vektorin y-komponentti.
     */
    public volatile double y;

    /**
     * Luo vektorin.
     * 
     * @param x x-komponentti
     * @param y y-komponentti
     */
    public SimpleVector(double x, double y) {
        this.set(x, y);
    }

    /**
     * Luo nollavektorin.
     */
    public SimpleVector() {
        this.set(0, 0);
    }

    /**
     * Antaa vektorin pituuden.
     * 
     * @return pituus
     */
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

    /**
     * Asettaa vektorin arvon.
     * 
     * @param x x-komponentti
     * @param y y-komponentti
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Asettaa vektorin arvon.
     * 
     * @param vec vektori, johon arvo asetetaan
     */
    public void set(SimpleVector vec) {
        this.set(vec.x, vec.y);
    }

    /**
     * Vähentää vektorista toisen vektorin.
     * 
     * @param vec toinen vektori
     */
    public void sub(SimpleVector vec) {
        this.x -= vec.x;
        this.y -= vec.y;
    }

    /*
     * (non-Javadoc) Palauttaa kloonin vektorista.
     * 
     * @see java.lang.Object#clone()
     */
    public SimpleVector clone() {
        return new SimpleVector(this.x, this.y);
    }

    /**
     * Muuttaa vektorin vastavektorikseen.
     */
    public void negate() {
        this.x = -this.x;
        this.y = -this.y;
    }

    /**
     * Normalisoi vektorin (suunta säilyy, pituudeksi tulee 1).
     */
    public void normalize() {
        this.scale(1.0d / this.length());
    }
}
