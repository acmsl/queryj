/*
                        QueryJ

    Copyright (C) 2004  Jose San Leandro Armendariz
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
 * Filename: $RCSfile$
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Retrieves the database metadata instance.
 *
 */
package org.acmsl.queryj.tools.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.QueryJException;
import org.acmsl.queryj.tools.AntCommand;
import org.acmsl.queryj.tools.AntFieldElement;
import org.acmsl.queryj.tools.AntFieldFkElement;
import org.acmsl.queryj.tools.AntTableElement;
import org.acmsl.queryj.tools.AntTablesElement;
import org.acmsl.queryj.tools.handlers.AbstractAntCommandHandler;
import org.acmsl.queryj.tools.metadata.engines.JdbcMetadataManager;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.metadata.MetadataTypeManager;
import org.acmsl.queryj.tools.handlers.ParameterValidationHandler;

/*
 * Importing some ACM-SL Commons classes.
 */
import org.acmsl.commons.logging.UniqueLogFactory;
import org.acmsl.commons.patterns.Command;

/*
 * Importing some Ant classes.
 */
import org.apache.tools.ant.BuildException;

/*
 * Importing some Commons-Logging classes.
 */
import org.apache.commons.logging.Log;

/*
 * Importing some JDK classes.
 */
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

/**
 * Retrieves the database metadata instance.
 * @author <a href="mailto:chous@acm-sl.org"
 *         >Jose San Leandro</a>
 * @version $Revision$ ($Author$ at $Date$)
 */
