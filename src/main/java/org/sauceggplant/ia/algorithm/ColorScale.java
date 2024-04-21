package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
import org.sauceggplant.ia.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * 色阶
 */
public class ColorScale implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ColorScale.class);

    private static final String OPEN = "ia.ui.io.file.open";

    @Override
    public void run(IaPanel iaPanel) {
        logger.info("ColorScale：色阶");
        //打开的图像
        BufferedImage image = iaPanel.getContent().getImage();
        if (null == image) {
            logger.error(PropertiesUtil.getProperty(OPEN));
            return;
        }

        //界面宽高
        int width = iaPanel.getOutput().getWidth();
        int height = iaPanel.getOutput().getHeight();

        //输出图像
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();

        //水平边距
        int marginH = 50;
        //垂直边距
        int marginV = 40;

        //清空背景，设置背景白色
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);

        //绘制坐标线
        graphics.setColor(Color.BLUE);
        graphics.drawLine(marginH, height - marginV, width - marginH, height - marginV);
        graphics.drawLine(marginH, marginV, marginH, height - marginV);

        //计算直方图
        Map<Integer, Integer> histogram = calcHistogram(image);

        //每个直方图区域的宽度
        double eachWidth = (width - 4 * marginH) / 256;

        //颜色统计直方图的最大值
        int maxValue = maxHistogram(histogram);

        //每个直方图区域的高度
        double eachHeight;
        //颜色统计直方图的最大值对应的块数
        int maxHeight;

        if (maxValue > (height - 4 * marginV)) {
            eachHeight = maxValue / (height - 4 * marginV);
            maxHeight = (int) (maxValue / eachHeight);
        } else {
            eachHeight = (height - 4 * marginV) / maxValue;
            maxHeight = (int) (eachHeight * maxValue);
        }

        //绘制纵坐标数值
        for (int i = 1; i < maxHeight; i++) {
            if (i % 50 == 0) {
                graphics.setColor(Color.BLUE);
                graphics.drawString("" + (int)(i * eachHeight), 15, height - marginV - maxHeight + (maxHeight - i));
            }
        }

        //横坐标按照灰度的颜色从0--255循环，对每个颜色绘制直方图
        for (int i = 0; i < 256; i++) {
            int x1 = (int) (eachWidth * i);
            int y1;
            if (maxValue > (height - 4 * marginV)) {
                y1 = (int) (histogram.get(i) / eachHeight);
            } else {
                y1 = (int) (eachHeight * histogram.get(i));
            }

            //绘制直方图数值
            graphics.setColor(Color.GRAY);
            graphics.drawRect(x1 + marginH, height - marginV - y1, (int) eachWidth, y1);
            //graphics.fillRect(x1 + marginH, height - marginV - y1, (int) eachWidth, y1);

            //绘制横坐标数值
            if (i % 15 == 0) {
                graphics.setColor(Color.BLUE);
                graphics.drawString("" + i, x1 + marginH, height - 25);
            }
        }
        graphics.dispose();
        iaPanel.getOutput().setImage(bufferedImage);
    }

    /**
     * 求直方图最大值
     *
     * @param histogram 直方图数据
     * @return 最大值
     */
    public Integer maxHistogram(Map<Integer, Integer> histogram) {
        int max = 0;
        for (int i = 0; i < 256; i++) {
            if (histogram.get(i) > max) {
                max = histogram.get(i);
            }
        }
        return max;
    }

    /**
     * 计算直方图数据
     *
     * @param image 图像
     */
    public Map<Integer, Integer> calcHistogram(BufferedImage image) {
        Map<Integer, Integer> histogram = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            histogram.put(i, 0);
        }
        //图像宽度，高度
        int width = image.getData().getWidth();
        int height = image.getData().getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //获取像素点的颜色(红，绿，蓝),RGB色彩空间
                int rgb = image.getRGB(i, j);
                Color color = new Color(rgb);
                //对红，绿，蓝求和再求平均值，将平均值赋值回原来的红，绿，蓝颜色分量，使红==绿==蓝
                int avg = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                int value = histogram.get(avg);
                histogram.put(avg, ++value);
            }
        }
        return histogram;
    }
}
