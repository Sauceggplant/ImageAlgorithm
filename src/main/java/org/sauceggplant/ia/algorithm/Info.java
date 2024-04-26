package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
import org.sauceggplant.ia.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 图像信息
 */
public class Info implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Info.class);

    private static final String OPEN = "ia.ui.io.file.open";

    /**
     * 图像信息
     *
     * @param iaPanel 面板
     */
    @Override
    public void run(IaPanel iaPanel) {
        logger.info("Info:信息");
        if (null == iaPanel.getContent().getImage()) {
            logger.error(PropertiesUtil.getProperty(OPEN));
            return;
        }

        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle("图像信息");
        dialog.setPreferredSize(new Dimension(400, 300));
        dialog.setSize(new Dimension(400, 300));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());
        int width = iaPanel.getContent().getImage().getWidth();
        int height = iaPanel.getContent().getImage().getHeight();

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        JPanel size = new JPanel();
        size.add(new JLabel("宽度:"));
        JLabel widthLabel = new JLabel("" + width);
        size.add(widthLabel);
        size.add(new JLabel("高度:"));
        JLabel heightLabel = new JLabel("" + height);
        size.add(heightLabel);

        JPanel path = new JPanel();
        path.add(new JLabel("路径:"));
        path.add(new JLabel(iaPanel.getPath()));

        jPanel.add(size);
        jPanel.add(path);

        dialog.getContentPane().add(jPanel, BorderLayout.CENTER);

        JButton close = new JButton("关闭");
        close.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });
        dialog.getContentPane().add(close, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
