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
 * 旋转
 */
public class Rotate implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Rotate.class);

    private static final String OPEN = "ia.ui.io.file.open";

    @Override
    public void run(IaPanel iaPanel) {
        logger.info("Rotate:旋转");
        BufferedImage bufferedImage = iaPanel.getContent().getImage();
        if (null == bufferedImage) {
            logger.error(PropertiesUtil.getProperty(OPEN));
            return;
        }

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 360, 90);
        slider.setToolTipText("旋转角度");
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle("旋转角度");
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
                logger.info("旋转角度:{}", value);
                BufferedImage bufferedImage = iaPanel.getContent().getImage();
                iaPanel.getOutput().setImage(rotate(bufferedImage, value, Color.WHITE.getRGB()));
                dialog.setVisible(false);
            }
        });
        dialog.getContentPane().add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * 图片旋转
     *
     * @param image           图像
     * @param theta           角度
     * @param backgroundColor 背景色
     * @return 旋转后图像
     */
    private BufferedImage rotate(BufferedImage image, int theta, int backgroundColor) {
        //图像宽高
        int width = image.getWidth();
        int height = image.getHeight();

        //旋转弧度
        double angle = theta * Math.PI / 180;//角度转弧度

        //旋转后宽高
        int width1 = (int) getWidth(width, height, angle);
        int height1 = (int) getHeight(width, height, angle);

        //输出图像
        BufferedImage result = new BufferedImage(width1, height1, image.getType());
        for (int i = 0; i < width1; i++) {
            for (int j = 0; j < height1; j++) {
                int x = i - width1 / 2;
                int y = height1 / 2 - j;
                //半径,距离
                double radius = Math.sqrt(x * x + y * y);
                double angle1;
                if (y > 0) {
                    angle1 = Math.acos(x / radius);
                } else {
                    angle1 = 2 * Math.PI - Math.acos(x / radius);
                }
                x = (int) (radius * Math.cos(angle1 - angle));
                y = (int) (radius * Math.sin(angle1 - angle));
                if (x < (width / 2) & x > -(width / 2) & y < (height / 2) & y > -(height / 2)) {
                    //在图像范围内，取像素点的颜色值
                    int rgb = image.getRGB(x + width / 2, height / 2 - y);
                    result.setRGB(i, j, rgb);
                } else {
                    //不在图像范围内，取背景色
                    result.setRGB(i, j, backgroundColor);
                }
            }
        }
        return result;
    }

    //旋转后图像宽度
    public double getWidth(int width, int height, double angle) {
        //半径
        double radius = Math.sqrt((width / 2) * (width / 2) + (height / 2) * (height / 2));
        //弧度
        double angle1 = Math.acos((width / 2) / radius);
        //距离1
        double maxX1 = radius * Math.cos(angle1 + angle);
        //距离2
        double maxX2 = radius * Math.cos(Math.PI - angle1 + angle);
        return 2 * Math.max(Math.abs(maxX1), Math.abs(maxX2));
    }

    //旋转后图像高度
    public double getHeight(int width, int height, double angle) {
        //半径
        double radius = Math.sqrt((width / 2) * (width / 2) + (height / 2) * (height / 2));
        //弧度
        double angle1 = Math.asin((height / 2) / radius);
        //距离1
        double maxY1 = radius * Math.sin(angle1 + angle);
        //距离2
        double maxY2 = radius * Math.sin(Math.PI - angle1 + angle);
        return 2 * Math.max(Math.abs(maxY1), Math.abs(maxY2));
    }
}
