package org.sauceggplant.ia.ui;

import org.sauceggplant.ia.util.IconUtil;
import org.sauceggplant.ia.util.PropertiesUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 日志右键菜单
 */
public class LogPopMenu extends JPopupMenu {

    private static final String NAME = "ia.ui.menu.log.name";
    private static final String ICON = "ia.ui.menu.log.icon";
    private static final String ICON_SIZE = "ia.ui.menu.icon.size";
    private static final String FONT_SIZE = "ia.ui.menu.font.size";
    private static final String FONT = "ia.ui.menu.font";

    private JTextArea textArea;

    public LogPopMenu(JTextArea textArea) {
        this.textArea = textArea;
        init();
    }

    /**
     * 右键菜单初始化
     */
    private void init() {
        JMenuItem clear = new JMenuItem();
        clear.setName(PropertiesUtil.getProperty(NAME));
        clear.setText(PropertiesUtil.getProperty(NAME));
        clear.setIcon(IconUtil.getIcon(PropertiesUtil.getProperty(ICON),
                PropertiesUtil.getProperty(NAME),
                Integer.parseInt(PropertiesUtil.getProperty(ICON_SIZE))));
        clear.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });
        int fontSize = Integer.parseInt(PropertiesUtil.getProperty(FONT_SIZE));
        clear.setFont(new Font(PropertiesUtil.getProperty(FONT), Font.PLAIN, fontSize));
        this.add(clear);
    }
}
