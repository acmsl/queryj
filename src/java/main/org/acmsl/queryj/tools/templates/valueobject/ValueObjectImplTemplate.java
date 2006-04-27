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
 * Description: Is able to generate basic value objects implementations
 *              according to database metadata.
 *
 */
package org.acmsl.queryj.tools.templates.valueobject;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.tools.metadata.DecoratorFactory;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.metadata.MetadataTypeManager;
import org.acmsl.queryj.tools.templates.InvalidTemplateException;
import org.acmsl.queryj.tools.templates.TableTemplate;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.utils.EnglishGrammarUtils;
import org.acmsl.commons.utils.StringUtils;

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
 * Is able to generate basic value objects implementations according
 * to database metadata.
 * @author <a href="mailto:chous@acm-sl.org"
 *         >Jose San Leandro</a>
 */
public class ValueObjectImplTemplate
    extends  AbstractValueObjectImplTemplate
    implements  ValueObjectImplTemplateDefaults
{
    /**
     * Builds a <code>ValueObjectImplTemplate</code> using given information.
     * @param packageName the package name.
     * @param tableTemplate the table template.
     * @param metadataManager the metadata manager.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     */
    public ValueObjectImplTemplate(
        final String packageName,
        final TableTemplate tableTemplate,
        final MetadataManager metadataManager,
        final String header,
        final DecoratorFactory decoratorFactory)
    {
        super(
            PACKAGE_DECLARATION,
            packageName,
            tableTemplate,
            metadataManager,
//            (header != null) ? header : DEFAULT_HEADER,
            DEFAULT_HEADER,
            decoratorFactory,
            ACMSL_IMPORTS,
            JDK_IMPORTS,
            DEFAULT_JAVADOC,
            CLASS_DEFINITION,
            DEFAULT_CLASS_START,
            DEFAULT_FIELD_DECLARATION,
            DEFAULT_CONSTRUCTOR,
            DEFAULT_CONSTRUCTOR_FIELD_JAVADOC,
            DEFAULT_CONSTRUCTOR_FIELD_DECLARATION,
            DEFAULT_CONSTRUCTOR_FIELD_VALUE_SETTER,
            DEFAULT_FIELD_VALUE_SETTER_METHOD,
            DEFAULT_FIELD_VALUE_GETTER_METHOD,
            DEFAULT_CLASS_END);
    }

    /**
     * Retrieves the source code generated by this template.
     * @param header the header.
     * @return such code.
     * @throws InvalidTemplateException if the template is invalid.
     */
    protected String generateOutput(final String header)
        throws  InvalidTemplateException
    {
        return generateOutput(header, getMetadataManager());
    }
    
    /**
     * Retrieves the source code generated by this template.
     * @param header the header.
     * @param metadataManager the metadata manager.
     * @return such code.
     * @throws InvalidTemplateException if the template is invalid.
     * @precondition metadataManager != null
     */
    protected String generateOutput(
        final String header, final MetadataManager metadataManager)
      throws  InvalidTemplateException
    {
        return
            generateOutput(
                getTableTemplate(),
                metadataManager,
                getPackageName(),
                header,
                getPackageDeclaration(),
                getAcmslImports(),
                getJdkImports(),
                getJavadoc(),
                getClassDefinition(),
                getClassStart(),
                getFieldDeclaration(),
                getConstructor(),
                getConstructorFieldJavadoc(),
                getConstructorFieldDefinition(),
                getConstructorFieldValueSetter(),
                getFieldValueSetterMethod(),
                getFieldValueGetterMethod(),
                getClassEnd(),
                metadataManager.getMetadataTypeManager(),
                EnglishGrammarUtils.getInstance(),
                StringUtils.getInstance());
    }

    /**
     * Retrieves the source code generated by this template.
     * @param tableTemplate the table template.
     * @param metadataManager the metadata manager.
     * instance.
     * @param packageName the package name.
     * @param header the header.
     * @param packageDeclaration the package declaration.
     * @param acmslImports the ACM-SL imports.
     * @param jdkImports the JDK imports.
     * @param javadoc the class javadoc.
     * @param classDefinition the class definition.
     * @param classStart the class start.
     * @param fieldDeclaration the field declaration.
     * @param constructor the constructor.
     * @param constructorFieldJavadoc the constructor field javadoc.
     * @param constructorFieldDefinition the constructor field definition.
     * @param constructorFieldValueSetter the constructor field value setter.
     * @param fieldValueSetterMethod the field value setter method.
     * @param fieldValueGetterMethod the field value getter method.
     * @param classEnd the class end.
     * @param metadataTypeManager the metadata type manager.
     * @param englishGrammarUtils the <code>EnglishGrammarUtils</code>
     * instance.
     * @param stringUtils the <code>StringUtils</code> instance.
     * @return such code.
     * @throws InvalidTemplateException if the template is invalid.
     * @precondition tableTemplate != null
     * @precondition metadataManager != null
     * @precondition packageName != null
     * @precondition header != null
     * @precondition packageDeclaration != null
     * @precondition acmslImports != null
     * @precondition jdkImports != null
     * @precondition javadoc != null
     * @precondition classDefinition != null
     * @precondition classStart != null
     * @precondition fieldDeclaration != null
     * @precondition constructor != null
     * @precondition constructorFieldJavadoc != null
     * @precondition constructorFieldDefinition != null
     * @precondition constructorFieldValueSetter != null
     * @precondition fieldValueSetterMethod != null
     * @precondition fieldValueGetterMethod != null
     * @precondition classEnd != null
     * @precondition metadataTypeManager != null
     * @precondition englishGrammarUtils != null
     * @precondition stringUtils != null
     */
    protected String generateOutput(
        final TableTemplate tableTemplate,
        final MetadataManager metadataManager,
        final String packageName,
        final String header,
        final String packageDeclaration,
        final String acmslImports,
        final String jdkImports,
        final String javadoc,
        final String classDefinition,
        final String classStart,
        final String fieldDeclaration,
        final String constructor,
        final String constructorFieldJavadoc,
        final String constructorFieldDefinition,
        final String constructorFieldValueSetter,
        final String fieldValueSetterMethod,
        final String fieldValueGetterMethod,
        final String classEnd,
        final MetadataTypeManager metadataTypeManager,
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

        MessageFormat t_ConstructorFormatter =
            new MessageFormat(constructor);

        if  (t_lFields != null)
        {
            Iterator t_itFields = t_lFields.iterator();

            MessageFormat t_DeclarationFormatter =
                new MessageFormat(fieldDeclaration);

            StringBuffer t_sbConstructorFieldJavadoc = new StringBuffer();

            MessageFormat t_ConstructorFieldJavadocFormatter =
                new MessageFormat(constructorFieldJavadoc);

            StringBuffer t_sbConstructorFieldDefinition = new StringBuffer();

            MessageFormat t_ConstructorFieldDefinitionFormatter =
                new MessageFormat(constructorFieldDefinition);

            StringBuffer t_sbConstructorFieldValueSetter = new StringBuffer();

            MessageFormat t_ConstructorFieldValueSetterFormatter =
                new MessageFormat(constructorFieldValueSetter);

            StringBuffer t_sbFieldValueSetterMethod = new StringBuffer();

            MessageFormat t_FieldValueSetterMethodFormatter =
                new MessageFormat(fieldValueSetterMethod);

            StringBuffer t_sbFieldValueGetterMethod = new StringBuffer();

            MessageFormat t_FieldValueGetterMethodFormatter =
                new MessageFormat(fieldValueGetterMethod);

            while  (t_itFields.hasNext()) 
            {
                String t_strField = (String) t_itFields.next();

                int t_iColumnType =
                    metadataManager.getColumnType(
                        tableTemplate.getTableName(),
                        t_strField);

                boolean t_bAllowsNull = false;

                boolean t_bIsPrimaryKey =
                    metadataManager.isPartOfPrimaryKey(
                        tableTemplate.getTableName(),
                        t_strField);

                if  (!t_bIsPrimaryKey)
                {
                    t_bAllowsNull =
                        metadataManager.allowsNull(
                            tableTemplate.getTableName(),
                            t_strField);
                }

                String t_strFieldType =
                    metadataTypeManager.getNativeType(t_iColumnType, t_bAllowsNull);

                if  (t_bAllowsNull)
                {
                    t_strFieldType =
                        metadataTypeManager.getSmartObjectType(t_iColumnType);
                }

                t_sbResult.append(
                    t_DeclarationFormatter.format(
                        new Object[]
                        {
                            t_strField,
                            t_strFieldType,
                            t_strField.toLowerCase()
                        }));

                t_sbConstructorFieldJavadoc.append(
                    t_ConstructorFieldJavadocFormatter.format(
                        new Object[]
                        {
                            t_strField.toLowerCase(),
                            t_strField
                        }));

                t_sbConstructorFieldDefinition.append(
                    t_ConstructorFieldDefinitionFormatter.format(
                        new Object[]
                        {
                            t_strFieldType,
                            t_strField.toLowerCase()
                        }));

                if  (t_itFields.hasNext())
                {
                    t_sbConstructorFieldDefinition.append(",");
                }
                    
                t_sbConstructorFieldValueSetter.append(
                    t_ConstructorFieldValueSetterFormatter.format(
                        new Object[]
                        {
                            stringUtils.capitalize(
                                t_strField.toLowerCase(),
                                '_'),
                            t_strField.toLowerCase()
                        }));

                t_sbFieldValueSetterMethod.append(
                    t_FieldValueSetterMethodFormatter.format(
                        new Object[]
                        {
                            t_strField.toLowerCase(),
                            t_strFieldType,
                            stringUtils.capitalize(
                                t_strField.toLowerCase(),
                                '_'),
                        }));

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

            t_sbResult.append(
                t_ConstructorFormatter.format(
                    new Object[]
                    {
                        stringUtils.capitalize(
                            englishGrammarUtils.getSingular(
                                tableTemplate.getTableName().toLowerCase()),
                            '_'),
                        t_sbConstructorFieldJavadoc.toString(),
                        t_sbConstructorFieldDefinition.toString(),
                        t_sbConstructorFieldValueSetter.toString()
                    }));

            t_sbResult.append(t_sbFieldValueSetterMethod);
            t_sbResult.append(t_sbFieldValueGetterMethod);
        }

        t_sbResult.append(classEnd);

        return t_sbResult.toString();
    }
}
