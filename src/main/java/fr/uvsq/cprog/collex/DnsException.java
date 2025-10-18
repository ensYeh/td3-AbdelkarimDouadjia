package fr.uvsq.cprog.collex;

/**
 * Exception thrown when a DNS operation fails.
 */
public class DnsException extends Exception {
  /**
   * Creates a DNS exception with a message.
   *
   * @param message the error message
   */
  public DnsException(String message) {
    super(message);
  }

  /**
   * Creates a DNS exception with a message and a cause.
   *
   * @param message the error message
   * @param cause the cause of the exception
   */
  public DnsException(String message, Throwable cause) {
    super(message, cause);
  }
}
