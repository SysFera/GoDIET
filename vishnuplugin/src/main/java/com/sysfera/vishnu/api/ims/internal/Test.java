package com.sysfera.vishnu.api.ims.internal;

public class Test {
	static {
		//System.loadLibrary("VISHNU_IMS");
	}
	public static void main(String[] args) {

		ListProcesses p = new ListProcesses();
		ProcessOp op = new ProcessOp();
		try {
			VISHNU_IMS.getProcesses("toto", p, op);
			new Test().display(p);
			p = new ListProcesses();
			VISHNU_IMS.getProcesses("0", p, op);
			new Test().display(p);
		} catch (InternalIMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void display(ListProcesses p) {
		for (int i = 0; i < p.getProcess().size(); i++) {
			System.out.println("Process name: "
					+ p.getProcess().get(i).getProcessName());
			System.out.println("Diet name: "
					+ p.getProcess().get(i).getDietId());
			System.out.println("Machine id: "
					+ p.getProcess().get(i).getMachineId());
		}
	}

}