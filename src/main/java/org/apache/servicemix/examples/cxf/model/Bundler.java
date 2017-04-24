package org.apache.servicemix.examples.cxf.model;

//@XmlRootElement(name = "Bundler")
public class Bundler {

	// private String state;
	private String name;
	private String version;
	private String location;

	// public String getState() {
	// return state;
	// }
	//
	// public void setState(String state) {
	// this.state = state;
	// }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


}
