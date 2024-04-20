package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * 高斯模糊
 */
public class GaussianBlur implements Algorithm {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(GaussianBlur.class);

    @Override
    public void run(IaPanel iaPanel) {
        logger.info("菜单：高斯模糊");
        BufferedImage image = iaPanel.getContent().getImage();
        if (null == image) {
            logger.error("请先打开一张图片");
            return;
        }

        JSlider radiusSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 1);
        radiusSlider.setToolTipText("半径(Radius):");
        radiusSlider.setMajorTickSpacing(1);
        radiusSlider.setMinorTickSpacing(1);
        radiusSlider.setPaintLabels(true);
        radiusSlider.setPaintTicks(true);
        radiusSlider.setPreferredSize(new Dimension(480, 80));

        JSlider sigmaSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 10);
        sigmaSlider.setToolTipText("标准差σ(Sigma)(x10):");
        sigmaSlider.setMajorTickSpacing(2);
        sigmaSlider.setMinorTickSpacing(1);
        sigmaSlider.setPaintLabels(true);
        sigmaSlider.setPaintTicks(true);
        sigmaSlider.setPreferredSize(new Dimension(480, 80));

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));
        JPanel radiusPanel = new JPanel();
        radiusPanel.setPreferredSize(new Dimension(600, 80));
        radiusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        radiusPanel.add(new JLabel("半径:"));
        radiusPanel.add(radiusSlider);

        JPanel sigmaPanel = new JPanel();
        sigmaPanel.setPreferredSize(new Dimension(600, 80));
        sigmaPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        sigmaPanel.add(new JLabel("σ:"));
        sigmaPanel.add(sigmaSlider);

        panel.add(radiusPanel);
        panel.add(sigmaPanel);

        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle("高斯模糊参数");
        dialog.setPreferredSize(new Dimension(600, 240));
        dialog.setSize(new Dimension(600, 240));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());
        JPanel blank = new JPanel();
        blank.setPreferredSize(new Dimension(20, 20));
        dialog.getContentPane().add(panel, BorderLayout.CENTER);
        dialog.getContentPane().add(blank, BorderLayout.EAST);
        JButton ok = new JButton("确定");
        ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double radius = (double) (radiusSlider.getValue() / radiusSlider.getMinorTickSpacing());
                double sigma = (double) (sigmaSlider.getValue() / sigmaSlider.getMinorTickSpacing() / 10.0);
                double[][] matrix = normalization(gaussianMatrix(radius, sigma));
                printMatrix(radius, sigma, matrix);
                BufferedImage result = gaussian(iaPanel.getContent().getImage(), matrix);
                iaPanel.getOutput().setImage(result);
                dialog.setVisible(false);
            }
        });
        dialog.getContentPane().add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public BufferedImage gaussian(BufferedImage image, double[][] matrix) {
        int width = image.getData().getWidth();
        int height = image.getData().getHeight();

        int mWidth = matrix.length;
        int mHeight = matrix[0].length;

        BufferedImage result = new BufferedImage(width, height, image.getType());

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //对图像每个像素点卷积处理

                //每个像素点需要计算卷积的坐标范围
                int x1 = i - mWidth / 2;
                int y1 = j - mHeight / 2;
                int x2 = i + mWidth / 2;
                int y2 = j + mHeight / 2;

                //对需要计算卷积范围的像素点遍历
                double sumAlpha = 0;
                double sumRed = 0;
                double sumGreen = 0;
                double sumBlue = 0;
                for (int x = x1, a = 0; x <= x2; x++, a++) {
                    for (int y = y1, b = 0; y <= y2; y++, b++) {
                        int tempX = x < 0 ? 0 : (x >= width ? (width - 1) : x);
                        int tempY = y < 0 ? 0 : (y >= height ? (height - 1) : y);
                        int rgb = image.getRGB(tempX, tempY);
                        Color c = new Color(rgb);
                        double rate = matrix[a][b];
                        sumAlpha = sumAlpha + c.getAlpha() * rate;
                        sumRed = sumRed + c.getRed() * rate;
                        sumGreen = sumGreen + c.getGreen() * rate;
                        sumBlue = sumBlue + c.getBlue() * rate;
                    }
                }
                sumAlpha = sumAlpha < 0 ? 0 : (sumAlpha > 255 ? 255 : sumAlpha);
                sumRed = sumRed < 0 ? 0 : (sumRed > 255 ? 255 : sumRed);
                sumGreen = sumGreen < 0 ? 0 : (sumGreen > 255 ? 255 : sumGreen);
                sumBlue = sumBlue < 0 ? 0 : (sumBlue > 255 ? 255 : sumBlue);
                result.setRGB(i, j, new Color((int)sumRed,(int)sumGreen,(int)sumBlue,(int)sumAlpha).getRGB());
            }
        }

        return result;
    }

    /**
     * 打印矩阵
     *
     * @param radius 半径
     * @param sigma  标准差
     * @param matrix 矩阵
     */
    public void printMatrix(double radius, double sigma, double[][] matrix) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                stringBuffer.append(matrix[i][j]).append("\t");
            }
            stringBuffer.append("\n");
        }
        logger.info("radius:{},sigma:{}高斯矩阵:\n{}", radius, sigma, stringBuffer.toString());
    }

