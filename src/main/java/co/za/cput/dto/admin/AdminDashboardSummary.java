package co.za.cput.dto.admin;

public class AdminDashboardSummary {

    private long totalStudents;
    private long verifiedStudents;
    private long totalLandlords;
    private long verifiedLandlords;
    private long totalAccommodations;
    private long availableAccommodations;
    private long pendingVerifications;
    private long approvedVerifications;
    private long rejectedVerifications;

    public AdminDashboardSummary() {
    }

    private AdminDashboardSummary(Builder builder) {
        this.totalStudents = builder.totalStudents;
        this.verifiedStudents = builder.verifiedStudents;
        this.totalLandlords = builder.totalLandlords;
        this.verifiedLandlords = builder.verifiedLandlords;
        this.totalAccommodations = builder.totalAccommodations;
        this.availableAccommodations = builder.availableAccommodations;
        this.pendingVerifications = builder.pendingVerifications;
        this.approvedVerifications = builder.approvedVerifications;
        this.rejectedVerifications = builder.rejectedVerifications;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public long getVerifiedStudents() {
        return verifiedStudents;
    }

    public long getTotalLandlords() {
        return totalLandlords;
    }

    public long getVerifiedLandlords() {
        return verifiedLandlords;
    }

    public long getTotalAccommodations() {
        return totalAccommodations;
    }

    public long getAvailableAccommodations() {
        return availableAccommodations;
    }

    public long getPendingVerifications() {
        return pendingVerifications;
    }

    public long getApprovedVerifications() {
        return approvedVerifications;
    }

    public long getRejectedVerifications() {
        return rejectedVerifications;
    }

    public static class Builder {
        private long totalStudents;
        private long verifiedStudents;
        private long totalLandlords;
        private long verifiedLandlords;
        private long totalAccommodations;
        private long availableAccommodations;
        private long pendingVerifications;
        private long approvedVerifications;
        private long rejectedVerifications;

        public Builder setTotalStudents(long totalStudents) {
            this.totalStudents = totalStudents;
            return this;
        }

        public Builder setVerifiedStudents(long verifiedStudents) {
            this.verifiedStudents = verifiedStudents;
            return this;
        }

        public Builder setTotalLandlords(long totalLandlords) {
            this.totalLandlords = totalLandlords;
            return this;
        }

        public Builder setVerifiedLandlords(long verifiedLandlords) {
            this.verifiedLandlords = verifiedLandlords;
            return this;
        }

        public Builder setTotalAccommodations(long totalAccommodations) {
            this.totalAccommodations = totalAccommodations;
            return this;
        }

        public Builder setAvailableAccommodations(long availableAccommodations) {
            this.availableAccommodations = availableAccommodations;
            return this;
        }

        public Builder setPendingVerifications(long pendingVerifications) {
            this.pendingVerifications = pendingVerifications;
            return this;
        }

        public Builder setApprovedVerifications(long approvedVerifications) {
            this.approvedVerifications = approvedVerifications;
            return this;
        }

        public Builder setRejectedVerifications(long rejectedVerifications) {
            this.rejectedVerifications = rejectedVerifications;
            return this;
        }

        public AdminDashboardSummary build() {
            return new AdminDashboardSummary(this);
        }
    }
}