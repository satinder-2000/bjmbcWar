package bjm.bc.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity(name = "REVENUE_PARTY")
public class RevenueParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "ORGANISATION")
    private String organisation;
    @Column(name = "MEMORABLE_DATE")
    private LocalDate memorableDate;
    @Column(name = "OWNER_ADHAAR_NUMBER")
    private String ownerAdhaarNumber;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PARTY_HASH")
    private String partyHash;
    @Column(name = "LINE1")
    private String line1;
    @Column(name = "STREET")
    private String street;
    @Column(name = "POST_CODE")
    private String postCode;
    @Column(name = "DISTRICT")
    private String district;
    @Column(name = "STATE_CODE")
    private String stateCode;
    @Column(name = "STATE")
    private String state;
    @Column(name = "COUNTRY")
    private String country;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "REVENUE_PARTY_ID")
    private List<RevenueAccount> revenueAccounts = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public LocalDate getMemorableDate() {
        return memorableDate;
    }

    public void setMemorableDate(LocalDate memorableDate) {
        this.memorableDate = memorableDate;
    }

    public String getOwnerAdhaarNumber() {
        return ownerAdhaarNumber;
    }

    public void setOwnerAdhaarNumber(String ownerAdhaarNumber) {
        this.ownerAdhaarNumber = ownerAdhaarNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPartyHash() {
        return partyHash;
    }

    public void setPartyHash(String partyHash) {
        this.partyHash = partyHash;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<RevenueAccount> getRevenueAccounts() {
        return revenueAccounts;
    }

    public void setRevenueAccounts(List<RevenueAccount> revenueAccounts) {
        this.revenueAccounts = revenueAccounts;
    }

}