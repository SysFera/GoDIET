package com.sysfera.vishnu.api.ims.stub;

import com.sysfera.vishnu.api.ims.internal.ProcessOp;

public class VISHNU_IMSStub {

	private static ListProcessesStub processes = new ListProcessesStub();

	public static ListProcessesStub getProcesses(String key, 
			ProcessOp p) {
		
		return processes;
	}

	public static void setProcesses(ListProcessesStub processes) {
		VISHNU_IMSStub.processes  = processes;

	}
}
