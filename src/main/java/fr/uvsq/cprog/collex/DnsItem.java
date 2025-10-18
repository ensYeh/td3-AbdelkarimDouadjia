package fr.uvsq.cprog.collex;

import java.util.Objects;

/**
 * Represents a DNS entry pairing an IP address with a qualified machine name.
 */
public class DnsItem {
  private final AdresseIP adresseIp;
  private final NomMachine nomMachine;

  /**
   * Creates a DNS entry.
   *
   * @param adresseIp the IP address
   * @param nomMachine the qualified machine name
   * @throws IllegalArgumentException if either parameter is null
   */
  public DnsItem(AdresseIP adresseIp, NomMachine nomMachine) {
    if (adresseIp == null || nomMachine == null) {
      throw new IllegalArgumentException(
          "L'adresse IP et le nom de machine ne peuvent pas être nuls");
    }
    this.adresseIp = adresseIp;
    this.nomMachine = nomMachine;
  }

  /**
   * Returns the IP address.
   *
   * @return the IP address
   */
  public AdresseIP getAdresseIp() {
    return adresseIp;
  }

  /**
   * Returns the qualified machine name.
   *
   * @return the machine name
   */
  public NomMachine getNomMachine() {
    return nomMachine;
  }

  @Override
  public String toString() {
    return adresseIp + " " + nomMachine;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    DnsItem other = (DnsItem) obj;
    return Objects.equals(adresseIp, other.adresseIp)
        && Objects.equals(nomMachine, other.nomMachine);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adresseIp, nomMachine);
  }
}
