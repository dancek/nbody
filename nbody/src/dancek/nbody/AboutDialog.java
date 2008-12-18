package dancek.nbody;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * About-ikkuna.
 * 
 * @author Hannu Hartikainen
 */
public class AboutDialog extends JDialog {

    private static final String ABOUT_HTML_FILE = "/about.html";
    private JLabel aboutLabel;
    private JScrollPane scrollPane;
    private JTextPane textPane;

    public AboutDialog(JFrame parent, boolean modal) {
        super(parent, modal);

        GridBagConstraints gridBagConstraints;

        this.aboutLabel = new javax.swing.JLabel();
        this.scrollPane = new javax.swing.JScrollPane();
        this.textPane = new javax.swing.JTextPane();

        this.setLayout(new GridBagLayout());

        this.aboutLabel.setText("nbody 1.0 by Hannu Hartikainen");

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.ipady = 30;
        this.add(this.aboutLabel, gridBagConstraints);

        scrollPane.setPreferredSize(new Dimension(400, 300));
        scrollPane.setViewportView(this.textPane);

        this.textPane.setEditable(false);
        
        try {
            URL uri = this.getClass().getResource(ABOUT_HTML_FILE);
            this.textPane.setPage(uri);
        } catch (IOException e) {
            // huono juttu jos sivua ei saatu ladattua, mutta minkäs teet; ei
            // kannata ohjelmaa silti kaataa...
            e.printStackTrace();
        }

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 2;
        this.add(this.scrollPane, gridBagConstraints);

        this.pack();
    }
}
