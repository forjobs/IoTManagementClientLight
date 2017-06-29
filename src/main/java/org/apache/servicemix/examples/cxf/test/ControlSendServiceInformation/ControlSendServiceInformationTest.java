package org.apache.servicemix.examples.cxf.test.ControlSendServiceInformation;

import java.util.ArrayList;
import java.util.List;

import org.apache.servicemix.examples.cxf.info.GatewayInfo;
import org.apache.servicemix.examples.cxf.info.ServiceInfo;
import org.apache.servicemix.examples.cxf.model.Bundler;
import org.apache.servicemix.examples.cxf.model.Gateway;
import org.apache.servicemix.examples.cxf.model.Service;
import org.apache.servicemix.examples.cxf.service.ServiceService;

public class ControlSendServiceInformationTest {

	public static List<Service> listServices;
	private ServiceService serviceService;
	private ServiceInfo serviceInfo = null;

	public ControlSendServiceInformationTest() {
		serviceService = new ServiceService();
		listServices = new ArrayList<Service>();
	}

	// used by blueprint
	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	// information about the services
	public void compareInfoService(List<Service> listSist) {
		List<Service> listCapt = new ArrayList<Service>();

		for (Service tr : listSist) {
			Service s = new Service();

			s.setNameService(tr.getNameService());
			s.setBundlerProvide(tr.getBundlerProvide());
			s.getListUsesBundles().addAll(tr.getListUsesBundles());

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

			List<Service> listServicesInformationAlteredBundlerUse = this
					.compareToServiceInformationBundlerAltered(listServices, listServicesIntance); 
																									
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

			if (!(listServicesInformationAlteredBundlerUse.isEmpty()
					|| listServicesInformationAlteredBundlerUse == null)) {
				System.out.println(
						"###########################Existe elementos em infoServiceAlteredInformationAlteredBundlerUse.");
				this.infoServiceAlteredInformationAlteredBundlerUse(listServicesInformationAlteredBundlerUse);
			}

		}

	}

	// ------------------------------------------------------------------------------------------------------------------

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
		
		for(Service sv : serviceDisconnected) {
			sv.getListUsesBundles().clear();
		}
		
		gatewaySend.getListService().addAll(serviceDisconnected);

		// sending information
		serviceService.sendServiceDisconnected(gatewaySend);

	}

	// method for sending and updating connected service
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

	// ENVIA OS SERVIÇOS QUE POSSUEM NOVOS BUNDLES
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

	// Auxiliary methods

	// retorna os serviços que existem na lista1 e não existe na lista2
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

	// retorna os serviços que possuem novos bundles
	// entrada - listaNova - listaPersistida
	public List<Service> compareToServiceInformationBundlerConnected(List<Service> s1, List<Service> s2) {

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

	// retorna os serviços que possuem bundles excluidos
	// entrada - listaPersistida - listaNova -
	public List<Service> compareToServiceInformationBundlerDisconnected(List<Service> s1, List<Service> s2) {

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

	// recebe a lista de serviços persistita e a lista de serviços então chama o
	// método de comparação de bundles para verificar se existe informações
	// diferentes nos bundles existentes. Caso exista ele monta uma classe com
	// as informaçẽos do gateway do serviços e dos bundles com informações
	// alteradas.

	// se há serviços com bundles
	// listaPesistente - listaNova
	public List<Service> compareToServiceInformationBundlerAltered(List<Service> s1, List<Service> s2) {

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

	// Returns elements of the first list that does not exist in the second
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

	// retorna a lista de bundles que possuem informações alteradas
	// listaPesistente - listaSistema
	public List<Bundler> compareToServiceInformationCompareInformationBundler(List<Bundler> l1, List<Bundler> l2) {

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
