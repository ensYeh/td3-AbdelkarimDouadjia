package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for DnsItem class.
 */
public class DnsItemTest {

  @Test
  public void testValidDnsItem() {
    AdresseIP ip = new AdresseIP("193.51.31.90");
    NomMachine nom = new NomMachine("www.uvsq.fr");
    DnsItem item = new DnsItem(ip, nom);

    assertEquals(ip, item.getAdresseIp());
    assertEquals(nom, item.getNomMachine());
  }

  @Test
  public void testToString() {
    AdresseIP ip = new AdresseIP("193.51.31.90");
    NomMachine nom = new NomMachine("www.uvsq.fr");
    DnsItem item = new DnsItem(ip, nom);

    assertEquals("193.51.31.90 www.uvsq.fr", item.toString());
  }

  @Test
  public void testInvalidDnsItemNullIp() {
    NomMachine nom = new NomMachine("www.uvsq.fr");
    assertThrows(IllegalArgumentException.class, () -> {
      new DnsItem(null, nom);
    });
  }

  @Test
  public void testInvalidDnsItemNullName() {
    AdresseIP ip = new AdresseIP("193.51.31.90");
    assertThrows(IllegalArgumentException.class, () -> {
      new DnsItem(ip, null);
    });
  }

  @Test
  public void testInvalidDnsItemBothNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      new DnsItem(null, null);
    });
  }

  @Test
  public void testEquals() {
    AdresseIP ip1 = new AdresseIP("193.51.31.90");
    NomMachine nom1 = new NomMachine("www.uvsq.fr");
    DnsItem item1 = new DnsItem(ip1, nom1);

    AdresseIP ip2 = new AdresseIP("193.51.31.90");
    NomMachine nom2 = new NomMachine("www.uvsq.fr");
    DnsItem item2 = new DnsItem(ip2, nom2);

    AdresseIP ip3 = new AdresseIP("193.51.25.12");
    NomMachine nom3 = new NomMachine("ecampus.uvsq.fr");
    DnsItem item3 = new DnsItem(ip3, nom3);

    assertEquals(item1, item2);
    assertNotEquals(item1, item3);
    assertNotEquals(item1, null);
    assertNotEquals(item1, "193.51.31.90 www.uvsq.fr");
  }

  @Test
  public void testHashCode() {
    AdresseIP ip1 = new AdresseIP("193.51.31.90");
    NomMachine nom1 = new NomMachine("www.uvsq.fr");
    DnsItem item1 = new DnsItem(ip1, nom1);

    AdresseIP ip2 = new AdresseIP("193.51.31.90");
    NomMachine nom2 = new NomMachine("www.uvsq.fr");
    DnsItem item2 = new DnsItem(ip2, nom2);

    assertEquals(item1.hashCode(), item2.hashCode());
  }
}
