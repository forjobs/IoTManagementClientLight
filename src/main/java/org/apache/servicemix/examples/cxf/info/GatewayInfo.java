package org.apache.servicemix.examples.cxf.info;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.apache.servicemix.examples.cxf.model.Bundler;
import org.apache.servicemix.examples.cxf.model.CPU;
import org.apache.servicemix.examples.cxf.model.Service;
import org.apache.servicemix.examples.cxf.util.InfraUtil;

/**
 * Class responsible capture information about the gateway
 *
 * @author Nilson Rodrigues Sousa
 */
public class GatewayInfo {

	// This constructor could be the cause of the error in
	// libsigar-amd64-linux.so in java.library.path
	// Possible solution will be to put the SigarProvisioner.provision () within
	// the classes that use the sigar variable
	public GatewayInfo() {

	}

	// description of OS in use
	public String getDescription() {
		return "Debian_Light";
	}
	
	// to be defined
	public String getModel() {
		return "0.11.2";
	}

	// to be defined
	public String getManufacturer() {
		return "Raspberry";
	}

	// to be defined
	public String getFirmware() {
		return "2.1.0";
	}

	// on-off used server side opportunistically
	public boolean isStatus() {
		// possible functionality is to use the method to return error status.
		return true;
	}

	// returns the list of components in existing stores
	public long getStorage() {
		// List<Storage> listSim = new ArrayList<Storage>();
		//
		// Storage storage;
		//
		// for (int i = 0; i < 3; i++) {
		// storage = new Storage();
		//
		// storage.setDeviceName("/dev/sda1");
		// storage.setFreeSpaceKB(3 * 100 * 12 * 5 * i);
		// storage.setTotalSizeKB(288237920);
		// storage.setMountPoint("/");
		// storage.setOsSpecificFSType("ext4");
		//
		// listSim.add(storage);
		// }
		// return listSim;

		return new Random().nextInt(19700621);
	}

	// method for the server side, but the client can register its actualization
	// if possible
	public Calendar getLastUpdate() {
		return Calendar.getInstance();
		//return InfraUtil.getDateHour();
	}

	// returns information about CPU characteristics
	public List<CPU> getCPU() {
		CPU cpu;
		List<CPU> listCPUInfoModel = new ArrayList<CPU>();

		for (int i = 0; i < 3; i++) {
			cpu = new CPU();

			cpu.setVendor("Intel");
			cpu.setClock(i + 1);
			cpu.setTotalCores(i + 1);
			cpu.setModel("Intel 4004");

			listCPUInfoModel.add(cpu);
		}

		return listCPUInfoModel;
	}

	// returns the mac address
	public String getMac() {
		return InfraUtil.getMacAddress();
	}

	// returns the ip address
	public String getIp() {
		return InfraUtil.getIpMachine();
	}

	// returns the hostname
	public String getHostName() {
		return "RaspberryHostName";

	}

	// returns the location
	public String getLocation() {
		// Will return a fake location
		return "-12.9990189,-38.5140298";
	}

	public List<Bundler> getListBundler() {
		return BundlerInfo.listBundler;
	}

	// will return a fake location
	public List<Service> getService() {
		return ServiceInfo.listService;
	}

	// returns a list with the existing network interfaces
	// untested method may then display errors
	public String[] getIntefaceNetwork() {
		return null;
	}

}
