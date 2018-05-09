package com.cjy.qiquan.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cjy.qiquan.exception.AppServiceException;
import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.Constant;
import com.cjy.qiquan.utils.ImageUtil;
import com.cjy.qiquan.utils.RandomUtil;
import com.cjy.qiquan.utils.StatesUtils;

public class Uploader {

	private static final Logger Out = Logger.getLogger(Uploader.class);
	// 输出文件地址
	private String url = "";
	// 上传文件名
	private String fileName = "";
	
	private String thumfileName="";
	// 状态
	private String state = "";
	// 文件类型
	private String type = "";
	// 原始文件名
	private String originalName = "";
	// 文件大小
	private long size = 0;

	private HttpServletRequest request = null;
	private String title = "";

	
	private String subFolder;
	
	// 保存路径
	private String savePath = "upload";
	// 文件大小限制，单位KB
	private int maxSize = 15111808;

	private HashMap<String, String> errorInfo = new HashMap<String, String>();

	private String mFile;

	public Uploader(HttpServletRequest request) {
		this.request = request;

		HashMap<String, String> tmp = this.errorInfo;
		tmp.put("SUCCESS", "SUCCESS"); // 默认成功
		tmp.put("NOFILE", "未包含文件上传域");
		tmp.put("TYPE", "不允许的文件格式");
		tmp.put("SIZE", "文件大小超出限制");
		tmp.put("ENTYPE", "请求类型ENTYPE错误");
		tmp.put("REQUEST", "上传请求异常");
		tmp.put("IO", "IO异常");
		tmp.put("DIR", "目录创建失败");
		tmp.put("UNKNOWN", "未知错误");

	}

	public void upload() throws Exception {

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile(mFile);
		if (file.isEmpty()) {
			Out.debug("nofile");
			this.state = this.errorInfo.get("NOFILE");
			return;
		}
		
		String w = request.getParameter("w");
		String h = request.getParameter("h");
		String x = request.getParameter("x");
		String y = request.getParameter("y");
		float o_w = 0.00f,o_h=0.00f,o_x=0.00f,o_y=0.00f;
		
		if (!CommonUtils.isBlankOrNull(w)){
			o_w = Float.parseFloat(w);
			o_h = Float.parseFloat(h);
			o_x = Float.parseFloat(x);
			o_y = Float.parseFloat(y);
		}
		
		if (file.getSize() > maxSize) {
			Out.debug("allow error");
			this.state = this.errorInfo.get("SIZE");
			return;
		}

		String subFolder = getSubFolder();
		
		String savePath = this.getFolder(this.savePath,subFolder);
		this.fileName = this.getName(file.getOriginalFilename());
		this.thumfileName = ImageUtil.DEFAULT_THUMB_PREVFIX + this.fileName;
		this.originalName = file.getOriginalFilename();
		this.type = this.getFileExt(this.fileName);
		this.size = file.getSize();
		String uri = savePath + "/" + fileName;
		this.url = Constant.APP_UPLOAD_URL + (Constant.APP_UPLOAD_URL.endsWith("/")?"":"/") + subFolder +"/"+ thumfileName;
		String ext = this.getFileExt(this.fileName);
		if (!Constant.allowType.contains(ext.toLowerCase())) {
			Out.debug("allow error");
			this.state = this.errorInfo.get("TYPE");
			return;
		}

		File newFile = new File(uri);
		try {
			file.transferTo(newFile);
			ImageUtil imageUtil = new ImageUtil();
			if (o_w>0){
				//是需要裁切
				imageUtil.cutImage(newFile.getAbsolutePath(), newFile.getAbsolutePath() ,(int)o_x,(int)o_y,(int)o_w,(int)o_h);
			}else{
				imageUtil.thumbnailImage(newFile.getAbsolutePath(), 640, 480, false);
			}
			
			state = this.errorInfo.get("SUCCESS");
		} catch (IllegalStateException e) {
			throw new AppServiceException(StatesUtils.States.upload_file_error, e.getMessage());
		} catch (IOException e) {
			throw new AppServiceException(StatesUtils.States.upload_file_error, e.getMessage());
		} finally {
			newFile.delete();
		}
	}
	
