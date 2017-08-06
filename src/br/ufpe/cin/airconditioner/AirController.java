package br.ufpe.cin.airconditioner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.iotivity.base.OcAccountManager.OnObserveListener;
import org.iotivity.base.OcConnectivityType;
import org.iotivity.base.OcException;
import org.iotivity.base.OcHeaderOption;
import org.iotivity.base.OcPlatform;
import org.iotivity.base.OcPresenceHandle;
import org.iotivity.base.OcRepresentation;
import org.iotivity.base.OcResource;
import org.iotivity.base.OcResource.OnGetListener;
import org.iotivity.base.OcResource.OnPostListener;

public class AirController implements OcPlatform.OnResourceFoundListener, OnObserveListener, OcResource.OnPostListener {

	private String mHost;
	private OcPresenceHandle mPresenceHandle;
	private boolean lock;
	private OcResource mBinaryResource;

	public AirController(String host) {
		this.mHost = host;

	}

	public void findAirConditioner(String host) throws OcException {
		lock = true;
		OcPlatform.findResource(host, "/oic/res?rt=oic.wk.d",
				EnumSet.of(OcConnectivityType.CT_ADAPTER_TCP, OcConnectivityType.CT_ADAPTER_IP), this);
		waitUntilFinished();
	}

	private void waitUntilFinished() {
		while (lock) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("Finding resource went wrong (Thread)");
			}
		}
	}

	@Override
	public void onFindResourceFailed(Throwable error, String info) {
		error.printStackTrace();
		System.out.println("Couldnt find resource... Additional Info = " + info + "\n Closing app");
		System.exit(0);
	}

	@Override
	public void onResourceFound(OcResource resource) {
		List<String> resourceInterfaces = resource.getResourceInterfaces();
		if (resourceInterfaces.contains("oic.d.airconditioner")) {
			String searchQuery = "/oic/res?di=";
			searchQuery += resource.getUniqueIdentifier();
			System.out.println("Air Conditioner Found!");
			try {
				OcPlatform.findResource(mHost, searchQuery,
						EnumSet.of(OcConnectivityType.CT_ADAPTER_TCP, OcConnectivityType.CT_ADAPTER_IP),
						new OcPlatform.OnResourceFoundListener() {						
							@Override
							public void onResourceFound(OcResource resource) {
								if(resource.getResourceInterfaces().contains("x.org.iotivity.ac")) {
									System.out.println("Air Conditioner resource found!");
									Map<String,String> query = new HashMap<String,String>();
									query.put("if", OcPlatform.LINK_INTERFACE);
									try {
										
										resource.get(query, new OnGetListener() {

											@Override
											public void onGetFailed(Throwable arg0) {
											System.out.println("Something went wrong");
												
											}
											
											@Override
											public void onGetCompleted(List<OcHeaderOption> headers, OcRepresentation repFound) {
												lock = false;
												System.out.println("Building binary switch");
												List<OcRepresentation> repChildren = repFound.getChildren();
												repChildren.forEach(repChild -> {
													System.out.println("RT: " + repChild.getResourceTypes().get(0));			
													if(repChild.getResourceTypes().contains("oic.r.switch.binary")) {
														System.out.println("Observing " + repChild.getUri());	
														try {
															mBinaryResource = OcPlatform.constructResourceObject(mHost,repChild.getUri(),
																	EnumSet.of(OcConnectivityType.CT_ADAPTER_TCP, OcConnectivityType.CT_ADAPTER_IP),
																	true,
																	new ArrayList<String>(Arrays.asList("oic.r.switch.binary")),
																	new ArrayList<String>(Arrays.asList(OcPlatform.DEFAULT_INTERFACE)));
														} catch (OcException e) {
															e.printStackTrace();
														}
														
													}
												});											
											}
										});
										lock = true;
										waitUntilFinished();
									} catch (OcException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									lock = true;
									waitUntilFinished();
								}
							}
							
							@Override
							public void onFindResourceFailed(Throwable arg0, String arg1) {
								// TODO Auto-generated method stub
								
							}
						});
						

				mPresenceHandle = OcPlatform.subscribeDevicePresence(mHost,
						new ArrayList<String>(Arrays.asList(resource.getUniqueIdentifier().toString())),
						EnumSet.of(OcConnectivityType.CT_ADAPTER_TCP, OcConnectivityType.CT_ADAPTER_IP),
						(org.iotivity.base.OcResource.OnObserveListener) this);

			} catch (OcException e) {
				e.printStackTrace();
			}
		}

		lock = false;
	}

	@Override
	public void onObserveCompleted(List<OcHeaderOption> headers, OcRepresentation rep, int code) {
	}

	@Override
	public void onObserveFailed(Throwable error) {
	}

	public void turnOnOff(boolean turnOnOrNot) throws OcException {
		if (mBinaryResource == null) {
			System.out.println("Binary Switch not found");
			return;
		}

		OcRepresentation binarySwitch = new OcRepresentation();
		binarySwitch.setValue("value", turnOnOrNot);
		Map<String, String> query = null;
		mBinaryResource.post("oic.r.switch.binary", OcPlatform.DEFAULT_INTERFACE, binarySwitch, query, this);

	}

	@Override
	public void onPostCompleted(List<OcHeaderOption> header, OcRepresentation rep) {
		System.out.println("Success!");
	}

	@Override
	public void onPostFailed(Throwable arg0) {
		System.out.println("Something went wrong on: Trying to change binarySwitch rep");
	}

}
