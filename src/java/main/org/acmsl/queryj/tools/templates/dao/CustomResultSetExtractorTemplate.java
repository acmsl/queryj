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
 * Filename: $RCSfile$
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Is able to create CustomResultSetExtractor implementation
 *              for each custom query requiring so.
 */
package org.acmsl.queryj.tools.templates.dao;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.tools.customsql.CustomResultUtils;
import org.acmsl.queryj.tools.customsql.CustomSqlProvider;
import org.acmsl.queryj.tools.customsql.ResultElement;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.templates.InvalidTemplateException;
import org.acmsl.queryj.tools.templates.BasePerCustomResultTemplate;

/*
 * Importing StringTemplate classes.
 */
import org.antlr.stringtemplate.StringTemplateGroup;

/*
 * Importing some JDK classes.
 */
import java.util.Map;

/**
 * Is able to create CustomResultSetExtractor implementations for each
 * custom query requiring so.
 * @author <a href="mailto:chous@acm-sl.org"
 *         >Jose San Leandro</a>
 */
public class CustomResultSetExtractorTemplate
    extends   BasePerCustomResultTemplate
{
    /**
     * Builds a <code>CustomResultSetExtractorTemplate</code> using
     * information.
     * @param result the custom result.
     * @param customSqlProvider the <code>CustomSqlProvider</code> instance.
     * @param packageName the package name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param basePackageName the base package name.
     * @param repositoryName the repository name.
     * @precondition result != null
     * @precondition customSqlProvider != null
     */
    public CustomResultSetExtractorTemplate(
        final ResultElement result,
        final CustomSqlProvider customSqlProvider,
        final MetadataManager metadataManager,
        final String packageName,
        final String engineName,
        final String engineVersion,
        final String basePackageName,
        final String repositoryName)
    {
        super(
            result,
            customSqlProvider,
            metadataManager,
            packageName,
            engineName,
            engineVersion,
            basePackageName,
            repositoryName);
    }

    /**
     * Fills the common parameters.
     * @param input the input.
     * @param result the result.
     * @param customSqlProvider the <code>CustomSqlProvider</code> instance.
     * @param metadataMananager the database metadata manager.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @precondition input != null
     * @precondition result != null
     * @precondition customSqlProvider != null
     * @precondition metadataManager != null
     * @precondition engineName != null
     * @precondition engineVersion != null
     */
    protected void fillCommonParameters(
        final Map input,
        final ResultElement result,
        final CustomSqlProvider customSqlProvider,
        final MetadataManager metadataManager,
        final String engineName,
        final String engineVersion)
    {
        super.fillCommonParameters(
            input,
            result,
            customSqlProvider,
            metadataManager,
            engineName,
            engineVersion);
        
        input.put(
            "table",
            retrieveTable(
                result,
                customSqlProvider,
                metadataManager,
                CustomResultUtils.getInstance()));
    }

    /**
     * Retrieves the table associated to the result.
     * @param resultElement the result element.
     * @param customSqlProvider the <code>CustomSqlProvider</code> instance.
     * @param metadataManager the database metadata manager.
     * @param customResultUtils the <code>CustomResultUtils</code> instance.
     * @return the table name.
     * @precondition resultElement != null
     * @precondition customSqlProvider != null
     * @precondition metadataManager != null
     * @precondition customResultUtils != null
     */
    protected String retrieveTable(
        final ResultElement resultElement,
        final CustomSqlProvider customSqlProvider,
        final MetadataManager metadataManager,
        final CustomResultUtils customResultUtils)
    {
        return
            customResultUtils.retrieveTable(
                resultElement, customSqlProvider, metadataManager);
    }

    /**
     * Retrieves the string template group.
     * @return such instance.
     */
    protected StringTemplateGroup retrieveGroup()
    {
        return retrieveGroup("/org/acmsl/queryj/dao/ResultSetExtractor.stg");
    }

    /**
     * Retrieves the template name.
     * @return such information.
     */
    public String getTemplateName()
    {
        return "ResultSetExtractor";
    }
}
