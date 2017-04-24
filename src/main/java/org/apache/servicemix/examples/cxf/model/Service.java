package org.apache.servicemix.examples.cxf.model;

import java.util.ArrayList;
import java.util.List;

// Class that reflects the serviceIoT
//@XmlRootElement(name = "Service")
public class Service {

	private long id;
	private String nameService;
	private String bundlerProvide;
	private List<String> listUsesBundles = new ArrayList<String>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNameService() {
		return nameService;
	}

	public void setNameService(String nameService) {
		this.nameService = nameService;
	}

	public String getBundlerProvide() {
		return bundlerProvide;
	}

	public void setBundlerProvide(String bundlerProvide) {
		this.bundlerProvide = bundlerProvide;
	}

	public List<String> getListUsesBundles() {
		return listUsesBundles;
	}

	public void setListUsesBundles(List<String> listUsesBundles) {
		this.listUsesBundles = listUsesBundles;
	}

}
