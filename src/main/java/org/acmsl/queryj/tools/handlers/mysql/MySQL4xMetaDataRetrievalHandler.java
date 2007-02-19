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

 ******************************************************************************
 *
 * Filename: MySQL4xMetaDataRetrievalHandler.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Retrieves the MySQL 4.x metadata.
 *
 */
package org.acmsl.queryj.tools.handlers.mysql;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.handlers.DatabaseMetaDataRetrievalHandler;
import org.acmsl.queryj.tools.metadata.engines.mysql.MySQL4xMetadataManager;
import org.acmsl.queryj.tools.metadata.MetadataExtractionLoggingListener;
import org.acmsl.queryj.tools.metadata.MetadataManager;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.patterns.Command;
import org.acmsl.commons.version.VersionUtils;

/*
 * Importing some Ant classes.
 */
import org.apache.tools.ant.BuildException;

/*
 * Importing some JDK classes.
 */
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

/**
 * Retrieves the MySQL 4.x metadata.
 * @author <a href="mailto:chous@acm-sl.org"
 *         >Jose San Leandro</a>
 * @version $Revision$ ($Author$ at $Date$)
 */
public class MySQL4xMetaDataRetrievalHandler
    extends  DatabaseMetaDataRetrievalHandler
{
    /**
     * Checks whether the database vendor matches this handler.
     * @param product the product name.
     * @param version the product version.
     * @param major the major version number.
     * @param minor the minor version number.
     * @return <code>true</code> in case it matches.
     * @precondition product != null
     */
    protected boolean checkVendor(
        final String productName,
        final String productVersion,
        final int majorVersion,
        final int minorVersion)
    {
        boolean result = (productName.indexOf("MySQL") > -1);

        if  (result)
        {
            result = checkVersion(productVersion, VersionUtils.getInstance());
        }
        
        return result;
    }

    /**
     * Checks the engine version.
     * @param version the version.
     * @param versionUtils the <code>VersionUtils</code> instance.
     * @return <code>true</code> if the version matches or is compatible with.
     * @precondition version != null
     * @precondition versionUtils != null
     */
    protected boolean checkVersion(
        final String version, final VersionUtils versionUtils)
    {
        return versionUtils.matches(version, "4.x");
    }
    
    /**
     * Builds a database metadata manager.
     * @param tableNames the table names.
     * @param procedureNames the procedure names.
     * @param disableTableExtraction if the table metadata should not
     * be extracted.
     * @param lazyTableExtraction if the table metadata should not
     * be extracted inmediately.
     * @param disableProcedureExtraction if the procedure metadata should not
     * be extracted.
     * @param lazyProcedureExtraction if the procedure metadata should not
     * be extracted inmediately.
     * @param metaData the database metadata.
     * @param catalog the database catalog.
     * @param schema the database schema.
     * @return the metadata manager instance.
     * @throws org.apache.tools.ant.BuildException whenever the required
     * parameters are not present or valid.
     * @precondition metaData != null
     */
    protected MetadataManager buildMetadataManager(
        final String[] tableNames,
        final String[] procedureNames,
        final boolean disableTableExtraction,
        final boolean lazyTableExtraction,
        final boolean disableProcedureExtraction,
        final boolean lazyProcedureExtraction,
        final DatabaseMetaData metaData,
        final String catalog,
        final String schema)
        throws  BuildException
    {
        MetadataManager result = null;

        try 
        {
            result =
                new MySQL4xMetadataManager(
                    MetadataExtractionLoggingListener.getInstance(),
                    tableNames,
                    procedureNames,
                    disableTableExtraction,
                    lazyTableExtraction,
                    disableProcedureExtraction,
                    lazyProcedureExtraction,
                    metaData,
                    catalog,
                    schema);
        }
        catch  (final RuntimeException exception)
        {
            throw exception;
        }
        catch  (final Exception exception)
        {
            throw new BuildException(exception);
        }

        return result;
    }
}
