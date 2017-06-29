package org.apache.servicemix.examples.cxf.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GatewayStatus")
public class GatewayStatus {

	private String baterryLevel;
	private String totalMemory;
	private String usedMemory;
	private String freeMemory;
	private String usedProcessor;
	private String freeProcessor;

	private String lastUpdate;

	private String mac;

	public String getBaterryLevel() {
		return baterryLevel;
	}

	public void setBaterryLevel(String baterryLevel) {
		this.baterryLevel = baterryLevel;
	}

	public String getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(String totalMemory) {
		this.totalMemory = totalMemory;
	}

	public String getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(String usedMemory) {
		this.usedMemory = usedMemory;
	}

	public String getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(String freeMemory) {
		this.freeMemory = freeMemory;
	}

	public String getUsedProcessor() {
		return usedProcessor;
	}

	public void setUsedProcessor(String usedProcessor) {
		this.usedProcessor = usedProcessor;
	}

	public String getFreeProcessor() {
		return freeProcessor;
	}

	public void setFreeProcessor(String freeProcessor) {
		this.freeProcessor = freeProcessor;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

}
