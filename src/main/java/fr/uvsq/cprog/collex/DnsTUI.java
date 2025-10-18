package fr.uvsq.cprog.collex;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Text User Interface for DNS operations.
 * Handles user input parsing and output display.
 */
@SuppressWarnings("AbbreviationAsWordInName")
public class DnsTUI {
  private static final Pattern IP_PATTERN = 
      Pattern.compile("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.){3}"
          + "(25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)$");
  private static final Pattern QUALIFIED_NAME_PATTERN = Pattern.compile("^[^\\s]+\\.[^\\s]+$");
  
  private final Scanner scanner;
  private final Dns dns;

  /**
   * Creates a DNS TUI.
   *
   * @param scanner the scanner for reading user input
   * @param dns the DNS database
   */
  public DnsTUI(Scanner scanner, Dns dns) {
    this.scanner = scanner;
    this.dns = dns;
  }

  /**
   * Reads and parses the next command from user input.
   *
   * @return the parsed command, or null if input is invalid
   */
  public Commande nextCommande() {
    if (!scanner.hasNextLine()) {
      return null;
    }

    String line = scanner.nextLine().trim();
    if (line.isEmpty()) {
      return null;
    }

    String[] parts = line.split("\\s+");
    
    // Check for quit command
    if (parts[0].equalsIgnoreCase("quit")) {
      return new QuitterCommande();
    }

    // Check for ls command
    if (parts[0].equalsIgnoreCase("ls")) {
      return parseListeCommande(parts);
    }

    // Check for add command
    if (parts[0].equalsIgnoreCase("add")) {
      return parseAjoutCommande(parts);
    }

    // Check if it's an IP address
    if (IP_PATTERN.matcher(parts[0]).matches()) {
      try {
        AdresseIP ip = new AdresseIP(parts[0]);
        return new RechercheIpCommande(dns, ip);
      } catch (IllegalArgumentException e) {
        System.out.println("ERREUR : Adresse IP invalide");
        return null;
      }
    }

    // Check if it's a qualified name
    if (QUALIFIED_NAME_PATTERN.matcher(parts[0]).matches()) {
      try {
        NomMachine nom = new NomMachine(parts[0]);
        return new RechercheNomCommande(dns, nom);
      } catch (IllegalArgumentException e) {
        System.out.println("ERREUR : Nom de machine invalide");
        return null;
      }
    }

    System.out.println("ERREUR : Commande non reconnue");
    return null;
  }

  private Commande parseListeCommande(String[] parts) {
    boolean sortByIp = false;
    String domain;

    if (parts.length == 2) {
      // ls <domain>
      domain = parts[1];
    } else if (parts.length == 3 && parts[1].equals("-a")) {
      // ls -a <domain>
      sortByIp = true;
      domain = parts[2];
    } else {
      System.out.println("ERREUR : Usage: ls [-a] <domaine>");
      return null;
    }

    return new ListeDomaineCommande(dns, domain, sortByIp);
  }

  private Commande parseAjoutCommande(String[] parts) {
    if (parts.length != 3) {
      System.out.println("ERREUR : Usage: add <adresse_ip> <nom_qualifie>");
      return null;
    }

    try {
      AdresseIP ip = new AdresseIP(parts[1]);
      NomMachine nom = new NomMachine(parts[2]);
      return new AjoutCommande(dns, ip, nom);
    } catch (IllegalArgumentException e) {
      System.out.println("ERREUR : " + e.getMessage());
      return null;
    }
  }

  /**
   * Displays the result of a command execution.
   *
   * @param result the result to display
   */
  public void affiche(Object result) {
    if (result != null) {
      System.out.println(result);
    }
  }
}
