package org.hentaibot.dtos;

public class Rule34Dto {
    private String file_url;
    private String tags;
    private String image;

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "File url: " + this.file_url +
                "\nTags: " + this.tags +
                "\nImage: " + this.image;
    }
}
