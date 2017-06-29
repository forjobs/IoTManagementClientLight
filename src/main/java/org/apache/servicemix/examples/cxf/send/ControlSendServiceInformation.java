package org.apache.servicemix.examples.cxf.send;

import java.util.ArrayList;
import java.util.List;

import org.apache.servicemix.examples.cxf.info.GatewayInfo;
import org.apache.servicemix.examples.cxf.info.ServiceInfo;
import org.apache.servicemix.examples.cxf.model.Bundler;
import org.apache.servicemix.examples.cxf.model.Gateway;
import org.apache.servicemix.examples.cxf.model.Service;
import org.apache.servicemix.examples.cxf.service.ServiceService;

/**
 * Class responsible for verifying changes to the service information at the
 * gateway and requesting submission of changes.
 *
 * @author Nilson Rodrigues Sousa
 */
public class ControlSendServiceInformation {

	private static List<Service> listServices;
	private ServiceService serviceService;
	private ServiceInfo serviceInfo = new ServiceInfo();

	public ControlSendServiceInformation() {
		serviceService = new ServiceService();
		listServices = new ArrayList<Service>();
	}

	// used by blueprint
	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	/**
	 * A method that compares service information and requests the submission of
	 * the modified information to the server.
	 * 
	 * @author Nilson Rodrigues Sousa
	 */
	// information about the services
	public void compareInfoService() {
		System.out.println("Entred in compareInfoService.<<<<<<<<<<<<<<<<<");
		List<Service> listCapt = new ArrayList<Service>();

		for (Service tr : serviceInfo.getListService()) {
			Service s = new Service();

			s.setNameService(tr.getNameService());
			s.setBundlerProvide(tr.getBundlerProvide());
			s.setListUsesBundles(tr.getListUsesBundles());

			listCapt.add(s);
		}

		if (listServices.isEmpty() || listServices == null) {

			for (Service tr : listCapt) {
				Service s = new Service();

				s.setNameService(tr.getNameService());
				s.setBundlerProvide(tr.getBundlerProvide());
				s.getListUsesBundles().addAll(tr.getListUsesBundles());

				listServices.add(s);
			}

			GatewayInfo gatewayInfo = new GatewayInfo();

			Gateway gatewaySend = new Gateway();
			gatewaySend.setMac(gatewayInfo.getMac());
			gatewaySend.setLastUpdate(gatewayInfo.getLastUpdate());
			gatewaySend.getListService().addAll(listServices);

			// sending information for the first time
			serviceService.sendServiceConnected(gatewaySend);

		} else {
			List<Service> listServicesIntance = new ArrayList<Service>();

			for (Service tr : listCapt) {
				Service s = new Service();

				s.setNameService(tr.getNameService());
				s.setBundlerProvide(tr.getBundlerProvide());
				s.getListUsesBundles().addAll(tr.getListUsesBundles());

				listServicesIntance.add(s);
			}

			// ------------------------------------------------------------------------------------------------------------------

			List<Service> listServicesDisconnected = this.compareToServices(listServices, listServicesIntance);

			List<Service> listServicesConnected = this.compareToServices(listServicesIntance, listServices);

			// Compare information service and bundlesUse

			List<Service> listServicesInformationConnectedBundlerUse = this
					.compareToServiceInformationBundlerConnected(listServicesIntance, listServices);

			List<Service> listServicesInformationDisconnectedBundlerUse = this
					.compareToServiceInformationBundlerDisconnected(listServices, listServicesIntance);

//			Responsibility for the ControlSendBundlerInformation class 
//			List<Service> listServicesInformationAlteredBundlerUse = this
//					.compareToServiceInformationBundlerAltered(listServices, listServicesIntance);

			// ------------------------------------------------------------------------------------------------------------------

			if (!(listServicesDisconnected.isEmpty() || listServicesDisconnected == null)) {
				System.out.println("\n###########################Existe elementos em listServicesDisconnected.\n");
				this.infoServiceDisconnected(listServicesDisconnected);
			}

			if (!(listServicesConnected.isEmpty() || listServicesConnected == null)) {
				System.out.println("\n###########################Existe elementos em listServicesConnected.\n");
				this.infoServiceConnected(listServicesConnected);
			}

			if (!(listServicesInformationConnectedBundlerUse.isEmpty()
					|| listServicesInformationConnectedBundlerUse == null)) {
				System.out.println(
						"###########################Existe elementos em infoServiceAlteredConnectedBundlerUse.");
				this.infoServiceAlteredConnectedBundlerUse(listServicesInformationConnectedBundlerUse);
			}

			if (!(listServicesInformationDisconnectedBundlerUse.isEmpty()
					|| listServicesInformationDisconnectedBundlerUse == null)) {
				System.out.println(
						"###########################Existe elementos em infoServiceAlteredDisconnectedBundlerUse.");
				this.infoServiceAlteredDisconnectedBundlerUse(listServicesInformationDisconnectedBundlerUse);
			}
			
//			Responsibility for the ControlSendBundlerInformation class 
//			if (!(listServicesInformationAlteredBundlerUse.isEmpty()
//					|| listServicesInformationAlteredBundlerUse == null)) {
//				System.out.println(
//						"###########################Existe elementos em infoServiceAlteredInformationAlteredBundlerUse.");
//				this.infoServiceAlteredInformationAlteredBundlerUse(listServicesInformationAlteredBundlerUse);
//			}

		}

	}

