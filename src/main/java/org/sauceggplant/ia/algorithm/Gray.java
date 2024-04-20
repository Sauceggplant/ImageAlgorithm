package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * 灰度
 */
public class Gray implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Gray.class);

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

    /**
     * 灰度经验值 int color = red*0.299+green*0.587+blue*0.114;
     * 灰度经验值，红
     */
    private static final int defaultRed = 299;

    /**
     * 灰度经验值，绿
     */
    private static final int defaultGreen = 587;

    /**
     * 灰度经验值，蓝
     */
    private static final int defaultBlue = 114;

    /**
     * 灰度
     *
     * @param iaPanel 面板
     */
    @Override
    public void run(IaPanel iaPanel) {
        logger.info("菜单：灰度");
        BufferedImage bufferedImage = iaPanel.getContent().getImage();
        if (null == bufferedImage) {
            logger.error("请先打开一张图片");
            return;
        }
        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle("灰度图像,颜色占比调整");
        dialog.setPreferredSize(new Dimension(600, 320));
        dialog.setSize(new Dimension(600, 320));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(sliderPanel(defaultRed, defaultGreen, defaultBlue), BorderLayout.CENTER);
        //比例灰度
        JButton ratio = new JButton("比例灰度");
        ratio.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int red = redSlider.getValue();
                int green = greenSlider.getValue();
                int blue = blueSlider.getValue();
                logger.info("灰度调整的颜色占比：红:{} 绿:{} 蓝:{}", red / 1000.0d, green / 1000.0d, blue / 1000.0d);
                //均值灰度
                iaPanel.getOutput().setImage(ratioGray(bufferedImage, red / 1000.0d, green / 1000.0d, blue / 1000.0d));
                dialog.setVisible(false);
            }
        });
        //均值灰度
        JButton avg = new JButton("均值灰度");
        avg.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //均值灰度
                iaPanel.getOutput().setImage(avgGray(bufferedImage));
                dialog.setVisible(false);
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(ratio);
        buttonPanel.add(avg);
        dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
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
        redSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, red);
        redSlider.setToolTipText("红色");
        redSlider.setMajorTickSpacing(100);
        redSlider.setMinorTickSpacing(1);
        redSlider.setPaintLabels(true);
        redSlider.setPaintTicks(true);
        redSlider.setPreferredSize(new Dimension(480, 80));

        greenSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, green);
        greenSlider.setToolTipText("绿色");
        greenSlider.setMajorTickSpacing(100);
        greenSlider.setMinorTickSpacing(1);
        greenSlider.setPaintLabels(true);
        greenSlider.setPaintTicks(true);
        greenSlider.setPreferredSize(new Dimension(480, 80));

        blueSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, blue);
        blueSlider.setToolTipText("蓝色");
        blueSlider.setMajorTickSpacing(100);
        blueSlider.setMinorTickSpacing(1);
        blueSlider.setPaintLabels(true);
        blueSlider.setPaintTicks(true);
        blueSlider.setPreferredSize(new Dimension(480, 80));

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));
        JPanel redPanel = new JPanel();
        redPanel.setPreferredSize(new Dimension(600, 80));
        redPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        redPanel.add(new JLabel("红色："));
        redPanel.add(redSlider);
        JPanel greenPanel = new JPanel();
        greenPanel.setPreferredSize(new Dimension(600, 80));
        greenPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        greenPanel.add(new JLabel("绿色："));
        greenPanel.add(greenSlider);
        JPanel bluePanel = new JPanel();
        bluePanel.setPreferredSize(new Dimension(600, 80));
        bluePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        bluePanel.add(new JLabel("蓝色："));
        bluePanel.add(blueSlider);
        panel.add(redPanel);
        panel.add(greenPanel);
        panel.add(bluePanel);
        return panel;
    }

    /**
     * 图像灰度算法
     *
     * @param bufferedImage 图像
     * @return 灰度处理后图像
     */
    public static BufferedImage avgGray(BufferedImage bufferedImage) {
        logger.info("均值灰度计算");
        //图像宽度，高度
        int width = bufferedImage.getData().getWidth();
        int height = bufferedImage.getData().getHeight();
        //构建图像，返回结果
        BufferedImage result = new BufferedImage(width, height, bufferedImage.getType());
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //获取像素点的颜色(红，绿，蓝),RGB色彩空间
                int rgb = bufferedImage.getRGB(i, j);
                Color color = new Color(rgb);
                //对红，绿，蓝求和再求平均值，将平均值赋值回原来的红，绿，蓝颜色分量，使红==绿==蓝
                int avg = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                //灰度颜色值,其中alpha是像素点的透明度，这里红绿蓝都使用计算后的平均值，透明度不变
                int targetColor = new Color(avg, avg, avg, color.getAlpha()).getRGB();
                //设置像素点的RGB颜色
                result.setRGB(i, j, targetColor);
            }
        }
        return result;
    }

    /**
     * 比例灰度
     *
     * @param bufferedImage 图像
     * @param red           红色比例
     * @param green         绿色比例
     * @param blue          蓝色比例
     * @return 灰度图像
     */
    private BufferedImage ratioGray(BufferedImage bufferedImage, double red, double green, double blue) {
        logger.info("比例灰度计算");
        //图像宽度，高度
        int width = bufferedImage.getData().getWidth();
        int height = bufferedImage.getData().getHeight();
        //构建图像，返回结果
        BufferedImage result = new BufferedImage(width, height, bufferedImage.getType());
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //获取像素点的颜色(红，绿，蓝),RGB色彩空间
                int rgb = bufferedImage.getRGB(i, j);
                Color color = new Color(rgb);
                //对红，绿，蓝计算后，赋值回原来的红，绿，蓝颜色分量，使红==绿==蓝
                double ratio = color.getRed() * red + color.getGreen() * green + color.getBlue() * blue;
                //阈值限定(0-255)
                int value = ratio > 255 ? 255 : (ratio < 0 ? 0 : (int) ratio);
                //赋值
                int targetColor = new Color(value, value, value, color.getAlpha()).getRGB();
                //设置像素点的RGB颜色
                result.setRGB(i, j, targetColor);
            }
        }
        return result;
    }
}
