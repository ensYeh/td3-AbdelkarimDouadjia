package fr.uvsq.cprog.collex;

/**
 * Command to search for an IP address by machine name.
 */
public class RechercheNomCommande implements Commande {
  private final Dns dns;
  private final NomMachine nomMachine;

  /**
   * Creates a command to search by machine name.
   *
   * @param dns the DNS database
   * @param nomMachine the machine name to search for
   */
  public RechercheNomCommande(Dns dns, NomMachine nomMachine) {
    this.dns = dns;
    this.nomMachine = nomMachine;
  }

  @Override
  public Object execute() {
    DnsItem item = dns.getItem(nomMachine);
    if (item == null) {
      return "Aucune adresse IP trouvée pour : " + nomMachine;
    }
    return item.getAdresseIp().getAdresse();
  }
}
