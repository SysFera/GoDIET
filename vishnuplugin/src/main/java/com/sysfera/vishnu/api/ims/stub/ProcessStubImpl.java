package com.sysfera.vishnu.api.ims.stub;

public class ProcessStubImpl  implements ProcessIF{
	
	private String machineId;
	private String dietId;
	private int state;
	private long timestamp;
	private String script;
	private String processName;
	
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getDietId() {
		return dietId;
	}
	public void setDietId(String dietId) {
		this.dietId = dietId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public long getTimestamp() {
		return timestamp;
	}

	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dietId == null) ? 0 : dietId.hashCode());
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
		ProcessStubImpl other = (ProcessStubImpl) obj;
		if (dietId == null) {
			if (other.dietId != null)
				return false;
		} else if (!dietId.equals(other.dietId))
			return false;
		return true;
	}

	@Override
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
		
	}
	
	
	

}
