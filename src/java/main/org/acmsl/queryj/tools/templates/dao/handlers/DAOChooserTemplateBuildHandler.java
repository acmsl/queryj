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
 * Description: Builds a DAOChooser using database metadata.
 *
 */
package org.acmsl.queryj.tools.templates.dao.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.AntCommand;
import org.acmsl.queryj.tools.handlers.AbstractAntCommandHandler;
import org.acmsl.queryj.tools.handlers.ParameterValidationHandler;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.templates.dao.DAOChooserTemplate;
import org.acmsl.queryj.tools.templates.dao.DAOChooserTemplateFactory;
import org.acmsl.queryj.tools.templates.dao.DAOChooserTemplateGenerator;
import org.acmsl.queryj.tools.templates.handlers.TableTemplateBuildHandler;
import org.acmsl.queryj.tools.templates.handlers.TemplateBuildHandler;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.patterns.Command;

/*
 * Importing some Ant classes.
 */
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/*
 * Importing some JDK classes.
 */
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Builds a DAOChooser using database metadata.
 * @author <a href="mailto:jsanleandro@yahoo.es"
           >Jose San Leandro</a>
 */
public class DAOChooserTemplateBuildHandler
    extends    AbstractAntCommandHandler
    implements TemplateBuildHandler
{
    /**
     * Creates a DAOChooserTemplateBuildHandler.
     */
    public DAOChooserTemplateBuildHandler() {};

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
        return
            handle(
                command.getAttributeMap(),
                command.getProject(),
                command.getTask());
    }

    /**
     * Handles given information.
     * @param parameters the parameters.
     * @param project the project, for logging purposes.
     * @param task the task, for logging purposes.
     * @return <code>true</code> if the chain should be stopped.
     * @precondition parameters != null
     */
    protected boolean handle(
        final Map parameters, final Project project, final Task task)
    {
        storeDAOChooserTemplate(
            buildDAOChooserTemplate(parameters, project, task),
            parameters);
        
        return false;
    }

    /**
     * Builds a DAOChooser template using the information
     * stored in the attribute map.
     * @param parameters the parameter map.
     * @param project the project, for logging purposes.
     * @param task the task, for logging purposes.
     * @return the <code>DAOChooserTemplate</code> instance.
     * @precondition parameters != null
     */
    protected DAOChooserTemplate buildDAOChooserTemplate(
        final Map parameters, final Project project, final Task task)
    {
        return
            buildDAOChooserTemplate(
                parameters,
                retrievePackage(parameters),
                retrieveRepository(parameters),
                retrieveTableNames(parameters),
                project,
                task);
    }

    /**
     * Builds a DAOChooser template using the information
     * stored in the attribute map.
     * @param parameters the parameter map.
     * @param packageName the package name.
     * @param repository the repository.
     * @param tableNames the table names.
     * @param project the project, for logging purposes.
     * @param task the task, for logging purposes.
     * @return the <code>DAOChooserTemplate</code> instance.
     * @precondition parameters != null
     * @precondition packageName != null
     * @precondition repository != null
     * @precondition tableNames != null
     */
    protected DAOChooserTemplate buildDAOChooserTemplate(
        final Map parameters,
        final String packageName,
        final String repository,
        final String[] tableNames,
        final Project project,
        final Task task)
    {
        DAOChooserTemplate result =
            buildDAOChooserTemplate(
                packageName,
                repository,
                project,
                task);

        for  (int t_iTableIndex = 0;
                  t_iTableIndex < tableNames.length;
                  t_iTableIndex++)
        {
            result.addTable(tableNames[t_iTableIndex]);
        }

        return result;
    }

    /**
     * Builds a DAOChooser template using given information.
     * @param packageName the package name.
     * @param repository the repository.
     * @param project the project, for logging purposes.
     * @param task the task, for logging purposes.
     * @return the template.
     * @precondition packageName != null
     * @precondition repository != null
     */
    protected DAOChooserTemplate buildDAOChooserTemplate(
        final String packageName,
        final String repository,
        final Project project,
        final Task task)
    {
        return
            buildDAOChooserTemplate(
                packageName,
                repository,
                DAOChooserTemplateGenerator.getInstance(),
                project,
                task);
    }

    /**
     * Builds a DAOChooser template using given information.
     * @param packageName the package name.
     * @param repository the repository.
     * @param project the project, for logging purposes.
     * @param task the task, for logging purposes.
     * @return the template.
     * @precondition packageName != null
     * @precondition repository != null
     * @precondition templateFactory != null
     */
    protected DAOChooserTemplate buildDAOChooserTemplate(
        final String packageName,
        final String repository,
        final DAOChooserTemplateFactory templateFactory,
        final Project project,
        final Task task)
    {
        return
            templateFactory.createDAOChooserTemplate(
                packageName, repository, project, task);
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param parameters the parameter map.
     * @return the package name.
     * @precondition parameters != null
     */
    protected String retrievePackage(final Map parameters)
    {
        return retrievePackage(parameters, PackageUtils.getInstance());
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param parameters the parameter map.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @return the package name.
     * @precondition parameters != null
     * @precondition packageUtils != null
     */
    protected String retrievePackage(
        final Map parameters, final PackageUtils packageUtils)
    {
        return
            packageUtils.retrieveDAOChooserPackage(
                (String) parameters.get(ParameterValidationHandler.PACKAGE));
    }

    /**
     * Retrieves the repository from the attribute map.
     * @param parameters the parameter map.
     * @return the repository.
     * @precondition parameters != null
     */
    protected String retrieveRepository(final Map parameters)
    {
        return
            (String) parameters.get(ParameterValidationHandler.REPOSITORY);

    }

    /**
     * Retrieves the tables' name from the attribute map.
     * @param parameters the parameter map.
     * @return the names.
     * @precondition parameters != null
     */
    protected String[] retrieveTableNames(final Map parameters)
    {
        return
            (String[]) parameters.get(TableTemplateBuildHandler.TABLE_NAMES);

    }

    /**
     * Stores the DAOChooser template in given attribute map.
     * @param daoChooserTemplate the DAOChooser template.
     * @param parameters the parameter map.
     * @throws BuildException if the template cannot be stored for any reason.
     * @precondition daoChooserTemplate != null
     * @precondition parameters != null
     */
    protected void storeDAOChooserTemplate(
        final DAOChooserTemplate daoChooserTemplate,
        final Map parameters)
    {
        parameters.put(
            TemplateMappingManager.DAO_CHOOSER_TEMPLATE,
            daoChooserTemplate);
    }
}
