package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
import org.sauceggplant.ia.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * 色阶RGB
 */
public class ColorScaleRgb implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ColorScaleRgb.class);

    private static final String OPEN = "ia.ui.io.file.open";
    private static final String BTN_OK = "ia.ui.btn.ok";

    @Override
    public void run(IaPanel iaPanel) {
        logger.info("ColorScaleRgb:色阶RGB");
        //打开的图像
        BufferedImage image = iaPanel.getContent().getImage();
        if (null == image) {
            logger.error(PropertiesUtil.getProperty(OPEN));
            return;
        }

        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle("色阶RGB");
        dialog.setPreferredSize(new Dimension(200, 100));
        dialog.setSize(new Dimension(200, 100));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());
        String[] labels = new String[]{"均值", "红色", "绿色", "蓝色"};
        JComboBox comboBox = new JComboBox(labels);
        comboBox.setSelectedIndex(0);
        JPanel panel = new JPanel();
        panel.add(comboBox);
        dialog.getContentPane().add(panel, BorderLayout.CENTER);
        JButton ok = new JButton(PropertiesUtil.getProperty(BTN_OK));
        ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = comboBox.getSelectedIndex();
                iaPanel.getOutput().setImage(scale(iaPanel, index));
                dialog.setVisible(false);
            }
        });
        dialog.getContentPane().add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public BufferedImage scale(IaPanel iaPanel, int index) {
        //界面宽高
        int width = iaPanel.getOutput().getWidth();
        int height = iaPanel.getOutput().getHeight();

        //输出图像
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();

        //水平边距
        int marginH = 50;
        //垂直边距
        int marginV = 40;

        //清空背景，设置背景白色
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);

        //绘制坐标线
        graphics.setColor(Color.BLUE);
        graphics.drawLine(marginH, height - marginV, width - marginH, height - marginV);
        graphics.drawLine(marginH, marginV, marginH, height - marginV);

        //计算直方图
        Map<Integer, Integer> histogram = calcHistogram(iaPanel.getContent().getImage(), index);

        //每个直方图区域的宽度
        double eachWidth = (width - 4 * marginH) / 256;

        //颜色统计直方图的最大值
        int maxValue = maxHistogram(histogram);

        //每个直方图区域的高度
        double eachHeight;
        //颜色统计直方图的最大值对应的块数
        int maxHeight;

        if (maxValue > (height - 4 * marginV)) {
            eachHeight = maxValue / (height - 4 * marginV);
            maxHeight = (int) (maxValue / eachHeight);
        } else {
            eachHeight = (height - 4 * marginV) / maxValue;
            maxHeight = (int) (eachHeight * maxValue);
        }

        //绘制纵坐标数值
        for (int i = 1; i < maxHeight; i++) {
            if (i % 50 == 0) {
                graphics.setColor(Color.BLUE);
                graphics.drawString("" + (int) (i * eachHeight), 15, height - marginV - maxHeight + (maxHeight - i));
            }
        }

        //横坐标按照灰度的颜色从0--255循环，对每个颜色绘制直方图
        for (int i = 0; i < 256; i++) {
            int x1 = (int) (eachWidth * i);
            int y1;
            if (maxValue > (height - 4 * marginV)) {
                y1 = (int) (histogram.get(i) / eachHeight);
            } else {
                y1 = (int) (eachHeight * histogram.get(i));
            }

            //绘制直方图数值
            graphics.setColor(Color.GRAY);
            graphics.drawRect(x1 + marginH, height - marginV - y1, (int) eachWidth, y1);
            //graphics.fillRect(x1 + marginH, height - marginV - y1, (int) eachWidth, y1);

            //绘制横坐标数值
            if (i % 15 == 0) {
                graphics.setColor(Color.BLUE);
                graphics.drawString("" + i, x1 + marginH, height - 25);
            }
        }
        graphics.dispose();
        return bufferedImage;
    }

    /**
     * 求直方图最大值
     *
     * @param histogram 直方图数据
     * @return 最大值
     */
    public Integer maxHistogram(Map<Integer, Integer> histogram) {
        int max = 0;
        for (int i = 0; i < 256; i++) {
            if (histogram.get(i) > max) {
                max = histogram.get(i);
            }
        }
        return max;
    }

    /**
     * 计算直方图数据
     *
     * @param image 图像
     */
    public Map<Integer, Integer> calcHistogram(BufferedImage image, int index) {
        Map<Integer, Integer> histogram = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            histogram.put(i, 0);
        }
        //图像宽度，高度
        int width = image.getData().getWidth();
        int height = image.getData().getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //获取像素点的颜色(红，绿，蓝),RGB色彩空间
                int rgb = image.getRGB(i, j);
                Color color = new Color(rgb);
                int c;
                if (index == 0) {
                    //均值,对红，绿，蓝求和再求平均值，将平均值赋值回原来的红，绿，蓝颜色分量，使红==绿==蓝
                    c = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                } else if (index == 1) {
                    //红
                    c = color.getRed();
                } else if (index == 2) {
                    //绿
                    c = color.getGreen();
                } else {
                    //蓝
                    c = color.getBlue();
                }
                int value = histogram.get(c);
                histogram.put(c, ++value);
            }
        }
        return histogram;
    }
}
