/*
                        QueryJ

    Copyright (C) 2002  Jose San Leandro Armendariz
                        jsanleandro@yahoo.es
                        chousz@yahoo.com

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
    Contact info: jsanleandro@yahoo.es
    Postal Address: c/Playa de Lagoa, 1
                    Urb. Valdecabanas
                    Boadilla del monte
                    28660 Madrid
                    Spain

 ******************************************************************************
 *
 * Filename: $RCSfile$
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Models <property> elements in sql.xml files.
 *
 * Last modified by: $Author$ at $Date$
 *
 * File version: $Revision$
 *
 * Project version: $Name$
 *
 * $Id$
 *
 */
package org.acmsl.queryj.sqlxml;

/*
 * Importing project-specific classes.
 */
import org.acmsl.queryj.sqlxml.AbstractIdElement;

/**
 * Models &lt;property&gt; elements in <b>sql.xml</b> files, which
 * satisfy the following DTD extract:
 *   <!ELEMENT property EMPTY>
 *  <!ATTLIST property
 *    id ID #REQUIRED
 *    column_name CDATA #IMPLIED
 *    index CDATA #IMPLIED
 *    name CDATA #IMPLIED
 *    type CDATA #REQUIRED>
 * @author <a href="mailto:jsanleandro@yahoo.es"
 *         >Jose San Leandro</a>
 * @version $Revision$
 */
public class PropertyElement
    extends  AbstractIdElement
{
    /**
     * The <i>column_name</i> attribute.
     */
    private String m__strColumnName;

    /**
     * The <i>index</i> attribute.
     */
    private int m__iIndex;

    /**
     * The <i>name</i> attribute.
     */
    private String m__strName;

    /**
     * The <i>type</i> attribute.
     */
    private String m__strType;

    /**
     * Creates a PropertyElement with given information.
     * @param id the <i>id</i> attribute.
     * @param columnName the <i>column_name</i> attribute.
     * @param index the <i>index</i> attribute.
     * @param name the <i>name</i> attribute.
     * @param type the <i>type</i> attribute.
     */
    public PropertyElement(
        final String id,
        final String columnName,
        final int index,
        final String name,
        final String type)
    {
        super(id);
        immutableSetColumnName(columnName);
        immutableSetIndex(index);
        immutableSetName(name);
        immutableSetType(type);
    }

    /**
     * Specifies the <i>column_name</i> attribute.
     * @param columnName such value.
     */
    protected final void immutableSetColumnName(final String columnName)
    {
        m__strColumnName = columnName;
    }

    /**
     * Specifies the <i>column_name</i> attribute.
     * @param columnName such value.
     */
    protected void setColumnName(final String columnName)
    {
        immutableSetColumnName(columnName);
    }

    /**
     * Retrieves the <i>column_name</i> attribute.
     * @return such value.
     */
    public String getColumnName()
    {
        return m__strColumnName;
    }

    /**
     * Specifies the <i>index</i> attribute.
     * @param index such value.
     */
    protected final void immutableSetIndex(final int index)
    {
        m__iIndex = index;
    }

    /**
     * Specifies the <i>index</i> attribute.
     * @param index such value.
     */
    protected void setIndex(final int index)
    {
        m__iIndex = index;
    }

    /**
     * Retrieves the <i>index</i> attribute.
     * @return such value.
     */
    public int getIndex()
    {
        return m__iIndex;
    }

    /**
     * Specifies the <i>name</i> attribute.
     * @param name such value.
     */
    protected final void immutableSetName(final String name)
    {
        m__strName = name;
    }

    /**
     * Specifies the <i>name</i> attribute.
     * @param name such value.
     */
    protected void setName(final String name)
    {
        m__strName = name;
    }

    /**
     * Retrieves the <i>name</i> attribute.
     * @return such value.
     */
    public String getName()
    {
        return m__strName;
    }

    /**
     * Specifies the <i>type</i> attribute.
     * @param type such value.
     */
    protected final void immutableSetType(final String type)
    {
        m__strType = type;
    }

    /**
     * Specifies the <i>type</i> attribute.
     * @param type such value.
     */
    protected void setType(final String type)
    {
        m__strType = type;
    }

    /**
     * Retrieves the <i>type</i> attribute.
     * @return such value.
     */
    public String getType()
    {
        return m__strType;
    }
}
