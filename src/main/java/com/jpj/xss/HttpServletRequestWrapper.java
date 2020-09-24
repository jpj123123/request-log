package com.jpj.xss;

import jodd.io.StreamUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper {

    //用于保存读取body中数据
    private final byte[] body;
    public HttpServletRequestWrapper(HttpServletRequest request)
            throws IOException {
        super(request);
        body = StreamUtil.readBytes(request.getReader(), "UTF-8");
    }


    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }
            @Override
            public boolean isFinished() {
                return true;
            }
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener arg0) {
                
            }
        };
    }
}


