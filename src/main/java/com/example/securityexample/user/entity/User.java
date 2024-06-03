package com.example.securityexample.user.entity;

import com.example.securityexample.borough.entity.Borough;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

  @Id
  @Column(name = "USER_ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true, nullable = false)
  private String email;
  @Column(nullable = false)
  private String password;
  private String phone;
  @Column(unique = true, nullable = false)
  private String nickname;
  private String address;
  @ManyToOne
  @JoinColumn(name = "BOROUGH_ID")
  private Borough borough;

  public String extractBoroughName() {
    return this.address.split(" ")[1];
  }

}

