package mck.service.aka.storage;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.util.Resources;
import org.junit.jupiter.api.Test;

public class UrlAliasStorageFactoryDeserializerTest {
  @Test
  public void deserialize_fromYaml() throws Exception {
    byte[] yaml =
        Resources.toByteArray(
            Resources.getResource("storage/test-storage-factory-deserializer.yaml"));
    ObjectMapper mapper = Jackson.newObjectMapper(new YAMLFactory());
    UrlAliasStorageFactoryDeserializer deserializer =
        mapper.readValue(yaml, UrlAliasStorageFactoryDeserializer.class);
    assertThat(deserializer.getFactoryClass())
        .isEqualTo(TestUrlAliasStorageFactory.class.getName());
    UrlAliasStorageFactory factory = deserializer.deserialize(mapper);
    assertThat(factory).isInstanceOf(TestUrlAliasStorageFactory.class);
    TestUrlAliasStorageFactory testFactory = (TestUrlAliasStorageFactory) factory;
    assertThat(testFactory.getFoo()).isEqualTo("foo");
    assertThat(testFactory.getBar()).isEqualTo("bar");
    assertThat(testFactory.build(null)).isNull();
  }

  @Test
  public void deserialize_runtimeObject() throws Exception {
    TestUrlAliasStorageFactory factory = new TestUrlAliasStorageFactory("foo", "bar");
    UrlAliasStorageFactoryDeserializer deserializer =
        new UrlAliasStorageFactoryDeserializer(factory.getClass().getName(), factory);
    assertThat(deserializer.getFactoryClass())
        .isEqualTo(TestUrlAliasStorageFactory.class.getName());
    UrlAliasStorageFactory deserializedFactory =
        deserializer.deserialize(Jackson.newObjectMapper());
    assertThat(deserializedFactory).isInstanceOf(TestUrlAliasStorageFactory.class);
    assertThat(deserializedFactory).isEqualTo(factory);
    assertThat(deserializedFactory).hasSameHashCodeAs(factory);

    TestUrlAliasStorageFactory castFactory = (TestUrlAliasStorageFactory) deserializedFactory;
    assertThat(castFactory.getFoo()).isEqualTo("foo");
    assertThat(castFactory.getBar()).isEqualTo("bar");
    assertThat(castFactory.build(null)).isNull();
  }
}
