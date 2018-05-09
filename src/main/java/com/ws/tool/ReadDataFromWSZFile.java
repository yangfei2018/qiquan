package com.ws.tool;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cjy.qiquan.cache.CacheManager;
import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.Constant;
import com.ws.util.FormatTransfer;
import com.ws.util.ZLibUtils;

public class ReadDataFromWSZFile implements Runnable {

	@Override
	public void run() {
		int dwZipSize = 0;
		int dwOriSize = 0;
		int size = 0;
		int EX_SIZE = 0;
		int getEX_SIZE = 0; // 实际读到的长度

		DataInputStream in = null;

		int fileSize = 0; // 刚刚打开文件时，这个就是文件总长度，是在变动的（因为另外一个程序在不断写入该文件）
		int fileSkip = 0; // 文件中已读取长度。这个变量的值，建议存储到一个参数文件中。这样，如果本程序中断了，则重启本程序后，可不必再处理已处理部分的数据，而只是接着处理之前未处理部分的数据
		fileSkip=getMyfileSkip(); //
		// 这里是一个假设的子程序，从该函数中读取到之前已处理的文件长度。这样可支持异常退出后的接续处理，而不用每次都从文件头开始处理

		while (true) { // 这个循环是不断读取文件（每次间隔 50毫秒 (0.05秒) ）
			try {
				Thread.sleep(50);

				java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("yyyyMMdd");
				// String strDate = f.format(new java.util.Date());
				String wszFile = Constant.APP_UPLOAD_PATH + "/wsTCP_DATA/wsWA_" + f.format(new java.util.Date())
						+ ".wsz"; // 实际使用时，文件名中很可能包括日期，例如一天一个对应文件
//				String wszFile = Constant.APP_UPLOAD_PATH + "/wsTCP_DATA/" + "wsSample.wsz";
									// 因此，文件名应该放入到循环中，这样才能得到最新的文件名（因为日期在零点会变动）
				// String datFile = "d:/wsTCP_DATA/wsWA_" + strDate + ".dat";
//				System.out.println("读取文件："+wszFile);
				try {
					// 因为文件名中可能包括日期，所以文件是动态的，所以需要在循环中打开文件
					// 因为是在循环中打开文件的，所以一定要在下一次循环之前，in.close();
					in = new DataInputStream(new BufferedInputStream(new FileInputStream(wszFile))); // 因为是在循环中打开文件中，因此后面的代码一定要在
																										// 下一循环之前，in.close
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Thread.sleep(500);
					continue; // 也许只是日期更换时，对应文件尚未创建，因此0.5秒之后再试
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						in.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Thread.sleep(8000);
					continue; // IO异常，这里应该记录日志，然后break; 然后发出提醒（例如送出邮件、短信等）；
								// 但也可能只是服务器IO临时异常（例如进行大文件的备份，影响到整个系统IO），因此也可等待稍微长一点时间，例如8秒后再试
								// 因为已经IO异常了，所以这里可稍微等候时间长一点，例如等待8秒；过短的间隙，会加重IO异常（此时已经IO异常了）
				}

				try {
					fileSize = in.available(); // 文件中，当前可读取的长度；从资料来看，java的 available() 返回的已考虑了之前读取的位置
												// 注意：java的available 并非文件长度，但文件刚刚打开时，就是文件长度
												// 由于文件名中包括日期（日期在变动），所以只能在循环中打开，每次都是刚打开文件，所以这个数据就成为了文件长度
					if (fileSkip > fileSize) {
						fileSkip = 0;
					} // 文件跳过长度 如果大于 文件本身的长度，说明很可能是文件名变动了。因此将 fileSkip重新设置为0（已经是另外一个新文件了）
					in.skipBytes(fileSkip); // 跳过之前已处理部分

					while ((fileSize - fileSkip) >= 4) { // 文件中至少有4字节，用于读取 EX_SIZE；这个循环用于
															// 将打开的文件，一直处理到文件末尾（已打开文件中，循环处理每一个报文）
						EX_SIZE = in.readInt();
						EX_SIZE = reverseInt(EX_SIZE);
						if ((fileSize - fileSkip) < (EX_SIZE + 4)) {
							break;
						} // 文件中没有读取部分，不足一个报文长度，则不处理，跳出本次循环

						fileSkip = fileSkip + EX_SIZE + 4; // 直接设置到下一报文头部。这样如果下面的压缩块中出现解压异常等情况，则可直接从下一报文头开始处理。
															// EX_SIZE没有包括自身的4字节，所以这里加上自身的4字节
						saveMySkip(fileSkip); // 这里可调用一个函数，该函数将
						// fileSkip存储到一个参数文件中。这样可方便万一异常退出，则下次直接从下一报文处开始处理；而不用从文件头处理

						// 经过以上判断，文件中肯定包括有一个报文长度，所以下面的代码，可循环处理这个报文中的每个压缩块
						getEX_SIZE = 0; // 设置为0
						while (getEX_SIZE < EX_SIZE) { // 这个循环处理报文中每一个压缩块
							dwZipSize = in.readInt();
							getEX_SIZE += 4; // 读取了4个字节，所以 getEX_SIZE加上4
							dwZipSize = reverseInt(dwZipSize); // 压缩块长度

							if (dwZipSize > 16777216 || dwZipSize <= 0) { // 信息包
								byte[] msgBuffer1 = new byte[EX_SIZE - 4]; // 报文长度，但已读取了4字节的dwZipSize，因此，这里是 EX_SIZE-4
								size = in.read(msgBuffer1);
								getEX_SIZE += size; // 又读取了size个字节，所以 getEX_SIZE再加上size
								// 如果需要，此处可将信息包 msgBuffer1，存储到日志文件中
							} else {
								if (dwZipSize > (EX_SIZE - getEX_SIZE)) { // dwZipSize 应该在 剩余长度 之内，如果
																			// dwZipSize不正确，则读出该报文的剩余数据（这样读取文件的下一位置才正确），然后跳出循环，直接处理下一报文；
									if ((EX_SIZE - getEX_SIZE) > 0) {
										byte[] tempBuffer1 = new byte[EX_SIZE - getEX_SIZE];
										size = in.read(tempBuffer1);
										getEX_SIZE += size;
									} // 又读取了size个字节，所以 getEX_SIZE再加上size
									break; // 退出本报文循环，处理下一报文
								}

								dwOriSize = in.readInt();
								getEX_SIZE += 4; // 又读取了4个字节，所以 getEX_SIZE再加上4
								dwOriSize = reverseInt(dwOriSize); // 解压缩后原始长度

								byte[] zipBuffer1 = new byte[dwZipSize - 4]; // dwZipSize-4，是实际的压缩块
								size = in.read(zipBuffer1);
								getEX_SIZE += size; // 又读取了size个字节，所以 getEX_SIZE再加上size
								// System.out.println("Test03:size::"+size);

								byte[] oriBuffer1 = ZLibUtils.decompress(zipBuffer1); // 解压缩，ZLibUtils代码可参见：
																						
								
								System.out.println("oriBuffer1.length:"+oriBuffer1.length+",dwOriSize:"+dwOriSize);
								
								if (oriBuffer1.length == dwOriSize) { // 解压缩后长度相等，可认为解压缩正确
									// 这里可调用一个函数（子程序），处理 oriBuffer1（oriBuffer1已是解压缩后的二进制记录数据了），可以调用一个函数（子程序），将
									// oriBuffer1 转化为txt，例如写入数据库等
									System.out.println("开始处理压缩信息");
									WSRecord wsRecord = WSRecordDecoder.decodeRecord(oriBuffer1);

									// TODO just for testing, will be deleted
									String time = dateFormat.format(wsRecord.getTime());
									String recordString = String.format(
											"%s,%s,%s,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f",
											time, wsRecord.getSzLabel(), wsRecord.getSzName(), wsRecord.getfPrice3(),
											wsRecord.getfVol2(), wsRecord.getfOpen_Int(), wsRecord.getfPrice2(),
											wsRecord.getfLastClose(), wsRecord.getfOpen(), wsRecord.getfHigh(),
											wsRecord.getfLow(), wsRecord.getfNewPrice(), wsRecord.getfVolume(),
											wsRecord.getfAmount(), wsRecord.getfBuyPrice1(), wsRecord.getfBuyPrice2(),
											wsRecord.getfBuyPrice3(), wsRecord.getfBuyPrice4(),
											wsRecord.getfBuyPrice5(), wsRecord.getfBuyVolume1(),
											wsRecord.getfBuyVolume2(), wsRecord.getfBuyVolume3(),
											wsRecord.getfBuyVolume4(), wsRecord.getfBuyVolume5(),
											wsRecord.getfSellPrice1(), wsRecord.getfSellPrice2(),
											wsRecord.getfSellPrice3(), wsRecord.getfSellPrice4(),
											wsRecord.getfSellPrice5(), wsRecord.getfSellVolume1(),
											wsRecord.getfSellVolume2(), wsRecord.getfSellVolume3(),
											wsRecord.getfSellVolume4(), wsRecord.getfSellVolume5());
									System.out.println(recordString);

								} else {
									// 解压缩不正确，则读出该报文的剩余数据（这样读取文件的下一位置才正确），然后跳出循环，直接处理下一报文；
									if ((EX_SIZE - getEX_SIZE) > 0) {
										byte[] tempBuffer2 = new byte[EX_SIZE - getEX_SIZE];
										size = in.read(tempBuffer2);
										getEX_SIZE += size;
									} // 又读取了size个字节，所以 getEX_SIZE再加上size
									break; // 退出本报文循环，处理下一报文
								}
							}
						}
					}
					in.close(); // 记得关闭文件哦（循环中，下次会重新打开文件并读写）
					in = null; // 我方对java不熟悉，也许这里应该将 in 设置为 null
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}

			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} // 等待50毫秒（0.05秒），以免不断读取文件

		}

	}

	public static int reverseInt(int i) {
		int result = FormatTransfer.hBytesToInt(FormatTransfer.toLH(i));
		return result;
	}
	
	
	private int getMyfileSkip() {
		
		java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("yyyyMMdd");
		String key = f.format(new Date(System.currentTimeMillis()));
		
		String v = CacheManager.instance.getCache("MYFILIESKIP", key);
		if (CommonUtils.hasText(v)) {
			return Integer.valueOf(v);
		}else {
			return 0;
		}
		
	}
	
	
	private void saveMySkip(int fileSkip) {
		java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("yyyyMMdd");
		String key = f.format(new Date(System.currentTimeMillis()));
		
		CacheManager.instance.addCache("MYFILIESKIP", key,String.valueOf(fileSkip));
	}

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
