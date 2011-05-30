package com.sysfera.godiet.vishnu;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sysfera.vishnu.api.ims.stub.ListProcessesStub;
import com.sysfera.vishnu.api.ims.stub.ProcessIF;
import com.sysfera.vishnu.api.ims.stub.ProcessStubImpl;
import com.sysfera.vishnu.api.ims.stub.VISHNU_IMSStub;


public class VishnuListenerTest {
	VISHNU_IMSStub vishnuStub = new VISHNU_IMSStub(); 
	
	@Before
	public void init()
	{
		
		//Init ProcessIFes
		ListProcessesStub processes = new ListProcessesStub();
		ProcessIF p = new ProcessStubImpl();
		p.setProcessName("UMS");
		p.setMachineId("machine_1");
		p.setDietId("diet_1_1");
		p.setState(1);
		p.setTimestamp(123);
		p.setScript("script1");
		processes.getProcesses().add(p);

		p.setProcessName("IMS");
		p.setMachineId("machine_1");
		p.setDietId("diet_1_2");
		p.setState(1);
		p.setTimestamp(124);
		p.setScript("script2");
		processes.getProcesses().add(p);


		p.setProcessName("IMS");
		p.setMachineId("machine_2");
		p.setDietId("diet_2_1");
		p.setState(1);
		p.setTimestamp(126);
		p.setScript("script3");
		processes.getProcesses().add(p);

		p.setProcessName("FMS");
		p.setMachineId("machine_2");
		p.setDietId("diet_2_2");
		p.setState(1);
		p.setTimestamp(126);
		p.setScript("script4");
		processes.getProcesses().add(p);

		p.setProcessName("TMS");
		p.setMachineId("machine_2");
		p.setDietId("diet_2_3");
		p.setState(1);
		p.setTimestamp(127);
		p.setScript("script5");
		processes.getProcesses().add(p);
		
		
		VISHNU_IMSStub.setProcesses(processes );

	}
	
	@Test
	public void test1()
	{
		
		ListProcessesStub processes  = VISHNU_IMSStub.getProcesses("random" , null);
		Assert.assertEquals(processes.getProcesses().size(),5);
		
		
		ProcessIF process = new ProcessStubImpl();
		process.setDietId("diet_2_3");
		processes.getProcesses().remove(process);
		
		Assert.assertEquals(4,processes.getProcesses().size());
		process.setDietId("notin");
		Assert.assertEquals(4,processes.getProcesses().size());
		
	}
	
}
