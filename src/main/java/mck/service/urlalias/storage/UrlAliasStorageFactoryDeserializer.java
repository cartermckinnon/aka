package mck.service.urlalias.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mck.service.urlalias.storage.memory.MemoryUrlAliasStorageFactory;

/**
 * Deserializes implementations of {@link UrlAliasStorageFactory}.
 *
 * <p>The factory implementation must be deserializable from YAML with Jackson, such as:
 *
 * <pre>
 * ---
 * factoryClass: com.example.MyUrlAliasStorageFactoryImpl
 * factory:
 *   foo: bar
 *   baz: oof
 * </pre>
 *
 * This class will convert the generic, deserialized object into an instance of type {@code
 * factoryClass}.
 *
 * @author Carter McKinnon {@literal <cartermckinnon@gmail.com>}
 */
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
