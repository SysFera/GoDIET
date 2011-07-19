package com.sysfera.godiet.model.configurator;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.model.SoftwareInterface;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.softwares.SoftwareManager;

/**
 * 
 * Dynamically build a freemarker model given the managed software.
 * 
 * 
 * @author phi
 * 
 */
public class TemplateModel {
	private static Logger log = LoggerFactory
			.getLogger(TemplateModel.class);

	/**
	 * 
	 * <pre>
	 * (root)
	 *   |
	 *   +- this (@see {@link SoftwareManager})
	 *   |		|
	 *   |		|
	 *   |		+ id
	 *   |	 	|
	 *   |		+ ...
	 *   +- parent (@see {@link Resource})
	 *   |		|
	 *   |		|
	 *   |		+ id
	 *   |	 	|
	 *   |		+ ...
	 *   |
	 *   +- node (@see {@link Resource})
	 *   |		|
	 *   |		|
	 *   |		+ id
	 *   |	 	|
	 *   |		+ ...
	 *   |
	 * </pre>
	 * 
	 * 
	 * @param software
	 * @return Null if software = null
	 */
	public static Map<Object, Object> buildModel(
			SoftwareInterface<? extends Software> software) {
		Map<Object, Object> freemarkerModel = new HashMap<Object, Object>();
		if(software == null){
			log.error("Try to build model with an nill resource");
			return null;
			
		}
		freemarkerModel.put("this",software);
		
		if (software.getSoftwareDescription().getParent() != null) {

			freemarkerModel.put("parent", software.getSoftwareDescription()
					.getParent());
		}
		if (software.getPluggedOn() != null) {
			freemarkerModel.put("node", software.getPluggedOn());
		} else {
			log.warn("Try to construct model from a not plugged software",
					software.getSoftwareDescription().getId());
		}
		return freemarkerModel;
	}
}
