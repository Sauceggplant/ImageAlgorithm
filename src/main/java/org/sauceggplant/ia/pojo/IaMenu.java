package org.sauceggplant.ia.pojo;

import java.util.List;

/**
 * 菜单
 */
public class IaMenu {

    /**
     * 名称
     */
    private String name;

    /**
     * 文本
     */
    private String text;

    /**
     * 图标
     */
    private String icon;

    /**
     * 快捷键
     */
    private String mnemonic;

    /**
     * 菜单项
     */
    private List<IaMenuItem> menuItemList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public List<IaMenuItem> getMenuItemList() {
        return menuItemList;
    }

    public void setMenuItemList(List<IaMenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }
}
