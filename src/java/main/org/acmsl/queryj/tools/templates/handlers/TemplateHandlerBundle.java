/*
                        QueryJ

    Copyright (C) 2002  Jose San Leandro Armendáriz
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
    Contact info: jsanleandro@yahoo.es
    Postal Address: c/Playa de Lagoa, 1
                    Urb. Valdecabañas
                    Boadilla del monte
                    28660 Madrid
                    Spain

 ******************************************************************************
 *
 * Filename: $RCSfile$
 *
 * Author: Jose San Leandro Armendáriz
 *
 * Description: Bundles a pair of template build and writing handlers.
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
package org.acmsl.queryj.tools.templates.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.AntCommand;
import org.acmsl.queryj.tools.handlers.AntCommandHandler;
import org.acmsl.queryj.tools.handlers.CompositeAntCommandHandler;
import org.acmsl.queryj.tools.templates.handlers.TemplateBuildHandler;
import org.acmsl.queryj.tools.templates.handlers.TemplateHandler;
import org.acmsl.queryj.tools.templates.handlers.TemplateWritingHandler;

/*
 * Importing some Ant classes.
 */
import org.apache.tools.ant.BuildException;

/**
 * Bundles a pair of template build and writing handlers.
 * @author <a href="mailto:jsanleandro@yahoo.es">Jose San Leandro</a>
 * @version $Revision$ at $Date$
 */
public class TemplateHandlerBundle
    extends    CompositeAntCommandHandler
    implements TemplateHandler
{
    /**
     * Builds a bundle with given handlers.
     * @param buildHandler the template build handler.
     * @param writingHandler the writing handler.
     * @precondition buildHandler != null
     * @precondition writingHandler != null
     */
    public TemplateHandlerBundle(
        final TemplateBuildHandler buildHandler,
        final TemplateWritingHandler writingHandler)
    {
        immutableAddHandler(buildHandler);
        immutableAddHandler(writingHandler);
    }

    /**
     * Builds a bundle with given ones.
     * @param bundles the first bundle.
     * @precondition bundles != null
     */
    public TemplateHandlerBundle(final TemplateHandlerBundle[] bundles)
    {
        for  (int t_iIndex = 0; t_iIndex < bundles.length; t_iIndex++)
        {
            immutableAddHandler(bundles[t_iIndex]);
        }
    }

    /**
     * Builds a bundle with given two.
     * @param firstBundle the first bundle.
     * @param secondBundle the second bundle, typically
     * related to test templates associated to the first one's.
     * @precondition firstBundle != null
     * @precondition secondBundle != null
     */
    public TemplateHandlerBundle(
        final TemplateHandlerBundle firstBundle,
        final TemplateHandlerBundle secondBundle)
    {
        this(new TemplateHandlerBundle[] { firstBundle, secondBundle });
    }

    /**
     * Adds a new handler to the collection.
     * @param handler the new handler to add.
     */
    private void immutableAddHandler(final TemplateHandler handler)
    {
        super.addHandler(handler);
    }
}
