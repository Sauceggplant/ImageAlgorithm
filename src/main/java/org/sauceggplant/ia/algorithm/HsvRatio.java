package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.enums.HsvEnum;
import org.sauceggplant.ia.ui.IaPanel;
import org.sauceggplant.ia.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * 色调，亮度，饱和度调整
 */
public class HsvRatio implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(HsvRatio.class);

    /**
     * 界面
     */
    private IaPanel iaPanel;

    /**
     * 色调滑块
     */
    private JSlider hSlider;

    /**
     * 饱和度滑块
     */
    private JSlider sSlider;

    /**
     * 亮度滑块
     */
    private JSlider vSlider;

    private static final String OPEN = "ia.ui.io.file.open";
    private static final String TITLE = "ia.ui.hsv.ratio.title";
    private static final String BTN_OK = "ia.ui.btn.ok";
    private static final String H = "ia.ui.hsv.ratio.h";
    private static final String S = "ia.ui.hsv.ratio.s";
    private static final String V = "ia.ui.hsv.ratio.v";

    @Override
    public void run(IaPanel iaPanel) {
        logger.info("HsvRatio：颜色占比");
        BufferedImage image = iaPanel.getContent().getImage();
        if (null == image) {
            logger.error(PropertiesUtil.getProperty(OPEN));
            return;
        }

        this.iaPanel = iaPanel;
        //当前图像颜色占比
        float initH = hsvRatio(HsvEnum.H);
        float initS = hsvRatio(HsvEnum.S);
        float initV = hsvRatio(HsvEnum.V);
        logger.info("{}：H:{} S:{} V:{}", PropertiesUtil.getProperty(TITLE), initH, initS, initV);
        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle(PropertiesUtil.getProperty(TITLE));
        dialog.setPreferredSize(new Dimension(600, 320));
        dialog.setSize(new Dimension(600, 320));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(sliderPanel((int)initH, (int)initS, (int)initV), BorderLayout.CENTER);
        JButton ok = new JButton(PropertiesUtil.getProperty(BTN_OK));
        ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int h = hSlider.getValue();
                int s = sSlider.getValue();
                int v = vSlider.getValue();
                logger.info("Slider: H:{} S:{} V:{}", h, s, v);
                hsvRatioChange(h - initH, s - initS, v - initV);
                dialog.setVisible(false);
            }
        });
        dialog.getContentPane().add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * 颜色调整
     *
     * @param h 色调
     * @param s 饱和度
     * @param v 亮度
     */
    public void hsvRatioChange(float h, float s, float v) {
        logger.info("Change: H:{} S:{} V:{}", h, s, v);
        BufferedImage image = iaPanel.getContent().getImage();
        int width = image.getData().getWidth();
        int height = image.getData().getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                Color c = new Color(rgb);
                float[] hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                float changeH = hsv[0] * 360.0f + h;
                float changeS = hsv[1] + s / 100.0f;
                float changeV = hsv[2] + v / 100.0f;
                changeH = changeH > 360.0f ? (changeH - 360.0f) : (changeH < 0.0f ? (changeH + 360.0f) : changeH);
                changeS = changeS > 1.0f ? 1.0f : (changeS < 0.0f ? 0.0f : changeS);
                changeV = changeV > 1.0f ? 1.0f : (changeV < 0.0f ? 0.0f : changeV);
                result.setRGB(i, j, Color.HSBtoRGB(changeH / 360.0f, changeS, changeV));
            }
        }
        iaPanel.getOutput().setImage(result);
    }

    /**
     * 滑块panel
     *
     * @param h 色调
     * @param s 饱和度
     * @param v 亮度
     * @return 界面
     */
    JPanel sliderPanel(int h, int s, int v) {
        hSlider = new JSlider(JSlider.HORIZONTAL, h - 180, h + 180, h);
        hSlider.setToolTipText(PropertiesUtil.getProperty(H));
        hSlider.setMajorTickSpacing(30);
        hSlider.setMinorTickSpacing(1);
        hSlider.setPaintLabels(true);
        hSlider.setPaintTicks(true);
        hSlider.setPreferredSize(new Dimension(480, 80));

        sSlider = new JSlider(JSlider.HORIZONTAL, s - 100, s + 100, s);
        sSlider.setToolTipText(PropertiesUtil.getProperty(S));
        sSlider.setMajorTickSpacing(20);
        sSlider.setMinorTickSpacing(1);
        sSlider.setPaintLabels(true);
        sSlider.setPaintTicks(true);
        sSlider.setPreferredSize(new Dimension(480, 80));

        vSlider = new JSlider(JSlider.HORIZONTAL, v - 100, v + 100, v);
        vSlider.setToolTipText(PropertiesUtil.getProperty(V));
        vSlider.setMajorTickSpacing(20);
        vSlider.setMinorTickSpacing(1);
        vSlider.setPaintLabels(true);
        vSlider.setPaintTicks(true);
        vSlider.setPreferredSize(new Dimension(480, 80));

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));
        JPanel hPanel = new JPanel();
        hPanel.setPreferredSize(new Dimension(600, 80));
        hPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        hPanel.add(new JLabel(PropertiesUtil.getProperty(H)));
        hPanel.add(hSlider);
        JPanel sPanel = new JPanel();
        sPanel.setPreferredSize(new Dimension(600, 80));
        sPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        sPanel.add(new JLabel(PropertiesUtil.getProperty(S)));
        sPanel.add(sSlider);
        JPanel vPanel = new JPanel();
        vPanel.setPreferredSize(new Dimension(600, 80));
        vPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        vPanel.add(new JLabel(PropertiesUtil.getProperty(V)));
        vPanel.add(vSlider);
        panel.add(hPanel);
        panel.add(sPanel);
        panel.add(vPanel);
        return panel;
    }

    /**
     * 颜色分量
     *
     * @param hsvEnum 颜色（HSV）
     * @return 平均颜色
     */
    public float hsvRatio(HsvEnum hsvEnum) {
        BufferedImage image = iaPanel.getContent().getImage();
        int width = image.getData().getWidth();
        int height = image.getData().getHeight();
        double sum = 0;
        if (HsvEnum.H.equals(hsvEnum)) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = image.getRGB(i, j);
                    Color c = new Color(rgb);
                    float[] hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                    sum += hsv[0] * 360.0d;
                }
            }
        } else if (HsvEnum.S.equals(hsvEnum)) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = image.getRGB(i, j);
                    Color c = new Color(rgb);
                    float[] hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                    sum += hsv[1] * 100.0d;
                }
            }
        } else if (HsvEnum.V.equals(hsvEnum)) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = image.getRGB(i, j);
                    Color c = new Color(rgb);
                    float[] hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                    sum += hsv[2] * 100.0d;
                }
            }
        }
        return (float)sum / width / height;
    }
}
