package org.apache.servicemix.examples.cxf.info;

import java.util.Random;

import org.apache.servicemix.examples.cxf.util.InfraUtil;

/**
 * Class responsible capture gateway status information
 *
 * @author Nilson Rodrigues Sousa
 */
public class GatewayStatusInfo {
	
		// returns the battery level in percent
		public String getBaterryLevel() {
			return String.valueOf(new Random().nextInt(100));
		}

		// returns the total of memory
		public String getTotalMemory() {
			return String.valueOf(new Random().nextInt(19700621));
		}

		// returns the total of used memory
		public String getUsedMemory() {
			return String.valueOf(new Random().nextInt(19700621));
		}

		// returns the total of free memory
		public String getFreeMemory() {
			return String.valueOf(new Random().nextInt(19700621));
		}

		// returns the percentage value of the processor's capacity used
		public String getUsedProcessor() {
			return String.valueOf(new Random().nextDouble() * 100);
		}

		// returns the percentage value of the free processor capacity
		public String getFreeProcessor() {
			return String.valueOf(new Random().nextDouble() * 100);
		}
		
		// returns the address mac
		public String getMac () {
			return InfraUtil.getMacAddress();
		}
		
		//return the hour and date last update
		public String getLastUpdate() {
			return InfraUtil.getDateHour();
		}

}
