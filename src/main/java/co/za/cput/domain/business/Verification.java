package co.za.cput.domain.business;
//Firstname: Siyabonga
//Lastname:Jiyane
//Student Number: 222359676

import co.za.cput.domain.users.Administrator;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Verification {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long verificationID;
    private LocalDate verificationDate; //The date the actual verification took place
    private String notes;// Reason for rejection or extra context about the verification.
    private LocalDateTime createAt; //When the verification request was created.
    private LocalDateTime updateAt;//When the verification status was last updated.

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus;

    public String getVerificationId() {
        return verificationID != null ? verificationID.toString() : null;
    }

    public enum VerificationStatus {
        PENDING,
        APPROVED,
        REJECTED;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_ID", referencedColumnName = "adminID", nullable = true)
    @JsonBackReference
    private Administrator administrator;

    @OneToOne
    @JoinColumn(name = "accommodation_ID", referencedColumnName = "accommodationID", nullable = true)
    private Accommodation accommodation;

    protected Verification() {}

    private Verification(Builder builder) {
        this.verificationID = builder.verificationID;
        this.verificationDate = builder.verificationDate;
        this.notes = builder.notes;
        this.createAt = builder.createAt;
        this.updateAt = builder.updateAt;
        this.verificationStatus = builder.verificationStatus;
        this.administrator = builder.administrator;
        this.accommodation = builder.accommodation;
    }

    public Long getVerificationID() {
        return verificationID;
    }

    public LocalDate getVerificationDate() {
        return verificationDate;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    @Override
    public String toString() {
        return "Verification{" +
                "verificationID=" + verificationID +
                ", verificationDate=" + verificationDate +
                ", notes='" + notes + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", verificationStatus=" + verificationStatus +
                ", administrator=" + administrator +
                ", accommodation=" + accommodation +
                '}';
    }

    public static class Builder {
        private Long verificationID;
        private LocalDate verificationDate;
        private String notes;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
        private VerificationStatus verificationStatus;
        private Administrator administrator;
        private Accommodation accommodation;

        public Builder setVerificationID(Long verificationID) {
            this.verificationID = verificationID;
            return this;
        }

        public Builder setVerificationDate(LocalDate verificationDate) {
            this.verificationDate = verificationDate;
            return this;
        }
        public Builder setNotes(String notes) {
            this.notes = notes;
            return this;
        }
        public Builder setCreateAt(LocalDateTime createAt) {
            this.createAt = createAt;
            return this;
        }
        public Builder setUpdateAt(LocalDateTime updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public Builder setVerificationStatus(VerificationStatus verificationStatus) {
            this.verificationStatus = verificationStatus;
            return this;
        }

        public Builder setAdministrator(Administrator administrator) {
            this.administrator = administrator;
            return this;
        }
        public Builder setAccommodation(Accommodation accommodation) {
            this.accommodation = accommodation;
            return this;
        }

        public Builder copy(Verification verification) {
            this.verificationID = verification.getVerificationID();
            this.verificationDate = verification.getVerificationDate();
            this.notes = verification.getNotes();
            this.createAt = verification.getCreateAt();
            this.updateAt = verification.getUpdateAt();
            this.verificationStatus = verification.getVerificationStatus();
            this.administrator = verification.getAdministrator();
            this.accommodation = verification.getAccommodation();
            return this;
        }

        public Verification build() {
            return new Verification(this);
        }
    }
}
