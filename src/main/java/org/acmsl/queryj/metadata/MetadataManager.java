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
 * Filename: MetadataManager.java
 *
 * Author: Jose San Leandro Armendariz (chous)
 *
 * Description: A refactored version of the original MetadataManager.
 *
 * Date: 6/6/12
 * Time: 8:43 AM
 *
 */
package org.acmsl.queryj.metadata;

/*
 * Importing some project classes.
 */

/*
 * Importing some JetBrains annotations.
 */
import org.acmsl.queryj.QueryJException;
    import org.jetbrains.annotations.NotNull;

/*
 * Importing some JDK classes.
 */
import java.io.Serializable;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * A refactored version of the original {@link MetadataManager}.
 * @author <a href="mailto:chous@acm-sl.org">chous</a>
 * @since 2012/06/06
 */
public interface MetadataManager
    extends Serializable
{
    /**
     * The system property prefix to disable generation for concrete (or all, with *) tables.
     */
    public static final String TABLES_DISABLED = "queryj.tables.disabled";

    /**
     * The system property to enable generation for concrete (or all, with * or missing property) tables.
     */
    public static final String TABLES_ENABLED = "queryj.tables.enabled";

    /**
     * Retrieves the name identifying the manager instance.
     * @return such name.
     */
    @NotNull
    public String getName();

    /**
     * Retrieves whether the engine is case sensitive or not.
     * @return such information.
     */
    public boolean isCaseSensitive();

    /**
     * Checks whether the generation phase is enabled for given table.
     * @param tableName the table name.
     * @return <code>true</code> in such case.
     */
    boolean isGenerationAllowedForTable(@NotNull final String tableName);

    /**
     * Retrieves the engine name.
     * @return such information.
     */
    @NotNull
    String getEngineName();

    /**
     * Retrieves the engine version.
     * @return such information.
     */
    @NotNull
    String getEngineVersion();

    /**
     * Retrieves the identifier quote string.
     * @return such information.
     */
    @NotNull
    @SuppressWarnings("unused")
    String getQuote();

    /**
     * Retrieves the type manager.
     * @return such instance.
     */
    public MetadataTypeManager getMetadataTypeManager();

    /**
     * Checks whether the engine requires specific CLOB handling.
     * @return <code>true</code> in such case.
     */
    public boolean requiresCustomClobHandling();

    /**
     * Retrieves the metadata.
     * @throws SQLException if the database operation fails.
     * @throws QueryJException if an error, which is identified by QueryJ,
     * occurs.
     */
    public void eagerlyFetchMetadata()
      throws SQLException,
             QueryJException;

    /**
     * Retrieves the {@link DatabaseMetaData}.
     * @return such information.
     */
    @NotNull
    public DatabaseMetaData getMetaData();

    /**
     * Retrieves the {@link TableDAO} instance.
     * @return such instance.
     */
    @NotNull
    TableDAO getTableDAO();

    /**
     * Retrieves the {@link ColumnDAO} instance.
     * @return such instance.
     */
    @NotNull
    ColumnDAO getColumnDAO();

    /**
     * Retrieves the {@link PrimaryKeyDAO} instance.
     * @return such instance.
     */
    @NotNull
    PrimaryKeyDAO getPrimaryKeyDAO();

    /**
     * Retrieves the {@link ForeignKeyDAO} instance.
     * @return such instance.
     */
    @NotNull
    ForeignKeyDAO getForeignKeyDAO();
}