package org.sauceggplant.ia.ui;

import org.sauceggplant.ia.util.MenuUtil;
import org.sauceggplant.ia.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;

/**
 * 菜单栏
 */
public class IaMenuBar extends JMenuBar {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(IaMenuBar.class);

    /**
     * 页面
     */
    private IaPanel iaPanel;

    /**
     * 菜单实用类
     */
    private MenuUtil menuUtil;

    /**
     * 初始化
     */
    private static final String MENU_INIT = "ia.ui.menu.init";

    /**
     * 初始化完成
     */
    private static final String MENU_INIT_DONE = "ia.ui.menu.init.done";

    public IaMenuBar(IaPanel iaPanel) {
        this.iaPanel = iaPanel;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        //初始化菜单栏
        initMenu();
    }

    /**
     * 初始化菜单
     */
    private void initMenu() {
        logger.info(PropertiesUtil.getProperty(MENU_INIT));
        menuUtil = new MenuUtil(iaPanel);
        List<IaMenu> iaMenuList = menuUtil.getIaMenuList();
        for (IaMenu iaMenu : iaMenuList) {
            JMenu jMenu = menuUtil.buildMenu(iaMenu);
            List<JMenuItem> jMenuItemList = menuUtil.buildMenuItemList(iaMenu, false);
            for (JMenuItem jMenuItem : jMenuItemList) {
                jMenu.add(jMenuItem);
            }
            this.add(jMenu);
        }
        logger.info(PropertiesUtil.getProperty(MENU_INIT_DONE));
    }
}
