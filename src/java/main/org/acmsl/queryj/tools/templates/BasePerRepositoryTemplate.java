//;-*- mode: java -*-
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

 *****************************************************************************
 *
 * Filename: $RCSfile: $
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Base logic for all per-repository templates.
 *
 */
package org.acmsl.queryj.tools.templates;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.metadata.DecoratorFactory;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.metadata.TableDecorator;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.utils.EnglishGrammarUtils;
import org.acmsl.commons.utils.StringUtils;

/*
 * Importing StringTemplate classes.
 */
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

/*
 * Importing some JDK classes.
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * Importing Apache Commons Logging classes.
 */
import org.apache.commons.logging.LogFactory;

/**
 * Base logic for all per-repository templates.
 * @author <a href="mailto:chous@acm-sl.org"
 *         >Jose San Leandro</a>
 */
public abstract class BasePerRepositoryTemplate
    extends  AbstractBasePerRepositoryTemplate
{
    /**
     * Builds a <code>BasePerRepositoryTemplate</code> using given
     * information.
     * @param metadataManager the database metadata manager.
     * @param header the header.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param packageName the package name.
     * @param basePackageName the base package name.
     * @param repositoryName the repository name.
     * @param engineName the engine name.
     * @param tables the tables.
     */
    public BasePerRepositoryTemplate(
        final MetadataManager metadataManager,
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
            header,
            decoratorFactory,
            packageName,
            basePackageName,
            repositoryName,
            engineName,
            tables);
    }

    /**
     * Retrieves the source code generated by this template.
     * @param header the header.
     * @return such code.
     */
    protected String generateOutput(final String header)
    {
        return
            generateOutput(
                header,
                getMetadataManager(),
                getDecoratorFactory(),
                getSubpackageName(),
                getBasePackageName(),
                getRepositoryName(),
                getEngineName(),
                getTables(),
                StringUtils.getInstance(),
                PackageUtils.getInstance());
    }

    /**
     * Retrieves the string template group.
     * @return such instance.
     */
    protected abstract StringTemplateGroup retrieveGroup();

    /**
     * Retrieves the source code generated by this template.
     * @param header the header.
     * @param metadataManager the database metadata manager.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param subpackageName the subpackage name.
     * @param basePackageName the base package name.
     * @param repositoryName the repository name.
     * @param engineName the engine name.
     * @param tables the tables.
     * @param stringUtils the StringUtils instance.
     * @param packageUtils the PackageUtils instance.
     * @return such code.
     * @precondition metadataManager != null
     * @precondition decoratorFactory != null
     * @precondition subpackageName != null
     * @precondition basePackageName != null
     * @precondition repositoryName != null
     * @precondition tables != null
     * @precondition stringUtils != null
     * @precondition packageUtils != null
     */
    protected String generateOutput(
        final String header,
        final MetadataManager metadataManager,
        final DecoratorFactory decoratorFactory,
        final String subpackageName,
        final String basePackageName,
        final String repositoryName,
        final String engineName,
        final Collection tables,
        final StringUtils stringUtils,
        final PackageUtils packageUtils)
    {
        String result = "";

        StringTemplateGroup t_Group = retrieveGroup();

        StringTemplate t_Template = retrieveTemplate(t_Group);

        fillParameters(
            new HashMap(),
            t_Template,
            header,
            metadataManager,
            decoratorFactory,
            subpackageName,
            basePackageName,
            repositoryName,
            engineName,
            tables,
            createTimestamp(),
            new Integer[]
            {
                STARTING_YEAR,
                new Integer(retrieveCurrentYear())
            },
            stringUtils);

        result = t_Template.toString();

        return result;
    }

    /**
     * Fills the template parameters.
     * @param input the parameter container.
     * @param template the template.
     * @param header the header.
     * @param metadataManager the database metadata manager.
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
     * @precondition metadataManager != null
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
        template.setAttribute("input", input);

        fillHeaderParameters(input, header, copyrightYears, timestamp);

        fillCoreParameters(
            input,
            metadataManager,
            decoratorFactory,
            basePackageName,
            subpackageName,
            tableRepositoryName,
            engineName,
            tables,
            timestamp,
            stringUtils);
    }

    /**
     * Fills the parameters for <code>java_header</code> rule.
     * @param input the input.
     * @param header the header.
     * @param copyrightYears the copyright years.
     * @param timestamp the timestamp.
     * @precondition input != null
     * @precondition copyrightYears != null
     * @precondition timestamp != null
     */
    protected void fillHeaderParameters(
        final Map input,
        final String header,
        final Integer[] copyrightYears,
        final String timestamp)
    {
        input.put("copyright_years", copyrightYears);
        input.put("timestamp", timestamp);

        if  (   (header != null)
             && (!input.containsKey("header")))
        {
            input.put("header", processHeader(input, header));
        }
    }


    /**
     * Fills the core parameters.
     * @param input the input.
     * @param metadataManager the database metadata manager.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param subpackageName the subpackage name.
     * @param basePackageName the base package name.
     * @param tableRepositoryName the table repository name.
     * @param engineName the engine name.
     * @param tables the tables.
     * @param timestamp the timestamp.
     * @param stringUtils the <code>StringUtils</code> instance.
     * @precondition input != null
     * @precondition metadataManager != null
     * @precondition decoratorFactory != null
     * @precondition subpackageName != null
     * @precondition basePackageName != null
     * @precondition tableRepositoryName != null
     * @precondition tables != null
     * @precondition timestamp != null
     * @precondition stringUtils != null
     */
    protected void fillCoreParameters(
        final Map input,
        final MetadataManager metadataManager,
        final DecoratorFactory decoratorFactory,
        final String subpackageName,
        final String basePackageName,
        final String tableRepositoryName,
        final String engineName,
        final Collection tables,
        final String timestamp,
        final StringUtils stringUtils)
    {
        input.put("tr_name", tableRepositoryName);

        input.put("tr_name_capitalized", capitalize(tableRepositoryName));

        input.put("base_package_name", basePackageName);

        input.put("engine_name", engineName);

        input.put(
            "dao_subpackage_name",
            retrieveDAOSubpackageName(
                basePackageName, engineName, PackageUtils.getInstance()));

        input.put(
            "tables",
            decorateTables(tables, metadataManager, decoratorFactory));
    }

    /**
     * Retrieves the DAO subpackage name.
     * @param basePackageName the base package name.
     * @param engineName the engine name.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @return such information.
     */
    protected String retrieveDAOSubpackageName(
        final String basePackageName,
        final String engineName,
        final PackageUtils packageUtils)
    {
        return packageUtils.retrieveDAOPackage(basePackageName, engineName);
    }

    /**
     * Decorates the tables.
     * @param tables the tables.
     * @param metadataManager the <code>MetadataManager</code> instance.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @return the decorated tables.
     * @precondition tables != null
     * @precondition metadataManager != null
     * @precondition decoratorFactory != null
     */
    protected Collection decorateTables(
        final Collection tables,
        final MetadataManager metadataManager,
        final DecoratorFactory decoratorFactory)
    {
        Collection result = new ArrayList();

        Iterator t_itTableIterator = tables.iterator();

        if  (t_itTableIterator != null)
        {
            while  (t_itTableIterator.hasNext())
            {
                result.add(
                    decorate(
                        (String) t_itTableIterator.next(),
                        metadataManager,
                        decoratorFactory));
            }
        }

        return result;
    }

    /**
     * Decorates given table.
     * @param table the table name.
     * @param metadataManager the <code>MetadataManager</code> instance.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @return the decorated table.
     * @precondition table != null
     * @precondition metadataManager != null
     * @precondition decoratorFactory != null
     */
    protected TableDecorator decorate(
        final String table,
        final MetadataManager metadataManager,
        final DecoratorFactory decoratorFactory)
    {
        return decoratorFactory.createTableDecorator(table, metadataManager);
    }
}
