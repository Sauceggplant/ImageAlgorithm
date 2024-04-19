package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 存储
 */
public class Save {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Save.class);

    /**
     * 存储
     *
     * @param iaPanel 面板
     */
    public void run(IaPanel iaPanel) {
        logger.info("菜单：存储");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("存储");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showSaveDialog(iaPanel);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            logger.info("存储的路径为：{}", file.getAbsolutePath());
            BufferedImage image = iaPanel.getOutput().getImage();
            if (null == image) {
                logger.info("图像保存失败, 输出图像为空");
                return;
            }
            try {
                ImageIO.write(image, "png", file);
            } catch (IOException e) {
                logger.info("图像保存失败", e);
            }
            logger.info("图像保存成功");
        }
    }
}
