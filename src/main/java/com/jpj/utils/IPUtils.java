package com.jpj.utils;

import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;

/**
 * 更具request获取IP地址
 */
public class IPUtils {

  /**
   * 获取Ip地址
   *
   * @param request
   * @return
   */
  public static String getIpAddr(HttpServletRequest request) {
	String Xip = request.getHeader("X-Real-IP");
	String XFor = request.getHeader("X-Forwarded-For");
	if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
	  //多次反向代理后会有多个ip值，第一个ip才是真实ip
	  int index = XFor.indexOf(",");
	  if (index != -1) {
		return XFor.substring(0, index);
	  } else {
		return XFor;
	  }
	}
	XFor = Xip;
	if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
	  return XFor;
	}
	if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
	  XFor = request.getHeader("Proxy-Client-IP");
	}
	if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
	  XFor = request.getHeader("WL-Proxy-Client-IP");
	}
	if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
	  XFor = request.getHeader("HTTP_CLIENT_IP");
	}
	if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
	  XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
	}
	if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
	  XFor = request.getRemoteAddr();
	}
	return XFor;
  }


  /**
   * 获取IP地址
   * <p>
   * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
   * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
   */
  public static String getHostIpAddr(HttpServletRequest request) {
	String ip = null;
	ip = getIpAddr(request);
	//使用代理，则获取第一个IP地址
	if (StringUtils.isEmpty(ip) && ip.length() > 15) {
	  if (ip.indexOf(",") > 0) {
		ip = ip.substring(0, ip.indexOf(","));
	  }
	}
	return ip;
  }
}
