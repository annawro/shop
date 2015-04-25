package pl.shop.base;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import pl.shop.shop.Shop;
import pl.shop.warehouse.WarehouseService;

@ApplicationPath("")
public class BaseApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {

		return Collections.emptySet();
	}
	
	
	private Set<Object> singletons = new HashSet<Object>();
    public BaseApplication() {
        singletons.add(new WarehouseService());
        singletons.add(new Shop());
        }
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
	
}
