package com.sysfera.godiet.common.services;

import java.io.IOException;

import com.sysfera.godiet.common.exceptions.DietResourceCreationException;
import com.sysfera.godiet.common.exceptions.ResourceAddException;
import com.sysfera.godiet.common.exceptions.XMLParseException;
import com.sysfera.godiet.common.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.common.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;

public interface XMLLoaderService {

	public abstract void registerConfigurationFile(String xmlInput)
			throws IOException, XMLParseException, GoDietConfigurationException;

	// TODO: Transactionnal
	public abstract void registerDietElements(String xmlInput)
			throws IOException, XMLParseException,
			DietResourceCreationException, DietResourceValidationException,
			IncubateException, GraphDataException;

	// TODO: Transactionnal
	public abstract void registerInfrastructureElements(String xmlInput)
			throws IOException, XMLParseException, ResourceAddException,
			GraphDataException;

}