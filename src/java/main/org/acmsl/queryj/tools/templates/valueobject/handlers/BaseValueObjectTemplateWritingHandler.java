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
 * Description: Writes base value object templates.
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
package org.acmsl.queryj.tools.templates.valueobject.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.AntCommand;
import org.acmsl.queryj.tools.handlers.AbstractAntCommandHandler;
import org.acmsl.queryj.tools.handlers.ParameterValidationHandler;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.templates.handlers.TemplateWritingHandler;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;
import org.acmsl.queryj.tools.templates.valueobject.handlers.BaseValueObjectTemplateBuildHandler;
import org.acmsl.queryj.tools.templates.valueobject.BaseValueObjectTemplate;
import org.acmsl.queryj.tools.templates.valueobject.BaseValueObjectTemplateGenerator;

/*
 * Importing some Ant classes.
 */
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Writes Base Value Object templates.
 * @author <a href="mailto:jsanleandro@yahoo.es"
           >Jose San Leandro</a>
 * @version $Revision$
 */
public class BaseValueObjectTemplateWritingHandler
    extends    AbstractAntCommandHandler
    implements TemplateWritingHandler
{
    /**
     * Creates a <code>BaseValueObjectTemplateWritingHandler</code>.
     */
    public BaseValueObjectTemplateWritingHandler() {};

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
     * Handles given command.
     * @param attributes the attributes.
     * @param project the project, for logging purposes.
     * @param task the task, for logging purposes.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition attributes != null
     */
    protected boolean handle(
        final Map attributes, final Project project, final Task task)
        throws  BuildException
    {
        return
            handle(
                retrieveBaseValueObjectTemplates(attributes),
                retrieveOutputDir(attributes),
                BaseValueObjectTemplateGenerator.getInstance(),
                project,
                task);
    }

    /**
     * Handles given command.
     * @param templates the templates.
     * @param outputDir the output dir.
     * @param generator the <code>ValueObjectTemplateGenerator</code>
     * instance.
     * @param project the project, for logging purposes.
     * @param task the task, for logging purposes.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition templates != null
     * @precondition outputDir != null
     * @precondition generator != null

     */
    protected boolean handle(
        final BaseValueObjectTemplate[] templates,
        final File outputDir,
        final BaseValueObjectTemplateGenerator generator,
        final Project project,
        final Task task)
      throws  BuildException
    {
        boolean result = false;

        try 
        {
            for  (int t_iValueObjectIndex = 0;
                      t_iValueObjectIndex < templates.length;
                      t_iValueObjectIndex++)
            {
                generator.write(
                    templates[t_iValueObjectIndex], outputDir);
            }
        }
        catch  (IOException ioException)
        {
            throw new BuildException(ioException);
        }
        
        return result;
    }

    /**
     * Retrieves the base value object template from the attribute map.
     * @param parameters the parameter map.
     * @return the template.
     * @throws BuildException if the template retrieval process if faulty.
     * @precondition parameters != null
     */
    protected BaseValueObjectTemplate[] retrieveBaseValueObjectTemplates(final Map parameters)
        throws  BuildException
    {
        return
            (BaseValueObjectTemplate[])
                parameters.get(
                    TemplateMappingManager.BASE_VALUE_OBJECT_TEMPLATES);
    }

    /**
     * Retrieves the output dir from the attribute map.
     * @param parameters the parameter map.
     * @return such folder.
     * @throws BuildException if the output-dir retrieval process if faulty.
     * @precondition parameters != null
     */
    protected File retrieveOutputDir(final Map parameters)
        throws  BuildException
    {
        return
            retrieveOutputDir(
                retrieveBaseDir(parameters),
                retrieveProjectPackage(parameters),
                PackageUtils.getInstance());
    }

    /**
     * Retrieves the output dir from the attribute map.
     * @param baseDir the base dir.
     * @param projectPackage the project package.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @return such folder.
     * @throws BuildException if the output-dir retrieval process if faulty.
     * @precondition baseDir != null
     * @precondition projectPackage != null
     * @precondition packageUtils != null
     */
    protected File retrieveOutputDir(
        final File baseDir,
        final String projectPackage,
        final PackageUtils packageUtils)
      throws  BuildException
    {
        return
            packageUtils.retrieveBaseValueObjectFolder(
                baseDir, projectPackage);
    }

    /**
     * Retrieves the base dir from the attribute map.
     * @param parameters the parameter map.
     * @return the base dir.
     * @throws BuildException if the package retrieval process if faulty.
     * @precondition parameters != null
     */
    protected File retrieveBaseDir(final Map parameters)
        throws  BuildException
    {
        return (File) parameters.get(ParameterValidationHandler.OUTPUT_DIR);
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param parameters the parameter map.
     * @return the package name.
     * @throws BuildException if the package retrieval process if faulty.
     * @precondition parameters != null
     */
    protected String retrieveProjectPackage(final Map parameters)
        throws  BuildException
    {
        return (String) parameters.get(ParameterValidationHandler.PACKAGE);
    }
}
