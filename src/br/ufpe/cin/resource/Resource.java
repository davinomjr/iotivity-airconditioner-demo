package br.ufpe.cin.resource;

import java.util.List;

import br.ufpe.cin.xa;
import br.ufpe.cin.contract.EntityHandlerResult;

public abstract class Resource implements OcPlatform.EntityHandler {
	
    private OcResourceHandle mHandle;
    private String mResourceName;
    protected OcRepresentation mRepresentation;
    protected List<Resource> mChildResources;

    
    protected abstract void init();
    protected abstract EntityHandlerResult getResourceRepresentation(OcResourceRequest request);
    protected abstract EntityHandlerResult setResourceRepresentation(OcResourceRequest request);
    

    public Resource(String uri, List<String> resourceTypes, List<String> resourceInterfaces, String resourceName) {
    	this.mResourceName = resourceName;
        mRepresentation = new OcRepresentation();
        mRepresentation.setResourceTypes(resourceTypes);
        mRepresentation.setResourceTypes(resourceInterfaces);
        mRepresentation.setUri(uri);
    }
    
   
    @Override
    public EntityHandlerResult handleEntity(OcResourceRequest ocResourceRequest) {
    	System.out.println(String.format("Server %s entity handler", mResourceName);	
        EntityHandlerResult ehResult = EntityHandlerResult.ERROR;
        if (ocResourceRequest == null) {
            return ehResult;
        }
        
        EnumSet<RequestHandlerFlag> requestFlags = ocResourceRequest.getRequestHandlerFlagSet();
        	       
        if (requestFlags.contains(RequestHandlerFlag.INIT)) {
            ehResult = this.init();
        }
        
        if (requestFlags.contains(RequestHandlerFlag.REQUEST)) {
            ehResult = handleRequest(ocResourceRequest);
        }

        return ehResult;
    }

    
    protected EntityHandlerResult sendResponse(OcResourceResponse response) {
        try {
            OcPlatform.sendResponse(response);
            return EntityHandlerResult.OK;
        } catch (OcException e) {
            Log.e("Error sending response:", e.toString());
            return EntityHandlerResult.ERROR;
        }
    }

    
    protected EntityHandlerResult handleRequest(OcResourceRequest ocResourceRequest) {
        EntityHandlerResult ehResult = EntityHandlerResult.ERROR;        
        RequestType requestType = ocResourceRequest.getRequestType();
        switch (requestType) {
            case GET:
                ehResult = this.getResourceRepresentation(ocResourceRequest);
                break;
            case PUT:
            case POST:
                ehResult = this.setResourceRepresentation(ocResourceRequest);
                break;
            case DELETE:
            	break;
        }
        return ehResult;
    }
}