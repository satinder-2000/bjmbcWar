package bjm.bc.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "EXPENSE_PARTY")
public class ExpenseParty {

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
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "PARTY_HASH")
    private String partyHash;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "EXPENSE_PARTY_ID")
    private List<ExpenseAccount> expenseAccounts = new ArrayList<>();

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPartyHash() {
        return partyHash;
    }

    public void setPartyHash(String partyHash) {
        this.partyHash = partyHash;
    }

    public List<ExpenseAccount> getExpenseAccounts() {
        return expenseAccounts;
    }

    public void setExpenseAccounts(List<ExpenseAccount> expenseAccounts) {
        this.expenseAccounts = expenseAccounts;
    }

}
