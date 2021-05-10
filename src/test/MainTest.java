package test;

import com.github.japskiddin.CipherWorker;

class MainTest {
  @org.junit.jupiter.api.Test
  void main() {
    String key = "1234567891234567";
    String[] argsEncrypt =
        new String[] { "--encrypt", "-src", "./test", "-dst", "./", "-key", key };
    new CipherWorker().checkArguments(argsEncrypt);

    String[] argsDecrypt =
        new String[] { "--decrypt", "-src", "./outputs_encrypted", "-dst", "./", "-key", key };
    new CipherWorker().checkArguments(argsDecrypt);
  }
}