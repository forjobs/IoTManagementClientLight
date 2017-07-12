package org.apache.servicemix.examples.cxf.send;

import java.util.ArrayList;
import java.util.List;

import org.apache.servicemix.examples.cxf.info.BundlerInfo;
import org.apache.servicemix.examples.cxf.info.GatewayInfo;
import org.apache.servicemix.examples.cxf.model.Bundler;
import org.apache.servicemix.examples.cxf.model.Gateway;
import org.apache.servicemix.examples.cxf.service.BundlerService;

/**
 * Class responsible for identifying bundles and changes in their information
 * and requesting them to be updated on the server.
 *
 * @author Nilson Rodrigues Sousa
 */
public class ControlSendBundlerInformation {

	private static List<Bundler> listBundlers;
	private BundlerService bundlerService;
	private BundlerInfo bundlerInfo = null;

	public ControlSendBundlerInformation() {
		bundlerService = new BundlerService();
		listBundlers = new ArrayList<Bundler>();
	}

	// used by blueprint
	public void setBundlerInfo(BundlerInfo bundlerInfo) {
		this.bundlerInfo = bundlerInfo;
	}

	/**
	 * Method comparing information, detect bundles installed, deleted, or
	 * modified
	 * 
	 * @author Nilson Rodrigues Sousa
	 */
	public void compareInfoBundler() {
		if (ControlSendGatewayInformation.storaged == true) {

			List<Bundler> listCapt = new ArrayList<Bundler>();

			for (Bundler tr : bundlerInfo.getListBundler()) {
				Bundler b = new Bundler();

				b.setName(tr.getName());
				b.setLocation(tr.getLocation());
				b.setState(tr.getState());
				b.setVersion(tr.getVersion());

				listCapt.add(b);
			}

			if (listBundlers.isEmpty() || listBundlers == null) {
				listBundlers.addAll(listCapt);

				GatewayInfo gatewayInfo = new GatewayInfo();

				Gateway gatewaySend = new Gateway();
				gatewaySend.setMac(gatewayInfo.getMac());
				gatewaySend.setLastUpdate(gatewayInfo.getLastUpdate());
				gatewaySend.getListBundler().addAll(listBundlers);

				// sending information for the first time
				bundlerService.sendListBundlerConnected(gatewaySend);
			} else {
				List<Bundler> listBundlersIntance = listCapt;

				List<Bundler> listBundlersDisconnected = this.compareToBundles(listBundlers, listBundlersIntance);

				List<Bundler> listBundlersConnected = this.compareToBundles(listBundlersIntance, listBundlers);

				List<Bundler> listInformationAltered = this.compareToInformation(listBundlersIntance, listBundlers);

				if (!(listBundlersDisconnected.isEmpty() || listBundlersDisconnected == null)) {
					this.infoBundlerDisconnected(listBundlersDisconnected);
				}

				if (!(listBundlersConnected.isEmpty() || listBundlersConnected == null)) {
					this.infoBundlerConnected(listBundlersConnected);
				}

				if (!(listInformationAltered.isEmpty() || listInformationAltered == null)) {
					this.infoBundlerAltered(listInformationAltered);
				}

			}
		}

	}

	/**
	 * Method responsible for requesting deletion of disconnected bundles
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @param disconnected
	 *            List<Bundler> - List bundles disconnected
	 */
	public void infoBundlerDisconnected(List<Bundler> disconnected) {
		GatewayInfo gatewayInfo = new GatewayInfo();
		Gateway gatewaySend = new Gateway();

		// removing bundles from the local information list
		for (Bundler bundlerDisc : disconnected) {
			for (Bundler bundler : listBundlers) {
				if (bundlerDisc.getName().equals(bundler.getName())
						&& bundlerDisc.getVersion().equals(bundler.getVersion())) {
					listBundlers.remove(bundler);
					break;
				}
			}
		}

		// mounting information to be sent
		gatewaySend.setMac(gatewayInfo.getMac());
		gatewaySend.setLastUpdate(gatewayInfo.getLastUpdate());
		gatewaySend.getListBundler().addAll(disconnected);

		// sending information
		bundlerService.sendListBundlerDisconnected(gatewaySend);
	}

	/**
	 * Method responsible for requesting inclusion of connected bundles
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @param connected
	 *            List<Bundler> - List bundles connected
	 */
	public void infoBundlerConnected(List<Bundler> connected) {
		GatewayInfo gatewayInfo = new GatewayInfo();
		Gateway gatewaySend = new Gateway();

		// update information locale
		listBundlers.addAll(connected);

		// mounting information to be sent
		gatewaySend.setMac(gatewayInfo.getMac());
		gatewaySend.setLastUpdate(gatewayInfo.getLastUpdate());
		gatewaySend.getListBundler().addAll(connected);

		// sending information
		bundlerService.sendListBundlerConnected(gatewaySend);
	}

	/**
	 * Method responsible for requesting modification of changed bundles
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @param altered
	 *            List<Bundler> - List bundles altered
	 */
	public void infoBundlerAltered(List<Bundler> altered) {
		GatewayInfo gatewayInfo = new GatewayInfo();
		Gateway gatewaySend = new Gateway();

		// update information locale
		for (Bundler bundlerAlter : altered) {
			for (Bundler bundler : listBundlers) {
				if (bundlerAlter.getName().equals(bundler.getName())
						&& bundlerAlter.getVersion().equals(bundler.getVersion())) {
					bundler.setName(bundlerAlter.getName());
					bundler.setVersion(bundlerAlter.getVersion());
					bundler.setLocation(bundlerAlter.getLocation());
					bundler.setState(bundlerAlter.getState());

					break;
				}
			}
		}

		// mounting information to be sent
		gatewaySend.setMac(gatewayInfo.getMac());
		gatewaySend.setLastUpdate(gatewayInfo.getLastUpdate());
		gatewaySend.getListBundler().addAll(altered);

		// sending information
		bundlerService.sendListBundlerAltered(gatewaySend);

	}

	/**
	 * Auxiliary method for comparing two list of bundles. Used to find new
	 * bundles and disconnected bundles
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @param l1
	 *            List<Bundler>, l2 List<Bundler>
	 * @return List<Bundler> - Returns elements of the first list that does not
	 *         exist in the second
	 */
	public List<Bundler> compareToBundles(List<Bundler> l1, List<Bundler> l2) {
		List<Bundler> result = new ArrayList<Bundler>();
		boolean exist;

		for (Bundler b1 : l1) {
			exist = false;
			for (Bundler b2 : l2) {
				if ((b1.getName().equals(b2.getName())) && (b1.getVersion().equals(b2.getVersion()))) {
					exist = true;
					break;
				}
			}
			if (exist == false) {
				result.add(b1);
			}
		}
		return result;
	}

	/**
	 * Auxiliary method for comparing information from two list of bundles
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @param l1
	 *            List<Bundler>, l2 List<Bundler>
	 * @return List<Bundler> - Returns bundles with changed information
	 */
	public List<Bundler> compareToInformation(List<Bundler> l1, List<Bundler> l2) {
		List<Bundler> result = new ArrayList<Bundler>();
		boolean exist;

		for (Bundler b1 : l1) {
			exist = true;
			for (Bundler b2 : l2) {
				if ((b1.getName().equals(b2.getName())) && (b1.getVersion().equals(b2.getVersion()))) {
					exist = false;
					if (b1.getLocation().equals(b2.getLocation()) && b1.getState().equals(b2.getState())) {
						exist = true;
					}
					break;
				}
			}
			// ALTER - within the conditional
			if (exist == false) {
				result.add(b1);
			}
		}

		return result;
	}

}
