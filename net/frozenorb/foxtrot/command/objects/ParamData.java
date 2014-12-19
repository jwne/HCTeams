package net.frozenorb.foxtrot.command.objects;

import net.frozenorb.foxtrot.command.annotations.*;

public class ParamData
{
    private String name;
    private boolean wildcard;
    private String defaultValue;
    private Class<?> parameterClass;
    
    public ParamData(final Class<?> parameterClass, final Param paramAnnotation) {
        super();
        this.name = paramAnnotation.name();
        this.wildcard = paramAnnotation.wildcard();
        this.defaultValue = paramAnnotation.defaultValue();
        this.parameterClass = parameterClass;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isWildcard() {
        return this.wildcard;
    }
    
    public String getDefaultValue() {
        return this.defaultValue;
    }
    
    public Class<?> getParameterClass() {
        return this.parameterClass;
    }
}
