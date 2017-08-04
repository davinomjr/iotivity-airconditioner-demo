package br.ufpe.cin.resource;

import br.ufpe.cin.model.BinarySwitch;
import br.ufpe.cin.model.OCRepresentation;

public class BinarySwitchResource extends Resource implements IResource {
	
    private final static String mResourceName = "Binary Switch";
    private OcResourceHandle mHandle;
	private BinarySwitch mBinarySwitch;
	private OcResourceHandle mHandle;
	
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
        mBinarySwitch.toggleBinarySwitch();
        return sendResponse(response);
	}
	
	@Override
    public EntityHandlerResult init() {
    	mBinarySwitch.setValue(true);
        return EntityHandlerResult.OK;
    }
}
