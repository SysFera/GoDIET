package com.sysfera.godiet.services;

import java.io.IOException;
import java.io.InputStream;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.ResourceAddException;
import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.exceptions.remote.IncubateException;

public interface XMLLoaderService {

	public abstract void registerConfigurationFile(InputStream xmlInput)
			throws IOException, XMLParseException, GoDietConfigurationException;

	// TODO: Transactionnal
	public abstract void registerDietElements(InputStream xmlInput)
			throws IOException, XMLParseException,
			DietResourceCreationException, DietResourceValidationException,
			IncubateException;

	// TODO: Transactionnal
	public abstract void registerInfrastructureElements(InputStream xmlInput)
			throws IOException, XMLParseException, ResourceAddException,
			GraphDataException;

}