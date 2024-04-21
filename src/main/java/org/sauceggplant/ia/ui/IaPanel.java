package org.sauceggplant.ia.ui;

import org.sauceggplant.ia.util.IconUtil;
import org.sauceggplant.ia.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 界面
 */
public class IaPanel extends JTabbedPane {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(IaPanel.class);

    /**
     * 界面初始化
     */
    private static final String INIT_PANEL = "ia.ui.panel.init";

    /**
     * 界面初始化完成
     */
    private static final String INIT_PANEL_DONE = "ia.ui.panel.init.done";

    /**
     * 图像icon
     */
    private static final String IMAGE_ICON = "ia.ui.panel.image.icon";

    /**
     * 输出icon
     */
    private static final String OUTPUT_ICON = "ia.ui.panel.output.icon";

    /**
     * 日志icon
     */
    private static final String LOG_ICON = "ia.ui.panel.log.icon";

    /**
     * icon size
     */
    private static final String ICON_SIZE = "ia.ui.panel.icon.size";

    /**
     * 文字
     */
    private static final String FONT = "ia.ui.panel.font";

    /**
     * 文字大小
     */
    private static final String FONT_SIZE = "ia.ui.panel.font.size";

    /**
     * parent
     */
    private IaWindow iaWindow;

    /**
     * 日志
     */
    private JTextArea logArea;

    /**
     * 图像
     */
    private IaImagePanel content;

    /**
     * 图像
     */
    private JScrollPane contentPane;

    /**
     * 输出
     */
    private IaImagePanel output;

    /**
     * 输出
     */
    private JScrollPane outputPane;

    /**
     * 右键菜单
     */
    private IaPopupMenu iaPopupMenu;

    /**
     * 当前处理的图像文件绝对路径
     */
    private String path;

    public IaPanel(int tabPlacement, int tabLayoutPolicy, JTextArea logArea, IaWindow iaWindow) {
        super(tabPlacement, tabLayoutPolicy);
        this.logArea = logArea;
        this.iaWindow = iaWindow;
        init();
    }

    protected void init() {
        logger.info(PropertiesUtil.getProperty(INIT_PANEL));
        this.setFont(new Font(PropertiesUtil.getProperty(FONT), Font.PLAIN,
                Integer.parseInt(PropertiesUtil.getProperty(FONT_SIZE))));

        JPanel imagePanel = new JPanel();
        imagePanel.setFont(new Font(PropertiesUtil.getProperty(FONT), Font.PLAIN,
                Integer.parseInt(PropertiesUtil.getProperty(FONT_SIZE))));
        imagePanel.setLayout(new BorderLayout());

        iaPopupMenu = new IaPopupMenu(this);

        content = new IaImagePanel(null);
        content.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    logger.info("右键菜单");
                    iaPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        contentPane = new JScrollPane(content);
        imagePanel.add(BorderLayout.CENTER, contentPane);
        this.addTab("图像", IconUtil.getIcon(PropertiesUtil.getProperty(IMAGE_ICON), "图像",
                Integer.parseInt(PropertiesUtil.getProperty(ICON_SIZE))), imagePanel);

        JPanel outputPanel = new JPanel();
        outputPanel.add(new JLabel("输出"));
        outputPanel.setFont(new Font(PropertiesUtil.getProperty(FONT), Font.PLAIN,
                Integer.parseInt(PropertiesUtil.getProperty(FONT_SIZE))));
        outputPanel.setLayout(new BorderLayout());
        output = new IaImagePanel(null);
        output.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    logger.info("右键菜单");
                    iaPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        outputPane = new JScrollPane(output);
        outputPanel.add(BorderLayout.CENTER, outputPane);
        this.addTab("输出", IconUtil.getIcon(PropertiesUtil.getProperty(OUTPUT_ICON), "输出",
                Integer.parseInt(PropertiesUtil.getProperty(ICON_SIZE))), outputPanel);

        JPanel logPanel = new JPanel();
        logPanel.setFont(new Font(PropertiesUtil.getProperty(FONT), Font.PLAIN,
                Integer.parseInt(PropertiesUtil.getProperty(FONT_SIZE))));
        logPanel.setLayout(new BorderLayout());
        logPanel.add(BorderLayout.CENTER, new JScrollPane(logArea));
        this.addTab("日志", IconUtil.getIcon(PropertiesUtil.getProperty(LOG_ICON), "日志",
                Integer.parseInt(PropertiesUtil.getProperty(ICON_SIZE))), logPanel);
        LogPopMenu logPopMenu = new LogPopMenu(logArea);
        logArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    logPopMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        this.setSelectedIndex(0);
        this.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                logger.info("当前选择的页签:" + getSelectedIndex());
            }
        });
        logger.info(PropertiesUtil.getProperty(INIT_PANEL_DONE));
    }

    public IaImagePanel getContent() {
        return content;
    }

    public IaImagePanel getOutput() {
        return output;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public IaWindow getIaWindow() {
        return iaWindow;
    }
}
