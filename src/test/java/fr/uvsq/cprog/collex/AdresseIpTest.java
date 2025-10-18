package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for AdresseIP class.
 */
public class AdresseIpTest {

  @Test
  public void testValidIpAddress() {
    AdresseIP ip = new AdresseIP("192.168.0.1");
    assertEquals("192.168.0.1", ip.getAdresse());
    assertEquals("192.168.0.1", ip.toString());
  }

  @Test
  public void testValidIpAddressEdgeCases() {
    new AdresseIP("0.0.0.0");
    new AdresseIP("255.255.255.255");
    new AdresseIP("193.51.31.90");
  }

  @Test
  public void testInvalidIpAddressNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      new AdresseIP(null);
    });
  }

  @Test
  public void testInvalidIpAddressFormat() {
    assertThrows(IllegalArgumentException.class, () -> {
      new AdresseIP("256.1.1.1");
    });
  }

  @Test
  public void testInvalidIpAddressTooFewOctets() {
    assertThrows(IllegalArgumentException.class, () -> {
      new AdresseIP("192.168.0");
    });
  }

  @Test
  public void testInvalidIpAddressTooManyOctets() {
    assertThrows(IllegalArgumentException.class, () -> {
      new AdresseIP("192.168.0.1.1");
    });
  }

  @Test
  public void testInvalidIpAddressNonNumeric() {
    assertThrows(IllegalArgumentException.class, () -> {
      new AdresseIP("abc.def.ghi.jkl");
    });
  }

  @Test
  public void testEquals() {
    AdresseIP ip1 = new AdresseIP("192.168.0.1");
    AdresseIP ip2 = new AdresseIP("192.168.0.1");
    AdresseIP ip3 = new AdresseIP("192.168.0.2");

    assertEquals(ip1, ip2);
    assertNotEquals(ip1, ip3);
    assertNotEquals(ip1, null);
    assertNotEquals(ip1, "192.168.0.1");
  }

  @Test
  public void testHashCode() {
    AdresseIP ip1 = new AdresseIP("192.168.0.1");
    AdresseIP ip2 = new AdresseIP("192.168.0.1");

    assertEquals(ip1.hashCode(), ip2.hashCode());
  }
}
