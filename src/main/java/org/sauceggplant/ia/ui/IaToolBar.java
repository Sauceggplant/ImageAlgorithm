package org.sauceggplant.ia.ui;

import org.sauceggplant.ia.util.MenuUtil;
import org.sauceggplant.ia.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 工具栏
 */
public class IaToolBar extends JToolBar {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(IaToolBar.class);

    private static final String TOOL_BAR_INIT = "ia.ui.toolbar.init";

    private static final String TOOL_BAR_INIT_DONE = "ia.ui.toolbar.init.done";

    /**
     * 面板
     */
    private IaPanel iaPanel;

    /**
     * 菜单实用类
     */
    private MenuUtil menuUtil;

    public IaToolBar(IaPanel iaPanel) {
        this.iaPanel = iaPanel;
        init();
    }

    private void init() {
        logger.info(PropertiesUtil.getProperty(TOOL_BAR_INIT));
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        menuUtil = new MenuUtil(iaPanel);
        java.util.List<IaMenu> iaMenuList = menuUtil.getIaMenuList();
        //logger.info("菜单项列表:{}", iaMenuList);
        for (IaMenu iaMenu : iaMenuList) {
            List<JButton> jButtonList = menuUtil.buildToolBarItemList(iaMenu);
            for (JButton tool : jButtonList) {
                logger.info("添加菜单项:{}", tool.getName());
                this.add(tool);
                //this.addSeparator();
            }
        }
        logger.info(PropertiesUtil.getProperty(TOOL_BAR_INIT_DONE));
    }
}
