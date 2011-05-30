package com.sysfera.vishnu;

import com.sysfera.godiet.exceptions.remote.CheckException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.validators.RuntimeValidator;

public class GoVishnuSedFactory {

	private final DietManager dietManager;
	private final RuntimeValidator validator;
	private final SoftwareController softwareController; 
	
	public GoVishnuSedFactory(DietManager dm) {
		this.dietManager = dm;
		validator = new EmptyValidator(dietManager);
		softwareController = new EmptySoftwareController();
	}
	
	public DietResourceManaged create(Sed sedDescription)
	{
		DietResourceManaged managedSed = new DietResourceManaged(softwareController, validator);
		managedSed.setManagedSoftware(sedDescription);
		return managedSed;
	}
	
	
	
	class EmptyValidator extends RuntimeValidator
	{

		public EmptyValidator(DietManager dietManager) {
			super(dietManager);
			
		}

		@Override
		public void wantLaunch(SoftwareManager managedResource)
				throws LaunchException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void wantStop(SoftwareManager managedResource)
				throws StopException {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	
	
	class EmptySoftwareController implements SoftwareController{

		@Override
		public void configure(SoftwareManager resource) throws PrepareException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void launch(SoftwareManager managedSofware)
				throws LaunchException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void check(SoftwareManager resource) throws CheckException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void stop(SoftwareManager resource) throws StopException {
			// TODO Auto-generated method stub
			
		}
		
	}
}
