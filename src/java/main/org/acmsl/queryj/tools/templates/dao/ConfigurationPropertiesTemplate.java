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
 * Description: Is able to generate the configuration file for configuring
 *              DAOChooser.
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
package org.acmsl.queryj.tools.templates.dao;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.PackageUtils;

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
 * Is able to generate the configuration file for configuring
 * DAOChooser.
 * @author <a href="mailto:jsanleandro@yahoo.es"
 *         >Jose San Leandro</a>
 * @version $Revision$
 */
public class ConfigurationPropertiesTemplate
    extends  AbstractConfigurationPropertiesTemplate
    implements  ConfigurationPropertiesTemplateDefaults
{
    /**
     * Builds a <code>ConfigurationPropertiesTemplate</code> using given
     * information.
     * @param repository the repository.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param basePackageName the base package name.
     */
    public ConfigurationPropertiesTemplate(
        final String repository,
        final String engineName,
        final String engineVersion,
        final String basePackageName)
    {
        super(
            DEFAULT_HEADER,
            repository,
            engineName,
            engineVersion,
            basePackageName,
            DEFAULT_DAO_FACTORY_SETTING);
    }

    /**
     * Retrieves the source code of the generated field tableName.
     * @return such source code.
     */
    public String toString()
    {
        return
            toString(
                PackageUtils.getInstance(),
                EnglishGrammarUtils.getInstance(),
                StringUtils.getInstance());
    }

    /**
     * Retrieves the source code of the generated field tableName.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @param englishGrammarUtils the <code>EnglishGrammarUtils</code>
     * instance.
     * @param stringUtils the <code>StringUtils</code> instance.
     * @return such source code.
     * @precondition packageUtils != null
     * @precondition englishGrammarUtils != null
     * @precondition stringUtils != null
     */
    protected String toString(
        final PackageUtils packageUtils,
        final EnglishGrammarUtils englishGrammarUtils,
        final StringUtils stringUtils)
    {
        StringBuffer t_sbResult = new StringBuffer();

        t_sbResult.append(getHeader());

        List t_lTables = getTables();

        if  (t_lTables != null)
        {
            MessageFormat t_DAOFactorySettingFormatter =
                new MessageFormat(getDAOFactorySetting());

            Iterator t_itTables = t_lTables.iterator();

            while  (   (t_itTables != null)
                    && (t_itTables.hasNext()))
            {
                String t_strTable = (String) t_itTables.next();

                if  (t_strTable != null)
                {
                    String t_strCapitalizedTable =
                        stringUtils.capitalize(
                            englishGrammarUtils.getSingular(
                                t_strTable),
                            '_');

                    t_sbResult.append(
                        t_DAOFactorySettingFormatter.format(
                            new Object[]
                            {
                                t_strCapitalizedTable,
                                getRepository(),
                                t_strCapitalizedTable.toLowerCase(),
                                packageUtils.retrieveDAOFactoryPackage(
                                    getBasePackageName(),
                                    getEngineName()),
                                stringUtils.capitalize(
                                    getEngineName(),
                                    '_'),
                                packageUtils.retrieveMockDAOFactoryPackage(
                                    getBasePackageName()),
                                packageUtils.retrieveXMLDAOFactoryPackage(
                                    getBasePackageName())
                            }));
                }
            }
        }

        return t_sbResult.toString();
    }
}
