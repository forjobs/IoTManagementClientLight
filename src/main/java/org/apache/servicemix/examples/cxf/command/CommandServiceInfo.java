package org.apache.servicemix.examples.cxf.command;

import org.apache.servicemix.examples.cxf.info.ServiceInfo;

//@Command(scope = "ws", name = "gatewayServiceInformation")
public class CommandServiceInfo  {

	// referenciar via CDI ou blueprint
	ServiceInfo serviceInfo = new ServiceInfo();

	public void execute() {
		serviceInfo.listNames();
	}

}
