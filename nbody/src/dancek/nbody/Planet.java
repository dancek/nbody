package dancek.nbody;

import java.awt.Color;

/**
 * Planeettaluokka. Planeetta on kappale, mutta sillä on joitain
 * erikoisominaisuuksia.
 * 
 * @author Hannu Hartikainen
 */
public class Planet extends PhysicalObject {

    // planeetan oletusmassa (tätä käytetään aluksi, oletusmassaa voi vaihtaa
    // käyttöliittymästä)
    protected static final double DEFAULT_MASS = 1e10;

    // käytetään järjestyslukua että saadaan jokaiselle planeetalle uniikki nimi
    private static int nextPlanetNumber = 0;

    private int radius;
    private String name;

    private Color color;

    /**
     * Luo uuden planeetan. Asettaa nopeuden nollaksi ja kutsuu toista
     * konstruktoria, joka huolehtii nimestä ja väristä.
     * 
     * @param x sijainnin x-komponentti
     * @param y sijainnin y-komponentti
     * @param mass massa
     */
    public Planet(double x, double y, double mass) {
        this(x, y, 0, 0, mass);
    }

    /**
     * Luo uuden planeetan. Asettaa nimeksi "PlanetX", missä X on järjestysluku.
     * Laittaa planeetalle satunnaisen värin.
     * 
     * @param x sijainnin x-komponentti
     * @param y sijainnin y-komponentti
     * @param xVelocity nopeuden x-komponentti
     * @param yVelocity nopeuden y-komponentti
     * @param mass massa
     */
    public Planet(double x, double y, double xVelocity, double yVelocity, double mass) {
        this(x, y, xVelocity, yVelocity, mass, "Planet" + ++nextPlanetNumber, new Color(Nbody.rand.nextInt(16777216)));
    }

    /**
     * Luo uuden planeetan.
     * 
     * @param x sijainnin x-komponentti
     * @param y sijainnin y-komponentti
     * @param xVelocity nopeuden x-komponentti
     * @param yVelocity nopeuden y-komponentti
     * @param mass massa
     * @param name planeetan nimi
     * @param color planeetan väri
     */
    public Planet(double x, double y, double xVelocity, double yVelocity, double mass, String name, Color color) {
        super(x, y, xVelocity, yVelocity, mass);
        this.setMass(mass);
        this.setName(name);
        this.setColor(color);
    }

    /**
     * Antaa planeetan värin.
     * 
     * @return väri
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Antaa planeetan nimen.
     * 
     * @return nimi
     */
    public String getName() {
        return this.name;
    }

    /**
     * Antaa planeetan säteen.
     * 
     * @return säde
     */
    public int getRadius() {
        return this.radius;
    }

    /**
     * Asettaa planeetalle nimen.
     * 
     * @param name nimi
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc) Antaa merkkijonokuvauksen planeetasta. Käytännössä kertoo
     * planeetan nimen, mutta voisi olla jotain muutakin.
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return this.getName();
    }

    /**
     * Asettaa planeetan värin.
     * 
     * @param color väri
     */
    protected void setColor(Color color) {
        this.color = color;
    }

    /*
     * (non-Javadoc) Asettaa planeetan massan ja laskee planeetalle säteen
     * massasta. Oikeasti pätisi r^3 ~ m mutta se ei ole kovin soveltuva
     * käyttöliittymän kannalta. Keskuskappaleet olisivat kymmeniä tai satoja
     * kertoja suurempia kuin kiertolaiset. Niinpä käytetään r ~ log(m), joka on
     * käytettävyyden kannalta paljon kivempi ratkaisu.
     * 
     * @see dancek.nbody.PhysicalObject#setMass(double)
     */
    protected void setMass(double mass) {
        super.setMass(mass);
        this.radius = (int) Math.log10(mass);
        if (this.radius < 1)
            this.radius = 1;
    }
}
