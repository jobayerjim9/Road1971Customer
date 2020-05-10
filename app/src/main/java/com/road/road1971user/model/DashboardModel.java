package com.road.road1971user.model;

public class DashboardModel {
    private String tittle, description, bannerLink, loadUri;

    public DashboardModel() {
    }

    public void setLoadUri(String loadUri) {
        this.loadUri = loadUri;
    }

    public String getLoadUri() {
        return loadUri;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBannerLink() {
        return bannerLink;
    }

    public void setBannerLink(String bannerLink) {
        this.bannerLink = bannerLink;
    }
}
