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
 * 透明度
 */
public class Alpha implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Alpha.class);

    private static final String OPEN = "ia.ui.io.file.open";

    private static final String TITLE = "ia.ui.alpha.title";

    private static final String BTN_OK = "ia.ui.btn.ok";

    @Override
    public void run(IaPanel iaPanel) {
        logger.info("Alpha:透明度");
        BufferedImage image = iaPanel.getContent().getImage();
        if (null == image) {
            logger.error(PropertiesUtil.getProperty(OPEN));
            return;
        }

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 255, 255);
        slider.setToolTipText(PropertiesUtil.getProperty(TITLE));
        slider.setMajorTickSpacing(32);
        slider.setMinorTickSpacing(1);
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
                    iaPanel.getOutput().setImage(alpha(bufferedImage, value));
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
     * 透明度
     *
     * @param image 图像
     * @param value 透明度
     * @return 图像
     */
    private BufferedImage alpha(BufferedImage image, int value) {
        //图像宽度，高度
        int width = image.getData().getWidth();
        int height = image.getData().getHeight();
        //构建图像，返回结果
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //获取像素点的颜色(红，绿，蓝),RGB色彩空间
                int rgb = image.getRGB(i, j);
                Color c = new Color(rgb);
                result.setRGB(i, j, new Color(c.getRed(), c.getGreen(), c.getBlue(), value).getRGB());
            }
        }
        return result;
    }
}
