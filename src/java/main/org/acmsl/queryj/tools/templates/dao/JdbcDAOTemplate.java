/*
                        QueryJ

    Copyright (C) 2002-2007  Jose San Leandro Armendariz
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
 * Filename: JdbcDAOTemplate.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Is able to create abstract base JDBC DAO.
 *
 */
package org.acmsl.queryj.tools.templates.dao;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.tools.metadata.DecoratorFactory;
import org.acmsl.queryj.tools.templates.TableTemplate;

/*
 * Importing some ACM-SL classes.
 */
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
 * Is able to create abstract base JDBC DAO.
 * @author <a href="mailto:chous@acm-sl.org"
 *         >Jose San Leandro</a>
 */
public class JdbcDAOTemplate
    extends  AbstractJdbcDAOTemplate
    implements JdbcDAOTemplateDefaults
{
    /**
     * Builds a JdbcDAOTemplate using given information.
     * @param header the header.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param packageName the package name.
     */
    public JdbcDAOTemplate(
        final String header,
        final DecoratorFactory decoratorFactory,
        final String packageName)
    {
        super(
//            (header != null) ? header : DEFAULT_HEADER,
            DEFAULT_HEADER,
            decoratorFactory,
            DEFAULT_PACKAGE_DECLARATION,
            packageName,
            DEFAULT_ACMSL_IMPORTS,
            DEFAULT_JDK_IMPORTS,
            DEFAULT_JDK_EXTENSION_IMPORTS,
            DEFAULT_LOGGING_IMPORTS,
            DEFAULT_JAVADOC,
            DEFAULT_CLASS_DEFINITION,
            DEFAULT_CLASS_START,
            DEFAULT_CLASS_CONSTRUCTOR,
            DEFAULT_ATTRIBUTE_ACCESSORS,
            DEFAULT_CONNECTION_RETRIEVAL_METHODS,
            DEFAULT_COMMIT_METHODS,
            DEFAULT_ROLLBACK_METHODS,
            DEFAULT_CONNECTION_CLOSING_METHODS,
            DEFAULT_TRANSACTION_TOKEN_FACTORY_METHODS,
            DEFAULT_CLASS_END);
    }

    /**
     * Retrieves the source code generated by this template.
     * @param header the header.
     * @return such code.
     */
    protected String generateOutput(final String header)
    {
        return
            generateOutput(
                header,
                getPackageDeclaration(),
                getPackageName(),
                getAcmslImports(),
                getJdkImports(),
                getJdkExtensionImports(),
                getLoggingImports(),
                getJavadoc(),
                getClassDefinition(),
                getClassStart(),
                getClassConstructor(),
                getAttributeAccessors(),
                getConnectionRetrievalMethods(),
                getCommitMethods(),
                getRollbackMethods(),
                getConnectionClosingMethods(),
                getTransactionTokenFactoryMethods(),
                getClassEnd(),
                StringUtils.getInstance());
    }

    /**
     * Retrieves the source code generated by this template.
     * @param header the header.
     * @param packageDeclaration the package declaration.
     * @param packageName the package name.
     * @param acmslImports the ACM-SL imports.
     * @param jdkImports the JDK imports.
     * @param jdkExtensionImports the JDK extension imports.
     * @param loggingImports the commons-logging imports.
     * @param javadoc the class Javadoc.
     * @param classDefinition the class definition.
     * @param classStart the class start.
     * @param classConstructor the class constructor.
     * @param attributeAccessors the attribute accessors.
     * @param connectionRetrievalMethods the connection retrieval methods.
     * @param commitMethods the commit methods.
     * @param rollbackMethods the rollback methods.
     * @param connectionClosingMethods the connection closing methods.
     * @param transactionTokenFactoryMethods the transaction token factory
     * methods.
     * @param classEnd the class end.
     * @param stringUtils the <code>StringUtils</code> instance.
     * @return such code.
     * @precondition stringUtils != null
     */
    protected String generateOutput(
        final String header,
        final String packageDeclaration,
        final String packageName,
        final String acmslImports,
        final String jdkImports,
        final String jdkExtensionImports,
        final String loggingImports,
        final String javadoc,
        final String classDefinition,
        final String classStart,
        final String classConstructor,
        final String attributeAccessors,
        final String connectionRetrievalMethods,
        final String commitMethods,
        final String rollbackMethods,
        final String connectionClosingMethods,
        final String transactionTokenFactoryMethods,
        final String classEnd,
        final StringUtils stringUtils)
    {
        StringBuffer t_sbResult = new StringBuffer();

        t_sbResult.append(header);

        MessageFormat t_PackageDeclarationFormatter =
            new MessageFormat(packageDeclaration);

        t_sbResult.append(
            t_PackageDeclarationFormatter.format(
                new Object[]{packageName}));

        t_sbResult.append(acmslImports);
        t_sbResult.append(jdkImports);
        t_sbResult.append(jdkExtensionImports);
        t_sbResult.append(loggingImports);

        t_sbResult.append(javadoc);

        t_sbResult.append(classDefinition);

        t_sbResult.append(classStart);

        t_sbResult.append(classConstructor);

        t_sbResult.append(attributeAccessors);
        t_sbResult.append(connectionRetrievalMethods);
        t_sbResult.append(commitMethods);
        t_sbResult.append(rollbackMethods);
        t_sbResult.append(connectionClosingMethods);
        t_sbResult.append(transactionTokenFactoryMethods);

        t_sbResult.append(classEnd);

        return t_sbResult.toString();
    }
}
