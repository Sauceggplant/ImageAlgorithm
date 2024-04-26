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
 * 色阶差
 */
public class DeltaColorScale implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DeltaColorScale.class);

    private static final String OPEN = "ia.ui.io.file.open";

    @Override
    public void run(IaPanel iaPanel) {
        logger.info("DeltaColorScale:色差分布");
        //打开的图像
        BufferedImage image = iaPanel.getContent().getImage();
        if (null == image) {
            logger.error(PropertiesUtil.getProperty(OPEN));
            return;
        }
        BufferedImage bufferedImage = deltaColorScale(iaPanel);
        iaPanel.getOutput().setImage(bufferedImage);
    }

    public BufferedImage deltaColorScale(IaPanel iaPanel) {
        //界面宽高
        int width = iaPanel.getOutput().getWidth();
        int height = iaPanel.getOutput().getHeight();

        //输出图像
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();

        //水平边距
        int marginH = 70;
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
        Map<Integer, Integer> histogram = calcHistogram(iaPanel.getContent().getImage());

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
                graphics.drawString("" + (int) (i * eachHeight), 15, height - marginV - maxHeight + (maxHeight - i));
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
        return bufferedImage;
    }

    /**
     * 计算直方图
     *
     * @param image
     * @return
     */
    public Map<Integer, Integer> calcHistogram(BufferedImage image) {
        Map<Integer, Integer> histogram = new HashMap<>();
        //图像宽度，高度
        int width = image.getData().getWidth();
        int height = image.getData().getHeight();

        for (int i = 0; i < 256; i++) {
            histogram.put(i, 0);
        }

        //定义返回
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //每个像素点

                //计算每个像素点的周围的区域
                int dxMin = i - 1 < 0 ? 0 : (i - 1);
                int dyMin = j - 1 < 0 ? 0 : (j - 1);

                //当前像素点颜色
                int rgb = image.getRGB(i, j);
                Color c = new Color(rgb);
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                int maxd = 0;
                //对区域做边缘计算
                for (int dx = dxMin; dx <= i; dx++) {
                    for (int dy = dyMin; dy <= j; dy++) {
                        if (dx == i && dy == j) {
                            continue;
                        }
                        //周围像素点颜色
                        int drgb = image.getRGB(dx, dy);
                        Color crgb = new Color(drgb);
                        int dred = red - crgb.getRed();
                        int dgreen = green - crgb.getGreen();
                        int dblue = blue - crgb.getBlue();
                        int d = Math.abs(dred) > Math.abs(dgreen) ? (Math.abs(dred) > Math.abs(dblue) ? dred : dblue) : (Math.abs(dgreen) > Math.abs(dblue) ? dgreen : dblue);
                        //maxd = Math.abs(d) > maxd ? Math.abs(d) : maxd;
                        int value = histogram.get(Math.abs(d));
                        histogram.put(Math.abs(d), ++value);
                    }
                }
//                int value = histogram.get(maxd);
//                histogram.put(Math.abs(maxd), ++value);
            }
        }
        return histogram;
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
}
