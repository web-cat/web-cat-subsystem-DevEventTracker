/*==========================================================================*\
 |  $Id$
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2014 Virginia Tech
 |
 |  This file is part of Web-CAT.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU Affero General Public License as published
 |  by the Free Software Foundation; either version 3 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU Affero General Public License
 |  along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/


package org.webcat.deveventtracker;

import com.webobjects.appserver.WORequest;
import er.extensions.appserver.ERXDirectAction;

//-------------------------------------------------------------------------
/**
 * This direct action class handles all response actions for this
 * subsystem.
 *
 * @author  edwards
 * @author  Last changed by $Author$
 * @version $Revision$, $Date$
 */
public class event
    extends ERXDirectAction
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new DirectAction object.
     *
     * @param aRequest The request to respond to
     */
    public event(WORequest aRequest)
    {
        super(aRequest);
    }


    //~ Methods ...............................................................
}
