package com.github.japskiddin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;

/**
 * Main worker class.
 */
public class CipherWorker {
  /**
   * Checks parameters and decides what to do next.
   *
   * @param args Entered parameters.
   */
  public void checkArguments(String[] args) {
    List<Option> optsList = new ArrayList<>();
    List<String> doubleOptsList = new ArrayList<>();

    for (int i = 0; i < args.length; i++) {
      if (args[i].charAt(0) == '-') {
        if (args[i].length() < 2) {
          throw new IllegalArgumentException("Not a valid argument: " + args[i]);
        }
        if (args[i].charAt(1) == '-') {
          if (args[i].length() < 3) {
            throw new IllegalArgumentException("Not a valid argument: " + args[i]);
          }
          // --opt
          doubleOptsList.add(args[i].substring(2));
        } else {
          if (args.length - 1 == i) {
            throw new IllegalArgumentException("Expected arg after: " + args[i]);
          }
          // -opt
          optsList.add(new Option(args[i].substring(1), args[i + 1]));
          i++;
        }
      }
    }

    String key = null, src = null, dst = null;

    for (Option option : optsList) {
      switch (option.getFlag()) {
        case "key" -> key = option.getOpt();
        case "dst" -> dst = option.getOpt();
        case "src" -> src = option.getOpt();
      }
    }

    for (String opt : doubleOptsList) {
      switch (opt) {
        case "version" -> {
          Package p = this.getClass().getPackage();
          System.out.println("Version: " + p.getImplementationVersion());
        }
        case "help" -> showHelp();
        case "decrypt" -> doCipher(Cipher.DECRYPT_MODE, src, dst, key);
        case "encrypt" -> doCipher(Cipher.ENCRYPT_MODE, src, dst, key);
      }
    }
  }

  /**
   * Checks all parameters and prepares files before crypt operations.
   *
   * @param type Type of operation (encrypt / decrypt).
   * @param src Path to folder with source files.
   * @param dst Path to folder with output files.
   * @param key Cipher key.
   */
  private void doCipher(int type, String src, String dst, String key) {
    if (src == null) {
      throw new IllegalArgumentException("Expected arg \"-src\"");
    }
    if (dst == null) {
      throw new IllegalArgumentException("Expected arg \"-dst\"");
    }
    if (key == null) {
      throw new IllegalArgumentException("Expected arg \"-key\"");
    }

    File dir = new File(src);
    if (!dir.exists()) {
      throw new NullPointerException("Folder doesn't exists.");
    }

    if (!dir.isDirectory()) {
      throw new NullPointerException("File isn't directory!");
    }

    File[] files = dir.listFiles();
    if (files == null || files.length == 0) {
      throw new NullPointerException("Folder is empty.");
    }

    File dstDir =
        new File(dst, "outputs" + (type == Cipher.ENCRYPT_MODE ? "_encrypted" : "_decrypted"));
    if (dstDir.exists()) {
      try {
        Utils.deleteDir(dstDir);
      } catch (IOException e) {
        System.err.println(e.getMessage());
        e.printStackTrace();
        return;
      }
    }

    boolean created = dstDir.mkdirs();
    if (!created) {
      throw new NullPointerException("Can't create output folder.");
    }

    for (File srcFile : files) {
      File dstFile = new File(dstDir, srcFile.getName());
      try {
        if (type == Cipher.ENCRYPT_MODE) {
          CryptoUtils.encrypt(key, srcFile, dstFile);
        } else {
          CryptoUtils.decrypt(key, srcFile, dstFile);
        }
      } catch (CryptoException ex) {
        System.out.println(ex.getMessage());
        ex.printStackTrace();
        return;
      }
    }

    System.out.println(
        (type == Cipher.ENCRYPT_MODE ? "Encryption" : "Decryption") + " done successfully!");
  }

  /**
   * Shows help information.
   */
  private void showHelp() {
    System.out.println(
        "Usage: [--encrypt | --decrypt | --help] -src <path> -dst <path> -key <key>");
    System.out.println("\n--help - Show help information");
    System.out.println("--version - Show library version");
    System.out.println("--encrypt - Encrypt files");
    System.out.println("--decrypt - Decrypt files");
    System.out.println("-src <path> - Path to folder with source files");
    System.out.println("-dst <path> - Path to folder with output files");
    System.out.println("-key <key> - Cipher key");
  }
}