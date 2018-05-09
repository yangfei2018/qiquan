package com.cjy.qiquan.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cjy.qiquan.model.UserSession;
import com.cjy.qiquan.utils.Constant;
import com.cjy.qiquan.utils.SpringApplicationContext;
import com.cjy.qiquan.web.utils.CookieUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 上下文
 * @author chenjiyin
 *
 */
public class WebContext extends SpringApplicationContext {
	
	private static final Logger log = Logger.getLogger(WebContext.class);   
	private static final ThreadLocal<Client> clientContext = new ThreadLocal<Client>();
	public static final AtomicLong REQUEST_COUTN = new AtomicLong();
	
	WebContext(){}
	
	public synchronized static void initApplicationContext(){
		if(applicationContext==null){
			ServletContext servletContext = getServletContext();
			if(servletContext==null){
				SpringApplicationContext.initApplicationContext();
				return;
			}
			if(servletContext!=null && applicationContext==null) {
				applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
				log.info("Initaling AppUtil.CONTEXT from Spring WEB...");
			}
		}
	}
	
	public static void setClient(HttpServletRequest request, HttpServletResponse response){
//		if(!Constant.encoding.equals(response.getCharacterEncoding())){
//			response.setContentType("text/html;charset="+Constant.encoding); 
//		}
//		if(!Constant.encoding.equals(request.getCharacterEncoding())){
//			try {
//				request.setCharacterEncoding(Constant.encoding);
//			} catch (UnsupportedEncodingException e) {
//			} 
//		}
		REQUEST_COUTN.incrementAndGet();
		clientContext.set(new Client(request, response));
		log.debug("添加一个请求...");
	}
	
	public static void releaseClient(){
		clientContext.set(null);
		clientContext.remove();
		log.debug("释放一个请求...");
	}

	public static HttpServletRequest getRequest() {
		if(clientContext.get()==null)
			throw new NullPointerException();
		return clientContext.get().request;
	}

	public static HttpServletResponse getResponse() {
		if(clientContext.get()==null)
			throw new NullPointerException();
		return clientContext.get().response;
	}

	public static HttpSession getSession() {
		return getRequest().getSession();
	}
	
	
	public static UserSession getUserLoginInfo(){
		if (WebContext.getSession().getAttribute("currentUser")==null){
			return null;
		}
		else{
			return (UserSession)WebContext.getSession().getAttribute("currentUser");
		}
		
	}
	
	public static ServletContext getServletContext(){
		return getSession().getServletContext();
	}


	public static void disConnection() {
		getSession().invalidate();
	}
	
	public static Object findAttribute(String name) {
		if (null != getRequest().getAttribute(name)) {
			return getRequest().getAttribute(name);
		} else if (null != getSession().getAttribute(name)) {
			return getSession().getAttribute(name);
		} else if (null != getServletContext().getAttribute(name)) {
			return getServletContext().getAttribute(name);
		}
		return null;
	}
	
	public static String getClientIP(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip==null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = "1.1.1.1";
		}
		return ip; 
	}

	public static String getClientIP(){
		return getClientIP(getRequest());
	}

	public static String getSessionId(){
		return getSession().getId();
	}
	
	public static String getParameter(String name) {
		return getRequest().getParameter(name);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getAttribute(String key) {
		return (T) getSession().getAttribute(key);
	}
	
	public static void removeAttribute(String key) {
		getSession().removeAttribute(key);
	}
	
	public static void putAttribute(String key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static PrintWriter getOut() throws IOException {
		return getResponse().getWriter();
	}
	
	public static void send(String msg){
		getResponse().setContentType("text/html;;charset="+Constant.encoding); 
		try {
			getOut().write(msg == null ? "" : msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendJson(String json) throws IOException {
		getResponse().setContentType("text/json;charset="+Constant.encoding); 
		getOut().write(json == null ? "{}" : json);
	}
	
	public static void sendJson(Object json){
		getResponse().setContentType("text/json;charset="+Constant.encoding);
		try {
			getOut().write(json == null ? "{}" : new ObjectMapper().writeValueAsString(json));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendFile(String filename, byte[] data) throws IOException {
		HttpServletResponse response = getResponse();
//		response.setContentType("application/x-download"); 
		response.setContentType("application/octet-stream"); 
		// attachment:会出现文件下载对话框
		// inline:会在Web浏览器中打开
		response.addHeader("Content-Disposition", "attachment;filename="+filename);
		response.addHeader("Content-Length", String.valueOf(data.length));
		response.getOutputStream().write(data);
	}

	public static String getRootPath() {
		return getServletContext().getRealPath(File.separator);
	}
	
	public static void setCookie(String name, String value){
		CookieUtils.addCookie(getRequest(), getResponse(), name, value);
	}
	
	public static void setCookie(String name,Object vaObject){
		try {
			CookieUtils.addCookie(getRequest(), getResponse(), name, new ObjectMapper().writeValueAsString(vaObject));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String getCookie(String name){
		Cookie cookie = CookieUtils.getCookie(getRequest(), name);
		return cookie == null ? null : cookie.getValue();
	}

	public static void delCookie(String name){
		CookieUtils.delCookie(getRequest(), getResponse(), name);
	}
	
	private static class Client {
		public HttpServletRequest request;
		public HttpServletResponse response;

		public Client(HttpServletRequest request, HttpServletResponse response) {
			this.request = request;
			this.response = response;
		}
	}
	
	public static byte getByte(String param) {
		String value = getString(param);
		return StringUtils.isEmpty(value)? 0 : Byte.valueOf(value);
	}
	
	public static short getShort(String param) {
		String value = getString(param);
		return StringUtils.isEmpty(value) ? 0 : Short.valueOf(value);
	}
	
	public static int getInt(String param) {
		String value = getString(param);
		return StringUtils.isEmpty(value) ? 0 : Integer.valueOf(value);
	}
	
	public static String getString(String param) {
		String value = getParameter(param);
		return StringUtils.hasText(value) ? value:"";
	}
}
