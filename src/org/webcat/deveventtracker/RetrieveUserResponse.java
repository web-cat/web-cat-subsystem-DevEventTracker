package org.webcat.deveventtracker;

import com.webobjects.appserver.WOContext;

public class RetrieveUserResponse extends XmlResponsePage {
    public RetrieveUserResponse(WOContext context) {
        super(context);
    }
    
    public String uuid;
}