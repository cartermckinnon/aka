package mck.service.urlalias.storage.memory;

import io.dropwizard.setup.Environment;
import mck.service.urlalias.storage.UrlAliasStorage;
import mck.service.urlalias.storage.UrlAliasStorageFactory;

public class MemoryUrlAliasStorageFactory implements UrlAliasStorageFactory {

    @Override
    public UrlAliasStorage build(Environment e) {
        return new MemoryUrlAliasStorage();
    }
    
}
