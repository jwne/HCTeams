package redis.clients.jedis;

import redis.clients.jedis.exceptions.*;

public class Response<T>
{
    protected T response;
    private boolean built;
    private boolean set;
    private Builder<T> builder;
    private Object data;
    private Response<?> dependency;
    private boolean requestDependencyBuild;
    
    public Response(final Builder<T> b) {
        super();
        this.response = null;
        this.built = false;
        this.set = false;
        this.dependency = null;
        this.requestDependencyBuild = false;
        this.builder = b;
    }
    
    public void set(final Object data) {
        this.data = data;
        this.set = true;
    }
    
    public T get() {
        if (!this.requestDependencyBuild && this.dependency != null && this.dependency.set && !this.dependency.built) {
            this.requestDependencyBuild = true;
            this.dependency.build();
        }
        if (!this.set) {
            throw new JedisDataException("Please close pipeline or multi block before calling this method.");
        }
        if (!this.built) {
            this.build();
        }
        return this.response;
    }
    
    public void setDependency(final Response<?> dependency) {
        this.dependency = dependency;
        this.requestDependencyBuild = false;
    }
    
    private void build() {
        if (this.data != null) {
            if (this.data instanceof JedisDataException) {
                throw new JedisDataException((Throwable)this.data);
            }
            this.response = this.builder.build(this.data);
        }
        this.data = null;
        this.built = true;
    }
    
    @Override
    public String toString() {
        return "Response " + this.builder.toString();
    }
}
