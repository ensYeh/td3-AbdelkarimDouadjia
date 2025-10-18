package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for NomMachine class.
 */
public class NomMachineTest {

  @Test
  public void testValidQualifiedName() {
    NomMachine nom = new NomMachine("www.uvsq.fr");
    assertEquals("www.uvsq.fr", nom.getNomQualifie());
    assertEquals("www", nom.getNomMachine());
    assertEquals("uvsq.fr", nom.getNomDomaine());
  }

  @Test
  public void testValidQualifiedNameWithMultipleDots() {
    NomMachine nom = new NomMachine("machine.sous.domaine.fr");
    assertEquals("machine", nom.getNomMachine());
    assertEquals("sous.domaine.fr", nom.getNomDomaine());
  }

  @Test
  public void testValidQualifiedNameSimple() {
    NomMachine nom = new NomMachine("ecampus.uvsq.fr");
    assertEquals("ecampus", nom.getNomMachine());
    assertEquals("uvsq.fr", nom.getNomDomaine());
  }

  @Test
  public void testInvalidQualifiedNameNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      new NomMachine(null);
    });
  }

  @Test
  public void testInvalidQualifiedNameEmpty() {
    assertThrows(IllegalArgumentException.class, () -> {
      new NomMachine("");
    });
  }

  @Test
  public void testInvalidQualifiedNameWhitespace() {
    assertThrows(IllegalArgumentException.class, () -> {
      new NomMachine("   ");
    });
  }

  @Test
  public void testInvalidQualifiedNameNoDot() {
    assertThrows(IllegalArgumentException.class, () -> {
      new NomMachine("machine");
    });
  }

  @Test
  public void testToString() {
    NomMachine nom = new NomMachine("www.uvsq.fr");
    assertEquals("www.uvsq.fr", nom.toString());
  }

  @Test
  public void testEquals() {
    NomMachine nom1 = new NomMachine("www.uvsq.fr");
    NomMachine nom2 = new NomMachine("www.uvsq.fr");
    NomMachine nom3 = new NomMachine("ecampus.uvsq.fr");

    assertEquals(nom1, nom2);
    assertNotEquals(nom1, nom3);
    assertNotEquals(nom1, null);
    assertNotEquals(nom1, "www.uvsq.fr");
  }

  @Test
  public void testHashCode() {
    NomMachine nom1 = new NomMachine("www.uvsq.fr");
    NomMachine nom2 = new NomMachine("www.uvsq.fr");

    assertEquals(nom1.hashCode(), nom2.hashCode());
  }

  @Test
  public void testTrimmedName() {
    NomMachine nom = new NomMachine("  www.uvsq.fr  ");
    assertEquals("www.uvsq.fr", nom.getNomQualifie());
    assertEquals("www", nom.getNomMachine());
  }
}
