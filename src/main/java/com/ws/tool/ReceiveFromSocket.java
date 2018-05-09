package com.ws.tool;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import com.cjy.qiquan.utils.Constant;

/**
 * 运行socket接收行情
 * @author chenjiyin
 *
 */
public class ReceiveFromSocket implements Runnable {

	@Override
	public void run() {
		String srcIP = "121.41.58.181"; // 服务器端 IP地址
		int srcPort = 22536; // 服务器端 端口号
		int getSize = 0; // 用于判断实际读取到的字节数
		java.util.Date newTime = new Date(); // 当前最新时间
		java.util.Date rcvTime = new Date(); // 收到行情的最新时间
		Socket s = null;
		String userName = "u3317";
		String passWord = "it2018";
		String SC = "dc";
		try {
			s = new Socket(InetAddress.getByName(srcIP), srcPort);
			// 往服务器写数据
			OutputStream os = s.getOutputStream();
			String socketpu = "m=" + SC + ";u=" + userName + ";p=" + passWord + ";EX_SIZE=1;t=i"; // 这里是您的连接串参数
			System.out.println("socketpu:"+socketpu);
			os.write(socketpu.getBytes());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

		// 获取服务器的反馈
		byte[] recBuffer = new byte[10240]; // 每次准备读10240字节
		InputStream is = null;
		try {
			is = s.getInputStream();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		DataInputStream in = new DataInputStream(new BufferedInputStream(is));
		while (true) {
			try {
				newTime = new Date();
				long diffTime = (newTime.getTime() - rcvTime.getTime()) / 1000; // 二者时间差（秒）
				if (diffTime > 120) { // 120秒没有收到行情了，可认为行情异常，因此需要断线重连。退出本程序，重启本程序
					in.close();
					s.close(); // 关闭连接

					// Process proc = Runtime.getRuntime().exec(cmd); // 启动自己这个java程序，可使用一个命令行参数
					// auto，有auto参数时，程序启动后自动连接
					// 我方对 java 不熟悉， java 中启动程序，可能就是 上面那一条语句，但贵方可再核实
					// System.exit(0); // 退出程序
				}

				java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("yyyyMMdd");
				String wszFile = Constant.APP_UPLOAD_PATH + "/wsTCP_DATA/wsWA_" + f.format(new java.util.Date())
						+ ".wsz"; // 实际使用时，文件名中很可能包括日期，例如一天一个对应文件，则零点日期会变更。所以在循环中每次获取最新时间
				getSize = in.read(recBuffer); // 实际读取数据；准备读取 recBuffer.length 字节的数据，实际只读取到 getSize字节的数据 //
												// System.out.println(getSize);
				if (getSize > 0) {
					System.out.println("收到数据:" + getSize);
					rcvTime = new Date(); // 标记，收到行情的最新时间
					AppendToFile(wszFile, recBuffer, getSize); // 将getBuffer中的前面getSize字节追加写入到对应的文件中
					// 【重要说明】；这种方式（本程序只接收并写入文件；另外一个程序则从文件中读取数据并解压、转码处理）
					// 简单可靠，思路清晰，对性能影响也不足10ms（0.01秒）
					// 如果需要在同一程序中进行接收 及 处理，可参见我方的C# demo 源码。则此处应该将
					// getBuffer中前面getSize复制到一个大的缓存（例如20MB）中，并对该缓存进行管理（复制到尾后再复制到头），读取数据的代码则从这个大的缓存中读取数据
				}
			} catch (Exception e) {
				e.printStackTrace();
				// 可以将异常记录到日志文件中

				// 出现异常，可中断连接，启动本程序，然后重新连接
				try {
					in.close();
					s.close(); // 关闭连接
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// Process proc = Runtime.getRuntime().exec(cmd); // 启动自己这个java程序，可使用一个命令行参数
				// auto，有auto参数时，程序启动后自动连接
				// 我方对 java 不熟悉， java 中启动程序，可能就是 上面那一条语句，但贵方可再核实
				// System.exit(0); // 退出程序
			}
		}
	}

	public void AppendToFile(String filename, byte[] content, int getSize) throws IOException {
		if (getSize > 0) {
			FileOutputStream fos = new FileOutputStream(filename, true); // true为追加方式写入，public FileOutputStream(String
																			// name,boolean append)
			fos.write(content, 0, getSize); // void write(byte[] b, int off, int len) 将指定 byte 数组中从偏移量 off 开始的 len
											// 个字节写入此文件输出流。
			fos.close();

		}
	}

}
