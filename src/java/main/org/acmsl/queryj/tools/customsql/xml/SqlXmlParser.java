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
 * Description: Is able to read the contents contained in QueryJ's sql.xml files.
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
package org.acmsl.queryj.tools.customsql.xml;

/*
 * Importing project-specific classes.
 */
import org.acmsl.queryj.tools.customsql.AbstractIdElement;
import org.acmsl.queryj.tools.customsql.ConnectionFlagsElement;
import org.acmsl.queryj.tools.customsql.ConnectionFlagsRefElement;
import org.acmsl.queryj.tools.customsql.CustomSqlProvider;
import org.acmsl.queryj.tools.customsql.ParameterElement;
import org.acmsl.queryj.tools.customsql.ParameterRefElement;
import org.acmsl.queryj.tools.customsql.PropertyElement;
import org.acmsl.queryj.tools.customsql.PropertyRefElement;
import org.acmsl.queryj.tools.customsql.ResultElement;
import org.acmsl.queryj.tools.customsql.ResultRefElement;
import org.acmsl.queryj.tools.customsql.ResultSetFlagsElement;
import org.acmsl.queryj.tools.customsql.ResultSetFlagsRefElement;
import org.acmsl.queryj.tools.customsql.StatementFlagsElement;
import org.acmsl.queryj.tools.customsql.StatementFlagsRefElement;
import org.acmsl.queryj.tools.customsql.SqlElement;
import org.acmsl.queryj.tools.customsql.xml.ConnectionFlagsElementFactory;
import org.acmsl.queryj.tools.customsql.xml.ParameterElementFactory;
import org.acmsl.queryj.tools.customsql.xml.ParameterRefElementFactory;
import org.acmsl.queryj.tools.customsql.xml.PropertyElementFactory;
import org.acmsl.queryj.tools.customsql.xml.PropertyRefElementFactory;
import org.acmsl.queryj.tools.customsql.xml.ResultElementFactory;
import org.acmsl.queryj.tools.customsql.xml.ResultRefElementFactory;
import org.acmsl.queryj.tools.customsql.xml.ResultSetFlagsElementFactory;
import org.acmsl.queryj.tools.customsql.xml.SqlElementFactory;
import org.acmsl.queryj.tools.customsql.xml.StatementFlagsElementFactory;

/*
 * Importing some JDK classes.
 */
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/*
 * Importing Digester classes.
 */
import org.apache.commons.digester.Digester;

/*
 * Importing Jakarta Commons Logging classes
 */
import org.apache.commons.logging.LogFactory;

/**
 * Is able to read the contents contained in QueryJ's sql.xml files.
 * @author <a href="mailto:jsanleandro@yahoo.es"
           >Jose San Leandro</a>
 * @version $Revision$
 */
