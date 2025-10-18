package fr.uvsq.cprog.collex;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * DNS database class that manages DNS entries with file persistence.
 */
@SuppressWarnings("AbbreviationAsWordInName")
public class Dns {
  private final Map<AdresseIP, DnsItem> ipMap;
  private final Map<NomMachine, DnsItem> nameMap;
  private final Path databasePath;

  /**
   * Creates a DNS database and loads entries from the properties file.
   *
   * @param propertiesFile the path to the properties file
   * @throws DnsException if the database cannot be loaded
   */
  public Dns(String propertiesFile) throws DnsException {
    this.ipMap = new HashMap<>();
    this.nameMap = new HashMap<>();

    try {
      Properties props = new Properties();
      try (InputStream input = getClass().getClassLoader()
          .getResourceAsStream(propertiesFile)) {
        if (input == null) {
          throw new DnsException("Fichier de propriétés introuvable : " + propertiesFile);
        }
        props.load(input);
      }

      String dbFile = props.getProperty("dns.database.file");
      if (dbFile == null) {
        throw new DnsException("Propriété 'dns.database.file' non trouvée");
      }

      this.databasePath = Paths.get(dbFile);
      loadDatabase();
    } catch (IOException e) {
      throw new DnsException("Erreur lors du chargement de la base de données", e);
    }
  }

  /**
   * Constructor for testing with a specific database path.
   *
   * @param databasePath the path to the database file
   * @throws DnsException if the database cannot be loaded
   */
  Dns(Path databasePath) throws DnsException {
    this.ipMap = new HashMap<>();
    this.nameMap = new HashMap<>();
    this.databasePath = databasePath;
    loadDatabase();
  }

  private void loadDatabase() throws DnsException {
    if (!Files.exists(databasePath)) {
      return; // Empty database
    }

    try {
      List<String> lines = Files.readAllLines(databasePath);
      for (String line : lines) {
        line = line.trim();
        if (line.isEmpty() || line.startsWith("#")) {
          continue;
        }

        String[] parts = line.split("\\s+");
        if (parts.length != 2) {
          continue; // Skip invalid lines
        }

        try {
          NomMachine nom = new NomMachine(parts[0]);
          AdresseIP ip = new AdresseIP(parts[1]);
          DnsItem item = new DnsItem(ip, nom);
          ipMap.put(ip, item);
          nameMap.put(nom, item);
        } catch (IllegalArgumentException e) {
          // Skip invalid entries
        }
      }
    } catch (IOException e) {
      throw new DnsException("Erreur lors de la lecture de la base de données", e);
    }
  }

  /**
   * Gets a DNS entry by IP address.
   *
   * @param ip the IP address to search for
   * @return the DNS entry, or null if not found
   */
  public DnsItem getItem(AdresseIP ip) {
    return ipMap.get(ip);
  }

  /**
   * Gets a DNS entry by machine name.
   *
   * @param nom the machine name to search for
   * @return the DNS entry, or null if not found
   */
  public DnsItem getItem(NomMachine nom) {
    return nameMap.get(nom);
  }

  /**
   * Gets all DNS entries for a given domain, sorted by machine name.
   *
   * @param domain the domain name
   * @return list of DNS entries in the domain, sorted by machine name
   */
  public List<DnsItem> getItems(String domain) {
    return nameMap.values().stream()
        .filter(item -> item.getNomMachine().getNomDomaine().equals(domain))
        .sorted(Comparator.comparing(item -> item.getNomMachine().getNomMachine()))
        .collect(Collectors.toList());
  }

  /**
   * Gets all DNS entries for a given domain, sorted by IP address.
   *
   * @param domain the domain name
   * @return list of DNS entries in the domain, sorted by IP address
   */
  public List<DnsItem> getItemsSortedByIp(String domain) {
    return nameMap.values().stream()
        .filter(item -> item.getNomMachine().getNomDomaine().equals(domain))
        .sorted(Comparator.comparing(item -> item.getAdresseIp().getAdresse()))
        .collect(Collectors.toList());
  }

  /**
   * Adds a new DNS entry to the database.
   *
   * @param ip the IP address
   * @param nom the machine name
   * @throws DnsException if the IP or name already exists, or if saving fails
   */
  public void addItem(AdresseIP ip, NomMachine nom) throws DnsException {
    if (ipMap.containsKey(ip)) {
      throw new DnsException("L'adresse IP existe déjà : " + ip);
    }
    if (nameMap.containsKey(nom)) {
      throw new DnsException("Le nom de machine existe déjà : " + nom);
    }

    DnsItem item = new DnsItem(ip, nom);
    ipMap.put(ip, item);
    nameMap.put(nom, item);

    saveToFile();
  }

  private void saveToFile() throws DnsException {
    try {
      List<String> lines = new ArrayList<>();
      for (DnsItem item : nameMap.values()) {
        lines.add(item.getNomMachine() + " " + item.getAdresseIp());
      }

      // Sort lines for consistent output
      lines.sort(String::compareTo);

      Files.write(databasePath, lines, StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
      throw new DnsException("Erreur lors de la sauvegarde de la base de données", e);
    }
  }
}
