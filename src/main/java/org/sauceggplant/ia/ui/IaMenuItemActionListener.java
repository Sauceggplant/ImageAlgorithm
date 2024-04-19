package org.sauceggplant.ia.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 菜单项监听
 */
public class IaMenuItemActionListener implements ActionListener {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(IaMenuItemActionListener.class);

    /**
     * 面板
     */
    private IaPanel iaPanel;

    public IaMenuItemActionListener(IaPanel iaPanel) {
//        logger.info("IaMenuItemActionListener 构造");
        this.iaPanel = iaPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!(e.getSource() instanceof JMenuItem)) {
            return;
        }
        JMenuItem menuItem = ((JMenuItem) e.getSource());
        logger.info("菜单项:{}", menuItem.getName());
        try {
            Class<?> clazz = Class.forName("org.sauceggplant.ia.algorithm."
                    + Character.toUpperCase(menuItem.getName().charAt(0))
                    + menuItem.getName().substring(1));
            logger.info("反射调用类:{}#run", clazz.getName());
            Method method = clazz.getMethod("run", IaPanel.class);
            Object instance = clazz.newInstance();
            method.invoke(instance, iaPanel);
        } catch (ClassNotFoundException ex) {
        } catch (NoSuchMethodException ex) {
        } catch (InvocationTargetException ex) {
        } catch (IllegalAccessException ex) {
        } catch (InstantiationException ex) {
        }
    }
}
