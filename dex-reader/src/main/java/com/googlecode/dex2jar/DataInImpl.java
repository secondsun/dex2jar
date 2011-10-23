/*
 * Copyright (c) 2009-2011 Panxiaobo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.dex2jar;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Stack;

/**
 * @author Panxiaobo [pxb1988@gmail.com]
 * @version $Id$
 */
public class DataInImpl implements DataIn {

    private static class XByteArrayInputStream extends ByteArrayInputStream {
        /**
         * @param buf
         */
        public XByteArrayInputStream(byte[] buf) {
            super(buf);
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }
    }

    private XByteArrayInputStream in;
    private Stack<Integer> stack = new Stack<Integer>();

    public DataInImpl(byte[] data) {
        in = new XByteArrayInputStream(data);
    }

    public int getCurrentPosition() {
        return this.in.getPos();
    }

    public void move(int absOffset) {
        in.setPos(absOffset);
    }

    public void pop() {
        this.move(stack.pop());
    }

    public void push() {
        stack.push(in.getPos());
    }

    public void pushMove(int absOffset) {
        this.push();
        this.move(absOffset);
    }

    public int readByte() {
        return (byte) in.read();
    }

    public byte[] readBytes(int size) {
        byte[] data = new byte[size];
        try {
            in.read(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public int readIntx() {
        return in.read() | (in.read() << 8) | (in.read() << 16) | (in.read() << 24);
    }

    public long readLeb128() {
        int bitpos = 0;
        long vln = 0L;
        do {
            int inp = in.read();
            vln |= ((long) (inp & 0x7F)) << bitpos;
            bitpos += 7;
            if ((inp & 0x80) == 0)
                break;
        } while (true);
        if (((1L << (bitpos - 1)) & vln) != 0)
            vln -= (1L << bitpos);
        return vln;
    }

    public long readLongx() {
        return (readIntx() & 0x00000000FFFFFFFFL) | (((long) readIntx()) << 32);
    }

    public int readShortx() {
        return (short) (readUByte() | (readUByte() << 8));
    }

    public int readUByte() {
        return in.read();
    }

    public int readUIntx() {
        return readIntx();
    }

    public long readULeb128() {
        long value = 0;
        int count = 0;
        int b = in.read();
        while ((b & 0x80) != 0) {
            value |= (b & 0x7f) << count;
            count += 7;
            b = in.read();
        }
        value |= (b & 0x7f) << count;
        return value;
    }

    @Override
    public int readUShortx() {
        return readUByte() | (readUByte() << 8);
    }

    public void skip(int bytes) {
        in.skip(bytes);
    }
}
