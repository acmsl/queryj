/*
                        QueryJ

    Copyright (C) 2002  Jose San Leandro Armendariz
                        jsanleandro@yahoo.es
                        chousz@yahoo.com

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
    Contact info: jsanleandro@yahoo.es
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
 * Description: Retrieves the externally-managed fields declaration and
 *              configures DatabaseMetaDataManager accordingly.
 *
 */
package org.acmsl.queryj.tools.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.AntCommand;
import org.acmsl.queryj.tools.AntExternallyManagedFieldsElement;
import org.acmsl.queryj.tools.AntFieldElement;
import org.acmsl.queryj.tools.AntTableElement;
import org.acmsl.queryj.tools.AntTablesElement;
import org.acmsl.queryj.tools.DatabaseMetaDataManager;
import org.acmsl.queryj.tools.handlers.AbstractAntCommandHandler;
import org.acmsl.queryj.tools.handlers.ParameterValidationHandler;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.patterns.Command;
import org.acmsl.commons.utils.StringValidator;

/*
 * Importing some Ant classes.
 */
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/*
 * Importing some JDK classes.
 */
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

/**
 * Retrieves the externally-managed fields declaration and
 * configures DatabaseMetaDataManager accordingly.
 * @author <a href="mailto:jsanleandro@yahoo.es"
 *         >Jose San Leandro</a>
 */
public class ExternallyManagedFieldsRetrievalHandler
    extends  AbstractAntCommandHandler
{
    /**
     * Creates an ExternallyManagedFieldsRetrievalHandler.
     */
    public ExternallyManagedFieldsRetrievalHandler() {};

    /**
     * Handles given command.
     * @param command the command to handle.
     * @return <code>true</code> if the chain should be stopped.
     */
    public boolean handle(final Command command)
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
                ((AntCommand) command).getProject().log(
                    ((AntCommand) command).getTask(),
                    buildException.getMessage(),
                    Project.MSG_ERR);
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
    public boolean handle(final AntCommand command)
        throws  BuildException
    {
        boolean result = false;

        if  (command != null) 
        {
            Map t_mAttributes = command.getAttributeMap();

            DatabaseMetaDataManager t_MetaDataManager =
                retrieveDatabaseMetaDataManager(t_mAttributes);

            Collection t_cFieldElements = null;

            AntExternallyManagedFieldsElement t_ExternallyManagedFieldsElement =
                    retrieveExternallyManagedFieldsElement(t_mAttributes);

            if  (   (t_MetaDataManager != null)
                 && (t_ExternallyManagedFieldsElement != null))
            {
                t_cFieldElements = t_ExternallyManagedFieldsElement.getFields();

                if  (   (t_cFieldElements != null)
                     && (t_cFieldElements.size() > 0))
                {
                    StringValidator t_StringValidator = StringValidator.getInstance();

                    if  (t_StringValidator == null)
                    {
                        throw new BuildException(
                            "Cannot continue: StringValidator not available");
                    }
                    else 
                    {
                        Iterator t_itFieldIterator =
                            t_cFieldElements.iterator();

                        while  (   (t_itFieldIterator != null)
                                && (t_itFieldIterator.hasNext()))
                        {
                            AntFieldElement t_Field =
                                (AntFieldElement) t_itFieldIterator.next();

                            if  (t_Field != null)
                            {
                                if  (t_StringValidator.isEmpty(
                                         t_Field.getName()))
                                {
                                    throw new BuildException(
                                        "Field name not specified.");
                                }
                                else if  (t_StringValidator.isEmpty(
                                              t_Field.getTableName()))
                                {
                                    throw new BuildException(
                                        "Field name not specified.");
                                }
                                else 
                                {
                                    t_MetaDataManager.addExternallyManagedField(
                                        t_Field.getTableName(),
                                        t_Field.getName(),
                                        t_Field.getKeyword());
                                }
                            }
                        }
                    }
                }
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
        final Map parameters)
      throws  BuildException
    {
        DatabaseMetaDataManager result = null;

        if  (parameters != null)
        {
            result =
                (DatabaseMetaDataManager)
                    parameters.get(
                        DatabaseMetaDataRetrievalHandler.DATABASE_METADATA_MANAGER);
        }
        
        return result;
    }

    /**
     * Retrieves the externally-managed-fields XML element stored in the
     * attribute map.
     * @param parameters the parameter map.
     * @return the externally-managed-fields information.
     * @throws BuildException if the retrieval process cannot be performed.
     */
    protected AntExternallyManagedFieldsElement
        retrieveExternallyManagedFieldsElement(final Map parameters)
        throws  BuildException
    {
        AntExternallyManagedFieldsElement result = null;

        if  (parameters != null) 
        {
            result =
                (AntExternallyManagedFieldsElement)
                    parameters.get(
                        ParameterValidationHandler.EXTERNALLY_MANAGED_FIELDS);
        }

        return result;
    }
}