	public void uploadOrign() throws Exception {

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile(mFile);
		if (file.isEmpty()) {
			Out.debug("nofile");
			this.state = this.errorInfo.get("NOFILE");
			return;
		}
		
		
//		if (file.getSize() > maxSize) {
//			Out.debug("allow error");
//			this.state = this.errorInfo.get("SIZE");
//			return;
//		}

		String subFolder = getSubFolder();
		
		String savePath = this.getFolder(this.savePath,subFolder);
//		this.fileName = this.getName(file.getOriginalFilename());
		this.fileName = file.getOriginalFilename();
		this.originalName = file.getOriginalFilename();
		this.type = this.getFileExt(this.fileName);
		this.size = file.getSize();
		String uri = savePath + "/" + fileName;
		this.url = Constant.APP_UPLOAD_URL + (Constant.APP_UPLOAD_URL.endsWith("/")?"":"/") + subFolder +"/"+ fileName;
//		String ext = this.getFileExt(this.fileName);
//		if (!Constant.allowType.contains(ext.toLowerCase())) {
//			Out.debug("allow error");
//			this.state = this.errorInfo.get("TYPE");
//			return;
//		}

		File newFile = new File(uri);
		try {
			file.transferTo(newFile);
			state = this.errorInfo.get("SUCCESS");
		} catch (IllegalStateException e) {
			throw new AppServiceException(StatesUtils.States.upload_file_error, e.getMessage());
		} catch (IOException e) {
			throw new AppServiceException(StatesUtils.States.upload_file_error, e.getMessage());
		} finally {
		}
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @return string
	 */
	private String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 依据原始文件名生成新文件名
	 * 
	 * @return
	 */
	private String getName(String fileName) {
		return this.fileName = "" + RandomUtil.getRandomTxt(8) + System.currentTimeMillis()
				+ this.getFileExt(fileName);
	}

	/**
	 * 根据字符串创建本地目录 并按照日期建立子目录返回
	 * 
	 * @param path
	 * @return
	 */
//	private String getFolder(String path) {
//		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
//		path += "/" + formater.format(new Date());
//		File dir = new File(this.getPhysicalPath(path));
//		if (!dir.exists()) {
//			try {
//				dir.mkdirs();
//			} catch (Exception e) {
//				this.state = this.errorInfo.get("DIR");
//				return "";
//			}
//		}
//		return path;
//	}
	
	private String getFolder(String path,String subFolder) {
		path += "/" + subFolder;
		File dir = new File(path);
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				this.state = this.errorInfo.get("DIR");
				return "";
			}
		}
		return path;
	}
	
	
	private String getSubFolder(){
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
		return formater.format(new Date());
	}
	
	
	public void setSubFolder(String subFolder) {
		this.subFolder = subFolder;
	}

	/**
	 * 根据传入的虚拟路径获取物理路径
	 * 
	 * @param path
	 * @return
	 */
	@Deprecated
	private String getPhysicalPath(String path) {
		String servletPath = this.request.getServletPath();
		String realPath = this.request.getSession().getServletContext().getRealPath(servletPath);
		return new File(realPath).getParent() + "/" + path;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public void setMaxSize(int size) {
		this.maxSize = size;
	}

	public long getSize() {
		return this.size;
	}

	public String getUrl() {
		return this.url;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getState() {
		return this.state;
	}

	public String getTitle() {
		return this.title;
	}

	public String getType() {
		return this.type;
	}

	public String getOriginalName() {
		return this.originalName;
	}

	public String getmFile() {
		return mFile;
	}

	public void setmFile(String mFile) {
		this.mFile = mFile;
	}

	public String getSavePath() {
		return savePath;
	}
	
	

}
