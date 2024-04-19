package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * 颜色分量
 */
public class ColorRatio implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ColorRatio.class);

    /**
     * 界面
     */
    private IaPanel iaPanel;

    /**
     * 红色滑块
     */
    private JSlider redSlider;

    /**
     * 绿色滑块
     */
    private JSlider greenSlider;

    /**
     * 蓝色滑块
     */
    private JSlider blueSlider;

    @Override
    public void run(IaPanel iaPanel) {
        this.iaPanel = iaPanel;
        logger.info("菜单：颜色占比");
        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle("颜色占比调整");
        dialog.setPreferredSize(new Dimension(600, 320));
        dialog.setSize(new Dimension(600, 320));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(sliderPanel(128, 128, 128), BorderLayout.CENTER);
        JButton ok = new JButton("确定");
        ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int red = redSlider.getValue();
                int green = redSlider.getValue();
                int blue = redSlider.getValue();
                colorRatioChange(red, green, blue);
                dialog.setVisible(false);
            }
        });
        dialog.getContentPane().add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * 滑块panel
     *
     * @param red   图像的红色
     * @param green 图像的绿色
     * @param blue  图像的蓝色
     * @return 界面
     */
    JPanel sliderPanel(int red, int green, int blue) {
        redSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, red);
        redSlider.setToolTipText("红色");
        redSlider.setMajorTickSpacing(15);
        redSlider.setMinorTickSpacing(1);
        redSlider.setPaintLabels(true);
        redSlider.setPaintTicks(true);
        redSlider.setPreferredSize(new Dimension(490, 80));

        greenSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, green);
        greenSlider.setToolTipText("绿色");
        greenSlider.setMajorTickSpacing(15);
        greenSlider.setMinorTickSpacing(1);
        greenSlider.setPaintLabels(true);
        greenSlider.setPaintTicks(true);
        greenSlider.setPreferredSize(new Dimension(490, 80));

        blueSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, blue);
        blueSlider.setToolTipText("蓝色");
        blueSlider.setMajorTickSpacing(15);
        blueSlider.setMinorTickSpacing(1);
        blueSlider.setPaintLabels(true);
        blueSlider.setPaintTicks(true);
        blueSlider.setPreferredSize(new Dimension(490, 80));

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));
        JPanel redPanel = new JPanel();
        redPanel.setPreferredSize(new Dimension(600, 80));
        redPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        redPanel.add(new JLabel("红色(0-255)："));
        redPanel.add(redSlider);
        JPanel greenPanel = new JPanel();
        greenPanel.setPreferredSize(new Dimension(600, 80));
        greenPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        greenPanel.add(new JLabel("绿色(0-255)："));
        greenPanel.add(greenSlider);
        JPanel bluePanel = new JPanel();
        bluePanel.setPreferredSize(new Dimension(600, 80));
        bluePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        bluePanel.add(new JLabel("蓝色(0-255)："));
        bluePanel.add(blueSlider);
        panel.add(redPanel);
        panel.add(greenPanel);
        panel.add(bluePanel);
        return panel;
    }

    public void colorRatioChange(int red, int green, int blue) {
        logger.info("调整的颜色占比：红:{} 绿:{} 蓝:{}", red, green, blue);
        BufferedImage bufferedImage = iaPanel.getContent().getImage();
        //TODO
        iaPanel.getOutput().setImage(bufferedImage);
    }
}
