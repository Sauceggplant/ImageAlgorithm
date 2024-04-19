package org.sauceggplant.ia.ui;

import org.sauceggplant.ia.util.MenuUtil;
import org.sauceggplant.ia.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;

/**
 * 右键菜单
 */
public class IaPopupMenu extends JPopupMenu {

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(IaPopupMenu.class);

    /**
     * 界面panel
     */
    final IaPanel iaPanel;

    /**
     * 菜单实用类
     */
    private MenuUtil menuUtil;

    /**
     * 右键菜单初始化
     */
    private static final String INIT_POPMENU = "ia.ui.popmenu.init";

    /**
     * 右键菜单初始化完毕
     */
    private static final String INIT_POPMENU_DONE = "ia.ui.popmenu.init.done";

    public IaPopupMenu(IaPanel iaPanel) {
        this.iaPanel = iaPanel;
        init();
    }

    /**
     * 右键菜单初始化
     */
    private void init() {
        logger.info(PropertiesUtil.getProperty(INIT_POPMENU));

        menuUtil = new MenuUtil(iaPanel);
        java.util.List<IaMenu> iaMenuList = menuUtil.getIaMenuList();
        logger.info("菜单项列表:{}", iaMenuList);
        for (IaMenu iaMenu : iaMenuList) {
            List<JMenuItem> jMenuItemList = menuUtil.buildMenuItemList(iaMenu);
            for (JMenuItem jMenuItem : jMenuItemList) {
                logger.info("添加菜单项:{}",jMenuItem.getName());
                this.add(jMenuItem);
            }
        }
        logger.info(PropertiesUtil.getProperty(INIT_POPMENU_DONE));
    }
}
