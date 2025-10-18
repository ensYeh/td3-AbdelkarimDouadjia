package fr.uvsq.cprog.collex;

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
 * Unit tests for DnsApp class.
 */
public class DnsAppTest {
  private Path tempFile;
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
        "www.uvsq.fr 193.51.31.90"
    );
    Files.write(tempFile, lines);

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
  public void testRunWithQuit() throws DnsException {
    Dns dns = new Dns(tempFile);
    Scanner scanner = new Scanner("quit\n");
    DnsTUI tui = new DnsTUI(scanner, dns);
    DnsApp app = new DnsApp(tui);

    app.run();

    String output = outputStream.toString();
    assertTrue(output.contains("DNS Simulator"));
    assertTrue(output.contains("Au revoir"));
  }

  @Test
  public void testRunWithQuery() throws DnsException {
    Dns dns = new Dns(tempFile);
    Scanner scanner = new Scanner("www.uvsq.fr\nquit\n");
    DnsTUI tui = new DnsTUI(scanner, dns);
    DnsApp app = new DnsApp(tui);

    app.run();

    String output = outputStream.toString();
    assertTrue(output.contains("193.51.31.90"));
  }

  @Test
  public void testRunWithInvalidCommand() throws DnsException {
    Dns dns = new Dns(tempFile);
    Scanner scanner = new Scanner("invalid\nquit\n");
    DnsTUI tui = new DnsTUI(scanner, dns);
    DnsApp app = new DnsApp(tui);

    app.run();

    String output = outputStream.toString();
    assertTrue(output.contains("DNS Simulator"));
  }
}
