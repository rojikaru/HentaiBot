package org.hentaibot.dtos;

// DTO - data transfer object

/**
 *
 * {
 *     "preview_url": "https://api-cdn.rule34.xxx/thumbnails/5377/thumbnail_4388d1ab31d98c6d0fd7ed114456b79f.jpg",
 *     "sample_url": "https://api-cdn.rule34.xxx/samples/5377/sample_4388d1ab31d98c6d0fd7ed114456b79f.jpg",
 *     "file_url": "https://api-cdn.rule34.xxx/images/5377/4388d1ab31d98c6d0fd7ed114456b79f.png",
 *     "directory": 5377,
 *     "hash": "4388d1ab31d98c6d0fd7ed114456b79f",
 *     "width": 2048,
 *     "height": 12672,
 *     "id": 9416035,
 *     "image": "4388d1ab31d98c6d0fd7ed114456b79f.png",
 *     "change": 1706095421,
 *     "owner": "antipenitant",
 *     "parent_id": 0,
 *     "rating": "explicit",
 *     "sample": true,
 *     "sample_height": 5259,
 *     "sample_width": 850,
 *     "score": 0,
 *     "tags": "anus ass breasts danganronpa harukawa_maki pussy tagme ultimateenf",
 *     "source": "https://www.deviantart.com/ultimateenf/art/Maki-chained-down-849989950",
 *     "status": "active",
 *     "has_notes": false,
 *     "comment_count": 0
 *   }
 */
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
}
