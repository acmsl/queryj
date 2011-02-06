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
 * Filename: XMLDAOTestTemplateBuildHandler.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Builds a XML DAO test template using database metadata.
 *
 */
package org.acmsl.queryj.tools.templates.dao.xml.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.QueryJBuildException;
import org.acmsl.queryj.tools.handlers.AbstractQueryJCommandHandler;
import org.acmsl.queryj.tools.handlers.DatabaseMetaDataRetrievalHandler;
import org.acmsl.queryj.tools.handlers.ParameterValidationHandler;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.templates.dao.xml.XMLDAOTestTemplate;
import org.acmsl.queryj.tools.templates.dao.xml.XMLDAOTestTemplateFactory;
import org.acmsl.queryj.tools.templates.dao.xml.XMLDAOTestTemplateGenerator;
import org.acmsl.queryj.tools.templates.handlers.TableTemplateBuildHandler;
import org.acmsl.queryj.tools.templates.handlers.TemplateBuildHandler;
import org.acmsl.queryj.tools.templates.TableTemplate;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;
import org.acmsl.queryj.tools.templates.TestTemplate;

/*
 * Importing some JDK classes.
 */
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Handles the building of XML DAO test templates using database
 * metadata.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class XMLDAOTestTemplateBuildHandler
    extends    AbstractQueryJCommandHandler
    implements TemplateBuildHandler
{
    /**
     * A cached empty table template array.
     */
    public static final TableTemplate[] EMPTY_TABLE_TEMPLATE_ARRAY =
        new TableTemplate[0];

    /**
     * Creates a <code>XMLDAOTestTemplateBuildHandler</code> instance.
     */
    public XMLDAOTestTemplateBuildHandler() {};

    /**
     * Handles given parameters.
     * @param parameters the parameters to handle.
     * @return <code>true</code> if the chain should be stopped.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     */
    protected boolean handle(final Map parameters)
        throws  QueryJBuildException
    {
        return
            handle(
                parameters,
                retrieveProjectPackage(parameters),
                retrieveUseSubfoldersFlag(parameters));
    }

    /**
     * Handles given parameters.
     * @param parameters the parameters to handle.
     * @param projectPackage the project package.
     * @param useSubfolders whether to use subfolders or not.
     * @return <code>true</code> if the chain should be stopped.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition projectPackage != null
     */
    protected boolean handle(
        final Map parameters,
        final String projectPackage,
        final boolean useSubfolders)
      throws  QueryJBuildException
    {
        buildTemplates(
            parameters,
            retrieveDatabaseMetaData(parameters),
            retrieveMetadataManager(parameters),
            retrieveXMLDAOTestPackage(projectPackage, useSubfolders),
            retrieveValueObjectPackage(projectPackage),
            retrieveXMLDAOPackage(projectPackage),
            retrieveTableTemplates(parameters),
            retrieveHeader(parameters),
            XMLDAOTestTemplateGenerator.getInstance());

        return false;
    }

    /**
     * Builds the <code>XMLDAOTest</code> templates..
     * @param parameters the parameters to handle.
     * @param metaData the database metadata.
     * @param metadataManager the database metadata manager.
     * @param packageName the package name.
     * @param voPackageName the value-object package name.
     * @param xmlDAOPackageName the package name of the XML DAOs.
     * @param tableTemplates the table templates.
     * @param header the header.
     * @param templateFactory the template factory.
     * @return <code>true</code> if the chain should be stopped.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition metaData != null
     * @precondition metadataManager != null
     * @precondition packageName != null
     * @precondition voPackageName != null
     * @precondition xmlDAOPackageName != null
     * @precondition tableTemplates != null
     * @precondition templateFactory != null
     */
    protected void buildTemplates(
        final Map parameters,
        final DatabaseMetaData metaData,
        final MetadataManager metadataManager,
        final String packageName,
        final String voPackageName,
        final String xmlDAOPackageName,
        final TableTemplate[] tableTemplates,
        final String header,
        final XMLDAOTestTemplateFactory templateFactory)
      throws  QueryJBuildException
    {
        int t_iLength =
            (tableTemplates != null) ? tableTemplates.length : 0;

        XMLDAOTestTemplate[] t_aXMLDAOTestTemplates =
            new XMLDAOTestTemplate[t_iLength];

        for  (int t_iXMLDAOTestIndex = 0;
                  t_iXMLDAOTestIndex < t_iLength;
                  t_iXMLDAOTestIndex++) 
        {
            t_aXMLDAOTestTemplates[t_iXMLDAOTestIndex] =
                templateFactory.createXMLDAOTestTemplate(
                    tableTemplates[t_iXMLDAOTestIndex],
                    metadataManager,
                    packageName,
                    xmlDAOPackageName,
                    voPackageName,
                    header);

            storeTestTemplate(
                t_aXMLDAOTestTemplates[t_iXMLDAOTestIndex],
                parameters);
        }

        storeXMLDAOTestTemplates(t_aXMLDAOTestTemplates, parameters);
    }

    /**
     * Retrieves the XML DAO Test's package name from the attribute map.
     * @param parameters the parameter map.
     * @return the package name.
     * @precondition parameters != null
     */
    protected String retrieveXMLDAOTestPackage(
        final String projectPackage, final boolean useSubfolders)
    {
        return
            retrieveXMLDAOTestPackage(
                projectPackage, useSubfolders, PackageUtils.getInstance());
    }

    /**
     * Retrieves the XML DAO Test's package name from given information.
     * @param projectPackage the project package.
     * @param useSubfolders whether to use subfolders.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @return the package name.
     * @precondition projectPackage != null
     * @precondition packageUtils != null
     */
    protected String retrieveXMLDAOTestPackage(
        final String projectPackage,
        final boolean useSubfolders,
        final PackageUtils packageUtils)
    {
        return
            packageUtils.retrieveXMLDAOTestPackage(
                projectPackage, useSubfolders);
    }

    /**
     * Retrieves the XML DAO's package name from the project package.
     * @param projectPackage the project package.
     * @return the package name.
     * @precondition projectPackage != null
     */
    protected String retrieveXMLDAOPackage(final String projectPackage)
    {
        return
            retrieveXMLDAOPackage(projectPackage, PackageUtils.getInstance());
    }

    /**
     * Retrieves the XML DAO's package name from the project package.
     * @param projectPackage the project package.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @return the package name.
     * @precondition projectPackage != null
     * @precondition packageUtils != null
     */
    protected String retrieveXMLDAOPackage(
        final String projectPackage, final PackageUtils packageUtils)
    {
        return packageUtils.retrieveXMLDAOPackage(projectPackage);
    }

    /**
     * Retrieves the value object's package name from the project package.
     * @param projectPackage the project package.
     * @return the package name.
     * @precondition projectPackage != null
     */
    protected String retrieveValueObjectPackage(final String projectPackage)
    {
        return
            retrieveValueObjectPackage(
                projectPackage, PackageUtils.getInstance());
    }

    /**
     * Retrieves the value object's package name from the project package.
     * @param projectPackage the project package.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @return the package name.
     * @precondition projectPackage != null
     * @precondition packageUtils != null
     */
    protected String retrieveValueObjectPackage(
        final String projectPackage, final PackageUtils packageUtils)
    {
        return
            packageUtils.retrieveValueObjectPackage(projectPackage);
    }

    /**
     * Retrieves the test template collection.
     * @param parameters the parameter map.
     * @return the test templates.
     * @precondition parameters != null
     */
    protected Collection retrieveTestTemplates(final Map parameters)
    {
        return
            (Collection) parameters.get(TemplateMappingManager.TEST_TEMPLATES);
    }

    /**
     * Stores the XML DAO template collection in given attribute map.
     * @param xmlDAOTestTemplates the XML DAO templates.
     * @param parameters the parameter map.
     * @precondition xmlDAOTestTemplates != null
     * @precondition parameters != null
     */
    protected void storeXMLDAOTestTemplates(
        final XMLDAOTestTemplate[] xmlDAOTestTemplates,
        final Map parameters)
    {
        parameters.put(
            TemplateMappingManager.XML_DAO_TEST_TEMPLATES,
            xmlDAOTestTemplates);
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
     * Stores the test template collection.
     * @param templates the test templates.
     * @param parameters the parameter map.
     * @return the test templates.
     * @precondition templates != null
     * @precondition parameters != null
     */
    protected void storeTestTemplates(
        final Collection templates, final Map parameters)
    {
        parameters.put(TemplateMappingManager.TEST_TEMPLATES, templates);
    }

    /**
     * Stores a new test template.
     * @param testTemplate the test template.
     * @param parameters the parameter map.
     * @precondition template != null
     * @precondition parameters != null
     */
    protected void storeTestTemplate(
        final TestTemplate template, final Map parameters)
    {
        Collection t_cTestTemplates = retrieveTestTemplates(parameters);

        if  (t_cTestTemplates == null) 
        {
            t_cTestTemplates = new ArrayList();
            storeTestTemplates(t_cTestTemplates, parameters);
        }

        t_cTestTemplates.add(template);
    }
}
