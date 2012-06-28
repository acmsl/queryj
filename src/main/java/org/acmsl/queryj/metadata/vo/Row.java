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
 * Filename: Row.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Represents 'row' entities in the metadata model.
 *
 */
package org.acmsl.queryj.metadata.vo;

/*
 * Importing JDK classes.
 */
import java.io.Serializable;
import java.util.List;

/**
 * Represents <i>row</i> entities in the metadata model.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public interface Row
    extends  java.lang.Comparable,
             Serializable
{
    /**
     * Retrieves the attribute name.
     * @return such name.
     */
    public String getName();

    /**
     * Retrieves the table name.
     * @return such information.
     */
    public String getTableName();

    /**
     * Retrieves the attributes.
     * @return such information.
     */
    public List<Attribute> getAttributes();
}
