package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for all command classes.
 */
public class CommandeTest {
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
  public void testRechercheIpCommandeFound() {
    AdresseIP ip = new AdresseIP("193.51.31.90");
    RechercheIpCommande cmd = new RechercheIpCommande(dns, ip);
    Object result = cmd.execute();

    assertEquals("www.uvsq.fr", result);
  }

  @Test
  public void testRechercheIpCommandeNotFound() {
    AdresseIP ip = new AdresseIP("1.2.3.4");
    RechercheIpCommande cmd = new RechercheIpCommande(dns, ip);
    Object result = cmd.execute();

    assertTrue(result.toString().contains("Aucune machine trouvée"));
  }

  @Test
  public void testRechercheNomCommandeFound() {
    NomMachine nom = new NomMachine("ecampus.uvsq.fr");
    RechercheNomCommande cmd = new RechercheNomCommande(dns, nom);
    Object result = cmd.execute();

    assertEquals("193.51.25.12", result);
  }

  @Test
  public void testRechercheNomCommandeNotFound() {
    NomMachine nom = new NomMachine("unknown.domain.fr");
    RechercheNomCommande cmd = new RechercheNomCommande(dns, nom);
    Object result = cmd.execute();

    assertTrue(result.toString().contains("Aucune adresse IP trouvée"));
  }

  @Test
  public void testListeDomaineCommandeSortByName() {
    ListeDomaineCommande cmd = new ListeDomaineCommande(dns, "uvsq.fr", false);
    Object result = cmd.execute();

    String output = result.toString();
    assertTrue(output.contains("ecampus.uvsq.fr"));
    assertTrue(output.contains("poste.uvsq.fr"));
    assertTrue(output.contains("www.uvsq.fr"));

    // Verify order (sorted by machine name: ecampus, poste, www)
    int ecampusIndex = output.indexOf("ecampus");
    int posteIndex = output.indexOf("poste");
    int wwwIndex = output.indexOf("www");
    assertTrue(ecampusIndex < posteIndex);
    assertTrue(posteIndex < wwwIndex);
  }

  @Test
  public void testListeDomaineCommandeSortByIp() {
    ListeDomaineCommande cmd = new ListeDomaineCommande(dns, "uvsq.fr", true);
    Object result = cmd.execute();

    String output = result.toString();
    // Verify order (sorted by IP: 193.51.25.12, 193.51.31.154, 193.51.31.90)
    int ip12Index = output.indexOf("193.51.25.12");
    int ip154Index = output.indexOf("193.51.31.154");
    int ip90Index = output.indexOf("193.51.31.90");
    assertTrue(ip12Index < ip154Index);
    assertTrue(ip154Index < ip90Index);
  }

  @Test
  public void testListeDomaineCommandeNotFound() {
    ListeDomaineCommande cmd = new ListeDomaineCommande(dns, "unknown.fr", false);
    Object result = cmd.execute();

    assertTrue(result.toString().contains("Aucune machine trouvée"));
  }

  @Test
  public void testAjoutCommandeSuccess() {
    AdresseIP newIp = new AdresseIP("193.51.25.24");
    NomMachine newNom = new NomMachine("pikachu.uvsq.fr");
    AjoutCommande cmd = new AjoutCommande(dns, newIp, newNom);
    Object result = cmd.execute();

    assertTrue(result.toString().contains("Entrée DNS ajoutée"));
    
    // Verify item was added
    DnsItem item = dns.getItem(newIp);
    assertEquals(newIp, item.getAdresseIp());
    assertEquals(newNom, item.getNomMachine());
  }

  @Test
  public void testAjoutCommandeDuplicateIp() {
    AdresseIP existingIp = new AdresseIP("193.51.31.90");
    NomMachine newNom = new NomMachine("duplicate.uvsq.fr");
    AjoutCommande cmd = new AjoutCommande(dns, existingIp, newNom);
    Object result = cmd.execute();

    assertTrue(result.toString().contains("ERREUR"));
    assertTrue(result.toString().contains("existe déjà"));
  }

  @Test
  public void testAjoutCommandeDuplicateName() {
    AdresseIP newIp = new AdresseIP("1.2.3.4");
    NomMachine existingNom = new NomMachine("www.uvsq.fr");
    AjoutCommande cmd = new AjoutCommande(dns, newIp, existingNom);
    Object result = cmd.execute();

    assertTrue(result.toString().contains("ERREUR"));
    assertTrue(result.toString().contains("existe déjà"));
  }

  @Test
  public void testQuitterCommande() {
    QuitterCommande cmd = new QuitterCommande();
    Object result = cmd.execute();

    assertEquals("quit", result);
  }
}
