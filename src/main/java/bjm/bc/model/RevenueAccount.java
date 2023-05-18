package bjm.bc.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "REVENUE_ACCOUNT")
public class RevenueAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "REVENUE_PARTY_ID")
    private int revenuePartyId;
    @Column(name = "REVENUE_ACCOUNT_HASH")
    private String revenueAccountHash;
    @Column(name = "REVENUE_CATEGORY_ID")
    private int revenueCategoryId;
    @Column(name = "NAME")
    private String name;
    @Column(name="CREATED_ON")
    private LocalDateTime createdOn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRevenuePartyId() {
        return revenuePartyId;
    }

    public void setRevenuePartyId(int revenuePartyId) {
        this.revenuePartyId = revenuePartyId;
    }

    

    public String getRevenueAccountHash() {
        return revenueAccountHash;
    }

    public void setRevenueAccountHash(String revenueAccountHash) {
        this.revenueAccountHash = revenueAccountHash;
    }

    public int getRevenueCategoryId() {
        return revenueCategoryId;
    }

    public void setRevenueCategoryId(int revenueCategoryId) {
        this.revenueCategoryId = revenueCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
