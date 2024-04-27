package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
import org.sauceggplant.ia.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;

/**
 * 图像裁剪
 */
public class Cut implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Binarization.class);

    private static final String OPEN = "ia.ui.io.file.open";

    private static final String TITLE = "ia.ui.cut.title";

    private static final String BTN_OK = "ia.ui.btn.ok";

    @Override
    public void run(IaPanel iaPanel) {
        logger.info("Cut:裁剪");
        BufferedImage image = iaPanel.getContent().getImage();
        if (null == image) {
            logger.error(PropertiesUtil.getProperty(OPEN));
            return;
        }

        //图像宽度，高度
        int width = image.getData().getWidth();
        int height = image.getData().getHeight();

        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle(PropertiesUtil.getProperty(TITLE));
        dialog.setPreferredSize(new Dimension(500, 200));
        dialog.setSize(new Dimension(500, 200));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));

        JPanel positionPanel = new JPanel();
        positionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel xStart = new JPanel();
        xStart.setLayout(new FlowLayout(FlowLayout.LEFT));
        xStart.add(new JLabel("起始(x)"));
        JTextField x = new JTextField("0", 10);
        // 设置文本框只接受数字
        ((AbstractDocument) x.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
                fb.insertString(offs, str.replaceAll("[^0-9]", ""), a);
            }

            @Override
            public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
                fb.replace(offs, length, str.replaceAll("[^0-9]", ""), a);
            }
        });

        xStart.add(x);

        JPanel yStart = new JPanel();
        yStart.setLayout(new FlowLayout(FlowLayout.LEFT));
        yStart.add(new JLabel("起始(y)"));
        JTextField y = new JTextField("0", 10);
        // 设置文本框只接受数字
        ((AbstractDocument) y.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
                fb.insertString(offs, str.replaceAll("[^0-9]", ""), a);
            }

            @Override
            public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
                fb.replace(offs, length, str.replaceAll("[^0-9]", ""), a);
            }
        });

        yStart.add(y);

        positionPanel.add(xStart);
        positionPanel.add(yStart);

        JPanel sizePanel = new JPanel();
        JPanel wPanel = new JPanel();
        wPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        wPanel.add(new JLabel("宽度"));
        JTextField w = new JTextField("" + width, 10);
        // 设置文本框只接受数字
        ((AbstractDocument) w.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
                fb.insertString(offs, str.replaceAll("[^0-9]", ""), a);
            }

            @Override
            public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
                fb.replace(offs, length, str.replaceAll("[^0-9]", ""), a);
            }
        });
        wPanel.add(w);

        JPanel hPanel = new JPanel();
        hPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        hPanel.add(new JLabel("高度"));
        JTextField h = new JTextField("" + height, 10);
        // 设置文本框只接受数字
        ((AbstractDocument) h.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
                fb.insertString(offs, str.replaceAll("[^0-9]", ""), a);
            }

            @Override
            public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
                fb.replace(offs, length, str.replaceAll("[^0-9]", ""), a);
            }
        });
        hPanel.add(h);

        sizePanel.add(wPanel);
        sizePanel.add(hPanel);

        panel.add(positionPanel);
        panel.add(sizePanel);

        x.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    int startX = Integer.parseInt(x.getText());
                    int cWidth = Integer.parseInt(w.getText());
                    if (startX + cWidth > width) {
                        w.setText(""+(width - startX));
                    }
                } catch (Exception e1) {
                }
            }
        });
        y.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    int startY = Integer.parseInt(y.getText());
                    int cHeight = Integer.parseInt(h.getText());
                    if (startY + cHeight > height) {
                        h.setText(""+(height - startY));
                    }
                } catch (Exception e1) {
                }
            }
        });

        dialog.getContentPane().add(panel, BorderLayout.CENTER);
        JButton ok = new JButton(PropertiesUtil.getProperty(BTN_OK));
        ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int xStart = Integer.parseInt(x.getText());
                int yStart = Integer.parseInt(y.getText());
                int width = Integer.parseInt(w.getText());
                int height = Integer.parseInt(h.getText());
                logger.info("{}:width:{},height:{}", PropertiesUtil.getProperty(TITLE), width, height);
                BufferedImage bufferedImage = iaPanel.getContent().getImage();
                if (null != bufferedImage) {
                    iaPanel.getOutput().setImage(cut(bufferedImage, xStart, yStart, width, height));
                } else {
                    logger.error(PropertiesUtil.getProperty(OPEN));
                }
                dialog.setVisible(false);
            }
        });
        dialog.getContentPane().add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private BufferedImage cut(BufferedImage image, int xStart, int yStart, int width, int height) {
        //构建图像，返回结果
        BufferedImage result = new BufferedImage(width, height, image.getType());
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //获取像素点的颜色(红，绿，蓝),RGB色彩空间
                int rgb = image.getRGB(i + xStart, j + yStart);
                result.setRGB(i, j, rgb);
            }
        }
        return result;
    }
}
