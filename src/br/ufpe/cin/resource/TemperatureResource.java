package br.ufpe.cin.resource;

import java.awt.List;
import java.util.EnumSet;

import br.ufpe.cin.xa;
import br.ufpe.cin.model.ThermoStat;

public class TemperatureResource extends Resource implements IResource  {
	
	private final static String mResourceName = "Temperature";
	private ThermoStat mThermoStat;
	private OcResourceHandle mHandle;
	
	public TemperatureResource(String uri, List<String> resourceTypes, List<String> resourceInterfaces, ThermoStat thermoStat) {
		super(uri, resourceTypes, resourceInterfaces, mResourceName);
		this.mThermoStat = thermoStat;
	}
	
	public void registerResource() throws OcException {
        if (mHandle == null) {
            mHandle = OcPlatform.registerResource(
            		mRepresentation.getUri(),
                    "temperature",
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
	 protected EntityHandlerResult getResourceRepresentation(OcResourceRequest ocResourceRequest) {
		 	OcResourceResponse response = new OcResourceResponse();
		 	response.setRequestHandle(request.getRequestHandle());
		 	response.setResourceHandle(request.getResourceHandle());
		 	response.setErrorCode(200);
		 	response.setResponseResult(EntityHandlerResult.OK);
	        response.setResourceRepresentation(mBinarySwitch.serialize());
	        return sendResponse(response);
	 }
	 
	 @Override
	 protected EntityHandlerResult setResourceRepresentation(OCRepresentation representation) {
		OcResourceResponse response = new OcResourceResponse();
 	    response.setRequestHandle(request.getRequestHandle());
 	    response.setResourceHandle(request.getResourceHandle());	    	    	   
        response.setResponseResult(EntityHandlerResult.OK);
        mThermoStat.update(representation);
        return sendResponse(response);
	}
	
	 @Override
	    public EntityHandlerResult init() {
	    	mBinarySwitch.setValue(true);
	        return EntityHandlerResult.OK;
	    }
}
