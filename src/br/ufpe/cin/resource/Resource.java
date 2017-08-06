package br.ufpe.cin.resource;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.iotivity.base.EntityHandlerResult;
import org.iotivity.base.OcException;
import org.iotivity.base.OcPlatform;
import org.iotivity.base.OcRepresentation;
import org.iotivity.base.OcResourceHandle;
import org.iotivity.base.OcResourceRequest;
import org.iotivity.base.OcResourceResponse;
import org.iotivity.base.RequestHandlerFlag;
import org.iotivity.base.RequestType;

import br.ufpe.cin.contract.IResource;

public abstract class Resource implements OcPlatform.EntityHandler, IResource {
	
    protected String mResourceName;
    protected OcResourceHandle mHandle;
    protected OcRepresentation mRepresentation;
    protected List<Resource> mChildResources;

    
    public OcRepresentation getRepresentation() {
		return mRepresentation;
	}
	public void setRepresentation(OcRepresentation mRepresentation) {
		this.mRepresentation = mRepresentation;
	}
	protected abstract EntityHandlerResult init();
    protected abstract EntityHandlerResult getResourceRepresentation(OcResourceRequest request) throws OcException;
    protected abstract EntityHandlerResult setResourceRepresentation(OcResourceRequest request) throws OcException;
    

    public Resource(String uri, List<String> resourceTypes, List<String> resourceInterfaces, String resourceName) {
    	this.mResourceName = resourceName;
    	this.mChildResources = new ArrayList<Resource>();
        mRepresentation = new OcRepresentation();
        mRepresentation.setResourceTypes(resourceTypes);
        mRepresentation.setResourceTypes(resourceInterfaces);
        mRepresentation.setUri(uri);
    }
    
   
    @Override
    public EntityHandlerResult handleEntity(OcResourceRequest ocResourceRequest) {
    	System.out.println(String.format("Server %s entity handler", mResourceName));	
        EntityHandlerResult ehResult = EntityHandlerResult.ERROR;
        if (ocResourceRequest == null) {
            return ehResult;
        }
        
        EnumSet<RequestHandlerFlag> requestFlags = ocResourceRequest.getRequestHandlerFlagSet();
        	       
        if (requestFlags.contains(RequestHandlerFlag.INIT)) {
            ehResult = this.init();
        }
        
        if (requestFlags.contains(RequestHandlerFlag.REQUEST)) {
            try {
				ehResult = handleRequest(ocResourceRequest);
			} catch (OcException e) {
				e.printStackTrace();
				System.out.println("Error on request");
			}
        }

        return ehResult;
    }
    
    
    public OcResourceHandle getResourceHandle() {
    	return this.mHandle;
    }

    
    protected EntityHandlerResult sendResponse(OcResourceResponse response) {
        try {
            OcPlatform.sendResponse(response);
            return EntityHandlerResult.OK;
        } catch (OcException e) {
            return EntityHandlerResult.ERROR;
        }
    }

    
    protected EntityHandlerResult handleRequest(OcResourceRequest ocResourceRequest) throws OcException {
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
    
    public void addChildResource(Resource resource) {
    	mChildResources.add(resource);
    }
}