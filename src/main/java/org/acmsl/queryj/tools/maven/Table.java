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

 ******************************************************************************
 *
 * Filename: Table.java
 *
 * Author: Jose Juan
 *
 * Description: Models a table.
 */
package org.acsml.queryj.tools.maven;

/*
 * Importing some JDK classes.
 */
import java.util.List;

/**
 * Models a table.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class Table
{
    /**
     * The name.
     */
    private String m__strName;
    
    /**
     * The list of fields.
     */
    private List m__aFields;
    
    /**
     * Returns the name.
     * @return such value.
     */
    protected final String immutableGetName()
    {
        return m__strName;
    }

    /**
     * Returns the name.
     * @return such value.
     */
    protected String getName()
    {
        return immutableGetName();
    }

    /**
     * Returns the fields.
     * @return such values.
     */
    protected final List immutableGetFields()
    {
        return m__aFields;
    }

    /**
     * Returns the fields.
     * @return such values.
     */
    protected List getFields()
    {
        return immutableGetFields();
    }
}