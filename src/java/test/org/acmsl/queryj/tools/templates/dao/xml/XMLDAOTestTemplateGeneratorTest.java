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
*              unittests.org.acmsl.queryj.tools.templates.dao.xml.
*
*/
package unittests.org.acmsl.queryj.tools.templates.dao.xml;

/*
* Importing project classes.
*/
// JUnitDoclet begin import
import org.acmsl.queryj.tools.templates.dao.xml.XMLDAOTestTemplateGenerator;
// JUnitDoclet end import

/*
* Importing JUnit classes.
*/
import junit.framework.TestCase;

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
* Tests XMLDAOTestTemplateGeneratorTest class.
* @see org.acmsl.queryj.tools.templates.dao.xml.XMLDAOTestTemplateGenerator
*/
public class XMLDAOTestTemplateGeneratorTest
// JUnitDoclet begin extends_implements
extends TestCase
// JUnitDoclet end extends_implements
{
  // JUnitDoclet begin class
  org.acmsl.queryj.tools.templates.dao.xml.XMLDAOTestTemplateGenerator xmldaotesttemplategenerator = null;
  // JUnitDoclet end class
  
  /**
  * Creates a XMLDAOTestTemplateGeneratorTest with given name.
  * @param name such name.
  */
  public XMLDAOTestTemplateGeneratorTest(String name)
  {
    // JUnitDoclet begin method XMLDAOTestTemplateGeneratorTest
    super(name);
    // JUnitDoclet end method XMLDAOTestTemplateGeneratorTest
  }
  
  /**
  * Creates an instance of the tested class.
  * @return such instance.
  
  */
  public org.acmsl.queryj.tools.templates.dao.xml.XMLDAOTestTemplateGenerator createInstance()
  throws Exception
  {
    // JUnitDoclet begin method testcase.createInstance
    return org.acmsl.queryj.tools.templates.dao.xml.XMLDAOTestTemplateGenerator.getInstance();
    // JUnitDoclet end method testcase.createInstance
  }
  
  /**
  * Performs any required steps before each test.
  * @throws Exception if an unexpected situation occurs.
  */
  protected void setUp()
  throws Exception
  {
    // JUnitDoclet begin method testcase.setUp
    super.setUp();
    xmldaotesttemplategenerator = createInstance();
    // JUnitDoclet end method testcase.setUp
  }
  
  /**
  * Performs any required steps after each test.
  * @throws Exception if an unexpected situation occurs.
  */
  protected void tearDown()
  throws Exception
  {
    // JUnitDoclet begin method testcase.tearDown
    xmldaotesttemplategenerator = null;
    super.tearDown();
    // JUnitDoclet end method testcase.tearDown
  }
  
  /**
  * Tests XMLDAOTestTemplateGeneratorTestgetInstance()
  * @throws Exception if an unexpected situation occurs.
  * @see org.acmsl.queryj.tools.templates.dao.xml.XMLDAOTestTemplateGenerator#getInstance()
  */
  public void testGetInstance()
  throws Exception
  {
    // JUnitDoclet begin method getInstance
    // JUnitDoclet end method getInstance
  }
  
  /**
  * Tests XMLDAOTestTemplateGeneratorTestaddTemplateFactoryClass()
  * @throws Exception if an unexpected situation occurs.
  * @see org.acmsl.queryj.tools.templates.dao.xml.XMLDAOTestTemplateGenerator#addTemplateFactoryClass(java.lang.String, java.lang.String)
  */
  public void testAddTemplateFactoryClass()
  throws Exception
  {
    // JUnitDoclet begin method addTemplateFactoryClass
    // JUnitDoclet end method addTemplateFactoryClass
  }
  
  /**
  * Tests XMLDAOTestTemplateGeneratorTestcreateXMLDAOTestTemplate()
  * @throws Exception if an unexpected situation occurs.
  * @see org.acmsl.queryj.tools.templates.dao.xml.XMLDAOTestTemplateGenerator#createXMLDAOTestTemplate(org.acmsl.queryj.tools.templates.TableTemplate, org.acmsl.queryj.tools.DatabaseMetaDataManager, java.lang.String, java.lang.String, java.lang.String)
  */
  public void testCreateXMLDAOTestTemplate()
  throws Exception
  {
    // JUnitDoclet begin method createXMLDAOTestTemplate
    // JUnitDoclet end method createXMLDAOTestTemplate
  }
  
  /**
  * Tests XMLDAOTestTemplateGeneratorTestwrite()
  * @throws Exception if an unexpected situation occurs.
  * @see org.acmsl.queryj.tools.templates.dao.xml.XMLDAOTestTemplateGenerator#write(org.acmsl.queryj.tools.templates.dao.xml.XMLDAOTestTemplate, java.io.File, org.apache.tools.ant.Project, org.apache.tools.ant.Task)
  */
  public void testWrite()
  throws Exception
  {
    // JUnitDoclet begin method write
    // JUnitDoclet end method write
  }
  
  
  
  /**
  * JUnitDoclet moves marker to this method, if there is not match
  * for them in the regenerated code and if the marker is not empty.
  * This way, no test gets lost when regenerating after renaming.
  * Method testVault is supposed to be empty.
  * @throws Exception if an unexpected situation occurs.
  */
  public void testVault()
  throws Exception
  {
    // JUnitDoclet begin method testcase.testVault
    // JUnitDoclet end method testcase.testVault
  }
  
  public static void main(String[] args)
  {
    // JUnitDoclet begin method testcase.main
    junit.textui.TestRunner.run(XMLDAOTestTemplateGeneratorTest.class);
    // JUnitDoclet end method testcase.main
  }
}
