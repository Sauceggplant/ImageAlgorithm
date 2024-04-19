package org.sauceggplant.ia.algorithm;

import ch.qos.logback.core.util.StringUtil;
import org.sauceggplant.ia.ui.IaPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 灰度
 */
public class Gray implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Gray.class);

    /**
     * 灰度
     *
     * @param iaPanel 面板
     */
    @Override
    public void run(IaPanel iaPanel) {
        logger.info("菜单：灰度");
        if (StringUtil.isNullOrEmpty(iaPanel.getPath())) {
            logger.error("图片文件路径为空，请先打开一张图片");
            return;
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(iaPanel.getPath()));
            iaPanel.getOutput().setImage(gray(bufferedImage));
        } catch (IOException e1) {
            logger.error("图片文件路径:{}", iaPanel.getPath(), e1);
        }
    }

    /**
     * 图像灰度算法
     * @param bufferedImage 图像
     * @return 灰度处理后图像
     */
    public static BufferedImage gray(BufferedImage bufferedImage) {
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
}
