package org.apache.servicemix.examples.cxf.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.karaf.bundle.core.BundleInfo;
import org.apache.karaf.bundle.core.BundleService;
import org.apache.servicemix.examples.cxf.model.Bundler;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class BundlerInfo {

	BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

	BundleService bundleService;

	String context = "0";

	List<String> ids;

	boolean defaultAllBundles = true;

	public void setBundleService(BundleService bundleService) {
		this.bundleService = bundleService;
	}

	List<Bundle> listBundle;

	protected static List<Bundler> listBundler = new ArrayList<Bundler>();

	public void listBundles() {

		Bundler bundler;
		
		listBundler = new ArrayList<Bundler>();
		
		int cont = 0;
		
		listBundle = bundleService.selectBundles(context, ids, defaultAllBundles);

		for (Bundle bundle : listBundle) {
			bundler = new Bundler();
			BundleInfo info = this.bundleService.getInfo(bundle);

			if (info.getStartLevel() >= -1) {

				bundler.setName(info.getName());
				bundler.setVersion(info.getVersion());
				//bundler.setState(info.getState().toString());
				bundler.setLocation(info.getUpdateLocation());

			}

			listBundler.add(bundler);
			
			Random gerador = new Random();
			
			cont++;
			
			//modificar para retornar apenas os serviços que não são de infra-estrutura
			if(cont > gerador.nextInt(11)) {
				break;
			}

		}
		System.out.println(">>> Lista atualizada.");
		
	}

	public void viewBundles() {

		Bundler bundler;
		
		listBundle = bundleService.selectBundles(context, ids, defaultAllBundles);

		for (Bundle bundle : listBundle) {
			bundler = new Bundler();
			
			BundleInfo info = this.bundleService.getInfo(bundle);

			if (info.getStartLevel() >= -1) {

				bundler.setName(info.getName());
				bundler.setVersion(info.getVersion());
				//bundler.setState(info.getState().toString());
				bundler.setLocation(info.getUpdateLocation());

				System.out.println(">>>> " + info.getName());
				System.out.println(">>>> " + info.getVersion());
				System.out.println(">>>> " + info.getState().toString());
				System.out.println(">>>> " + info.getUpdateLocation());
				System.out.println("##################################");

			}
			
		}

	}

}