	// ------------------------------------------------------------------------------------------------------------------

	/**
	 * Method that receives a list of disconnected services, removes these
	 * elements from the local list, and sends the list of disconnected services
	 * to the server to update the database.
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @param serviceDisconnected
	 *            List<Service>
	 */
	// method for sending and updating disconnected service
	public void infoServiceDisconnected(List<Service> serviceDisconnected) {

		GatewayInfo gatewayInfo = new GatewayInfo();
		Gateway gatewaySend = new Gateway();

		// removing bundles from the local information list
		for (Service serviceDisc : serviceDisconnected) {
			for (Service service : listServices) {
				if (serviceDisc.getNameService().equals(service.getNameService())
						&& serviceDisc.getBundlerProvide().equals(service.getBundlerProvide())) {
					listServices.remove(service);
					break;
				}
			}
		}

		// mounting information to be sent
		gatewaySend.setMac(gatewayInfo.getMac());
		gatewaySend.setLastUpdate(gatewayInfo.getLastUpdate());

		for (Service sv : serviceDisconnected) {
			sv.getListUsesBundles().clear();
		}

		gatewaySend.getListService().addAll(serviceDisconnected);

		// sending information
		serviceService.sendServiceDisconnected(gatewaySend);

	}

	/**
	 * Method that receives a list of connected services, adds these elements to
	 * the local list, and sends the list of connected services to the server to
	 * update the database.
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @param serviceConnected
	 *            List<Service>
	 */
	public void infoServiceConnected(List<Service> serviceConnected) {

		GatewayInfo gatewayInfo = new GatewayInfo();
		Gateway gatewaySend = new Gateway();

		// update information locale
		listServices.addAll(serviceConnected);

		// mounting information to be sent
		gatewaySend.setMac(gatewayInfo.getMac());
		gatewaySend.setLastUpdate(gatewayInfo.getLastUpdate());
		gatewaySend.getListService().addAll(serviceConnected);

		// sending information
		serviceService.sendServiceConnected(gatewaySend);

	}

