/*
                        QueryJ

    Copyright (C) 2002  Jose San Leandro Armendáriz
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
                    Urb. Valdecabañas
                    Boadilla del monte
                    28660 Madrid
                    Spain

 ******************************************************************************
 *
 * Filename: $RCSfile$
 *
 * Author: Jose San Leandro Armendáriz
 *
 * Description: Is able to generate MySQL's numeric function repositories.
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
package org.acmsl.queryj.tools.templates.functions.numeric.mysql;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.QueryJException;
import org.acmsl.queryj.tools.templates.functions.numeric.mysql
    .MySQLNumericFunctionsTemplate;

import org.acmsl.queryj.tools.templates.functions.numeric
    .NumericFunctionsTemplate;

import org.acmsl.queryj.tools.templates.functions.numeric
    .NumericFunctionsTemplateGenerator;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.utils.io.FileUtils;
import org.acmsl.commons.utils.StringUtils;

/*
 * Importing some Ant classes.
 */
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Is able to generate MySQL's numeric function repositories.
 * @author <a href="mailto:jsanleandro@yahoo.es"
           >Jose San Leandro</a>
 * @version $Revision$
 */
public class MySQLNumericFunctionsTemplateGenerator
    extends  NumericFunctionsTemplateGenerator
{
    /**
     * Singleton implemented as a weak reference.
     */
    private static WeakReference singleton;

    /**
     * Public constructor to allow reflective instantiation.
     */
    public MySQLNumericFunctionsTemplateGenerator() {};

    /**
     * Specifies a new weak reference.
     * @param generator the generator instance to use.
     */
    protected static void setMySQLReference(MySQLNumericFunctionsTemplateGenerator generator)
    {
        singleton = new WeakReference(generator);
    }

    /**
     * Retrieves the weak reference.
     * @return such reference.
     */
    protected static WeakReference getReference()
    {
        return singleton;
    }

    /**
     * Retrieves a MySQLNumericFunctionsTemplateGenerator instance.
     * @return such instance.
     */
    public static MySQLNumericFunctionsTemplateGenerator getMySQLInstance()
    {
        MySQLNumericFunctionsTemplateGenerator result = null;

        WeakReference reference = getReference();

        if  (reference != null) 
        {
            result = (MySQLNumericFunctionsTemplateGenerator) reference.get();
        }

        if  (result == null) 
        {
            result = new MySQLNumericFunctionsTemplateGenerator() {};

            setReference(result);
        }

        return result;
    }

    /**
     * Generates a numeric functions template.
     * @param header the header.
     * @param packageDeclaration the package declaration.
     * @param packageName the package name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the identifier quote string.
     * @param acmslImports the ACM-SL imports.
     * @param jdkImports the JDK imports.
     * @param javadoc the class Javadoc.
     * @param classDefinition the class definition.
     * @param classStart the class start.
     * @param singletonBody the singleton body.
     * @param classConstructor the class constructor.
     * @param innerClass the inner class.
     * @param classEnd the class end.
     * @return a template.
     * @throws QueryJException if the factory class is invalid.
     */
    public NumericFunctionsTemplate createNumericFunctionsTemplate(
        String header,
        String packageDeclaration,
        String packageName,
        String engineName,
        String engineVersion,
        String quote,
        String acmslImports,
        String jdkImports,
        String javadoc,
        String classDefinition,
        String classStart,
        String singletonBody,
        String classConstructor,
        String innerClass,
        String classEnd)
      throws  QueryJException
    {
        NumericFunctionsTemplate result = null;

        if  (   (packageName   != null)
             && (engineName    != null)
             && (engineVersion != null)
             && (quote         != null))
        {
            result =
                new MySQLNumericFunctionsTemplate(
                    header,
                    packageDeclaration,
                    packageName,
                    engineName,
                    engineVersion,
                    quote,
                    acmslImports,
                    jdkImports,
                    javadoc,
                    classDefinition,
                    classStart,
                    singletonBody,
                    classConstructor,
                    innerClass,
                    classEnd) {};
        }

        return result;
    }

    /**
     * Generates a numeric functions template.
     * @param header the header.
     * @param packageDeclaration the package declaration.
     * @param packageName the package name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the identifier quote string.
     * @param acmslImports the ACM-SL imports.
     * @param jdkImports the JDK imports.
     * @param javadoc the class Javadoc.
     * @param classDefinition the class definition.
     * @param classStart the class start.
     * @param singletonBody the singleton body.
     * @param classConstructor the class constructor.
     * @param innerClass the inner class.
     * @param classEnd the class end.
     * @param project the project, for logging purposes.
     * @param task the task, for logging purposes.
     * @return a template.
     * @throws QueryJException if the factory class is invalid.
     */
    public NumericFunctionsTemplate createNumericFunctionsTemplate(
        String  header,
        String  packageDeclaration,
        String  packageName,
        String  engineName,
        String  engineVersion,
        String  quote,
        String  acmslImports,
        String  jdkImports,
        String  javadoc,
        String  classDefinition,
        String  classStart,
        String  singletonBody,
        String  classConstructor,
        String  innerClass,
        String  classEnd,
        Project project,
        Task    task)
      throws  QueryJException
    {
        NumericFunctionsTemplate result = null;

        if  (   (packageName   != null)
             && (engineName    != null)
             && (engineVersion != null)
             && (quote         != null))
        {
            result =
                new MySQLNumericFunctionsTemplate(
                    header,
                    packageDeclaration,
                    packageName,
                    engineName,
                    engineVersion,
                    quote,
                    acmslImports,
                    jdkImports,
                    javadoc,
                    classDefinition,
                    classStart,
                    singletonBody,
                    classConstructor,
                    innerClass,
                    classEnd,
                    project,
                    task) {};
        }

        return result;
    }

    /**
     * Generates a numeric functions template.
     * @param packageName the package name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the identifier quote string.
     * @return a template.
     * @throws QueryJException if the factory class is invalid.
     */
    public NumericFunctionsTemplate createNumericFunctionsTemplate(
        String packageName,
        String engineName,
        String engineVersion,
        String quote)
      throws  QueryJException
    {
        NumericFunctionsTemplate result = null;

        if  (   (packageName   != null)
             && (engineName    != null)
             && (engineVersion != null)
             && (quote         != null))
        {
            result =
                new MySQLNumericFunctionsTemplate(
                    packageName,
                    engineName,
                    engineVersion,
                    quote) {};
        }

        return result;
    }

    /**
     * Generates a numeric functions template.
     * @param packageName the package name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the identifier quote string.
     * @param project the project, for logging purposes.
     * @param task the task, for logging purposes.
     * @return a template.
     * @throws QueryJException if the factory class is invalid.
     */
    public NumericFunctionsTemplate createNumericFunctionsTemplate(
        String  packageName,
        String  engineName,
        String  engineVersion,
        String  quote,
        Project project,
        Task    task)
      throws  QueryJException
    {
        NumericFunctionsTemplate result = null;

        if  (   (packageName   != null)
             && (engineName    != null)
             && (engineVersion != null)
             && (quote         != null))
        {
            result =
                new MySQLNumericFunctionsTemplate(
                    packageName,
                    engineName,
                    engineVersion,
                    quote,
                    project,
                    task) {};
        }

        return result;
    }
}
