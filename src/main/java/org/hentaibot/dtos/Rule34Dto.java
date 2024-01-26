package org.hentaibot.dtos;

// DTO - data transfer object


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
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "File url: " + file_url +
                "\nTags: " + tags +
                "\nImage: " + image;
    }
}
