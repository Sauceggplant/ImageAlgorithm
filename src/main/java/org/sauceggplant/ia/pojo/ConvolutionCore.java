package org.sauceggplant.ia.pojo;

/**
 * 卷积核
 */
public class ConvolutionCore {

    /**
     * 卷积核名称
     */
    private String name;

    /**
     * 卷积核的数据
     */
    private double[][] core;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[][] getCore() {
        return core;
    }

    public void setCore(double[][] core) {
        this.core = core;
    }
}
