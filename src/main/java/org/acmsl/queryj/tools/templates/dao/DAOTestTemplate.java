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
 * Filename: DAOTestTemplate.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Template for creating JUnit tests to ensure generated DAOs
 *              are working fine and the connection correctly managed.
 */
package org.acmsl.queryj.tools.templates.dao;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.tools.customsql.CustomSqlProvider;
import org.acmsl.queryj.tools.metadata.DecoratorFactory;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.templates.BasePerTableTemplate;

/*
 * Importing StringTemplate classes.
 */
import org.antlr.stringtemplate.StringTemplateGroup;

/**
 * Template for creating JUnit tests to ensure generated DAOs
 * are working fine and the connection correctly managed.
 * @author <a href="mailto:chous@acm-sl.org"
           >Jose San Leandro</a>
 */
public class DAOTestTemplate
    extends  BasePerTableTemplate
{
    /**
     * Builds a <code>DAOTestTemplate</code> using given information.
     * @param tableName the table name.
     * @param metadataManager the database metadata manager.
     * @param customSqlProvider the CustomSqlProvider instance.
     * @param header the header.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param packageName the package name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the identifier quote string.
     * @param basePackageName the base package name.
     * @param repositoryName the repository name.
     * @param jmx whether to support JMX.
     * @param jndiLocation the location of the datasource in JNDI.
     */
    public DAOTestTemplate(
        final String tableName,
        final MetadataManager metadataManager,
        final CustomSqlProvider customSqlProvider,
        final String header,
        final DecoratorFactory decoratorFactory,
        final String packageName,
        final String engineName,
        final String engineVersion,
        final String quote,
        final String basePackageName,
        final String repositoryName,
        final boolean jmx,
        final String jndiLocation)
    {
        super(
            tableName,
            metadataManager,
            customSqlProvider,
            header,
            decoratorFactory,
            packageName,
            engineName,
            engineVersion,
            quote,
            basePackageName,
            repositoryName,
            jmx,
            jndiLocation);
    }

    /**
     * Builds a key to store the template cache.
     * @return such key.
     */
    protected Object buildTemplateCacheKey()
    {
        return "//DAOTestTemplate//";
    }

    /**
     * Retrieves the string template group.
     * @return such instance.
     */
    protected StringTemplateGroup retrieveGroup()
    {
        return retrieveGroup("/org/acmsl/queryj/dao/DAOTest.stg");
    }

    /**
     * Retrieves the template name.
     * @return such information.
     */
    public String getTemplateName()
    {
        return "DAOTest";
    }
}
