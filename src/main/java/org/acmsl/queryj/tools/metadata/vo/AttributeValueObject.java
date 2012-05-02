/*
                        QueryJ

    Copyright (C) 2002-today  Jose San Leandro Armendariz
                              chous@acm-sl.org

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    Thanks to ACM S.L. for distributing this library under the GPL license.
    Contact info: jose.sanleandro@acm-sl.com

 *****************************************************************************
 *
 * Filename: AttributeValueObject.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Value-object implementation of Attribute interface.
 *
 */
package org.acmsl.queryj.tools.metadata.vo;

/**
 * Value-object implementation of <code>Attribute</code> interface.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public final class AttributeValueObject
    extends AbstractAttribute
{
    /**
     * Creates an <code>AttributeValueObject</code> with the following
     * information.
     * @param name the name.
     * @param type the type.
     * @param nativeType the native type.
     * @param fieldType the field type.
     * @param tableName the table name.
     * @param managedExternally whether the attribute is managed externally.
     * @param allowsNull whether the attribute allows null values or not.
     * @param value the optional value.
     */
    public AttributeValueObject(
        final String name,
        final int type,
        final String nativeType,
        final String fieldType,
        final String tableName,
        final String comment,
        final boolean managedExternally,
        final boolean allowsNull,
        final String value,
        final boolean readOnly,
        final boolean isBool,
        final String booleanTrue,
        final String booleanFalse,
        final String booleanNull)
    {
        super(
            name,
            type,
            nativeType,
            fieldType,
            tableName,
            comment,
            managedExternally,
            allowsNull,
            value,
            readOnly,
            isBool,
            booleanTrue,
            booleanFalse,
            booleanNull);
    }
}

