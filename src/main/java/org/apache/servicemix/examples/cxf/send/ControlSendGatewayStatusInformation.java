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

		// if storaged - Indicates that the server is aware of the existence of
		// the gateway - ignore send information
		if (ControlSendGatewayInformation.storaged) {

			if (gatewayStatus == null) {
				gatewayStatus = new GatewayStatus();
				
				gatewayStatus.setMac(gatewayStatusInfo.getMac());
				gatewayStatus.setDate(gatewayStatusInfo.getDate());
				gatewayStatus.setBaterryLevel(gatewayStatusInfo.getBaterryLevel());
				gatewayStatus.setFreeMemory(gatewayStatusInfo.getFreeMemory());
				gatewayStatus.setFreeProcessor(gatewayStatusInfo.getFreeProcessor());
				gatewayStatus.setTotalMemory(gatewayStatusInfo.getTotalMemory());
				gatewayStatus.setUsedMemory(gatewayStatusInfo.getUsedMemory());
				gatewayStatus.setUsedProcessor(gatewayStatusInfo.getUsedProcessor());

				gatewayStatusService.sendGatewayStatus(gatewayStatus);
			} else {

				// returns the gateway status information
				gatewayStatusCompare.setMac(gatewayStatusInfo.getMac());
				gatewayStatusCompare.setDate(gatewayStatusInfo.getDate());
				gatewayStatusCompare.setBaterryLevel(gatewayStatusInfo.getBaterryLevel());
				gatewayStatusCompare.setFreeMemory(gatewayStatusInfo.getFreeMemory());
				gatewayStatusCompare.setFreeProcessor(gatewayStatusInfo.getFreeProcessor());
				gatewayStatusCompare.setTotalMemory(gatewayStatusInfo.getTotalMemory());
				gatewayStatusCompare.setUsedMemory(gatewayStatusInfo.getUsedMemory());
				gatewayStatusCompare.setUsedProcessor(gatewayStatusInfo.getUsedProcessor());

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

				if (modify == true) {
					gatewaySend.setDate(gatewayStatusCompare.getDate());
					gatewaySend.setMac(gatewayStatusCompare.getMac());
					gatewayStatusService.sendGatewayStatus(gatewaySend);
				}
			}
		}
	}
}
