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
 * Filename: DAOListenerImplTemplateBuildHandler.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Builds a DAO listener implementations if requested.
 *
 */
package org.acmsl.queryj.tools.templates.dao.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.customsql.CustomSqlProvider;
import org.acmsl.queryj.tools.customsql.Sql;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.templates.BasePerRepositoryTemplate;
import org.acmsl.queryj.tools.templates.BasePerRepositoryTemplateFactory;
import org.acmsl.queryj.tools.templates.dao.DAOListenerImplTemplateGenerator;
import org.acmsl.queryj.tools.templates.handlers.BasePerRepositoryTemplateBuildHandler;
import org.acmsl.queryj.tools.templates.TableTemplate;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;
import org.acmsl.queryj.tools.PackageUtils;

/*
 * Importing some Apache Ant classes.
 */
import org.apache.tools.ant.BuildException;

/*
 * Importing some JDK classes.
 */
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Builds a DAO listener implementations if requested.
 * @author <a href="mailto:chous@acm-sl.org"
           >Jose San Leandro</a>
 */
public class DAOListenerImplTemplateBuildHandler
    extends  BasePerRepositoryTemplateBuildHandler
{
    /**
     * Retrieves the per-repository template factory.
     * @return such instance.
     */
    protected BasePerRepositoryTemplateFactory retrieveTemplateFactory()
    {
        return DAOListenerImplTemplateGenerator.getInstance();
    }

    /**
     * Handles given information.
     * @param parameters the parameters.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the quote character.
     * @param metadataManager the database metadata manager.
     * @param customSqlProvider the custom sql provider.
     * @param templateFactory the template factory.
     * @param projectPackage the project package.
     * @param packageName the package name.
     * @param repository the repository.
     * @param header the header.
     * @param jmx whether to support JMX or not.
     * @param tableTemplates the table templates.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition engineName != null
     * @precondition metadataManager != null
     * @precondition customSqlProvider != null
     * @precondition templateFactory != null
     * @precondition projectPackage != null
     * @precondition packageName != null
     * @precondition repository != null
     * @precondition tableTemplates != null
     */
    protected boolean handle(
        final Map parameters,
        final String engineName,
        final String engineVersion,
        final String quote,
        final MetadataManager metadataManager,
        final CustomSqlProvider customSqlProvider,
        final BasePerRepositoryTemplateFactory templateFactory,
        final String projectPackage,
        final String packageName,
        final String repository,
        final String header,
        final boolean jmx,
        final TableTemplate[] tableTemplates)
      throws  BuildException
    {
        return
            super.handle(
                parameters,
                engineName,
                engineVersion,
                quote,
                metadataManager,
                customSqlProvider,
                templateFactory,
                projectPackage,
                packageName,
                repository,
                header,
                jmx,
                tableTemplates);
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
    {
        return
            packageUtils.retrieveDAOListenerImplPackage(projectPackage);
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
            TemplateMappingManager.DAO_LISTENER_IMPL_TEMPLATE,
            template);
    }
}
