package org.sauceggplant.ia.log;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 控制台日志
 */
public class ConsoleLog {

    /**
     * 日志
     */
    private JTextArea logArea;

    /**
     * 显示行数
     */
    private int entries = 0;

    /**
     * 显示日志的最大行数
     */
    private static final int MAX_ENTRIES = 4096;

    public ConsoleLog(JTextArea logArea) {
        this.logArea = logArea;
        replaceSystemOutputStream();
    }

    /**
     * 日志输出流切logArea
     */
    private void replaceSystemOutputStream() {
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                updateLogArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateLogArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };
        System.setOut(new PrintStream(outputStream, true));
        System.setErr(new PrintStream(outputStream, true));
    }

    /**
     * 日志输出
     *
     * @param text 日志信息
     */
    private void updateLogArea(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = logArea.getDocument();
                    if (entries >= MAX_ENTRIES) {
                        int endOfs = logArea.getLineEndOffset(entries - MAX_ENTRIES);
                        document.remove(0, endOfs);
                        entries = entries - 1;
                    }
                    entries = entries + 1;
                    logArea.append(text);
                    logArea.setCaretPosition(document.getLength());
                } catch (BadLocationException e) {
                }
            }
        });
    }
}
