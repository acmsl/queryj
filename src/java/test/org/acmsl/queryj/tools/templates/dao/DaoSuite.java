/*
                      Project tests

Copyright (C) 2003  Jose San Leandro Armend?riz
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
Contact info: jsr000@terra.es
Postal Address: c/Playa de Lagoa, 1
Urb. Valdecaba?as
Boadilla del monte
28660 Madrid
Spain

******************************************************************************
*
* Filename: $RCSfile$
*
* Author: Jose San Leandro Armend?riz
*
* Description: Executes all tests defined for package
*              unittests.org.acmsl.queryj.tools.templates.dao.
*
* Last modified by: $Author$ at $Date$
*
* File version: $Revision$
*
* Project version: $Name$
*
* $Id$
*/
package unittests.org.acmsl.queryj.tools.templates.dao;


/*
* Importing project classes.
*/

import unittests.org.acmsl.queryj.tools.templates.dao.handlers.HandlersSuite;
import unittests.org.acmsl.queryj.tools.templates.dao.mock.MockSuite;
import unittests.org.acmsl.queryj.tools.templates.dao.xml.XmlSuite;


/*
/* Importing JUnit classes.
*/
import junit.framework.TestSuite;

// JUnitDoclet begin import
// JUnitDoclet end import

/*
This file is part of  JUnitDoclet, a project to generate basic
test cases  from source code and  helping to keep them in sync
during refactoring.

Copyright (C) 2002  ObjectFab GmbH  (http://www.objectfab.de/)

This library is  free software; you can redistribute it and/or
modify  it under the  terms of  the  GNU Lesser General Public
License as published  by the  Free Software Foundation; either
version 2.1  of the  License, or  (at your option)  any  later
version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or  FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You  should  have  received a  copy of the  GNU Lesser General
Public License along with this  library; if not, write  to the
Free  Software  Foundation, Inc.,  59 Temple Place,  Suite 330,
Boston, MA  02111-1307  USA
*/


/**
* Executes all tests defined for package
* org.acmsl.queryj.tools.templates.dao
* @version $Revision$
* @see org.acmsl.queryj.tools.templates.dao
*/
public class DaoSuite
// JUnitDoclet begin extends_implements
// JUnitDoclet end extends_implements
{
  // JUnitDoclet begin class
  // JUnitDoclet end class
  
  public static TestSuite suite()
  {
    TestSuite suite;
    
    suite =
    new TestSuite("unittests.org.acmsl.queryj.tools.templates.dao");
    
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.DAOBundleTest.class);
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.DAOChooserTemplateGeneratorTest.class);
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.DataAccessManagerTemplateGeneratorTest.class);
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.ConfigurationPropertiesTemplateGeneratorTest.class);
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.JdbcDAOTemplateGeneratorTest.class);
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.DAOChooserUtilsTest.class);
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.ConfigurationPropertiesTemplateTest.class);
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.BaseDAOFactoryTemplateTest.class);
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.DAOTestTemplateGeneratorTest.class);
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.DAOTemplateUtilsTest.class);
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.DAOFactoryTemplateGeneratorTest.class);
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.DAOTemplateGeneratorTest.class);
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.DAOChooserTemplateTest.class);
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.BaseDAOTemplateGeneratorTest.class);
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.BaseDAOTemplateTest.class);
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.BaseDAOFactoryTemplateGeneratorTest.class);
    suite.addTestSuite(unittests.org.acmsl.queryj.tools.templates.dao.DAOTemplateTest.class);
    
    suite.addTest(unittests.org.acmsl.queryj.tools.templates.dao.handlers.HandlersSuite.suite());
    suite.addTest(unittests.org.acmsl.queryj.tools.templates.dao.mock.MockSuite.suite());
    suite.addTest(unittests.org.acmsl.queryj.tools.templates.dao.xml.XmlSuite.suite());
    
    
    // JUnitDoclet begin method suite
    // JUnitDoclet end method suite
    
    return suite;
  }
  
  public static void main(String[] args)
  {
    // JUnitDoclet begin method testsuite.main
    junit.textui.TestRunner.run(suite());
    // JUnitDoclet end method testsuite.main
  }
}
