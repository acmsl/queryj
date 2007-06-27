//;-*- mode: java -*-
/*
                        QueryJ

    Copyright (C) 2002-2007  Jose San Leandro Armendariz
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
    Contact info: chous@acm-sl.org
    Postal Address: c/Playa de Lagoa, 1
                    Urb. Valdecabanas
                    Boadilla del monte
                    28660 Madrid
                    Spain

 *****************************************************************************
 *
 * Filename: AbstractAttribute.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Abstract logicless implementation of Attribute interface.
 *
 */
package org.acmsl.queryj.tools.metadata.vo;

/*
 * Importing project classes.
 */
import org.acmsl.queryj.tools.metadata.vo.Attribute;

/**
 * Abstract logicless implementation of <code>Attribute</code> interface.
 * @author <a href="mailto:chous@acm-sl.org"
 *         >Jose San Leandro</a>
 */
public abstract class AbstractAttribute
    implements Attribute
{
    /**
     * The name.
     */
    private String m__strName;

    /**
     * The type.
     */
    private int m__iType;

    /**
     * The native type.
     */
    private String m__strNativeType;

    /**
     * The field type.
     */
    private String m__strFieldType;

    /**
     * The table name.
     */
    private String m__strTableName;

    /**
     * The comment.
     */
    private String m__strComment;

    /**
     * Whether the attribute is managed externally or not.
     */
    private boolean m__bManagedExternally;

    /**
     * Whether the attribute allows null values or not.
     */
    private boolean m__bAllowsNull = false;

    /**
     * The optional value.
     */
    private String m__strValue;

    /**
     * Whether the attribute is marked as read-only.
     */
    private boolean m__bReadOnly = false;

    /**
     * Whether the attribute is marked as boolean.
     */
    private boolean m__bBoolean = false;

    /**
     * The symbol for <code>true</code> values in boolean attributes.
     */
    private String m__strBooleanTrue;

    /**
     * The symbol for <code>false</code> values in boolean attributes.
     */
    private String m__strBooleanFalse;

    /**
     * The symbol for <code>null</code> values in boolean attributes.
     */
    private String m__strBooleanNull;

    /**
     * The stack trace to know who created me.
     */
    private final StackTraceElement[] m__aStackTrace =
        new RuntimeException("fake").getStackTrace();

    /**
     * Creates an <code>AbstractAttribute</code> with minimal information,
     * allowing lazy-loading mechanisms for non-essential information.
     * @param tableName the table name.
     * @param name the name.
     */
    protected AbstractAttribute(
        final String tableName, final String name)
    {
        immutableSetTableName(tableName);
        immutableSetName(name);
    }

    /**
     * Creates an <code>AbstractAttribute</code> with the following
     * information.
     * @param name the name.
     * @param type the type.
     * @param nativeType the native type.
     * @param fieldType the field type.
     * @param tableName the table name.
     * @param comment the comment.
     * @param managedExternally whether the attribute is managed externally.
     * @param allowsNull whether the attribute allows null values or not.
     * @param value the optional value.
     * @param readOnly whether the attribute is marked as read-only.
     * @param isBool whether the attribute is marked as boolean.
     * @param booleanTrue the symbol for <code>true</code> values in boolean attributes.
     * @param booleanFalse the symbol for <code>false</code> values in boolean attributes.
     * @param booleanNull the symbol for <code>null</code> values in boolean attributes.
     */
    protected AbstractAttribute(
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
        this(tableName, name);
        immutableSetType(type);
        immutableSetNativeType(nativeType);
        immutableSetFieldType(fieldType);
        immutableSetComment(comment);
        immutableSetManagedExternally(managedExternally);
        immutableSetAllowsNull(allowsNull);
        immutableSetValue(value);
        immutableSetReadOnly(readOnly);
        immutableSetBoolean(isBool);
        immutableSetBooleanTrue(booleanTrue);
        immutableSetBooleanFalse(booleanFalse);
        immutableSetBooleanNull(booleanNull);
    }

    /**
     * Specifies the name.
     * @param name such name.
     */
    protected final void immutableSetName(final String name)
    {
        m__strName = name;
    }

    /**
     * Specifies the name.
     * @param name such name.
     */
    protected void setName(final String name)
    {
        immutableSetName(name);
    }

    /**
     * Retrieves the attribute name.
     * @return such name.
     */
    public String getName()
    {
        return m__strName;
    }

    /**
     * Specifies the type.
     * @param type such information.
     */
    protected final void immutableSetType(final int type)
    {
        m__iType = type;
    }

    /**
     * Specifies the type.
     * @param type such information.
     */
    protected void setType(final int type)
    {
        immutableSetType(type);
    }

    /**
     * Retrieves the attribute type.
     * @return its type.
     */
    public int getType()
    {
        return m__iType;
    }

    /**
     * Specifies the native type.
     * @param nativeType such information.
     */
    protected final void immutableSetNativeType(final String nativeType)
    {
        m__strNativeType = nativeType;
    }

    /**
     * Specifies the native type.
     * @param nativeType such information.
     */
    protected void setNativeType(final String nativeType)
    {
        immutableSetNativeType(nativeType);
    }

    /**
     * Retrieves the native type.
     * @return such information.
     */
    public String getNativeType()
    {
        return m__strNativeType;
    }

    /**
     * Specifies the field type.
     * @param fieldType such information.
     */
    protected final void immutableSetFieldType(final String fieldType)
    {
        m__strFieldType = fieldType;
    }

    /**
     * Specifies the field type.
     * @param fieldType such information.
     */
    protected void setFieldType(final String fieldType)
    {
        immutableSetFieldType(fieldType);
    }

    /**
     * Retrieves the field type.
     * @return such information.
     */
    public String getFieldType()
    {
        return m__strFieldType;
    }

    /**
     * Specifies the table name.
     * @param tableName such information.
     */
    protected final void immutableSetTableName(final String tableName)
    {
        m__strTableName = tableName;
    }

    /**
     * Specifies the table name.
     * @param tableName such information.
     */
    protected void setTableName(final String tableName)
    {
        immutableSetTableName(tableName);
    }

    /**
     * Retrieves the table name.
     * @return such information.
     */
    public String getTableName()
    {
        return m__strTableName;
    }

    /**
     * Specifies the comment.
     * @param comment such comment.
     */
    protected final void immutableSetComment(final String comment)
    {
        m__strComment = comment;
    }

    /**
     * Specifies the comment.
     * @param comment such comment.
     */
    protected void setComment(final String comment)
    {
        immutableSetComment(comment);
    }

    /**
     * Retrieves the attribute comment.
     * @return such comment.
     */
    public String getComment()
    {
        return m__strComment;
    }

    /**
     * Specifies whether it's managed externally.
     * @param such information.
     */
    protected final void immutableSetManagedExternally(
        final boolean managedExternally)
    {
        m__bManagedExternally = managedExternally;
    }

    /**
     * Specifies whether it's managed externally.
     * @param such information.
     */
    protected void setManagedExternally(final boolean managedExternally)
    {
        immutableSetManagedExternally(managedExternally);
    }

    /**
     * Retrieves whether it's managed externally.
     * @return such information.
     */
    public boolean getManagedExternally()
    {
        return m__bManagedExternally;
    }

    /**
     * Specifies whether the attribute allows null values or not.
     * @param allowsNull such information.
     */
    protected final void immutableSetAllowsNull(final boolean allowsNull)
    {
        m__bAllowsNull = allowsNull;
    }

    /**
     * Specifies whether the attribute allows null values or not.
     * @param allowsNull such information.
     */
    protected void setAllowsNull(final boolean allowsNull)
    {
        immutableSetAllowsNull(allowsNull);
    }

    /**
     * Retrieves whether it allows null values or not.
     * @return such information.
     */
    public boolean getAllowsNull()
    {
        return m__bAllowsNull;
    }

    /**
     * Specifies the optional attribute's value, meaning
     * it doesn't just describe the metadata, but also
     * contains data.
     * @param value such information.
     */
    protected final void immutableSetValue(final String value)
    {
        m__strValue = value;
    }

    /**
     * Specifies the optional attribute's value, meaning
     * it doesn't just describe the metadata, but also
     * contains data.
     * @param value such information.
     */
    protected void setValue(final String value)
    {
        immutableSetValue(value);
    }

    /**
     * Retrieves the optional attribute's value, meaning
     * it doesn't just describe the metadata, but also
     * contains data.
     * @return such information.
     */
    public String getValue()
    {
        return m__strValue;
    }

    /**
     * Specifies whether the attribute is marked as read-only.
     * @param flag such condition.
     */
    protected final void immutableSetReadOnly(final boolean flag)
    {
        m__bReadOnly = flag;
    }

    /**
     * Specifies whether the attribute is marked as read-only.
     * @param flag such condition.
     */
    protected void setReadOnly(final boolean flag)
    {
        immutableSetReadOnly(flag);
    }

    /**
     * Retrieves whether the attribute is marked as read-only.
     * @return such condition.
     */
    public boolean isReadOnly()
    {
        return m__bReadOnly;
    }

    /**
     * Specifies whether the attribute is marked as boolean.
     * @param flag such condition.
     */
    protected final void immutableSetBoolean(final boolean flag)
    {
        m__bBoolean = flag;
    }

    /**
     * Specifies whether the attribute is marked as boolean.
     * @param flag such condition.
     */
    protected void setBoolean(final boolean flag)
    {
        immutableSetBoolean(flag);
    }

    /**
     * Retrieves whether the attribute is marked as boolean.
     * @return such condition.
     */
    public boolean isBoolean()
    {
        return m__bBoolean;
    }

    /**
     * Specifies the symbol for <code>true</code> values.
     * @param value such information.
     */
    protected final void immutableSetBooleanTrue(final String value)
    {
        m__strBooleanTrue = value;
    }

    /**
     * Specifies the symbol for <code>true</code> values.
     * @param value such information.
     */
    protected void setBooleanTrue(final String value)
    {
        immutableSetBooleanTrue(value);
    }

    /**
     * Retrieves the symbol for <code>true</code> values.
     * @return such information.
     */
    public String getBooleanTrue()
    {
        return m__strBooleanTrue;
    }

    /**
     * Specifies the symbol for <code>false</code> values.
     * @param value such information.
     */
    protected final void immutableSetBooleanFalse(final String value)
    {
        m__strBooleanFalse = value;
    }

    /**
     * Specifies the symbol for <code>false</code> values.
     * @param value such information.
     */
    protected void setBooleanFalse(final String value)
    {
        immutableSetBooleanFalse(value);
    }

    /**
     * Retrieves the symbol for <code>false</code> values.
     * @return such information.
     */
    public String getBooleanFalse()
    {
        return m__strBooleanFalse;
    }

    /**
     * Specifies the symbol for <code>null</code> values.
     * @param value such information.
     */
    protected final void immutableSetBooleanNull(final String value)
    {
        m__strBooleanNull = value;
    }

    /**
     * Specifies the symbol for <code>null</code> values.
     * @param value such information.
     */
    protected void setBooleanNull(final String value)
    {
        immutableSetBooleanNull(value);
    }

    /**
     * Retrieves the symbol for <code>null</code> values.
     * @return such information.
     */
    public String getBooleanNull()
    {
        return m__strBooleanNull;
    }

    /**
     * Retrieves the attribute name.
     * @return such information.
     */
    public String toString()
    {
        return getName();
    }
}
