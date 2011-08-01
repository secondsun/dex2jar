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
package com.googlecode.dex2jar.ir.expr;

import org.objectweb.asm.Type;

import com.googlecode.dex2jar.ir.ToStringUtil;
import com.googlecode.dex2jar.ir.Value;
import com.googlecode.dex2jar.ir.Value.E1Expr;
import com.googlecode.dex2jar.ir.Value.VT;
import com.googlecode.dex2jar.ir.ValueBox;

/**
 * Represent a Type expression
 * 
 * @see VT#CHECK_CAST
 * @see VT#INSTANCE_OF
 * @see VT#NEW_ARRAY
 * @see VT#CAST
 * 
 * @author Panxiaobo <pxb1988 at gmail.com>
 * @version $Id$
 */
public class TypeExpr extends E1Expr {

    public Type type;

    public TypeExpr(VT vt, Value value, Type type) {
        super(vt, new ValueBox(value));
        this.type = type;

    }

    public String toString() {
        switch (super.vt) {
        case CHECK_CAST:
        case CAST:
            return "((" + ToStringUtil.toShortClassName(type) + ")" + op + ")";
        case INSTANCE_OF:
            return "(" + op + " instanceof " + ToStringUtil.toShortClassName(type) + ")";
        case NEW_ARRAY:
            return "new " + ToStringUtil.toShortClassName(type) + "[" + op + "]";
        }
        return "UNKNOW";
    }

}
