package com.ws.tool;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;

import com.ws.util.DataUtils;
import com.ws.util.ZLibUtils;

/**
 * Unpack *.wsz file.
 * 
 * @author muyinliu
 * @date 20150320
 */
public class UnpackWSZFile {

	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static void unpackWSZFile(File file) throws Exception {
		long fileLength = file.length();
		FileInputStream inputStream = new FileInputStream(file);
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		long position = 0;

		while (position < fileLength) {
			int packDataLength =
					DataUtils.reverseInt(dataInputStream.readInt()) - 4;
			position += 4;
			int unpackDataLength =
					DataUtils.reverseInt(dataInputStream.readInt());
			position += 4;

			byte[] packDataBytes = new byte[packDataLength];
			dataInputStream.read(packDataBytes);
			position += packDataLength;

			/* decompress zlib data */
			byte[] unpackDataBytes = ZLibUtils.decompress(packDataBytes);

			if (unpackDataBytes.length == unpackDataLength) {
				/* decompress OK */
				for (int i = 0; i < unpackDataBytes.length; i += 156) {
					byte[] recordBytes = new byte[156];
					for (int j = 0; j < recordBytes.length; j++) {
						recordBytes[j] = unpackDataBytes[j + i];
					}
					WSRecord wsRecord =
							WSRecordDecoder.decodeRecord(recordBytes);

					// TODO just for testing, will be deleted
					String time = dateFormat.format(wsRecord.getTime());
					String recordString = String.format("%s,%s,%s,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f", 
							time,
							wsRecord.getSzLabel(), 
							wsRecord.getSzName(),
							wsRecord.getfPrice3(), 
							wsRecord.getfVol2(),
							wsRecord.getfOpen_Int(), 
							wsRecord.getfPrice2(),
							wsRecord.getfLastClose(), 
							wsRecord.getfOpen(),
							wsRecord.getfHigh(), 
							wsRecord.getfLow(),
							wsRecord.getfNewPrice(), 
							wsRecord.getfVolume(),
							wsRecord.getfAmount(), 
							wsRecord.getfBuyPrice1(),
							wsRecord.getfBuyPrice2(), 
							wsRecord.getfBuyPrice3(),
							wsRecord.getfBuyPrice4(), 
							wsRecord.getfBuyPrice5(),
							wsRecord.getfBuyVolume1(),
							wsRecord.getfBuyVolume2(),
							wsRecord.getfBuyVolume3(),
							wsRecord.getfBuyVolume4(),
							wsRecord.getfBuyVolume5(),
							wsRecord.getfSellPrice1(),
							wsRecord.getfSellPrice2(),
							wsRecord.getfSellPrice3(),
							wsRecord.getfSellPrice4(),
							wsRecord.getfSellPrice5(),
							wsRecord.getfSellVolume1(),
							wsRecord.getfSellVolume2(),
							wsRecord.getfSellVolume3(),
							wsRecord.getfSellVolume4(),
							wsRecord.getfSellVolume5());
					System.out.println(recordString);

					// TODO now have wsRecord object,
					// can export to *.txt or *.csv yourself
				}
			} else {
				/* decompress failure */
				System.err.println("Decompress data error from position "
						+ (position - packDataLength) + " to " + position);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		unpackWSZFile(new File("./wsSample.wsz"));
	}
}
