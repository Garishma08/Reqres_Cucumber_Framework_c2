package endpoints;

public class Endpoints {

    public static final String CREATE_RECORD = "/api/collections/{slug}/records";
    public static final String GET_RECORDS = "/api/collections/{slug}/records";
    public static final String GET_RECORD_BY_ID = "/api/collections/{slug}/records/{id}";
    public static final String UPDATE_RECORD = "/api/collections/{slug}/records/{id}";
    public static final String DELETE_RECORD = "/api/collections/{slug}/records/{id}";
    public static final String DELETE_COLLECTION = "/api/collections/";
    public static final String CREATE_USER = "/api/users";
    public static final String GET_USERS = "/api/users";
    public static final String GET_USERS_BY_PAGE = "/api/users?page=";
    public static final String GET_USER_BY_ID = "/api/users/";
    public static final String INVALID_USERS = "/api/users/999";
    public static final String REGISTER = "/api/register";
    public static final String LOGIN = "/api/login";
    public static final String LIST_COLORS = "/api/unknown";
    public static final String GET_COLLECTIONS = "/api/collections";
    public static final String CREATE_COLLECTION = "/api/collections";
    public static final String GET_COLLECTION_BY_SLUG = "/api/collections/";
    public static final String UPDATE_COLLECTION = "/api/collections/";

    public static final String UPDATE_USER = "/api/users/";
    public static final String PATCH_USER = "/api/users/";
    public static final String DELETE_USER = "/api/users/";
}
