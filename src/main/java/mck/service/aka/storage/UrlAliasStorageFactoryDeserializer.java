package mck.service.aka.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mck.service.aka.storage.memory.MemoryUrlAliasStorageFactory;

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
 * factoryClass}. In this sense, it does not "deserialize" the object, it converts it to a type
 * defined at runtime.
 *
 * <p>By default, {@link MemoryUrlAliasStorageFactory} is returned by {@link
 * #deserialize(ObjectMapper)}.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UrlAliasStorageFactoryDeserializer {

  private String factoryClass = MemoryUrlAliasStorageFactory.class.getName();
  private Object factory = new MemoryUrlAliasStorageFactory();

  public UrlAliasStorageFactoryDeserializer() {}

  public UrlAliasStorageFactoryDeserializer(String factoryClass, Object factory) {
    this.factoryClass = factoryClass;
    this.factory = factory;
  }

  public UrlAliasStorageFactory deserialize(ObjectMapper mapper)
      throws IllegalArgumentException, ClassNotFoundException {
    return (UrlAliasStorageFactory) mapper.convertValue(factory, Class.forName(factoryClass));
  }
}
