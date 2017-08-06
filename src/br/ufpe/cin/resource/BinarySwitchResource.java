package br.ufpe.cin.resource;

import java.util.EnumSet;
import java.util.List;

import org.iotivity.base.EntityHandlerResult;
import org.iotivity.base.OcException;
import org.iotivity.base.OcPlatform;
import org.iotivity.base.OcResourceHandle;
import org.iotivity.base.OcResourceRequest;
import org.iotivity.base.OcResourceResponse;
import org.iotivity.base.ResourceProperty;

import br.ufpe.cin.contract.IResource;
import br.ufpe.cin.model.BinarySwitch;

public class BinarySwitchResource extends Resource implements IResource {
	
    private final static String mResourceName = "Binary Switch";
	private BinarySwitch mBinarySwitch;
	
	public BinarySwitchResource(String uri, List<String> resourceTypes, List<String> resourceInterfaces, BinarySwitch binarySwitch) {
		super(uri, resourceTypes, resourceInterfaces, mResourceName);
		this.mBinarySwitch = binarySwitch;
	}
	
	public void registerResource() throws OcException {
        if (mHandle == null) {
            mHandle = OcPlatform.registerResource(
            		mRepresentation.getUri(),
                    "switch.binary",
                    OcPlatform.DEFAULT_INTERFACE,
                    this,
                    EnumSet.of(ResourceProperty.DISCOVERABLE, ResourceProperty.OBSERVABLE)
            );
        }
    }
	
	   public void unregisterResource() throws OcException {
	        if (mHandle != null) {
	            OcPlatform.unregisterResource(mHandle);
	        }
	    }
	
	 @Override
	 protected EntityHandlerResult getResourceRepresentation(OcResourceRequest request) throws OcException {
		 	OcResourceResponse response = new OcResourceResponse();
		 	response.setRequestHandle(request.getRequestHandle());
		 	response.setResourceHandle(request.getResourceHandle());
		 	response.setResponseResult(EntityHandlerResult.OK);
	        response.setResourceRepresentation(mBinarySwitch.serialize());
	        return sendResponse(response);
	 }
	 
	 @Override
	 protected EntityHandlerResult setResourceRepresentation(OcResourceRequest request) {
		OcResourceResponse response = new OcResourceResponse();
  	    response.setRequestHandle(request.getRequestHandle());
  	    response.setResourceHandle(request.getResourceHandle());	    	    	   
        response.setResponseResult(EntityHandlerResult.OK);
        mBinarySwitch.toggleBinarySwitch();
        return sendResponse(response);
	}
	
	@Override
    public EntityHandlerResult init() {
    	mBinarySwitch.setValue(true);
        return EntityHandlerResult.OK;
    }
}
