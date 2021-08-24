package com.github.japskiddin;

public class Main {
  // TODO: 24.08.2021 Add option for cipher only one file
  public static void main(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException("No parameters found! Print --help for more information.");
    }

    CipherWorker cipherWorker = new CipherWorker();
    cipherWorker.checkArguments(args);
  }
}