//;-*- mode: java -*-
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
 * Filename: BasePerTableTemplateBuildHandler.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Builds a per-table templates using database metadata.
 *
 */
package org.acmsl.queryj.tools.templates.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.QueryJBuildException;
import org.acmsl.queryj.tools.customsql.CustomSqlProvider;
import org.acmsl.queryj.tools.metadata.DecoratorFactory;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.handlers.AbstractQueryJCommandHandler;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.templates.BasePerTableTemplate;
import org.acmsl.queryj.tools.templates.BasePerTableTemplateFactory;
import org.acmsl.queryj.tools.templates.dao.DAOTemplateUtils;
import org.acmsl.queryj.tools.templates.MetaLanguageUtils;
import org.acmsl.queryj.tools.templates.TableTemplate;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.logging.UniqueLogFactory;

/*
 * Importing some JDK classes.
 */
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/*
 * Importing some Apache Commons Logging classes.
 */
import org.apache.commons.logging.Log;

/*
 * Importing some JetBrains annotations.
 */
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Builds a per-table template using database metadata.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public abstract class BasePerTableTemplateBuildHandler
       <T extends BasePerTableTemplate,
        TF extends BasePerTableTemplateFactory<T>>
    extends AbstractQueryJCommandHandler
    implements TemplateBuildHandler
{
    /**
     * Creates a {@ BasePerTableTemplateBuildHandler} instance.
     */
    public BasePerTableTemplateBuildHandler() {}

    /**
     * Handles given information.
     *
     *
     * @param parameters the parameters.
     * @return <code>true</code> if the chain should be stopped.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     */
    protected boolean handle(@NotNull final Map parameters)
        throws  QueryJBuildException
    {
        buildTemplate(parameters, retrieveDatabaseMetaData(parameters));

        return false;
    }

    /**
     * Builds the template.
     * @param parameters the parameters.
     * @param metaData the database metadata.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition metaData != null
     */
    protected void buildTemplate(
        @NotNull final Map parameters, @NotNull final DatabaseMetaData metaData)
      throws  QueryJBuildException
    {
        try
        {
            buildTemplate(
                parameters,
                metaData.getDatabaseProductName(),
                retrieveDatabaseProductVersion(metaData),
                fixQuote(metaData.getIdentifierQuoteString()),
                retrieveMetadataManager(parameters));
        }
        catch  (@NotNull final SQLException sqlException)
        {
            throw
                new QueryJBuildException(
                    "Cannot build the table-specific template", sqlException);
        }
    }

    /**
     * Builds the template.
     * @param parameters the parameters.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the quote character.
     * @param metadataManager the {@link MetadataManager} instance.
     * @throws QueryJBuildException if the build process cannot be performed.
     */
    protected void buildTemplate(
        @NotNull final Map parameters,
        @Nullable final String engineName,
        @Nullable final String engineVersion,
        @Nullable final String quote,
        @NotNull final MetadataManager metadataManager)
      throws  QueryJBuildException
    {
        buildTemplate(
            parameters,
            engineName,
            engineVersion,
            quote,
            metadataManager,
            retrieveCustomSqlProvider(parameters),
            retrieveTemplateFactory(),
            retrieveProjectPackage(parameters),
            retrieveTableRepositoryName(parameters),
            retrieveHeader(parameters),
            retrieveImplementMarkerInterfaces(parameters),
            metadataManager.getTableNames());
    }

    /**
     * Retrieves the template factory.
     * @return such instance.
     */
    protected abstract TF retrieveTemplateFactory();

    /**
     * Builds the template.
     * @param parameters the parameters.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the quote character.
     * @param metadataManager the database metadata manager.
     * @param customSqlProvider the custom sql provider.
     * @param templateFactory the template factory.
     * @param projectPackage the project package.
     * @param repository the repository.
     * @param header the header.
     * @param implementMarkerInterfaces whether to implement marker
     * interfaces.
     * @param tableNames the table names.
     * @throws QueryJBuildException if the build process cannot be performed.
     */
    protected void buildTemplate(
        @NotNull final Map parameters,
        @NotNull final String engineName,
        @NotNull final String engineVersion,
        @NotNull final String quote,
        @NotNull final MetadataManager metadataManager,
        @NotNull final CustomSqlProvider customSqlProvider,
        @NotNull final TF templateFactory,
        @NotNull final String projectPackage,
        @NotNull final String repository,
        @NotNull final String header,
        final boolean implementMarkerInterfaces,
        @Nullable final String[] tableNames)
      throws  QueryJBuildException
    {
        int t_iLength = (tableNames != null) ? tableNames.length : 0;

        List<T> t_lTemplates = new ArrayList<T>();

        @Nullable T t_Template;

        String t_strTableName = null;

        for  (int t_iIndex = 0; t_iIndex < t_iLength; t_iIndex++) 
        {
            @Nullable TableTemplate t_TableTemplate = null;

            if (tableNames != null)
            {
                t_strTableName = tableNames[t_iIndex];
            }

            if (t_strTableName != null)
            {
                if (metadataManager.isGenerationAllowedForTable(t_strTableName))
                {
                    t_Template =
                        createTemplate(
                            templateFactory,
                            t_strTableName,
                            metadataManager,
                            customSqlProvider,
                            retrievePackage(
                                t_strTableName, engineName, parameters),
                            engineName,
                            engineVersion,
                            quote,
                            projectPackage,
                            repository,
                            header,
                            implementMarkerInterfaces,
                            parameters);

                    if  (t_Template != null)
                    {
                        t_lTemplates.add(t_Template);
                    }
                }
            }
        }

        storeTemplates(t_lTemplates, parameters);
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param tableName the table name.
     * @param engineName the engine name.
     * @param parameters the parameter map.
     * @return the package name.
     * @throws QueryJBuildException if the package retrieval process if faulty.
     * @precondition parameters != null
     */
    protected String retrievePackage(
        @NotNull final String tableName, @NotNull final String engineName, @NotNull final Map parameters)
      throws  QueryJBuildException
    {
        return
            retrievePackage(
                tableName,
                engineName,
                retrieveProjectPackage(parameters),
                PackageUtils.getInstance());
    }

    /**
     * Retrieves the package name.
     *
     *
     *
     * @param tableName the table name.
     * @param engineName the engine name.
     * @param projectPackage the project package.
     * @param packageUtils the {@link org.acmsl.queryj.tools.PackageUtils} instance.
     * @return the package name.
     * @precondition projectPackage != null
     * @precondition packageUtils != null
     */
    protected abstract String retrievePackage(
        @NotNull final String tableName,
        @NotNull final String engineName,
        @NotNull final String projectPackage,
        @NotNull final PackageUtils packageUtils) throws QueryJBuildException;

    /**
     * Stores the template collection in given attribute map.
     * @param templates the templates.
     * @param parameters the parameter map.
     * @precondition templates != null
     * @precondition parameters != null
     */
    protected abstract void storeTemplates(
        @NotNull final List<T> templates, @NotNull final Map parameters);

    /**
     * Retrieves the table templates.
     * @param parameters the parameter map.
     * @return such templates.
     * @throws QueryJBuildException if the templates cannot be stored for
     * any reason.
     * @precondition parameters != null
     */
    @Nullable
    protected TableTemplate[] retrieveTableTemplates(@NotNull final Map parameters)
        throws  QueryJBuildException
    {
        return
            (TableTemplate[])
                parameters.get(TableTemplateBuildHandler.TABLE_TEMPLATES);
    }

    /**
     * Creates a template with required information.
     * @param templateFactory the {@link BasePerTableTemplateFactory} instance.
     * @param tableName the table name.
     * @param metadataManager the {@link MetadataManager} instance.
     * @param customSqlProvider the {@link CustomSqlProvider} instance.
     * @param packageName the package name.
     * @param engineName the database engine name.
     * @param engineVersion the database engine version.
     * @param quote the quote identifier.
     * @param projectPackage the project's base package.
     * @param repository the repository name.
     * @param header the custom file header.
     * @param implementMarkerInterfaces whether to use marker interface or not.
     * @param parameters the parameter map.
     */
    @Nullable
    protected T createTemplate(
        @NotNull final TF templateFactory,
        @NotNull final String tableName,
        @NotNull final MetadataManager metadataManager,
        @NotNull final CustomSqlProvider customSqlProvider,
        @NotNull final String packageName,
        @NotNull final String engineName,
        @NotNull final String engineVersion,
        @NotNull final String quote,
        @NotNull final String projectPackage,
        @NotNull final String repository,
        @NotNull final String header,
        final boolean implementMarkerInterfaces,
        @NotNull final Map parameters)
      throws  QueryJBuildException
    {
        return
            templateFactory.createTemplate(
                tableName,
                metadataManager,
                customSqlProvider,
                packageName,
                engineName,
                engineVersion,
                quote,
                projectPackage,
                repository,
                header,
                implementMarkerInterfaces);
    }

    /**
     * Checks whether given table contains static values or not.
     * @param tableName the table name.
     * @param metadataManager the {@link MetadataManager} instance.
     * @return such information.
     * @precondition tableName != null
     * @precondition metadataManager != null
     */
    protected boolean isStaticTable(
        @NotNull final String tableName, @NotNull final MetadataManager metadataManager)
    {
        return
            isStaticTable(
                tableName, metadataManager, MetaLanguageUtils.getInstance());
    }

    /**
     * Checks whether given table contains static values or not.
     * @param tableName the table name.
     * @param metadataManager the {@link MetadataManager} instance.
     * @param metaLanguageUtils the {@link MetaLanguageUtils} instance.
     * @return such information.
     * @precondition tableName != null
     * @precondition metadataManager != null
     * @precondition metaLanguageUtils != null
     */
    protected boolean isStaticTable(
        @NotNull final String tableName,
        @NotNull final MetadataManager metadataManager,
        @NotNull final MetaLanguageUtils metaLanguageUtils)
    {
        return
            metaLanguageUtils.containsStaticValues(
                tableName, metadataManager);
    }

    /**
     * Checks whether given table contains static values or not.
     * @param parameters the parameter map.
     * @param tableName the table name.
     * @param metadataManager the {@link MetadataManager} instance.
     * @param decoratorFactory the decorator factory.
     * @return such information.
     * @precondition parameters != null
     * @precondition tableName != null
     * @precondition metadataManager != null
     * @precondition decoratorFactory != null
     */
    @Nullable
    protected Collection retrieveStaticContent(
        @NotNull final Map parameters,
        @NotNull final String tableName,
        @NotNull final MetadataManager metadataManager,
        @NotNull final DecoratorFactory decoratorFactory)
    {
        @Nullable Collection result =
            retrieveCachedStaticContent(parameters, tableName);

        if  (result == null)
        {
            try
            {
                result =
                    retrieveStaticContent(
                        tableName,
                        metadataManager,
                        decoratorFactory,
                        DAOTemplateUtils.getInstance());

                if (result != null)
                {
                    storeCachedStaticContent(result, parameters, tableName);
                }
            }
            catch  (@NotNull final SQLException sqlException)
            {
                Log t_Log =
                    UniqueLogFactory.getLog(
                        BasePerTableTemplateBuildHandler.class);
                
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Error retrieving static content for " + tableName,
                        sqlException);
                }
            }
        }

        return result;
    }

    /**
     * Checks whether given table contains static values or not.
     * @param tableName the table name.
     * @param metadataManager the {@link MetadataManager} instance.
     * @param decoratorFactory the decorator factory.
     * @param daoTemplateUtils the {@link DAOTemplateUtils} instance.
     * @return such information.
     * @throws SQLException if the operation fails.
     * @precondition tableName != null
     * @precondition metadataManager != null
     * @precondition decoratorFactory != null
     * @precondition daoTemplateUtils != null
     */
    @Nullable
    protected Collection retrieveStaticContent(
        @NotNull final String tableName,
        @NotNull final MetadataManager metadataManager,
        @NotNull final DecoratorFactory decoratorFactory,
        @NotNull final DAOTemplateUtils daoTemplateUtils)
      throws  SQLException
    {
        return
            daoTemplateUtils.queryContents(
                tableName, metadataManager, decoratorFactory);
    }

    /**
     * Retrieves the cached static content for given table.
     * @param parameters the parameter map.
     * @param tableName the table name.
     * @return such content, if present.
     * @precondition parameters != null
     * @precondition tableName != null
     */
    @Nullable
    protected Collection retrieveCachedStaticContent(
        @NotNull final Map parameters, @NotNull final String tableName)
    {
        return (Collection) parameters.get(buildStaticContentKey(tableName));
    }

    /**
     * Stores the static content of a concrete table.
     * @param contents the contents to cache.
     * @param parameters the parameters.
     * @param tableName the table name.
     * @precondition contents != null
     * @precondition parameters != null
     * @precondition tableName != null
     */
    @SuppressWarnings("unchecked")
    protected void storeCachedStaticContent(
        @NotNull final Collection contents,
        @NotNull final Map parameters,
        @NotNull final String tableName)
    {
        parameters.put(buildStaticContentKey(tableName), contents);
    }

    /**
     * Builds a key for storing the static content for given table.
     * @param tableName the table name.
     * @return such key.
     * @precondition tableName != null
     */
    @NotNull
    protected Object buildStaticContentKey(@NotNull final String tableName)
    {
        return "..static-contents-for-table-" + tableName + "..";
    }
}