/*
 * Copyright  (c) 2017. By AsherLi0103
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.github.asherli0103.utils;


import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.*;
import java.io.*;
import java.net.URL;

/**
 * 图片处理工具类：<br>
 * 功能：缩放图像、切割图像、图像类型转换、彩色转黑白、文字水印、图片水印等
 *
 * @author AsherLi0103
 * @version 1.0.00
 */
@SuppressWarnings(value = {"unused"})
public class ImageUtil {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 几种常见的图片格式
     */
    public static String IMAGE_TYPE_GIF = "gif";// 图形交换格式
    public static String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
    public static String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
    public static String IMAGE_TYPE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
    public static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
    public static String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop

    /**
     * 缩放图像（按比例缩放）
     *
     * @param srcImageFile 源图像文件地址
     * @param result       缩放后的图像地址
     * @param scale        缩放比例
     * @param flag         缩放选择:true 放大; false 缩小;
     */
    public static void scale(String srcImageFile, String result, int scale, boolean flag) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
            int width = src.getWidth(); // 得到源图宽
            int height = src.getHeight(); // 得到源图长
            if (flag) {// 放大
                width = width * scale;
                height = height * scale;
            } else {// 缩小
                width = width / scale;
                height = height / scale;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            ImageIO.write(tag, IMAGE_TYPE_JPEG, new File(result));// 输出到文件流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缩放图像（按高度和宽度缩放）
     *
     * @param srcImageFile 源图像文件地址
     * @param height       缩放后的高度
     * @param width        缩放后的宽度
     * @param bb           比例不对时是否需要补白：true为补白; false为不补白;
     * @return exception
     */
    public static byte[] scale2(InputStream srcImageFile, int height, int width, boolean bb) {
        try {
            double ratio; // 缩放比例
            BufferedImage bi = ImageIO.read(srcImageFile);
            Image itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue() / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bb) {//补白
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null))
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null),
                            itemp.getHeight(null), Color.white, null);
                else
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null),
                            itemp.getHeight(null), Color.white, null);
                g.dispose();
                itemp = image;
            }
            //ImageIO.write((BufferedImage) itemp, "JPEG", new File(result));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write((BufferedImage) itemp, IMAGE_TYPE_JPG, out);
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 缩放图像（按高度和宽度缩放）
     *
     * @param srcImageFile 源图像文件地址
     * @param result       缩放后的图像地址
     * @param height       缩放后的高度
     * @param width        缩放后的宽度
     * @param bb           比例不对时是否需要补白：true为补白; false为不补白;
     */
    public static void scale2(String srcImageFile, String result, int height, int width, boolean bb) {
        try {
            double ratio; // 缩放比例
            File f = new File(srcImageFile);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue() / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bb) {//补白
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null))
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null),
                            itemp.getHeight(null), Color.white, null);
                else
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null),
                            itemp.getHeight(null), Color.white, null);
                g.dispose();
                itemp = image;
            }
            ImageIO.write((BufferedImage) itemp, IMAGE_TYPE_JPEG, new File(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像切割(按指定起点坐标和宽高切割)
     *
     * @param srcImageFile 源图像地址
     * @param result       切片后的图像地址
     * @param x            目标切片起点坐标X
     * @param y            目标切片起点坐标Y
     * @param width        目标切片宽度
     * @param height       目标切片高度
     */
    public static void cut(String srcImageFile, String result, int x, int y, int width, int height) {
        try {
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                // 四个参数分别为图像起点坐标和宽高
                // 即: CropImageFilter(int x,int y,int width,int height)
                ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
                Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(),
                        cropFilter));
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
                g.dispose();
                // 输出为文件
                ImageIO.write(tag, IMAGE_TYPE_JPEG, new File(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像切割（指定切片的行数和列数）
     *
     * @param srcImageFile 源图像地址
     * @param descDir      切片目标文件夹
     * @param rows         目标切片行数。默认2，必须是范围 [1, 20] 之内
     * @param cols         目标切片列数。默认2，必须是范围 [1, 20] 之内
     */
    public static void cut2(String srcImageFile, String descDir, int rows, int cols) {
        try {
            if (rows <= 0 || rows > 20) rows = 2; // 切片行数
            if (cols <= 0 || cols > 20) cols = 2; // 切片列数
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                int destWidth; // 每张切片的宽度
                int destHeight; // 每张切片的高度
                // 计算切片的宽度和高度
                if (srcWidth % cols == 0) {
                    destWidth = srcWidth / cols;
                } else {
                    destWidth = (int) Math.floor(srcWidth / cols) + 1;
                }
                if (srcHeight % rows == 0) {
                    destHeight = srcHeight / rows;
                } else {
                    destHeight = (int) Math.floor(srcWidth / rows) + 1;
                }
                // 循环建立切片
                // 改进的想法:是否可用多线程加快切割速度
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // 四个参数分别为图像起点坐标和宽高
                        // 即: CropImageFilter(int x,int y,int width,int height)
                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(),
                                cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        ImageIO.write(tag, IMAGE_TYPE_JPEG, new File(descDir + "_r" + i + "_c" + j + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像切割（指定切片的宽度和高度）
     *
     * @param srcImageFile 源图像地址
     * @param descDir      切片目标文件夹
     * @param destWidth    目标切片宽度。默认200
     * @param destHeight   目标切片高度。默认150
     */
    public static void cut3(String srcImageFile, String descDir, int destWidth, int destHeight) {
        try {
            if (destWidth <= 0) destWidth = 200; // 切片宽度
            if (destHeight <= 0) destHeight = 150; // 切片高度
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > destWidth && srcHeight > destHeight) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                int cols; // 切片横向数量
                int rows; // 切片纵向数量
                // 计算切片的横向和纵向数量
                if (srcWidth % destWidth == 0) {
                    cols = srcWidth / destWidth;
                } else {
                    cols = (int) Math.floor(srcWidth / destWidth) + 1;
                }
                if (srcHeight % destHeight == 0) {
                    rows = srcHeight / destHeight;
                } else {
                    rows = (int) Math.floor(srcHeight / destHeight) + 1;
                }
                // 循环建立切片
                // 改进的想法:是否可用多线程加快切割速度
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // 四个参数分别为图像起点坐标和宽高
                        // 即: CropImageFilter(int x,int y,int width,int height)
                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(),
                                cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        ImageIO.write(tag, IMAGE_TYPE_JPEG, new File(descDir + "_r" + i + "_c" + j + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像类型转换：GIF-&gt;JPG、GIF-&gt;PNG、PNG-&gt;JPG、PNG-&gt;GIF(X)、BMP-&gt;PNG
     *
     * @param srcImageFile  源图像地址
     * @param formatName    包含格式非正式名称的 String：如JPG、JPEG、GIF等
     * @param destImageFile 目标图像地址
     */
    public static void convert(String srcImageFile, String formatName, String destImageFile) {
        try {
            File f = new File(srcImageFile);
            BufferedImage src = ImageIO.read(f);
            ImageIO.write(src, formatName, new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 彩色转为黑白
     *
     * @param srcImageFile  源图像地址
     * @param destImageFile 目标图像地址
     */
    public static void gray(String srcImageFile, String destImageFile) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile));
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            src = op.filter(src, null);
            ImageIO.write(src, IMAGE_TYPE_JPEG, new File(destImageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给图片添加文字水印
     *
     * @param pressText     水印文字
     * @param srcImageFile  源图像地址
     * @param destImageFile 目标图像地址
     * @param fontName      水印的字体名称
     * @param fontStyle     水印的字体样式
     * @param color         水印的字体颜色
     * @param fontSize      水印的字体大小
     * @param x             修正值
     * @param y             修正值
     * @param alpha         透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public static void pressText(String pressText, String srcImageFile, String destImageFile, String fontName, int fontStyle, Color color, int fontSize, int x, int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 在指定坐标绘制水印文字
            g.drawString(pressText, (width - (getLength(pressText) * fontSize)) / 2 + x, (height - fontSize) / 2 + y);
            g.dispose();
            ImageIO.write(image, IMAGE_TYPE_JPEG, new File(destImageFile));// 输出到文件流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给图片添加文字水印
     *
     * @param pressText     水印文字
     * @param srcImageFile  源图像地址
     * @param destImageFile 目标图像地址
     * @param fontName      字体名称
     * @param fontStyle     字体样式
     * @param color         字体颜色
     * @param fontSize      字体大小
     * @param x             修正值
     * @param y             修正值
     * @param alpha         透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public static void pressText2(String pressText, String srcImageFile, String destImageFile, String fontName, int fontStyle, Color color, int fontSize, int x, int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 在指定坐标绘制水印文字
            g.drawString(pressText, (width - (getLength(pressText) * fontSize)) / 2 + x, (height - fontSize) / 2 + y);
            g.dispose();
            ImageIO.write(image, IMAGE_TYPE_JPEG, new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给图片添加图片水印
     *
     * @param pressImg      水印图片
     * @param srcImageFile  源图像地址
     * @param destImageFile 目标图像地址
     * @param x             修正值。 默认在中间
     * @param y             修正值。 默认在中间
     * @param alpha         透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public static void pressImage(String pressImg, String srcImageFile, String destImageFile,
                                  int x, int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            // 水印文件
            Image src_biao = ImageIO.read(new File(pressImg));
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawImage(src_biao, (wideth - wideth_biao) / 2, (height - height_biao) / 2, wideth_biao, height_biao,
                    null);
            // 水印文件结束
            g.dispose();
            ImageIO.write(image, IMAGE_TYPE_JPEG, new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算text的长度（一个中文算两个字符）
     *
     * @param text exception
     * @return exception
     */
    public static int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if ((text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length / 2;
    }

//    /**
//     * 程序入口：用于测试
//     * @param args
//     */
//    public static void main(String[] args) {
//        // 1-缩放图像：
//        // 方法一：按比例缩放
//        ImageUtil.scale("e:/abc.jpg", "e:/abc_scale.jpg", 2, true);//测试OK
//        // 方法二：按高度和宽度缩放
//        ImageUtil.scale2("e:/abc.jpg", "e:/abc_scale2.jpg", 500, 300, true);//测试OK
//        // 2-切割图像：
//        // 方法一：按指定起点坐标和宽高切割
//        ImageUtil.cut("e:/abc.jpg", "e:/abc_cut.jpg", 0, 0, 400, 400 );//测试OK
//        // 方法二：指定切片的行数和列数
//        ImageUtil.cut2("e:/abc.jpg", "e:/", 2, 2 );//测试OK
//        // 方法三：指定切片的宽度和高度
//        ImageUtil.cut3("e:/abc.jpg", "e:/", 300, 300 );//测试OK
//        // 3-图像类型转换：
//        ImageUtil.convert("e:/abc.jpg", "GIF", "e:/abc_convert.gif");//测试OK
//        // 4-彩色转黑白：
//        ImageUtil.gray("e:/abc.jpg", "e:/abc_gray.jpg");//测试OK
//        // 5-给图片添加文字水印：
//        // 方法一：
//        ImageUtil.pressText("我是水印文字","e:/abc.jpg","e:/abc_pressText.jpg","宋体",Font.BOLD,Color.white,80, 0, 0, 0.5f);//测试OK
//        // 方法二：
//        ImageUtil.pressText2("我也是水印文字", "e:/abc.jpg","e:/abc_pressText2.jpg", "黑体", 36, Color.white, 80, 0, 0, 0.5f);//测试OK
//        
//        // 6-给图片添加图片水印：
//        ImageUtil.pressImage("e:/abc2.jpg", "e:/abc.jpg","e:/abc_pressImage.jpg", 0, 0, 0.5f);//测试OK
//    }

    /**
     * 导入本地图片到缓冲区
     *
     * @param file 图片文件
     * @return 图片缓冲流
     */
    public static BufferedImage loadImageLocal(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            logger.error("缓冲区缓存本地图片失败,失败原因: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 导入本地图片到缓冲区
     *
     * @param imgName 图片路径 ,全路径带有文件名
     * @return 图片缓冲流
     */
    public static BufferedImage loadImageLocal(String imgName) {
        try {
            return ImageIO.read(new FileInputStream(new File(imgName)));
        } catch (IOException e) {
            logger.error("缓冲区缓存本地图片失败,失败原因: {}", e.getMessage(), e);
        }
        return null;
    }

    public static BufferedImage loadImageLocal(InputStream imgName) {
        try {
            return ImageIO.read(imgName);
        } catch (IOException e) {
            logger.error("缓冲区缓存本地图片失败,失败原因: {}", e.getMessage(), e);
        }
        return null;
    }


    /**
     * 导入网络图片到缓冲区
     *
     * @param imgName 图片网络路径
     * @return 图片
     */
    public static BufferedImage loadImageUrl(String imgName) {
        try {
            URL url = new URL(imgName);
            return ImageIO.read(url);
        } catch (IOException e) {

            // logger.error("网络图片导入失败,失败原因: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将图片文件按指定格式写到本地
     *
     * @param newImage 图片保存路径,全路径带有文件名
     * @param img      图片缓冲流
     */
    public static void writeImageFile(String newImage, BufferedImage img) {
        if (newImage != null && img != null) {
            try {
                File file = new File(newImage);
                String type = newImage.substring(newImage.lastIndexOf(".") + 1, newImage.length());
                ImageIO.write(img, type, file);
            } catch (IOException e) {
                logger.error("生成本地图片文件失败,失败原因:{}", e.getMessage(), e);
            }
        }
    }

    /**
     * 将图片缓冲流写入临时文件
     *
     * @param imageName 图片名称,名称不能带有后缀
     * @param img       图片缓冲流
     * @param format    图片类型 ,类型不能带有"."
     * @return 临时文件
     */
    public static File writeImageTempFile(String imageName, BufferedImage img, String format) {
        if (StringUtil.isNotBlank(imageName) && ObjectUtil.isNotNull(img) && StringUtil.isNotBlank(format)) {

            // 先检查保存的图片格式是否正确
            String[] legalFormats = {"jpg", "JPG", "png", "PNG", "bmp", "BMP"};
            int i = 0;
            for (i = 0; i < legalFormats.length; i++) {
                if (format.equals(legalFormats[i])) {
                    break;
                }
            }
            if (i == legalFormats.length) { // 图片格式不支持
                logger.error("不是保存所支持的图片格式!");
                return null;
            }

            // 再检查文件后缀和保存的格式是否一致
            String postfix = imageName.substring(imageName.lastIndexOf('.') + 1);
            if (postfix.equalsIgnoreCase("." + format)) {
                logger.error("文件名不能带有后缀!");
                return null;
            }

            try {
                File file = File.createTempFile(imageName, "." + format);
                boolean falg = ImageIO.write(img, format, file);
                if (falg) {
                    return file;
                } else {
                    logger.info("图片写入失败");
                }
            } catch (IOException e) {
                logger.error("生成临时缓存图片文件失败,失败原因:{}", e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 保存图片
     *
     * @param savedImg 待保存的图像
     * @param saveDir  保存的目录
     * @param fileName 保存的文件名，必须带后缀，比如 "beauty.jpg"
     * @param format   文件格式：jpg、png或者bmp
     */
    public static boolean saveImage(BufferedImage savedImg, String saveDir, String fileName, String format) {
        boolean flag = false;

        // 先检查保存的图片格式是否正确
        String[] legalFormats = {"jpg", "JPG", "png", "PNG", "bmp", "BMP"};
        int i = 0;
        for (i = 0; i < legalFormats.length; i++) {
            if (format.equals(legalFormats[i])) {
                break;
            }
        }
        if (i == legalFormats.length) { // 图片格式不支持
            logger.error("不是保存所支持的图片格式!");
            return false;
        }

        // 再检查文件后缀和保存的格式是否一致
        String postfix = fileName.substring(fileName.lastIndexOf('.') + 1);
        if (!postfix.equalsIgnoreCase(format)) {
            logger.error("待保存文件后缀和保存的格式不一致!");
            return false;
        }

        String fileUrl = saveDir + fileName;
        File file = new File(fileUrl);
        try {
            flag = ImageIO.write(savedImg, format, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 待合并的两张图必须满足这样的前提，如果水平方向合并，则高度必须相等；如果是垂直方向合并，宽度必须相等。
     * mergeImage方法不做判断，自己判断。
     *
     * @param img1         待合并的第一张图
     * @param img2         带合并的第二张图
     * @param isHorizontal 为true时表示水平方向合并，为false时表示垂直方向合并
     * @return 返回合并后的BufferedImage对象
     * @throws IOException 合并失败抛出
     */
    public static BufferedImage mergeImage(BufferedImage img1, BufferedImage img2, boolean isHorizontal) throws IOException {
        int w1 = img1.getWidth();
        int h1 = img1.getHeight();
        int w2 = img2.getWidth();
        int h2 = img2.getHeight();

        // 从图片中读取RGB
        int[] ImageArrayOne = new int[w1 * h1];
        ImageArrayOne = img1.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
        int[] ImageArrayTwo = new int[w2 * h2];
        ImageArrayTwo = img2.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);

        // 生成新图片
        BufferedImage DestImage = null;
        if (isHorizontal) { // 水平方向合并
            DestImage = new BufferedImage(w1 + w2, h1, BufferedImage.TYPE_INT_RGB);
            DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            DestImage.setRGB(w1, 0, w2, h2, ImageArrayTwo, 0, w2);
        } else { // 垂直方向合并
            DestImage = new BufferedImage(w1, h1 + h2,
                    BufferedImage.TYPE_INT_RGB);
            DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            DestImage.setRGB(0, h1, w2, h2, ImageArrayTwo, 0, w2); // 设置下半部分的RGB
        }

        return DestImage;
    }

    /**
     * 图片叠加
     *
     * @param img1          带合成第一张图片
     * @param img2          带合成第二张图片
     * @param x             x轴定位
     * @param y             y轴定位
     * @param xSize         第二张图片压缩宽度
     * @param ySize         第二张图片压缩高度
     * @param isCompression 是否压缩第二张图片
     * @return 合成图片
     */
    public static BufferedImage superpositionImage(BufferedImage img1, BufferedImage img2, int x, int y, int xSize, int ySize, boolean isCompression) {
        int x1 = 0;
        int y1 = 0;
        int w1 = img1.getWidth();
        int h1 = img1.getHeight();

        // 从图片中读取RGB
        int[] ImageArrayOne = new int[w1 * h1];
        ImageArrayOne = img1.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
        if (isCompression) {
            try {
                img2 = Thumbnails.of(img2).size(xSize, ySize).asBufferedImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int w2 = img2.getWidth();
        int h2 = img2.getHeight();
        int[] ImageArrayTwo = new int[w2 * h2];
        ImageArrayTwo = img2.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);
        if (x >= w1 || y >= h1) {
            x1 = w1;
            y1 = h1;
        } else {
            x1 = x;
            y1 = y;
        }
        //生成新图片
        BufferedImage ImageNew = new BufferedImage(w1, h1, BufferedImage.TYPE_INT_RGB);
        ImageNew.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1);//设置左半部分的RGB
        ImageNew.setRGB(x1, y1, w2, h2, ImageArrayTwo, 0, w2);//设置右半部分的RGB
        return ImageNew;
    }

    /**
     * 修改图片,返回修改后的图片缓冲区（只输出一行文本）
     *
     * @param img       图片
     * @param content   要添加的文字
     * @param x         文字x轴位置
     * @param y         文字y轴位置
     * @param fontType  字体名称
     * @param fontStyle 字体样式
     * @param fontSize  字体大小
     * @param fontColor 字体颜色
     * @param backColor 背景颜色
     * @return 合成后图片
     */
    public static BufferedImage modifyImage(BufferedImage img, Object content, int x, int y, String fontType, int fontStyle, int fontSize, Color fontColor, Color backColor) {

        Graphics2D g = null;
        int x1 = 0;
        int y1 = 0;

        Font font = new Font("宋体", fontStyle, fontSize);// 添加字体的属性设置
        if (StringUtil.isNotBlank(fontType)) {
            font = new Font(fontType, fontStyle, fontSize);// 添加字体的属性设置
        }

        try {
            int w = img.getWidth();
            int h = img.getHeight();
            g = img.createGraphics();
            if (ObjectUtil.isNull(backColor)) {
                g.setBackground(Color.WHITE);
            } else {
                g.setBackground(backColor);
            }
            if (ObjectUtil.isNull(fontColor)) {
                g.setColor(Color.white);//设置字体颜色
            } else {
                g.setColor(fontColor);
            }
            g.setFont(font);
            // 验证输出位置的纵坐标和横坐标
            if (x >= w || y >= h) {
                x1 = w;
                y1 = h - fontSize + 2;
            } else {
                x1 = x;
                y1 = y;
            }
            if (content != null) {
                g.drawString(content.toString(), x1, y1);
            }
            g.dispose();
        } catch (Exception e) {
            logger.error("图片修改失败,失败原因: {}", e.getMessage(), e);
        }

        return img;
    }

    /**
     * 修改图片,返回修改后的图片缓冲区（输出多个文本段） xory：true表示将内容在一行中输出；false表示将内容多行输出
     *
     * @param img        图片
     * @param contentArr 要添加的文字
     * @param x          文字x轴位置
     * @param y          文字y轴位置
     * @param xory       true表示将内容在一行中输出；false表示将内容多行输出
     * @param fontType   字体名称
     * @param fontStyle  字体样式
     * @param fontSize   字体大小
     * @param fontColor  字体颜色
     * @param backColor  背景颜色
     * @return 合成后图片
     */
    public static BufferedImage modifyImage(BufferedImage img, Object[] contentArr,
                                            int x, int y, boolean xory, String fontType, int fontStyle, int fontSize, Color fontColor, Color backColor) {
        Graphics2D g = null;
        int x1 = 0;
        int y1 = 0;

//        Font font = new Font("宋体", fontStyle, fontSize);// 添加字体的属性设置
//        if (StringUtils.isNotBlank(fontType)) {
        Font font = new Font(fontType, fontStyle, fontSize);// 添加字体的属性设置
//        }

        try {
            int w = img.getWidth();
            int h = img.getHeight();
            g = img.createGraphics();
            if (ObjectUtil.isNull(backColor)) {
                g.setBackground(Color.WHITE);
            } else {
                g.setBackground(backColor);
            }
            if (ObjectUtil.isNull(fontColor)) {
                g.setColor(Color.white);//设置字体颜色
            } else {
                g.setColor(fontColor);
            }
            g.setFont(font);
            // 验证输出位置的纵坐标和横坐标
            if (x >= w || y >= h) {
                x1 = w;
                y1 = h - fontSize + 2;
            } else {
                x1 = x;
                y1 = y;
            }
            if (contentArr != null) {
                int arrlen = contentArr.length;
                if (xory) {
                    for (Object aContentArr : contentArr) {
                        g.drawString(aContentArr.toString(), x1, y1);
                        x1 += aContentArr.toString().length()
                                * fontSize / 2 + 10;// 重新计算文本输出位置
                    }
                } else {
                    for (Object aContentArr : contentArr) {
                        g.drawString(aContentArr.toString(), x1, y1);
                        y1 += fontSize + 2;// 重新计算文本输出位置
                    }
                }
            }
            g.dispose();
        } catch (Exception e) {
            logger.error("图片修改失败,失败原因: {}", e.getMessage(), e);
        }

        return img;
    }

    /**
     * 图片切圆角
     *
     * @param srcImage
     * @param radius
     * @return
     */
    public static BufferedImage setClip(BufferedImage srcImage, int radius) {
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        BufferedImage image = new BufferedImage(width, height, Transparency.TRANSLUCENT);
        Graphics2D gs = image.createGraphics();
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setClip(new RoundRectangle2D.Double(0, 0, width, height, radius, radius));
        gs.setBackground(Color.WHITE);
        gs.drawImage(srcImage, 0, 0, null);
        gs.dispose();
        return image;
    }

    /**
     * 图片设置圆角
     *
     * @param srcImage
     * @param radius
     * @param border
     * @param padding
     * @return
     * @throws IOException
     */
    public static BufferedImage setRadius(BufferedImage srcImage, int radius, int border, int padding) throws IOException {
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        int canvasWidth = width + padding * 2;
        int canvasHeight = height + padding * 2;

        BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, Transparency.TRANSLUCENT);
        Graphics2D gs = image.createGraphics();
        gs.setBackground(Color.WHITE);
        gs.setComposite(AlphaComposite.Src);
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setColor(Color.WHITE);
        gs.fill(new RoundRectangle2D.Float(0, 0, canvasWidth, canvasHeight, radius, radius));
        gs.setComposite(AlphaComposite.SrcAtop);
        gs.drawImage(setClip(srcImage, radius), padding, padding, null);
        if (border != 0) {
            gs.setColor(Color.GRAY);
            gs.setStroke(new BasicStroke(border));
            gs.drawRoundRect(padding, padding, canvasWidth - 2 * padding, canvasHeight - 2 * padding, radius, radius);
        }
        gs.dispose();
        return image;
    }

    /**
     * 图片设置圆角
     *
     * @param srcImage
     * @return
     * @throws IOException
     */
    public static BufferedImage setRadius(BufferedImage srcImage) throws IOException {
        int radius = (srcImage.getWidth() + srcImage.getHeight()) / 15;
        return setRadius(srcImage, radius, 0, 0);
    }
}
