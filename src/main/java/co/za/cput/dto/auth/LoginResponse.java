package co.za.cput.dto.auth;

public class LoginResponse {

    private boolean success;
    private String message;
    private UserRole role;
    private Long userId;
    private String fullName;
    private String email;
    private boolean requiresVerification;

    public LoginResponse() {
    }

    private LoginResponse(Builder builder) {
        this.success = builder.success;
        this.message = builder.message;
        this.role = builder.role;
        this.userId = builder.userId;
        this.fullName = builder.fullName;
        this.email = builder.email;
        this.requiresVerification = builder.requiresVerification;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public UserRole getRole() {
        return role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isRequiresVerification() {
        return requiresVerification;
    }

    public static class Builder {
        private boolean success;
        private String message;
        private UserRole role;
        private Long userId;
        private String fullName;
        private String email;
        private boolean requiresVerification;

        public Builder setSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setRole(UserRole role) {
            this.role = role;
            return this;
        }

        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setRequiresVerification(boolean requiresVerification) {
            this.requiresVerification = requiresVerification;
            return this;
        }

        public LoginResponse build() {
            return new LoginResponse(this);
        }
    }
}