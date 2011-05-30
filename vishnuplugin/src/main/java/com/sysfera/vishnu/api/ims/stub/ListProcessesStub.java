package com.sysfera.vishnu.api.ims.stub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListProcessesStub {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((processes == null) ? 0 : processes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ListProcessesStub other = (ListProcessesStub) obj;
		if (processes == null) {
			if (other.processes != null)
				return false;
		} else if (!processes.equals(other.processes))
			return false;
		return true;
	}

	List<ProcessIF> processes  =  Collections.synchronizedList(new ArrayList<ProcessIF>());
	
	public List<ProcessIF> getProcesses() {
		return processes;
	}
}
