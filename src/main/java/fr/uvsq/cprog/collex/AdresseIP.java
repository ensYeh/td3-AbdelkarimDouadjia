package fr.uvsq.cprog.collex;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents an IP address (e.g., 192.168.0.1).
 */
@SuppressWarnings("AbbreviationAsWordInName")
public class AdresseIP {
  private static final Pattern IP_PATTERN = 
      Pattern.compile("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.){3}"
          + "(25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)$");
  
  private final String adresse;

  /**
   * Creates an IP address from a string.
   *
   * @param adresse the IP address string (e.g., "192.168.0.1")
   * @throws IllegalArgumentException if the address format is invalid
   */
  public AdresseIP(String adresse) {
    if (adresse == null || !IP_PATTERN.matcher(adresse).matches()) {
      throw new IllegalArgumentException("Format d'adresse IP invalide : " + adresse);
    }
    this.adresse = adresse;
  }

  /**
   * Returns the IP address as a string.
   *
   * @return the IP address string
   */
  public String getAdresse() {
    return adresse;
  }

  @Override
  public String toString() {
    return adresse;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    AdresseIP other = (AdresseIP) obj;
    return Objects.equals(adresse, other.adresse);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adresse);
  }
}
