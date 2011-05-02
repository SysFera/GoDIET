package com.sysfera.godiet.exceptions.remote;

import com.sysfera.godiet.exceptions.generics.GoDietException;


/**
 * Throw when a remote execution could not be done
 * @author phi
 *
 */
public class CheckException extends GoDietException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CheckException(String errormessage)
	{
		this("TMPDEFAULTCONTEXT","TEMPCODE",errormessage);
	}

	public CheckException(String errormessage, Throwable e)
	{
		this("TMPDEFAULTCONTEXT",e.getMessage(),errormessage);
	}
	public CheckException(String errorContext, String errorCode,
			String errorMessage) {
		super(errorContext, errorCode, errorMessage);
	}

}