	/**
	 * A method that receives a list of services that have new bundles, adds
	 * those bundles to the appropriate services in the local list, and sends
	 * the list of services with new bundles to the server to update the
	 * database.
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @param listServiceBundlesConnected
	 *            List<Service>
	 */
	public void infoServiceAlteredConnectedBundlerUse(List<Service> listServiceBundlesConnected) {

		GatewayInfo gatewayInfo = new GatewayInfo();
		Gateway gatewaySend = new Gateway();

		// update information locale
		for (Service serviceAlter : listServiceBundlesConnected) {
			for (Service service : listServices) {
				if (serviceAlter.getNameService().equals(service.getNameService())
						&& serviceAlter.getBundlerProvide().equals(service.getBundlerProvide())) {
					service.setNameService(serviceAlter.getNameService());

					Bundler newBundler = new Bundler();

					newBundler.setName(serviceAlter.getBundlerProvide().getName());
					newBundler.setVersion(serviceAlter.getBundlerProvide().getVersion());
					newBundler.setLocation(serviceAlter.getBundlerProvide().getLocation());
					newBundler.setState(serviceAlter.getBundlerProvide().getState());

					service.setBundlerProvide(newBundler);

					List<Bundler> listBundlerTemp = new ArrayList<Bundler>();

					for (Bundler bg : serviceAlter.getListUsesBundles()) {
						newBundler = new Bundler();

						newBundler.setName(bg.getName());
						newBundler.setVersion(bg.getVersion());
						newBundler.setLocation(bg.getLocation());
						newBundler.setState(bg.getState());

						listBundlerTemp.add(newBundler);
					}

					service.getListUsesBundles().addAll(listBundlerTemp);
					break;
				}
			}
		}

		// mounting information to be sent
		gatewaySend.setMac(gatewayInfo.getMac());
		gatewaySend.setLastUpdate(gatewayInfo.getLastUpdate());
		gatewaySend.getListService().addAll(listServiceBundlesConnected);

		// sending information
		serviceService.sendServiceAlteredConnectedBundlerUse(gatewaySend);

	}

	/**
	 * Method that receives a list of services that have bundles removed,
	 * removes these bundles from the appropriate services in the local list,
	 * and sends the list of services with removed bundles to the server to
	 * update the database.
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @param listServiceBundlesDisconnected
	 *            List<Service>
	 */
	public void infoServiceAlteredDisconnectedBundlerUse(List<Service> listServiceBundlesDisconnected) {

		GatewayInfo gatewayInfo = new GatewayInfo();
		Gateway gatewaySend = new Gateway();

		// update information locale
		for (Service serviceAlter : listServiceBundlesDisconnected) {
			for (Service service : listServices) {
				if (serviceAlter.getNameService().equals(service.getNameService())
						&& serviceAlter.getBundlerProvide().equals(service.getBundlerProvide())) {

					List<Bundler> listBundlerAux1 = new ArrayList<Bundler>();
					listBundlerAux1.addAll(serviceAlter.getListUsesBundles());

					List<Bundler> listBundlerAux2 = new ArrayList<Bundler>();
					listBundlerAux2.addAll(service.getListUsesBundles());

					for (Bundler bundlerDisconnected : listBundlerAux1) {
						for (Bundler bundlerToRemove : listBundlerAux2) {
							if (bundlerDisconnected.getName().equals(bundlerToRemove.getName())
									&& bundlerDisconnected.getVersion().equals(bundlerToRemove.getVersion())) {
								service.getListUsesBundles().remove(bundlerToRemove);
								break;
							}
						}
					}
				}
			}
		}

		// mounting information to be sent
		gatewaySend.setMac(gatewayInfo.getMac());
		gatewaySend.setLastUpdate(gatewayInfo.getLastUpdate());
		gatewaySend.getListService().addAll(listServiceBundlesDisconnected);

		// sending information
		serviceService.sendServiceAlteredDisconnectedBundlerUse(gatewaySend);

	}

	/**
	 * A method that receives a list of services that have bundles changed,
	 * updates the bundle information in the appropriate services in the local
	 * list, and sends the list of services with edited bundles to the server to
	 * update the database.
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @param listServiceBundlesAltered
	 *            List<Service>
	 */
	public void infoServiceAlteredInformationAlteredBundlerUse(List<Service> listServiceBundlesAltered) {

		GatewayInfo gatewayInfo = new GatewayInfo();
		Gateway gatewaySend = new Gateway();

		// update information locale
		for (Service serviceAlter : listServiceBundlesAltered) {
			for (Service service : listServices) {
				if (serviceAlter.getNameService().equals(service.getNameService())
						&& serviceAlter.getBundlerProvide().equals(service.getBundlerProvide())) {

					for (Bundler bundlerModified : serviceAlter.getListUsesBundles()) {
						for (Bundler bundlerToUpdate : service.getListUsesBundles()) {

							if (bundlerModified.getName().equals(bundlerToUpdate.getName())
									&& bundlerModified.getVersion().equals(bundlerToUpdate.getVersion())) {
								bundlerToUpdate.setLocation(bundlerModified.getLocation());
								bundlerToUpdate.setState(bundlerModified.getState());
								break;
							}
						}
					}
				}
			}
		}

		// mounting information to be sent
		gatewaySend.setMac(gatewayInfo.getMac());
		gatewaySend.setLastUpdate(gatewayInfo.getLastUpdate());
		gatewaySend.getListService().addAll(listServiceBundlesAltered);

		// sending information
		serviceService.sendServiceAlteredInformationAlteredBundlerUse(gatewaySend);

	}

