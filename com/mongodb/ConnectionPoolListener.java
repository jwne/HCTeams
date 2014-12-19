package com.mongodb;

import java.util.*;

interface ConnectionPoolListener extends EventListener
{
    void connectionPoolOpened(ConnectionPoolOpenedEvent p0);
    
    void connectionPoolClosed(ConnectionPoolEvent p0);
    
    void connectionCheckedOut(ConnectionEvent p0);
    
    void connectionCheckedIn(ConnectionEvent p0);
    
    void waitQueueEntered(ConnectionPoolWaitQueueEvent p0);
    
    void waitQueueExited(ConnectionPoolWaitQueueEvent p0);
    
    void connectionAdded(ConnectionEvent p0);
    
    void connectionRemoved(ConnectionEvent p0);
}
