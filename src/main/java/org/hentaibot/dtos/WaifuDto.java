package org.hentaibot.dtos;

public class WaifuDto {
    private String url;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    @Override
    public String toString() {
        return "File url: " + url;
    }
}
