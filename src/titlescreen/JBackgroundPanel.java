package titlescreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
/**
 * @author DELVIGNE Brian, DIOT Sébastien, GNALY-NGUYEN Kouadjo, LEHMAN Ylon
 * @version 16/05/2021
 */
public class JBackgroundPanel extends JPanel {
    /**
     * Une Image pour le fond de la fenetre de lancememnt
     */
    private Image img;
    /**
     * Constructeur par initialisation
     * @param path String
     * @param layout LayoutManager
     */
    public JBackgroundPanel(String path, LayoutManager layout) {
        try {
            img = ImageIO.read(this.getClass().getResourceAsStream(path));
        } catch(IOException err) {
            err.printStackTrace();
        }
        this.setLayout(layout);
        this.setSize(img.getWidth(this), img.getHeight(this));
        this.setPreferredSize(new Dimension(img.getWidth(this), img.getHeight(this)));
    }
    /**
     * Constructeur par initialisation
     * @param path String
     */
    public JBackgroundPanel(String path) {
        try {
            img = ImageIO.read(this.getClass().getResourceAsStream(path));
        } catch(IOException err) {
            err.printStackTrace();
        }
        this.setSize(img.getWidth(this), img.getHeight(this));
        this.setPreferredSize(new Dimension(img.getWidth(this), img.getHeight(this)));
    }
    /**
     * Setter du ration
     * @param x double
     */
    public void setRatio(double x) {
        try {
            if (x <= 0 ) {
                throw new IllegalArgumentException("Ratio must be bigger than 0.");
            }            
            this.setSize((int)(img.getWidth(this)*x), (int)(img.getHeight(this)*x));
            this.setPreferredSize(new Dimension((int)(img.getWidth(this)*x), (int)(img.getHeight(this)*x)));
        } catch (IllegalArgumentException err) {
            err.printStackTrace();
        }
    }
    /**
     * Methode qui affiche l'image
     * @param g Graphics
     */
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }
}