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
 * Filename: ConfigurationPropertiesTemplate.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Is able to generate the configuration file for configuring
 *              DAOChooser.
 *
 */
package org.acmsl.queryj.tools.templates.dao;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.tools.customsql.CustomSqlProvider;
import org.acmsl.queryj.tools.metadata.CachingTableDecorator;
import org.acmsl.queryj.tools.metadata.DecorationUtils;
import org.acmsl.queryj.tools.metadata.DecoratorFactory;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.metadata.MetadataTypeManager;
import org.acmsl.queryj.tools.metadata.TableDecorator;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.templates.BasePerRepositoryTemplate;

/*
 * Importing StringTemplate classes.
 */
import org.antlr.stringtemplate.StringTemplateGroup;

/*
 * Importing some ACM-SL Commons classes.
 */
import org.acmsl.commons.utils.StringUtils;

/*
 * Importing StringTemplate classes.
 */
import org.antlr.stringtemplate.StringTemplateGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Importing some JDK classes.
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Is able to generate the configuration file for configuring
 * DAOChooser.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class ConfigurationPropertiesTemplate
    extends  BasePerRepositoryTemplate
{
    /**
     * Builds a <code>ConfigurationPropertiesTemplate</code> using given
     * information.
     * @param metadataManager the database metadata manager.
     * @param metadataTypeManager the metadata type manager.
     * @param customSqlProvider the <code>CustomSqlProvider</code> instance.
     * @param header the header.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param packageName the package name.
     * @param basePackageName the base package name.
     * @param repositoryName the repository name.
     * @param engineName the engine name.
     * @param tables the tables.
     */
    public ConfigurationPropertiesTemplate(
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
     * Retrieves the string template group.
     * @return such instance.
     */
    @Nullable
    protected StringTemplateGroup retrieveGroup()
    {
        return retrieveGroup("/org/acmsl/queryj/ConfigurationProperties.stg");
    }

    /**
     * Retrieves the template name.
     * @return such information.
     */
    @NotNull
    public String getTemplateName()
    {
        return "ConfigurationProperties";
    }

    /**
     * Fills the core parameters.
     * @param input the input.
     * @param metadataManager the database metadata manager.
     * @param customSqlProvider the <code>CustomSqlProvider</code> instance.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param basePackageName the base package name.
     * @param subpackageName the subpackage name.
     * @param tableRepositoryName the table repository name.
     * @param engineName the engine name.
     * @param tables the tables.
     * @param timestamp the timestamp.
     * @param stringUtils the <code>StringUtils</code> instance.
     * @precondition input != null
     * @precondition metadataManager != null
     * @precondition customSqlProvider != null
     * @precondition decoratorFactory != null
     * @precondition subpackageName != null
     * @precondition basePackageName != null
     * @precondition tableRepositoryName != null
     * @precondition tables != null
     * @precondition timestamp != null
     * @precondition stringUtils != null
     */
    protected void fillCoreParameters(
        @NotNull final Map input,
        final MetadataManager metadataManager,
        final CustomSqlProvider customSqlProvider,
        @NotNull final DecoratorFactory decoratorFactory,
        final String basePackageName,
        final String subpackageName,
        final String tableRepositoryName,
        @NotNull final String engineName,
        @NotNull final Collection tables,
        final String timestamp,
        final StringUtils stringUtils)
    {
        super.fillCoreParameters(
            input,
            metadataManager,
            customSqlProvider,
            decoratorFactory,
            basePackageName,
            subpackageName,
            tableRepositoryName,
            engineName,
            tables,
            timestamp,
            stringUtils);

        input.put("engine_name", engineName);
        input.put("engine_name_lowercased", engineName.toLowerCase());

        @Nullable String t_strProcessedHeader = getProcessedHeader(input);
        if  (t_strProcessedHeader != null)
        {
            input.put("splitted_header", split(t_strProcessedHeader));
        }
    }

    /**
     * Splits given value into multiple lines.
     * @param value the text.
     * @return the splitted text.
     */
    protected String[] split(final String value)
    {
        return split(value, DecorationUtils.getInstance());
    }
    
    /**
     * Splits given value into multiple lines.
     * @param value the text.
     * @param decorationUtils the <code>DecorationUtils</code> instance.
     * @return the splitted text.
     */
    protected String[] split(
        final String value, @NotNull final DecorationUtils decorationUtils)
    {
        return decorationUtils.split(value);
    }
}
