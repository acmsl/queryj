/*
                        QueryJ

    Copyright (C) 2002  Jose San Leandro Armend�riz
                        jsanleandro@yahoo.es
                        chousz@yahoo.com

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    Thanks to ACM S.L. for distributing this library under the GPL license.
    Contact info: jsanleandro@yahoo.es
    Postal Address: c/Playa de Lagoa, 1
                    Urb. Valdecaba�as
                    Boadilla del monte
                    28660 Madrid
                    Spain

 ******************************************************************************
 *
 * Filename: $RCSfile$
 *
 * Author: Jose San Leandro Armend�riz
 *
 * Description: Builds a base DAO factory templates using database metadata.
 *
 * Last modified by: $Author$ at $Date$
 *
 * File version: $Revision$
 *
 * Project version: $Name$
 *
 * $Id$
 *
 */
package org.acmsl.queryj.tools.templates.dao.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.QueryJException;
import org.acmsl.queryj.tools.AntCommand;
import org.acmsl.queryj.tools.DatabaseMetaDataManager;
import org.acmsl.queryj.tools.handlers.AntCommandHandler;
import org.acmsl.queryj.tools.handlers.DatabaseMetaDataRetrievalHandler;
import org.acmsl.queryj.tools.handlers.ParameterValidationHandler;
import org.acmsl.queryj.tools.MetaDataUtils;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.templates.dao.BaseDAOFactoryTemplate;
import org.acmsl.queryj.tools.templates.dao.BaseDAOFactoryTemplateGenerator;
import org.acmsl.queryj.tools.templates.handlers.TableTemplateBuildHandler;
import org.acmsl.queryj.tools.templates.TableTemplate;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.patterns.Command;
import org.acmsl.commons.version.Version;
import org.acmsl.commons.version.VersionFactory;

/*
 * Importing some Ant classes.
 */
import org.apache.tools.ant.BuildException;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.util.Map;


/*
 * Importing Jakarta Commons Logging classes.
 */
import org.apache.commons.logging.LogFactory;

/**
 * Builds base DAO factory templates using database metadata.
 * @author <a href="mailto:jsanleandro@yahoo.es"
           >Jose San Leandro</a>
 * @version $Revision$
 */
