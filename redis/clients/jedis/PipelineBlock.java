package redis.clients.jedis;

@Deprecated
public abstract class PipelineBlock extends Pipeline
{
    public abstract void execute();
}
