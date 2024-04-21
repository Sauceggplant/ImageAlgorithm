package org.sauceggplant.ia.util;

import javax.swing.*;
import java.awt.*;

/**
 * 图标实用类
 */
public class IconUtil {

    /**
     * 获取图标
     *
     * @param source      源
     * @param description 描述
     * @param size        大小
     * @return 图标
     */
    public static ImageIcon getIcon(String source, String description, int size) {
        ImageIcon icon = new ImageIcon(IconUtil.class.getClassLoader().getResource("image/"+source), description);
        Image image = icon.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT);
        return new ImageIcon(image);
    }
}
