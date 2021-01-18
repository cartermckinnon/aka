package mck.service.urlalias.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** @author Carter McKinnon {@literal <cartermckinnon@gmail.com>} */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SetUrlAliasRequest {
  @NotNull public URI url;
  @NotNull @NotBlank public String alias;

  @JsonCreator
  public SetUrlAliasRequest(@JsonProperty("url") URI url, @JsonProperty("alias") String alias) {
    this.url = url;
    this.alias = alias;
  }
}
