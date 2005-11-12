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
 * Filename: $RCSfile$
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Builds a table template using database metadata.
 *
 */
package org.acmsl.queryj.tools.templates.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.AntCommand;
import org.acmsl.queryj.tools.handlers.AbstractAntCommandHandler;
import org.acmsl.queryj.tools.handlers.DatabaseMetaDataRetrievalHandler;
import org.acmsl.queryj.tools.handlers.ParameterValidationHandler;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.metadata.MetadataTypeManager;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.templates.handlers.TemplateBuildHandler;
import org.acmsl.queryj.tools.templates.TableTemplate;
import org.acmsl.queryj.tools.templates.TableTemplateFactory;
import org.acmsl.queryj.tools.templates.TableTemplateGenerator;

/*
 * Importing some Ant classes.
 */
import org.apache.tools.ant.BuildException;

/*
 * Importing some JDK classes.
 */
import java.util.Map;

/**
 * Builds a table template using database metadata.
 * @author <a href="mailto:chous@acm-sl.org"
           >Jose San Leandro</a>
 */
public class TableTemplateBuildHandler
    extends    AbstractAntCommandHandler
    implements TemplateBuildHandler
{
    /**
     * The table templates attribute name.
     */
    public static final String TABLE_TEMPLATES = "table.templates";

    /**
     * The table names attribute name.
     */
    public static final String TABLE_NAMES = "table.names";

    /**
     * Creates a TableTemplateBuildHandler.
     */
    public TableTemplateBuildHandler() {};

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
     * Handles given information.
     * @param parameters the parameters.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     */
    protected boolean handle(final Map parameters)
        throws  BuildException
    {
        handle(
            parameters,
            retrieveMetadataManager(parameters),
            retrieveTablePackage(parameters),
            TableTemplateGenerator.getInstance());

        return false;
    }

    /**
     * Handles given information.
     * @param parameters the parameters.
     * @param metadataManager the database metadata manager.
     * @param tablePackage the table package.
     * @param templateFactory the template factory.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition databaseMetaDataManager != null
     * @precondition tablePackage != null
     * @precondition templateFactory != null
     */
    protected void handle(
        final Map parameters,
        final MetadataManager metadataManager,
        final String packageName,
        final TableTemplateFactory templateFactory)
      throws  BuildException
    {
        String[] t_astrTableNames = metadataManager.getTableNames();

        String[] t_astrColumnNames = null;

        MetadataTypeManager t_MetadataTypeManager =
            metadataManager.getMetadataTypeManager();

        int t_iColumnType = -1;

        if  (t_astrTableNames != null) 
        {
            TableTemplate[] t_aTableTemplates =
                new TableTemplate[t_astrTableNames.length];

            for  (int t_iTableIndex = 0;
                      t_iTableIndex < t_astrTableNames.length;
                      t_iTableIndex++) 
            {
                t_aTableTemplates[t_iTableIndex] =
                    templateFactory.createTableTemplate(
                        packageName,
                        t_astrTableNames[t_iTableIndex]);

                t_astrColumnNames =
                    metadataManager.getColumnNames(
                        t_astrTableNames[t_iTableIndex]);

                if  (t_astrColumnNames != null) 
                {
                    for  (int t_iColumnIndex = 0;
                              t_iColumnIndex < t_astrColumnNames.length;
                              t_iColumnIndex++) 
                    {
                        t_aTableTemplates[t_iTableIndex].addField(
                            t_astrColumnNames[t_iColumnIndex]);

                        t_iColumnType =
                            metadataManager.getColumnType(
                                t_astrTableNames[t_iTableIndex],
                                t_astrColumnNames[t_iColumnIndex]);

                        t_aTableTemplates[t_iTableIndex].addFieldType(
                            t_astrColumnNames[t_iColumnIndex],
                            t_MetadataTypeManager.getQueryJFieldType(
                                t_iColumnType));
                    }
                }
            }

            storeTableNames(t_astrTableNames, parameters);
            storeTableTemplates(t_aTableTemplates, parameters);
        }
    }

    /**
     * Retrieves the table package name from the attribute map.
     * @param parameters the parameter map.
     * @return the package name.
     * @throws BuildException if the package retrieval process if faulty.
     * @precondition parameters != null
     */
    protected String retrieveTablePackage(final Map parameters)
        throws  BuildException
    {
        return retrieveTablePackage(parameters, PackageUtils.getInstance());
    }

    /**
     * Retrieves the table package name from the attribute map.
     * @param parameters the parameter map.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @return the package name.
     * @throws BuildException if the package retrieval process if faulty.
     * @precondition parameters != null
     * @precondition packageUtils != null
     */
    protected String retrieveTablePackage(
        final Map parameters, final PackageUtils packageUtils)
        throws  BuildException
    {
        return
            packageUtils.retrieveTablePackage(
                retrieveProjectPackage(parameters));
    }

    /**
     * Stores the table name collection in given attribute map.
     * @param tableNames the table names.
     * @param parameters the parameter map.
     * @throws BuildException if the templates cannot be stored for any reason.
     * @precondition tableNames != null
     * @precondition parameters != null
     */
    protected void storeTableNames(
        final String[] tableNames,
        final Map parameters)
      throws  BuildException
    {
        parameters.put(TABLE_NAMES, tableNames);
    }

    /**
     * Stores the table template collection in given attribute map.
     * @param tableTemplates the table templates.
     * @param parameters the parameter map.
     * @throws BuildException if the templates cannot be stored for any reason.
     * @precondition tableTemplates != null
     * @precondition parameters != null
     */
    protected void storeTableTemplates(
        final TableTemplate[] tableTemplates,
        final Map parameters)
        throws  BuildException
    {
        parameters.put(TABLE_TEMPLATES, tableTemplates);
    }
}
