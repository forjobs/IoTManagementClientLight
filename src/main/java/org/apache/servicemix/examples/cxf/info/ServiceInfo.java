package org.apache.servicemix.examples.cxf.info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.apache.karaf.shell.support.ShellUtil;
import org.apache.servicemix.examples.cxf.model.Service;
import org.apache.servicemix.examples.cxf.util.ObjectClassMatcher;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class ServiceInfo {

	// @Argument(index = 0, name = "objectClass", description = "Name of service
	// objectClass to filter for", required = false, multiValued = false)
	// @Completion(ObjectClassCompleter.class)
	String objectClass;

	boolean showAll;

	BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

	protected static List<Service> listService = new ArrayList<Service>();

	public void execute() throws Exception {

		List<ServiceReference<?>> serviceRefs = new ArrayList<ServiceReference<?>>();
		Bundle[] bundles = bundleContext.getBundles();
		for (Bundle bundle : bundles) {
			ServiceReference<?>[] services = bundle.getRegisteredServices();
			if (services != null) {
				for (ServiceReference<?> ref : services) {
					String[] objectClasses = (String[]) ref.getProperty(Constants.OBJECTCLASS);
					if (objectClass == null || ObjectClassMatcher.matchesAtLeastOneName(objectClasses, objectClass)) {
						serviceRefs.add(ref);
					}
				}
			}
		}

		Collections.sort(serviceRefs, new ServiceClassComparator());
		
		int cont = 0;
		
		for (ServiceReference<?> serviceRef : serviceRefs) {
			
			
			Random gerador = new Random();
			
			if(cont > gerador.nextInt(11))
				break;
			
			cont++;
			
			if (showAll || !isCommand((String[]) serviceRef.getProperty(Constants.OBJECTCLASS))) {

				Service service = new Service();

				String[] objectClass = (String[]) serviceRef.getProperty(Constants.OBJECTCLASS);

				// name service
				service.setNameService(ShellUtil.getValueString(objectClass));

				// service.id
				for (String key : serviceRef.getPropertyKeys()) {
					if (!Constants.OBJECTCLASS.equals(key)) {
						if (key.equals("service.id")) {
							service.setId(Long.parseLong(ShellUtil.getValueString(serviceRef.getProperty(key))));
						}
					}
				}
				// service.bundleProvide
				service.setBundlerProvide(formatOutput(ShellUtil.getBundleName(serviceRef.getBundle())));

				// service.listUsesBundles
				Bundle[] usingBundles = serviceRef.getUsingBundles();
				if (usingBundles != null) {
					for (Bundle bundle : usingBundles) {
						service.getListUsesBundles().add(formatOutput(ShellUtil.getBundleName(bundle)));
					}
				}
				listService.add(service);
			}
		}

		printCustom();

	}

	private void printCustom() {

		for (Service service : listService) {
			System.out.println("\n#######################################\n");

			System.out.println("Service.id: " + service.getId());
			System.out.println("Service.name: " + service.getNameService());
			System.out.println("Service.bundlerProvide: " + service.getBundlerProvide());
			System.out.println("Service.usedBy: ");

			for (String userBy : service.getListUsesBundles()) {
				System.out.println(" " + userBy);
			}
		}

	}

	private String formatOutput(String string) {		
		String stringFormated = "";
		
		for(int i = 0; i < string.length(); i++) {
			if(string.charAt(i) == '(') {
				break;
			}
			stringFormated += string.charAt(i);
		}
		stringFormated = stringFormated.substring(0, stringFormated.length() - 1);
		return stringFormated;
	}

	private boolean isCommand(String[] objectClasses) {
		for (String objectClass : objectClasses) {
			if (objectClass.equals("org.apache.felix.service.command.Function")) {
				return true;
			}
		}
		return false;
	}

	public final class ServiceClassComparator implements Comparator<ServiceReference<?>> {
		@Override
		public int compare(ServiceReference<?> o1, ServiceReference<?> o2) {
			String class1 = getObjectClass(o1);
			String class2 = getObjectClass(o2);
			return class1.compareTo(class2);
		}

		private String getObjectClass(ServiceReference<?> o1) {
			Object value = o1.getProperty(Constants.OBJECTCLASS);
			if (value == null || !(value instanceof String[])) {
				return "";
			}
			String[] values = (String[]) value;
			return values.length == 0 ? "" : values[0];
		}
	}

}
