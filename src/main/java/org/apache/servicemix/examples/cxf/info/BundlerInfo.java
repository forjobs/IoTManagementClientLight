package org.apache.servicemix.examples.cxf.info;

import java.util.ArrayList;
import java.util.List;

import org.apache.karaf.bundle.core.BundleInfo;
import org.apache.karaf.bundle.core.BundleService;
import org.apache.servicemix.examples.cxf.interfaces.BundlerInfoInterface;
import org.apache.servicemix.examples.cxf.model.Bundler;
import org.osgi.framework.Bundle;

/**
 * Class responsible detect gateway and update database with your ip, mac and
 * status
 *
 * @author Nilson Rodrigues Sousa
 */
public class BundlerInfo implements BundlerInfoInterface{

	BundleService bundleService;

	String context = "0";

	List<String> ids;

	boolean defaultAllBundles = true;

	List<Bundle> listBundle;

	protected static List<Bundler> listBundler = new ArrayList<Bundler>();
	
	// used by blueprint
	public void setBundleService(BundleService bundleService) {
		this.bundleService = bundleService;
	}
	
	/**
	 * Method returns information about the bundles
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @return List<Bundler> - Bundles existing gateway
	 */
	public List<Bundler> getListBundler() {
		try {
			Bundler bundler;

			listBundler = new ArrayList<Bundler>();
			
			listBundle = bundleService.selectBundles(context, ids, defaultAllBundles);
			
			for (Bundle bundle : listBundle) {
				bundler = new Bundler();
				BundleInfo info = this.bundleService.getInfo(bundle);

				if (info.getStartLevel() >= -1) {

					bundler.setName(info.getName());
					bundler.setVersion(info.getVersion());
					bundler.setState(info.getState().toString());
					bundler.setLocation(info.getUpdateLocation());

				}
				
				listBundler.add(bundler);		
			}
			
			return listBundler;
		} catch (Exception e) {
			System.out.println("Failure in capture information bundler.");
			return null;
		}
		
	}
	
}
