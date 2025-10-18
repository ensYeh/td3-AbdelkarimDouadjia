package fr.uvsq.cprog.collex;

/**
 * Command to quit the application.
 */
public class QuitterCommande implements Commande {
  /**
   * Creates a command to quit the application.
   */
  public QuitterCommande() {
    // No parameters needed
  }

  @Override
  public Object execute() {
    return "quit";
  }
}
