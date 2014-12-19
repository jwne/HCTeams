package com.mongodb;

import java.util.*;

interface ServerSelector
{
    List<ServerDescription> choose(ClusterDescription p0);
}
