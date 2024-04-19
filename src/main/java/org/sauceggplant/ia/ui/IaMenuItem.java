package org.sauceggplant.ia.ui;

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
}
