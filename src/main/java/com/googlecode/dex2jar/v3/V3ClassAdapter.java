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
package com.googlecode.dex2jar.v3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import com.googlecode.dex2jar.Annotation;
import com.googlecode.dex2jar.Annotation.Item;
import com.googlecode.dex2jar.Field;
import com.googlecode.dex2jar.Method;
import com.googlecode.dex2jar.asm.TypeNameAdapter;
import com.googlecode.dex2jar.visitors.DexClassVisitor;
import com.googlecode.dex2jar.visitors.DexFieldVisitor;
import com.googlecode.dex2jar.visitors.DexMethodVisitor;

/**
 * @author Panxiaobo [pxb1988@gmail.com]
 * @version $Id$
 */
public class V3ClassAdapter implements DexClassVisitor {

    protected int access_flags;
    protected Map<String, Integer> accessFlagsMap;
    protected List<Annotation> anns = new ArrayList<Annotation>();
    protected boolean build = false;
    protected String className;
    protected ClassVisitor cv;
    protected String file;
    protected String[] interfaceNames;
    protected String superClass;

    /**
     * @param cv
     * @param access_flags
     * @param className
     * @param superClass
     * @param interfaceNames
     */
    public V3ClassAdapter(Map<String, Integer> accessFlagsMap, ClassVisitor cv, int access_flags, String className, String superClass, String[] interfaceNames) {
        super();
        this.accessFlagsMap = accessFlagsMap;
        this.cv = new TypeNameAdapter(cv);
        this.access_flags = access_flags;
        this.className = className;
        this.superClass = superClass;
        this.interfaceNames = interfaceNames;
    }

    protected void build() {
        if (!build) {
            String signature = null;
            for (Iterator<Annotation> it = anns.iterator(); it.hasNext();) {
                Annotation ann = it.next();
                if ("Ldalvik/annotation/Signature;".equals(ann.type)) {
                    it.remove();
                    for (Item item : ann.items) {
                        if (item.name.equals("value")) {
                            Annotation values = (Annotation) item.value;
                            StringBuilder sb = new StringBuilder();
                            for (Item i : values.items) {
                                sb.append(i.value.toString());
                            }
                            signature = sb.toString();
                        }
                    }
                }
            }

            if (isInnerClass) {
                Integer i = accessFlagsMap.get(className);
                if (i != null) {
                    access_flags = i;
                }
            }

            if ((access_flags & Opcodes.ACC_INTERFACE) == 0) { // issue 55
                access_flags |= Opcodes.ACC_SUPER;// 解决生成的class文件使用dx重新转换时使用的指令与原始指令不同的问题
            }

            cv.visit(Opcodes.V1_6, access_flags, className, signature, superClass, interfaceNames);
            for (Annotation ann : anns) {
                if (ann.type.equals("Ldalvik/annotation/MemberClasses;")) {
                    for (Item i : ann.items) {
                        if (i.name.equals("value")) {
                            for (Item j : ((Annotation) i.value).items) {
                                String name = j.value.toString();
                                Integer access = accessFlagsMap.get(name);
                                String innerName = name.substring(className.length(), name.length() - 1);
                                cv.visitInnerClass(name, className, innerName, access == null ? 0 : access);
                            }
                        }
                    }
                    continue;
                } else if (ann.type.equals("Ldalvik/annotation/EnclosingClass;")) {
                    for (Item i : ann.items) {
                        if (i.name.equals("value")) {
                            String outerName = i.value.toString();
                            String innerName = className.substring(outerName.length(), className.length() - 1);
                            cv.visitInnerClass(className, outerName, innerName, access_flags);
                        }
                    }
                    continue;
                } else if (ann.type.equals("Ldalvik/annotation/InnerClass;")) {
                    continue;
                } else if (ann.type.equals("Ldalvik/annotation/EnclosingMethod;")) {
                    continue;
                }
                AnnotationVisitor av = cv.visitAnnotation(ann.type, ann.visible);
                V3AnnAdapter.accept(ann.items, av);
                av.visitEnd();
            }
            if (file != null) {
                cv.visitSource(file, null);
            }
            build = true;
        }
    }

    boolean isInnerClass = false;

    public AnnotationVisitor visitAnnotation(String name, boolean visitable) {
        if (!isInnerClass) {
            isInnerClass = "Ldalvik/annotation/InnerClass;".equals(name);
        }
        Annotation ann = new Annotation(name, visitable);
        anns.add(ann);
        return new V3AnnAdapter(ann);
    }

    public void visitEnd() {
        build();
        cv.visitEnd();
    }

    public DexFieldVisitor visitField(Field field, Object value) {
        build();
        return new V3FieldAdapter(cv, field, value);
    }

    public DexMethodVisitor visitMethod(Method method) {
        build();
        return new V3MethodAdapter(cv, method);
    }

    public void visitSource(String file) {
        this.file = file;
    }

}