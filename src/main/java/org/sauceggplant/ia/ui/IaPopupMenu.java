package org.sauceggplant.ia.ui;

import org.sauceggplant.ia.util.MenuUtil;

import javax.swing.*;
import java.util.List;

/**
 * 右键菜单
 */
public class IaPopupMenu extends JPopupMenu {

    /**
     * 界面panel
     */
    final IaPanel iaPanel;

    /**
     * 菜单实用类
     */
    private MenuUtil menuUtil;

    public IaPopupMenu(IaPanel iaPanel) {
        this.iaPanel = iaPanel;
        init();
    }

    /**
     * 右键菜单初始化
     */
    private void init() {
        menuUtil = new MenuUtil(iaPanel);
        java.util.List<IaMenu> iaMenuList = menuUtil.getIaMenuList();
        for (IaMenu iaMenu : iaMenuList) {
            List<JMenuItem> jMenuItemList = menuUtil.buildMenuItemList(iaMenu);
            for (JMenuItem jMenuItem : jMenuItemList) {
                this.add(jMenuItem);
            }
        }
    }
}
