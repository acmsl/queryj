/*
                        QueryJ

    Copyright (C) 2002-2005  Jose San Leandro Armendariz
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
 * Description: Builds a FkStatementSetter template.
 *
 */
package org.acmsl.queryj.tools.templates.dao.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.QueryJException;
import org.acmsl.queryj.tools.AntCommand;
import org.acmsl.queryj.tools.DatabaseMetaDataManager;
import org.acmsl.queryj.tools.handlers.AbstractAntCommandHandler;
import org.acmsl.queryj.tools.handlers.DatabaseMetaDataRetrievalHandler;
import org.acmsl.queryj.tools.handlers.ParameterValidationHandler;
import org.acmsl.queryj.tools.logging.QueryJLog;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.templates.dao.FkStatementSetterTemplate;
import org.acmsl.queryj.tools.templates.dao.FkStatementSetterTemplateFactory;
import org.acmsl.queryj.tools.templates.dao.FkStatementSetterTemplateGenerator;
import org.acmsl.queryj.tools.templates.TableTemplate;
import org.acmsl.queryj.tools.templates.handlers.TableTemplateBuildHandler;
import org.acmsl.queryj.tools.templates.handlers.TemplateBuildHandler;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.patterns.Command;

/*
 * Importing some Ant classes.
 */
import org.apache.tools.ant.BuildException;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Builds a FkStatementSetter template.
 * @author <a href="mailto:chous@acm-sl.org"
 *         >Jose San Leandro</a>
 */
