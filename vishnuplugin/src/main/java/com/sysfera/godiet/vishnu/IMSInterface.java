package com.sysfera.godiet.vishnu;

import java.util.List;

import com.sysfera.godiet.vishnu.model.ProcessOps;
import com.sysfera.godiet.vishnu.model.Processus;
import com.sysfera.godiet.vishnu.model.SessionKey;

public interface IMSInterface {
	public List<Processus> getProcesses(SessionKey sessionKey,ProcessOps options);
}
