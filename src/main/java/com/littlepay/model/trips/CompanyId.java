package com.littlepay.model.trips;

public class CompanyId {
    private String id;

    public CompanyId(String companyId) {
        this.id = companyId;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof CompanyId)) {
            return false;
        }

        CompanyId companyId = (CompanyId) o;

        return this.id.equals(companyId.getId());
    }
}
