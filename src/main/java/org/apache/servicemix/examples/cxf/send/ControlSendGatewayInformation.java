package org.apache.servicemix.examples.cxf.send;

import org.apache.servicemix.examples.cxf.info.GatewayInfo;
import org.apache.servicemix.examples.cxf.model.Gateway;
import org.apache.servicemix.examples.cxf.service.GatewayService;

/**
 * Class responsible for verifying changes to gateway cadastral information and
 * requesting submission.
 *
 * @author Nilson Rodrigues Sousa
 */
public class ControlSendGatewayInformation {

	private static Gateway gateway;
	private GatewayService gatewayService;
	private GatewayInfo gatewayInfo;
	private static boolean storaged = false;

	public ControlSendGatewayInformation() {
		gatewayService = new GatewayService();
		gatewayInfo = new GatewayInfo();
	}

	/**
	 * Method that compares the information and requests the sending of the
	 * modified information to the server.
	 * 
	 * @author Nilson Rodrigues Sousa
	 */
	public void compareInfoGateway() {
		Gateway gatewaySend;

		Gateway gatewayCompare = new Gateway();

		if (gateway == null) {
			gateway = new Gateway();

			gateway.setDescription(gatewayInfo.getDescription());
			gateway.setFirmware(gatewayInfo.getFirmware());
			gateway.setHostName(gatewayInfo.getHostName());
			gateway.setIp(gatewayInfo.getIp());
			gateway.setLocation(gatewayInfo.getLocation());
			gateway.setMac(gatewayInfo.getMac());
			gateway.setManufacturer(gatewayInfo.getManufacturer());
			gateway.setModel(gatewayInfo.getModel());
			gateway.setStorage(gatewayInfo.getStorage());
			gateway.setLastUpdate(gatewayInfo.getLastUpdate());
			gateway.setListBundler(null);
			gateway.setListService(null);

			gatewayService.sendGatewayAdd(gateway);
		} else {
			// returns the gateway information
			gatewayCompare.setDescription(gatewayInfo.getDescription());
			gatewayCompare.setFirmware(gatewayInfo.getFirmware());
			gatewayCompare.setHostName(gatewayInfo.getHostName());
			gatewayCompare.setIp(gatewayInfo.getIp());
			gatewayCompare.setLocation(gatewayInfo.getLocation());
			gatewayCompare.setMac(gatewayInfo.getMac());
			gatewayCompare.setManufacturer(gatewayInfo.getManufacturer());
			gatewayCompare.setModel(gatewayInfo.getModel());
			gatewayCompare.setStorage(gatewayInfo.getStorage());
			gatewayCompare.setLastUpdate(gatewayInfo.getLastUpdate());

			// responsible for storing information to be sent
			gatewaySend = new Gateway();

			Boolean modify = false;

			if (!gateway.getDescription().equals(gatewayCompare.getDescription())) {
				gatewaySend.setDescription(gatewayCompare.getDescription());
				gateway.setDescription(gatewayCompare.getDescription());
				modify = true;
			}

			if (!gateway.getModel().equals(gatewayCompare.getModel())) {
				gatewaySend.setModel(gatewayCompare.getModel());
				gateway.setModel(gatewayCompare.getModel());
				modify = true;
			}

			if (!gateway.getManufacturer().equals(gatewayCompare.getManufacturer())) {
				gatewaySend.setManufacturer(gatewayCompare.getManufacturer());
				gateway.setManufacturer(gatewayCompare.getManufacturer());
				modify = true;
			}

			if (!gateway.getFirmware().equals(gatewayCompare.getFirmware())) {
				gatewaySend.setFirmware(gatewayCompare.getFirmware());
				gateway.setFirmware(gatewayCompare.getFirmware());
				modify = true;
			}

			if (!(gateway.getStorage() == gatewayCompare.getStorage())) {
				gatewaySend.setStorage(gatewayCompare.getStorage());
				gateway.setStorage(gatewayCompare.getStorage());
				modify = true;
			}

			if (!gateway.getMac().equals(gatewayCompare.getMac())) {
				gatewaySend.setMac(gatewayCompare.getMac());
				gateway.setMac(gatewayCompare.getMac());
				modify = true;
			}

			if (!gateway.getIp().equals(gatewayCompare.getIp())) {
				gatewaySend.setIp(gatewayCompare.getIp());
				gateway.setIp(gatewayCompare.getIp());
				modify = true;
			}

			if (!gateway.getHostName().equals(gatewayCompare.getHostName())) {
				gatewaySend.setHostName(gatewayCompare.getHostName());
				gateway.setHostName(gatewayCompare.getHostName());
				modify = true;
			}

			if (!gateway.getLocation().equals(gatewayCompare.getLocation())) {
				gatewaySend.setLocation(gatewayCompare.getLocation());
				gateway.setLocation(gatewayCompare.getLocation());
				modify = true;
			}

			// conditional to send information
			if (modify == true) {
				gatewaySend.setLastUpdate(gatewayCompare.getLastUpdate());
				gatewaySend.setMac(gatewayCompare.getMac());
				gatewaySend.setListBundler(null);
				gatewaySend.setListService(null);
				gatewayService.sendGatewayInformationAltered(gatewaySend);
			}
		}
	}

}
