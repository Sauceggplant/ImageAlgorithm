package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
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

    /**
     * 图像信息
     *
     * @param iaPanel 面板
     */
    @Override
    public void run(IaPanel iaPanel) {
        logger.info("菜单：信息");
        JDialog dialog = new JDialog(iaPanel.getIaWindow());
        dialog.setTitle("图像信息");
        dialog.setPreferredSize(new Dimension(400, 300));
        dialog.setSize(new Dimension(400, 300));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(iaPanel.getIaWindow());
        dialog.getContentPane().setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.add(new JLabel("宽度:"));
        JLabel width = new JLabel("" + iaPanel.getContent().getImage().getWidth());
        jPanel.add(width);
        jPanel.add(new JLabel("高度:"));
        JLabel height = new JLabel("" + iaPanel.getContent().getImage().getHeight());
        jPanel.add(height);
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
