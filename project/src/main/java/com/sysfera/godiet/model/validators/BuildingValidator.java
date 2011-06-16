package com.sysfera.godiet.model.validators;

import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.generated.Software;

/**
 * Software building validation
 * @author phi
 *
 */
public class BuildingValidator {


	/**
	 * A software must have an id and not already registered
	 * @param software
	 * @throws DietResourceValidationException
	 */
	private static  void validate(Software software,DietManager dietManager)
			throws DietResourceValidationException {
		if (software.getId() == null
				|| dietManager.getManagedSoftware(software.getId()) != null) {
			throw new DietResourceValidationException("");
		}

	}

	/**
	 * 
	 * @see BuildingValidator#validate(Software)
	 * @param masterAgent
	 * @throws DietResourceValidationException
	 */
	public static void validate(MasterAgent masterAgent,DietManager dietManager)
			throws DietResourceValidationException {
		validate((Software) masterAgent,dietManager);
	}

	/**
	 * 
	 * @see BuildingValidator#validate(Software)
	 * @param omniNames
	 * @throws DietResourceValidationException
	 */
	public static void validate(OmniNames omniNames,DietManager dietManager)
			throws DietResourceValidationException {
		validate((Software) omniNames,dietManager);
	}
	/**
	 * 
	 * @see BuildingValidator#validate(Software)
	 * @param forwarder
	 * @throws DietResourceValidationException
	 */
	public static void validate(Forwarder forwarder,DietManager dietManager)
			throws DietResourceValidationException {
		validate((Software) forwarder,dietManager);
	}
	/**
	 * Local agent need to have a registred parent (LA or MA)
	 * 
	 * @see BuildingValidator#validate(Software)
	 * @param localAgent
	 * @throws DietResourceValidationException
	 */
	public static void validate(LocalAgent localAgent,DietManager dietManager)
			throws DietResourceValidationException {
		validate((Software) localAgent, dietManager);

		if (localAgent.getParent() == null) {
			throw new DietResourceValidationException(
					"Local agent need to have a registred parent");
		}

		if (dietManager.getManagedSoftware(localAgent.getParent().getId()) == null) {
			throw new DietResourceValidationException(
					"Local agent need to have a registred parent. "
							+ localAgent.getParent().getId() + "not registred");
		}
	}

	/**
	 * Sed need to have a registred parent (LA or MA)
	 * 
	 * @see BuildingValidator#validate(Software)
	 * @param localAgent
	 * @throws DietResourceValidationException
	 */
	public static void validate(Sed sed,DietManager dietManager) throws DietResourceValidationException {
		validate((Software) sed,dietManager);

		if (sed.getParent() == null) {
			throw new DietResourceValidationException(
					"Sed need to have a registred parent");
		}

		if (dietManager.getManagedSoftware(sed.getParent().getId()) == null) {
			throw new DietResourceValidationException(
					"Sed need to have a registred parent. "
							+ sed.getParent().getId() + " not registred");
		}
	}
}
