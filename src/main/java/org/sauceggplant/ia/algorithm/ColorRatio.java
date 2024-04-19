package org.sauceggplant.ia.algorithm;

import ch.qos.logback.core.util.StringUtil;
import org.sauceggplant.ia.ui.IaImagePanel;
import org.sauceggplant.ia.ui.IaPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 颜色分量
 */
public class ColorRatio implements Algorithm {

    @Override
    public void run(IaPanel iaPanel) {

        JSlider redSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 128);
        redSlider.setToolTipText("红色");
        redSlider.setMajorTickSpacing(16);
        redSlider.setMinorTickSpacing(1);
        redSlider.setPaintLabels(true);
        redSlider.setPaintTicks(true);

        JSlider greenSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 128);
        greenSlider.setToolTipText("绿色");
        greenSlider.setMajorTickSpacing(16);
        greenSlider.setMinorTickSpacing(1);
        greenSlider.setPaintLabels(true);
        greenSlider.setPaintTicks(true);

        JSlider blueSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 128);
        blueSlider.setToolTipText("蓝色");
        blueSlider.setMajorTickSpacing(16);
        blueSlider.setMinorTickSpacing(1);
        blueSlider.setPaintLabels(true);
        blueSlider.setPaintTicks(true);

        //TODO

        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle("颜色占比");
        dialog.setPreferredSize(new Dimension(500, 100));
        dialog.setSize(new Dimension(500, 100));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());
        JButton ok = new JButton("确定");
        ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });
        dialog.getContentPane().add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
