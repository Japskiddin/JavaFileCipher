package com.github.japskiddin;

public class Main {
  public static void main(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException("No parameters found! Print --help for more information.");
    }

    CipherWorker cipherWorker = new CipherWorker();
    cipherWorker.checkArguments(args);
  }
}