package mck.service.urlalias.storage.redis;

import java.net.URI;
import java.nio.charset.StandardCharsets;

public class RedisKeys {
  public static final String ALIAS_PATTERN = "alias:*";

  public static final String URL_PATTERN = "url:*";

  public static byte[] alias(byte[] alias) {
    byte[] key = new byte[6 + alias.length];
    key[0] = 'a';
    key[1] = 'l';
    key[2] = 'i';
    key[3] = 'a';
    key[4] = 's';
    key[5] = ':';
    for (int i = 0; i < alias.length; i++) {
      key[i + 6] = alias[i];
    }
    return key;
  }

  public static byte[] alias(String alias) {
    return alias(alias.getBytes(StandardCharsets.UTF_8));
  }

  public static byte[] url(URI url) {
    int size = 5; // 'url:' and ':' for scheme delimiter
    String host = url.getHost();
    if (host != null) {
      size += host.length();
    }
    String path = url.getRawPath();
    if (path != null) {
      size += path.length();
    }
    String scheme = url.getScheme();
    if (scheme != null) {
      size += scheme.length();
    }
    String port = "";
    int portNumber = url.getPort();
    if (portNumber >= 0) {
      port = Integer.toString(portNumber);
      size += port.length() + 1; // extra ':' needed when port is present
    }
    byte[] key = new byte[size];
    key[0] = 'u';
    key[1] = 'r';
    key[2] = 'l';
    key[3] = ':';
    int pos = reverseAppendSplits(key, 4, host, '.');
    key[pos++] = ':';
    pos = append(key, pos, scheme);
    if (portNumber >= 0) {
      key[pos++] = ':';
      pos = append(key, pos, port);
    }
    pos = append(key, pos, path);
    return key;
  }

  public static URI urlFromKey(String key) {
    String reversedUrl = key.substring(4);
    byte[] buf = new byte[reversedUrl.length() + 2];
    int pathBegin = reversedUrl.indexOf('/');
    if (pathBegin == -1) {
      pathBegin = reversedUrl.length();
    }
    int pos = 0;
    String sub = reversedUrl.substring(0, pathBegin);
    String[] splits = sub.split(":");
    // {<reversed host>, <scheme>, <port>}
    pos = append(buf, pos, splits[1]); // add protocol
    pos = append(buf, pos, "://");
    pos = reverseAppendSplits(buf, pos, splits[0], '.'); // splits[0] is a reversed host
    if (splits.length == 3) { // has a port
      buf[pos++] = ':';
      pos = append(buf, pos, splits[2]);
    }
    append(buf, pos, reversedUrl.substring(pathBegin));
    return URI.create(new String(buf, StandardCharsets.UTF_8));
  }

  /**
   * Reverse-append the "parts" of a string delimited by a "split" character. For example, {@code
   * www.example.com} would become {@code com.example.www} for the split character {@code c}.
   *
   * @param buf output buffer
   * @param startOffset offset at which to start writing in output buffer
   * @param s string delimited by a split character; may be null or of zero length.
   * @param split character which delimits the parts of the string.
   * @return the new starting offset for writing in the output buffer; may be unchanged.
   */
  protected static int reverseAppendSplits(byte[] buf, int startOffset, String s, char split) {
    if (s == null || s.isEmpty()) {
      return startOffset;
    }
    char[] chars = s.toCharArray();
    int writePos = startOffset;
    int prevSplit = chars.length;
    int i, j;
    for (i = chars.length - 1; i >= 0; i--) {
      if (chars[i] == split) {
        for (j = i + 1; j < prevSplit; j++) {
          buf[writePos++] = (byte) chars[j];
        }
        buf[writePos++] = '.';
        prevSplit = i;
      }
    }
    for (i = 0; i < prevSplit; i++) {
      buf[writePos++] = (byte) chars[i];
    }
    return writePos;
  }

  protected static int append(byte[] key, int startOffset, String s) {
    if (s == null) {
      return startOffset;
    }
    int pos = startOffset;
    for (char c : s.toCharArray()) {
      key[pos++] = (byte) c;
    }
    return pos;
  }
}
