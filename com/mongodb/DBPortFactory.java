package com.mongodb;

class DBPortFactory implements ConnectionFactory
{
    private final MongoOptions options;
    
    DBPortFactory(final MongoOptions options) {
        super();
        this.options = options;
    }
    
    public Connection create(final ServerAddress serverAddress, final PooledConnectionProvider provider, final int generation) {
        return new DBPort(serverAddress, provider, this.options, generation);
    }
}
