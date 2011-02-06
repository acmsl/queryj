//;-*- mode: java -*-
/*
                        QueryJ

    Copyright (C) 2002-today  Jose San Leandro Armendariz
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
    Contact info: jose.sanleandro@acm-sl.com

 ******************************************************************************
 *
 * Filename: CompositeQueryJCommandHandler.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: General-purpose AntCommandHandler composed of an arbitrary
 *              number of handlers, to which the logic is delegated to,
 *              following GoF's Composite pattern.
 *
 */
package org.acmsl.queryj.tools.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.QueryJBuildException;
import org.acmsl.queryj.tools.QueryJCommand;
import org.acmsl.queryj.tools.handlers.AbstractQueryJCommandHandler;

/*
 * Importing some ACM-SL Commons classes.
 */
import org.acmsl.commons.patterns.Command;
import org.acmsl.commons.logging.UniqueLogFactory;

/*
 * Importing some JDK classes.
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/*
 * Importing some Apache Commons Logging classes.
 */
import org.apache.commons.logging.Log;

/**
 * General-purpose AntCommandHandler composed of an arbitrary number
 * of handlers, to which the logic is delegated to, following GoF's
 * <b>Composite pattern</b>.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class CompositeQueryJCommandHandler
    implements  QueryJCommandHandler
{
    /**
     * The handler collection.
     */
    private Collection m__cHandlers;

    /**
     * Builds a <code>CompositeQueryJCommandHandler</code> instance.
     */
    public CompositeQueryJCommandHandler()
    {
        immutableSetHandlerCollection(new ArrayList());
    }

    /**
     * Specifies the handler collection.
     * @param collection such collection.
     */
    private void immutableSetHandlerCollection(
        final Collection collection)
    {
        m__cHandlers = collection;
    }

    /**
     * Specifies the handler collection.
     * @param collection such collection.
     */
    protected void setHandlerCollection(
        final Collection collection)
    {
        immutableSetHandlerCollection(collection);
    }

    /**
     * Retrieves the template build handler.
     * @return such handler.
     */
    protected Collection getHandlerCollection()
    {
        return m__cHandlers;
    }

    /**
     * Adds a new handler to the collection.
     * @param handler the new handler to add.
     */
    protected void addHandler(final QueryJCommandHandler handler)
    {
        addHandler(handler, getHandlerCollection());
    }

    /**
     * Adds a new handler to the collection.
     * @param handler the new handler to add.
     * @param collection the handler collection.
     */
    protected void addHandler(
        final QueryJCommandHandler handler, final Collection handlerCollection)
    {
        if  (   (handler != null)
             && (handlerCollection != null))
        {
            handlerCollection.add(handler);
        }
    }

    /**
     * Handles given command.
     * @param command the command to handle.
     * @return <code>true</code> if the chain should be stopped.
     * @precondition command != null
     */
    public boolean handle(final Command command)
    {
        boolean result = false;

        if  (command instanceof QueryJCommand)
        {
            QueryJCommand t_Command = (QueryJCommand) command;
            
            try 
            {
                result = handle(t_Command);
            }
            catch  (final QueryJBuildException buildException)
            {
                Log t_Log =
                    UniqueLogFactory.getLog(
                        CompositeQueryJCommandHandler.class);

                if  (t_Log != null)
                {
                    t_Log.error("Chain step failed.", buildException);
                }
            }
        }
        
        return result;
    }

    /**
     * Handles given command.
     * @param command the command.
     * @return <code>true</code> to avoid further processing of such command
     * by different handlers.
     * @throws QueryJBuildException if the build process cannot be performed.
     */
    public boolean handle(final QueryJCommand command)
        throws  QueryJBuildException
    {
        return handle(command, getHandlerCollection());
    }

    /**
     * Handles given command.
     * @param command the command.
     * @param handlerCollection the handler collection.
     * @return <code>true</code> to avoid further processing of such command
     * by different handlers.
     * @throws QueryJBuildException if the build process cannot be performed.
     */
    protected boolean handle(
        final QueryJCommand command, final Collection handlerCollection)
      throws  QueryJBuildException
    {
        boolean result = false;

        Iterator t_itHandlerIterator =
            (handlerCollection != null)
            ?  handlerCollection.iterator()
            :  null;

        if  (t_itHandlerIterator != null)
        {
            while  (t_itHandlerIterator.hasNext())
            {
                Object t_Handler = t_itHandlerIterator.next();

                if  (t_Handler instanceof QueryJCommandHandler)
                {
                    result =
                        handle(command, (QueryJCommandHandler) t_Handler);
                }

                if  (result)
                {
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Handles given command.
     * @param command the command.
     * @param handler the concrete handler.
     * @return <code>true</code> to avoid further processing of such command
     * by different handlers.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition handler != null
     */
    protected boolean handle(
        final QueryJCommand command, final QueryJCommandHandler handler)
      throws  QueryJBuildException
    {
        return handler.handle(command);
    }
}