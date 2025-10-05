package co.za.cput.domain.business;

//Firstname:        Sinhle Xiluva
//LastName:         Mthethwa
//Student Number:   221802797.

import co.za.cput.domain.generic.Address;
import co.za.cput.domain.users.Landlord;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Accommodation {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long accommodationID;
    private double rent;
    private boolean wifiAvailable;
    private boolean furnished;
    private double distanceFromCampus;
    private boolean utilitiesIncluded;// utilities like water/electricity/WI-FI are included

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    public enum RoomType {
        SINGLE,
        DOUBLE,
        SHARED,
        EN_SUITE
    }

    @Enumerated(EnumType.STRING)
    private BathroomType bathroomType;

    public enum BathroomType {
        PRIVATE,
        SHARED
    }

    @Enumerated(EnumType.STRING)
    private AccommodationStatus accommodationStatus;

    public enum AccommodationStatus {
        AVAILABLE,
        FULL,
    }

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_ID", referencedColumnName = "addressID")
    private Address address;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "landLord_ID", referencedColumnName = "landLordID")
    @JsonBackReference("landlord-accommodation")
    private Landlord landlord;

    @OneToMany(mappedBy ="accommodation",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)//"accommodation" must match the object name in booking;
    @JsonManagedReference
    private List<Booking> bookings = new ArrayList<Booking>();

    protected Accommodation() {
    }

    private Accommodation(Accommodation.Builder builder) {
        this.accommodationID = builder.accommodationID;
        this.rent = builder.rent;
        this.wifiAvailable = builder.wifiAvailable;
        this.furnished = builder.furnished;
        this.distanceFromCampus = builder.distanceFromCampus;
        this.utilitiesIncluded = builder.utilitiesIncluded;
        this.roomType = builder.roomType;
        this.bathroomType = builder.bathroomType;
        this.accommodationStatus = builder.accommodationStatus;
        this.address = builder.address;
        this.landlord = builder.landlord;
        this.bookings = builder.bookings;
    }

    public Long getAccommodationID() {
        return accommodationID;
    }

    public double getRent() {
        return rent;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public AccommodationStatus getAccommodationStatus() {
        return accommodationStatus;
    }

    public boolean getIsWifiAvailable() {
        return wifiAvailable;
    }

    public boolean getIsFurnished() {
        return furnished;
    }

    public double getDistanceFromCampus() {
        return distanceFromCampus;
    }

    public boolean getIsUtilitiesIncluded() {
        return utilitiesIncluded;
    }

    public BathroomType getBathroomType() {
        return bathroomType;
    }

    public Address getAddress() {
        return address;
    }

    public Landlord getLandlord() {
        return landlord;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    @Override
    public String toString() {
        return "Accommodation{" +
                "accommodationID=" + accommodationID +
                ", rent=" + rent +
                ", wifiAvailable=" + wifiAvailable +
                ", furnished=" + furnished +
                ", distanceFromCampus=" + distanceFromCampus +
                ", utilitiesIncluded=" + utilitiesIncluded +
                ", roomType=" + roomType +
                ", bathroomType=" + bathroomType +
                ", accommodationStatus=" + accommodationStatus +
                ", address=" + address +
                ", landlordID=" + (landlord != null ? landlord.getLandlordID() : "null") +
                ", bookings=" + bookings +
                '}';
    }

    public static class Builder {
        private Long accommodationID;
        private double rent;
        private boolean wifiAvailable;
        private boolean furnished;
        private double distanceFromCampus;
        private boolean utilitiesIncluded;
        private RoomType roomType;
        private BathroomType bathroomType;
        private AccommodationStatus accommodationStatus;
        private Address address;
        private Landlord landlord;
        private List<Booking> bookings;


        public Builder setAccommodationID(Long accommodationID) {
            this.accommodationID = accommodationID;
            return this;
        }

        public Builder setRent(double rent) {
            this.rent = rent;
            return this;
        }

        public Builder setWifiAvailable(boolean wifiAvailable) {
            this.wifiAvailable = wifiAvailable;
            return this;
        }
        public Builder setFurnished(boolean furnished) {
            this.furnished = furnished;
            return this;
        }
        public Builder setDistanceFromCampus(double distanceFromCampus) {
            this.distanceFromCampus = distanceFromCampus;
            return this;
        }
        public Builder setUtilitiesIncluded(boolean utilitiesIncluded) {
            this.utilitiesIncluded = utilitiesIncluded;
            return this;
        }
        public Builder setBathroomType(BathroomType bathroomType) {
            this.bathroomType = bathroomType;
            return this;
        }

        public Builder setRoomType(RoomType roomType) {
            this.roomType = roomType;
            return this;
        }

        public Builder setAccommodationStatus(AccommodationStatus accommodationStatus) {
            this.accommodationStatus = accommodationStatus;
            return this;
        }

        public Builder setAddress(Address address) {
            this.address = address;
            return this;
        }

       public Builder setLandlord(Landlord landlord) {
            this.landlord = landlord;
            return this;
        }

        public Builder setBookings(List<Booking> bookings) {
            this.bookings = bookings;
            return this;
        }

        public Accommodation.Builder copy(Accommodation accommodation) {
            this.accommodationID = accommodation.getAccommodationID();
            this.rent = accommodation.getRent();
            this.wifiAvailable = accommodation.getIsWifiAvailable();
            this.furnished = accommodation.getIsFurnished();
            this.distanceFromCampus = accommodation.getDistanceFromCampus();
            this.utilitiesIncluded = accommodation.getIsUtilitiesIncluded();
            this.roomType = accommodation.getRoomType();
            this.bathroomType = accommodation.getBathroomType();
            this.accommodationStatus = accommodation.getAccommodationStatus();
            this.address = accommodation.getAddress();
            this.landlord = accommodation.getLandlord();
            return this;
        }

        public Accommodation build() {
            return new Accommodation(this);
        }
    }
}



