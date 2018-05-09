package com.ws.util;
import java.io.ByteArrayOutputStream;   
import java.io.IOException;   
import java.util.zip.Deflater;   
import java.util.zip.Inflater;   

/** 
* ZLib压缩工具 
*   
* @author 梁栋 
* @version 1.0 
* @since 1.0 
*/ 
public abstract class ZLibUtils {   

    /** 
     * 压缩 
     *   
     * @param data 
     *            待压缩数据 
     * @return byte[] 压缩后的数据 
     */ 
    public static byte[] compress(byte[] data) {   
        byte[] output = new byte[0];   

        Deflater compresser = new Deflater();   

        compresser.reset();   
        compresser.setInput(data);   
        compresser.finish();   
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);   
        try {   
            byte[] buf = new byte[1024];   
            while (!compresser.finished()) {   
                int i = compresser.deflate(buf);   
                bos.write(buf, 0, i);   
            }   
            output = bos.toByteArray();   
        } catch (Exception e) {   
            output = data;   
            e.printStackTrace();   
        } finally {   
            try {   
                bos.close();   
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        }   
        compresser.end();   
        return output;   
    }   

    /** 
     * 解压缩 
     *   
     * @param data 
     *            待解压的数据 
     * @return byte[] 解压缩后的数据 
     */ 
    public static byte[] decompress(byte[] data) {   
        byte[] output = new byte[0];   

        Inflater decompresser = new Inflater();   
        decompresser.reset();   
        decompresser.setInput(data);   

        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);   
        try {   
            byte[] buf = new byte[1024];   
            while (!decompresser.finished()) {   
                int i = decompresser.inflate(buf);   
                o.write(buf, 0, i);   
            }
            output = o.toByteArray();   
        } catch (Exception e) {   
            output = data;   
            e.printStackTrace();   
        } finally {   
            try {   
                o.close();   
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        }   

        decompresser.end();   
        return output;   
    }   
    
    
    public static void main(String[] args){
    String inputStr = "snowolf@zlex.org;dongliang@zlex.org;zlex.dongliang@zl";   
         System.err.println("输入字符串:\t" + inputStr);   
         byte[] input = inputStr.getBytes();   
         System.err.println("输入字节长度:\t" + input.length);   
   
         byte[] data = ZLibUtils.compress(input);   
         System.err.println("压缩后字节长度:\t" + data.length);   
   
         byte[] output = ZLibUtils.decompress(data);   
         System.err.println("解压缩后字节长度:\t" + output.length);   
         String outputStr = new String(output);   
         System.err.println("输出字符串:\t" + outputStr);   
    }
}