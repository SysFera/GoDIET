package com.sysfera.godiet.model.utils;

import java.util.List;

import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Var;

public class ResourceUtil {

	/**
	 * Search the value in Node's environment variables
	 * @param node
	 * @param varName
	 * @return the value or null if it varName doesn't exist
	 */
	public static String getEnvValue(final Resource node,final String varName)
	{
		List<Var> vars = node.getEnv().getVar();
		for (Var var : vars) {
			if(var.getName().equals(varName)) {
				return var.getValue();
                        }
		}
		return null;
	}
}
