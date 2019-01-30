package com.navismart.navismart.model;

public class AdminVerificationModel {
    String marinaName, marinaManagerName, email, receptionCapacity, locationAddress, description = "Not Uploaded", termsAndCondition = "Not Uploaded";

    public AdminVerificationModel() {
    }

    public String getMarinaName() {
        return marinaName;
    }

    public void setMarinaName(String marinaName) {
        this.marinaName = marinaName;
    }

    public String getMarinaManagerName() {
        return marinaManagerName;
    }

    public void setMarinaManagerName(String marinaManagerName) {
        this.marinaManagerName = marinaManagerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReceptionCapacity() {
        return receptionCapacity;
    }

    public void setReceptionCapacity(String receptionCapacity) {
        this.receptionCapacity = receptionCapacity;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTermsAndCondition() {
        return termsAndCondition;
    }

    public void setTermsAndCondition(String termsAndCondition) {
        this.termsAndCondition = termsAndCondition;
    }
}
