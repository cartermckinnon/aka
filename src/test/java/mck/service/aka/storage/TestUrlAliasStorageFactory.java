package mck.service.aka.storage;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.setup.Environment;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** Used by {@link UrlAliasStorageFactoryDeserializerTest}. */
@Getter
@Setter
@EqualsAndHashCode
public class TestUrlAliasStorageFactory implements UrlAliasStorageFactory {
  @JsonProperty("foo")
  private String foo;

  @JsonProperty("bar")
  private String bar;

  public TestUrlAliasStorageFactory(
      @JsonProperty("foo") String foo, @JsonProperty("bar") String bar) {
    this.foo = foo;
    this.bar = bar;
  }

  @Override
  public UrlAliasStorage build(Environment e) {
    return null;
  }
}
