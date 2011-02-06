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
 * Filename: Field.java
 *
 * Author: Jose Juan
 *
 * Description: Models a field.
 */
package org.acsml.queryj.tools.maven;

/**
 * Models a field
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class Field
{
    /**
     * The name.
     */
    private String m__strName;
    
    /**
     * The type.
     */
    private String m__strType;
    
    /**
     * The primary key.
     */
    private String m__strPk;
    
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
     * Returns the type.
     * @return such value.
     */
    protected final String immutableGetType()
    {
        return m__strType;
    }
    
    /**
     * Returns the type.
     * @return such value.
     */
    protected String getType()
    {
        return immutableGetType();
    }
    
    /**
     * Returns the primary key.
     * @return such value.
     */
    protected final String immutableGetPk() 
    {
        return m__strPk;
    }
    
    /**
     * Returns the primary key.
     * @return such value.
     */
    protected String getPk() 
    {
        return immutableGetPk();
    }
}