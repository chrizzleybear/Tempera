package at.qe.skeleton.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "threshold_tip")
public class ThresholdTip implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 1000)
  private String tip;

  public ThresholdTip(String tip) {
    this.tip = tip;
  }

  public ThresholdTip() {this.tip = "No tips given."; }

  public Long getId() {
    return id;
  }

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
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
