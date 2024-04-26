package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
import org.sauceggplant.ia.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * 马赛克
 */
public class Mosaic implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Mosaic.class);

    private static final String OPEN = "ia.ui.io.file.open";

    @Override
    public void run(IaPanel iaPanel) {
        logger.info("Mosaic:马赛克");
        BufferedImage image = iaPanel.getContent().getImage();
        if (null == image) {
            logger.error(PropertiesUtil.getProperty(OPEN));
            return;
        }

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 2, 100, 2);
        slider.setToolTipText("马赛克阈值");
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);

        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle("马赛克阈值");
        dialog.setPreferredSize(new Dimension(500, 100));
        dialog.setSize(new Dimension(500, 100));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());
        JPanel blank = new JPanel();
        blank.setPreferredSize(new Dimension(20, 20));
        dialog.getContentPane().add(slider, BorderLayout.CENTER);
        dialog.getContentPane().add(blank, BorderLayout.EAST);
        JButton ok = new JButton("确定");
        ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int value = slider.getValue();
                logger.info("马赛克阈值:{}", value);
                BufferedImage bufferedImage = iaPanel.getContent().getImage();
                iaPanel.getOutput().setImage(mosaic(bufferedImage, value));
                dialog.setVisible(false);
            }
        });
        dialog.getContentPane().add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * 马赛克
     *
     * @param image 图像
     * @param value 阈值
     * @return 马赛克图像
     */
    public BufferedImage mosaic(BufferedImage image, int value) {
        //图像宽高
        int width = image.getData().getWidth();
        int height = image.getData().getHeight();
        //生成返回图像
        BufferedImage result = new BufferedImage(width, height, image.getType());
        //按照阈值对图像分割块
        int xBlock = width % value == 0 ? (width / value) : (width / value + 1);
        int yBlock = height % value == 0 ? (height / value) : (height / value + 1);
        //对每个分割块做处理
        for (int i = 0; i < xBlock; i++) {
            for (int j = 0; j < yBlock; j++) {
                mosaicBlock(image, result, width, height, value, i, j);
            }
        }
        return result;
    }

    /**
     * 对每个块进行马赛克计算
     *
     * @param image  原始图像
     * @param result 输出图像
     * @param width  原始图像宽度
     * @param height 原始图像高度
     * @param value  马赛克阈值(正方形，每个块的边长)
     * @param i      块的行索引
     * @param j      块的列索引
     */
    public void mosaicBlock(BufferedImage image, BufferedImage result, int width, int height, int value, int i, int j) {
        //对某个块的所有像素求均值的颜色
        Color avgColor = getAvgColor(image, width, height, value, i, j);
        //对某个块内的所有像素进行赋值，块内的颜色统一为计算的均值颜色
        for (int cursorX = 0; cursorX < value; cursorX++) {
            for (int cursorY = 0; cursorY < value; cursorY++) {
                int cursorWidth = i * value + cursorX;
                int cursorHeight = j * value + cursorY;
                if (cursorWidth >= width) {
                    cursorWidth = width - 1;
                }
                if (cursorHeight >= height) {
                    cursorHeight = height - 1;
                }
                result.setRGB(cursorWidth, cursorHeight, avgColor.getRGB());
            }
        }
    }

    /**
     * 对某个块的所有像素，求均值颜色
     *
     * @param image  原始图像
     * @param width  原始图像的宽度
     * @param height 原始图像的高度
     * @param value  马赛克的阈值（正方形块的边长）
     * @param i      块的行索引
     * @param j      块的列索引
     * @return 均值颜色
     */
    public Color getAvgColor(BufferedImage image, int width, int height, int value, int i, int j) {
        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;
        int sumAlpha = 0;
        int count = 0;
        for (int cursorX = 0; cursorX < value; cursorX++) {
            for (int cursorY = 0; cursorY < value; cursorY++) {
                int cursorWidth = i * value + cursorX;
                int cursorHeight = j * value + cursorY;
                if (cursorWidth >= width) {
                    cursorWidth = width - 1;
                }
                if (cursorHeight >= height) {
                    cursorHeight = height - 1;
                }
                int color = image.getRGB(cursorWidth, cursorHeight);
                Color c = new Color(color);
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                int alpha = c.getAlpha();
                sumRed += red;
                sumGreen += green;
                sumBlue += blue;
                sumAlpha += alpha;
                ++count;
            }
        }
        int avgRed = sumRed / count;
        int avgGreen = sumGreen / count;
        int avgBlue = sumBlue / count;
        int avgAlpha = sumAlpha / count;
        return new Color(avgRed, avgGreen, avgBlue, avgAlpha);
    }
}
