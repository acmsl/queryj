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
 * Filename: OracleMetadataManager.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Manages the information metadata stored in an Oracle database.
 *
 */
package org.acmsl.queryj.tools.metadata.engines.oracle;

/*
 * Importing project-specific classes.
 */
import org.acmsl.queryj.Condition;
import org.acmsl.queryj.Field;
import org.acmsl.queryj.QueryFactory;
import org.acmsl.queryj.QueryJException;
import org.acmsl.queryj.QueryResultSet;
import org.acmsl.queryj.SelectQuery;
import org.acmsl.queryj.tools.metadata.engines.JdbcMetadataManager;
import org.acmsl.queryj.tools.metadata.MetadataTypeManager;

/*
 * Importing some ACM-SL Commons classes.
 */
import org.acmsl.commons.logging.UniqueLogFactory;

/*
 * Importing Commons-Logging classes.
 */
import org.apache.commons.logging.Log;

/*
 * Importing some JetBrains annotations.
 */
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Importing some JDK classes.
 */
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * Manages the information metadata stored in an Oracle database.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class OracleMetadataManager
    extends  JdbcMetadataManager
    implements  OracleTableRepository
{
    /**
     * Creates an {@link OracleMetadataManager} using given information.
     * @param tableNames explicitly specified table names.
     * @param procedureNames explicitly specified procedure names.
     * @param disableTableExtraction <code>true</code> to disable table
     * extraction.
     * @param lazyTableExtraction if the table metadata should not be
     * retrieved from the database until the tables to extract are
     * specified.
     * @param disableProcedureExtraction <code>true</code> to disable
     * procedure extraction.
     * @param lazyProcedureExtraction if the procedure metadata should not
     * be retrieved from the database until the procedures to extract are
     * specified.
     * @param metaData the database meta data.
     * @param catalog the database catalog.
     * @param schema the database schema.
     * @param caseSensitive whether the database engine is case sensitive or not.
     * @throws SQLException if the database operation fails.
     * @throws QueryJException if an error, which is identified by QueryJ,
     * occurs.
     */
    public OracleMetadataManager(
        final String[] tableNames,
        final String[] procedureNames,
        final boolean disableTableExtraction,
        final boolean lazyTableExtraction,
        final boolean disableProcedureExtraction,
        final boolean lazyProcedureExtraction,
        final DatabaseMetaData metaData,
        final String catalog,
        final String schema,
        final boolean caseSensitive)
        throws  SQLException,
                QueryJException
    {
        super(
            tableNames,
            procedureNames,
            disableTableExtraction,
            lazyTableExtraction,
            disableProcedureExtraction,
            lazyProcedureExtraction,
            metaData,
            catalog,
            schema,
            caseSensitive);
    }

    /**
     * Creates an <code>OracleMetadataManager</code> using given information.
     * @param tableNames explicitly specified table names.
     * @param procedureNames explicitly specified procedure names.
     * @param metaData the database meta data.
     * @param catalog the database catalog.
     * @param schema the database schema.
     * @param caseSensitive whether the database engine is case sensitive.
     * @throws SQLException if the database operation fails.
     * @throws QueryJException if an error, which is identified by QueryJ,
     * occurs.
     */
    public OracleMetadataManager(
        final String[] tableNames,
        final String[] procedureNames,
        final DatabaseMetaData metaData,
        final String catalog,
        final String schema,
        final boolean caseSensitive)
        throws  SQLException,
                QueryJException
    {
        super(
            tableNames,
            procedureNames,
            metaData,
            catalog,
            schema,
            caseSensitive);
    }

    /**
     * Extracts the primary keys.
     * @param metaData the database metadata.
     * @param catalog the database catalog.
     * @param schema the database schema.
     * @throws SQLException if any kind of SQL exception occurs.
     * @throws QueryJException if any other error occurs.
     * @precondition metaData != null
     */
    protected void extractPrimaryKeys(
        @NotNull final DatabaseMetaData metaData,
        final String catalog,
        final String schema)
      throws  SQLException,
              QueryJException
    {
        extractPrimaryKeys(
            metaData.getConnection(),
            catalog,
            schema,
            getTableNames(metaData, catalog, schema),
            QueryFactory.getInstance());
    }

    /**
     * Extracts the primary keys.
     * @param connection the database connection.
     * @param catalog the database catalog.
     * @param schema the database schema.
     * @param tableNames the table names.
     * @param queryFactory the <code>QueryFactory</code> instance.
     * @throws SQLException if any kind of SQL exception occurs.
     * @throws QueryJException if any other error occurs.
     * @precondition connection != null
     * @precondition tableNames != null
     * @precndition queryFactory != null
     */
    protected void extractPrimaryKeys(
        @NotNull final Connection connection,
        final String catalog,
        final String schema,
        @Nullable final String[] tableNames,
        @NotNull final QueryFactory queryFactory)
      throws  SQLException,
              QueryJException
    {
        @Nullable ResultSet t_rsResults = null;

        @Nullable PreparedStatement t_PreparedStatement = null;

        Log t_Log = UniqueLogFactory.getLog(OracleMetadataManager.class);

        int t_iLength = (tableNames != null) ? tableNames.length : 0;
            
        try
        {
            for  (int t_iTableIndex = 0;
                      t_iTableIndex < t_iLength;
                      t_iTableIndex++) 
            {
                try
                {
                    @NotNull SelectQuery t_Query = queryFactory.createSelectQuery();

                    t_Query.select(USER_CONS_COLUMNS.COLUMN_NAME);

                    t_Query.from(USER_CONS_COLUMNS);
                    t_Query.from(USER_CONSTRAINTS);

                    @NotNull Condition t_Condition =
                        USER_CONS_COLUMNS.CONSTRAINT_NAME.equals(
                            USER_CONSTRAINTS.CONSTRAINT_NAME);

                    // USER_CONS_COLUMNS.CONSTRAINT_NAME = USER_CONSTRAINTS.CONSTRAINT_NAME

                    t_Condition =
                        t_Condition.and(
                            USER_CONS_COLUMNS.TABLE_NAME.equals());

                    //(USER_CONS_COLUMNS.CONSTRAINT_NAME = USER_CONSTRAINTS.CONSTRAINT_NAME)
                    // AND (USER_CONS_COLUMNS.TABLE_NAME = ?)

                    t_Condition =
                        t_Condition.and(
                            USER_CONSTRAINTS.CONSTRAINT_TYPE.equals("P"));

                    //((USER_CONS_COLUMNS.CONSTRAINT_NAME = USER_CONSTRAINTS.CONSTRAINT_NAME)
                    // AND (USER_CONS_COLUMNS.TABLE_NAME = ?)) AND (USER_CONSTRAINTS.CONSTRAINT_TYPE = 'P')

                    t_Query.where(t_Condition);

                    t_PreparedStatement = t_Query.prepareStatement(connection);

                    t_Query.setString(
                        USER_CONS_COLUMNS.TABLE_NAME.equals(),
                        tableNames[t_iTableIndex]);

                    //SELECT USER_CONS_COLUMNS.COLUMN_NAME
                    //FROM USER_CONS_COLUMNS, USER_CONSTRAINTS
                    //WHERE ((USER_CONS_COLUMNS.CONSTRAINT_NAME = USER_CONSTRAINTS.CONSTRAINT_NAME)
                    // AND (USER_CONS_COLUMNS.TABLE_NAME = ?)) AND (USER_CONSTRAINTS.CONSTRAINT_TYPE = 'P')

                    if  (t_Log != null)
                    {
                        t_Log.debug(
                            "query:" + t_Query);
                    }
                    
                    t_rsResults = t_Query.executeQuery();

                    while  (   (t_rsResults != null)
                            && (t_rsResults.next()))
                    {
                        addPrimaryKey(
                            tableNames[t_iTableIndex],
                            t_rsResults.getString(
                                USER_CONS_COLUMNS.COLUMN_NAME.toSimplifiedString()));
                    }
                }
                catch  (@NotNull final SQLException sqlException)
                {
                    throw
                        new QueryJException(
                            "cannot.retrieve.primary.keys",
                            sqlException);
                }
            }
        }
        catch  (@NotNull final QueryJException queryjException)
        {
            throw queryjException;
        }
        finally 
        {
            try 
            {
                if  (t_rsResults != null)
                {
                    t_rsResults.close();
                }
            }
            catch  (@NotNull final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot close the result set.",
                        sqlException);
                }
            }

            try 
            {
                if  (t_PreparedStatement != null)
                {
                    t_PreparedStatement.close();
                }
            }
            catch  (@NotNull final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot close the statement.",
                        sqlException);
                }
            }
        }
    }

    /**
     * Extracts the foreign keys.
     * @param metaData the database metadata.
     * @param catalog the database catalog.
     * @param schema the database schema.
     * @throws SQLException if any kind of SQL exception occurs.
     * @throws QueryJException if any other error occurs.
     * @precondition metaData != null
     */
    protected void extractForeignKeys(
        final DatabaseMetaData metaData,
        final String catalog,
        final String schema)
      throws  SQLException,
              QueryJException
    {
        // extractForeignKeys(
        //     metaData.getConnection(),
        //     catalog,
        //     schema,
        //     getTableNames(metaData, catalog, schema),
        //     QueryFactory.getInstance(),
        //     OracleTextFunctions.getInstance());
    }

    /**
     * Extracts the foreign keys.
     * @param connection the database connection.
     * @param catalog the database catalog.
     * @param schema the database schema.
     * @param tableNames the table names.
     * @param queryFactory the query factory.
     * @param textFunctions the {@link OracleTextFunctions} instance.
     * @throws SQLException if any kind of SQL exception occurs.
     * @throws QueryJException if any other error occurs.
     * @precondition connection != null
     * @precondition tableNames != null
     * @precondition queryFactory != null
     * @precondition textFunctions != null
     */
    protected void extractForeignKeys(
        @NotNull final Connection connection,
        final String catalog,
        final String schema,
        @Nullable final String[] tableNames,
        @NotNull final QueryFactory queryFactory,
        @NotNull final OracleTextFunctions textFunctions)
      throws  SQLException,
              QueryJException
    {
        @Nullable QueryResultSet t_Results = null;

        @Nullable PreparedStatement t_PreparedStatement = null;

        Log t_Log = UniqueLogFactory.getLog(OracleMetadataManager.class);
        
        int t_iLength = (tableNames != null) ? tableNames.length : 0;
        
        try
        {
                try
                {
                    // select con.constraint_name,
                    //        rcol.table_name,
                    //        rcol.column_name,
                    //        con.table_name,
                    //        col.column_name
                    // from user_constraints con,
                    //      user_cons_columns col,
                    //      user_constraints rcon,
                    //      user_cons_columns rcol
                    // where rcon.constraint_type = 'R'
                    //   and rcon.r_constraint_name = con.constraint_name
                    //   and col.table_name = con.table_name
                    //   and col.constraint_name = con.constraint_name
                    //   and rcol.table_name = rcon.table_name
                    //   and rcol.constraint_name = rcon.constraint_name
                    //   and rcol.position = col.position
                    //   and upper(col.table_name) = ?

                    @NotNull SelectQuery t_Query = queryFactory.createSelectQuery();

                    @Nullable OracleUserConstraintsTable CON =
                        OracleUserConstraintsTable.getInstance("con");
                    @Nullable OracleUserConsColumnsTable COL =
                        OracleUserConsColumnsTable.getInstance("col");
                    @Nullable OracleUserConstraintsTable RCON =
                        OracleUserConstraintsTable.getInstance("rcon");
                    @Nullable OracleUserConsColumnsTable RCOL =
                        OracleUserConsColumnsTable.getInstance("rcol");

                    t_Query.select(CON.CONSTRAINT_NAME);
                    t_Query.select(RCOL.TABLE_NAME);
                    t_Query.select(RCOL.COLUMN_NAME);
                    t_Query.select(CON.TABLE_NAME);
                    t_Query.select(COL.COLUMN_NAME);

                    t_Query.from(CON);
                    t_Query.from(COL);
                    t_Query.from(RCON);
                    t_Query.from(RCOL);

                    t_Query.where(
                             RCON.CONSTRAINT_TYPE.equals("R")
                        .and(RCON.R_CONSTRAINT_NAME.equals(
                                 CON.CONSTRAINT_NAME))
                        .and(COL.TABLE_NAME.equals(CON.TABLE_NAME))
                        .and(COL.CONSTRAINT_NAME.equals(CON.CONSTRAINT_NAME))
                        .and(RCOL.TABLE_NAME.equals(RCON.TABLE_NAME))
                        .and(RCOL.CONSTRAINT_NAME.equals(RCON.CONSTRAINT_NAME))
                        .and(RCOL.POSITION.equals(COL.POSITION))
                        .and(textFunctions.upper(COL.TABLE_NAME).in(tableNames.length)));

                    t_PreparedStatement = t_Query.prepareStatement(connection);

                    t_Query.setStrings(
                        textFunctions.upper(COL.TABLE_NAME).in(tableNames.length),
                        toUpperCase(tableNames));

                    t_Results = (QueryResultSet) t_Query.executeQuery();

                    if  (t_Results != null)
                    {
                        @NotNull Collection t_cOriginAttributes = new ArrayList();
                        @NotNull Collection t_cDestinationAttributes = new ArrayList();
                        boolean t_bNewFk = true;
                        @Nullable String t_strLastTable = null;
                        @Nullable String t_strCurrentTable = null;
                        @Nullable String t_strOriginTable = null;

                        while  (t_Results.next())
                        {
                            t_strOriginTable = t_Results.getString(COL.TABLE_NAME);

                            t_strCurrentTable = 
                                t_Results.getString(RCOL.TABLE_NAME);

                            if  (t_strLastTable == null)
                            {
                                t_strLastTable = t_strCurrentTable;
                            }

                            t_bNewFk =
                                !(t_strLastTable.equals(t_strCurrentTable));

                            if  (t_bNewFk)
                            {
                                addForeignKey(
                                    t_strLastTable,
                                    (String[])
                                        t_cOriginAttributes.toArray(
                                            EMPTY_STRING_ARRAY),
                                    t_strOriginTable,
                                    (String[])
                                        t_cDestinationAttributes.toArray(
                                            EMPTY_STRING_ARRAY));

                                t_cOriginAttributes = new ArrayList();
                                t_cDestinationAttributes = new ArrayList();
                                t_strLastTable = t_strCurrentTable;
                            }

                            t_cOriginAttributes.add(
                                t_Results.getString(RCOL.COLUMN_NAME));
                            t_cDestinationAttributes.add(
                                t_Results.getString(COL.COLUMN_NAME));
                        }

                        if  (!t_bNewFk)
                        {
                            addForeignKey(
                                t_strLastTable,
                                (String[])
                                    t_cOriginAttributes.toArray(
                                        EMPTY_STRING_ARRAY),
                                t_strOriginTable,
                                (String[])
                                    t_cDestinationAttributes.toArray(
                                        EMPTY_STRING_ARRAY));
                        }
                    }
                }
                catch  (@NotNull final SQLException sqlException)
                {
                    throw
                        new QueryJException(
                            "cannot.retrieve.foreign.keys",
                            sqlException);
                }
//            }
        }
        catch  (@NotNull final QueryJException queryjException)
        {
            throw queryjException;
        }
        finally 
        {
            try 
            {
                if  (t_Results != null)
                {
                    t_Results.close();
                }
            }
            catch  (@NotNull final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot close the result set.",
                        sqlException);
                }
            }

            try 
            {
                if  (t_PreparedStatement != null)
                {
                    t_PreparedStatement.close();
                }
            }
            catch  (@NotNull final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot close the statement.",
                        sqlException);
                }
            }
        }
    }

    /**
     * Retrieves the table names.
     * @param metaData the metadata.
     * @param catalog the catalog.
     * @param schema the schema.
     * @return the list of all table names.
     * @throws SQLException if the database operation fails.
     * @precondition metaData != null
     */
    protected String[] getTableNames(
        @NotNull final DatabaseMetaData metaData,
        final String catalog,
        final String schema)
      throws  SQLException,
              QueryJException
    {
        return
            getTableNames(
                metaData.getConnection(),
                catalog,
                schema,
                QueryFactory.getInstance());
    }
                
    /**
     * Retrieves the table names.
     * @param connection the connection.
     * @param catalog the catalog.
     * @param schema the schema.
     * @param queryFactory the <code>QueryFactory</code> instance.
     * @return the list of all table names.
     * @throws SQLException if the database operation fails.
     * @precondition connection != null
     * @precondition queryFactory != null
     */
    protected String[] getTableNames(
        @NotNull final Connection connection,
        final String catalog,
        final String schema,
        @NotNull final QueryFactory queryFactory)
      throws  SQLException,
              QueryJException
    {
        String[] result;

        Log t_Log = UniqueLogFactory.getLog(OracleMetadataManager.class);
        
        @Nullable QueryResultSet t_Results = null;

        @Nullable PreparedStatement t_PreparedStatement = null;

        try
        {
            try
            {
                @NotNull SelectQuery t_Query = queryFactory.createSelectQuery();

                t_Query.select(USER_TABLES.TABLE_NAME);

                t_Query.from(USER_TABLES);

                t_Query.where(USER_TABLES.TABLE_NAME.notEquals("PLAN_TABLE"));

                t_PreparedStatement = t_Query.prepareStatement(connection);

                t_Results = t_Query.retrieveMatchingResults();
            }
            catch  (@NotNull final SQLException sqlException)
            {
                throw
                    new QueryJException(
                        "cannot.retrieve.database.table.names",
                            sqlException);
            }

            result = extractTableNames(t_Results);
        }
        catch  (@NotNull final SQLException sqlException)
        {
            if  (t_Log != null)
            {
                t_Log.error(
                    "Cannot retrieve the table names.",
                    sqlException);
            }

            throw sqlException;
        }
        catch  (@NotNull final QueryJException queryjException)
        {
            if  (t_Log != null)
            {
                t_Log.error(
                    "Cannot retrieve the table names.",
                    queryjException);
            }

            throw queryjException;
        }
        finally 
        {
            try 
            {
                if  (t_Results != null)
                {
                    t_Results.close();
                }
            }
            catch  (@NotNull final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot close the result set.",
                        sqlException);
                }
            }

            try 
            {
                if  (t_PreparedStatement != null)
                {
                    t_PreparedStatement.close();
                }
            }
            catch  (@NotNull final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot close the statement.",
                        sqlException);
                }
            }
        }

        if  (result == null)
        {
            result = EMPTY_STRING_ARRAY;
        }

        return result;
    }

    /**
     * Extracts the table names from given result set.
     * @param resultSet the result set with the table information.
     * @return the list of table names.
     * @throws SQLException if the database operation fails.
     * @precondition resultSet != null
     */
    @NotNull
    protected String[] extractTableNames(@NotNull final QueryResultSet resultSet)
        throws  SQLException
    {
        return
            extractStringFields(
                resultSet,
                USER_TABLES.TABLE_NAME)[0];
    }

    /**
     * Retrieves the column names from given table name.
     * @param metaData the metadata.
     * @param catalog the catalog.
     * @param schema the schema.
     * @param tableName the table name.
     * @return the list of all column names.
     * @throws SQLException if the database operation fails.
     * @throws QueryJException if the any other error occurs.
     * @precondition metaData != null
     */
    protected String[] getColumnNames(
        @NotNull final DatabaseMetaData metaData,
        final String catalog,
        final String schema,
        final String tableName)
      throws  SQLException,
              QueryJException
    {
        return
            getColumnNames(
                metaData.getConnection(),
                catalog,
                schema,
                tableName,
                QueryFactory.getInstance());
    }

    /**
     * Retrieves the column names from given table name.
     * @param connection the connection.
     * @param catalog the catalog.
     * @param schema the schema.
     * @param tableName the table name.
     * @param queryFactory the <code>QueryFactory</code> instance.
     * @return the list of all column names.
     * @throws SQLException if the database operation fails.
     * @throws QueryJException if the any other error occurs.
     * @precondition connection != null
     * @precondition queryFactory != null
     */
    protected String[] getColumnNames(
        @NotNull final Connection connection,
        final String catalog,
        final String schema,
        final String tableName,
        @NotNull final QueryFactory queryFactory)
      throws  SQLException,
              QueryJException
    {
        String[] result;

        Log t_Log = UniqueLogFactory.getLog(OracleMetadataManager.class);
        
        @Nullable ResultSet t_rsResults = null;

        @Nullable PreparedStatement t_PreparedStatement = null;

        try
        {
            try
            {
                @NotNull SelectQuery t_Query = queryFactory.createSelectQuery();

                t_Query.select(USER_TAB_COLUMNS.COLUMN_NAME);

                t_Query.from(USER_TAB_COLUMNS);

                t_Query.where(USER_TAB_COLUMNS.TABLE_NAME.equals());

                t_Query.orderBy(USER_TAB_COLUMNS.COLUMN_ID);
                t_PreparedStatement = t_Query.prepareStatement(connection);

                t_Query.setString(
                    USER_TAB_COLUMNS.TABLE_NAME.equals(),
                    tableName);

                /*
                t_PreparedStatement = t_Connection.prepareStatement(t_Query.toString());

                t_PreparedStatement.setString(
                    1,
                    tableName);
                */

                t_rsResults = t_Query.executeQuery();
                //t_rsResults = t_PreparedStatement.executeQuery();
            }
            catch  (@NotNull final SQLException sqlException)
            {
                throw
                    new QueryJException(
                        "cannot.retrieve.database.column.names",
                        sqlException);
            }

            result = extractColumnNames(t_rsResults);
        }
        catch  (@NotNull final SQLException sqlException)
        {
            if  (t_Log != null)
            {
                t_Log.error(
                    "Cannot retrieve the column names.",
                    sqlException);
            }

            throw sqlException;
        }
        catch  (@NotNull final QueryJException queryjException)
        {
            if  (t_Log != null)
            {
                t_Log.error(
                    "Cannot retrieve the column names.",
                    queryjException);
            }

            throw queryjException;
        }
        finally 
        {
            try 
            {
                if  (t_rsResults != null)
                {
                    t_rsResults.close();
                }
            }
            catch  (@NotNull final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot close the result set.",
                        sqlException);
                }
            }

            try 
            {
                if  (t_PreparedStatement != null)
                {
                    t_PreparedStatement.close();
                }
            }
            catch  (@NotNull final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot close the statement.",
                        sqlException);
                }
            }
        }

        if  (result == null)
        {
            result = EMPTY_STRING_ARRAY;
        }

        return result;
    }

    /**
     * Extracts the column names from given result set.
     * @param resultSet the result set with the table information.
     * @return the list of column names.
     * @throws SQLException if the database operation fails.
     * @precondition resultSet != null
     */
    @NotNull
    protected String[] extractColumnNames(@NotNull final ResultSet resultSet)
        throws  SQLException
    {
        return
            extractStringFields(
                resultSet,
                USER_TAB_COLUMNS.COLUMN_NAME) [0];
    }

    /**
     * Retrieves the column types from given table name.
     * @param metaData the metadata.
     * @param catalog the catalog.
     * @param schema the schema.
     * @param tableName the table name.
     * @param size the number of fields to extract.
     * @return the list of all column types.
     * @throws SQLException if the database operation fails.
     * @throws QueryJException if any other error occurs.
     * @precondition metaData != null
     */
    protected int[] getColumnTypes(
        @NotNull final DatabaseMetaData metaData,
        final String catalog,
        final String schema,
        final String tableName,
        final int size)
      throws  SQLException,
              QueryJException
    {
        return
            getColumnTypes(
                metaData.getConnection(),
                catalog,
                schema,
                tableName,
                size,
                QueryFactory.getInstance());
    }

    /**
     * Retrieves the column types from given table name.
     * @param connection the connection.
     * @param catalog the catalog.
     * @param schema the schema.
     * @param tableName the table name.
     * @param size the number of fields to extract.
     * @param queryFactory the <code>QueryFactory</code> instance.
     * @return the list of all column types.
     * @throws SQLException if the database operation fails.
     * @throws QueryJException if any other error occurs.
     * @precondition connection != null
     * @precondition queryFactory != null
     */
    protected int[] getColumnTypes(
        @NotNull final Connection connection,
        final String catalog,
        final String schema,
        final String tableName,
        final int size,
        @NotNull final QueryFactory queryFactory)
      throws  SQLException,
              QueryJException
    {
        int[] result;

        Log t_Log = UniqueLogFactory.getLog(OracleMetadataManager.class);
        
        @Nullable ResultSet t_rsResults = null;

        @Nullable PreparedStatement t_PreparedStatement = null;

        try
        {
            try
            {
                @NotNull SelectQuery t_Query = queryFactory.createSelectQuery();

                t_Query.select(USER_TAB_COLUMNS.DATA_TYPE);

                t_Query.from(USER_TAB_COLUMNS);

                t_Query.where(
                    USER_TAB_COLUMNS.TABLE_NAME.equals());

                t_PreparedStatement = t_Query.prepareStatement(connection);

                t_Query.setString(
                    USER_TAB_COLUMNS.TABLE_NAME.equals(),
                    tableName);

                t_rsResults = t_Query.executeQuery();
            }
            catch  (@NotNull final SQLException sqlException)
            {
                throw
                    new QueryJException(
                        "cannot.retrieve.database.column.types",
                        sqlException);
            }

            result = extractColumnTypes(t_rsResults, USER_TAB_COLUMNS.DATA_TYPE);
        }
        catch  (@NotNull final SQLException sqlException)
        {
            if  (t_Log != null)
            {
                t_Log.error(
                    "Cannot retrieve the column types.",
                    sqlException);
            }

            throw sqlException;
        }
        catch  (@NotNull final QueryJException queryjException)
        {
            if  (t_Log != null)
            {
                t_Log.error(
                    "Cannot retrieve the column types.",
                    queryjException);
            }

            throw queryjException;
        }
        finally 
        {
            try 
            {
                if  (t_rsResults != null)
                {
                    t_rsResults.close();
                }
            }
            catch  (@NotNull final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot close the result set.",
                        sqlException);
                }
            }

            try 
            {
                if  (t_PreparedStatement != null)
                {
                    t_PreparedStatement.close();
                }
            }
            catch  (@NotNull final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot close the statement.",
                        sqlException);
                }
            }
        }

        if  (result == null)
        {
            result = EMPTY_INT_ARRAY;
        }

        return result;
    }

    /**
     * Extracts the column types from given result set.
     * @param resultSet the result set with the column information.
     * @param field the field name.
     * @return the list of column types.
     * @throws SQLException if the database operation fails.
     * @precondition resultSet != null
     */
    protected int[] extractColumnTypes(
        @NotNull final ResultSet resultSet, final Field field)
      throws  SQLException
    {
        return
            extractColumnTypes(
                resultSet, field.toSimplifiedString(), getMetadataTypeManager());
    }

    /**
     * Extracts the column types from given result set.
     * @param resultSet the result set with the column information.
     * @param fieldName the field name.
     * @return the list of column types.
     * @throws SQLException if the database operation fails.
     * @precondition resultSet != null
     */
    protected int[] extractColumnTypes(
        @NotNull final ResultSet resultSet, final String fieldName)
        throws  SQLException
    {
        return
            extractColumnTypes(
                resultSet, fieldName, getMetadataTypeManager());
    }

    /**
     * Extracts the column types from given result set.
     * @param resultSet the result set with the column information.
     * @param fieldName the field name.
     * @param metadataTypeManager the metadata type manager.
     * @return the list of column types.
     * @throws SQLException if the database operation fails.
     * @precondition resultSet != null
     * @precondition metadataTypeManager != null
     */
    protected int[] extractColumnTypes(
        @NotNull final ResultSet resultSet,
        final String fieldName,
        @NotNull final MetadataTypeManager metadataTypeManager)
      throws  SQLException
    {
        int[] result = EMPTY_INT_ARRAY;

        @NotNull String[] t_astrTypes = extractStringFields(resultSet, fieldName)[0];

        int t_iCount = (t_astrTypes != null) ? t_astrTypes.length : 0;

        if  (t_iCount > 0)
        {
            result = new int[t_iCount];

            for  (int t_iIndex = 0;
                      t_iIndex < t_iCount;
                      t_iIndex++) 
            {
                result[t_iIndex] =
                    metadataTypeManager.getJavaType(t_astrTypes[t_iIndex]);
            }
        }

        return result;
    }

    /**
     * Retrieves whether the columns allow null values.
     * @param metaData the metadata.
     * @param catalog the catalog.
     * @param schema the schema.
     * @param tableName the table name.
     * @param size the number of fields to extract.
     * @return the list of all column types.
     * @throws SQLException if the database operation fails.
     * @throws QueryJException if any other error occurs.
     * @precondition metaData != null
     */
    protected boolean[] getAllowNulls(
        @NotNull final DatabaseMetaData metaData,
        final String catalog,
        final String schema,
        final String tableName,
        final int size)
      throws  SQLException,
              QueryJException
    {
        return
            getAllowNulls(
                metaData.getConnection(),
                catalog,
                schema,
                tableName,
                size,
                QueryFactory.getInstance());
    }

    /**
     * Retrieves whether the columns allow null values.
     * @param connection the connection.
     * @param catalog the catalog.
     * @param schema the schema.
     * @param tableName the table name.
     * @param size the number of fields to extract.
     * @param queryFactory the <code>QueryFactory</code> instance.
     * @return the list of all column types.
     * @throws SQLException if the database operation fails.
     * @throws QueryJException if any other error occurs.
     * @precondition connection != null
     * @precondition queryFactory != null
     */
    protected boolean[] getAllowNulls(
        @NotNull final Connection connection,
        final String catalog,
        final String schema,
        final String tableName,
        final int size,
        @NotNull final QueryFactory queryFactory)
      throws  SQLException,
              QueryJException
    {
        boolean[] result;

        Log t_Log = UniqueLogFactory.getLog(OracleMetadataManager.class);
        
        @Nullable ResultSet t_rsResults = null;

        @Nullable PreparedStatement t_PreparedStatement = null;

        try
        {
            try
            {
                @NotNull SelectQuery t_Query = queryFactory.createSelectQuery();

                t_Query.select(USER_TAB_COLUMNS.NULLABLE);

                t_Query.from(USER_TAB_COLUMNS);

                t_Query.where(
                    USER_TAB_COLUMNS.TABLE_NAME.equals());

                t_PreparedStatement = t_Query.prepareStatement(connection);

                t_Query.setString(
                    USER_TAB_COLUMNS.TABLE_NAME.equals(),
                    tableName);

                t_rsResults = t_Query.executeQuery();
            }
            catch  (@NotNull final SQLException sqlException)
            {
                throw
                    new QueryJException(
                        "cannot.retrieve.database.column.types",
                        sqlException);
            }

            result =
                extractAllowNull(
                    t_rsResults,
                    USER_TAB_COLUMNS.NULLABLE);
        }
        catch  (@NotNull final SQLException sqlException)
        {
            if  (t_Log != null)
            {
                t_Log.error(
                    "Cannot retrieve the column types.",
                    sqlException);
            }

            throw sqlException;
        }
        catch  (@NotNull final QueryJException queryjException)
        {
            if  (t_Log != null)
            {
                t_Log.error(
                    "Cannot retrieve the column types.",
                    queryjException);
            }

            throw queryjException;
        }
        finally 
        {
            try 
            {
                if  (t_rsResults != null)
                {
                    t_rsResults.close();
                }
            }
            catch  (@NotNull final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot close the result set.",
                        sqlException);
                }
            }

            try 
            {
                if  (t_PreparedStatement != null)
                {
                    t_PreparedStatement.close();
                }
            }
            catch  (@NotNull final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot close the statement.",
                        sqlException);
                }
            }
        }

        if  (result == null)
        {
            result = new boolean[0];
        }

        return result;
    }

    /**
     * Extracts the column types from given result set.
     * @param resultSet the result set with the column information.
     * @param field the field.
     * @return the list of column types.
     * @throws SQLException if the database operation fails.
     * @precondition resultSet != null
     */
    @NotNull
    protected boolean[] extractAllowNull(
        @NotNull final ResultSet resultSet, @NotNull final Field field)
        throws  SQLException
    {
        return extractAllowNull(resultSet, field.toSimplifiedString());
    }

    /**
     * Extracts the column types from given result set.
     * @param resultSet the result set with the column information.
     * @param fieldName the field name.
     * @return the list of column types.
     * @throws SQLException if the database operation fails.
     * @precondition resultSet != null
     */
    @NotNull
    protected boolean[] extractAllowNull(
        @NotNull final ResultSet resultSet, @NotNull final String fieldName)
      throws  SQLException
    {
        boolean[] result = null;

        @NotNull String[] t_astrTypes = extractStringFields(resultSet, fieldName)[0];

        int t_iCount = (t_astrTypes != null) ? t_astrTypes.length : 0;

        if  (t_iCount > 0)
        {
            result = new boolean[t_iCount];

            for  (int t_iIndex = 0;
                      t_iIndex < t_iCount;
                      t_iIndex++) 
            {
                result[t_iIndex] =
                    !("N".equalsIgnoreCase(t_astrTypes[t_iIndex]));
            }
        }

        if (result == null)
        {
            result = new boolean[0];
        }

        return result;
    }

    /**
     * Extracts the table comments.
     * @param metaData the database metadata.
     * @param catalog the database catalog.
     * @param schema the database schema.
     * @param tableName the table name.
     * @return the table comments.
     * @throws SQLException if any kind of SQL exception occurs.
     * @throws QueryJException if any other error occurs.
     * @precondition metaData != null
     * @precondition tableName != null
     */
    @Nullable
    protected String getTableComment(
        @NotNull final DatabaseMetaData metaData,
        final String catalog,
        final String schema,
        final String tableName)
      throws  SQLException,
              QueryJException
    {
        return
            getTableComment (
                metaData.getConnection(),
                catalog,
                schema,
                tableName,
                QueryFactory.getInstance());
    }

    /**
     * Extracts the table comments.
     * @param connection the database connection.
     * @param catalog the database catalog.
     * @param schema the database schema.
     * @param tableName the table name.
     * @param queryFactory the <code>QueryFactory</code> instance.
     * @return the table comments.
     * @throws SQLException if any kind of SQL exception occurs.
     * @throws QueryJException if any other error occurs.
     * @precondition connection != null
     * @precondition tableNames != null
     * @precndition queryFactory != null
     */
    @Nullable
    protected String getTableComment(
        @NotNull final Connection connection,
        final String catalog,
        final String schema,
        final String tableName,
        @NotNull final QueryFactory queryFactory)
      throws  SQLException,
              QueryJException
    {
        @Nullable String result = "";

        Log t_Log = UniqueLogFactory.getLog(OracleMetadataManager.class);
        
        @Nullable ResultSet t_rsResults = null;

        @Nullable PreparedStatement t_PreparedStatement = null;

        try
        {
            try
            {
                @NotNull SelectQuery t_Query = queryFactory.createSelectQuery();

                t_Query.select(USER_TAB_COMMENTS.COMMENTS);

                t_Query.from(USER_TAB_COMMENTS);

                t_Query.where(
                    USER_TAB_COMMENTS.TABLE_NAME.equals());

                t_PreparedStatement = t_Query.prepareStatement(connection);

                t_Query.setString(
                    USER_TAB_COMMENTS.TABLE_NAME.equals(),
                    tableName);

                if  (t_Log != null)
                {
                    t_Log.debug("query:" + t_Query);
                }
                
                t_rsResults = t_Query.executeQuery();

                @Nullable String t_strComment = null;

                if  (   (t_rsResults != null)
                        && (t_rsResults.next()))
                {
                    t_strComment =
                        t_rsResults.getString(
                            USER_TAB_COMMENTS.COMMENTS.toSimplifiedString());

                    if  (t_strComment != null)
                    {
                        if  (t_Log != null)
                        {
                            t_Log.trace(
                                  "Comments for table "+ tableName
                                  + " = " + t_strComment);
                        }
                    }
                    else
                    {
                        t_strComment = "";
                    }

                    result = t_strComment;
                }
            }
            catch  (@NotNull final SQLException sqlException)
            {
                throw
                    new QueryJException(
                        "cannot.retrieve.table.comments",
                        sqlException);
            }
        }
        catch  (@NotNull final QueryJException queryjException)
        {
            throw queryjException;
        }
        finally 
        {
            try 
            {
                if  (t_rsResults != null)
                {
                    t_rsResults.close();
                }
            }
            catch  (@NotNull final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot close the result set.",
                        sqlException);
                }
            }

            try 
            {
                if  (t_PreparedStatement != null)
                {
                    t_PreparedStatement.close();
                }
            }
            catch  (@NotNull final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot close the statement.",
                        sqlException);
                }
            }
        }

        return result;
    }

    /**
     * Retrieves the type manager.
     * @return such instance.
     */
    @NotNull
    public MetadataTypeManager getMetadataTypeManager()
    {
        return OracleMetadataTypeManager.getInstance();
    }

    /**
     * Retrieves the name identifying the manager instance.
     * @return such name.
     */
    @NotNull
    public String getName()
    {
        return "oracle";
    }

    /**
     * Checks whether the engine requires specific BLOB handling.
     * @return <code>true</code> in such case.
     */
    public boolean requiresCustomBlobHandling()
    {
        // TODO: Test whether this is needed.
        return true;
    }

    /**
     * Checks whether the engine requires specific CLOB handling.
     * @return <code>true</code> in such case.
     */
    public boolean requiresCustomClobHandling()
    {
        return true;
    }

    /**
     * Converts given values to upper case.
     * @param values the values to convert.
     * @return the upper-case version of given values.
     * @precondition values != null
     */
    @NotNull
    protected String[] toUpperCase(@Nullable final String[] values)
    {
        @NotNull final String[] result = new String[(values != null) ? values.length : 0];

        int t_iCount = 0;

        if (values != null)
        {
            t_iCount = values.length;
        }

        @Nullable String value;

        for (int t_iIndex = 0 ; t_iIndex < t_iCount; t_iIndex++)
        {
            value = values[t_iIndex];

            if (value != null)
            {
                result[t_iIndex] = value.toUpperCase(Locale.ENGLISH);
            }
        }

        return result;
    }
}
