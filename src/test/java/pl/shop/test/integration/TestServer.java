package pl.shop.test.integration;

import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;

public enum TestServer
{
    INSTANCE;

    private final TJWSEmbeddedJaxrsServer server;
    
    TestServer()
    {
        server = new TJWSEmbeddedJaxrsServer();
    }

    public static TestServer getInstance()
    {
        return INSTANCE;
    }

    public TJWSEmbeddedJaxrsServer startServerOnPort(int port)
    {
        server.setPort(port);
        server.start();
        server.setRootResourcePath("/");
    	return server;
    }
    
}
