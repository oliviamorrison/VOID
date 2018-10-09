package gameworld;

public enum Item {

  Antidote, Beer, BoltCutter, Coin, Diffuser, HealthPack;

  public String toString() {

    switch (this) {
      case Antidote:
        return "antidote";
      case Beer:
        return "beer";
      case BoltCutter:
        return "bolt cutter";
      case Coin:
        return "coin";
      case Diffuser:
        return "diffuser";
      case HealthPack:
        return "health pack";
      default:
        return null;
    }
  }

}
