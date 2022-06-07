package com.littlepay.model.trips;

public class CompanyId {
    private static int counter = 0;
    private String id;

    public CompanyId() {
        id = "company" + (++counter);
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
