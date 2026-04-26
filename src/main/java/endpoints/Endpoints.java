package endpoints;

public class Endpoints {

    public static final String CREATE_RECORD = "/api/collections/{slug}/records";
    public static final String GET_RECORDS = "/api/collections/{slug}/records";
    public static final String GET_RECORD_BY_ID = "/api/collections/{slug}/records/{id}";
    public static final String UPDATE_RECORD = "/api/collections/{slug}/records/{id}";
    public static final String DELETE_RECORD = "/api/collections/{slug}/records/{id}";
    public static final String DELETE_COLLECTION = "/api/collections/";
}