//    /**
//     * 归一化
//     *
//     * @param matrix 矩阵
//     * @return 归一化矩阵
//     */
//    public double[][] normalization(double[][] matrix) {
//        double[][] result = new double[matrix.length][matrix[0].length];
//        double max = matrix[0][0];
//        double min = matrix[0][0];
//        for (int i = 0; i < matrix.length; i++) {
//            for (int j = 0; j < matrix[0].length; j++) {
//                if (matrix[i][j] > max) {
//                    max = matrix[i][j];
//                }
//                if (matrix[i][j] < min) {
//                    min = matrix[i][j];
//                }
//            }
//        }
//        for (int i = 0; i < matrix.length; i++) {
//            for (int j = 0; j < matrix[0].length; j++) {
//                result[i][j] = (matrix[i][j] - min) / (max - min);
//            }
//        }
//        return result;
//    }

    /**
     * 归一化
     *
     * @param matrix 矩阵
     * @return 归一化矩阵
     */
    public double[][] normalization(double[][] matrix) {
        double[][] result = new double[matrix.length][matrix[0].length];
        double sum = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                sum += matrix[i][j];
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                result[i][j] = matrix[i][j] / sum;
            }
        }
        return result;
    }

    /**
     * 高斯矩阵
     *
     * @param radius 半径
     * @param sigma  参数
     * @return 高斯矩阵
     */
    public double[][] gaussianMatrix(double radius, double sigma) {
        //向上取整
        int r = (int) Math.ceil(radius);
        //设定高斯矩阵
        double[][] result = new double[2 * r + 1][2 * r + 1];
        //对高斯矩阵的坐标循环
        for (int i = 0; i <= 2 * r; i++) {
            for (int j = 0; j <= 2 * r; j++) {
                //temp=x^2+y^2
                double temp = Math.abs(i - r) * Math.abs(i - r) + Math.abs(j - r) * Math.abs(j - r);
                //当前坐标距离中心点的距离,中心点 (x=r,y=r)
                double s = Math.sqrt(temp);
                //距离超出半径，权重为0
                if (s > radius) {
                    result[i][j] = 0;
                }
                result[i][j] = calcWeight(temp, sigma);
            }
        }
        return result;
    }

    /**
     * 权重
     *
     * @param temp  平方和
     * @param sigma 参数
     * @return 权重
     */
    public double calcWeight(double temp, double sigma) {
        return 1 / (2.0d * Math.PI * sigma * sigma) * Math.pow(Math.E, temp * (-1.0d) / (2 * sigma * sigma));
    }
}
