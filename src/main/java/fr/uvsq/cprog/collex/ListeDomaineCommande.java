package fr.uvsq.cprog.collex;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Command to list all machines in a domain.
 */
public class ListeDomaineCommande implements Commande {
  private final Dns dns;
  private final String domaine;
  private final boolean sortByIp;

  /**
   * Creates a command to list machines in a domain.
   *
   * @param dns the DNS database
   * @param domaine the domain name
   * @param sortByIp if true, sort by IP address; otherwise sort by machine name
   */
  public ListeDomaineCommande(Dns dns, String domaine, boolean sortByIp) {
    this.dns = dns;
    this.domaine = domaine;
    this.sortByIp = sortByIp;
  }

  @Override
  public Object execute() {
    List<DnsItem> items = sortByIp 
        ? dns.getItemsSortedByIp(domaine)
        : dns.getItems(domaine);

    if (items.isEmpty()) {
      return "Aucune machine trouvée pour le domaine : " + domaine;
    }

    return items.stream()
        .map(item -> item.getAdresseIp() + " " + item.getNomMachine())
        .collect(Collectors.joining("\n"));
  }
}
