# ImageAlgorithm
Java Swing 图像处理 算法学习

## 原理

Java 通过 `java.awt.image.BufferedImage`类，对图像进行加工处理，

在RGB(计算机的三原色，R-red:红,G-green:绿,B-blue:蓝)色彩空间中，

可以理解为图像由三个矩阵(红色矩阵，绿色矩阵，蓝色矩阵)组成，

矩阵的行就是图像的宽， 矩阵的列就是图像的高，

图像的每个像素，就可以通过(x,y,rgb)表示，

即获取颜色`int rgb = BufferedImage.getRGB(int x, int y);`

赋值`BufferedImage.setRGB(int x, int y, int rgb);`

对于整型的rgb值，从`00000000-FFFFFFFF`,每2位16进制就代表一种含义，

分别是透明度，红，绿，蓝；

即
```java
int alpha = (rgb>>24)&0xFF;
```
```java
int red = (rgb>>16)&0xFF;
```
```java
int green = (rgb>>8)&0xFF;
```
```java
int blue = rgb&0xFF;
```

`alpha`(0-完全透明,255-不透明),

`red`(0-无红色,255-饱满红色),

`green`(0-无绿色,255-饱满绿色),

`blue`(0-无蓝色,255-饱满蓝色),

计算机常用显示的颜色：

黑色(red=0,green=0,blue=0)

红色(red=255,green=0,blue=0)

绿色(red=0,green=255,blue=0)

蓝色(red=0,green=0,blue=255)

灰色(red=128,green=128,blue=128)

白色(red=255,green=255,blue=255)

在Java的`swing`组件中，

基于`javax.swing.JComponent`

`public void paint(Graphics g)` 对界面绘制。

坐标计算从左上角(0,0)开始计算，

`Graphics`封装的较完善，可以直接对点、线、几何图形、图像、文字等直接绘制。

## 说明

原图
![原图](image/原图.png)

页签（图像、输出、日志）
![页签](image/日志.png)

右键菜单
![右键菜单](image/右键菜单.png)

信息
![信息](image/信息.png)

图像读取
![图像读取](image/图像读取.png)

比例缩放
![比例缩放](image/图像比例缩放.png)

灰度
![灰度](image/灰度.png)

二值化
![二值化](image/二值化.png)

颜色占比
![颜色占比](image/颜色比例调整.png)

色阶
![色阶](image/色阶.png)

马赛克
![马赛克](image/马赛克.png)

关于
![关于](image/关于.png)

## 自定义算法

1. 在
```
\ImageAlgorithm\src\main\resources\menu.json
```
增加菜单项目

2. 菜单项的`name`对应`org.sauceggplant.ia.algorithm`中的类

增加一个算法类

3. 算法类实现`org.sauceggplant.ia.algorithm.Algorithm`接口

实现方法`public void run(IaPanel iaPanel)`
```java
@Override
public void run(IaPanel iaPanel){
        //打开的图像，文件路径
        String imagePath = iaPanel.getPath();
        //打开的图像，数据
        BufferedImage imageData = iaPanel.getContent().getImage();

        //获取图像中某个像素点的颜色,屏幕左上角坐标为(0,0),右下角坐标为(width-1,height-1)
        int rgb = imageData.getRGB(int x, int y);
        java.awt.Color c = new java.awt.Color(rgb);
        int red = c.getRed();       //红色 0--255
        int green = c.getGreen();   //绿色 0--255
        int blue = c.getBlue();     //蓝色 0--255
        int alpha = c.getAlpha();   //透明度

        //构建颜色,RGB色彩空间
        // (当 red=0,green=0,blue=0时，为黑色)
        // (当 red=255,green=255,blue=255时，为白色)
        java.awt.Color c1 = new java.awt.Color(red,green,blue,alpha);
        //变更某个像素点的颜色
        imageData.setRGB(int x, int y, c1.getRGB());

        //图像页签
        IaImagePanel content = iaPanel.getContent();
        //输出页签
        IaImagePanel output = iaPanel.getOutput();
        
        //将加工后的图像信息展示到输出页签
        output.setImage(imageData);
}
```