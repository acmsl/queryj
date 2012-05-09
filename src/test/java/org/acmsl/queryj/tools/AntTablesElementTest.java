/*
                      Project tests

Copyright (C) 2003  Jose San Leandro Armend?riz
chous@acm-sl.org

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
Contact info: pepe@acm-sl.com
Postal Address: c/Playa de Lagoa, 1
Urb. Valdecaba?as
Boadilla del monte
28660 Madrid
Spain

******************************************************************************
*
* Filename: AntTablesElementTest.java
*
* Author: Jose San Leandro Armend?riz
*
* Description: Executes all tests defined for package
*              org.acmsl.queryj.tools.
*
*/
package org.acmsl.queryj.tools;

/*
* Importing project classes.
*/
// JUnitDoclet begin import
import org.acmsl.queryj.tools.AntTablesElement;
// JUnitDoclet end import

/*
* Importing JUnit classes.
*/
import junit.framework.TestCase;

/*
This file is part of  JUnitDoclet, a project to generate basic
test cases  from source code and  helping to keep them in sync
during refactoring.

Copyright (C) 2002-2007  ObjectFab GmbH  (http://www.objectfab.de/)

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
* Tests AntTablesElementTest class.
* @see org.acmsl.queryj.tools.AntTablesElement
*/
public class AntTablesElementTest
// JUnitDoclet begin extends_implements
extends TestCase
// JUnitDoclet end extends_implements
{
  // JUnitDoclet begin class
  org.acmsl.queryj.tools.AntTablesElement anttableselement = null;
  // JUnitDoclet end class
  
  /**
  * Creates a AntTablesElementTest with given name.
  * @param name such name.
  */
  public AntTablesElementTest(String name)
  {
    // JUnitDoclet begin method AntTablesElementTest
    super(name);
    // JUnitDoclet end method AntTablesElementTest
  }
  
  /**
  * Creates an instance of the tested class.
  * @return such instance.
  
  */
  public org.acmsl.queryj.tools.AntTablesElement createInstance()
  throws Exception
  {
    // JUnitDoclet begin method testcase.createInstance
    return new org.acmsl.queryj.tools.AntTablesElement();
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
    anttableselement = createInstance();
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
    anttableselement = null;
    super.tearDown();
    // JUnitDoclet end method testcase.tearDown
  }
  
  /**
  * Tests AntTablesElementTestgetTables()
  * @throws Exception if an unexpected situation occurs.
  * @see org.acmsl.queryj.tools.AntTablesElement#getTables()
  */
  public void testGetTables()
  throws Exception
  {
    // JUnitDoclet begin method getTables
    // JUnitDoclet end method getTables
  }
  
  /**
  * Tests AntTablesElementTestsetDynamicAttribute()
  * @throws Exception if an unexpected situation occurs.
  * @see org.acmsl.queryj.tools.AntTablesElement#setDynamicAttribute(java.lang.String, java.lang.String)
  */
  public void testSetDynamicAttribute()
  throws Exception
  {
    // JUnitDoclet begin method setDynamicAttribute
    // JUnitDoclet end method setDynamicAttribute
  }
  
  /**
  * Tests AntTablesElementTestcreateDynamicElement()
  * @throws Exception if an unexpected situation occurs.
  * @see org.acmsl.queryj.tools.AntTablesElement#createDynamicElement(java.lang.String)
  */
  public void testCreateDynamicElement()
  throws Exception
  {
    // JUnitDoclet begin method createDynamicElement
    // JUnitDoclet end method createDynamicElement
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
    junit.textui.TestRunner.run(AntTablesElementTest.class);
    // JUnitDoclet end method testcase.main
  }
}