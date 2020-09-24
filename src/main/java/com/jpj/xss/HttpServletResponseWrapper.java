package com.jpj.xss;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * 返回值输出代理类
 *
 * @Title: HttpServletResponseWrapper
 * @Description:
 * @author kokJuis
 * @date 上午9:52:11
 */
public class HttpServletResponseWrapper extends javax.servlet.http.HttpServletResponseWrapper
{
    private ByteArrayOutputStream buffer;
    private ServletOutputStream out;

    public HttpServletResponseWrapper(HttpServletResponse httpServletResponse)
    {
        super(httpServletResponse);
        buffer = new ByteArrayOutputStream();
        out = new WrapperOutputStream(buffer);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException
    {
        return out;
    }

    @Override
    public void flushBuffer() throws IOException
    {
        if (out != null)
        {
            out.flush();
        }
    }
    //将response中的内容flush到ByterArrayOutputStream中。
    public byte[] getContent() throws IOException
    {
        //将outstream中的内容强制输出
        flushBuffer();
        return buffer.toByteArray();
    }

    class WrapperOutputStream extends ServletOutputStream
    {
        private ByteArrayOutputStream bos ;

        public WrapperOutputStream(ByteArrayOutputStream bos)
        {
            this.bos = bos;
        }
        //关键点，向bos中写入response中的内容，并将buffer中的内容写入buffer中
        @Override
        public void write(int b) throws IOException
        {
            bos.write(b);
        }
        @Override
        public boolean isReady()
        {
            // TODO Auto-generated method stub
            return true;
        }
        @Override
        public void setWriteListener(WriteListener arg0)
        {
            // TODO Auto-generated method stub
        }
    }
}