public class BaseDAOFactoryTemplateBuildHandler
    implements  AntCommandHandler
{
    /**
     * A cached empty table template array.
     */
    public static final TableTemplate[] EMPTY_TABLE_TEMPLATE_ARRAY =
        new TableTemplate[0];

    /**
     * Creates a BaseDAOFactoryTemplateBuildHandler.
     */
    public BaseDAOFactoryTemplateBuildHandler() {};

    /**
     * Handles given command.
     * @param command the command to handle.
     * @return <code>true</code> if the chain should be stopped.
     */
    public boolean handle(Command command)
    {
        boolean result = false;

        if  (command instanceof AntCommand) 
        {
            try 
            {
                result = handle((AntCommand) command);
            }
            catch  (BuildException buildException)
            {
                LogFactory.getLog(getClass()).error(
                    "unhandled.exception",
                    buildException);
            }
        }
        
        return result;
    }

    /**
     * Handles given command.
     * @param command the command to handle.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     */
    public boolean handle(AntCommand command)
        throws  BuildException
    {
        boolean result = false;

        if  (command != null) 
        {
            try
            {
                Map attributes = command.getAttributeMap();

                DatabaseMetaDataManager t_MetaDataManager =
                    retrieveDatabaseMetaDataManager(attributes);

                String t_strPackage = retrievePackage(attributes);

                BaseDAOFactoryTemplateGenerator
                    t_BaseDAOFactoryTemplateGenerator =
                        BaseDAOFactoryTemplateGenerator.getInstance();

                if  (   (t_MetaDataManager                 != null)
                     && (t_BaseDAOFactoryTemplateGenerator != null))
                {
                    TableTemplate[] t_aTableTemplates =
                        retrieveTableTemplates(attributes);

                    if  (t_aTableTemplates != null)
                    {
                        BaseDAOFactoryTemplate[] t_aBaseDAOFactoryTemplates =
                            new BaseDAOFactoryTemplate[
                                    t_aTableTemplates.length];

                        for  (int t_iBaseDAOFactoryIndex = 0;
                                    t_iBaseDAOFactoryIndex
                                  < t_aBaseDAOFactoryTemplates.length;
                                  t_iBaseDAOFactoryIndex++) 
                        {
                            t_aBaseDAOFactoryTemplates[
                                t_iBaseDAOFactoryIndex] =
                                    t_BaseDAOFactoryTemplateGenerator
                                        .createBaseDAOFactoryTemplate(
                                            t_aTableTemplates[
                                                t_iBaseDAOFactoryIndex],
                                            t_MetaDataManager,
                                            t_strPackage,
                                            retrieveProjectPackage(
                                                attributes));
                        }

                        storeBaseDAOFactoryTemplates(
                            t_aBaseDAOFactoryTemplates, attributes);
                    }
                }
            }
            catch  (QueryJException queryjException)
            {
                throw new BuildException(queryjException);
            }
        }
        
        return result;
    }

    /**
     * Retrieves the database metadata manager from the attribute map.
     * @param parameters the parameter map.
     * @return the manager.
     * @throws BuildException if the manager retrieval process if faulty.
     */
    protected DatabaseMetaDataManager retrieveDatabaseMetaDataManager(
            Map parameters)
        throws  BuildException
    {
        DatabaseMetaDataManager result = null;

        if  (parameters != null)
        {
            result =
                (DatabaseMetaDataManager)
                    parameters.get(
                        DatabaseMetaDataRetrievalHandler
                            .DATABASE_METADATA_MANAGER);
        }
        
        return result;
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param parameters the parameter map.
     * @return the package name.
     * @throws BuildException if the package retrieval process if faulty.
     */
    protected String retrievePackage(Map parameters)
        throws  BuildException
    {
        String result = null;

        PackageUtils t_PackageUtils = PackageUtils.getInstance();

        if  (   (parameters     != null)
             && (t_PackageUtils != null))
        {
            result =
                t_PackageUtils.retrieveBaseDAOFactoryPackage(
                    retrieveProjectPackage(parameters));
        }

        return result;
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param parameters the parameter map.
     * @return the package name.
     * @throws BuildException if the package retrieval process if faulty.
     */
    protected String retrieveProjectPackage(Map parameters)
        throws  BuildException
    {
        String result = null;

        if  (parameters != null)
        {
            result =
                (String) parameters.get(ParameterValidationHandler.PACKAGE);
        }
        
        return result;
    }

    /**
     * Stores the base DAO factory template collection in given attribute map.
     * @param baseDAOFactoryTemplates the base DAO factory templates.
     * @param parameters the parameter map.
     * @throws BuildException if the templates cannot be stored for any reason.
     */
    protected void storeBaseDAOFactoryTemplates(
            BaseDAOFactoryTemplate[] baseDAOFactoryTemplates,
            Map                      parameters)
        throws  BuildException
    {
        if  (   (baseDAOFactoryTemplates != null)
             && (parameters              != null))
        {
            parameters.put(
                TemplateMappingManager.BASE_DAO_FACTORY_TEMPLATES,
                baseDAOFactoryTemplates);
        }
    }

    /**
     * Retrieves the table templates.
     * @param parameters the parameter map.
     * @return such templates.
     * @throws BuildException if the templates cannot be stored for any reason.
     */
    protected TableTemplate[] retrieveTableTemplates(
            Map parameters)
        throws  BuildException
    {
        TableTemplate[] result = EMPTY_TABLE_TEMPLATE_ARRAY;

        if  (parameters != null)
        {
            result =
                (TableTemplate[])
                    parameters.get(TableTemplateBuildHandler.TABLE_TEMPLATES);
        }

        return result;
    }

    /**
     * Concrete version object updated everytime it's checked-in in a
     * CVS repository.
     */
    public static final Version VERSION =
        VersionFactory.createVersion("$Revision$");

    /**
     * Retrieves the current version of this object.
     * @return the version object with such information.
     */
    public Version getVersion()
    {
        return VERSION;
    }

    /**
     * Retrieves the current version of this class. It's defined because
     * this is a utility class that cannot be instantiated.
     * @return the object with class version information.
     */
    public static Version getClassVersion()
    {
        return VERSION;
    }
}