	// ----------------------------------------------- AUXILIARY METHODS

	/**
	 * Method responsible for returning services that exist in list1 and does
	 * not exist in list2
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @param s1
	 *            List<Service>, s2 List<Service>
	 * @return List<Service> -Returns the services that exist in l1 and do not
	 *         exist in l2
	 */
	public List<Service> compareToServices(List<Service> s1, List<Service> s2) {

		List<Service> result = new ArrayList<Service>();
		boolean exist;

		for (Service service1 : s1) {
			exist = false;
			for (Service service2 : s2) {
				if ((service1.getNameService().equals(service2.getNameService()))
						&& (service1.getBundlerProvide().equals(service2.getBundlerProvide()))) {
					exist = true;
					break;
				}
			}
			if (exist == false) {
				result.add(service1);
			}
		}

		return result;
	}

	// ------------------------------------------------------------------------------------------------------------------

	/**
	 * Returns services that have new bundles. Receiving as input a system list
	 * and the local list that has the persistent information from the last
	 * update.
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @param s1
	 *            List<Service>, s2 List<Service>
	 * @return List<Service> - Returns services that have new bundles
	 */
	public List<Service> compareToServiceInformationBundlerConnected(List<Service> s1, List<Service> s2) {
		// input - listSystem(Karaf) - listPersistent
		List<Service> result = new ArrayList<Service>();

		for (Service service1 : s1) {
			for (Service service2 : s2) {
				if ((service1.getNameService().equals(service2.getNameService()))
						&& (service2.getBundlerProvide().equals(service2.getBundlerProvide()))) {

					List<Bundler> listTempService1 = new ArrayList<Bundler>();
					listTempService1.addAll(service1.getListUsesBundles());

					List<Bundler> listTempService2 = new ArrayList<Bundler>();
					listTempService2.addAll(service2.getListUsesBundles());

					List<Bundler> listBundlerConnected = compareToServiceInformationCompareBundlers(listTempService1,
							listTempService2);

					if (!(listBundlerConnected.isEmpty() || listBundlerConnected == null)) {
						Service newService = new Service();

						newService.setNameService(service1.getNameService());

						Bundler newBundler = new Bundler();
						newBundler.setName(service1.getBundlerProvide().getName());
						newBundler.setVersion(service1.getBundlerProvide().getVersion());
						newBundler.setLocation(service1.getBundlerProvide().getLocation());
						newBundler.setState(service1.getBundlerProvide().getState());

						newService.setBundlerProvide(newBundler);

						newService.getListUsesBundles().addAll(listBundlerConnected);

						result.add(newService);
					}
				}
			}
		}

		return result;

	}

	/**
	 * The method that receives the list as persistent with the last update and
	 * the list with the system information at that time.
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @param s1
	 *            List<Service>, s2 List<Service>
	 * @return List<Service> - Returns services that have excluded bundles
	 */
	public List<Service> compareToServiceInformationBundlerDisconnected(List<Service> s1, List<Service> s2) {
		// input - listPersistent - listSystem(Karaf)
		List<Service> result = new ArrayList<Service>();

		for (Service service1 : s1) {
			for (Service service2 : s2) {
				if ((service1.getNameService().equals(service2.getNameService()))
						&& (service2.getBundlerProvide().equals(service2.getBundlerProvide()))) {

					List<Bundler> listTempService1 = new ArrayList<Bundler>();
					listTempService1.addAll(service1.getListUsesBundles());

					List<Bundler> listTempService2 = new ArrayList<Bundler>();
					listTempService2.addAll(service2.getListUsesBundles());

					List<Bundler> listBundlerConnected = compareToServiceInformationCompareBundlers(listTempService1,
							listTempService2);

					if (!(listBundlerConnected.isEmpty() || listBundlerConnected == null)) {
						Service serviceTemp = new Service();

						serviceTemp.setNameService(service1.getNameService());
						serviceTemp.setBundlerProvide(service1.getBundlerProvide());
						serviceTemp.getListUsesBundles().addAll(listBundlerConnected);

						result.add(serviceTemp);
					}
				}
			}
		}

		return result;

	}

