//;-*- mode: java -*-
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
 * Filename: RepositoryDAOFactoryTemplate.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Is able to generate repository DAO factories  according
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

/*
 * Importing some StringTemplate classes.
 */
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

/*
 * Importing some ACM-SL Commons classes.
 */
import org.acmsl.commons.utils.StringUtils;

/*
 * Importing some JDK classes.
 */
import java.util.Collection;
import java.util.Map;

/**
 * Is able to generate repository DAO factories according to database
 * metadata.
 * @author <a href="mailto:chous@acm-sl.org"
 *         >Jose San Leandro</a>
 */
public class RepositoryDAOFactoryTemplate
    extends  RepositoryDAOTemplate
{
    /**
     * The datasource's JNDI location.
     */
    private String m__strJNDIDataSource;

    /**
     * Builds a <code>RepositoryDAOFactoryTemplate</code> using given
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
     * @param jndiDataSource the JNDI location of the data source.
     * @param tables the tables.
     */
    public RepositoryDAOFactoryTemplate(
        final MetadataManager metadataManager,
        final MetadataTypeManager metadataTypeManager,
        final CustomSqlProvider customSqlProvider,
        final String header,
        final DecoratorFactory decoratorFactory,
        final String packageName,
        final String basePackageName,
        final String repositoryName,
        final String engineName,
        final String jndiDataSource,
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

        immutableSetJNDIDataSource(jndiDataSource);
    }


    /**
     * Specifies the JNDI data source.
     * @param jndiDataSource the new JNDI data source.
     */
    private void immutableSetJNDIDataSource(final String jndiDataSource)
    {
        m__strJNDIDataSource = jndiDataSource;
    }

    /**
     * Specifies the JNDI data source.
     * @param jndiDataSource the new JNDI data source.
     */
    protected void setJNDIDataSource(final String jndiDataSource)
    {
        immutableSetJNDIDataSource(jndiDataSource);
    }

    /**
     * Retrieves the JNDI data source.
     * @return such information.
     */
    public String getJNDIDataSource() 
    {
        return m__strJNDIDataSource;
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
        final Map input,
        final StringTemplate template,
        final String header,
        final MetadataManager metadataManager,
        final MetadataTypeManager metadataTypeManager,
        final CustomSqlProvider customSqlProvider,
        final DecoratorFactory decoratorFactory,
        final String subpackageName,
        final String basePackageName,
        final String tableRepositoryName,
        final String engineName,
        final Collection tables,
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

        input.put("jndi_location", getJNDIDataSource());
    }
    /**
     * Retrieves the string template group.
     * @return such instance.
     */
    protected StringTemplateGroup retrieveGroup()
    {
        return
            retrieveGroup(
                "/org/acmsl/queryj/dao/RepositoryDAOFactory.stg");
    }

    /**
     * Retrieves the template name.
     * @return such information.
     */
    public String getTemplateName()
    {
        return "Repository DAO Factory";
    }
}
