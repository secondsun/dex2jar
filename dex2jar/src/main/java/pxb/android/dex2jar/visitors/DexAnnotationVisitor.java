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
package pxb.android.dex2jar.visitors;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;

/**
 * @author Panxiaobo [pxb1988@126.com]
 * @version $Id$
 */
public interface DexAnnotationVisitor {
	/**
	 * Visits a primitive value of the annotation.
	 * 
	 * @param name
	 *          the value name.
	 * @param value
	 *          the actual value, whose type must be {@link Byte}, {@link Boolean}
	 *          , {@link Character}, {@link Short}, {@link Integer}, {@link Long},
	 *          {@link Float}, {@link Double}, {@link String} or {@link Type}.
	 *          This value can also be an array of byte, boolean, short, char,
	 *          int, long, float or double values (this is equivalent to using
	 *          {@link #visitArray visitArray} and visiting each array element in
	 *          turn, but is more convenient).
	 */
	public void visit(String name, Object value);

	/**
	 * Visits an enumeration value of the annotation.
	 * 
	 * @param name
	 *          the value name.
	 * @param desc
	 *          the class descriptor of the enumeration class.
	 * @param value
	 *          the actual enumeration value.
	 */
	void visitEnum(String name, String desc, String value);

	/**
	 * Visits a nested annotation value of the annotation.
	 * 
	 * @param name
	 *          the value name.
	 * @param desc
	 *          the class descriptor of the nested annotation class.
	 * @return a visitor to visit the actual nested annotation value, or
	 *         <tt>null</tt> if this visitor is not interested in visiting this
	 *         nested annotation. <i>The nested annotation value must be fully
	 *         visited before calling other methods on this annotation
	 *         visitor</i>.
	 */
	public DexAnnotationVisitor visitAnnotation(String name, String desc);

	/**
	 * Visits an array value of the annotation. Note that arrays of primitive
	 * types (such as byte, boolean, short, char, int, long, float or double) can
	 * be passed as value to {@link #visit visit}. This is what
	 * {@link ClassReader} does.
	 * 
	 * @param name
	 *          the value name.
	 * @return a visitor to visit the actual array value elements, or
	 *         <tt>null</tt> if this visitor is not interested in visiting these
	 *         values. The 'name' parameters passed to the methods of this visitor
	 *         are ignored. <i>All the array values must be visited before calling
	 *         other methods on this annotation visitor</i>.
	 */
	DexAnnotationVisitor visitArray(String name);

	/**
	 * Visits the end of the annotation.
	 */
	void visitEnd();
}
