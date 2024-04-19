package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 退出
 */
public class Quit {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Quit.class);

    /**
     * 退出
     * @param iaPanel 面板
     */
    public void run(IaPanel iaPanel) {
        logger.info("菜单：退出");
        System.exit(0);
    }
}
