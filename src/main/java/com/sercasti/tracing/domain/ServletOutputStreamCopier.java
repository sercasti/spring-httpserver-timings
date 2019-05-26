package com.sercasti.tracing.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class ServletOutputStreamCopier extends ServletOutputStream {

    private final OutputStream outputStream;
    private final ByteArrayOutputStream copy;
    private long counter;
    private int bufsize = 1024 * 1024 * 1024;
    private boolean written;

    public ServletOutputStreamCopier(final OutputStream outputStream) {
        this.outputStream = outputStream;
        this.copy = new ByteArrayOutputStream();
        this.counter = 0;
        written = false;
    }

    private void writeBuf() throws IOException {
        outputStream.write(copy.toByteArray());
        outputStream.flush();
        written = true;
    }

    @Override
    public void write(final int bytes) throws IOException {
        if (counter > bufsize) {
            outputStream.write(bytes);
        } else {
            copy.write(bytes);
            if (counter == bufsize) {
                writeBuf();
            }
            counter++;
        }
    }

    public void reallyFlush() throws IOException {
        if (!written) {
            writeBuf();
        }
    }

    public boolean isCommitted() {
        return written;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(final WriteListener writeListener) {
    }
}