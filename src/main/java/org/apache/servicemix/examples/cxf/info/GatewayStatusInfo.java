package org.apache.servicemix.examples.cxf.info;

import java.util.Calendar;
import java.util.Random;

import org.apache.servicemix.examples.cxf.util.InfraUtil;

/**
 * Class responsible capture gateway status information
 *
 * @author Nilson Rodrigues Sousa
 */
public class GatewayStatusInfo {
	
		// returns the battery level in percent
		public double getBaterryLevel() {
			return  Double.parseDouble(String.valueOf(new Random().nextInt(100)));
		}

		// returns the total of memory
		public long getTotalMemory() {
			return Long.parseLong(String.valueOf(new Random().nextInt(19700621)));
		}

		// returns the total of used memory
		public long getUsedMemory() {
			return Long.parseLong(String.valueOf(new Random().nextInt(19700621)));
		}

		// returns the total of free memory
		public long getFreeMemory() {
			return Long.parseLong(String.valueOf(new Random().nextInt(19700621)));
		}

		// returns the percentage value of the processor's capacity used
		public double getUsedProcessor() {
			return Double.parseDouble(String.valueOf(new Random().nextDouble() * 100));
		}

		// returns the percentage value of the free processor capacity
		public double getFreeProcessor() {
			return Double.parseDouble(String.valueOf(new Random().nextDouble() * 100));
		}
		
		// returns the address mac
		public String getMac () {
			return InfraUtil.getMacAddress();
		}
		
		//return the hour and date last update
		public Calendar getDate() {
			return Calendar.getInstance();
		}

}
