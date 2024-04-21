package org.sauceggplant.ia.pojo;

/**
 * 菜单项
 */
public class IaMenuItem {

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
    private String accelerator;

    /**
     * 是否添加右键菜单
     * false-右键菜单不显示该功能
     */
    private Boolean popMenu = true;

    /**
     * 是否添加工具栏
     * false-工具栏不显示该功能
     */
    private Boolean toolBar = false;

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

    public String getAccelerator() {
        return accelerator;
    }

    public void setAccelerator(String accelerator) {
        this.accelerator = accelerator;
    }

    public Boolean getPopMenu() {
        return popMenu;
    }

    public void setPopMenu(Boolean popMenu) {
        this.popMenu = popMenu;
    }

    public Boolean getToolBar() {
        return toolBar;
    }

    public void setToolBar(Boolean toolBar) {
        this.toolBar = toolBar;
    }
}
