package org.sauceggplant.ia.ui;

import org.sauceggplant.ia.log.ConsoleLog;
import org.sauceggplant.ia.util.IconUtil;
import org.sauceggplant.ia.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * 窗口
 */
public class IaWindow extends JFrame {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(IaWindow.class);

    /**
     * 标题
     */
    private static final String TITLE = "ia.ui.window.title";

    /**
     * 窗口宽度
     */
    private static final String WIDTH = "ia.ui.window.width";

    /**
     * 窗口高度
     */
    private static final String HEIGHT = "ia.ui.window.height";

    /**
     * 图标
     */
    private static final String ICON = "ia.ui.window.icon";

    /**
     * 图标大小
     */
    private static final String ICON_SIZE = "ia.ui.window.icon.size";

    /**
     * 窗口初始化
     */
    private static final String INIT = "ia.ui.window.init";

    /**
     * 窗口初始化完成
     */
    private static final String INIT_DONE = "ia.ui.window.init.done";

    /**
     * 组件初始化
     */
    private static final String INIT_COMPONENT = "ia.ui.window.component.init";

    /**
     * 组件初始化完成
     */
    private static final String INIT_COMPONENT_DONE = "ia.ui.window.component.init.done";

    /**
     * 日志字体
     */
    private static final String LOG_FONT = "ia.ui.panel.log.font";

    /**
     * 日志字体大小
     */
    private static final String LOG_FONT_SIZE = "ia.ui.panel.log.font.size";

    /**
     * 日志字体颜色
     */
    private static final String LOG_FONT_COLOR = "ia.ui.panel.log.color";

    /**
     * 日志背景色
     */
    private static final String LOG_FONT_BG_COLOR = "ia.ui.panel.log.bgcolor";

    /**
     * 标题加载失败警告
     */
    private static final String TITLE_WARN = "[ia.properties]ia.ui.window.title load fail";

    /**
     * 窗口大小加载失败警告
     */
    private static final String SIZE_WARN = "[ia.properties]ia.ui.window.width;ia.ui.window.height load fail";

    /**
     * 窗口图标加载失败
     */
    private static final String ICON_WARN = "[ia.properties]ia.ui.window.icon load fail";

    /**
     * 默认标题
     */
    private static final String DEFAULT_TITLE = "Image Algorithm";

    /**
     * 默认宽度
     */
    private static final int DEFAULT_WIDTH = 800;

    /**
     * 默认高度
     */
    private static final int DEFAULT_HEIGHT = 600;

    /**
     * 输出日志区域
     */
    private JTextArea logArea = new JTextArea(10, 80);

    /**
     * 工具栏
     */
    private IaToolBar toolBar;

    /**
     * 默认构造
     */
    public IaWindow() {
        initConsoleLog();
        init();
    }

    /**
     * 初始化控制台日志
     */
    private void initConsoleLog() {
        logArea.setEditable(false);
        logArea.setForeground(new Color(Integer.parseInt(PropertiesUtil.getProperty(LOG_FONT_COLOR))));
        logArea.setBackground(new Color(Integer.parseInt(PropertiesUtil.getProperty(LOG_FONT_BG_COLOR))));
        logArea.setFont(new Font(PropertiesUtil.getProperty(LOG_FONT), Font.PLAIN,
                Integer.parseInt(PropertiesUtil.getProperty(LOG_FONT_SIZE))));
        new ConsoleLog(logArea);
    }

    /**
     * 初始化
     */
    protected void init() {
        logger.info(PropertiesUtil.getProperty(INIT));

        //初始化组件
        initComponent();

        //窗口标题
        String title = PropertiesUtil.getProperty(TITLE);
        if (null == title || title.isBlank()) {
            logger.warn(TITLE_WARN);
            this.setTitle(DEFAULT_TITLE);
        } else {
            this.setTitle(title);
        }

        //窗口尺寸
        int width, height;
        try {
            width = Integer.parseInt(PropertiesUtil.getProperty(WIDTH));
            height = Integer.parseInt(PropertiesUtil.getProperty(HEIGHT));
        } catch (NumberFormatException n) {
            logger.warn(SIZE_WARN, n);
            width = DEFAULT_WIDTH;
            height = DEFAULT_HEIGHT;
        }

        //图标
        try {
            this.setIconImage(IconUtil.getIcon(
                    PropertiesUtil.getProperty(ICON),
                    title,
                    Integer.parseInt(PropertiesUtil.getProperty(ICON_SIZE))
            ).getImage());
        } catch (Exception e) {
            logger.warn(ICON_WARN, e);
        }

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(width, height));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        logger.info(PropertiesUtil.getProperty(INIT_DONE));
    }

    /**
     * 初始化组件
     */
    protected void initComponent() {
        logger.info(PropertiesUtil.getProperty(INIT_COMPONENT));
        this.getContentPane().setLayout(new BorderLayout());
        IaPanel iaPanel = new IaPanel(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT, logArea, this);
        this.getContentPane().add(BorderLayout.CENTER, iaPanel);
        this.setJMenuBar(new IaMenuBar(iaPanel));
        toolBar = new IaToolBar(iaPanel);
        this.getContentPane().add(BorderLayout.PAGE_START, toolBar);
        logger.info(PropertiesUtil.getProperty(INIT_COMPONENT_DONE));
    }

    public IaToolBar getToolBar() {
        return toolBar;
    }
}