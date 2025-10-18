package fr.uvsq.cprog.collex;

/**
 * Command to search for a machine name by IP address.
 */
public class RechercheIpCommande implements Commande {
  private final Dns dns;
  private final AdresseIP adresseIp;

  /**
   * Creates a command to search by IP address.
   *
   * @param dns the DNS database
   * @param adresseIp the IP address to search for
   */
  public RechercheIpCommande(Dns dns, AdresseIP adresseIp) {
    this.dns = dns;
    this.adresseIp = adresseIp;
  }

  @Override
  public Object execute() {
    DnsItem item = dns.getItem(adresseIp);
    if (item == null) {
      return "Aucune machine trouvée pour l'adresse IP : " + adresseIp;
    }
    return item.getNomMachine().getNomQualifie();
  }
}
