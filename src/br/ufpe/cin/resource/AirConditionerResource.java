package br.ufpe.cin.resource;

public class AirConditionerResource extends Resource implements IResource {

	    private final static String mResourceName = "Air Conditioner";
		private AirConditioner mAirConditioner;
		private OcResourceHandle mHandle;

		public AirConditionerResource(String uri, List<String> resourceTypes, List<String> resourceInterfaces, AirConditioner airConditioner) {
			super(uri, resourceTypes, resourceInterfaces, mResourceName);
			this.mAirConditioner = airConditioner;
		}
		
		public void registerResource() throws OcException {
	        if (mHandle == null) {
	            mHandle = OcPlatform.registerResource(
	            		mRepresentation.getUri(),
	                    "aircon",
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
	    protected EntityHandlerResult getResourceRepresentation(OcResourceRequest request) {
	        OcResourceResponse response = new OcResourceResponse();
	        response.setRequestHandle(request.getRequestHandle());
	        response.setResourceHandle(request.getResourceHandle());
	        response.setErrorCode(200);
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
