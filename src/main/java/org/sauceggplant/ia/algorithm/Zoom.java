package org.sauceggplant.ia.algorithm;

import ch.qos.logback.core.util.StringUtil;
import org.sauceggplant.ia.ui.IaPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 缩放
 */
public class Zoom {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Zoom.class);

    /**
     * 面板
     */
    private IaPanel iaPanel;

    /**
     * 缩放
     *
     * @param iaPanel 面板
     */
    public void run(IaPanel iaPanel) {
        logger.info("菜单：缩放");
        this.iaPanel = iaPanel;
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
        slider.setToolTipText("图像缩放比例");
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(2);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle("缩放百分比");
        dialog.setPreferredSize(new Dimension(500, 100));
        dialog.setSize(new Dimension(500, 100));
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(slider, BorderLayout.CENTER);
        JButton ok = new JButton("确定");
        ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int value = slider.getValue();
                if (StringUtil.isNullOrEmpty(iaPanel.getPath())) {
                    logger.error("图片文件路径为空，请先打开一张图片");
                    return;
                }
                try {
                    BufferedImage bufferedImage = ImageIO.read(new File(iaPanel.getPath()));
                    double rate = value / 100.0D;
                    logger.info("图片缩放比例:{}", rate);
                    iaPanel.getOutput().setImage(zoom(bufferedImage, rate));
                } catch (IOException e1) {
                    logger.error("图片文件路径:{}", iaPanel.getPath(), e1);
                }
            }
        });
        dialog.getContentPane().add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * 图像缩放算法实现
     *
     * @param bufferedImage 图像
     * @param rate          缩放比例
     * @return 输出图像
     */
    public BufferedImage zoom(BufferedImage bufferedImage, double rate) {
        //图像宽度，高度
        int width = bufferedImage.getData().getWidth();
        int height = bufferedImage.getData().getHeight();

        //缩放后的宽度，高度
        int changWidth = (int) (width * rate);
        int changeHeight = (int) (height * rate);

        //构建图像，返回结果
        BufferedImage result = new BufferedImage(changWidth, changeHeight, bufferedImage.getType());

        if (rate == 1) {
            //比例不变
            return bufferedImage;
        } else {
            for (int i = 0; i < changWidth; i++) {
                for (int j = 0; j < changeHeight; j++) {
                    //按照比例获取像素点的RGB
                    result.setRGB(i, j, bufferedImage.getRGB((int) (i / rate), (int) (j / rate)));
                }
            }
            return result;
        }
    }
}
