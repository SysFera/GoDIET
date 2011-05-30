
package com.sysfera.vishnu.api.ims.internal;

public class VISHNU_IMS {
    public static int getProcesses(String sessionKey, ListProcesses list, ProcessOp op) throws InternalIMSException {
	if (sessionKey.compareTo("0")==0) {
	    throw new InternalIMSException ("Exception of test launched");
	}
	Process p = new Process();
	p.setProcessName("UMS");
	p.setMachineId("machine_1");
	p.setDietId("diet_1_1");
	p.setState(1);
	p.setTimestamp(123);
	p.setScript("script1");
	list.getProcess().push_back(p);

	p.setProcessName("IMS");
	p.setMachineId("machine_1");
	p.setDietId("diet_1_2");
	p.setState(1);
	p.setTimestamp(124);
	p.setScript("script2");
	list.getProcess().push_back(p);


	p.setProcessName("IMS");
	p.setMachineId("machine_2");
	p.setDietId("diet_2_1");
	p.setState(1);
	p.setTimestamp(126);
	p.setScript("script3");
	list.getProcess().push_back(p);

	p.setProcessName("FMS");
	p.setMachineId("machine_2");
	p.setDietId("diet_2_2");
	p.setState(1);
	p.setTimestamp(126);
	p.setScript("script4");
	list.getProcess().push_back(p);

	p.setProcessName("TMS");
	p.setMachineId("machine_2");
	p.setDietId("diet_2_3");
	p.setState(1);
	p.setTimestamp(127);
	p.setScript("script5");
	list.getProcess().push_back(p);
	return 0;
    }

}