public abstract class DatabaseMetaDataRetrievalHandler
    extends  AbstractAntCommandHandler
{
    /**
     * The Database Metadata attribute name.
     */
    public static final String DATABASE_METADATA = "jdbc.database.metadata";

    /**
     * The Database Metadata manager attribute name.
     */
    public static final String METADATA_MANAGER = "jdbc.database.metadata.manager";

    /**
     * The table names attribute name.
     */
    public static final String TABLE_NAMES = "table.names";

    /**
     * The procedure names attribute name.
     */
    public static final String PROCEDURE_NAMES = "procedure.names";

    /**
     * The table fields attribute name.
     */
    public static final String TABLE_FIELDS = "table.fields";

    /**
     * The flag indicating whether the extraction has already been done.
     */
    public static final String METADATA_EXTRACTION_ALREADY_DONE =
        "metadata.extraction.already.done";

    /**
     * Creates a DatabaseMetaDataRetrievalHandler.
     */
    public DatabaseMetaDataRetrievalHandler() {};

    /**
     * Handles given command.
     * @param command the command to handle.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition command != null
     */
    public boolean handle(final AntCommand command)
    {
        return handle(command.getAttributeMap());
    }

    /**
     * Handles given parameters.
     * @param parameters the parameters to handle.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition parameters != null
     */
    protected boolean handle(final Map parameters)
    {
        return
            handle(
                parameters,
                retrieveAlreadyDoneFlag(parameters),
                retrieveMetadata(parameters));
    }

    /**
     * Handles given parameters.
     * @param parameters the parameters to handle.
     * @param alreadyDone the flag indicating whether the metadata
     * extraction has already been done.
     * @param metaData the database metadata.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition metaData != null
     */
    protected boolean handle(
        final Map parameters,
        final boolean alreadyDone,
        final DatabaseMetaData metaData)
      throws  BuildException
    {
        boolean result = false;

        if  (    (!alreadyDone)
              && (checkVendor(metaData)))
        {
            result =
                /*
                handle(
                    parameters,
                    metaData,
                    retrieveExtractTables(parameters),
                    retrieveExtractProcedures(parameters));
                */
                oldHandle(parameters);

            storeAlreadyDoneFlag(parameters);
        }

        return result;
    }

    /**
     * Handles given parameters.
     * @param parameters the parameters to handle.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     */
    protected boolean oldHandle(final Map parameters)
        throws  BuildException
    {
        boolean result = false;

        storeMetadata(retrieveMetadata(parameters), parameters);

        MetadataManager t_MetadataManager = null;

        boolean t_bDisableTableExtraction =
            !retrieveExtractTables(parameters);

        boolean t_bDisableProcedureExtraction =
            !retrieveExtractProcedures(parameters);

        Collection t_cTableElements = null;

        Iterator t_itTableElements = null;

        String[] t_astrTableNames = null;

        Collection t_cFieldElements = null;

        int t_iTableIndex = 0;

        Map t_mKeys = new HashMap();

        Collection t_cTables = null;

        if  (!t_bDisableTableExtraction)
        {
            AntTablesElement t_TablesElement =
                retrieveTablesElement(parameters);

            if  (t_TablesElement != null)
            {
                t_cTableElements = t_TablesElement.getTables();

                if  (   (t_cTableElements != null)
                        && (t_cTableElements.size() > 0))
                {
                    t_astrTableNames = new String[t_cTableElements.size()];

                    t_itTableElements = t_cTableElements.iterator();

                    while  (   (t_itTableElements != null)
                               && (t_itTableElements.hasNext()))
                    {
                        AntTableElement t_Table =
                            (AntTableElement) t_itTableElements.next();

                        if  (t_Table != null)
                        {
                            t_astrTableNames[t_iTableIndex] = t_Table.getName();
                        }

                        t_iTableIndex++;
                    }

                    storeTableNames(t_astrTableNames, parameters);

                    t_itTableElements = t_cTableElements.iterator();

                    t_iTableIndex = 0;

                    while  (   (t_itTableElements != null)
                               && (t_itTableElements.hasNext()))
                    {
                        AntTableElement t_Table =
                            (AntTableElement) t_itTableElements.next();

                        if  (t_Table != null)
                        {
                            t_cFieldElements = t_Table.getFields();

                            if  (   (t_cFieldElements != null)
                                    && (t_cFieldElements.size() > 0))
                            {
                                break;
                            }
                        }
                    }
                }
            }
        }

        t_MetadataManager =
            buildMetadataManager(
                t_bDisableTableExtraction,
                (   (t_cFieldElements != null)
                    && (t_cFieldElements.size() > 0)),
                t_bDisableProcedureExtraction,
                false,
                parameters);

        storeMetadataManager(t_MetadataManager, parameters);

        MetadataTypeManager t_MetadataTypeManager =
            t_MetadataManager.getMetadataTypeManager();

        if  (t_cTableElements != null)
        {
            t_itTableElements = t_cTableElements.iterator();
        }

        if  (   (t_itTableElements != null)
             && (t_MetadataTypeManager != null)
             && (t_MetadataManager != null))
        {
            while  (t_itTableElements.hasNext())
            {
                AntTableElement t_Table =
                    (AntTableElement) t_itTableElements.next();

                if  (t_Table != null)
                {
                    t_cTables = (Collection) t_mKeys.get(buildTableKey());

                    if  (t_cTables == null)
                    {
                        t_cTables = new ArrayList();
                        t_mKeys.put(buildTableKey(), t_cTables);
                    }

                    t_cTables.add(t_Table.getName());

                    t_cFieldElements = t_Table.getFields();

                    if  (   (t_cFieldElements  != null)
                            && (t_cFieldElements.size() > 0))
                    {
                        String[] t_astrTableFieldNames =
                            new String[t_cFieldElements.size()];

                        Iterator t_itFieldElements = t_cFieldElements.iterator();

                        int t_iFieldIndex = 0;

                        while  (   (t_itFieldElements != null)
                                   && (t_itFieldElements.hasNext()))
                        {
                            AntFieldElement t_Field =
                                (AntFieldElement) t_itFieldElements.next();

                            if  (t_Field != null)
                            {
                                t_astrTableFieldNames[t_iFieldIndex] =
                                    t_Field.getName();

                                t_MetadataManager.addColumnType(
                                    t_Table.getName(),
                                    t_Field.getName(),
                                    t_MetadataTypeManager.getJavaType(
                                        t_Field.getType()));

                                Collection t_cFields =
                                    (Collection)
                                    t_mKeys.get(buildTableFieldsKey(t_Table.getName()));

                                if  (t_cFields == null)
                                {
                                    t_cFields = new ArrayList();
                                    t_mKeys.put(buildTableFieldsKey(t_Table.getName()), t_cFields);
                                }

                                t_cFields.add(t_Field.getName());

                                if  (t_Field.isPk())
                                {
                                    t_MetadataManager.addPrimaryKey(
                                        t_Table.getName(),
                                        t_Field.getName());

                                    Collection t_cPks =
                                        (Collection) t_mKeys.get(buildPkKey(t_Table.getName()));

                                    if  (t_cPks == null)
                                    {
                                        t_cPks = new ArrayList();
                                        t_mKeys.put(buildPkKey(t_Table.getName()), t_cPks);
                                    }

                                    t_cPks.add(t_Field.getName());

                                    t_mKeys.put(
                                        buildPkKey(
                                            t_Table.getName(),
                                            t_Field.getName()),
                                        t_Field.getName());
                                }

                                Collection t_cFieldFks =
                                    t_Field.getFieldFks();

                                if  (t_cFieldFks != null)
                                {
                                    t_mKeys.put(
                                        buildFkKey(
                                            t_Table.getName(),
                                            t_Field.getName()),
                                        t_cFieldFks);
                                }
                            }

                            t_iFieldIndex++;
                        }

                        t_MetadataManager.addColumnNames(
                            t_Table.getName(),
                            t_astrTableFieldNames);
                    }
                }
            }

            storeMetadataManager(t_MetadataManager, parameters);
        }

        t_cTables = (Collection) t_mKeys.get(buildTableKey());

        if  (t_cTables != null)
        {
            Iterator t_itTables = t_cTables.iterator();

            while  (   (t_itTables != null)
                       && (t_itTables.hasNext()))
            {
                String t_strTableName = (String) t_itTables.next();

                if  (t_strTableName != null)
                {
                    Collection t_cFields =
                        (Collection)
                        t_mKeys.get(
                            buildTableFieldsKey(
                                t_strTableName));

                    if  (t_cFields != null)
                    {
                        Iterator t_itFields = t_cFields.iterator();

                        while  (   (t_itFields != null)
                                   && (t_itFields.hasNext()))
                        {
                            String t_strFieldName =
                                (String) t_itFields.next();

                            Collection t_cFieldFks =
                                (Collection)
                                t_mKeys.get(
                                    buildFkKey(
                                        t_strTableName,
                                        t_strFieldName));

                            if  (t_cFieldFks != null)
                            {
                                Iterator t_itFieldFks = t_cFieldFks.iterator();

                                while  (   (t_itFieldFks != null)
                                           && (t_itFieldFks.hasNext()))
                                {
                                    AntFieldFkElement t_FieldFk =
                                        (AntFieldFkElement)
                                        t_itFieldFks.next();

                                    if  (t_FieldFk != null)
                                    {
                                        t_MetadataManager.addForeignKey(
                                            t_strTableName,
                                            new String[] {t_strFieldName},
                                            t_FieldFk.getTable(),
                                            new String[] {t_FieldFk.getField()});
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return result;
    }

    /**
     * Handles given parameters.
     * @param parameters the parameters to handle.
     * @param metaData the database metadata.
     * @param enableTableExtraction whether to extract tables.
     * @param enableProcedureExtractor whether to extract procedures.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition metaData != null
     */
    protected boolean handle(
        final Map parameters,
        final DatabaseMetaData metaData,
        final boolean enableTableExtraction,
        final boolean enableProcedureExtraction)
    {
        boolean result = false;

        storeMetadata(metaData, parameters);

        AntTablesElement t_TablesElement = null;

        if  (enableTableExtraction)
        {
            t_TablesElement = retrieveTablesElement(parameters);

            String[] t_astrTableNames =
                extractTableNames(parameters, t_TablesElement);

            storeTableNames(t_astrTableNames, parameters);
        }

        MetadataManager t_MetadataManager =
            buildMetadataManager(
                !enableTableExtraction,
                lazyTableExtraction(t_TablesElement),
                !enableProcedureExtraction,
                false,
                parameters);

        storeMetadataManager(t_MetadataManager, parameters);

        if  (enableProcedureExtraction)
        {
            if  (t_TablesElement == null)
            {
                t_TablesElement = retrieveTablesElement(parameters);
            }

            extractProcedures(
                parameters,
                metaData,
                t_MetadataManager,
                t_TablesElement,
                t_MetadataManager.getMetadataTypeManager());
        }

        return result;
    }


    /**
     * Retrieves the names of the user-defined tables.
     * @param parameters the command parameters.
     * @param tablesElement the &lt;tables&gt; element.
     * @return such table names.
     * @throws BuildException if the process fails.
     * @precondition parameters != null
     */
    protected String[] extractTableNames(
        final Map parameters, final AntTablesElement tablesElement)
    {
        String[] result = new String[0];

        if  (tablesElement != null)
        {
            result = extractTableNames(tablesElement.getTables());
        }

        return result;
    }

    /**
     * Retrieves the fields of the user-defined tables.
     * @param tables the table definitions.
     * @return such fields.
     * @throws BuildException if the process fails.
     * @precondition tables != null
     */
    protected String[] extractTableNames(final Collection tables)
    {
        String[] result = null;

        int t_iLength = (tables != null) ? tables.size() : 0;

        result = new String[t_iLength];

        int t_iTableIndex = 0;

        if  (t_iLength > 0)
        {
            Iterator t_itTableElements = tables.iterator();

            if  (t_itTableElements != null)
            {
                while  (t_itTableElements.hasNext())
                {
                    AntTableElement t_Table =
                        (AntTableElement) t_itTableElements.next();

                    if  (t_Table != null)
                    {
                        result[t_iTableIndex] = t_Table.getName();
                    }

                    t_iTableIndex++;
                }
            }
        }

        return result;
    }

    /**
     * Retrieves whether the tables should be extracted on demand.
     * @param tablesElement the tables element.
     * @return the result of such analysis..
     * @throws BuildException if the process fails.
     * @precondition tablesElement != null
     */
    protected boolean lazyTableExtraction(final AntTablesElement tablesElement)
    {
        boolean result = false;

        if  (tablesElement != null)
        {
            result = lazyTableExtraction(tablesElement.getTables());
        }

        return result;
    }

    /**
     * Retrieves whether the tables should be extracted on demand.
     * @param tables the table definitions.
     * @return the result of such analysis..
     * @throws BuildException if the process fails.
     * @precondition tables != null
     */
    protected boolean lazyTableExtraction(final Collection tables)
    {
        boolean result = false;

        int t_iLength = (tables != null) ? tables.size() : 0;

        if  (t_iLength > 0)
        {
            Iterator t_itTableElements = tables.iterator();

            if  (t_itTableElements != null)
            {
                while  (t_itTableElements.hasNext())
                {
                    AntTableElement t_Table =
                        (AntTableElement) t_itTableElements.next();

                    if  (t_Table != null)
                    {
                        Collection t_cFields = t_Table.getFields();

                        if  (   (t_cFields != null)
                             && (t_cFields.size()> 0))
                        {
                            result = true;

                            break;
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Extracts the procedures..
     * @param parameters the parameters to handle.
     * @param metaData the database metadata.
     * @param metadataManager the metadata manager.
     * @param tablesElement the tables element.
     * @param metadataTypeManager the metadata type manager.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition metaData != null
     * @precondition metadataManager != null
     * @precondition tableElements != null
     * @precondition metadataTypeManager != null
     */
    protected boolean extractProcedures(
        final Map parameters,
        final DatabaseMetaData metaData,
        final MetadataManager metadataManager,
        final AntTablesElement tablesElement,
        final MetadataTypeManager metadataTypeManager)
    {
        return
            extractProcedures(
                parameters,
                metaData,
                metadataManager,
                tablesElement.getTables(),
                buildTableKey(),
                metadataTypeManager);
    }

    /**
     * Extracts the procedures..
     * @param parameters the parameters to handle.
     * @param metaData the database metadata.
     * @param metadataManager the metadata manager.
     * @param tableElements the table elements.
     * @param tableKey the key to store the tables.
     * @param metadataTypeManager the metadata type manager.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition metaData != null
     * @precondition metadataManager != null
     * @precondition tableElements != null
     * @precondition tableKey != null
     * @precondition metadataTypeManager != null
     */
    protected boolean extractProcedures(
        final Map parameters,
        final DatabaseMetaData metaData,
        final MetadataManager metadataManager,
        final Collection tableElements,
        final Object tableKey,
        final MetadataTypeManager metadataTypeManager)
    {
        boolean result = false;

        Iterator t_itTableElements = null;

        String[] t_astrTableNames = null;

        int t_iTableIndex = 0;

        Map t_mKeys = new HashMap();

        Collection t_cTables = null;

        Collection t_cFields = null;

        Object t_TableFieldsKey = null;

        Object t_TablePkKey = null;

        Collection t_cPks = null;

        int t_iLength = 0;

        AntTableElement t_Table = null;

        Collection t_cFieldElements = null;

        AntFieldElement t_Field = null;

        String[] t_astrTableFieldNames = null;

        Collection t_cFieldFks = null;

        Iterator t_itFieldElements = null;

        int t_iFieldIndex = 0;

        if  (tableElements != null)
        {
            t_itTableElements = tableElements.iterator();
        }

        if  (t_itTableElements != null)
        {
            while  (t_itTableElements.hasNext())
            {
                t_Table = (AntTableElement) t_itTableElements.next();

                if  (t_Table != null)
                {
                    t_cTables = (Collection) t_mKeys.get(tableKey);

                    if  (t_cTables == null)
                    {
                        t_cTables = new ArrayList();
                        t_mKeys.put(tableKey, t_cTables);
                    }

                    t_cTables.add(t_Table.getName());

                    t_cFieldElements = t_Table.getFields();

                    t_iLength =
                        (t_cFieldElements != null)
                        ?  t_cFieldElements.size()
                        :  0;

                    if  (t_iLength > 0)
                    {
                        t_astrTableFieldNames = new String[t_iLength];

                        t_itFieldElements = t_cFieldElements.iterator();

                        t_iFieldIndex = 0;

                        if  (t_itFieldElements != null)
                        {
                            while  (t_itFieldElements.hasNext())
                            {
                                t_Field =
                                    (AntFieldElement) t_itFieldElements.next();

                                if  (t_Field != null)
                                {
                                    t_astrTableFieldNames[t_iFieldIndex] =
                                        t_Field.getName();

                                    metadataManager.addColumnType(
                                        t_Table.getName(),
                                        t_Field.getName(),
                                        metadataTypeManager.getJavaType(
                                            t_Field.getType()));

                                    t_TableFieldsKey =
                                        buildTableFieldsKey(t_Table.getName());

                                    t_cFields =
                                        (Collection) t_mKeys.get(t_TableFieldsKey);

                                    if  (t_cFields == null)
                                    {
                                        t_cFields = new ArrayList();
                                        t_mKeys.put(t_TableFieldsKey, t_cFields);
                                    }

                                    t_cFields.add(t_Field.getName());

                                    if  (t_Field.isPk())
                                    {
                                        metadataManager.addPrimaryKey(
                                            t_Table.getName(),
                                            t_Field.getName());

                                        t_TablePkKey =
                                            buildPkKey(t_Table.getName());

                                        t_cPks =
                                            (Collection) t_mKeys.get(t_TablePkKey);

                                        if  (t_cPks == null)
                                        {
                                            t_cPks = new ArrayList();
                                            t_mKeys.put(t_TablePkKey, t_cPks);
                                        }

                                        t_cPks.add(t_Field.getName());

                                        t_mKeys.put(
                                            buildPkKey(
                                                t_Table.getName(),
                                                t_Field.getName()),
                                            t_Field.getName());
                                    }

                                    t_cFieldFks = t_Field.getFieldFks();

                                    if  (t_cFieldFks != null)
                                    {
                                        t_mKeys.put(
                                            buildFkKey(
                                                t_Table.getName(),
                                                t_Field.getName()),
                                            t_cFieldFks);
                                    }
                                }

                                t_iFieldIndex++;
                            }

                            metadataManager.addColumnNames(
                                t_Table.getName(),
                                t_astrTableFieldNames);
                        }
                    }
                }
            }
        }

        if  (t_cTables != null)
        {
            extractForeignKeys(
                t_cTables, t_mKeys, metadataManager, tableKey);
        }

        return result;
    }

    /**
     * Extracts the procedures..
     * @param extractedMap the already-extracted information.
     * @param metadataManager the database metadata manager.
     * @param tableKey the key to store the tables.
     * @throws BuildException if the build process cannot be performed.
     * @precondition tables != null
     * @precondition extractedMap != null
     * @precondition metadataManager != null
     * @precondition tableKey != null
     */
    protected void extractForeignKeys(
        final Collection tables,
        final Map extractedMap,
        final MetadataManager metadataManager,
        final Object tableKey)
    {
        Iterator t_itTables = tables.iterator();

        if  (t_itTables != null)
        {
            while  (t_itTables.hasNext())
            {
                String t_strTableName = (String) t_itTables.next();

                if  (t_strTableName != null)
                {
                    Collection t_cFields =
                        (Collection)
                            extractedMap.get(
                                buildTableFieldsKey(t_strTableName));

                    if  (t_cFields != null)
                    {
                        Iterator t_itFields = t_cFields.iterator();

                        if  (t_itFields != null)
                        {
                            while  (t_itFields.hasNext())
                            {
                                String t_strFieldName =
                                    (String) t_itFields.next();

                                Collection t_cFieldFks =
                                    (Collection)
                                        extractedMap.get(
                                            buildFkKey(
                                                t_strTableName,
                                                t_strFieldName));

                                Iterator t_itFieldFks = null;

                                if  (t_cFieldFks != null)
                                {
                                    t_itFieldFks =
                                        t_cFieldFks.iterator();
                                }

                                if  (t_itFieldFks != null)
                                {
                                    while  (t_itFieldFks.hasNext())
                                    {
                                        AntFieldFkElement t_FieldFk =
                                            (AntFieldFkElement)
                                                t_itFieldFks.next();

                                        if  (t_FieldFk != null)
                                        {
                                            metadataManager.addForeignKey(
                                                t_strTableName,
                                                new String[] {t_strFieldName},
                                                t_FieldFk.getTable(),
                                                new String[]
                                                {
                                                    t_FieldFk.getField()
                                                });
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Retrieves the database metadata using the JDBC connection stored in the
     * attribute map.
     * @param parameters the parameter map.
     * @return the metadata instance.
     * @throws BuildException if the retrieval process cannot be performed.
     */
    protected DatabaseMetaData retrieveMetadata(final Map parameters)
        throws  BuildException
    {
        DatabaseMetaData result = null;

        if  (parameters != null) 
        {
            result =
                retrieveMetadata(
                    (Connection)
                        parameters.get(
                            JdbcConnectionOpeningHandler.JDBC_CONNECTION));
        }

        return result;
    }

    /**
     * Retrieves the database metadata.
     * @param connection the JDBC connection.
     * @return the metadata instance.
     * @throws org.apache.tools.ant.BuildException whenever the required
     * parameters are not present or valid.
     */
    protected DatabaseMetaData retrieveMetadata(final Connection connection)
        throws  BuildException
    {
        DatabaseMetaData result = null;

        if  (connection != null)
        {
            try 
            {
                result = connection.getMetaData();
            }
            catch  (final SQLException exception)
            {
                throw new BuildException(exception);
            }
        }

        return result;
    }

    /**
     * Retrieves the tables XML element stored in the
     * attribute map.
     * @param parameters the parameter map.
     * @return the table information.
     * @throws BuildException if the retrieval process cannot be performed.
     * @precondition parameters != null
     */
    protected AntTablesElement retrieveTablesElement(final Map parameters)
        throws  BuildException
    {
        return
            (AntTablesElement)
                parameters.get(ParameterValidationHandler.TABLES);
    }

    /**
     * Retrieves whether the table extraction is disabled or not.
     * @param parameters the parameter map.
     * @return such setting.
     * @throws BuildException if the retrieval process cannot be performed.
     */
    protected boolean retrieveExtractTables(final Map parameters)
        throws  BuildException
    {
        return
            retrieveBoolean(
                ParameterValidationHandler.EXTRACT_TABLES, parameters);
    }

    /**
     * Retrieves whether the procedure extraction is disabled or not.
     * @param parameters the parameter map.
     * @return such setting.
     * @throws BuildException if the retrieval process cannot be performed.
     */
    protected boolean retrieveExtractProcedures(final Map parameters)
        throws  BuildException
    {
        return
            retrieveBoolean(
                ParameterValidationHandler.EXTRACT_PROCEDURES, parameters);
    }

    /**
     * Retrieves a boolean value stored in the attribute map.
     * @param name the key name.
     * @param parameters the parameter map.
     * @return the boolean value.
     * @throws BuildException if the retrieval process cannot be performed.
     * @precondition parameters != null
     */
    protected boolean retrieveBoolean(final String name, final Map parameters)
        throws  BuildException
    {
        boolean result = false;

        Object t_Flag = parameters.get(name);

        if  (   (t_Flag != null)
             && (t_Flag instanceof Boolean))
        {
            result = ((Boolean) t_Flag).booleanValue();
        }

        return result;
    }

    /**
     * Retrieves the database metadata manager using the database metadata and
     * parameters stored in the attribute map.
     * @param disableTableExtraction if the table metadata should not be
     * extracted.
     * @param lazyTableExtraction if the table metadata should not be
     * extracted inmediately.
     * @param disableProcedureExtraction if the procedure metadata should not be
     * extracted.
     * @param lazyProcedureExtraction if the procedure metadata should not be
     * extracted inmediately.
     * @param parameters the parameter map.
     * @return the metadata manager instance.
     * @throws BuildException if the retrieval process cannot be performed.
     * @precondition parameters != null
     */
    protected MetadataManager buildMetadataManager(
        final boolean disableTableExtraction,
        final boolean lazyTableExtraction,
        final boolean disableProcedureExtraction,
        final boolean lazyProcedureExtraction,
        final Map parameters)
      throws  BuildException
    {
        MetadataManager result = null;

        if  (parameters != null) 
        {
            result = retrieveMetadataManager(parameters);

            if  (result == null) 
            {
                result =
                    buildMetadataManager(
                        (String[]) parameters.get(TABLE_NAMES),
                        (String[]) parameters.get(PROCEDURE_NAMES),
                        disableTableExtraction,
                        lazyTableExtraction,
                        disableProcedureExtraction,
                        lazyProcedureExtraction,
                        retrieveDatabaseMetaData(parameters),
                        (String)
                            parameters.get(
                                ParameterValidationHandler.JDBC_CATALOG),
                        (String)
                            parameters.get(
                                ParameterValidationHandler.JDBC_SCHEMA));
            }
        }

        if  (result != null)
        {
            try 
            {
                result.retrieveMetadata();
            }
            catch  (final SQLException sqlException)
            {
                throw
                    new BuildException(
                        "Cannot retrieve metadata.", sqlException);
            }
            catch  (final QueryJException queryjException)
            {
                throw
                    new BuildException(
                        "Cannot retrieve metadata.", queryjException);
            }
        }
        
        return result;
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
                new JdbcMetadataManager(
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
        catch  (final Exception exception)
        {
            Log t_Log = UniqueLogFactory.getLog(getClass());
                
            if  (t_Log != null)
            {
                t_Log.error(
                    "Cannot build a database metadata manager.",
                    exception);
            }

            throw new BuildException(exception);
        }

        return result;
    }

    /**
     * Stores the database metadata in the attribute map.
     * @param metaData the database metadata.
     * @param parameters the parameter map.
     * @throws BuildException if the metadata cannot be stored for any reason.
     * @precondition metaData != null
     * @precondition parameters != null
     */
    protected void storeMetadata(
        final DatabaseMetaData metaData, final Map parameters)
      throws  BuildException
    {
        parameters.put(DATABASE_METADATA, metaData);
    }

    /**
     * Stores a flag indicating the metadata extraction has already been done.
     * @param parameters the parameter map.
     * @precondition parameters != null
     */
    protected void storeAlreadyDoneFlag(final Map parameters)
    {
        parameters.put(METADATA_EXTRACTION_ALREADY_DONE, Boolean.TRUE);
    }

    /**
     * Retrieves the flag which indicates whether the metadata extraction
     * has been done already.
     * @param parameters the parameter map.
     * @precondition metaData != null
     */
    protected boolean retrieveAlreadyDoneFlag(final Map parameters)
    {
        boolean result = false;

        Object t_Flag = parameters.get(METADATA_EXTRACTION_ALREADY_DONE);

        if  (   (t_Flag  != null)
             && (t_Flag instanceof Boolean))
        {
            result = ((Boolean) t_Flag).booleanValue();
        }

        return result;
    }

    /**
     * Stores the table names in the attribute map.
     * @param tableNames the table names.
     * @param parameters the parameter map.
     * @throws BuildException if the table names cannot be stored for any reason.
     * @precondition tableNames != null
     * @precondition parameters != null
     */
    protected void storeTableNames(
        final String[] tableNames, final Map parameters)
      throws  BuildException
    {
        parameters.put(TABLE_NAMES, tableNames);
    }

    /**
     * Stores the database metadata manager in the attribute map.
     * @param metadataManager the metadata manager.
     * @param parameters the parameter map.
     * @throws BuildException if the manager cannot be stored for any reason.
     * @precondition metadataManager != null
     * @precondition parameters != null
     */
    protected void storeMetadataManager(
        final MetadataManager metadataManager, final Map parameters)
      throws  BuildException
    {
        parameters.put(METADATA_MANAGER, metadataManager);
    }

    /**
     * Builds the table key.
     * @return the map key.
     */
    protected Object buildTableKey()
    {
        return "'@'@'table";
    }

    /**
     * Builds the table fields key.
     * @return the map key.
     */
    protected Object buildTableFieldsKey(final Object key)
    {
        return ".98.table'@'@'fields`p" + key;
    }

    /**
     * Builds a pk key using given object.
     * @param firstKey the first object key.
     * @return the map key.
     */
    protected Object buildPkKey(final Object firstKey)
    {
        return ".|\\|.pk" + firstKey;
    }

    /**
     * Builds a pk key using given object.
     * @param firstKey the first object key.
     * @param secondKey the second object key.
     * @return the map key.
     */
    protected Object buildPkKey(
        final Object firstKey, final Object secondKey)
    {
        return buildPkKey(firstKey) + "-.,.,-" + secondKey;
    }

    /**
     * Builds a fk key using given object.
     * @param firstKey the first object key.
     * @return the map key.
     */
    protected Object buildFkKey(final Object firstKey)
    {
        return "==fk''" + firstKey;
    }

    /**
     * Builds a fk key using given object.
     * @param firstKey the first object key.
     * @param secondKey the second object key.
     * @return the map key.
     */
    protected Object buildFkKey(
        final Object firstKey, final Object secondKey)
    {
        return buildFkKey(firstKey) + ".,.," + secondKey;
    }

    /**
     * Checks whether the database vendor matches this handler.
     * @param metaData the database metadata.
     * @return <code>true</code> in case it matches.
     * @throws BuildException if the check fails.
     * @precondition metaData != null
     */
    protected boolean checkVendor(final DatabaseMetaData metaData)
        throws  BuildException
    {
        boolean result = false;

        BuildException t_ExceptionToThrow = null;

        Log t_Log = UniqueLogFactory.getLog(getClass());

        String t_strProduct = null;
        String t_strVersion = null;
        int t_iMajorVersion = 0;
        int t_iMinorVersion = 0;

        try
        {
            t_strProduct = metaData.getDatabaseProductName();
        }
        catch  (final SQLException sqlException)
        {
            if  (t_Log != null)
            {
                t_Log.error(
                    "Cannot retrieve database vendor's product name.",
                    sqlException);
            }

            t_ExceptionToThrow =
                new BuildException(
                    "cannot.retrieve.vendor.product.name",
                    sqlException);
        }

        if  (t_ExceptionToThrow == null)
        {
            try
            {
                t_strVersion = metaData.getDatabaseProductVersion();
            }
            catch  (final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.warn(
                        "Cannot retrieve database vendor's product version.",
                        sqlException);
                }
            }

            try
            {
                t_iMajorVersion = metaData.getDatabaseMajorVersion();
            }
            catch  (final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.warn(
                          "Cannot retrieve database vendor's major version "
                        + "number.",
                        sqlException);
                }
            }

            try
            {
                t_iMinorVersion = metaData.getDatabaseMinorVersion();
            }
            catch  (final SQLException sqlException)
            {
                if  (t_Log != null)
                {
                    t_Log.warn(
                          "Cannot retrieve database vendor's major version "
                        + "number.",
                        sqlException);
                }
            }

            result =
                checkVendor(
                    t_strProduct,
                    t_strVersion,
                    t_iMajorVersion,
                    t_iMinorVersion);
        }
        else
        {
            throw t_ExceptionToThrow;
        }

        return result;
    }

    /**
     * Checks whether the database vendor matches this handler.
     * @param product the product name.
     * @param version the product version.
     * @param major the major version number.
     * @param minor the minor version number.
     * @return <code>true</code> in case it matches.
     * @precondition product != null
     */
    protected abstract boolean checkVendor(
        final String productName,
        final String productVersion,
        final int majorVersion,
        final int minorVersion);
}
