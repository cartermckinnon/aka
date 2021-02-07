package mck.service.urlalias.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mck.service.urlalias.storage.memory.MemoryUrlAliasStorageFactory;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UrlAliasStorageFactoryDeserializer {

  private Object factory = new MemoryUrlAliasStorageFactory();
  private String factoryClass = "mck.service.urlalias.storage.memory.MemoryUrlAliasStorageFactory";

  public UrlAliasStorageFactory deserialize(ObjectMapper mapper)
      throws IllegalArgumentException, ClassNotFoundException {
    return (UrlAliasStorageFactory) mapper.convertValue(factory, Class.forName(factoryClass));
  }
}
