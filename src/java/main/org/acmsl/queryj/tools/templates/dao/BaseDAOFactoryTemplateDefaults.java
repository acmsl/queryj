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
 * Description: Defines the default subtemplates used to generate base DAO
 *              factories according to database metadata.
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

/**
 * Defines the default subtemplates used to generate base DAO factories
 * according to database metadata.
 * @author <a href="mailto:jsanleandro@yahoo.es"
 *         >Jose San Leandro</a>
 * @version $Revision$
 */
public interface BaseDAOFactoryTemplateDefaults
{
    /**
     * The default header.
     */
    public static final String DEFAULT_HEADER =
          "/*\n"
        + "                        QueryJ\n"
        + "\n"
        + "    Copyright (C) 2002  Jose San Leandro Armendariz\n"
        + "                        jsanleandro@yahoo.es\n"
        + "                        chousz@yahoo.com\n"
        + "\n"
        + "    This library is free software; you can redistribute it and/or\n"
        + "    modify it under the terms of the GNU General Public\n"
        + "    License as published by the Free Software Foundation; either\n"
        + "    version 2 of the License, or any later "
        + "version.\n"
        + "\n"
        + "    This library is distributed in the hope that it will be "
        + "useful,\n"
        + "    but WITHOUT ANY WARRANTY; without even the implied warranty "
        + "of\n"
        + "    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the "
        + "GNU\n"
        + "    General Public License for more details.\n"
        + "\n"
        + "    You should have received a copy of the GNU General Public\n"
        + "    License along with this library; if not, write to the Free "
        + "Software\n"
        + "    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  "
        + "02111-1307  USA\n"
        + "\n"
        + "    Thanks to ACM S.L. for distributing this library under the GPL "
        + "license.\n"
        + "    Contact info: jsanleandro@yahoo.es\n"
        + "    Postal Address: c/Playa de Lagoa, 1\n"
        + "                    Urb. Valdecabanas\n"
        + "                    Boadilla del monte\n"
        + "                    28660 Madrid\n"
        + "                    Spain\n"
        + "\n"
        + " *****************************************************************"
        + "*************\n"
        + " *\n"
        + " * Filename: $" + "RCSfile: $\n"
        + " *\n"
        + " * Author: QueryJ\n"
        + " *\n"
        + " * Description: Is able to create {0} DAO instances.\n"
        + " *              (Abstract Factory pattern)\n"
        + " *\n"
        + " * Last modified by: $" + "Author: $ at $" + "Date: $\n"
        + " *\n"
        + " * File version: $" + "Revision: $\n"
        + " *\n"
        + " * Project version: $" + "Name: $\n"
        + " *\n"
        + " * $" + "Id: $\n"
        + " *\n"
        + " */\n";

    /**
     * The package declaration.
     */
    public static final String PACKAGE_DECLARATION =
        "package {0};\n\n"; // package

    /**
     * The ACM-SL imports.
     */
    public static final String DEFAULT_PROJECT_IMPORTS =
          "/*\n"
        + " * Importing some project classes.\n"
        + " */\n"
        + "import {0}.{1}DAO;\n" // package - Table
        + "import {2}.DAOChooser;\n\n"; // DAOChooser package

    /**
     * The JDK imports.
     */
    public static final String DEFAULT_JDK_IMPORTS =
          "/*\n"
        + " * Importing some JDK classes.\n"
        + " */\n"
        + "import java.lang.IllegalAccessException;\n"
        + "import java.lang.InstantiationException;\n"
        + "import java.lang.ref.WeakReference;\n\n";

    /**
     * The commons-logging imports.
     */
    public static final String DEFAULT_COMMONS_LOGGING_IMPORTS =
          "/*\n"
        + " * Importing some commons-logging classes.\n"
        + " */\n"
        + "import org.apache.commons.logging.LogFactory;\n\n";

    /**
     * The default class Javadoc.
     */
    public static final String DEFAULT_JAVADOC =
          "/**\n"
        + " * Is able to create {0} DAO instances (Abstract Factory pattern).\n"
        + " * @author <a href=\"http://maven.acm-sl.org/queryj\">QueryJ</a>\n"
        + " * @version $" + "Revision: $\n"
        + " */\n";

    /**
     * The class definition.
     */
    public static final String CLASS_DEFINITION =
           "public abstract class {0}DAOFactory\n"; // table

    /**
     * The class start.
     */
    public static final String DEFAULT_CLASS_START =
        "{\n";

    /**
     * The class getInstance() method.
     */
    public static final String DEFAULT_GET_INSTANCE_METHOD =
          "    /**\n"
        + "     * Retrieves a {0}DAOFactory instance.\n"
        + "     * @return such instance.\n"
        + "     */\n"
        + "    public static {0}DAOFactory getInstance()\n"
        + "    '{'\n"
        + "        return getInstance(DAOChooser.getInstance());\n"
        + "    '}'\n\n"
        + "    /**\n"
        + "     * Retrieves a {0}DAOFactory instance.\n"
        + "     * @param daoChooser the DAOChooser instance.\n"
        + "     * @return such instance.\n"
        + "     * @precondition daoChooser != null\n"
        + "     */\n"
        + "    protected static {0}DAOFactory getInstance(final DAOChooser daoChooser)\n"
        + "    '{'\n"
        + "        {0}DAOFactory result = null;\n\n"
        + "        String t_str{0}DAOFactoryClassName =\n"
        + "            t_DAOChooser.get{0}DAOFactoryClassName();\n\n"
        + "        if  (t_str{0}DAOFactoryClassName != null)\n"
        + "        '{'\n"
        + "            try\n"
        + "            '{'\n"
        + "                Class t_FactoryClass =\n"
        + "                    Class.forName(\n"
        + "                        t_str{0}DAOFactoryClassName);\n\n"
        + "                result =\n"
        + "                    ({0}DAOFactory) t_FactoryClass.newInstance();\n"
        + "            '}'\n"
        + "            catch  (final ClassNotFoundException classNotFoundException)\n"
        + "            '{'\n"
        + "                LogFactory.getLog({0}DAOFactory.class).error(\n"
        + "                    \"Cannot find {0} DAO Factory implementation class\",\n"
        + "                    classNotFoundException);\n"
        + "            '}'\n"
        + "            catch  (final InstantiationException instantiationException)\n"
        + "            '{'\n"
        + "                LogFactory.getLog({0}DAOFactory.class).error(\n"
        + "                    \"Cannot instantiate {0} DAO Factory implementation\",\n"
        + "                    instantiationException);\n"
        + "            '}'\n"
        + "            catch  (final IllegalAccessException illegalAccessException)\n"
        + "            '{'\n"
        + "                LogFactory.getLog({0}DAOFactory.class).error(\n"
        + "                    \"Cannot access {0} DAO Factory implementation\",\n"
        + "                    illegalAccessException);\n"
        + "            '}'\n"
        + "        '}'\n"
        + "        else\n"
        + "        '{'\n"
        + "            LogFactory.getLog({0}DAOFactory.class).error(\n"
        + "                \"{0} DAO Factory implementation not specified\");\n"
        + "        '}'\n\n"
        + "        return result;\n"
        + "    '}'\n\n";

    /**
     * The factory method.
     */
    public static final String DEFAULT_FACTORY_METHOD =
          "    /**\n"
        + "     * Creates {0} DAO.\n"
        + "     * @return such DAO.\n"
        + "     */\n"
        + "    public abstract {0}DAO create{0}DAO();\n"; // table

    /**
     * The default class end.
     */
    public static final String DEFAULT_CLASS_END = "}";
}
