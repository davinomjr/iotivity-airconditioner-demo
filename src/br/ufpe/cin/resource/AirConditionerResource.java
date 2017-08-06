package br.ufpe.cin.resource;

import java.util.EnumSet;
import java.util.List;

import org.iotivity.base.EntityHandlerResult;
import org.iotivity.base.OcException;
import org.iotivity.base.OcPlatform;
import org.iotivity.base.OcResourceRequest;
import org.iotivity.base.OcResourceResponse;
import org.iotivity.base.PayloadType;
import org.iotivity.base.ResourceProperty;

import br.ufpe.cin.contract.IResource;
import br.ufpe.cin.model.AirConditioner;

public class AirConditionerResource extends Resource implements IResource {

	private final static String mResourceName = "Air Conditioner";
	private AirConditioner mAirConditioner;

	public AirConditionerResource(String uri, List<String> resourceTypes, List<String> resourceInterfaces,
			AirConditioner airConditioner) {
		super(uri, resourceTypes, resourceInterfaces, mResourceName);
		this.mAirConditioner = airConditioner;
	}

	public void registerResource() throws OcException {
		if (mHandle == null) {
			mHandle = OcPlatform.registerResource(mRepresentation.getUri(), "aircon", OcPlatform.DEFAULT_INTERFACE,
					this, EnumSet.of(ResourceProperty.DISCOVERABLE, ResourceProperty.OBSERVABLE));
		}
	}

	public void unregisterResource() throws OcException {
		if (mHandle != null) {
			OcPlatform.unregisterResource(mHandle);
		}
	}

	public void setDeviceInfo() throws OcException {
		OcPlatform.bindTypeToResource(mHandle, "oic.d.airconditioner");
		OcPlatform.setPropertyValue(PayloadType.DEVICE.getValue(), "OC_RSRVD_DEVICE_NAME", "FAC_2016");
		OcPlatform.setPropertyValue(PayloadType.DEVICE.getValue(), "OC_RSRVD_PROTOCOL_INDEPENDENT_ID","d7d2b492-83ac-4783-9dcc-b1b54587ebed");
	}

	@Override
	protected EntityHandlerResult getResourceRepresentation(OcResourceRequest request) throws OcException {
		OcResourceResponse response = new OcResourceResponse();
		response.setRequestHandle(request.getRequestHandle());
		response.setResourceHandle(request.getResourceHandle());
		response.setResponseResult(EntityHandlerResult.OK);
		response.setResourceRepresentation(mAirConditioner.serialize());
		return sendResponse(response);
	}

	@Override
	protected EntityHandlerResult setResourceRepresentation(OcResourceRequest request) {
		OcResourceResponse response = new OcResourceResponse();
		response.setRequestHandle(request.getRequestHandle());
		response.setResourceHandle(request.getResourceHandle());
		mAirConditioner.toggleMode();
		response.setResponseResult(EntityHandlerResult.OK);
		return sendResponse(response);
	}

	@Override
	protected EntityHandlerResult init() {
		mAirConditioner.turnOn();
		return EntityHandlerResult.OK;
	}
}