	/**
	 * Method receives the list of persistent services and the list of system
	 * services then calls the bundle comparison method to check if there is
	 * different information in the existing bundles. If it does, it assembles a
	 * class with the gateway, service, and bundle information with changed
	 * information.
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @param s1
	 *            List<Service>, s2 List<Service>
	 * @return List<Service> - Returns a list of information about services and
	 *         bundles with changed information
	 */

	public List<Service> compareToServiceInformationBundlerAltered(List<Service> s1, List<Service> s2) {
		// input - listPersistent - listSystem(Karaf)
		List<Service> result = new ArrayList<Service>();

		for (Service service1 : s1) {
			for (Service service2 : s2) {
				if ((service1.getNameService().equals(service2.getNameService()))
						&& (service2.getBundlerProvide().equals(service2.getBundlerProvide()))) {

					List<Bundler> listBundlerAltered = this.compareToServiceInformationCompareInformationBundler(
							service1.getListUsesBundles(), service2.getListUsesBundles());

					if (!(listBundlerAltered.isEmpty() || listBundlerAltered == null)) {
						Service temp = new Service();
						// modificar para capturar somente o que está alterado.
						temp.setNameService(service2.getNameService());

						Bundler newBundler = new Bundler();
						newBundler.setName(service2.getBundlerProvide().getName());
						newBundler.setVersion(service2.getBundlerProvide().getVersion());
						newBundler.setLocation(service2.getBundlerProvide().getLocation());
						newBundler.setState(service2.getBundlerProvide().getState());

						temp.setBundlerProvide(newBundler);

						List<Bundler> listTemp = new ArrayList<Bundler>();
						for (Bundler bd : listBundlerAltered) {
							for (Bundler bd2 : service2.getListUsesBundles()) {

								if (bd.getName().equals(bd2.getName()) && bd.getVersion().equals(bd2.getVersion())) {
									newBundler = new Bundler();

									newBundler.setName(bd2.getName());
									newBundler.setState(bd2.getState());
									newBundler.setLocation(bd2.getLocation());
									newBundler.setVersion(bd2.getVersion());

									listTemp.add(newBundler);
								}
							}

						}

						temp.getListUsesBundles().addAll(listTemp);

						result.add(temp);
					}
				}
			}
		}

		return result;

	}

	/**
	 * Method that compares two lists of bundles and returns the bundles that
	 * exist in the first list and do not exist in the second.
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @param l1
	 *            List<Bundler>, l2 List<Bundler>
	 * @return List<Bundler> - Returns elements of the first list that does not
	 *         exist in the second
	 */
	public List<Bundler> compareToServiceInformationCompareBundlers(List<Bundler> l1, List<Bundler> l2) {

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
	 * Method that receives the persistent list and a system list and returns
	 * the list of bundles that have changed information
	 * 
	 * @author Nilson Rodrigues Sousa
	 * @param l1
	 *            List<Bundler>, l2 List<Bundler>
	 * @return List<Bundler> - Returns the list of bundles that have changed
	 *         information
	 */
	public List<Bundler> compareToServiceInformationCompareInformationBundler(List<Bundler> l1, List<Bundler> l2) {
		// input - listPersistent - listSystem(Karaf)
		List<Bundler> result = new ArrayList<Bundler>();
		boolean exist;

		for (Bundler b1 : l1) {
			exist = false;
			for (Bundler b2 : l2) {
				if ((b1.getName().equals(b2.getName())) && (b1.getVersion().equals(b2.getVersion()))) {
					if (!(b1.getLocation().equals(b2.getLocation())) || !(b1.getState().equals(b2.getState()))) {
						exist = true;
					}
					break;
				}
			}
			if (exist == true) {
				result.add(b1);
			}
		}

		return result;
	}

}