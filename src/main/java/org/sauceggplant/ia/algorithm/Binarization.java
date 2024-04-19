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
 * 二值化
 */
public class Binarization {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Binarization.class);

    /**
     * 缩放
     *
     * @param iaPanel 面板
     */
    public void run(IaPanel iaPanel) {
        logger.info("菜单：二值化");
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 255, 128);
        slider.setToolTipText("二值化阈值");
        slider.setMajorTickSpacing(32);
        slider.setMinorTickSpacing(8);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle("二值化阈值");
        dialog.setPreferredSize(new Dimension(500, 100));
        dialog.setSize(new Dimension(500, 100));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(slider, BorderLayout.CENTER);
        JButton ok = new JButton("确定");
        ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int value = slider.getValue();
                logger.info("二值化阈值:{}", value);
                if (StringUtil.isNullOrEmpty(iaPanel.getPath())) {
                    logger.error("图片文件路径为空，请先打开一张图片");
                    return;
                }
                try {
                    BufferedImage bufferedImage = ImageIO.read(new File(iaPanel.getPath()));
                    iaPanel.getOutput().setImage(binarization(bufferedImage, value));
                } catch (IOException e1) {
                    logger.error("图片文件路径:{}", iaPanel.getPath(), e1);
                }
                dialog.setVisible(false);
            }
        });
        dialog.getContentPane().add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * 二值化处理算法
     * @param bufferedImage 图像
     * @param value 阈值
     * @return 二值化处理后的图像
     */
    public BufferedImage binarization(BufferedImage bufferedImage, int value) {
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
                //对红，绿，蓝求和再求平均值，平均值与阈值比较，高于阈值为白色，低于阈值为黑色
                int avg = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                //赋值，生成黑白图像，进行二值化
                if (avg >= value) {
                    result.setRGB(i, j, Color.WHITE.getRGB());
                } else {
                    result.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }
        return result;
    }
}
