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
 * Description: Is able to generate value objects according to database
 *              metadata.
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
package org.acmsl.queryj.tools.templates.valueobject;

/*
 * Importing project-specific classes.
 */
import org.acmsl.queryj.tools.DatabaseMetaDataManager;
import org.acmsl.queryj.tools.MetaDataUtils;
import org.acmsl.queryj.tools.templates.TableTemplate;
import org.acmsl.queryj.tools.templates.InvalidTemplateException;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.utils.EnglishGrammarUtils;
import org.acmsl.commons.utils.StringUtils;

/*
 * Importing Ant classes.
 */
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/*
 * Importing some JDK classes.
 */
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Is able to generate base value objects according to database metadata.
 * @author <a href="mailto:jsanleandro@yahoo.es"
 *         >Jose San Leandro</a>
 * @version $Revision$
 */
public class BaseValueObjectTemplate
    extends  AbstractBaseValueObjectTemplate
    implements  BaseValueObjectTemplateDefaults
{
    /**
     * Builds a <code>BaseValueObjectTemplate</code> using given information.
     * @param packageName the package name.
     * @param tableTemplate the table template.
     * @param metaDataManager the metadata manager.
     * @param project the project, for logging purposes.
     * @param task the task, for logging purposes.
     */
    public BaseValueObjectTemplate(
        final String packageName,
        final TableTemplate tableTemplate,
        final DatabaseMetaDataManager metaDataManager,
        final Project project,
        final Task task)
    {
        super(
            DEFAULT_HEADER,
            PACKAGE_DECLARATION,
            packageName,
            tableTemplate,
            metaDataManager,
            ACMSL_IMPORTS,
            JDK_IMPORTS,
            DEFAULT_JAVADOC,
            CLASS_DEFINITION,
            DEFAULT_CLASS_START,
            DEFAULT_FIELD_VALUE_GETTER_METHOD,
            DEFAULT_CLASS_END,
            project,
            task);
    }

    /**
     * Retrieves the source code generated by this template.
     * @return such code.
     * @throws InvalidTemplateException if the template is invalid.
     */
    protected String generateOutput()
        throws  InvalidTemplateException
    {
        return
            generateOutput(
                getTableTemplate(),
                getMetaDataManager(),
                getPackageName(),
                getHeader(),
                getPackageDeclaration(),
                getAcmslImports(),
                getJdkImports(),
                getJavadoc(),
                getClassDefinition(),
                getClassStart(),
                getFieldValueGetterMethod(),
                getClassEnd(),
                MetaDataUtils.getInstance(),
                EnglishGrammarUtils.getInstance(),
                StringUtils.getInstance());
    }

    /**
     * Retrieves the source code generated by this template.
     * @param tableTemplate the table template.
     * @param metaDataManager the <code>DatabaseMetaDataManager</code>
     * instance.
     * @param packageName the package name.
     * @param header the header.
     * @param packageDeclaration the package declaration.
     * @param acmslImports the ACM-SL imports.
     * @param jdkImports the JDK imports.
     * @param javadoc the class javadoc.
     * @param classDefinition the class definition.
     * @param classStart the class start.
     * @param fieldValueGetterMethod the field value getter method.
     * @param classEnd the class end.
     * @param metaDataUtils the <code>MetaDataUtils</code> instance.
     * @param englishGrammarUtils the <code>EnglishGrammarUtils</code>
     * instance.
     * @param stringUtils the <code>StringUtils</code> instance.
     * @return such code.
     * @throws InvalidTemplateException if the template is invalid.
     * @precondition tableTemplate != null
     * @precondition metaDataManager != null
     * @precondition packageName != null
     * @precondition header != null
     * @precondition packageDeclaration != null
     * @precondition acmslImports != null
     * @precondition jdkImports != null
     * @precondition javadoc != null
     * @precondition classDefinition != null
     * @precondition classStart != null
     * @precondition fieldValueGetterMethod != null
     * @precondition classEnd != null
     * @precondition metaDataUtils != null
     * @precondition englishGrammarUtils != null
     * @precondition stringUtils != null
     */
    protected String generateOutput(
        final TableTemplate tableTemplate,
        final DatabaseMetaDataManager metaDataManager,
        final String packageName,
        final String header,
        final String packageDeclaration,
        final String acmslImports,
        final String jdkImports,
        final String javadoc,
        final String classDefinition,
        final String classStart,
        final String fieldValueGetterMethod,
        final String classEnd,
        final MetaDataUtils metaDataUtils,
        final EnglishGrammarUtils englishGrammarUtils,
        final StringUtils stringUtils)
      throws  InvalidTemplateException
    {
        StringBuffer t_sbResult = new StringBuffer();

        MessageFormat t_Formatter = new MessageFormat(header);
        t_sbResult.append(
            t_Formatter.format(
                new Object[]
                {
                    englishGrammarUtils.getSingular(
                        tableTemplate.getTableName())
                }));

        t_Formatter = new MessageFormat(packageDeclaration);
        t_sbResult.append(
            t_Formatter.format(
                new Object[]
                {
                    packageName
                }));

        //t_sbResult.append(acmslImports);
        t_sbResult.append(jdkImports);

        t_Formatter = new MessageFormat(javadoc);
        t_sbResult.append(
            t_Formatter.format(
                new Object[]
                {
                    englishGrammarUtils.getSingular(
                        tableTemplate.getTableName())
                }));

        t_Formatter = new MessageFormat(classDefinition);
        t_sbResult.append(
            t_Formatter.format(
                new Object[]
                {
                    stringUtils.capitalize(
                        englishGrammarUtils.getSingular(
                            tableTemplate.getTableName().toLowerCase()),
                        '_')
                }));

        t_sbResult.append(classStart);

        List t_lFields = tableTemplate.getFields();

        if  (t_lFields != null)
        {
            Iterator t_itFields = t_lFields.iterator();

            StringBuffer t_sbFieldValueGetterMethod = new StringBuffer();

            MessageFormat t_FieldValueGetterMethodFormatter =
                new MessageFormat(fieldValueGetterMethod);

            while  (t_itFields.hasNext()) 
            {
                String t_strField = (String) t_itFields.next();

                String t_strFieldType =
                    metaDataUtils.getNativeType(
                        metaDataManager.getColumnType(
                            tableTemplate.getTableName(),
                            t_strField));

                t_sbFieldValueGetterMethod.append(
                    t_FieldValueGetterMethodFormatter.format(
                        new Object[]
                        {
                            t_strField.toLowerCase(),
                            t_strFieldType,
                            stringUtils.capitalize(
                                t_strField.toLowerCase(),
                                '_'),
                        }));
            }

            t_sbResult.append(t_sbFieldValueGetterMethod);
        }

        t_sbResult.append(classEnd);

        return t_sbResult.toString();
    }
}
