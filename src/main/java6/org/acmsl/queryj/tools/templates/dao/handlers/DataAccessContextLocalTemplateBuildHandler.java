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
 * Filename: DataAccessContextLocalTemplateBuildHandler.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Builds the Spring DAO declaration file.
 *
 */
package org.acmsl.queryj.tools.templates.dao.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.QueryJException;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.customsql.CustomSqlProvider;
import org.acmsl.queryj.tools.handlers.ParameterValidationHandler;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.metadata.MetadataTypeManager;
import org.acmsl.queryj.tools.templates.BasePerRepositoryTemplate;
import org.acmsl.queryj.tools.templates.BasePerRepositoryTemplateFactory;
import org.acmsl.queryj.tools.templates.dao.DataAccessContextLocalTemplateFactory;
import org.acmsl.queryj.tools.templates.dao.DataAccessContextLocalTemplateGenerator;
import org.acmsl.queryj.tools.templates.handlers.BasePerRepositoryTemplateBuildHandler;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;

/*
 * Importing some Ant classes.
 */
import org.apache.tools.ant.BuildException;

/*
 * Importing some JDK classes.
 */
import java.util.Collection;
import java.util.Map;

/*
 * Importing some Commons-Collection classes.
 */
import org.apache.commons.logging.LogFactory;

/**
 * Builds the Spring DAO declaration file.
 * @author <a href="mailto:chous@acm-sl.org"
 *         >Jose San Leandro</a>
 */
public class DataAccessContextLocalTemplateBuildHandler
    extends  BasePerRepositoryTemplateBuildHandler
{
    /**
     * Creates a DataAccessContextLocalTemplateBuildHandler.
     */
    public DataAccessContextLocalTemplateBuildHandler() {};

    /**
     * Retrieves the template factory.
     * @return such instance.
     */
    protected BasePerRepositoryTemplateFactory retrieveTemplateFactory()
    {
        return DataAccessContextLocalTemplateGenerator.getInstance();
    }

    /**
     * Uses the factory to create the template.
     * @param metadataManager the <code>MetadataManager</code> instance.
     * @param metadataTypeManager the metadata type manager.
     * @param customSqlProvider the <code>CustomSqlProvider</code> instance.
     * @param factory the template factory.
     * @param packageName the package name.
     * @param projectPackage the base package.
     * @param repository the repository.
     * @param engineName the engine name.
     * @param tableNames the table names.
     * @param header the header.
     * @param jmx whether to support JMX or not.
     * @param tableNames the table names.
     * @param parameters the parameters.
     * @return the template.
     * @throws QueryJException on invalid input.
     * @precondition metadataManager != null
     * @precondition metadataTypeManager != null
     * @precondition customSqlProvider != null
     * @precondition packageName != null
     * @precondition projectPackage != null
     * @precondition repository != null
     * @precondition engineName != null
     * @precondition tableNames != null
     * @precondition factory != null
     */
    protected BasePerRepositoryTemplate createTemplate(
        final MetadataManager metadataManager,
        final MetadataTypeManager metadataTypeManager,
        final CustomSqlProvider customSqlProvider,
        final BasePerRepositoryTemplateFactory templateFactory,
        final String projectPackage,
        final String packageName,
        final String repository,
        final String engineName,
        final String header,
        final boolean jmx,
        final Collection tableNames,
        final Map parameters)
      throws  QueryJException
    {
        BasePerRepositoryTemplate result = null;

        if  (templateFactory instanceof DataAccessContextLocalTemplateFactory)
        {
            result =
                ((DataAccessContextLocalTemplateFactory) templateFactory)
                    .createTemplate(
                        metadataManager,
                        metadataTypeManager,
                        customSqlProvider,
                        packageName,
                        projectPackage,
                        repository,
                        engineName,
                        retrieveJNDILocation(parameters),
                        tableNames,
                        header,
                        jmx);
        }
        else
        {
            LogFactory.getLog(BasePerRepositoryTemplateBuildHandler.class).warn(
                  "Unexpected BasePerRepository factory. "
                + "Expecting DataAccessContextLocalTemplateFactory.");
        }

        return result;
    }

    /**
     * Retrieves the package name.
     * @param engineName the engine name.
     * @param projectPackage the project package.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @return the package name.
     * @throws BuildException if the package retrieval process if faulty.
     * @precondition projectPackage != null
     * @precondition packageUtils != null
     */
    protected String retrievePackage(
        final String engineName,
        final String projectPackage,
        final PackageUtils packageUtils)
      throws  BuildException
    {
        return "";
    }

    /**
     * Stores the template in given attribute map.
     * @param template the template.
     * @param parameters the parameter map.
     * @precondition template != null
     * @precondition parameters != null
     */
    protected void storeTemplate(
        final BasePerRepositoryTemplate template, final Map parameters)
    {
        parameters.put(
            TemplateMappingManager.DATAACCESSCONTEXTLOCAL_TEMPLATE,
            template);
    }

    /**
     * Retrieves the JNDI location from the attribute map.
     * @param parameters the parameter map.
     * @return the location.
     * @precondition parameters != null
     */
    protected String retrieveJNDILocation(final Map parameters)
    {
        return
            (String) parameters.get(ParameterValidationHandler.JNDI_DATASOURCES);
    }
}