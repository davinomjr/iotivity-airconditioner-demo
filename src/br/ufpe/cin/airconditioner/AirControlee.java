package br.ufpe.cin.airconditioner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.iotivity.base.OcException;
import org.iotivity.base.OcRepresentation;
import org.iotivity.base.OcResourceHandle;

import br.ufpe.cin.contract.IResource;
import br.ufpe.cin.model.AirConditioner;
import br.ufpe.cin.model.BinarySwitch;
import br.ufpe.cin.model.ThermoStat;
import br.ufpe.cin.resource.AirConditionerResource;
import br.ufpe.cin.resource.BinarySwitchResource;
import br.ufpe.cin.resource.TemperatureResource;


public class AirControlee {
	
	private AirConditionerResource mAirConditionerResource;
	private BinarySwitchResource mBinarySwitchResource;
	private TemperatureResource tempResource;
 //	private FirmwareResource mFirmwareResource; skipped on this sample, NOT MANDATORY (OIC Specs)

	private Set<IResource> mResources = new HashSet<>();
	
	public void createResources() throws OcException {
		AirConditioner airCon = new AirConditioner();
		BinarySwitch bSwitch = new BinarySwitch();
		ThermoStat mThermo = new ThermoStat();
		
		
		List<String> resourceTypes = new ArrayList<>();
		List<String> resourceInterfaces = new ArrayList<>();
		
		//initiating resources
		
		AirConditionerResource airCondResource = new AirConditionerResource("/aircon/0", new ArrayList<String>(Arrays.asList("x.org.iotivity.ac")), new ArrayList<String>(Arrays.asList("DEFAULT_INTERFACE", "BATCH_INTERFACE", "LINK_INTERFACE")), new AirConditioner());
		BinarySwitchResource switchResource = new BinarySwitchResource("/power/0", new ArrayList<>(Arrays.asList("oic.r.switch.binary")), new ArrayList<>(Arrays.asList("DEFAULT_INTERFACE")), new BinarySwitch());
		TemperatureResource tempResource = new TemperatureResource("/temperature/0", new ArrayList<>(Arrays.asList("oic.r.temperature")), new ArrayList<>(Arrays.asList("DEFAULT_INTERFACE")), new ThermoStat());
		
		
		System.out.println("Creating resources");		
		mResources.add(airCondResource );
		mResources.add(switchResource);
		mResources.add(tempResource);		
		initResources();
		
		airCondResource.setDeviceInfo();
		airCondResource.addChildResource(switchResource);
		airCondResource.addChildResource(tempResource);	
	}
	
	public List<OcResourceHandle> getResourceHandles(){
		List<OcResourceHandle> handles = new ArrayList<>();
		mResources.forEach(t -> handles.add(t.getResourceHandle()));
		return handles;
	}
	
	public void setBinarySwitchRep(boolean value) throws OcException {
		OcRepresentation rep = new OcRepresentation();
		rep.setValue("value", value);
		mBinarySwitchResource.setRepresentation(rep);
	}
	
	private void initResources()  {
		System.out.println("Registering resources");
		mResources.forEach(resource -> {
			try {
				resource.registerResource();
			} catch (OcException e) {
				System.out.println("Problem registering the resource");
				e.printStackTrace();
			}
		});
	}
	
			
	
}
