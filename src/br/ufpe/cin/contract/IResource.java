package br.ufpe.cin.contract;

import java.util.List;

import org.iotivity.base.OcException;
import org.iotivity.base.OcRepresentation;
import org.iotivity.base.OcResourceHandle;

public interface IResource {
	void registerResource() throws OcException;
    void unregisterResource() throws OcException;
    OcResourceHandle getResourceHandle();
}
