package org.sauceggplant.ia;

import org.sauceggplant.ia.ui.IaWindow;

import javax.swing.*;
import java.awt.*;

/**
 * 图像算法应用
 */
public class ImageAlgorithmApplication {

    /**
     * 主程序入口
     *
     * @param args 默认参数
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (UnsupportedLookAndFeelException
                         | IllegalAccessException
                         | InstantiationException
                         | ClassNotFoundException e) {
                }
                //新建一个窗口
                new IaWindow();
            }
        });
    }
}
