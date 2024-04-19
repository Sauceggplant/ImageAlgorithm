package org.sauceggplant.ia.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图像
 */
public class IaImagePane extends JScrollPane {

    /**
     * 图像
     */
    private BufferedImage image;

    /**
     * 图像
     *
     * @param image 图像
     */
    public IaImagePane(BufferedImage image) {
        this.image = image;
        IaImagePanel imagePanel = new IaImagePanel(image);
        this.add(imagePanel);
        if (null != image) {
            this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.updateUI();
    }
}
