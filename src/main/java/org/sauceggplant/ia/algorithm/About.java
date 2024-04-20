package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
import org.sauceggplant.ia.util.IconUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * 关于
 */
public class About implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(About.class);

    /**
     * 关于
     *
     * @param iaPanel 面板
     */
    @Override
    public void run(IaPanel iaPanel) {
        logger.info("菜单：关于");
        JOptionPane.showMessageDialog(iaPanel,
                "图像算法\n" +
                        "版本:\t1.1-SNAPSHOT\n" +
                        "版权:\tzhaozx19881105@163.com",
                "关于",
                JOptionPane.INFORMATION_MESSAGE,
                IconUtil.getIcon("icon.png", "about", 64));
    }
}
