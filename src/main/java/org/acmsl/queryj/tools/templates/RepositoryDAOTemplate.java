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
 * Filename: RepositoryDAOTemplate.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Is able to generate DAO repository implementations according
 *              to database metadata.
 *
 */
package org.acmsl.queryj.tools.templates;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.tools.customsql.CustomSqlProvider;
import org.acmsl.queryj.tools.metadata.DecoratorFactory;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.metadata.MetadataTypeManager;
import org.acmsl.queryj.tools.templates.dao.DAOTemplateUtils;

/*
 * Importing some StringTemplate classes.
 */
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

/*
 * Importing some ACM-SL Commons classes.
 */
import org.acmsl.commons.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Importing some JDK classes.
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Is able to generate DAO repository implementations according to database
 * metadata.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class RepositoryDAOTemplate
    extends  BasePerRepositoryTemplate
{
    /**
     * Builds a <code>RepositoryDAOTemplate</code> using given
     * information.
     * @param metadataManager the database metadata manager.
     * @param metadataTypeManager the database metadata type manager.
     * @param customSqlProvider the <code>CustomSqlProvider</code> instance.
     * @param header the header.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param packageName the package name.
     * @param basePackageName the base package name.
     * @param repositoryName the repository name.
     * @param engineName the engine name.
     * @param tables the tables.
     */
    public RepositoryDAOTemplate(
        final MetadataManager metadataManager,
        final MetadataTypeManager metadataTypeManager,
        final CustomSqlProvider customSqlProvider,
        final String header,
        final DecoratorFactory decoratorFactory,
        final String packageName,
        final String basePackageName,
        final String repositoryName,
        final String engineName,
        final Collection tables)
    {
        super(
            metadataManager,
            metadataTypeManager,
            customSqlProvider,
            header,
            decoratorFactory,
            packageName,
            basePackageName,
            repositoryName,
            engineName,
            tables);
    }

    /**
     * Fills the template parameters.
     * @param input the parameter container.
     * @param template the template.
     * @param header the header.
     * @param metadataManager the database metadata manager.
     * @param metadataTypeManager the database metadata type manager.
     * @param customSqlProvider the <code>CustomSqlProvider</code> instance.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param subpackageName the subpackage name.
     * @param basePackageName the base package name.
     * @param timestamp the timestamp.
     * @param tableRepositoryName the table repository.
     * @param engineName the engine name.
     * @param tables the table names.
     * @param timestamp the timestamp.
     * @param copyrightYears the copyright years.
     * @param stringUtils the <code>StringUtils</code> instance.
     * @precondition input != null
     * @precondition template != null
     * @precondition customSqlProvider != null
     * @precondition metadataManager != null
     * @precondition metadataTypeManager != null
     * @precondition decoratorFactory != null
     * @precondition basePackageName != null
     * @precondition subpackageName != null
     * @precondition tableRepositoryName != null
     * @precondition tables != null
     * @precondition timestamp != null
     * @precondition copyrightYears != null
     * @precondition stringUtils != null
     */
    protected void fillParameters(
        @NotNull final Map input,
        @NotNull final StringTemplate template,
        final String header,
        final MetadataManager metadataManager,
        final MetadataTypeManager metadataTypeManager,
        final CustomSqlProvider customSqlProvider,
        @NotNull final DecoratorFactory decoratorFactory,
        final String subpackageName,
        final String basePackageName,
        final String tableRepositoryName,
        @NotNull final String engineName,
        @NotNull final Collection tables,
        final String timestamp,
        final Integer[] copyrightYears,
        final StringUtils stringUtils)
    {
        super.fillParameters(
            input,
            template,
            header,
            metadataManager,
            metadataTypeManager,
            customSqlProvider,
            decoratorFactory,
            subpackageName,
            basePackageName,
            tableRepositoryName,
            engineName,
            tables,
            timestamp,
            copyrightYears,
            stringUtils);

        fillParameters(
            input,
            template,
            header,
            metadataManager,
            metadataTypeManager,
            customSqlProvider,
            decoratorFactory,
            subpackageName,
            basePackageName,
            tableRepositoryName,
            engineName,
            tables,
            timestamp,
            copyrightYears,
            stringUtils,
            DAOTemplateUtils.getInstance(),
            TemplateUtils.getInstance());
    }

    /**
     * Fills the template parameters.
     * @param input the parameter container.
     * @param template the template.
     * @param header the header.
     * @param metadataManager the database metadata manager.
     * @param metadataTypeManager the database metadata type manager.
     * @param customSqlProvider the <code>CustomSqlProvider</code> instance.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param subpackageName the subpackage name.
     * @param basePackageName the base package name.
     * @param timestamp the timestamp.
     * @param tableRepositoryName the table repository.
     * @param engineName the engine name.
     * @param tables the table names.
     * @param timestamp the timestamp.
     * @param copyrightYears the copyright years.
     * @param stringUtils the <code>StringUtils</code> instance.
     * @param daoTemplateUtils the <code>DAOTemplateUtils</code> instance.
     * @param templateUtils the <code>TemplateUtils</code> instance.
     * @precondition input != null
     * @precondition template != null
     * @precondition metadataManager != null
     * @precondition metadataTypeManager != null
     * @precondition customSqlProvider != null
     * @precondition decoratorFactory != null
     * @precondition basePackageName != null
     * @precondition subpackageName != null
     * @precondition tableRepositoryName != null
     * @precondition tables != null
     * @precondition timestamp != null
     * @precondition copyrightYears != null
     * @precondition stringUtils != null
     * @precondition daoTemplateUtils != null
     * @precondition templateUtils != null
     */
    protected void fillParameters(
        @NotNull final Map input,
        final StringTemplate template,
        final String header,
        final MetadataManager metadataManager,
        final MetadataTypeManager metadataTypeManager,
        final CustomSqlProvider customSqlProvider,
        @NotNull final DecoratorFactory decoratorFactory,
        final String subpackageName,
        final String basePackageName,
        final String tableRepositoryName,
        final String engineName,
        final Collection tables,
        final String timestamp,
        final Integer[] copyrightYears,
        final StringUtils stringUtils,
        @NotNull final DAOTemplateUtils daoTemplateUtils,
        @NotNull final TemplateUtils templateUtils)
    {
        @Nullable Collection t_cCustomSelects =
            retrieveCustomSelects(
                customSqlProvider,
                metadataManager,
                metadataTypeManager,
                decoratorFactory,
                daoTemplateUtils,
                templateUtils);

        input.put("custom_selects", t_cCustomSelects);

        @Nullable Collection t_cCustomUpdatesOrInserts =
            retrieveCustomUpdatesOrInserts(
                customSqlProvider,
                metadataManager,
                metadataTypeManager,
                decoratorFactory,
                daoTemplateUtils,
                templateUtils);

        input.put("custom_updates_or_inserts", t_cCustomUpdatesOrInserts);

        @Nullable Collection t_cCustomSelectsForUpdate =
            retrieveCustomSelectsForUpdate(
                customSqlProvider,
                metadataManager,
                metadataTypeManager,
                decoratorFactory,
                daoTemplateUtils,
                templateUtils);

        input.put("custom_selects_for_update", t_cCustomSelectsForUpdate);

        @Nullable Collection t_cCustomResults =
            retrieveCustomResults(
                customSqlProvider,
                metadataManager,
                decoratorFactory,
                daoTemplateUtils,
                templateUtils);

        input.put("custom_results", t_cCustomResults);
    }

    /**
     * Retrieves the custom selects.
     * @param customSqlProvider the provider.
     * @param metadataManager the database metadata manager.
     * @param metadataTypeManager the metadata type manager.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param daoTemplateUtils the <code>DAOTemplateUtils</code> instance.
     * @param templateUtils the <code>TemplateUtils</code> instance.
     * @return the custom selects.
     * @precondition metadataManager != null
     * @precondition metadataTypeManager != null
     * @precondition decoratorFactory != null
     * @precondition daoTemplateUtils != null
     * @precondition templateUtils != null
     * @precondition templateUtils != null
     */
    @Nullable
    protected Collection retrieveCustomSelects(
        @Nullable final CustomSqlProvider customSqlProvider,
        final MetadataManager metadataManager,
        final MetadataTypeManager metadataTypeManager,
        @NotNull final DecoratorFactory decoratorFactory,
        @NotNull final DAOTemplateUtils daoTemplateUtils,
        @NotNull final TemplateUtils templateUtils)
    {
        @Nullable Collection result = null;

        if  (customSqlProvider != null)
        {
            result =
                templateUtils.retrieveCustomSelects(
                    customSqlProvider,
                    metadataManager,
                    metadataTypeManager,
                    decoratorFactory,
                    daoTemplateUtils);
        }

        if  (result == null)
        {
            result = new ArrayList();
        }

        return result;
    }

    /**
     * Retrieves the custom updates or inserts.
     * @param customSqlProvider the provider.
     * @param metadataManager the database metadata manager.
     * @param metadataTypeManager the metadata type manager.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param daoTemplateUtils the <code>DAOTemplateUtils</code> instance.
     * @param templateUtils the <code>TemplateUtils</code> instance.
     * @return the custom sql.
     * @precondition metadataManager != null
     * @precondition metadataTypeManager != null
     * @precondition decoratorFactory != null
     * @precondition daoTemplateUtils != null
     * @precondition templateUtils != null
     */
    @Nullable
    protected Collection retrieveCustomUpdatesOrInserts(
        @Nullable final CustomSqlProvider customSqlProvider,
        final MetadataManager metadataManager,
        final MetadataTypeManager metadataTypeManager,
        @NotNull final DecoratorFactory decoratorFactory,
        @NotNull final DAOTemplateUtils daoTemplateUtils,
        @NotNull final TemplateUtils templateUtils)
    {
        @Nullable Collection result = null;

        if  (customSqlProvider != null)
        {
            result =
                templateUtils.retrieveCustomUpdatesOrInserts(
                    customSqlProvider,
                    metadataManager,
                    metadataTypeManager,
                    decoratorFactory,
                    daoTemplateUtils);
        }

        if  (result == null)
        {
            result = new ArrayList();
        }

        return result;
    }

    /**
     * Retrieves the custom selects.
     * @param customSqlProvider the provider.
     * @param metadataManager the database metadata manager.
     * @param metadataTypeManager the metadata type manager.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param daoTemplateUtils the <code>DAOTemplateUtils</code> instance.
     * @param templateUtils the <code>TemplateUtils</code> instance.
     * @return the custom selects.
     * @precondition metadataManager != null
     * @precondition metadataTypeManager != null
     * @precondition decoratorFactory != null
     * @precondition daoTemplateUtils != null
     * @precondition templateUtils != null
     */
    @Nullable
    protected Collection retrieveCustomSelectsForUpdate(
        @Nullable final CustomSqlProvider customSqlProvider,
        final MetadataManager metadataManager,
        final MetadataTypeManager metadataTypeManager,
        @NotNull final DecoratorFactory decoratorFactory,
        @NotNull final DAOTemplateUtils daoTemplateUtils,
        @NotNull final TemplateUtils templateUtils)
    {
        @Nullable Collection result = null;

        if  (customSqlProvider != null)
        {
            result =
                templateUtils.retrieveCustomSelectsForUpdate(
                    customSqlProvider,
                    metadataManager,
                    metadataTypeManager,
                    decoratorFactory,
                    daoTemplateUtils);
        }

        if  (result == null)
        {
            result = new ArrayList();
        }

        return result;
    }

    /**
     * Retrieves the custom results.
     * @param customSqlProvider the provider.
     * @param metadataManager the database metadata manager.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param daoTemplateUtils the <code>DAOTemplateUtils</code> instance.
     * @param templateUtils the <code>TemplateUtils</code> instance.
     * @return the custom results.
     * @precondition tableName != null
     * @precondition metadataManager != null
     * @precondition decoratorFactory != null
     * @precondition daoTemplateUtils != null
     * @precondition templateUtils != null
     */
    @Nullable
    protected Collection retrieveCustomResults(
        @Nullable final CustomSqlProvider customSqlProvider,
        final MetadataManager metadataManager,
        final DecoratorFactory decoratorFactory,
        @NotNull final DAOTemplateUtils daoTemplateUtils,
        @NotNull final TemplateUtils templateUtils)
    {
        @Nullable Collection result = null;

        if  (customSqlProvider != null)
        {
            result =
                templateUtils.retrieveCustomResults(
                    customSqlProvider,
                    metadataManager,
                    decoratorFactory,
                    daoTemplateUtils);
        }

        if  (result == null)
        {
            result = new ArrayList();
        }

        return result;
    }

    /**
     * Retrieves the string template group.
     * @return such instance.
     */
    @Nullable
    @Override
    public StringTemplateGroup retrieveGroup()
    {
        return retrieveGroup("/org/acmsl/queryj/dao/RepositoryDAO.stg");
    }

    /**
     * Retrieves the template name.
     * @return such information.
     */
    @NotNull
    public String getTemplateName()
    {
        return "Repository DAO";
    }
}
