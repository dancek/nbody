package dancek.io;

import java.io.File;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileFilter;

/**
 * Oma FileFilter, joka seuloo tiedostoja regexillä.
 * 
 * @author Hannu Hartikainen
 */
public class RegexFileFilter extends FileFilter {

    private Pattern pattern;
    private String description;

    /**
     * Luo uuden filtterin regexistä ja antaa sille kuvauksen (tämä oli selkein
     * tapa mahdollistaa lokalisaatio, minkä keksin).
     * 
     * @param description filtterin kuvaus
     */
    public RegexFileFilter(String regex, String description) {
        this.pattern = Pattern.compile(regex);
        this.description = description;
    }

    public boolean accept(File f) {
        return f.isDirectory() || this.pattern.matcher(f.getName()).matches();
    }

    public String getDescription() {
        return this.description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }
}
