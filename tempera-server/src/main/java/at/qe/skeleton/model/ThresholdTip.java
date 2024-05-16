package at.qe.skeleton.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;

@Entity
public class ThresholdTip implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String tip;

  public ThresholdTip(String tip) {
    this.tip = tip;
  }

  public ThresholdTip() {}

  public String getTip() {
    return tip;
  }

  @Override
  public int hashCode() {
    return this.tip.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (!(o instanceof ThresholdTip other)) {
      return false;
    }
    return other.tip.equals(this.tip);
  }

  @Override
  public String toString() {
    return tip;
  }
}
