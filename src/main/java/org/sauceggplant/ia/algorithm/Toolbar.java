package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 工具栏
 */
public class Toolbar implements Algorithm {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Toolbar.class);

    @Override
    public void run(IaPanel iaPanel) {
        logger.info("Toolbar:工具栏");
        if (iaPanel.getIaWindow().getToolBar().isVisible()) {
            iaPanel.getIaWindow().getToolBar().setVisible(false);
            logger.info("隐藏工具栏");
        } else {
            iaPanel.getIaWindow().getToolBar().setVisible(true);
            logger.info("显示工具栏");
        }
    }
}
