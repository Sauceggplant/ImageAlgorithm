package org.sauceggplant.ia.util;

import com.alibaba.fastjson2.JSONArray;
import org.sauceggplant.ia.pojo.ConvolutionCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 卷积核实用类
 */
public class ConvolutionCoreUtil {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ConvolutionCoreUtil.class);

    /**
     * 读取json文件，转换菜单
     *
     * @return 菜单
     */
    public static List<ConvolutionCore> getConvolutionCoreList() {
        String content = null;
        try {
            InputStream is = ConvolutionCoreUtil.class.getClassLoader().getResourceAsStream("convolutionCore.json");
            content = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .collect(Collectors.joining(
                            System.lineSeparator()
                    ));
        } catch (Exception e) {
            logger.error("getConvolutionCoreList.error", e);
        }
        return JSONArray.parseArray(content, ConvolutionCore.class);
    }
}