public abstract class SqlXmlParser
    implements  CustomSqlProvider
{
    /**
     * The complete sql.xml contents.
     */
    private Collection m__cSqlXmlContents;

    /**
     * The key-value pairs.
     */
    private Map m__mSqlXmlContents;

    /**
     * The input stream.
     */
    private InputStream m__isInput;

    /**
     * The class loader.
     */
    private ClassLoader m__ClassLoader;

    /**
     * Creates a SqlXmlParser with given input stream.
     * @param input the input stream.
     * @precondition input != null
     */
    public SqlXmlParser(final InputStream input)
    {
        immutableSetInput(input);
    }

    /**
     * Specifies the map with sql.xml contents.
     * @param map the new map.
     */
    protected void setMap(final Map map)
    {
        m__mSqlXmlContents = map;
    }

    /**
     * Retrieves the map with sql.xml contents.
     * return such map.
     */
    public Map getMap()
    {
        return m__mSqlXmlContents;
    }

    /**
     * Specifies the sql.xml element collection.
     * @param collection the new collection.
     */
    protected void setCollection(final Collection collection)
    {
        m__cSqlXmlContents = collection;
    }

    /**
     * Retrieves the sql.xml element collection.
     * return such collection.
     */
    public Collection getCollection()
    {
        return m__cSqlXmlContents;
    }

    /**
     * Specifies the class loader (to give it to Digester).
     * @param classLoader the class loader.
     */
    public void setClassLoader(final ClassLoader classLoader)
    {
        m__ClassLoader = classLoader;
    }

    /**
     * Retrieves the class loader.
     * @return such instance.
     */
    public ClassLoader getClassLoader()
    {
        return m__ClassLoader;
    }

    /**
     * Specifies the input stream.
     * @param input the input stream.
     */
    private void immutableSetInput(final InputStream input)
    {
        m__isInput = input;
    }

    /**
     * Specifies the input stream.
     * @param input the input stream.
     */
    protected void setInput(final InputStream input)
    {
        immutableSetInput(input);
    }

    /**
     * Retrieves the input stream.
     * @return such stream.
     */
    protected InputStream getInput()
    {
        return m__isInput;
    }

    /**
     * Loads the information from the XML resource.
     * @return the Customer information.
     */
    protected Map load()
    {
        return load(configureDigester(getClassLoader()), getInput());
    }

    /**
     * Loads the information from the XML resource.
     * @param digester the Digester instance.
     * @param stream the input stream.
     * @return the Customer information.
     * @precondition (stream != null)
     */
    protected synchronized Map load(
        final Digester digester,
        final InputStream input)
    {
        Map result = null;

        if  (digester != null)
        {
            Collection collection = new ArrayList();

            digester.push(collection);

            try
            {
                if  (input != null)
                {
                    digester.parse(input);

                    result = new Hashtable();
                    processSqlXml(collection, result);
                    setCollection(collection);
                    setMap(result);
                }
            }
            catch (final Exception exception)
            {
                LogFactory.getLog(getClass()).error(
                    "Cannot read sql.xml information.",
                    exception);
            }
        }

        return result;
    }

    /**
     * Creates and configures a digester instance.
     * @param classLoader <i>optional</i> the class loader.
     * @return a configured digester instance.
     */
    protected Digester configureDigester(final ClassLoader classLoader)
    {
        Digester result = new Digester();

        if  (classLoader != null)
        {
            result.setClassLoader(classLoader);
        }

        // <sql-list>

        //   <sql>
        result.addFactoryCreate(
            "sql-list/sql",
            new SqlElementFactory());

        //     <value>
        result.addRule(
            "sql-list/sql/value",
            new UntrimmedCallMethodRule("setValue", 0));
        //     </value>

        //     <parameter-ref>
        result.addFactoryCreate(
            "sql-list/sql/parameter-ref",
            new ParameterRefElementFactory());
        result.addSetNext("sql-list/sql/parameter-ref", "add");
        //     </parameter-ref>

        //     <result-ref>
        result.addFactoryCreate(
            "sql-list/sql/result-ref",
            new ResultRefElementFactory());
        result.addSetNext(
            "sql-list/sql/result-ref", "setResultRef");
        //     </result-ref>

        //     <connection-flags-ref>
        result.addFactoryCreate(
            "sql-list/sql/connection-flags-ref",
            new ConnectionFlagsRefElementFactory());
        result.addSetNext(
            "sql-list/sql/connection-flags-ref", "setConnectionFlagsRef");
        //     </connection-flags-ref>

        //     <statement-flags-ref>
        result.addFactoryCreate(
            "sql-list/sql/statement-flags-ref",
            new StatementFlagsRefElementFactory());
        result.addSetNext(
            "sql-list/sql/statement-flags-ref", "setStatementFlagsRef");
        //     </statement-flags-ref>

        //     <resultset-flags-ref>
        result.addFactoryCreate(
            "sql-list/sql/resultset-flags-ref",
            new ResultSetFlagsRefElementFactory());
        result.addSetNext(
            "sql-list/sql/resultset-flags-ref", "setResultSetFlagsRef");
        //     </resultset-flags-ref>

        result.addSetNext("sql-list/sql", "add");

        //   </sql>

        //   <parameter-list>

        /*
        result.addObjectCreate(
            "sql-list/parameter-list",
            "java.util.ArrayList");
        */

        //     <parameter>
        result.addFactoryCreate(
            "sql-list/parameter-list/parameter",
            new ParameterElementFactory());
        result.addSetRoot(
            "sql-list/parameter-list/parameter", "add");
        //     </parameter>
        //   </parameter-list>

        //   <result-list>
        //     <result>
        result.addFactoryCreate(
            "sql-list/result-list/result",
            new ResultElementFactory());
        result.addSetRoot("sql-list/result-list/result", "add");

        //       <property-ref>
        result.addFactoryCreate(
            "sql-list/result-list/result/property-ref",
            new PropertyRefElementFactory());
        result.addSetNext(
            "sql-list/result-list/result/property-ref", "add");
        //       </property-ref>
        //     </result>
        //   </result-list>

        //   <property-list>
        //     <property>
        result.addFactoryCreate(
            "sql-list/property-list/property",
            new PropertyElementFactory());
        result.addSetRoot(
            "sql-list/property-list/property", "add");
        //     </property>
        //   </property-list>

        //   <flag-list>
        //     <connection-flags>
        result.addFactoryCreate(
            "sql-list/flag-list/connection-flags",
            new ConnectionFlagsElementFactory());
        result.addSetRoot(
            "sql-list/flag-list/connection-flags", "add");
        //     </connection-flags>
        //     <statement-flags>
        result.addFactoryCreate(
            "sql-list/flag-list/statement-flags",
            new StatementFlagsElementFactory());
        result.addSetRoot(
            "sql-list/flag-list/statement-flags", "add");
        //     </statement-flags>
        //     <resultset-flags>
        result.addFactoryCreate(
            "sql-list/flag-list/resultset-flags",
            new ResultSetFlagsElementFactory());
        result.addSetRoot(
            "sql-list/flag-list/resultset-flags", "add");
        //     </resultset-flags>
        //   </flag-list>

        // <sql-list>

        return result;
    }

    /**
     * Processes given sql.xml information.
     * @param collection the sql.xml element collection.
     * @param map the sql.xml map.
     * @precondition collection != null
     * @precondition map != null
     */
    protected synchronized void processSqlXml(
        final Collection collection, final Map map)
    {
        Iterator t_Iterator = collection.iterator();

        if  (t_Iterator != null)
        {
            while  (t_Iterator.hasNext())
            {
                Object t_Object = t_Iterator.next();

                if  (t_Object != null)
                {
                    if  (t_Object instanceof SqlElement)
                    {
                        map.put(
                            buildSqlKey(t_Object), t_Object);
                    }
                    else if  (t_Object instanceof ParameterElement)
                    {
                        map.put(
                            buildParameterKey(
                                (AbstractIdElement) t_Object),
                            t_Object);
                    }
                    else if  (t_Object instanceof ResultElement)
                    {
                        map.put(
                            buildResultKey(
                                (AbstractIdElement) t_Object),
                            t_Object);
                    }
                    else if  (t_Object instanceof PropertyElement)
                    {
                        map.put(
                            buildPropertyKey(
                                (AbstractIdElement) t_Object),
                            t_Object);
                    }
                    else if  (t_Object instanceof ConnectionFlagsElement)
                    {
                        map.put(
                            buildConnectionFlagsKey(
                                (AbstractIdElement) t_Object),
                            t_Object);
                    }
                    else if  (t_Object instanceof StatementFlagsElement)
                    {
                        map.put(
                            buildStatementFlagsKey(
                                (AbstractIdElement) t_Object),
                            t_Object);
                    }
                    else if  (t_Object instanceof ResultSetFlagsElement)
                    {
                        map.put(
                            buildResultSetFlagsKey(
                                (AbstractIdElement) t_Object),
                            t_Object);
                    }
                }
            }
        }
    }

    /**
     * Parses the sql.xml associated to this instance.
     */
    public void parse()
    {
        Map t_mSqlXmlMap = getMap();

        if  (t_mSqlXmlMap == null)
        {
            t_mSqlXmlMap = load();
        }
    }

    /**
     * Builds a key for &lt;sql&gt; elements.
     * @param sqlElement such element.
     * @return the key.
     */
    protected Object buildSqlKey(final Object sqlElement)
    {
        return "\\/sql\\::" + sqlElement;
    }

    /**
     * Builds a key for &lt;parameter&gt; elements.
     * @param parameterElement such element.
     * @return the key.
     */
    protected Object buildParameterKey(final AbstractIdElement parameterElement)
    {
        return buildParameterKey(parameterElement.getId());
    }

    /**
     * Builds a key for &lt;parameter&gt; elements, via their id.
     * @param parameterElementId such element id.
     * @return the key.
     */
    protected Object buildParameterKey(final String parameterElementId)
    {
        return "\\/parameter\\::" + parameterElementId;
    }

    /**
     * Builds a key for &lt;result&gt; elements.
     * @param resultElement such element.
     * @return the key.
     */
    protected Object buildResultKey(final AbstractIdElement resultElement)
    {
        return buildResultKey(resultElement.getId());
    }

    /**
     * Builds a key for &lt;result&gt; elements, via their id.
     * @param resultElementId such element id.
     * @return the key.
     */
    protected Object buildResultKey(final String resultElementId)
    {
        return "\\/result\\::" + resultElementId;
    }

    /**
     * Builds a key for &lt;property&gt; elements.
     * @param propertyElement such element.
     * @return the key.
     */
    protected Object buildPropertyKey(final AbstractIdElement propertyElement)
    {
        return buildPropertyKey(propertyElement.getId());
    }

    /**
     * Builds a key for &lt;property&gt; elements, via their id.
     * @param propertyElementId such element id.
     * @return the key.
     */
    protected Object buildPropertyKey(final String propertyElementId)
    {
        return "\\/property\\::" + propertyElementId;
    }

    /**
     * Builds a key for &lt;connection-flags&gt; elements.
     * @param connectionFlagsElement such element.
     * @return the key.
     */
    protected Object buildConnectionFlagsKey(
        final AbstractIdElement connectionFlagsElement)
    {
        return buildConnectionFlagsKey(connectionFlagsElement.getId());
    }

    /**
     * Builds a key for &lt;connection-flags&gt; elements, via their id.
     * @param connectionFlagsElementId such element id.
     * @return the key.
     */
    protected Object buildConnectionFlagsKey(final String connectionFlagsElementId)
    {
        return "\\/connection|flags\\::" + connectionFlagsElementId;
    }

    /**
     * Builds a key for &lt;statement-flags&gt; elements.
     * @param statementFlagsElement such element.
     * @return the key.
     */
    protected Object buildStatementFlagsKey(
        final AbstractIdElement statementFlagsElement)
    {
        return buildStatementFlagsKey(statementFlagsElement.getId());
    }

    /**
     * Builds a key for &lt;statement-flags&gt; elements, via their id.
     * @param statementFlagsElementId such element id.
     * @return the key.
     */
    protected Object buildStatementFlagsKey(final String statementFlagsElementId)
    {
        return "\\/statement|flags\\::" + statementFlagsElementId;
    }

    /**
     * Builds a key for &lt;resultset-flags&gt; elements.
     * @param resultSetFlagsElement such element.
     * @return the key.
     */
    protected Object buildResultSetFlagsKey(
        final AbstractIdElement resultSetFlagsElement)
    {
        return buildResultSetFlagsKey(resultSetFlagsElement.getId());
    }

    /**
     * Builds a key for &lt;resultset-flags&gt; elements, via their id.
     * @param resultSetFlagsElementId such element id.
     * @return the key.
     */
    protected Object buildResultSetFlagsKey(final String resultSetFlagsElementId)
    {
        return "\\/resultset|flags\\::" + resultSetFlagsElementId;
    }

    /**
     * Resolves the parameter reference.
     * @param reference such reference.
     * @return the referenced parameter.
     * @precondition reference != null
     */
    public ParameterElement resolveReference(
        final ParameterRefElement reference)
    {
        return resolveReference(reference, getMap());
    }

    /**
     * Resolves the parameter reference.
     * @param reference such reference.
     * @param map the map.
     * @return the referenced parameter.
     * @precondition reference != null
     */
    protected ParameterElement resolveReference(
        final ParameterRefElement reference,
        final Map map)
    {
        ParameterElement result = null;

        if  (map != null)
        {
            result =
                (ParameterElement)
                    map.get(buildParameterKey(reference.getId()));
        }

        return result;
    }

    /**
     * Resolves the result reference.
     * @param reference such reference.
     * @return the referenced result.
     * @precondition reference != null
     */
    public ResultElement resolveReference(
        final ResultRefElement reference)
    {
        return resolveReference(reference, getMap());
    }

    /**
     * Resolves the result reference.
     * @param reference such reference.
     * @param map the map.
     * @return the referenced result.
     * @precondition reference != null
     */
    protected ResultElement resolveReference(
        final ResultRefElement reference,
        final Map map)
    {
        ResultElement result = null;

        if  (map != null)
        {
            result =
                (ResultElement)
                    map.get(buildResultKey(reference.getId()));
        }

        return result;
    }

    /**
     * Resolves the property reference.
     * @param reference such reference.
     * @return the referenced property.
     * @precondition reference != null
     */
    public PropertyElement resolveReference(
        final PropertyRefElement reference)
    {
        return resolveReference(reference, getMap());
    }

    /**
     * Resolves the property reference.
     * @param reference such reference.
     * @param map the map.
     * @return the referenced property.
     * @precondition reference != null
     */
    protected PropertyElement resolveReference(
        final PropertyRefElement reference,
        final Map map)
    {
        PropertyElement result = null;

        if  (map != null)
        {
            result =
                (PropertyElement)
                    map.get(buildPropertyKey(reference.getId()));
        }

        return result;
    }

    /**
     * Resolves the connection-flags reference.
     * @param reference such reference.
     * @return the referenced connection flags.
     * @precondition reference != null
     */
    public ConnectionFlagsElement resolveReference(
        final ConnectionFlagsRefElement reference)
    {
        return resolveReference(reference, getMap());
    }

    /**
     * Resolves given connection-flags reference.
     * @param reference such reference.
     * @param map the map.
     * @return the referenced connection flags.
     * @precondition reference != null
     */
    protected ConnectionFlagsElement resolveReference(
        final ConnectionFlagsRefElement reference,
        final Map map)
    {
        ConnectionFlagsElement result = null;

        if  (map != null)
        {
            result =
                (ConnectionFlagsElement)
                    map.get(buildConnectionFlagsKey(reference.getId()));
        }

        return result;
    }

    /**
     * Resolves the statement-flags reference.
     * @param reference such reference.
     * @return the referenced statement flags.
     * @precondition reference != null
     */
    public StatementFlagsElement resolveReference(
        final StatementFlagsRefElement reference)
    {
        return resolveReference(reference, getMap());
    }

    /**
     * Resolves given statement-flags reference.
     * @param reference such reference.
     * @param map the map.
     * @return the referenced statement flags.
     * @precondition reference != null
     */
    protected StatementFlagsElement resolveReference(
        final StatementFlagsRefElement reference,
        final Map map)
    {
        StatementFlagsElement result = null;

        if  (map != null)
        {
            result =
                (StatementFlagsElement)
                    map.get(buildStatementFlagsKey(reference.getId()));
        }

        return result;
    }

    /**
     * Resolves the resultset-flags reference.
     * @param reference such reference.
     * @return the referenced resultset flags.
     * @precondition reference != null
     */
    public ResultSetFlagsElement resolveReference(
        final ResultSetFlagsRefElement reference)
    {
        return resolveReference(reference, getMap());
    }

    /**
     * Resolves given resultset-flags reference.
     * @param reference such reference.
     * @param map the map.
     * @return the referenced resultset flags.
     * @precondition reference != null
     */
    protected ResultSetFlagsElement resolveReference(
        final ResultSetFlagsRefElement reference,
        final Map map)
    {
        ResultSetFlagsElement result = null;

        if  (map != null)
        {
            result =
                (ResultSetFlagsElement)
                    map.get(buildResultSetFlagsKey(reference.getId()));
        }

        return result;
    }
}
