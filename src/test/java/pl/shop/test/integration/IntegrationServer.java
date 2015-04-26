package pl.shop.test.integration;

import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;



public abstract class IntegrationServer implements ApplicationContextAware {

	protected ApplicationContext applicationContext;
	private static int port = 12345;
	
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.applicationContext = arg0;
	}
	
	
	protected String getHost() {
		return "http://localhost:";
	}
	
	protected abstract String getService();
	
	protected String getUrl() {
		return getHost()+port+getService();
	}

	protected static TJWSEmbeddedJaxrsServer initServer() {
		 TJWSEmbeddedJaxrsServer server = new TJWSEmbeddedJaxrsServer();
		 server.setPort(++port);
	     server.start();
	     server.setRootResourcePath("/");
	     return server;
	}


}
