/*
 * Copyright (c) 2009-2010 Panxiaobo
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pxb.android.dex2jar.v3;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

import pxb.android.dex2jar.Field;
import pxb.android.dex2jar.visitors.DexAnnotationVisitor;
import pxb.android.dex2jar.visitors.DexFieldVisitor;

/**
 * @author Panxiaobo [pxb1988@126.com]
 * @version $Id$
 */
public class V3FieldAdapter implements DexFieldVisitor {
	protected List<Ann> anns = new ArrayList<Ann>();
	protected boolean build = false;
	protected ClassVisitor cv;
	protected Field field;
	protected FieldVisitor fv;
	Object value;

	protected void build() {
		if (!build) {
			FieldVisitor fv = cv.visitField(field.getAccessFlags(), field.getName(), field.getType(), null, value);
			if (fv != null) {
				for (Ann ann : anns) {
					AnnotationVisitor av = fv.visitAnnotation(ann.type, ann.visible == 1);
					V3AnnAdapter.accept(ann.items, av);
					av.visitEnd();
				}
			}
			this.fv = fv;
			build = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pxb.android.dex2jar.visitors.DexFieldVisitor#visitAnnotation(java.lang
	 * .String, int)
	 */
	public DexAnnotationVisitor visitAnnotation(String name, int visitable) {
		Ann ann = new Ann(name, visitable);
		anns.add(ann);
		return new V3AnnAdapter(ann);
	}

	/**
	 * @param cv
	 * @param field
	 */
	public V3FieldAdapter(ClassVisitor cv, Field field, Object value) {
		super();
		this.cv = cv;
		this.field = field;
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pxb.android.dex2jar.visitors.DexFieldVisitor#visitEnd()
	 */
	public void visitEnd() {
		build();
		if (fv != null)
			fv.visitEnd();
	}

}
