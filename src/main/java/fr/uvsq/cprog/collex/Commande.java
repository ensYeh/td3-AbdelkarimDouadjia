package fr.uvsq.cprog.collex;

/**
 * Command pattern interface for DNS operations.
 * Each command encapsulates a specific action that can be executed.
 */
public interface Commande {
  /**
   * Executes the command and returns the result.
   *
   * @return the result of the command execution, or null if no result
   */
  Object execute();
}
