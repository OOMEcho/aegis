package com.aegis.common.repeatable;

import org.jetbrains.annotations.NotNull;
import org.springframework.util.StreamUtils;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 可重复读取的Request包装器
 *
 * @author xuesong.lei
 * @since 2025-09-07
 */
public class RepeatableHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public RepeatableHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.body = StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() {
        return new RepeatableServletInputStream(this.body);
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    public String getBodyAsString() {
        return new String(this.body, StandardCharsets.UTF_8);
    }

    public byte[] getBodyAsByteArray() {
        return this.body.clone();
    }

    private static class RepeatableServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream inputStream;
        private ReadListener readListener;

        public RepeatableServletInputStream(byte[] body) {
            this.inputStream = new ByteArrayInputStream(body);
        }

        @Override
        public int read() {
            int data = inputStream.read();
            if (data == -1 && readListener != null) {
                try {
                    readListener.onAllDataRead();
                } catch (IOException e) {
                    readListener.onError(e);
                }
            }
            return data;
        }

        @Override
        public int read(@NotNull byte[] b, int off, int len) {
            return inputStream.read(b, off, len);
        }

        @Override
        public boolean isFinished() {
            return inputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            this.readListener = listener;
            if (!isFinished()) {
                try {
                    readListener.onDataAvailable();
                } catch (IOException e) {
                    readListener.onError(e);
                }
            } else {
                try {
                    readListener.onAllDataRead();
                } catch (IOException e) {
                    readListener.onError(e);
                }
            }
        }
    }
}
