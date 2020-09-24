package com.jpj.xss;


import com.jpj.utils.IPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;


public class LoggerFilter implements Filter {

  Logger logger = LoggerFactory.getLogger(LoggerFilter.class);

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse res,
					   FilterChain chain) throws IOException, ServletException {
	long reqTime = System.currentTimeMillis();
	String method = "GET";
	HttpServletRequest requestWrapper = null;
	HttpServletResponseWrapper responseWrapper = null;
	String url = "";
	String urlParam = "";
	String ip = "";
	if (servletRequest instanceof HttpServletRequest) {
	  method = ((HttpServletRequest) servletRequest).getMethod();
	  requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) servletRequest);  //替换
	  responseWrapper = new HttpServletResponseWrapper((HttpServletResponse) res); //response替换
	  url = ((HttpServletRequest) servletRequest).getServletPath();
	  urlParam = ((HttpServletRequest) servletRequest).getQueryString();
	  ip = IPUtils.getIpAddr((HttpServletRequest) servletRequest);
	}
	requestWrapper.getAttributeNames();
	String param = "";
	if ("POST".equalsIgnoreCase(method)) {
	  param = getBodyString(requestWrapper.getReader());
//            logger.info(url + "请求参数" + param);
	  before(requestWrapper, responseWrapper);
	} else {
	  param = getBodyString(requestWrapper.getReader());
	}
	chain.doFilter(requestWrapper, responseWrapper);
	if (!url.matches("(/v2/.*)|(/swagger.*)|(/webjars.*)|(/configuration.*)|(/druid.*)|(/.*\\.ico)")) {
		String resStr = getResponseString(responseWrapper, res);
		long resTime = System.currentTimeMillis();
	  long m = resTime - reqTime;
	  logger.info("ip:{},URL:{}?{},请求耗时{}毫秒,参数:{},返回结果:{}", ip, url, urlParam, m, param, resStr);

//	  if (m > 5000) {
//		String elog = String.format("请求耗时%s毫秒\nurl%s参数:%s", m, url, param);
//		DingdingUtil.post(elog, true);
//	  }
	}
//	  -javaagent:D:\ideaprojects\gitproject\myagent\target\myagent-jar-with-dependencies.jar
	after(requestWrapper, responseWrapper);
  }


  @Override
  public void destroy() {
  }

  //获取request请求body中参数
  public static String getBodyString(BufferedReader br) {
	String inputLine;
	StringBuffer bodyBuffer = new StringBuffer();
	try {
	  while ((inputLine = br.readLine()) != null) {
		bodyBuffer.append(inputLine);
	  }
	  br.close();
	} catch (IOException e) {
	  System.out.println("IOException: " + e);
	}
	return bodyBuffer.toString();
  }

  public static String getResponseString(HttpServletResponseWrapper httpServletResponseWrapper, ServletResponse response) throws IOException {
	String result = null;
	byte[] content = httpServletResponseWrapper.getContent();
	if (content.length > 0) {
	  result = new String(content, "UTF-8");
	}
	if(result != null) {
	  ServletOutputStream outputStream = response.getOutputStream();
	  outputStream.write(content);
	  outputStream.flush();
	}
	return result;
  }

  public void before(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

  }

  public void after(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

  }
}
