package org.webcat.deveventtracker;

import com.webobjects.appserver.WOContext;

public class RetrieveStudentProjectResponse extends XmlResponsePage {
    public RetrieveStudentProjectResponse(WOContext context) {
        super(context);
    }
    
    public String uuid;
    public String pushLogs;
}