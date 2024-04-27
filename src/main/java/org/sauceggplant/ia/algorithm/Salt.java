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
 * 图像加盐
 */
public class Salt implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Salt.class);

    /**
     * 加盐
     */
    private JSlider saltSlider;

    /**
     * 红色滑块
     */
    private JSlider redSlider;

    /**
     * 绿色滑块
     */
    private JSlider greenSlider;

    /**
     * 蓝色滑块
     */
    private JSlider blueSlider;

    private static final String OPEN = "ia.ui.io.file.open";
    private static final String RED = "ia.ui.color.ratio.red";
    private static final String GREEN = "ia.ui.color.ratio.green";
    private static final String BLUE = "ia.ui.color.ratio.blue";

    @Override
    public void run(IaPanel iaPanel) {
        logger.info("Salt:加盐");
        //打开的图像
        BufferedImage image = iaPanel.getContent().getImage();
        if (null == image) {
            logger.error(PropertiesUtil.getProperty(OPEN));
            return;
        }

        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle("加盐阈值");
        dialog.setPreferredSize(new Dimension(600, 420));
        dialog.setSize(new Dimension(600, 420));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());
        JPanel blank = new JPanel();
        blank.setPreferredSize(new Dimension(20, 20));
        dialog.getContentPane().add(sliderPanel(), BorderLayout.CENTER);
        dialog.getContentPane().add(blank, BorderLayout.EAST);
        JButton ok = new JButton("确定");
        ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int value = saltSlider.getValue();
                int red = redSlider.getValue();
                int green = greenSlider.getValue();
                int blue = blueSlider.getValue();
                logger.info("加盐阈值:{} red:{} green:{} blue:{}", value, red, green, blue);
                BufferedImage bufferedImage = iaPanel.getContent().getImage();
                iaPanel.getOutput().setImage(salt(bufferedImage, value, new Color(red, green, blue).getRGB()));
                dialog.setVisible(false);
            }
        });
        dialog.getContentPane().add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * 滑块panel
     *
     * @return 界面
     */
    JPanel sliderPanel() {
        saltSlider = new JSlider(JSlider.HORIZONTAL, 0, 50000, 1000);
        saltSlider.setToolTipText("加盐");
        saltSlider.setMajorTickSpacing(5000);
        saltSlider.setMinorTickSpacing(1);
        saltSlider.setPaintLabels(true);
        saltSlider.setPaintTicks(true);
        saltSlider.setPreferredSize(new Dimension(480, 80));

        redSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
        redSlider.setToolTipText(PropertiesUtil.getProperty(RED));
        redSlider.setMajorTickSpacing(15);
        redSlider.setMinorTickSpacing(1);
        redSlider.setPaintLabels(true);
        redSlider.setPaintTicks(true);
        redSlider.setPreferredSize(new Dimension(480, 80));

        greenSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
        greenSlider.setToolTipText(PropertiesUtil.getProperty(GREEN));
        greenSlider.setMajorTickSpacing(15);
        greenSlider.setMinorTickSpacing(1);
        greenSlider.setPaintLabels(true);
        greenSlider.setPaintTicks(true);
        greenSlider.setPreferredSize(new Dimension(480, 80));

        blueSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
        blueSlider.setToolTipText(PropertiesUtil.getProperty(BLUE));
        blueSlider.setMajorTickSpacing(15);
        blueSlider.setMinorTickSpacing(1);
        blueSlider.setPaintLabels(true);
        blueSlider.setPaintTicks(true);
        blueSlider.setPreferredSize(new Dimension(480, 80));

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));

        JPanel saltPanel = new JPanel();
        saltPanel.setPreferredSize(new Dimension(600, 80));
        saltPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        saltPanel.add(new JLabel("加盐"));
        saltPanel.add(saltSlider);
        JPanel redPanel = new JPanel();
        redPanel.setPreferredSize(new Dimension(600, 80));
        redPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        redPanel.add(new JLabel(PropertiesUtil.getProperty(RED)));
        redPanel.add(redSlider);
        JPanel greenPanel = new JPanel();
        greenPanel.setPreferredSize(new Dimension(600, 80));
        greenPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        greenPanel.add(new JLabel(PropertiesUtil.getProperty(GREEN)));
        greenPanel.add(greenSlider);
        JPanel bluePanel = new JPanel();
        bluePanel.setPreferredSize(new Dimension(600, 80));
        bluePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        bluePanel.add(new JLabel(PropertiesUtil.getProperty(BLUE)));
        bluePanel.add(blueSlider);

        panel.add(saltPanel);
        panel.add(redPanel);
        panel.add(greenPanel);
        panel.add(bluePanel);
        return panel;
    }

    /**
     * 加盐
     *
     * @param image 图像
     * @param value 阈值
     * @return 加盐后图像
     */
    private BufferedImage salt(BufferedImage image, int value, int color) {
        //图像宽度，高度
        int width = image.getData().getWidth();
        int height = image.getData().getHeight();

        //构建图像，返回结果
        BufferedImage result = new BufferedImage(width, height, image.getType());
        //赋值
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                result.setRGB(i, j, rgb);
            }
        }
        //加盐
        for (int i = 0; i < value; i++) {
            int rWidth = (int) (Math.random() * width);
            int rHeight = (int) (Math.random() * height);
            //logger.info("[{},{}]", rWidth, rHeight);
            result.setRGB(rWidth, rHeight, color);
        }
        return result;
    }
}