package com.ws.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
//微盛 推送式 HTTP URL API接口 demo 程序，完整《开发文档》及说明，请参见：http://test.wstock.cn/wsTCP_API.htm
//说明：本程序 演示 如何通过 微盛推送式HTTP URL API接口获取实时行情(zlib解压缩，二进制格式转txt格式)。
//		本程序也支持下载 网页、图片，也支持下载微盛请求式API接口（gzip解压缩）
//参数1：URL，如果缺省，则默认为  http://test.wstock.cn/cgi-bin/wsRTAPI/wsr2.asp?t=n&m=市场&u=用户名&p=密码
//		 		可用市场、用户名、密码，可来信申请。
//				m=市场： 可通过英文逗号分隔不同的市场，可在一个链接中，同时获取多个市场的实时行情
//				t=n：获取最新实时行情。如果更改为t=i，则可获取到对应的全市场全部瓶中的快照行情（t=i，仅用于获取全市场快照，一天最多只能获取100次）
//参数2：编码方式，例如 utf-8 （可缺省，例如输出一个字符a）；
//参数3：存储到文件（含路径名），例如 d:/temp/wsz.txt  如果缺省，则在命令行中直接输出，不存储
//参数4：指定一个品种代码（股票代码）；如果指定，则在命令行中显示该股票（品种）的行情。
//
//java命令行编译本文件：javac wsGetwsz.java
//java命令行执行本程序： java wsGetwsz http://test.wstock.cn/cgi-bin/wsRTAPI/wsr2.asp?t=n&m=市场&u=用户名&p=密码 a wsHTTP.txt
//
//wstock.net  微盛投资 2015/11/08
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

import com.cjy.qiquan.utils.Constant;

public class wsGetwsz {
	static String ContentType="";	//实际下载得到的内容类型，来自 HTTP头部的 Content-Type，例如 Content-Type: text/plain; charset=utf-8
	static String ContentEncoding="";	//实际下载得到的内容类型，来自 HTTP头部的  Content-Encoding，例如 Content-Encoding: gzip
	static String ContentDisposition="";		// 微盛的全推送式HTTP API接口，会包括附件描述信息，因此可通过此标志判断是否为wsz格式; attachment; filename=wstock.net
	static String ViewCode="";			//	准备在接收到的代码中，显示哪一只股票的代码
	static boolean boolWSZ=false;		// 是否为wsz格式，默认为否
	static int iTotalBytes=0;					//  总共下载了多次字节数据
	
