package com.mongodb;

import org.bson.util.annotations.*;
import org.bson.util.*;
import java.util.*;

@Immutable
class ClusterDescription
{
    private final ClusterConnectionMode connectionMode;
    private final ClusterType type;
    private final Set<ServerDescription> all;
    
    public ClusterDescription(final ClusterConnectionMode connectionMode, final ClusterType type, final List<ServerDescription> serverDescriptions) {
        super();
        Assertions.notNull("all", serverDescriptions);
        this.connectionMode = Assertions.notNull("connectionMode", connectionMode);
        this.type = Assertions.notNull("type", type);
        final Set<ServerDescription> serverDescriptionSet = new TreeSet<ServerDescription>(new Comparator<ServerDescription>() {
            public int compare(final ServerDescription o1, final ServerDescription o2) {
                return o1.getAddress().toString().compareTo(o2.getAddress().toString());
            }
        });
        serverDescriptionSet.addAll(serverDescriptions);
        this.all = Collections.unmodifiableSet((Set<? extends ServerDescription>)serverDescriptionSet);
    }
    
    public boolean isCompatibleWithDriver() {
        for (final ServerDescription cur : this.all) {
            if (!cur.isCompatibleWithDriver()) {
                return false;
            }
        }
        return true;
    }
    
    public ClusterConnectionMode getConnectionMode() {
        return this.connectionMode;
    }
    
    public ClusterType getType() {
        return this.type;
    }
    
    public Set<ServerDescription> getAll() {
        return this.all;
    }
    
    public ServerDescription getByServerAddress(final ServerAddress serverAddress) {
        for (final ServerDescription cur : this.getAll()) {
            if (cur.getAddress().equals(serverAddress)) {
                return cur;
            }
        }
        return null;
    }
    
    public List<ServerDescription> getPrimaries() {
        return this.getServersByPredicate(new Predicate() {
            public boolean apply(final ServerDescription serverDescription) {
                return serverDescription.isPrimary();
            }
        });
    }
    
    public List<ServerDescription> getSecondaries() {
        return this.getServersByPredicate(new Predicate() {
            public boolean apply(final ServerDescription serverDescription) {
                return serverDescription.isSecondary();
            }
        });
    }
    
    public List<ServerDescription> getSecondaries(final TagSet tags) {
        return this.getServersByPredicate(new Predicate() {
            public boolean apply(final ServerDescription serverDescription) {
                return serverDescription.isSecondary() && serverDescription.hasTags(tags);
            }
        });
    }
    
    public List<ServerDescription> getAny() {
        return this.getServersByPredicate(new Predicate() {
            public boolean apply(final ServerDescription serverDescription) {
                return serverDescription.isOk();
            }
        });
    }
    
    public List<ServerDescription> getAnyPrimaryOrSecondary() {
        return this.getServersByPredicate(new Predicate() {
            public boolean apply(final ServerDescription serverDescription) {
                return serverDescription.isPrimary() || serverDescription.isSecondary();
            }
        });
    }
    
    public List<ServerDescription> getAnyPrimaryOrSecondary(final TagSet tags) {
        return this.getServersByPredicate(new Predicate() {
            public boolean apply(final ServerDescription serverDescription) {
                return (serverDescription.isPrimary() || serverDescription.isSecondary()) && serverDescription.hasTags(tags);
            }
        });
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ClusterDescription that = (ClusterDescription)o;
        return this.all.equals(that.all) && this.connectionMode == that.connectionMode;
    }
    
    public int hashCode() {
        int result = this.all.hashCode();
        result = 31 * result + this.connectionMode.hashCode();
        return result;
    }
    
    public String toString() {
        return "ClusterDescription{type=" + this.getType() + ", connectionMode=" + this.connectionMode + ", all=" + this.all + '}';
    }
    
    public String getShortDescription() {
        final StringBuilder serverDescriptions = new StringBuilder();
        String delimiter = "";
        for (final ServerDescription cur : this.all) {
            serverDescriptions.append(delimiter).append(cur.getShortDescription());
            delimiter = ", ";
        }
        return String.format("{type=%s, servers=[%s]", this.type, serverDescriptions);
    }
    
    private List<ServerDescription> getServersByPredicate(final Predicate predicate) {
        final List<ServerDescription> membersByTag = new ArrayList<ServerDescription>();
        for (final ServerDescription cur : this.all) {
            if (predicate.apply(cur)) {
                membersByTag.add(cur);
            }
        }
        return membersByTag;
    }
    
    private interface Predicate
    {
        boolean apply(ServerDescription p0);
    }
}
