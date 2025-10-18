package fr.uvsq.cprog.collex;

import java.util.Objects;

/**
 * Represents a qualified machine name (e.g., www.uvsq.fr).
 * A qualified name consists of a machine name (before first '.') and a domain name (after first
 * '.').
 */
public class NomMachine {
  private final String nomQualifie;

  /**
   * Creates a qualified machine name.
   *
   * @param nomQualifie the qualified name (e.g., "www.uvsq.fr")
   * @throws IllegalArgumentException if the name is null, empty, or doesn't contain a domain
   */
  public NomMachine(String nomQualifie) {
    if (nomQualifie == null || nomQualifie.trim().isEmpty()) {
      throw new IllegalArgumentException("Le nom de machine ne peut pas être vide");
    }
    if (!nomQualifie.contains(".")) {
      throw new IllegalArgumentException(
          "Le nom doit être qualifié (contenir au moins un point) : " + nomQualifie);
    }
    this.nomQualifie = nomQualifie.trim();
  }

  /**
   * Returns the machine name (part before the first '.').
   *
   * @return the machine name
   */
  public String getNomMachine() {
    int index = nomQualifie.indexOf('.');
    return nomQualifie.substring(0, index);
  }

  /**
   * Returns the domain name (part after the first '.').
   *
   * @return the domain name
   */
  public String getNomDomaine() {
    int index = nomQualifie.indexOf('.');
    return nomQualifie.substring(index + 1);
  }

  /**
   * Returns the fully qualified name.
   *
   * @return the qualified name
   */
  public String getNomQualifie() {
    return nomQualifie;
  }

  @Override
  public String toString() {
    return nomQualifie;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    NomMachine other = (NomMachine) obj;
    return Objects.equals(nomQualifie, other.nomQualifie);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nomQualifie);
  }
}
