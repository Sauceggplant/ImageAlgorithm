package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.enums.ColorEnum;
import org.sauceggplant.ia.ui.IaPanel;
import org.sauceggplant.ia.util.PropertiesUtil;
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

    private static final String OPEN = "ia.ui.io.file.open";
    private static final String TITLE = "ia.ui.color.ratio.title";
    private static final String BTN_OK = "ia.ui.btn.ok";
    private static final String RED = "ia.ui.color.ratio.red";
    private static final String GREEN = "ia.ui.color.ratio.green";
    private static final String BLUE = "ia.ui.color.ratio.blue";

    @Override
    public void run(IaPanel iaPanel) {
        logger.info("ColorRatio：颜色占比");
        BufferedImage image = iaPanel.getContent().getImage();
        if (null == image) {
            logger.error(PropertiesUtil.getProperty(OPEN));
            return;
        }

        this.iaPanel = iaPanel;
        //当前图像颜色占比
        int initRed = colorRatio(ColorEnum.RED);
        int initGreen = colorRatio(ColorEnum.GREEN);
        int initBlue = colorRatio(ColorEnum.BLUE);
        logger.info("{}：Red:{} Green:{} Blue:{}", PropertiesUtil.getProperty(TITLE), initRed, initGreen, initBlue);
        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle(PropertiesUtil.getProperty(TITLE));
        dialog.setPreferredSize(new Dimension(600, 320));
        dialog.setSize(new Dimension(600, 320));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(sliderPanel(initRed, initGreen, initBlue), BorderLayout.CENTER);
        JButton ok = new JButton(PropertiesUtil.getProperty(BTN_OK));
        ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int red = redSlider.getValue();
                int green = greenSlider.getValue();
                int blue = blueSlider.getValue();
                logger.info("Slider: Red:{} Green:{} Blue:{}", red, green, blue);
                colorRatioChange(red - initRed, green - initGreen, blue - initBlue);
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
        redSlider = new JSlider(JSlider.HORIZONTAL, red - 255, red + 255, red);
        redSlider.setToolTipText(PropertiesUtil.getProperty(RED));
        redSlider.setMajorTickSpacing(30);
        redSlider.setMinorTickSpacing(1);
        redSlider.setPaintLabels(true);
        redSlider.setPaintTicks(true);
        redSlider.setPreferredSize(new Dimension(480, 80));

        greenSlider = new JSlider(JSlider.HORIZONTAL, green - 255, green + 255, green);
        greenSlider.setToolTipText(PropertiesUtil.getProperty(GREEN));
        greenSlider.setMajorTickSpacing(30);
        greenSlider.setMinorTickSpacing(1);
        greenSlider.setPaintLabels(true);
        greenSlider.setPaintTicks(true);
        greenSlider.setPreferredSize(new Dimension(480, 80));

        blueSlider = new JSlider(JSlider.HORIZONTAL, blue - 255, blue + 255, blue);
        blueSlider.setToolTipText(PropertiesUtil.getProperty(BLUE));
        blueSlider.setMajorTickSpacing(30);
        blueSlider.setMinorTickSpacing(1);
        blueSlider.setPaintLabels(true);
        blueSlider.setPaintTicks(true);
        blueSlider.setPreferredSize(new Dimension(480, 80));

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));
        JPanel redPanel = new JPanel();
        redPanel.setPreferredSize(new Dimension(600, 80));
        redPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        redPanel.add(new JLabel(PropertiesUtil.getProperty(RED)));
        redPanel.add(redSlider);
        JPanel greenPanel = new JPanel();
        greenPanel.setPreferredSize(new Dimension(600, 80));
        greenPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        greenPanel.add(new JLabel(PropertiesUtil.getProperty(GREEN)));
        greenPanel.add(greenSlider);
        JPanel bluePanel = new JPanel();
        bluePanel.setPreferredSize(new Dimension(600, 80));
        bluePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        bluePanel.add(new JLabel(PropertiesUtil.getProperty(BLUE)));
        bluePanel.add(blueSlider);
        panel.add(redPanel);
        panel.add(greenPanel);
        panel.add(bluePanel);
        return panel;
    }

    /**
     * 颜色调整
     *
     * @param red   红
     * @param green 绿
     * @param blue  蓝
     */
    public void colorRatioChange(int red, int green, int blue) {
        logger.info("Change: Red:{} Green:{} Blue:{}", red, green, blue);
        BufferedImage image = iaPanel.getContent().getImage();
        int width = image.getData().getWidth();
        int height = image.getData().getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                Color c = new Color(rgb);
                int changeRed = c.getRed() + red;
                int changeGreen = c.getGreen() + green;
                int changeBlue = c.getBlue() + blue;
                //颜色超出阈值矫正(颜色范围0--255)
                changeRed = changeRed > 255 ? 255 : (changeRed < 0 ? 0 : changeRed);
                changeGreen = changeGreen > 255 ? 255 : (changeGreen < 0 ? 0 : changeGreen);
                changeBlue = changeBlue > 255 ? 255 : (changeBlue < 0 ? 0 : changeBlue);
                result.setRGB(i, j, new Color(changeRed, changeGreen, changeBlue, c.getAlpha()).getRGB());
            }
        }
        iaPanel.getOutput().setImage(result);
    }

    /**
     * 颜色分量
     *
     * @param colorEnum 颜色（RGB）
     * @return 平均颜色
     */
    public int colorRatio(ColorEnum colorEnum) {
        BufferedImage image = iaPanel.getContent().getImage();
        int width = image.getData().getWidth();
        int height = image.getData().getHeight();
        long sum = 0;
        if (ColorEnum.RED.equals(colorEnum)) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = image.getRGB(i, j);
                    Color c = new Color(rgb);
                    sum += c.getRed();
                }
            }
        } else if (ColorEnum.GREEN.equals(colorEnum)) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = image.getRGB(i, j);
                    Color c = new Color(rgb);
                    sum += c.getGreen();
                }
            }
        } else if (ColorEnum.BLUE.equals(colorEnum)) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = image.getRGB(i, j);
                    Color c = new Color(rgb);
                    sum += c.getBlue();
                }
            }
        }
        return (int) sum / width / height;
    }
}
