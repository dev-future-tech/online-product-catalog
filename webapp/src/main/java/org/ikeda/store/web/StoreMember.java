package org.ikeda.store.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.inject.Named;

@Named
@RequestScoped
public class StoreMember {

    @ManagedProperty(value = "#{param.pageId}")
    private String pageId;

    private String username;

    private String memberPoints;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMemberPoints() {
        return memberPoints;
    }

    public void setMemberPoints(String memberPoints) {
        this.memberPoints = memberPoints;
    }

    public String goToPage() {
        return pageId;
    }
}
