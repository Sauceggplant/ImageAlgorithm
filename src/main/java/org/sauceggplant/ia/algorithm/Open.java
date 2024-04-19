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
 * 打开
 */
public class Open implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Open.class);

    /**
     * 打开
     *
     * @param iaPanel 面板
     */
    @Override
    public void run(IaPanel iaPanel) {
        logger.info("菜单：打开");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("打开");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = fileChooser.showOpenDialog(iaPanel);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectFile = fileChooser.getSelectedFile();
            try {
                BufferedImage bufferedImage = ImageIO.read(selectFile);
                iaPanel.setPath(selectFile.getAbsolutePath());
                logger.info("图片文件路径:{}", selectFile.getAbsolutePath());
                iaPanel.getContent().setImage(bufferedImage);
            } catch (IOException e) {
                logger.error("图片文件路径:{}", selectFile.getAbsolutePath(), e);
            }
        }
    }
}
