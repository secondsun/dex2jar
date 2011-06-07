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
package com.googlecode.dex2jar.optimize;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.TraceMethodVisitor;

/**
 * @author Panxiaobo [pxb1988@gmail.com]
 * @version $Id$
 */
public class Util implements Opcodes {
    public static boolean needBreak(AbstractInsnNode ins) {
        switch (ins.getType()) {
        case AbstractInsnNode.JUMP_INSN:
        case AbstractInsnNode.LOOKUPSWITCH_INSN:
        case AbstractInsnNode.TABLESWITCH_INSN:
        case AbstractInsnNode.LABEL:
            return true;
        }
        return false;
    }

    public static int var(AbstractInsnNode p) {
        return ((VarInsnNode) p).var;
    }

    public static void var(AbstractInsnNode p, int r) {
        ((VarInsnNode) p).var = r;
    }

    public static boolean isWrite(AbstractInsnNode p) {
        if (p instanceof VarInsnNode) {
            VarInsnNode q = (VarInsnNode) p;
            switch (q.getOpcode()) {
            case ISTORE:
            case LSTORE:
            case DSTORE:
            case FSTORE:
            case ASTORE:
                return true;
            }
        }
        return false;
    }

    public static boolean isSameVar(AbstractInsnNode p, AbstractInsnNode q) {
        return ((VarInsnNode) p).var == ((VarInsnNode) q).var;
    }

    public static boolean isRead(AbstractInsnNode p) {
        if (p instanceof VarInsnNode) {
            VarInsnNode q = (VarInsnNode) p;
            switch (q.getOpcode()) {
            case ILOAD:
            case DLOAD:
            case LLOAD:
            case FLOAD:
            case ALOAD:
                return true;
            }
        }
        return false;
    }

    public static boolean isEnd(AbstractInsnNode p) {
        switch (p.getOpcode()) {
        case ATHROW:
        case RETURN:
        case IRETURN:
        case LRETURN:
        case FRETURN:
        case DRETURN:
        case ARETURN:
            return true;
        }
        return false;
    }

    public static void dump(InsnList insnList) {
        TraceMethodVisitor tr = new TraceMethodVisitor();
        tr.text.clear();
        insnList.accept(tr);
        int i = 0;
        for (Object o : tr.text) {
            System.out.print((i++) + " " + o);
        }
        tr.text.clear();
    }
}
