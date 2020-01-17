package org.example.util;

public final class Mappings {

    public static final String HOME = "/";
    public static final String SLASH = "/";
    public static final String SHOW = "/show/";
    public static final String DELETE = "delete";
    public static final String DISPLAY = "display";
    public static final String DISPLAY_PHOTOS = "displayPhotos";
    public static final String UPDATE_DATA = "updateData";
    public static final String UPDATE_PHOTO = "updatePhoto";
    public static final String SHOW_IMG = "show_img/";

    //*** Mappings used flags.th.xml ***
    public static final String LANG_PL = "/?lang=pl";
    public static final String LANG_EN = "/?lang=en";

    public static final String ADD_PHOTO = "/add_photo/";
    public static final String PHOTO_ID = "{photoId}";

    public static final String ADD_ALBUM = "/add_album/";
    public static final String REMOVE_ALBUM = "/remove_album/";
    public static final String EDIT_ALBUM = "/edit_album/";
    public static final String SHOW_ALBUM = "/show_album/";
    public static final String ALBUM_ID = "{albumId}";

    public static final String ADD_PHOTO_ALBUM_ID = ADD_PHOTO + ALBUM_ID;
    public static final String SHOW_ALBUMS = SHOW + ALBUM_ID;
    public static final String EDIT_ALBUM_ALBUM_ID = EDIT_ALBUM + ALBUM_ID;
    public static final String SHOW_ALBUM_ALBUM_ID = SHOW_ALBUM + ALBUM_ID;
    public static final String DISPLAY_PHOTOS_ALBUM_ID_PHOTO_ID_DISPLAY =
            DISPLAY_PHOTOS + SLASH + ALBUM_ID + SLASH + PHOTO_ID + SLASH + DISPLAY;
    public static final String REMOVE_ALBUM_ALBUM_ID_DELETE = REMOVE_ALBUM + ALBUM_ID + SLASH + DELETE;
    public static final String EDIT_ALBUM_ALBUM_ID_UPDATE_DATA = EDIT_ALBUM + ALBUM_ID + SLASH + UPDATE_DATA;
    public static final String EDIT_ALBUM_ALBUM_ID_UPDATE_PHOTO = EDIT_ALBUM + ALBUM_ID + SLASH + UPDATE_PHOTO;
    public static final String EDIT_ALBUM_ALBUM_ID_PHOTO_ID_DELETE =
            EDIT_ALBUM + ALBUM_ID + SLASH + PHOTO_ID + SLASH + DELETE;
    public static final String ADD_PHOTO_ALBUM_ID_SHOW_PHOTO_ID_DELETE =
            ADD_PHOTO + ALBUM_ID + SHOW + PHOTO_ID + SLASH + DELETE;
    public static final String SHOW_IMG_ALBUM_ID_PHOTO_ID = SHOW_IMG + ALBUM_ID + SLASH + PHOTO_ID;

    private Mappings() {

    }
}
