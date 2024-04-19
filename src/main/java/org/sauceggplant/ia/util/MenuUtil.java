package org.sauceggplant.ia.util;

import com.alibaba.fastjson2.JSONArray;
import org.sauceggplant.ia.ui.IaMenu;
import org.sauceggplant.ia.ui.IaMenuItem;
import org.sauceggplant.ia.ui.IaMenuItemActionListener;
import org.sauceggplant.ia.ui.IaPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * json实用类
 */
public class MenuUtil {

    /**
     * 页面
     */
    private IaPanel iaPanel;

    /**
     * 菜单项监听
     */
    private IaMenuItemActionListener iaMenuItemActionListener;

    /**
     * 字体
     */
    private static final String FONT = "ia.ui.menu.font";

    /**
     * 字体大小
     */
    private static final String FONT_SIZE = "ia.ui.menu.font.size";

    /**
     * 图标大小
     */
    private static final String ICON_SIZE = "ia.ui.menu.icon.size";

    public MenuUtil(IaPanel iaPanel) {
        this.iaPanel = iaPanel;
        iaMenuItemActionListener = new IaMenuItemActionListener(iaPanel);
    }

    /**
     * 读取json文件，转换菜单
     *
     * @return 菜单
     */
    public List<IaMenu> getIaMenuList() {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(MenuUtil.class.getClassLoader().getResource("menu.json").toURI())));
        } catch (IOException e) {
        } catch (URISyntaxException e) {
        }
        return JSONArray.parseArray(content, IaMenu.class);
    }

    /**
     * 菜单
     *
     * @param iaMenu 菜单
     * @return 菜单
     */
    public JMenu buildMenu(IaMenu iaMenu) {
        return buildMenu(iaMenu.getName(),
                iaMenu.getText(),
                iaMenu.getIcon(),
                iaMenu.getMnemonic());
    }

    /**
     * 菜单项列表
     *
     * @param iaMenu 菜单项列表
     * @return 菜单项
     */
    public List<JMenuItem> buildMenuItemList(IaMenu iaMenu) {
        List<JMenuItem> jMenuItemList = new ArrayList<>();
        for (IaMenuItem jMenuItem : iaMenu.getMenuItemList()) {
            jMenuItemList.add(buildMenuItem(
                    jMenuItem.getName(),
                    jMenuItem.getText(),
                    jMenuItem.getIcon(),
                    jMenuItem.getAccelerator()));
        }
        return jMenuItemList;
    }

    /**
     * 构建菜单
     *
     * @param text     文字
     * @param icon     图标
     * @param mnemonic 快捷键
     * @return 菜单
     */
    public JMenu buildMenu(String name, String text, String icon, String mnemonic) {
        JMenu jMenu = new JMenu();
        jMenu.setName(name);
        jMenu.setText(text);
        jMenu.setIcon(IconUtil.getIcon(icon, text,
                Integer.parseInt(PropertiesUtil.getProperty(ICON_SIZE))));
        jMenu.setMnemonic(mnemonic.charAt(0));
        int fontSize = Integer.parseInt(PropertiesUtil.getProperty(FONT_SIZE));
        jMenu.setFont(new Font(PropertiesUtil.getProperty(FONT), Font.PLAIN, fontSize));
        return jMenu;
    }

    /**
     * 构建菜单项
     *
     * @param name        名称
     * @param text        文字
     * @param icon        图标
     * @param accelerator 快捷键
     * @return 菜单项
     */
    public JMenuItem buildMenuItem(String name, String text, String icon, String accelerator) {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setName(name);
        menuItem.setText(text);
        menuItem.setIcon(IconUtil.getIcon(icon, text,
                Integer.parseInt(PropertiesUtil.getProperty(ICON_SIZE))));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(Integer.parseInt(accelerator),
                InputEvent.ALT_DOWN_MASK));
        menuItem.addActionListener(iaMenuItemActionListener);
        int fontSize = Integer.parseInt(PropertiesUtil.getProperty(FONT_SIZE));
        menuItem.setFont(new Font(PropertiesUtil.getProperty(FONT), Font.PLAIN, fontSize));
        return menuItem;
    }
}
