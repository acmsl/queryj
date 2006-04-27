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

 ******************************************************************************
 *
 * Filename: $RCSfile: $
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Builds a value object template using database metadata.
 *
 */
package org.acmsl.queryj.tools.templates.valueobject.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.QueryJException;
import org.acmsl.queryj.tools.AntCommand;
import org.acmsl.queryj.tools.handlers.AbstractAntCommandHandler;
import org.acmsl.queryj.tools.handlers.DatabaseMetaDataRetrievalHandler;
import org.acmsl.queryj.tools.handlers.ParameterValidationHandler;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.templates.handlers.TableTemplateBuildHandler;
import org.acmsl.queryj.tools.templates.handlers.TemplateBuildHandler;
import org.acmsl.queryj.tools.templates.TableTemplate;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;
import org.acmsl.queryj.tools.templates.valueobject.ValueObjectTemplate;
import org.acmsl.queryj.tools.templates.valueobject.ValueObjectTemplateFactory;
import org.acmsl.queryj.tools.templates.valueobject
    .ValueObjectTemplateGenerator;

/*
 * Importing some Ant classes.
 */
import org.apache.tools.ant.BuildException;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * Builds a value object template using database metadata.
 * @author <a href="mailto:chous@acm-sl.org"
 *         >Jose San Leandro</a>
 */
public class ValueObjectTemplateBuildHandler
    extends    AbstractAntCommandHandler
    implements TemplateBuildHandler
{
    /**
     * A cached empty table template array.
     */
    public static final TableTemplate[] EMPTY_TABLE_TEMPLATE_ARRAY =
        new TableTemplate[0];

    /**
     * Creates a <code>ValueObjectTemplateBuildHandler</code>.
     */
    public ValueObjectTemplateBuildHandler() {};

    /**
     * Handles given command.
     * @param command the command to handle.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition command != null
     */
    public boolean handle(final AntCommand command)
        throws  BuildException
    {
        return handle(command.getAttributeMap());
    }

    /**
     * Handles given command.
     * @param attributes the attributes.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition attributes != null
     */
    public boolean handle(final Map attributes)
        throws  BuildException
    {
        return
            handle(
                attributes,
                retrieveDatabaseMetaData(attributes),
                retrieveMetadataManager(attributes),
                retrievePackage(attributes),
                retrieveHeader(attributes),
                ValueObjectTemplateGenerator.getInstance(),
                retrieveTableTemplates(attributes));
    }

    /**
     * Handles given command.
     * @param attributes the attributes.
     * @param databaseMetaData the <code>DatabaseMetaData</code> instance.
     * @param metadataManager the metadata manager.
     * @param packageName the package name.
     * @param header the header.
     * @param templateFactory the template factory.
     * @param tableTemplates the table templates.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition attributes != null
     * @precondition databaseMetaData != null
     * @precondition metadataManager != null
     * @precondition packageName != null
     * @precondition templateFactory != null
     * @precondition tableTemplates != null
     */
    public boolean handle(
        final Map attributes,
        final DatabaseMetaData databaseMetaData,
        final MetadataManager metadataManager,
        final String packageName,
        final String header,
        final ValueObjectTemplateFactory templateFactory,
        final TableTemplate[] tableTemplates)
      throws  BuildException
    {
        boolean result = false;

        int t_iCount = (tableTemplates != null) ? tableTemplates.length : 0;
            
        try
        {
            ValueObjectTemplate[] t_aValueObjectTemplates =
                new ValueObjectTemplate[t_iCount];

            for  (int t_iValueObjectIndex = 0;
                      t_iValueObjectIndex < t_iCount;
                      t_iValueObjectIndex++) 
            {
                String t_strQuote =
                    databaseMetaData.getIdentifierQuoteString();

                if  (t_strQuote == null)
                {
                    t_strQuote = "\"";
                }

                if  (t_strQuote.equals("\""))
                {
                    t_strQuote = "\\\"";
                }

                t_aValueObjectTemplates[t_iValueObjectIndex] =
                    templateFactory.createValueObjectTemplate(
                        packageName,
                        tableTemplates[t_iValueObjectIndex],
                        metadataManager,
                        header);
            }

            storeValueObjectTemplates(t_aValueObjectTemplates, attributes);
        }
        catch  (final SQLException sqlException)
        {
            throw new BuildException(sqlException);
        }
        catch  (final QueryJException queryjException)
        {
            throw new BuildException(queryjException);
        }
        
        return result;
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param parameters the parameter map.
     * @return the package name.
     * @throws BuildException if the package retrieval process if faulty.
     * @precondition parameters != null
     */
    public static String retrievePackage(final Map parameters)
        throws  BuildException
    {
        return
            retrievePackage(parameters, PackageUtils.getInstance());
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param parameters the parameter map.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @return the package name.
     * @throws BuildException if the package retrieval process if faulty.
     * @precondition parameters != null
     * @precondition packageUtils != null
     */
    protected static String retrievePackage(
        final Map parameters, final PackageUtils packageUtils)
      throws  BuildException
    {
        return
            packageUtils.retrieveValueObjectPackage(
                (String)
                    parameters.get(ParameterValidationHandler.PACKAGE));
    }

    /**
     * Stores the value object template collection in given attribute map.
     * @param valueObjectTemplates the value object templates.
     * @param parameters the parameter map.
     * @throws BuildException if the templates cannot be stored for any reason.
     * @precondition valueObjectTemplates != null
     * @precondition parameters != null
     */
    protected void storeValueObjectTemplates(
        final ValueObjectTemplate[] valueObjectTemplates,
        final Map parameters)
      throws  BuildException
    {
        parameters.put(
            TemplateMappingManager.VALUE_OBJECT_TEMPLATES,
            valueObjectTemplates);
    }

    /**
     * Retrieves the table templates.
     * @param parameters the parameter map.
     * @return such templates.
     * @throws BuildException if the templates cannot be stored for any reason.
     * @precondition parameters != null
     */
    protected TableTemplate[] retrieveTableTemplates(
        final Map parameters)
      throws  BuildException
    {
        return
            (TableTemplate[])
                parameters.get(TableTemplateBuildHandler.TABLE_TEMPLATES);
    }
}
