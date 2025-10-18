package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for DnsTUI class.
 */
public class DnsTuiTest {
  private Path tempFile;
  private Dns dns;
  private ByteArrayOutputStream outputStream;
  private PrintStream originalOut;

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
        "ecampus.uvsq.fr 193.51.25.12"
    );
    Files.write(tempFile, lines);
    dns = new Dns(tempFile);

    // Capture System.out
    outputStream = new ByteArrayOutputStream();
    originalOut = System.out;
    System.setOut(new PrintStream(outputStream));
  }

  /**
   * Teardown method to delete temporary file and restore System.out after each test.
   *
   * @throws Exception if cleanup fails
   */
  @After
  public void tearDown() throws Exception {
    System.setOut(originalOut);
    if (tempFile != null && Files.exists(tempFile)) {
      Files.delete(tempFile);
    }
  }

  @Test
  public void testNextCommandeIpAddress() {
    Scanner scanner = new Scanner("193.51.31.90");
    DnsTUI tui = new DnsTUI(scanner, dns);
    
    Commande cmd = tui.nextCommande();
    assertNotNull(cmd);
    assertTrue(cmd instanceof RechercheIpCommande);
  }

  @Test
  public void testNextCommandeQualifiedName() {
    Scanner scanner = new Scanner("www.uvsq.fr");
    DnsTUI tui = new DnsTUI(scanner, dns);
    
    Commande cmd = tui.nextCommande();
    assertNotNull(cmd);
    assertTrue(cmd instanceof RechercheNomCommande);
  }

  @Test
  public void testNextCommandeLsSimple() {
    Scanner scanner = new Scanner("ls uvsq.fr");
    DnsTUI tui = new DnsTUI(scanner, dns);
    
    Commande cmd = tui.nextCommande();
    assertNotNull(cmd);
    assertTrue(cmd instanceof ListeDomaineCommande);
  }

  @Test
  public void testNextCommandeLsWithFlag() {
    Scanner scanner = new Scanner("ls -a uvsq.fr");
    DnsTUI tui = new DnsTUI(scanner, dns);
    
    Commande cmd = tui.nextCommande();
    assertNotNull(cmd);
    assertTrue(cmd instanceof ListeDomaineCommande);
  }

  @Test
  public void testNextCommandeAdd() {
    Scanner scanner = new Scanner("add 1.2.3.4 test.domain.fr");
    DnsTUI tui = new DnsTUI(scanner, dns);
    
    Commande cmd = tui.nextCommande();
    assertNotNull(cmd);
    assertTrue(cmd instanceof AjoutCommande);
  }

  @Test
  public void testNextCommandeQuit() {
    Scanner scanner = new Scanner("quit");
    DnsTUI tui = new DnsTUI(scanner, dns);
    
    Commande cmd = tui.nextCommande();
    assertNotNull(cmd);
    assertTrue(cmd instanceof QuitterCommande);
  }

  @Test
  public void testNextCommandeQuitCaseInsensitive() {
    Scanner scanner = new Scanner("QUIT");
    DnsTUI tui = new DnsTUI(scanner, dns);
    
    Commande cmd = tui.nextCommande();
    assertNotNull(cmd);
    assertTrue(cmd instanceof QuitterCommande);
  }

  @Test
  public void testNextCommandeEmptyLine() {
    Scanner scanner = new Scanner("\n");
    DnsTUI tui = new DnsTUI(scanner, dns);
    
    Commande cmd = tui.nextCommande();
    assertNull(cmd);
  }

  @Test
  public void testNextCommandeInvalidCommand() {
    Scanner scanner = new Scanner("invalid");
    DnsTUI tui = new DnsTUI(scanner, dns);
    
    Commande cmd = tui.nextCommande();
    assertNull(cmd);
    assertTrue(outputStream.toString().contains("ERREUR"));
  }

  @Test
  public void testNextCommandeInvalidLsUsage() {
    Scanner scanner = new Scanner("ls");
    DnsTUI tui = new DnsTUI(scanner, dns);
    
    Commande cmd = tui.nextCommande();
    assertNull(cmd);
    assertTrue(outputStream.toString().contains("Usage"));
  }

  @Test
  public void testNextCommandeInvalidAddUsage() {
    Scanner scanner = new Scanner("add 1.2.3.4");
    DnsTUI tui = new DnsTUI(scanner, dns);
    
    Commande cmd = tui.nextCommande();
    assertNull(cmd);
    assertTrue(outputStream.toString().contains("Usage"));
  }

  @Test
  public void testAffiche() {
    Scanner scanner = new Scanner("");
    DnsTUI tui = new DnsTUI(scanner, dns);
    
    tui.affiche("Test output");
    String output = outputStream.toString();
    assertTrue(output.contains("Test output"));
  }

  @Test
  public void testAfficheNull() {
    Scanner scanner = new Scanner("");
    DnsTUI tui = new DnsTUI(scanner, dns);
    
    tui.affiche(null);
    assertEquals("", outputStream.toString());
  }

  @Test
  public void testNextCommandeNoInput() {
    Scanner scanner = new Scanner("");
    DnsTUI tui = new DnsTUI(scanner, dns);
    
    Commande cmd = tui.nextCommande();
    assertNull(cmd);
  }
}
