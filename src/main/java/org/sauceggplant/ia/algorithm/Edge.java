package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
import org.sauceggplant.ia.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * 边缘检测
 */
public class Edge implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Edge.class);

    private static final String OPEN = "ia.ui.io.file.open";
    private static final String TITLE = "ia.ui.edge.title";
    private static final String BTN_OK = "ia.ui.btn.ok";

    @Override
    public void run(IaPanel iaPanel) {
        logger.info("Edge:边缘检测");
        BufferedImage image = iaPanel.getContent().getImage();
        if (null == image) {
            logger.error(PropertiesUtil.getProperty(OPEN));
            return;
        }

        //第一步：灰度
        logger.info("计算灰度");
        BufferedImage grayImage = gray(image);

        //第二步：高斯滤波
        logger.info("高斯滤波");
        BufferedImage gaussianImage = gaussian(grayImage);

        //第三步：Sobel
        logger.info("Sobel计算");
        BufferedImage sobelGx = sobelGx(gaussianImage);
        BufferedImage sobelGy = sobelGy(gaussianImage);

        //第四步：梯度和方向
        logger.info("梯度和方向计算");
        double[][] gradient = gradient(sobelGx, sobelGy);
        double[][] theta = theta(sobelGx, sobelGy);


        JSlider sliderLow = new JSlider(JSlider.HORIZONTAL, 0, 361, 0);
        sliderLow.setToolTipText(PropertiesUtil.getProperty(TITLE));
        sliderLow.setMajorTickSpacing(30);
        sliderLow.setMinorTickSpacing(1);
        sliderLow.setPaintLabels(true);
        sliderLow.setPaintTicks(true);
        sliderLow.setPreferredSize(new Dimension(480, 100));

        JSlider sliderHigh = new JSlider(JSlider.HORIZONTAL, 0, 361, 361);
        sliderHigh.setToolTipText(PropertiesUtil.getProperty(TITLE));
        sliderHigh.setMajorTickSpacing(30);
        sliderHigh.setMinorTickSpacing(1);
        sliderHigh.setPaintLabels(true);
        sliderHigh.setPaintTicks(true);
        sliderHigh.setPreferredSize(new Dimension(480, 100));

        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle(PropertiesUtil.getProperty(TITLE));
        dialog.setPreferredSize(new Dimension(550, 300));
        dialog.setSize(new Dimension(550, 300));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));
        panel.add(new JLabel("低阈值"));
        panel.add(sliderLow);
        panel.add(new JLabel("高阈值"));
        panel.add(sliderHigh);
        dialog.getContentPane().add(panel, BorderLayout.CENTER);
        JButton ok = new JButton(PropertiesUtil.getProperty(BTN_OK));
        ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int low = sliderLow.getValue();
                int high = sliderHigh.getValue();
                logger.info("低阈值:{}, 高阈值:{}", low, high);

                //第五步：非极大值抑制
                logger.info("非极大值抑制");
                double[][] threshold = threshold(gradient, theta, low);

                //第六步：双阈值处理
                logger.info("双阈值处理");
                BufferedImage image = canny(threshold, low, high);

                //第七步：边缘链接
                //TODO

                iaPanel.getOutput().setImage(image);
                dialog.setVisible(false);
            }
        });
        dialog.getContentPane().add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * 灰度
     *
     * @param image 图像
     * @return 灰度图像
     */
    public BufferedImage gray(BufferedImage image) {
        //图像宽度，高度
        int width = image.getData().getWidth();
        int height = image.getData().getHeight();
        //输出图像
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //定义返回
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //获取像素点的颜色(红，绿，蓝),RGB色彩空间
                int rgb = image.getRGB(i, j);
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

    /**
     * 高斯滤波
     *
     * @param image 图像
     * @return 高斯图像
     */
    public BufferedImage gaussian(BufferedImage image) {
        float[] core = new float[]{0.0625f, 0.125f, 0.0625f, 0.125f, 0.25f, 0.125f, 0.0625f, 0.125f, 0.0625f};
        Kernel kernel = new Kernel(3, 3, core);
        ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return convolveOp.filter(image, null);
    }

    /**
     * 索贝尔Gx
     *
     * @param image 图像
     * @return 索贝尔图像
     */
    public BufferedImage sobelGx(BufferedImage image) {
        float[] core = new float[]{-1.0f, 0.0f, 1.0f, -2.0f, 0.0f, 2.0f, -1.0f, 0.0f, 1.0f};
        Kernel kernel = new Kernel(3, 3, core);
        ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return convolveOp.filter(image, null);
    }

    /**
     * 索贝尔Gy
     *
     * @param image 图像
     * @return 索贝尔图像
     */
    public BufferedImage sobelGy(BufferedImage image) {
        float[] core = new float[]{-1.0f, -2.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f};
        Kernel kernel = new Kernel(3, 3, core);
        ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return convolveOp.filter(image, null);
    }

    /**
     * 计算梯度
     *
     * @param sobelGx 索贝尔Gx
     * @param sobelGy 索贝尔Gy
     * @return 梯度
     */
    public double[][] gradient(BufferedImage sobelGx, BufferedImage sobelGy) {
        //图像宽度，高度
        int width = sobelGx.getData().getWidth();
        int height = sobelGx.getData().getHeight();
        double[][] result = new double[width][height];
        //定义返回
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                result[i][j] = Math.sqrt(new Color(sobelGx.getRGB(i, j)).getRed()
                        * new Color(sobelGx.getRGB(i, j)).getRed()
                        + new Color(sobelGy.getRGB(i, j)).getRed()
                        * new Color(sobelGy.getRGB(i, j)).getRed());
            }
        }
        return result;
    }

    /**
     * 计算角度
     *
     * @param sobelGx 索贝尔Gx
     * @param sobelGy 索贝尔Gy
     * @return 角度
     */
    public double[][] theta(BufferedImage sobelGx, BufferedImage sobelGy) {
        //图像宽度，高度
        int width = sobelGx.getData().getWidth();
        int height = sobelGx.getData().getHeight();
        double[][] result = new double[width][height];
        //定义返回
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (new Color(sobelGy.getRGB(i, j)).getRed() == 0) {
                    result[i][j] = Math.atan(0);
                } else {
                    result[i][j] = Math.atan((double) (new Color(sobelGy.getRGB(i, j)).getRed()) / (double) (new Color(sobelGx.getRGB(i, j)).getRed()));
                }
            }
        }
        return result;
    }

    /**
     * 非极大值抑制
     *
     * @param gradient     梯度
     * @param theta        角度
     * @param thresholdLow 低阈值
     * @return 抑制后的梯度
     */
    public double[][] threshold(double[][] gradient, double[][] theta, double thresholdLow) {
        for (int i = 0; i < gradient.length; i++) {
            for (int j = 0; j < gradient[0].length; j++) {
                gradient[i][j] = gradient[i][j] >= thresholdLow ? gradient[i][j] : 0;
                double thetaX = Math.cos(theta[i][j]);
                double thetaY = Math.sin(theta[i][j]);
                int dx = (int) (i + thetaX);
                int dy = (int) (j + thetaY);
                dx = dx < 0 ? 0 : (dx > (gradient.length - 1) ? (gradient.length - 1) : dx);
                dy = dy < 0 ? 0 : (dy > (gradient[0].length - 1) ? (gradient[0].length - 1) : dy);
                gradient[i][j] = gradient[i][j] >= gradient[dx][dy] ? gradient[i][j] : 0;
            }
        }
        return gradient;
    }

    /**
     * canny 计算
     *
     * @param threshold     梯度
     * @param thresholdLow  低阈值
     * @param thresholdHigh 高阈值
     * @return 图像
     */
    public BufferedImage canny(double[][] threshold, double thresholdLow, double thresholdHigh) {
        BufferedImage image = new BufferedImage(threshold.length, threshold[0].length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < threshold.length; i++) {
            for (int j = 0; j < threshold[0].length; j++) {
                if (threshold[i][j] >= thresholdHigh) {
                    image.setRGB(i, j, Color.WHITE.getRGB());
                } else if (threshold[i][j] < thresholdHigh && threshold[i][j] >= thresholdLow) {
                    image.setRGB(i, j, Color.GRAY.getRGB());
                } else {
                    image.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }
        return image;
    }
}
