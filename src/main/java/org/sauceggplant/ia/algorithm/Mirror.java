package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
import org.sauceggplant.ia.util.IconUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * 景象
 */
public class Mirror implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Mirror.class);

    /**
     * 界面
     */
    private IaPanel iaPanel;

    @Override
    public void run(IaPanel iaPanel) {
        logger.info("菜单：镜像");
        this.iaPanel = iaPanel;
        int result = JOptionPane.showOptionDialog(iaPanel.getIaWindow(),
                "请选择镜像类型",
                "镜像", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                IconUtil.getIcon("icon.png", "镜像", 16),
                new String[]{"水平镜像", "垂直镜像"}, "水平镜像");
        switch (result) {
            case 0:
                mirrorH();
                break;
            case 1:
                mirrorV();
                break;
        }
    }

    /**
     * 水平镜像
     */
    public void mirrorH() {
        BufferedImage image = iaPanel.getContent().getImage();
        if (null == image) {
            logger.error("请先打开一张图片");
            return;
        }
        int width = image.getData().getWidth();
        int height = image.getData().getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                result.setRGB(width - 1 - i, j, rgb);
            }
        }
        iaPanel.getOutput().setImage(result);
    }

    /**
     * 垂直镜像
     */
    public void mirrorV() {
        BufferedImage image = iaPanel.getContent().getImage();
        if (null == image) {
            logger.error("请先打开一张图片");
            return;
        }
        int width = image.getData().getWidth();
        int height = image.getData().getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                result.setRGB(i, height - 1 - j, rgb);
            }
        }
        iaPanel.getOutput().setImage(result);
    }
}
