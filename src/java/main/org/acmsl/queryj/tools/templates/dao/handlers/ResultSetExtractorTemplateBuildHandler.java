//;-*- mode: java -*-
/*
                        QueryJ

    Copyright (C) 2002-2006  Jose San Leandro Armendariz
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
 * Filename: $RCSfile: $
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Builds a ResultSetExtractor template.
 *
 */
package org.acmsl.queryj.tools.templates.dao.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.QueryJBuildException;
import org.acmsl.queryj.tools.customsql.CustomSqlProvider;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.handlers.AbstractQueryJCommandHandler;
import org.acmsl.queryj.tools.handlers.ParameterValidationHandler;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.templates.dao.ResultSetExtractorTemplate;
import org.acmsl.queryj.tools.templates.dao.ResultSetExtractorTemplateFactory;
import org.acmsl.queryj.tools.templates.dao.ResultSetExtractorTemplateGenerator;
import org.acmsl.queryj.tools.templates.TableTemplate;
import org.acmsl.queryj.tools.templates.handlers.TableTemplateBuildHandler;
import org.acmsl.queryj.tools.templates.handlers.TemplateBuildHandler;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * Builds a ResultSetExtractor template.
 * @author <a href="mailto:chous@acm-sl.org"
 *         >Jose San Leandro</a>
 */
public class ResultSetExtractorTemplateBuildHandler
    extends    AbstractQueryJCommandHandler
    implements TemplateBuildHandler
{
    /**
     * Creates a <code>ResultSetExtractorTemplateBuildHandler</code>
     * instance.
     */
    public ResultSetExtractorTemplateBuildHandler() {};

    /**
     * Handles given information.
     * @param parameters the parameters.
     * @return <code>true</code> if the chain should be stopped.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     */
    protected boolean handle(final Map parameters)
        throws  QueryJBuildException
    {
        buildTemplates(parameters, retrieveDatabaseMetaData(parameters));

        return false;
    }

    /**
     * Builds the templates.
     * @param parameters the parameters.
     * @param metaData the database metadata.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition metaData != null
     */
    protected void buildTemplates(
        final Map parameters,
        final DatabaseMetaData metaData)
      throws  QueryJBuildException
    {
        try
        {
            buildTemplates(
                parameters,
                metaData.getDatabaseProductName(),
                metaData.getDatabaseProductVersion(),
                fixQuote(metaData.getIdentifierQuoteString()));
        }
        catch  (final SQLException sqlException)
        {
            throw
                new QueryJBuildException(
                      "Cannot retrieve database product name, "
                    + "version or quote string",
                    sqlException);
        }
    }

    /**
     * Builds the templates.
     * @param parameters the parameters.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the quote.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition engineName != null
     */
    protected void buildTemplates(
        final Map parameters,
        final String engineName,
        final String engineVersion,
        final String quote)
      throws  QueryJBuildException
    {
        buildTemplates(
            parameters,
            engineName,
            engineVersion,
            quote,
            retrieveMetadataManager(parameters),
            retrieveCustomSqlProvider(parameters),
            retrieveProjectPackage(parameters),
            retrieveTableRepositoryName(parameters),
            retrieveHeader(parameters),
            retrieveImplementMarkerInterfaces(parameters),
            ResultSetExtractorTemplateGenerator.getInstance(),
            filterTableTemplates(
                retrieveTableTemplates(parameters),
                retrieveCustomSqlProvider(parameters)));
    }

    /**
     * Builds the <code>ResultSetExtractor</code> templates..
     * @param parameters the parameters.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the quote symbol.
     * @param metadataManager the database metadata manager.
     * @param customSqlProvider the <code>CustomSqlProvider</code> instance.
     * @param basePackageName the base package name.
     * @param repository the repository.
     * @param header the header.
     * @param implementMarkerInterfaces whether to implement marker
     * interfaces.
     * @param templateFactory the template factory.
     * @param tableTemplates the table templates.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition engineName != null
     * @precondition metadataManager != null
     * @precondition customSqlProvider != null
     * @precondition packageName != null
     * @precondition basePackageName != null
     * @precondition repositoryName != null
     * @precondition templateFactory != null
     * @precondition tableTemplates != null
     */
    protected void buildTemplates(
        final Map parameters,
        final String engineName,
        final String engineVersion,
        final String quote,
        final MetadataManager metadataManager,
        final CustomSqlProvider customSqlProvider,
        final String basePackageName,
        final String repositoryName,
        final String header,
        final boolean implementMarkerInterfaces,
        final ResultSetExtractorTemplateFactory templateFactory,
        final TableTemplate[] tableTemplates)
      throws  QueryJBuildException
    {
        int t_iLength = (tableTemplates != null) ? tableTemplates.length : 0;
        
        ResultSetExtractorTemplate[] t_aTemplates =
            new ResultSetExtractorTemplate[t_iLength];

        TableTemplate t_TableTemplate;
        String t_strTableName;

        for  (int t_iIndex = 0; t_iIndex < t_iLength; t_iIndex++) 
        {
            t_TableTemplate = tableTemplates[t_iIndex];

            if  (t_TableTemplate != null)
            {
                t_strTableName = t_TableTemplate.getTableName();

                t_aTemplates[t_iIndex] =
                    templateFactory.createResultSetExtractorTemplate(
                        t_strTableName,
                        metadataManager,
                        customSqlProvider,
                        retrievePackage(
                            engineName, t_strTableName, parameters),
                        engineName,
                        engineVersion,
                        quote,
                        basePackageName,
                        repositoryName,
                        header,
                        implementMarkerInterfaces);
            }
        }

        storeTemplates(t_aTemplates, parameters);
    }

    /**
     * Filters the table templates to process.
     * @param tableTemplates the table templates.
     * @param customSqlProvider the custom SQL provider.
     * @return the filtered templates.
     * @precondition tableTemplates != null
     * @precondition customSqlProvider != null
     */
    protected TableTemplate[] filterTableTemplates(
        final TableTemplate[] tableTemplates,
        final CustomSqlProvider customSqlProvider)
    {
        return tableTemplates;
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param engineName the engine name.
     * @param tableName the table name.
     * @param parameters the parameter map.
     * @return the package name.
     * @precondition parameters != null
     * @precondition engineName != null
     * @precondition tableName != null
     */
    protected String retrievePackage(
        final String engineName,
        final String tableName,
        final Map parameters)
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
    {
        return
            packageUtils.retrieveResultSetExtractorPackage(
                projectPackage,
                engineName,
                tableName);
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
     * @precondition parameters != null
     */
    protected TableTemplate[] retrieveTableTemplates(final Map parameters)
    {
        return
            (TableTemplate[])
                parameters.get(TableTemplateBuildHandler.TABLE_TEMPLATES);
    }

    /**
     * Retrieves the custom-sql provider from the attribute map.
     * @param parameters the parameter map.
     * @return the provider.
     * @precondition parameters != null
     */
    public static CustomSqlProvider retrieveCustomSqlProvider(
        final Map parameters)
    {
        return
            DAOTemplateBuildHandler.retrieveCustomSqlProvider(parameters);
    }

    /**
     * Stores the templates in given attribute map.
     * @param templates the templates.
     * @param parameters the parameter map.
     * @precondition templates != null
     * @precondition parameters != null
     */
    protected void storeTemplates(
        final ResultSetExtractorTemplate[] templates,
        final Map parameters)
    {
        parameters.put(
            TemplateMappingManager.RESULTSET_EXTRACTOR_TEMPLATES,
            templates);
    }
}
