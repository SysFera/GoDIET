package com.sysfera.vishnu.api.ims.stub;

public interface ProcessIF {

	
	public abstract String getProcessName();

	public abstract void setProcessName(String _processName);

	public abstract String getMachineId();

	public abstract void setMachineId(String _machineId);

	public abstract String getDietId();

	public abstract void setDietId(String _dietId);

	public abstract int getState();

	public abstract void setState(int _state);

	public abstract long getTimestamp();
	public abstract void setTimestamp(long timestamp);

	public abstract String getScript();

	public abstract void setScript(String _script);

}