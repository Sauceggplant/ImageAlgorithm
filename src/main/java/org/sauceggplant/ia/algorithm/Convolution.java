package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

/**
 * 卷积
 */
public class Convolution implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Convolution.class);

    /**
     * 三阶算子
     */
    private static double[][][] core = new double[][][]{
            {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}},//索贝尔(Sobel)水平
            {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}},//索贝尔(Sobel)垂直
            {{1, 1, 1}, {0, 0, 0}, {-1, -1, -1}},//Prewitt水平
            {{1, 0, -1}, {1, 0, -1}, {1, 0, -1}},//Prewitt垂直
            {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}},//拉普拉斯(Laplacian)4
            {{1, 1, 1}, {1, -8, 1}, {1, 1, 1}},//拉普拉斯(Laplacian)8
            {{-1, -0.5, -1}, {-0.5, 6, -0.5}, {-1, -0.5, -1}},//拉普拉斯变形1
            {{1, 0.5, 1}, {0.5, -6, 0.5}, {1, 0.5, 1}},//拉普拉斯变形2
            {{1.0d / 9, 1.0d / 9, 1.0d / 9}, {1.0d / 9, 1.0d / 9, 1.0d / 9}, {1.0d / 9, 1.0d / 9, 1.0d / 9}},//均值模糊
            {{1 / 16.0d, 2 / 16.0d, 1 / 16.0d}, {2 / 16.0d, 4 / 16.0d, 2 / 16.0d}, {1 / 16.0d, 2 / 16.0d, 1 / 16.0d}},//高斯(Gaussian)模糊
            {{0.075, 0.124, 0.075}, {0.124, 0.204, 0.124}, {0.075, 0.124, 0.075}},//高斯(Gaussian)模糊，sigma=1，归一化
            {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}}//自定义
    };

    /**
     * 3*3算子
     */
    private JTextField[] coreData3 = new JTextField[9];

    @Override
    public void run(IaPanel iaPanel) {
        logger.info("菜单：卷积");

        if (null == iaPanel.getContent().getImage()) {
            logger.error("请先打开一张图片");
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel option = new JPanel();
        option.setLayout(new FlowLayout(FlowLayout.LEFT));
        option.add(new JLabel("算子"));
        String[] coreItems = new String[]{
                "索贝尔(Sobel)水平",
                "索贝尔(Sobel)垂直",
                "Prewitt水平",
                "Prewitt垂直",
                "拉普拉斯(Laplacian)4",
                "拉普拉斯(Laplacian)8",
                "拉普拉斯变形1",
                "拉普拉斯变形2",
                "均值模糊",
                "高斯(Gaussian)模糊",
                "高斯(Gaussian)模糊，sigma=1，归一化",
                "自定义算子"};
        JComboBox<String> coreComboBox = new JComboBox(coreItems);
        coreComboBox.setSelectedIndex(0);
        option.add(coreComboBox);
        panel.add(option, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new GridLayout(3, 3));
        initCoreData3(0);
        for (int i = 0; i < 9; i++) {
            content.add(coreData3[i]);
        }
        panel.add(content, BorderLayout.CENTER);
        coreComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int index = coreComboBox.getSelectedIndex();
                    ((JTextField) (content.getComponents()[0])).setText("" + core[index][0][0]);
                    ((JTextField) (content.getComponents()[1])).setText("" + core[index][0][1]);
                    ((JTextField) (content.getComponents()[2])).setText("" + core[index][0][2]);
                    ((JTextField) (content.getComponents()[3])).setText("" + core[index][1][0]);
                    ((JTextField) (content.getComponents()[4])).setText("" + core[index][1][1]);
                    ((JTextField) (content.getComponents()[5])).setText("" + core[index][1][2]);
                    ((JTextField) (content.getComponents()[6])).setText("" + core[index][2][0]);
                    ((JTextField) (content.getComponents()[7])).setText("" + core[index][2][1]);
                    ((JTextField) (content.getComponents()[8])).setText("" + core[index][2][2]);
                }
            }
        });

        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle("卷积");
        dialog.setPreferredSize(new Dimension(500, 400));
        dialog.setSize(new Dimension(500, 400));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(panel, BorderLayout.CENTER);
        JButton ok = new JButton("确定");
        ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //选择的算子序号
                int index = coreComboBox.getSelectedIndex();
                logger.info("卷积：选择的算子序号:{}", index);
                //选择的算子
                double[][] selectCore;
                if (index == coreComboBox.getItemCount() - 1) {
                    selectCore = new double[3][3];
                    selectCore[0][0] = Double.parseDouble(coreData3[0].getText());
                    selectCore[0][1] = Double.parseDouble(coreData3[1].getText());
                    selectCore[0][2] = Double.parseDouble(coreData3[2].getText());
                    selectCore[1][0] = Double.parseDouble(coreData3[3].getText());
                    selectCore[1][1] = Double.parseDouble(coreData3[4].getText());
                    selectCore[1][2] = Double.parseDouble(coreData3[5].getText());
                    selectCore[2][0] = Double.parseDouble(coreData3[6].getText());
                    selectCore[2][1] = Double.parseDouble(coreData3[7].getText());
                    selectCore[2][1] = Double.parseDouble(coreData3[8].getText());
                } else {
                    selectCore = core[index];
                }

                //打印算子
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < selectCore.length; i++) {
                    for (int j = 0; j < selectCore[0].length; j++) {
                        stringBuffer.append(selectCore[i][j]).append("\t");
                    }
                    stringBuffer.append("\n");
                }
                logger.info("三阶算子为:{}\n {}", coreItems[index], stringBuffer.toString());

                //图像卷积计算
                BufferedImage image = convolution(iaPanel.getContent().getImage(), selectCore);
                //输出图像
                iaPanel.getOutput().setImage(image);
                dialog.setVisible(false);
            }
        });
        dialog.getContentPane().add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void initCoreData3(int selectCoreIndex) {
        coreData3[0] = new JTextField("" + core[selectCoreIndex][0][0]);
        coreData3[1] = new JTextField("" + core[selectCoreIndex][0][1]);
        coreData3[2] = new JTextField("" + core[selectCoreIndex][0][2]);
        coreData3[3] = new JTextField("" + core[selectCoreIndex][1][0]);
        coreData3[4] = new JTextField("" + core[selectCoreIndex][1][1]);
        coreData3[5] = new JTextField("" + core[selectCoreIndex][1][2]);
        coreData3[6] = new JTextField("" + core[selectCoreIndex][2][0]);
        coreData3[7] = new JTextField("" + core[selectCoreIndex][2][1]);
        coreData3[8] = new JTextField("" + core[selectCoreIndex][2][2]);
    }

    public BufferedImage convolution(BufferedImage image, double[][] core) {
        //图像的宽高
        int width = image.getData().getWidth();
        int height = image.getData().getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //边界坐标值处理
                int x0 = i - 1;
                int x1 = i + 1;
                int y0 = j - 1;
                int y1 = j + 1;
                x0 = x0 < 0 ? 0 : x0;
                x1 = x1 >= width ? (width - 1) : x1;
                y0 = y0 < 0 ? 0 : y0;
                y1 = y1 >= height ? (height - 1) : y1;

                //获取颜色值
                int rgb00 = image.getRGB(x0, y0);
                int rgb01 = image.getRGB(x0, j);
                int rgb02 = image.getRGB(x0, y1);

                int rgb10 = image.getRGB(i, y0);
                int rgb11 = image.getRGB(i, j);
                int rgb12 = image.getRGB(i, y1);

                int rgb20 = image.getRGB(x1, y0);
                int rgb21 = image.getRGB(x1, j);
                int rgb22 = image.getRGB(x1, y1);

                //卷积计算
                double red = core[0][0] * new Color(rgb00).getRed()
                        + core[0][1] * new Color(rgb01).getRed()
                        + core[0][2] * new Color(rgb02).getRed()
                        + core[1][0] * new Color(rgb10).getRed()
                        + core[1][1] * new Color(rgb11).getRed()
                        + core[1][2] * new Color(rgb12).getRed()
                        + core[2][0] * new Color(rgb20).getRed()
                        + core[2][1] * new Color(rgb21).getRed()
                        + core[2][2] * new Color(rgb22).getRed();

                double green = core[0][0] * new Color(rgb00).getGreen()
                        + core[0][1] * new Color(rgb01).getGreen()
                        + core[0][2] * new Color(rgb02).getGreen()
                        + core[1][0] * new Color(rgb10).getGreen()
                        + core[1][1] * new Color(rgb11).getGreen()
                        + core[1][2] * new Color(rgb12).getGreen()
                        + core[2][0] * new Color(rgb20).getGreen()
                        + core[2][1] * new Color(rgb21).getGreen()
                        + core[2][2] * new Color(rgb22).getGreen();

                double blue = core[0][0] * new Color(rgb00).getBlue()
                        + core[0][1] * new Color(rgb01).getBlue()
                        + core[0][2] * new Color(rgb02).getBlue()
                        + core[1][0] * new Color(rgb10).getBlue()
                        + core[1][1] * new Color(rgb11).getBlue()
                        + core[1][2] * new Color(rgb12).getBlue()
                        + core[2][0] * new Color(rgb20).getBlue()
                        + core[2][1] * new Color(rgb21).getBlue()
                        + core[2][2] * new Color(rgb22).getBlue();

                //阈值(0-255)处理，卷积后超出边界后修正
                red = red < 0 ? 0 : (red > 255 ? 255 : red);
                green = green < 0 ? 0 : (green > 255 ? 255 : green);
                blue = blue < 0 ? 0 : (blue > 255 ? 255 : blue);

                //卷积计算后赋值
                result.setRGB(i, j, new Color((int) red, (int) green, (int) blue, new Color(rgb11).getAlpha()).getRGB());
            }
        }
        return result;
    }
}
