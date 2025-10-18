package fr.uvsq.cprog.collex;

/**
 * Command to add a new DNS entry.
 */
public class AjoutCommande implements Commande {
  private final Dns dns;
  private final AdresseIP adresseIp;
  private final NomMachine nomMachine;

  /**
   * Creates a command to add a DNS entry.
   *
   * @param dns the DNS database
   * @param adresseIp the IP address
   * @param nomMachine the machine name
   */
  public AjoutCommande(Dns dns, AdresseIP adresseIp, NomMachine nomMachine) {
    this.dns = dns;
    this.adresseIp = adresseIp;
    this.nomMachine = nomMachine;
  }

  @Override
  public Object execute() {
    try {
      dns.addItem(adresseIp, nomMachine);
      return "Entrée DNS ajoutée : " + adresseIp + " " + nomMachine;
    } catch (DnsException e) {
      return "ERREUR : " + e.getMessage();
    }
  }
}
