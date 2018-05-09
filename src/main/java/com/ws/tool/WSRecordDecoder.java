package com.ws.tool;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Date;

import com.ws.util.DataUtils;

/**
 * WS Record decoder.
 * @author muyinliu
 * @date 20150320
 */
public class WSRecordDecoder {
	
	/**
	 * Decode wsz record.
	 * 
	 * @param bytes
	 *            156bytes
	 * @return WSRecord object
	 * @throws Exception
	 */
	public static WSRecord decodeRecord(byte[] bytes) throws Exception {
		ByteArrayInputStream byteArrayInputStream =
				new ByteArrayInputStream(bytes);
		DataInputStream unpackDataInputStream =
				new DataInputStream(byteArrayInputStream);

		WSRecord wsRecord = new WSRecord();

		Date time =
				new Date(
						(long) DataUtils.reverseInt(unpackDataInputStream.readInt()) * 1000);
		byte[] marketStockCodeBytes = new byte[12];
		unpackDataInputStream.read(marketStockCodeBytes);
		String szLabel = new String(marketStockCodeBytes);
		byte[] stockNameBytes = new byte[16];
		unpackDataInputStream.read(stockNameBytes);
		String szName = new String(stockNameBytes, "GBK");
		float fPrice3 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fVol2 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fOpen_Int = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fPrice2 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fLastClose = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fOpen = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fHigh = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fLow = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fNewPrice = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fVolume = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fAmount = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fBuyPrice1 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fBuyPrice2 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fBuyPrice3 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fBuyPrice4 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fBuyPrice5 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fBuyVolume1 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fBuyVolume2 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fBuyVolume3 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fBuyVolume4 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fBuyVolume5 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fSellPrice1 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fSellPrice2 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fSellPrice3 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fSellPrice4 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fSellPrice5 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fSellVolume1 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fSellVolume2 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fSellVolume3 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fSellVolume4 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());
		float fSellVolume5 = DataUtils.reverseFloat(unpackDataInputStream.readFloat());

		wsRecord.setSzLabel(szLabel);
		wsRecord.setSzName(szName);
		wsRecord.setfPrice3(fPrice3);
		wsRecord.setfVol2(fVol2);
		wsRecord.setfOpen_Int(fOpen_Int);
		wsRecord.setfPrice2(fPrice2);
		wsRecord.setfLastClose(fLastClose);
		wsRecord.setfOpen(fOpen);
		wsRecord.setfHigh(fHigh);
		wsRecord.setfLow(fLow);
		wsRecord.setfNewPrice(fNewPrice);
		wsRecord.setfVolume(fVolume);
		wsRecord.setfAmount(fAmount);
		wsRecord.setfBuyPrice1(fBuyPrice1);
		wsRecord.setfBuyPrice2(fBuyPrice2);
		wsRecord.setfBuyPrice3(fBuyPrice3);
		wsRecord.setfBuyPrice4(fBuyPrice4);
		wsRecord.setfBuyPrice5(fBuyPrice5);
		wsRecord.setfBuyVolume1(fBuyVolume1);
		wsRecord.setfBuyVolume2(fBuyVolume2);
		wsRecord.setfBuyVolume3(fBuyVolume3);
		wsRecord.setfBuyVolume4(fBuyVolume4);
		wsRecord.setfBuyVolume5(fBuyVolume5);
		wsRecord.setfSellPrice1(fSellPrice1);
		wsRecord.setfSellPrice2(fSellPrice2);
		wsRecord.setfSellPrice3(fSellPrice3);
		wsRecord.setfSellPrice4(fSellPrice4);
		wsRecord.setfSellPrice5(fSellPrice5);
		wsRecord.setfSellVolume1(fSellVolume1);
		wsRecord.setfSellVolume2(fSellVolume2);
		wsRecord.setfSellVolume3(fSellVolume3);
		wsRecord.setfSellVolume4(fSellVolume4);
		wsRecord.setfSellVolume5(fSellVolume5);
		wsRecord.setTime(time);
		return wsRecord;
	}
}
