package org.sauceggplant.ia.algorithm;

import org.sauceggplant.ia.ui.IaPanel;
import org.sauceggplant.ia.util.IconUtil;
import org.sauceggplant.ia.util.PropertiesUtil;
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

    private static final String MESSAGE = "ia.ui.about.message";

    private static final String TITLE = "ia.ui.about.title";

    private static final String ICON = "ia.ui.about.icon";

    private static final String ICON_SIZE = "ia.ui.about.icon.size";

    /**
     * 关于
     *
     * @param iaPanel 面板
     */
    @Override
    public void run(IaPanel iaPanel) {
        logger.info("About:关于");
        JOptionPane.showMessageDialog(iaPanel,
                PropertiesUtil.getProperty(MESSAGE),
                PropertiesUtil.getProperty(TITLE),
                JOptionPane.INFORMATION_MESSAGE,
                IconUtil.getIcon(PropertiesUtil.getProperty(ICON),
                        PropertiesUtil.getProperty(TITLE),
                        Integer.parseInt(PropertiesUtil.getProperty(ICON_SIZE))));
    }
}
