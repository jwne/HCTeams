package com.mongodb;

import java.util.concurrent.*;
import java.util.*;

class LatencyMinimizingServerSelector implements ServerSelector
{
    private final long acceptableLatencyDifferenceNanos;
    
    LatencyMinimizingServerSelector(final long acceptableLatencyDifference, final TimeUnit timeUnit) {
        super();
        this.acceptableLatencyDifferenceNanos = TimeUnit.NANOSECONDS.convert(acceptableLatencyDifference, timeUnit);
    }
    
    public List<ServerDescription> choose(final ClusterDescription clusterDescription) {
        return this.getServersWithAcceptableLatencyDifference(clusterDescription.getAll(), this.getBestLatencyNanos(clusterDescription.getAll()));
    }
    
    public String toString() {
        return "LatencyMinimizingServerSelector{acceptableLatencyDifference=" + TimeUnit.MILLISECONDS.convert(this.acceptableLatencyDifferenceNanos, TimeUnit.NANOSECONDS) + " ms" + '}';
    }
    
    private long getBestLatencyNanos(final Set<ServerDescription> members) {
        long bestPingTime = Long.MAX_VALUE;
        for (final ServerDescription cur : members) {
            if (!cur.isOk()) {
                continue;
            }
            if (cur.getAverageLatencyNanos() >= bestPingTime) {
                continue;
            }
            bestPingTime = cur.getAverageLatencyNanos();
        }
        return bestPingTime;
    }
    
    private List<ServerDescription> getServersWithAcceptableLatencyDifference(final Set<ServerDescription> servers, final long bestPingTime) {
        final List<ServerDescription> goodSecondaries = new ArrayList<ServerDescription>(servers.size());
        for (final ServerDescription cur : servers) {
            if (!cur.isOk()) {
                continue;
            }
            if (cur.getAverageLatencyNanos() - this.acceptableLatencyDifferenceNanos > bestPingTime) {
                continue;
            }
            goodSecondaries.add(cur);
        }
        return goodSecondaries;
    }
}
