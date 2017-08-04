package br.ufpe.cin.contract;

public interface IResource {
	void registerResource() throws OcException;
    void unregisterResource() throws OcException;
}
