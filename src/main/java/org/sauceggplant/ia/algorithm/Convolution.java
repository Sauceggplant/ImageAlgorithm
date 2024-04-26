package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.pojo.ConvolutionCore;
import org.sauceggplant.ia.ui.IaPanel;
import org.sauceggplant.ia.util.ConvolutionCoreUtil;
import org.sauceggplant.ia.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.List;

/**
 * 卷积
 */
public class Convolution implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Convolution.class);

    /**
     * 卷积核
     */
    private static List<ConvolutionCore> convolutionCoreList = ConvolutionCoreUtil.getConvolutionCoreList();

    private static final String OPEN = "ia.ui.io.file.open";

    @Override
    public void run(IaPanel iaPanel) {
        logger.info("ConvolutionPlus:卷积");

        BufferedImage image = iaPanel.getContent().getImage();
        if (null == image) {
            logger.error(PropertiesUtil.getProperty(OPEN));
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel option = new JPanel();
        option.setLayout(new FlowLayout(FlowLayout.LEFT));
        option.add(new JLabel("算子"));

        final int[] width = {convolutionCoreList.get(0).getCore().length};
        final int[] height = {convolutionCoreList.get(0).getCore()[0].length};
        JComboBox<String> coreComboBox = new JComboBox(getCoreNames());
        coreComboBox.setSelectedIndex(0);
        JPanel content = new JPanel();
        content.setLayout(new GridLayout(width[0], height[0]));
        float[] core = getCores()[0];
        for (int i = 0; i < (width[0] * height[0]); i++) {
            JTextField field = new JTextField("" + core[i]);
            field.setEditable(false);
            content.add(field);
        }
        panel.add(content, BorderLayout.CENTER);
        coreComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int index = coreComboBox.getSelectedIndex();
                    boolean editable = false;
                    if (index == convolutionCoreList.size() - 1) {
                        editable = true;
                    }
                    int width = convolutionCoreList.get(index).getCore().length;
                    int height = convolutionCoreList.get(index).getCore()[0].length;
                    content.removeAll();
                    content.setLayout(new GridLayout(width, height));
                    float[] core = getCores()[index];
                    for (int i = 0; i < (width * height); i++) {
                        JTextField field = new JTextField("" + core[i]);
                        field.setEditable(editable);
                        content.add(field);
                    }
                    content.updateUI();
                }
            }
        });
        option.add(coreComboBox);

        panel.add(option, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);

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
                logger.info("卷积：选择的算子序号:{}--{}", index, getCoreNames()[index]);

                int width = convolutionCoreList.get(index).getCore().length;
                int height = convolutionCoreList.get(index).getCore()[0].length;

                //最后一个设定为自定义
                float[] core = new float[content.getComponents().length];
                int count = 0;
                if (index == convolutionCoreList.size() - 1) {
                    for (Component field : content.getComponents()) {
                        JTextField eachField = (JTextField) field;
                        core[count] = Float.parseFloat(eachField.getText());
                        ++count;
                    }
                } else {
                    core = getCores()[index];
                }
                Kernel kernel = new Kernel(width, height, core);
                // 使用ConvolveOp应用内核进行边缘检测
//                ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);
                ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
                BufferedImage edgeImage = convolveOp.filter(image, null);
                // 输出
                iaPanel.getOutput().setImage(edgeImage);
                dialog.setVisible(false);
            }
        });
        dialog.getContentPane().add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private String[] getCoreNames() {
        String[] names = new String[convolutionCoreList.size()];
        for (int i = 0; i < convolutionCoreList.size(); i++) {
            names[i] = convolutionCoreList.get(i).getName();
        }
        return names;
    }

    private float[][] getCores() {
        float[][] core = new float[convolutionCoreList.size()][];
        for (int i = 0; i < convolutionCoreList.size(); i++) {
            core[i] = getData(convolutionCoreList.get(i).getCore());
        }
        return core;
    }

    private float[] getData(double[][] data) {
        float[] result = new float[data.length * data[0].length];
        int count = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                result[count] = (float) data[i][j];
                count++;
            }
        }
        return result;
    }
}