public class FkStatementSetterTemplateBuildHandler
    extends    AbstractAntCommandHandler
    implements TemplateBuildHandler
{
    /**
     * A cached empty String array.
     */
    protected final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * A cached empty template array.
     */
    protected final FkStatementSetterTemplate[]
        EMPTY_FKSTATEMENTSETTERTEMPLATE_ARRAY =
            new FkStatementSetterTemplate[0];

    /**
     * Creates a FkStatementSetterTemplateBuildHandler.
     */
    public FkStatementSetterTemplateBuildHandler() {};

    /**
     * Handles given command.
     * @param command the command to handle.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition command != null
     */
    public boolean handle(final AntCommand command)
        throws  BuildException
    {
        return handle(command.getAttributeMap());
    }

    /**
     * Handles given information.
     * @param parameters the parameters.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition parameters != null
     */
    protected boolean handle(final Map parameters)
        throws  BuildException
    {
        return
            handle(
                parameters,
                retrieveDatabaseMetaData(parameters));
    }

    /**
     * Handles given information.
     * @param parameters the parameters.
     * @param metaData the database metadata.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition metaData != null
     */
    protected boolean handle(
        final Map parameters, final DatabaseMetaData metaData)
      throws  BuildException
    {
        boolean result = false;

        try
        {
            handle(
                parameters,
                metaData.getDatabaseProductName());
        }
        catch  (final SQLException sqlException)
        {
            throw new BuildException(sqlException);
        }

        return result;
    }

    /**
     * Handles given information.
     * @param parameters the parameters.
     * @param engineName the engine name.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition engineName != null
     */
    protected boolean handle(
        final Map parameters, final String engineName)
      throws  BuildException
    {
        return
            handle(
                parameters,
                engineName,
                retrieveDatabaseMetaDataManager(parameters),
                retrieveProjectPackage(parameters),
                retrieveTableRepositoryName(parameters),
                FkStatementSetterTemplateGenerator.getInstance(),
                retrieveTableTemplates(parameters));
    }

    /**
     * Handles given information.
     * @param parameters the parameters.
     * @param engineName the engine name.
     * @param metaDataManager the database metadata manager.
     * @param basePackageName the base package name.
     * @param repository the repository.
     * @param templateFactory the template factory.
     * @param tableTemplates the table templates.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition engineName != null
     * @precondition metaDataManager != null
     * @precondition packageName != null
     * @precondition basePackageName != null
     * @precondition repositoryName != null
     * @precondition templateFactory != null
     * @precondition tableTemplates != null
     */
    protected boolean handle(
        final Map parameters,
        final String engineName,
        final DatabaseMetaDataManager metaDataManager,
        final String basePackageName,
        final String repositoryName,
        final FkStatementSetterTemplateFactory templateFactory,
        final TableTemplate[] tableTemplates)
      throws  BuildException
    {
        boolean result = false;

        Collection t_cTemplates = new ArrayList();

        int t_iLength = (tableTemplates != null) ? tableTemplates.length : 0;
        
        try
        {
            for  (int t_iIndex = 0;
                      t_iIndex < t_iLength;
                      t_iIndex++) 
            {
                String[] t_astrFks =
                    metaDataManager.getForeignKeys(
                        tableTemplates[t_iIndex].getTableName());

                String[] t_astrSimpleFks =
                    retrieveSimpleFks(
                        t_astrFks,
                        tableTemplates[t_iIndex].getTableName(),
                        metaDataManager);

                for  (int t_iFkIndex = 0;
                          t_iFkIndex < t_astrSimpleFks.length;
                          t_iFkIndex++)
                {
                    t_cTemplates.add(
                        templateFactory.createFkStatementSetterTemplate(
                            tableTemplates[t_iIndex],
                            new String[] {t_astrFks[t_iFkIndex]},
                            metaDataManager,
                            retrievePackage(
                                engineName,
                                tableTemplates[t_iIndex].getTableName(),
                                parameters),
                            basePackageName,
                            repositoryName));
                }

                String[] t_astrReferredTables =
                    retrieveReferredTablesByCompoundFks(
                        tableTemplates[t_iIndex].getTableName(),
                        metaDataManager);

                int t_iReferredTablesLength =
                    (t_astrReferredTables != null)
                    ?  t_astrReferredTables.length
                    :  0;

                for  (int t_iReferredTableIndex = 0;
                          t_iReferredTableIndex < t_iReferredTablesLength;
                          t_iReferredTableIndex++)
                {
                    t_cTemplates.add(
                        templateFactory.createFkStatementSetterTemplate(
                            tableTemplates[t_iIndex],
                            metaDataManager.getForeignKeys(
                                tableTemplates[t_iIndex].getTableName(),
                                t_astrReferredTables[t_iReferredTableIndex]),
                            metaDataManager,
                            retrievePackage(
                                engineName,
                                tableTemplates[t_iIndex].getTableName(),
                                parameters),
                            basePackageName,
                            repositoryName));
                }
            }

            storeTemplates(
                (FkStatementSetterTemplate[])
                    t_cTemplates.toArray(
                        EMPTY_FKSTATEMENTSETTERTEMPLATE_ARRAY),
                parameters);
        }
        catch  (final QueryJException queryjException)
        {
            throw new BuildException(queryjException);
        }
        
        return result;
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param engineName the engine name.
     * @param tableName the table name.
     * @param parameters the parameter map.
     * @return the package name.
     * @throws BuildException if the package retrieval process if faulty.
     * @precondition parameters != null
     * @precondition engineName != null
     * @precondition tableName != null
     */
    protected String retrievePackage(
        final String engineName,
        final String tableName,
        final Map parameters)
      throws  BuildException
    {
        return
            retrievePackage(
                retrieveProjectPackage(parameters),
                engineName,
                tableName,
                PackageUtils.getInstance());
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param projectPackage the project package.
     * @param engineName the engine name.
     * @param tableName the table name.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @return the package name.
     * @throws BuildException if the package retrieval process if faulty.
     * @precondition engineName != null
     * @precondition projectPackage != null
     * @precondition tableName != null
     * @precondition packageUtils != null
     */
    protected String retrievePackage(
        final String projectPackage,
        final String engineName,
        final String tableName,
        final PackageUtils packageUtils)
      throws  BuildException
    {
        return
            packageUtils.retrieveFkStatementSetterPackage(
                projectPackage,
                engineName,
                tableName);
    }

    /**
     * Retrieves the database metadata manager from the attribute map.
     * @param parameters the parameter map.
     * @return the manager.
     * @throws BuildException if the manager retrieval process if faulty.
     * @precondition parameters != null
     */
    protected DatabaseMetaDataManager retrieveDatabaseMetaDataManager(
        final Map parameters)
      throws  BuildException
    {
        return
            (DatabaseMetaDataManager)
                parameters.get(
                    DatabaseMetaDataRetrievalHandler.DATABASE_METADATA_MANAGER);
    }

    /**
     * Retrieves the repository name.
     * @param parameters the parameters.
     * @return the repository's name.
     * @precondition parameters != null
     */
    protected String retrieveTableRepositoryName(final Map parameters)
    {
        return
            (String)
                parameters.get(
                    ParameterValidationHandler.REPOSITORY);
    }

    /**
     * Retrieves the table templates.
     * @param parameters the parameter map.
     * @return such templates.
     * @throws BuildException if the templates cannot be stored for any reason.
     * @precondition parameters != null
     */
    protected TableTemplate[] retrieveTableTemplates(final Map parameters)
        throws  BuildException
    {
        return
            (TableTemplate[])
                parameters.get(TableTemplateBuildHandler.TABLE_TEMPLATES);
    }

    /**
     * Stores the templates in given attribute map.
     * @param templates the templates.
     * @param parameters the parameter map.
     * @precondition templates != null
     * @precondition parameters != null
     */
    protected void storeTemplates(
        final FkStatementSetterTemplate[] templates,
        final Map parameters)
    {
        parameters.put(
            TemplateMappingManager.FK_STATEMENT_SETTER_TEMPLATES,
            templates);
    }

    /**
     * Checks whether the table associated to given template contains foreign keys.
     * @param tableTemplate the table template.
     * @param metaDataManager the database metadata manager.
     * @return <code>true</code> in such case.
     * @precondition tableTemplate != null
     * @precondition metaDataManager != null
     */
    protected boolean containsForeignKeys(
        final TableTemplate tableTemplate,
        final DatabaseMetaDataManager metaDataManager)
    {
        return metaDataManager.containsForeignKeys(tableTemplate.getTableName());
    }

    /**
     * Retrieves the simple foreign keys.
     * @param allFks the complete list of foreign keys.
     * @param tableName the table name.
     * @param metaDataManager the <code>DatabaseMetaDataManager</code> instance.
     * @precondition allFks != null
     * @precondition tableName != null
     * @precondition metaDataManager != null
     */
    protected String[] retrieveSimpleFks(
        final String[] allFks,
        final String tableName,
        final DatabaseMetaDataManager metaDataManager)
    {
        Collection t_cResult = new ArrayList();

        Map t_mAux = new HashMap();

        int t_iLength = (allFks != null) ? allFks.length : 0;

        for  (int t_iIndex = 0; t_iIndex < t_iLength; t_iIndex++)
        {
            String t_strReferredTable =
                metaDataManager.getReferredTable(tableName, allFks[t_iIndex]);

            if  (t_mAux.containsKey(t_strReferredTable))
            {
                t_mAux.remove(t_strReferredTable);
            }
            else
            {
                t_mAux.put(t_strReferredTable, allFks[t_iIndex]);
            }
        }

        t_cResult = t_mAux.values();

        return (String[]) t_cResult.toArray(EMPTY_STRING_ARRAY);
    }

    /**
     * Retrieves the tables referred by compound foreign keys.
     * @param tableName the table name.
     * @param metaDataManager the <code>DatabaseMetaDataManager</code> instance.
     * @precondition tableName != null
     * @precondition metaDataManager != null
     */
    protected String[] retrieveReferredTablesByCompoundFks(
        final String tableName,
        final DatabaseMetaDataManager metaDataManager)
    {
        Collection t_cResult = new ArrayList();

        String[] t_astrReferredTables = metaDataManager.getReferredTables(tableName);

        int t_iLength =
            (t_astrReferredTables != null) ? t_astrReferredTables.length : 0;

        for  (int t_iIndex = 0; t_iIndex < t_iLength; t_iIndex++)
        {
            String[] t_astrForeignKeys =
                metaDataManager.getForeignKeys(tableName, t_astrReferredTables[t_iIndex]);

            if  (   (t_astrForeignKeys != null)
                 && (t_astrForeignKeys.length > 0))
            {
                t_cResult.add(t_astrReferredTables[t_iIndex]);
            }
        }

        return (String[]) t_cResult.toArray(EMPTY_STRING_ARRAY);
    }
}
