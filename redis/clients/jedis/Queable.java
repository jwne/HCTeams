package redis.clients.jedis;

import java.util.*;

public class Queable
{
    private Queue<Response<?>> pipelinedResponses;
    
    public Queable() {
        super();
        this.pipelinedResponses = new LinkedList<Response<?>>();
    }
    
    protected void clean() {
        this.pipelinedResponses.clear();
    }
    
    protected Response<?> generateResponse(final Object data) {
        final Response<?> response = this.pipelinedResponses.poll();
        if (response != null) {
            response.set(data);
        }
        return response;
    }
    
    protected <T> Response<T> getResponse(final Builder<T> builder) {
        final Response<T> lr = new Response<T>(builder);
        this.pipelinedResponses.add(lr);
        return lr;
    }
    
    protected boolean hasPipelinedResponse() {
        return this.pipelinedResponses.size() > 0;
    }
    
    protected int getPipelinedResponseLength() {
        return this.pipelinedResponses.size();
    }
}
