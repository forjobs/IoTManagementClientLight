package org.apache.servicemix.examples.cxf.send;

import org.apache.servicemix.examples.cxf.info.GatewayStatusInfo;
import org.apache.servicemix.examples.cxf.model.GatewayStatus;
import org.apache.servicemix.examples.cxf.service.GatewayStatusService;

public class ControlSendGatewayStatusInformation {

	private static GatewayStatus gatewayStatus;
	private GatewayStatusService gatewayStatusService;
	private GatewayStatusInfo gatewayStatusInfo;

	public ControlSendGatewayStatusInformation() {
		gatewayStatusService = new GatewayStatusService();
		gatewayStatusInfo = new GatewayStatusInfo();
	}

	// information about gateway status
	public void compareInfoGatewayStatus() {

		GatewayStatus gatewaySend;

		GatewayStatus gatewayStatusCompare = new GatewayStatus();

		if (gatewayStatus == null) {
			gatewayStatus = new GatewayStatus();
			
			gatewayStatus.setBaterryLevel(gatewayStatusInfo.getBaterryLevel());
			gatewayStatus.setFreeMemory(gatewayStatusInfo.getFreeMemory());
			gatewayStatus.setFreeProcessor(gatewayStatusInfo.getFreeProcessor());
			gatewayStatus.setLastUpdate(gatewayStatusInfo.getLastUpdate());
			gatewayStatus.setMac(gatewayStatusInfo.getMac());
			gatewayStatus.setTotalMemory(gatewayStatusInfo.getTotalMemory());
			gatewayStatus.setUsedMemory(gatewayStatusInfo.getUsedMemory());
			gatewayStatus.setUsedProcessor(gatewayStatusInfo.getUsedProcessor());
			gatewayStatus.setLastUpdate(gatewayStatusInfo.getLastUpdate());
			
			gatewayStatusService.sendGatewayStatus(gatewayStatus);
		} else {
			
			// returns the gateway status information
			gatewayStatusCompare.setBaterryLevel(gatewayStatusInfo.getBaterryLevel());
			gatewayStatusCompare.setFreeMemory(gatewayStatusInfo.getFreeMemory());
			gatewayStatusCompare.setFreeProcessor(gatewayStatusInfo.getFreeProcessor());
			gatewayStatusCompare.setLastUpdate(gatewayStatusInfo.getLastUpdate());
			gatewayStatusCompare.setMac(gatewayStatusInfo.getMac());
			gatewayStatusCompare.setTotalMemory(gatewayStatusInfo.getTotalMemory());
			gatewayStatusCompare.setUsedMemory(gatewayStatusInfo.getUsedMemory());
			gatewayStatusCompare.setUsedProcessor(gatewayStatusInfo.getUsedProcessor());
			gatewayStatusCompare.setLastUpdate(gatewayStatusInfo.getLastUpdate());
			
			// responsible for storing information to be sent
			gatewaySend = new GatewayStatus();

			Boolean modify = false;
			
			if (!(gatewayStatus.getBaterryLevel() == gatewayStatusCompare.getBaterryLevel())) {
				gatewaySend.setBaterryLevel(gatewayStatusCompare.getBaterryLevel());
				gatewayStatus.setBaterryLevel(gatewayStatusCompare.getBaterryLevel());
				modify = true;
			}

			if (!(gatewayStatus.getTotalMemory() == gatewayStatusCompare.getTotalMemory())) {
				gatewaySend.setTotalMemory(gatewayStatusCompare.getTotalMemory());
				gatewayStatus.setTotalMemory(gatewayStatusCompare.getTotalMemory());
				modify = true;
			}

			if (!(gatewayStatus.getUsedMemory() == gatewayStatusCompare.getUsedMemory())) {
				gatewaySend.setUsedMemory(gatewayStatusCompare.getUsedMemory());
				gatewayStatus.setUsedMemory(gatewayStatusCompare.getUsedMemory());
				modify = true;
			}

			if (!(gatewayStatus.getFreeMemory() == gatewayStatusCompare.getFreeMemory())) {
				gatewaySend.setFreeMemory(gatewayStatusCompare.getFreeMemory());
				gatewayStatus.setFreeMemory(gatewayStatusCompare.getFreeMemory());
				modify = true;
			}

			if (!(gatewayStatus.getUsedProcessor() == gatewayStatusCompare.getUsedProcessor())) {
				gatewaySend.setUsedProcessor(gatewayStatusCompare.getUsedProcessor());
				gatewayStatus.setUsedProcessor(gatewayStatusCompare.getUsedProcessor());
				modify = true;
			}

			if (!(gatewayStatus.getFreeProcessor() == gatewayStatusCompare.getFreeProcessor())) {
				gatewaySend.setFreeProcessor(gatewayStatusCompare.getFreeProcessor());
				gatewayStatus.setFreeProcessor(gatewayStatusCompare.getFreeProcessor());
				modify = true;
			}
			
			if(modify == true) {
				gatewaySend.setLastUpdate(gatewayStatusCompare.getLastUpdate());
				gatewaySend.setMac(gatewayStatusCompare.getMac());
				gatewayStatusService.sendGatewayStatus(gatewaySend);
			}
		}
	}

}
