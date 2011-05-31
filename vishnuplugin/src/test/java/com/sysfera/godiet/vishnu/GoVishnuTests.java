package com.sysfera.godiet.vishnu;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.sysfera.godiet.managers.DietManager;
import com.sysfera.vishnu.GoVishnuPlugin;
import com.sysfera.vishnu.api.ims.stub.ListProcessesStub;
import com.sysfera.vishnu.api.ims.stub.ProcessIF;
import com.sysfera.vishnu.api.ims.stub.ProcessStubImpl;
import com.sysfera.vishnu.api.ims.stub.VISHNU_IMSStub;

public class GoVishnuTests {
	GoVishnuPlugin vishnuPlugin;
	DietManager dm;
	
	
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

		p = new ProcessStubImpl();
		p.setProcessName("IMS");
		p.setMachineId("machine_1");
		p.setDietId("diet_1_2");
		p.setState(1);
		p.setTimestamp(124);
		p.setScript("script2");
		processes.getProcesses().add(p);

		p = new ProcessStubImpl();
		p.setProcessName("IMS");
		p.setMachineId("machine_2");
		p.setDietId("diet_2_1");
		p.setState(1);
		p.setTimestamp(126);
		p.setScript("script3");
		processes.getProcesses().add(p);
		p = new ProcessStubImpl();
		p.setProcessName("FMS");
		p.setMachineId("machine_2");
		p.setDietId("diet_2_2");
		p.setState(1);
		p.setTimestamp(126);
		p.setScript("script4");
		processes.getProcesses().add(p);

		p = new ProcessStubImpl();
		p.setProcessName("TMS");
		p.setMachineId("machine_2");
		p.setDietId("diet_2_3");
		p.setState(1);
		p.setTimestamp(127);
		p.setScript("script5");
		processes.getProcesses().add(p);
		
		
		VISHNU_IMSStub.setProcesses(processes );
		
	}
	
	
	/**
	 * Check if all resources are add in DietManager
	 */
	@Test
	public void test1()
	{
		dm = new DietManager();
		vishnuPlugin = new GoVishnuPlugin(dm);
		
		vishnuPlugin.init(0);
		
		
		vishnuPlugin.stopListener();
		vishnuPlugin.waitProperExit();
		Assert.assertEquals(5,dm.getSeds().size());
		Assert.assertNotNull(dm.getManagedSoftware("diet_1_1"));
		Assert.assertNotNull(dm.getManagedSoftware("diet_1_2"));
		Assert.assertNotNull(dm.getManagedSoftware("diet_2_2"));
		Assert.assertNotNull(dm.getManagedSoftware("diet_2_3"));

		Assert.assertNotNull(dm.getManagedSoftware("diet_2_1"));
		
		
	}
	
	/**
	 * Simulate a removed resource. Restart polling 
	 */
	@Test
	public void test2()
	{
		dm = new DietManager();
		vishnuPlugin = new GoVishnuPlugin(dm);
		
		vishnuPlugin.init(0);
		ProcessIF procesRemoved = new ProcessStubImpl();
		procesRemoved.setDietId("diet_2_1");
		VISHNU_IMSStub.getProcesses("fake", null).getProcesses().remove(procesRemoved);

		vishnuPlugin.stopListener();
		vishnuPlugin.waitProperExit();
		
		
		Assert.assertEquals(4,dm.getSeds().size());
		Assert.assertNull(dm.getManagedSoftware("diet_2_1"));
		
		//Restart 
		vishnuPlugin.init(0);

	}
}
