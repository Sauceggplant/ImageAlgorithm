package org.sauceggplant.ia.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图像panel
 */
public class IaImagePanel extends JPanel {

    /**
     * 图像
     */
    private BufferedImage image;

    /**
     * 图像panel
     *
     * @param image 图像
     */
    public IaImagePanel(BufferedImage image) {
        this.image = image;
        if (null != image) {
            this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        if (null != image) {
            this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        }
        this.updateUI();
    }
}
