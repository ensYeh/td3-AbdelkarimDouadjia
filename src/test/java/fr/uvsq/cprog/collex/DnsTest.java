package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for Dns class.
 */
public class DnsTest {
  private Path tempFile;
  private Dns dns;

  /**
   * Setup method to create a temporary database file before each test.
   *
   * @throws Exception if setup fails
   */
  @Before
  public void setUp() throws Exception {
    tempFile = Files.createTempFile("dns-test", ".txt");
    List<String> lines = List.of(
        "www.uvsq.fr 193.51.31.90",
        "ecampus.uvsq.fr 193.51.25.12",
        "poste.uvsq.fr 193.51.31.154"
    );
    Files.write(tempFile, lines);
    dns = new Dns(tempFile);
  }

  /**
   * Teardown method to delete temporary file after each test.
   *
   * @throws Exception if cleanup fails
   */
  @After
  public void tearDown() throws Exception {
    if (tempFile != null && Files.exists(tempFile)) {
      Files.delete(tempFile);
    }
  }

  @Test
  public void testLoadDatabase() throws DnsException {
    assertNotNull(dns);
  }

  @Test
  public void testGetItemByIp() throws DnsException {
    AdresseIP ip = new AdresseIP("193.51.31.90");
    DnsItem item = dns.getItem(ip);

    assertNotNull(item);
    assertEquals(ip, item.getAdresseIp());
    assertEquals("www.uvsq.fr", item.getNomMachine().getNomQualifie());
  }

  @Test
  public void testGetItemByName() throws DnsException {
    NomMachine nom = new NomMachine("ecampus.uvsq.fr");
    DnsItem item = dns.getItem(nom);

    assertNotNull(item);
    assertEquals(nom, item.getNomMachine());
    assertEquals("193.51.25.12", item.getAdresseIp().getAdresse());
  }

  @Test
  public void testGetItemByIpNotFound() throws DnsException {
    AdresseIP ip = new AdresseIP("1.2.3.4");
    DnsItem item = dns.getItem(ip);

    assertNull(item);
  }

  @Test
  public void testGetItemByNameNotFound() throws DnsException {
    NomMachine nom = new NomMachine("unknown.domain.fr");
    DnsItem item = dns.getItem(nom);

    assertNull(item);
  }

  @Test
  public void testGetItemsByDomain() throws DnsException {
    List<DnsItem> items = dns.getItems("uvsq.fr");

    assertEquals(3, items.size());
    // Should be sorted by machine name
    assertEquals("ecampus", items.get(0).getNomMachine().getNomMachine());
    assertEquals("poste", items.get(1).getNomMachine().getNomMachine());
    assertEquals("www", items.get(2).getNomMachine().getNomMachine());
  }

  @Test
  public void testGetItemsByDomainEmpty() throws DnsException {
    List<DnsItem> items = dns.getItems("unknown.fr");

    assertTrue(items.isEmpty());
  }

  @Test
  public void testGetItemsSortedByIp() throws DnsException {
    List<DnsItem> items = dns.getItemsSortedByIp("uvsq.fr");

    assertEquals(3, items.size());
    // Should be sorted by IP address
    assertEquals("193.51.25.12", items.get(0).getAdresseIp().getAdresse());
    assertEquals("193.51.31.154", items.get(1).getAdresseIp().getAdresse());
    assertEquals("193.51.31.90", items.get(2).getAdresseIp().getAdresse());
  }

  @Test
  public void testAddItem() throws DnsException, IOException {
    AdresseIP newIp = new AdresseIP("193.51.25.24");
    NomMachine newNom = new NomMachine("pikachu.uvsq.fr");

    dns.addItem(newIp, newNom);

    // Verify item was added
    DnsItem item = dns.getItem(newIp);
    assertNotNull(item);
    assertEquals(newIp, item.getAdresseIp());
    assertEquals(newNom, item.getNomMachine());

    // Verify file was updated
    List<String> lines = Files.readAllLines(tempFile);
    assertTrue(lines.stream().anyMatch(line -> line.contains("pikachu.uvsq.fr")));
  }

  @Test
  public void testAddItemDuplicateIp() throws DnsException {
    AdresseIP existingIp = new AdresseIP("193.51.31.90");
    NomMachine newNom = new NomMachine("duplicate.uvsq.fr");

    DnsException exception = assertThrows(DnsException.class, () -> {
      dns.addItem(existingIp, newNom);
    });

    assertTrue(exception.getMessage().contains("existe déjà"));
  }

  @Test
  public void testAddItemDuplicateName() throws DnsException {
    AdresseIP newIp = new AdresseIP("1.2.3.4");
    NomMachine existingNom = new NomMachine("www.uvsq.fr");

    DnsException exception = assertThrows(DnsException.class, () -> {
      dns.addItem(newIp, existingNom);
    });

    assertTrue(exception.getMessage().contains("existe déjà"));
  }

  @Test
  public void testLoadEmptyDatabase() throws DnsException, IOException {
    Path emptyFile = Files.createTempFile("dns-empty", ".txt");
    try {
      Dns emptyDns = new Dns(emptyFile);
      assertNotNull(emptyDns);

      List<DnsItem> items = emptyDns.getItems("any.domain");
      assertTrue(items.isEmpty());
    } finally {
      Files.deleteIfExists(emptyFile);
    }
  }

  @Test
  public void testLoadDatabaseWithComments() throws DnsException, IOException {
    Path commentFile = Files.createTempFile("dns-comment", ".txt");
    try {
      List<String> lines = List.of(
          "# This is a comment",
          "test.domain.fr 10.0.0.1",
          "",
          "# Another comment",
          "test2.domain.fr 10.0.0.2"
      );
      Files.write(commentFile, lines);

      Dns commentDns = new Dns(commentFile);
      List<DnsItem> items = commentDns.getItems("domain.fr");

      assertEquals(2, items.size());
    } finally {
      Files.deleteIfExists(commentFile);
    }
  }
}
