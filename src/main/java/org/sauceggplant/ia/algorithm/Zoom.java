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
 * 缩放
 */
public class Zoom implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Zoom.class);

    private static final String OPEN = "ia.ui.io.file.open";

    /**
     * 缩放
     *
     * @param iaPanel 面板
     */
    @Override
    public void run(IaPanel iaPanel) {
        logger.info("Zoom:缩放");
        if (null == iaPanel.getContent().getImage()) {
            logger.error(PropertiesUtil.getProperty(OPEN));
            return;
        }

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
        slider.setToolTipText("图像缩放比例");
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(2);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle("缩放百分比(%)");
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
                BufferedImage bufferedImage = iaPanel.getContent().getImage();
                int value = slider.getValue();
                double rate = value / 100.0D;
                logger.info("图片缩放比例:{}", rate);
                iaPanel.getOutput().setImage(zoom(bufferedImage, rate));
                dialog.setVisible(false);
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
