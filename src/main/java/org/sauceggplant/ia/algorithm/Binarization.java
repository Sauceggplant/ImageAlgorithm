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
 * 二值化
 */
public class Binarization implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Binarization.class);

    private static final String OPEN = "ia.ui.io.file.open";

    private static final String TITLE = "ia.ui.binarization.title";

    private static final String BTN_OK = "ia.ui.btn.ok";

    /**
     * 缩放
     *
     * @param iaPanel 面板
     */
    @Override
    public void run(IaPanel iaPanel) {
        logger.info("Binarization:二值化");
        BufferedImage image = iaPanel.getContent().getImage();
        if (null == image) {
            logger.error(PropertiesUtil.getProperty(OPEN));
            return;
        }

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 255, 128);
        slider.setToolTipText(PropertiesUtil.getProperty(TITLE));
        slider.setMajorTickSpacing(32);
        slider.setMinorTickSpacing(8);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle(PropertiesUtil.getProperty(TITLE));
        dialog.setPreferredSize(new Dimension(500, 100));
        dialog.setSize(new Dimension(500, 100));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(slider, BorderLayout.CENTER);
        JButton ok = new JButton(PropertiesUtil.getProperty(BTN_OK));
        ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int value = slider.getValue();
                logger.info("{}:{}", PropertiesUtil.getProperty(TITLE), value);
                BufferedImage bufferedImage = iaPanel.getContent().getImage();
                if (null != bufferedImage) {
                    iaPanel.getOutput().setImage(binarization(bufferedImage, value));
                } else {
                    logger.error(PropertiesUtil.getProperty(OPEN));
                }
                dialog.setVisible(false);
            }
        });
        dialog.getContentPane().add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * 二值化处理算法
     *
     * @param bufferedImage 图像
     * @param value         阈值
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