	// 追加方式写入，将byte数组写入文件，从off偏移开始，写入len字节
	// wstock.net 2015/10/13
	public static void AppendToFile(String filename, byte[] data, int off, int len) {		// throws IOException  {
		if (filename.length()>0 && len>0) {
			try {
				FileOutputStream fos = new FileOutputStream(filename,true);   // true：采用追加方式写入
				fos.write(data,off,len);		// 如文件存在，会自动追加写入到文件末尾
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 微盛Rpt156 或者 MT64 数据结构 转 txt
	// 自动识别是 156字节的数据结构，还是 MT市场（64字节）的数据结构
	// wstock.net 2015/11/05
	public static String wsRpt156or64ToTXT(byte[] b) {  
		boolean boolMT=false;	int recLen=156;			// 默认每条记录156字节

		if ( b.length<64) {return "error,b.length<64,  b.length=" + b.length;}			// 至少应该有64字节

		String str="时间,代码,名称,总笔数或前结算价,现量,持仓或保留,结算价或保留,前收盘价,开盘价,最高价,最低价,最新价,总量,总金额,委买价1,委买价2,委买价3,委买价4,委买价5,委买量1,委买量2,委买量3,委买量4,委买量5,委卖价1,委卖价2,委卖价3,委卖价4,委卖价5,委卖量1,委卖量2,委卖量3,委卖量4,委卖量5\r\n"; 		// 字段名称
		if (b[4]==70 && b[5]==88 && b.length % 64 ==0 ) {boolMT=true; recLen=64;str="时间,代码,名称,开盘价,最高价,最低价,委买价1,委卖价1\r\n";}		// MT市场的代码为FX字头。FX的ASCII码为70、88，因此通过此特征，结合每条记录长度为64字节来判断
		if (! boolMT ) { if  (b.length % 156 !=0 || b.length<156) {return "error,b.length=" + b.length;} }
		
		for (int i=0;i<b.length;i+=recLen) {
			String s2="";
			// 转化时间
			int k=bytesToInt(b,i); long k2=k; k2=k2*1000; Timestamp ts=new Timestamp(k2); // System.out.println("k=" + k + "\tk2=" + k2 + "\tts=" + ts);
			DateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				s2=sdf.format(ts);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 股票代码
			String m_szLabel = "";  m_szLabel=new String(b, i+4, 12); k=m_szLabel.indexOf("\00"); if (k>=0) {m_szLabel=m_szLabel.substring(0,k); }
			String m_szName = "";

			// 股票的中文简称
			try {
				m_szName=new String(b, i+16, (boolMT?12:16),"GBK"); k=m_szName.indexOf("\00"); if (k>=0) {m_szName=m_szName.substring(0,k); }
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			s2=s2 + "," + m_szLabel + "," + m_szName;

			if (boolMT) {		// 每条记录64字节的情况，首先是4字节的float，然后是4个8字节的double
				float f=bytesToFloat(b,i+28); s2 += String.format(",%1$.5f",f);		// 开盘价
				for (int j=0;j<4;j++) {			// 每条记录64字节的情况，从32字节开始，以后总共4个double，利用循环转出
					double d=bytesToDouble(b,i+32+j*8); s2 += String.format(",%1$.5f",d);
				}
			} else { 				
				for (int j=0;j<31;j++) {			// 每条记录156字节的情况，从32字节开始，之后全部为float，总共31个float，利用循环转出
					float f=bytesToFloat(b,i+32+j*4); s2 += String.format(",%1$.3f",f);
				}
			}
			
			str += s2 + "\r\n";	
			if ( ViewCode.length()>0 ) {	if ( m_szLabel.equals(ViewCode) ) {System.out.println(s2);} }			// 用户指定了需要显示的代码，则显示对应代码
		}
		return str;
	}

	// 字节数组转整型，该函数中字节数组是高字节在后的方式；java中，byte、int、long均是带符号方式（有正负）
	// wstock.net 2015/11/04
	public static int bytesToInt(byte[] b,int pos) {  
		if (b.length<pos+4) {return 0;}
		// int i = (b[pos] << 24) & 0xFF000000;        i |= (b[pos+1] << 16) & 0xFF0000;          i |= (b[pos+2] << 8) & 0xFF00;          i |= b[pos+3] & 0xFF;		// 高字节在前
		int i = (b[pos+3] << 24) & 0xFF000000;        i |= (b[pos+2] << 16) & 0xFF0000;          i |= (b[pos+1] << 8) & 0xFF00;          i |= b[pos] & 0xFF;  			// 高字节在后
		return i;
	}

	// java字节数组转8字节整型，本函数中对应的 字节数组，采用的是高字节在后的方式
	// 如果不强制转换为long，那么默认会当作int，导致最高32位丢失  
	public static long bytesToLong(byte[] b,int pos) {  
			if (b.length<pos+4) {return 0;}
				long l = ((long) b[pos+7] << 56) & 0xFF00000000000000L;  
				l |= ((long) b[pos+6] << 48) & 0xFF000000000000L;  
				l |= ((long) b[pos+5] << 40) & 0xFF0000000000L;  
				l |= ((long) b[pos+4] << 32) & 0xFF00000000L;  
				l |= ((long) b[pos+3] << 24) & 0xFF000000L;  
				l |= ((long) b[pos+2] << 16) & 0xFF0000L;  
				l |= ((long) b[pos+1] << 8) & 0xFF00L;  
				l |= (long) b[pos] & 0xFFL;  
			return l;  
	}

	// 字节数组 转 double
	public static double bytesToDouble(byte[] b,int pos) {  
		return Double.longBitsToDouble(bytesToLong(b,pos));  
	}  

	// 字节数组转 float
	public static float bytesToFloat(byte[] b,int pos) {  
		return Float.intBitsToFloat(bytesToInt(b,pos));  
	}  
	
	// 获取网页编码方式，支持匹配 http头 或 html代码
	// wstock.net 2015/10/10
	private static String getCharset(String str) {
		String s2="";

		// 先匹配 html 中的charset，类似：  <meta http-equiv='Content-Type' content='text/html; charset=gb2312'>
		Pattern pattern = Pattern.compile("(?m)charset *= *(.*?)[ ?\'?\"?>?]",Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		if ( matcher.find() ) {
			s2=matcher.group();	s2=s2.replaceAll("[ =\'\">]","");  s2=s2.replaceAll("(?i)charset","");
			if (s2 != null) if (s2.length()>1 ) {return s2;}		// System.out.println("bbbb:" + s2);
		}

		// 然后匹配 http header中的charset，类似：Content-Type: text/plain; charset=utf-8
		pattern = Pattern.compile("(?s)^.*?charset *= *(.*?)$",Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(str);
		if ( matcher.find() ) {	s2=matcher.replaceAll("$1"); return s2; }
		
		return null;
	 }

	//  解压微盛wsz数据，（微盛推送式接口 采用wsz压缩）
	// data就是接收到的数据；返回解压缩后的数据（字节数组）
	// wstock.net  2015/11/05
	public static byte[] decompressWSZ(byte[] data) {
		byte[] output = new byte[0];
		String str="";
		int pos=0; 
		
		while (pos+4 <= data.length) {
			int dwZipSize=bytesToInt(data,pos);			// 压缩块长度
			if ( dwZipSize>16777216 || dwZipSize<=0 ) {
				// 这是一个信息块，不必处理；也可在此处存储到 日志文件中
				String str2=new String(data);	System.out.println("wsz message info:" + str2);break;	// wsz中最多只包括一个信息块，因此如果为信息块，则处理后即可退出循环
			} else {
				if (pos+4 > data.length) { break; }		// 长度不足，终止循环
				int dwOriSize=bytesToInt(data,pos+4);	if (dwOriSize<=0) {break;}	// 解压缩后原始数据长度；如果不正确，则终止循环
				byte[] unzip=decompress(data,pos+8,dwZipSize-4);		// 解压缩，因为dwZipSize中包括了原始数据4字节，因此这里需要 dwZipSize-4
				pos=pos+4+dwZipSize;			// 指定下次解压缩的位置；因为wsz中可能包括多个压缩块
				if ( unzip == null ) {
					System.out.println("wsz 解压缩错误，返回null.");
				} else if (unzip.length==1 && unzip[0]==0) {
					System.out.println("wsz 解压缩错误，返回1个字节0x00");
				} else if (unzip.length != dwOriSize) {
					System.out.println("wsz 解压缩错误，返回" + unzip.length + "个字节；而原始长度应该为：" + dwOriSize + "字节");
				} else {
					// AppendToFile("d:/temp/wsTest.dat",data,0,data.length);		// 原始数据追加存储到文件，这个是解压缩后的 二进制记录数据，可存储到文件中
					str+=wsRpt156or64ToTXT(unzip);		// 将二进制记录数据，转化为txt格式
				}
			}
		}
		if (str != null) {
			if ( str.length() > 0 ) {
				try {
					output = str.getBytes("GBK");		// 这里将 字符串 转化为 字节数组，必须指定正确的编码方式；这里采用的是GBK编码
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
     return output;
 }

	 
	//  解压Gzip数据，（微盛请求式接口 采用Gzip压缩）
	// data就是接收到的数据；返回解压缩后的数据（字节数组）
	public static byte[] decompressGzip(byte[] data) {
		GZIPInputStream gis = null;
		try {
				ByteArrayInputStream bais = new ByteArrayInputStream(data);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				gis = new GZIPInputStream(bais);
				int count;
				byte data2[] = new byte[data.length];
				while ((count = gis.read(data2, 0, data.length)) != -1) {
				    baos.write(data2, 0, count);
				}
				gis.close();	  data = baos.toByteArray();
				baos.flush();	baos.close();	bais.close();            //         System.out.println("解压成功");
		} catch (IOException e) {
			e.printStackTrace();		//System.out.println(ex);           
		} finally {
			try {
				gis.close();
		} catch (IOException e) {
				e.printStackTrace();// System.out.println(ex);
         }
     }
     return data;
 }


  // 解压缩zlib数据（微盛推送式接口 采用zlib压缩）
  // @param data  待解压缩的数据
  // @return byte[] 解压缩后的数据
 public static byte[] decompress(byte[] data,int offset, int length) {  
     byte[] output = new byte[0];

     Inflater decompresser = new Inflater();
     decompresser.reset();
     decompresser.setInput(data,offset,length);

     ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);  	// ByteArrayOutputStream会自动管理内存空间，这里用 data.length只是一个初始化长度（压缩块长度，实际返回时，已变更为解压缩后的长度）
     try {  
         byte[] buf = new byte[1024];  
         while (!decompresser.finished()) {  
             int i = decompresser.inflate(buf);  
             o.write(buf, 0, i);
         }
         output = o.toByteArray();  
     } catch (Exception e) {  
         // output = data;			// 如需要，出现异常后，也可返回原始数据  
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

	// 通过字节数组下载网页,这种方法更灵活，支持下载（抓取）图片、文件，以及压缩格式gzip的网页
	// 这里通过java的 ByteArrayOutputStream 来作为缓存，代码会简单些（不用自己管理缓存大小）
	// 返回下载的数据总长度（字节）；下载的数据通过data参数返回
	// wstock.net 2015/10/12
	public static byte[] wsGetHTMLByByte(String strURL){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		// 初始化缓存
		
		try{
			URL url = new URL(strURL);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(10*1000);				// 设置超时间为10秒
			conn.setRequestProperty("User-Agent", "wstock java demo:wsGetHTMLByByte");   //告诉服务器，自己程序的名称。
			
			InputStream in = conn.getInputStream();		// 输入流
			byte[] bs = new byte[10240];			// 每次最多读取10KB
			int len = 0;	int total_size=0;	int total_len=0;
			total_len=conn.getContentLength();		// getContentLength 实际来自 HTTP头部的 content-length ，对于动态输出页面，WebServer未必包括content-length
			ContentType = conn.getContentType();		// 例如：Content-Type: text/plain; charset=utf-8
			ContentEncoding=conn.getContentEncoding();			// 例如：Content-Encoding: gzip
			ContentDisposition=conn.getHeaderField("Content-Disposition");		//例如：attachment; filename=wstock.net
			// System.out.println("ContentType:" + ContentType);
			// System.out.println("ContentEncoding from HTTP Header:" + ContentEncoding);
			// System.out.println("content-length:" + total_len);
			// System.out.println("Content-Disposition:" + ContentDisposition);

			// 循环判断，如果bs为空，则in.read()方法返回-1，具体请参考InputStream的read();
			while ((len = in.read(bs)) >0 ) { 			// while ((len = in.read(bs)) != -1) {
				baos.write(bs, 0, len);					// 将新收到的数据放入缓存baos尾部， java的 ByteArrayOutputStream 会自动管理缓存大小
				total_size+=len;	// System.out.println("downloading:" + total_size + "/" + total_len );   // 显示下载进度
			}
			in.close();
			if (total_size>0) {
				iTotalBytes+=total_size;	byte[] data = baos.toByteArray();	baos.flush();	baos.close();
				System.out.println("本次下载了：" + total_size + "字节；总共下载了：" + iTotalBytes + "字节" );   // 显示下载进度
				return data;
			} else {
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 下载指定URL的数据
	//  三个参数，依次为 URL、编码方式、存储的文件名
	// wstock.net  2015/11/06
	private static int wsDownload(String URL, String encoding, String filename) {
		byte[] html_buffer=wsGetHTMLByByte(URL);			// 下载文件 或者 获取对应网页的数据
		if (html_buffer==null) {System.out.println("get 0 byte,return."); return -1;}
		
		// gzip解压缩
		String str=ContentEncoding;
		if ( str != null) {
			if ( str.toLowerCase().indexOf("gzip")>=0 ) {		// 是gzip压缩方式，需要先解压缩
				html_buffer=decompressGzip(html_buffer);		// 解压缩
				System.out.println("gzip unzip OK.");
			}
		}
		// gzip解压缩
		
		// zlib（wsz格式）解压缩
		if ( boolWSZ  && html_buffer.length>=4) {		// 是wsz压缩方式，需要先解压缩
			// AppendToFile("d:/temp/wsTest.wsz",html_buffer,0,html_buffer.length);		// 原始wsz数据可追加存储到文件，这个是二进制压缩数据
			html_buffer=decompressWSZ(html_buffer);		// wsz格式解压缩
			encoding="GBK";				// wsz格式是二进制数据，其中的“股票中文简称”采用的是GBK编码。因此设置编码方式为GBK
			System.out.println("wsz unzip OK.");
		}
		// zlib（wsz格式）解压缩

		if (filename.length()>0 && html_buffer.length > 0) {		// 存储到文件
			AppendToFile(filename,html_buffer,0,html_buffer.length);		// 追加存储到文件
			System.out.println("下载的数据已存储到文件：" + filename);
		} else {			// 显示下载的内容
			// 提取 编码方式
			if ( encoding.length()<2 ) {
				str=ContentType; if (str!=null ) { str=getCharset(str); if (str!=null ) { encoding=str;	} }  // 如果用户没有手动指定编码方式，则尝试从服务器端返回数据中提取 charset
			} 
			if ( encoding.length()<2 ) {		// 尝试从数据中提取 字符集，例如 html中的 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
				int i=1024; if (i>html_buffer.length) {i=html_buffer.length;} 	str = new String(html_buffer, 0, i);		// 这里将 字节数组最多前面1K字节 转化为 字符串，直接按默认字符集转化，以便提取charset
				if (str!=null ) { str=getCharset(str); if (str!=null ) { encoding=str;	} }
			}
			if ( encoding.length()<2 ) { encoding="utf-8"; }				// 如果无法正确提取到编码方式，则默认为utf-8
			System.out.println("encoding:" + encoding);
			// 提取 编码方式

			try {
				str = new String(html_buffer, 0, html_buffer.length, encoding);		// 这里将 字节数组 转化为 字符串，必须指定正确的编码方式（encoding）
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			System.out.println(str);
		}
		return 0;
	}

	// 参数1：URL，如果缺省，则默认为  http://test.wstock.cn/cgi-bin/wsRTAPI/wsr2.asp?t=n&m=市场&u=用户名&p=密码
	//		 		可用市场、用户名、密码，可来信申请。
	//				m=市场： 可通过英文逗号分隔不同的市场，可在一个链接中，同时获取多个市场的实时行情
	//				t=n：获取最新实时行情。如果更改为t=i，则可获取到对应的全市场全部瓶中的快照行情（t=i，仅用于获取全市场快照，一天最多只能获取100次）
	// 参数2：编码方式，例如 utf-8 （可缺省，例如输出一个字符a）；
	// 参数3：存储到文件（含路径名），例如 d:/temp/wsz.txt  如果缺省，则在命令行中直接输出，不存储
	// 参数4：指定一个品种代码（股票代码）；如果指定，则在命令行中显示该股票（品种）的行情。
	// wstock.net  2015/10/09
	public static void main(String args[]){
		String tURL="http://test.wstock.cn/cgi-bin/wsRTAPI/wsr2.asp?m=SH&u=u3317&p=it2018&t=n";		// 微盛推送式HTTP API接口 			// 微盛请求式API接口类似为："http://db2015.wstock.cn/wsDB_API/stock.php?market=SH60&r_type=2";
		String encoding="a";		// utf-8
		String filename= Constant.APP_UPLOAD_PATH + "/wsTCP_DATA/" + "wsSample.dat";
		int i=0;
		
		if (args.length>=1) { if (args[0].length()>1) { tURL=args[0];} }
		if (args.length>=2) { if (args[1].length()>1) { encoding=args[1];} }
		if (args.length>=3) {filename=args[2];}
		if (args.length>=4) {ViewCode=args[3];}			// 可在此处设置，需要显示输出的代码

		if (tURL.toLowerCase().indexOf("http")<0) {tURL="http://"+tURL;}		// 增加默认协议头
		System.out.println("URL:" + tURL);
		
		// 通过URL特征，来判断是否为 微盛投资 推送式HTTP URL API接口
		boolWSZ=false; if ( tURL.toLowerCase().indexOf("wstock.cn") >0 && tURL.toLowerCase().indexOf("wsr2.asp") >0 && tURL.indexOf("u=")>0 && tURL.indexOf("p=")>0 && tURL.indexOf("m=")>0 ) {boolWSZ=true;}
		
		// 对于微盛推送式API接口（且不是t=i的快照请求数据），则每300毫秒 循环下载一次，不断获取微盛推送式实时行情
		if (boolWSZ && tURL.indexOf("t=i")<0) {
			while (true) {
				try {
					Thread.sleep(300);							// 300毫秒请求一次
				} catch (InterruptedException e) {
					e.printStackTrace();
				}			
				wsDownload(tURL,encoding,filename);			// 获取微盛推送式实时行情
			}
		} else {
			wsDownload(tURL,encoding,filename);			// 其余情况，则只下载一次对应网址（URL）的数据
		}
	}
}
