package fr.uvsq.cprog.collex;

import java.util.Scanner;

/**
 * Main DNS application.
 */
public class DnsApp {
  private final DnsTUI tui;

  /**
   * Creates a DNS application.
   *
   * @param tui the text user interface
   */
  public DnsApp(DnsTUI tui) {
    this.tui = tui;
  }

  /**
   * Runs the DNS application main loop.
   */
  public void run() {
    System.out.println("DNS Simulator - Tapez 'quit' pour quitter");
    System.out.println("Commandes disponibles:");
    System.out.println("  <nom.qualifie>           - Afficher l'adresse IP");
    System.out.println("  <adresse.ip>             - Afficher le nom qualifié");
    System.out.println("  ls [-a] <domaine>        - Lister les machines du domaine");
    System.out.println("  add <ip> <nom.qualifie>  - Ajouter une entrée DNS");
    System.out.println("  quit                     - Quitter l'application");
    System.out.println();

    while (true) {
      System.out.print("> ");
      Commande cmd = tui.nextCommande();
      
      if (cmd == null) {
        continue;
      }

      Object result = cmd.execute();
      
      if ("quit".equals(result)) {
        System.out.println("Au revoir !");
        break;
      }

      tui.affiche(result);
    }
  }

  /**
   * Main entry point of the application.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    try {
      Dns dns = new Dns("application.properties");
      Scanner scanner = new Scanner(System.in);
      DnsTUI tui = new DnsTUI(scanner, dns);
      DnsApp app = new DnsApp(tui);
      app.run();
      scanner.close();
    } catch (DnsException e) {
      System.err.println("ERREUR FATALE : " + e.getMessage());
      System.exit(1);
    }
  }
}
