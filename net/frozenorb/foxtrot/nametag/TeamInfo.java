package net.frozenorb.foxtrot.nametag;

import lombok.*;

public class TeamInfo
{
    @NonNull
    private String name;
    @NonNull
    private String prefix;
    @NonNull
    private String suffix;
    
    public TeamInfo(@NonNull final String name, @NonNull final String prefix, @NonNull final String suffix) {
        super();
        if (name == null) {
            throw new NullPointerException("name");
        }
        if (prefix == null) {
            throw new NullPointerException("prefix");
        }
        if (suffix == null) {
            throw new NullPointerException("suffix");
        }
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
    }
    
    @NonNull
    public String getName() {
        return this.name;
    }
    
    @NonNull
    public String getPrefix() {
        return this.prefix;
    }
    
    @NonNull
    public String getSuffix() {
        return this.suffix;
    }
    
    public void setName(@NonNull final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        this.name = name;
    }
    
    public void setPrefix(@NonNull final String prefix) {
        if (prefix == null) {
            throw new NullPointerException("prefix");
        }
        this.prefix = prefix;
    }
    
    public void setSuffix(@NonNull final String suffix) {
        if (suffix == null) {
            throw new NullPointerException("suffix");
        }
        this.suffix = suffix;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TeamInfo)) {
            return false;
        }
        final TeamInfo other = (TeamInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0065: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0065;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$prefix = this.getPrefix();
        final Object other$prefix = other.getPrefix();
        Label_0102: {
            if (this$prefix == null) {
                if (other$prefix == null) {
                    break Label_0102;
                }
            }
            else if (this$prefix.equals(other$prefix)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$suffix = this.getSuffix();
        final Object other$suffix = other.getSuffix();
        if (this$suffix == null) {
            if (other$suffix == null) {
                return true;
            }
        }
        else if (this$suffix.equals(other$suffix)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof TeamInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 0 : $name.hashCode());
        final Object $prefix = this.getPrefix();
        result = result * 59 + (($prefix == null) ? 0 : $prefix.hashCode());
        final Object $suffix = this.getSuffix();
        result = result * 59 + (($suffix == null) ? 0 : $suffix.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "TeamInfo(name=" + this.getName() + ", prefix=" + this.getPrefix() + ", suffix=" + this.getSuffix() + ")";
    }
}
