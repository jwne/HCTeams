package com.mongodb;

import java.io.*;

public class MongoSocketException extends MongoException
{
    private static final long serialVersionUID = -4415279469780082174L;
    
    MongoSocketException(final String msg, final IOException ioe) {
        super(-2, msg, ioe);
    }
    
    MongoSocketException(final IOException ioe) {
        super(ioe.toString(), ioe);
    }
}
