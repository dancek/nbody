package dancek.nbody;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * About-ikkuna. Sisältää JTextPanen, joka näyttää HTML-tiedostoa.
 * 
 * @author Hannu Hartikainen
 */
public class AboutDialog extends JDialog {

    private static final Dimension PREFERRED_SIZE = new Dimension(400, 300);
    private static final String ABOUT_HTML_FILE = "/about.html";
    
    private JLabel aboutLabel;
    private JScrollPane scrollPane;
    private JTextPane textPane;
    private JButton okButton;

    /**
     * AboutDialogin konstruktori, joka tekee dialogin alusta loppuun valmiiksi.
     * 
     * @param parent JFrame, jolle dialogi kuuluu
     * @param modal onko paneeli modaalinen
     */
    public AboutDialog(JFrame parent, boolean modal) {
        super(parent, modal);

        GridBagConstraints gridBagConstraints;

        this.aboutLabel = new JLabel();
        this.scrollPane = new JScrollPane();
        this.textPane = new JTextPane();
        this.okButton = new JButton("OK");

        this.setResizable(false);
        this.setTitle("About nbody");
        this.setLayout(new GridBagLayout());

        this.aboutLabel.setText("nbody " + Nbody.VERSION + " by Hannu Hartikainen");

        this.okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        
        // laitetaan tekstinäkymä scrollpaneen, jotta sitä voi scrollata
        this.scrollPane.setViewportView(this.textPane);
        this.scrollPane.setPreferredSize(PREFERRED_SIZE);

        this.textPane.setEditable(false);
        
        try {
            // ladataan sivu
            URL url = this.getClass().getResource(ABOUT_HTML_FILE);
            this.textPane.setPage(url);
        } catch (IOException e) {
            // huono juttu jos sivua ei saatu ladattua, mutta minkäs teet; ei
            // kannata ohjelmaa silti kaataa...
        }

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.ipady = 30;
        this.add(this.aboutLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 2;
        this.add(this.scrollPane, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 3;
        this.add(this.okButton, gridBagConstraints);

        this.pack();
        
        this.setLocationRelativeTo(null);
    }
}
