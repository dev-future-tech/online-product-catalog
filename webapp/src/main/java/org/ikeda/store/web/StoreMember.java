package org.ikeda.store.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.inject.Named;
import org.ikeda.store.web.ejb.ProductBean;

import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
public class StoreMember {
    private static final Logger logger
            = Logger.getLogger(StoreMember.class.getName());
    @ManagedProperty(value = "#{param.pageId}")
    private String pageId;

    private String username;

    private String memberPoints;

    public String getUsername() {
        return "Jackson";
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
        logger.log(Level.INFO, "Going to page{0}", pageId);
        return pageId;
    }
}
