package br.ufpe.cin.airconditioner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.ufpe.cin.contract.IResource;
import br.ufpe.cin.model.AirConditioner;
import br.ufpe.cin.model.BinarySwitch;
import br.ufpe.cin.model.ThermoStat;
import br.ufpe.cin.resource.AirConditionerResource;
import br.ufpe.cin.resource.BinarySwitchResource;
import br.ufpe.cin.resource.Resource;
import br.ufpe.cin.resource.TemperatureResource;

public class AirControlee {
	
	private AirConditionerResource mAirConditionerResource;
	private BinarySwitchResource mBinarySwitchResource;
	private TemperatureResource tempResource;
 //	private FirmwareResource mFirmwareResource;

	private List<IResource> mResources;
	
	public void createResources() {
		AirConditioner airCon = new AirControlee();
		BinarySwitch bSwitch = new BinarySwitch();
		ThermoStat mThermo = new ThermoStat();
		
		mResources = new ArrayList<Resource>();
		java.util.List<String> resourceTypes = new ArrayList<>();
		List<String> resourceInterfaces = new ArrayList<>();
		
		System.out.println("Creating resources");		
		mResources.add(new AirConditionerResource("/aircon/0", new ArrayList<>(Arrays.asList("x.org.iotivity.ac"), new ArrayList<>(Arrays.asList("DEFAULT_INTERFACE", "BATCH_INTERFACE", "LINK_INTERFACE")));
		mResources.add(new AirConditionerResource("/power/0", new ArrayList<>(Arrays.asList("oic.r.switch.binary"), new ArrayList<>(Arrays.asList("DEFAULT_INTERFACE")));
		mResources.add(new AirConditionerResource("/temperature/0", new ArrayList<>(Arrays.asList("oic.r.temperature"), new ArrayList<>(Arrays.asList("DEFAULT_INTERFACE")));
		
		initResources();
	}
	
	
	private void initResources() {
		System.out.println("Registering resources");
		mResources.forEach(resource -> resource.registerResource());
	}
	
			
	
}
