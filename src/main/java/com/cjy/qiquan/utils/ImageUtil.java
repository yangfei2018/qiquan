package com.cjy.qiquan.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cjy.qiquan.cache.CacheManager;

/**
 * <p>Title: ImageUtil </p>
 * <p>Description: </p>
 * <p>Email: icerainsoft@hotmail.com </p> 
 * @author Ares
 * @date 2014年10月28日 上午10:24:26
 */
public class ImageUtil {

    private Logger log = LoggerFactory.getLogger(getClass());
    
    public static String DEFAULT_THUMB_PREVFIX = "thum_";
    public static String DEFAULT_CUT_PREVFIX = "thum_";
    private static Boolean DEFAULT_FORCE = false;
    
    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 根据图片路径生成缩略图 </p>
     * @param imagePath    原图片路径
     * @param w            缩略图宽
     * @param h            缩略图高
     * @param prevfix    生成缩略图的前缀
     * @param force        是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
     */
    public void thumbnailImage(File imgFile, int w, int h, String prevfix, boolean force){
        if(imgFile.exists()){
            try {
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String suffix = null;
                // 获取图片后缀
                if(imgFile.getName().indexOf(".") > -1) {
                    suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀全部小写，然后判断后缀是否合法
//                if(suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0){
//                    log.error("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
//                    return ;
//                }
                log.debug("target image's size, width:{}, height:{}.",w,h);
                Image img = ImageIO.read(imgFile);
                if(!force){
                    // 根据原图与要求的缩略图比例，找到最合适的缩略图比例
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    if((width*1.0)/w < (height*1.0)/h){
                        if(width > w){
                            h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w/(width*1.0)));
                            log.debug("change image's height, width:{}, height:{}.",w,h);
                        }
                    } else {
                        if(height > h){
                            w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h/(height*1.0)));
                            log.debug("change image's width, width:{}, height:{}.",w,h);
                        }
                    }
                }
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
                g.dispose();
                String p = imgFile.getPath();
                // 将图片保存在原目录并加上前缀
                ImageIO.write(bi, suffix, new File(p.substring(0,p.lastIndexOf(File.separator)) + File.separator + prevfix +imgFile.getName()));
            } catch (IOException e) {
               log.error("generate thumbnail image failed.",e);
            }
        }else{
            log.warn("the image is not exist.");
        }
    }
    
    public void thumbnailImage(String imagePath, int w, int h, String prevfix, boolean force){
        File imgFile = new File(imagePath);
        thumbnailImage(imgFile, w, h, prevfix, force);
    }
    
    public void thumbnailImage(String imagePath, int w, int h, boolean force){
        thumbnailImage(imagePath, w, h, DEFAULT_THUMB_PREVFIX, force);
    }
    
    public void thumbnailImage(String imagePath, int w, int h){
        thumbnailImage(imagePath, w, h, DEFAULT_FORCE);
    }
    
    
    /**
     * <p>Title: cutImage</p>
     * <p>Description:  根据原图与裁切size截取局部图片</p>
     * @param srcImg    源图片
     * @param output    图片输出流
     * @param rect        需要截取部分的坐标和大小
     */
    public void cutImage(File srcImg, OutputStream output, java.awt.Rectangle rect){
        if(srcImg.exists()){
            java.io.FileInputStream fis = null;
            ImageInputStream iis = null;
            try {
                fis = new FileInputStream(srcImg);
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames()).replace("]", ",");
                String suffix = null;
                // 获取图片后缀
                if(srcImg.getName().indexOf(".") > -1) {
                    suffix = srcImg.getName().substring(srcImg.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀全部小写，然后判断后缀是否合法
                if(suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()+",") < 0){
                    log.error("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
                    return ;
                }
                // 将FileInputStream 转换为ImageInputStream
                iis = ImageIO.createImageInputStream(fis);
                // 根据图片类型获取该种类型的ImageReader
                ImageReader reader = ImageIO.getImageReadersBySuffix(suffix).next();
                reader.setInput(iis,false);
                int old_width = reader.getWidth(0);
                int old_height = reader.getHeight(0);
                
                ImageReadParam param = reader.getDefaultReadParam();
                param.setSourceRegion(rect);
                System.out.println("width:"+old_width+"height:"+old_height+",cutImage:x:"+rect.getX()+",y:"+rect.getY()+",h:"+rect.getHeight()+",w:"+rect.getWidth());
                
                BufferedImage bi = reader.read(0, param);
                int w = bi.getHeight();
                if (w>300){
                	w = 300;
                }
                BufferedImage bii = new BufferedImage(w, w, BufferedImage.TYPE_INT_RGB);
                Graphics g = bii.getGraphics();
                g.drawImage(bi, 0, 0, w, w, Color.LIGHT_GRAY, null);
                g.dispose();
                
                System.out.println("bi.width:"+bi.getWidth()+"bi.height:"+bi.getHeight());
                ImageIO.write(bii, suffix, output);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(fis != null) fis.close();
                    if(iis != null) iis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            log.warn("the src image is not exist.");
        }
    }
    
    public void cutImage(File srcImg, OutputStream output, int x, int y, int width, int height){
        cutImage(srcImg, output, new java.awt.Rectangle(x, y, width, height));
    }
    
    public void cutImage(File srcImg, String destImgPath, java.awt.Rectangle rect){
        File destImg = new File(destImgPath);
        if(destImg.exists()){
            String p = destImg.getPath();
            try {
                if(!destImg.isDirectory()) p = destImg.getParent();
                if(!p.endsWith(File.separator)) p = p + File.separator;
                cutImage(srcImg, new java.io.FileOutputStream(p + DEFAULT_CUT_PREVFIX + srcImg.getName()), rect);
            } catch (FileNotFoundException e) {
                log.warn("the dest image is not exist.");
            }
        }else log.warn("the dest image folder is not exist.");
    }
    
    public void cutImage(File srcImg, String destImg, int x, int y, int width, int height){
        cutImage(srcImg, destImg, new java.awt.Rectangle(x, y, width, height));
    }
    
    public void cutImage(String srcImg, String destImg, int x, int y, int width, int height){
        cutImage(new File(srcImg), destImg, new java.awt.Rectangle(x, y, width, height));
    }
    
    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        for(int i=10011;i<=10017;i++){
        	sb.append(i).append(",");
        }
//        sb.append("insert into t_topics(t_topic_id,t_topic_name,t_topic_parent_id,t_country_id)\n");
//        sb.append("select 10000,'美国高中申请',110,1000\n");
//        sb.append("union\n");
//        sb.append("select 10001,'美国本科申请',110,1000\n");
//        sb.append("union\n");
//        sb.append("select 10002,'美国硕士申请',110,1000\n");
//        sb.append("union\n");
//        sb.append("select 10003,'美国博士申请',110,1000\n");
//        sb.append("union\n");
//        sb.append("select 10004,'美国签证申请',110,1000\n");
//        sb.append("union\n");
//        sb.append("select 10005,'雅思',120,1000\n");
//        sb.append("union\n");
//        sb.append("select 10006,'托福',120,1000\n");
//        sb.append("union\n");
//        sb.append("select 10007,'GRE',120,1000\n");
//        sb.append("union\n");
//        sb.append("select 10008,'GMRT',120,1000\n");
//        sb.append("union\n");
//        sb.append("select 10009,'SAT',120,1000\n");
//        sb.append("union\n");
//        sb.append("select 10010,'ACT',120,1000\n");
//        sb.append("union\n");
//        sb.append("select 10011,'其他考试',120,1000\n");
//        sb.append("union\n");
//        sb.append("select 10012,'行前准备',130,1000\n");
//        sb.append("union\n");
//        sb.append("select 10013,'海外生活',130,1000\n");
//        sb.append("union\n");
//        sb.append("select 10014,'打工就业',130,1000\n");
//        sb.append("union\n");
//        sb.append("select 10015,'移民',130,1000\n");
//        sb.append("union\n");
//        sb.append("select 10016,'旅行',130,1000\n");
//        sb.append("union\n");
//        sb.append("select 10017,'其他信息',130,1000\n");
//        
//        
//        
//        sb.append("union\n");
//        sb.append("select 20000,'英国高中申请',210,2000\n");
//        sb.append("union\n");
//        sb.append("select 20001,'英国本科申请',210,2000\n");
//        sb.append("union\n");
//        sb.append("select 20002,'英国硕士申请',210,2000\n");
//        sb.append("union\n");
//        sb.append("select 20003,'英国博士申请',210,2000\n");
//        sb.append("union\n");
//        sb.append("select 20004,'英国签证申请',210,2000\n");
//        sb.append("union\n");
//        sb.append("select 20005,'雅思',220,2000\n");
//        sb.append("union\n");
//        sb.append("select 20006,'托福',220,2000\n");
//        sb.append("union\n");
//        sb.append("select 20007,'A-level',220,2000\n");
//        sb.append("union\n");
//        sb.append("select 20008,'IB',220,2000\n");
//        sb.append("union\n");
//        sb.append("select 20011,'其他考试',220,2000\n");
//        sb.append("union\n");
//        sb.append("select 20012,'行前准备',230,2000\n");
//        sb.append("union\n");
//        sb.append("select 20013,'海外生活',230,2000\n");
//        sb.append("union\n");
//        sb.append("select 20014,'打工就业',230,2000\n");
//        sb.append("union\n");
//        sb.append("select 20015,'移民',230,2000\n");
//        sb.append("union\n");
//        sb.append("select 20016,'旅行',230,2000\n");
//        sb.append("union\n");
//        sb.append("select 20017,'其他信息',230,2000\n");
//        
//        
//        String f ="3";
//        String c = "澳新";
//        sb.append("union\n");
//        sb.append("select "+f+"0000,'"+c+"高中申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0001,'"+c+"本科申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0002,'"+c+"硕士申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0003,'"+c+"博士申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0004,'"+c+"签证申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0005,'雅思',"+f+"20,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0006,'托福',"+f+"20,"+f+"000\n");
//        sb.append("union\n");
////        sb.append("select "+f+"0007,'A-level',"+f+"20,"+f+"000\n");
////        sb.append("union\n");
////        sb.append("select "+f+"0008,'IB',"+f+"20,"+f+"000\n");
////        sb.append("union\n");
//        sb.append("select "+f+"0011,'其他考试',"+f+"20,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0012,'行前准备',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0013,'海外生活',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0014,'打工就业',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0015,'移民',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0016,'旅行',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0017,'其他信息',"+f+"30,"+f+"000\n");
//        
//        
//        f ="4";
//        c = "加拿大";
//        sb.append("union\n");
//        sb.append("select "+f+"0000,'"+c+"高中申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0001,'"+c+"本科申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0002,'"+c+"硕士申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0003,'"+c+"博士申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0004,'"+c+"签证申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0005,'雅思',"+f+"20,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0006,'托福',"+f+"20,"+f+"000\n");
//        sb.append("union\n");
////        sb.append("select "+f+"0007,'A-level',"+f+"20,"+f+"000\n");
////        sb.append("union\n");
////        sb.append("select "+f+"0008,'IB',"+f+"20,"+f+"000\n");
////        sb.append("union\n");
//        sb.append("select "+f+"0011,'其他考试',"+f+"20,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0012,'行前准备',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0013,'海外生活',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0014,'打工就业',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0015,'移民',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0016,'旅行',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0017,'其他信息',"+f+"30,"+f+"000\n");
//        
//        f ="5";
//        c = "欧洲";
//        sb.append("union\n");
//        sb.append("select "+f+"0000,'"+c+"高中申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0001,'"+c+"本科申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0002,'"+c+"硕士申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0003,'"+c+"其他申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0004,'"+c+"签证申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0005,'雅思',"+f+"20,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0006,'托福',"+f+"20,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0007,'德适',"+f+"20,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0008,'法语',"+f+"20,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0011,'其他考试',"+f+"20,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0012,'行前准备',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0013,'海外生活',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0014,'打工就业',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0015,'移民',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0016,'旅行',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0017,'其他信息',"+f+"30,"+f+"000\n");
//        
//        
//        f ="6";
//        c = "亚洲";
//        sb.append("union\n");
//        sb.append("select "+f+"0001,'"+c+"本科申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0002,'"+c+"硕士申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0003,'"+c+"其他申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0004,'"+c+"签证申请',"+f+"10,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0005,'雅思',"+f+"20,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0006,'托福',"+f+"20,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0007,'JLPT考试',"+f+"20,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0008,'韩语考试',"+f+"20,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0011,'其他考试',"+f+"20,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0012,'行前准备',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0013,'海外生活',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0014,'打工就业',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0015,'移民',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0016,'旅行',"+f+"30,"+f+"000\n");
//        sb.append("union\n");
//        sb.append("select "+f+"0017,'其他信息',"+f+"30,"+f+"000\n");
        
//        System.out.println(sb.toString());
        
        CacheManager.instance.addCacheEx("__demo",
				"test", "demo", Constant.DATE.TIME_ONE_DAY_SECOND * 30);
        
        String v = CacheManager.instance.getCache("__demo",
				"test");
        
        System.out.println(v);
    }

}