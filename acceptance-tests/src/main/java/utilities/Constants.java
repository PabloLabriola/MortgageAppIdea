package utilities;

public class Constants {

    static {
        GATEWAY_HOST = System.getProperty("gatewayhost","http://localhost:8760/");
    }

    //URL
    public static final String GATEWAY_HOST;
    public static final String FEASIBILTY_URI = "/mortgagefeasibility/validate";
    public static final String AUTHENTICATION_URI = "/companymanagement/auth/token";
    public static final String CONFIRM_URI = "/mortgagefeasibility/confirm";


    public static final String VALIDATE = "/validate";
    public static final String AUTH_TOKEN = "/auth/token";
    public static final String AUTH_REFRESH = "/auth/refreshtoken";

    //RESPONSE
    public static final String REJECTED = "REJECTED";
    public static final String APPROVED = "APPROVED";
    public static final String HOLD = "HOLD";
    public static final String CONFIRMED = "CONFIRMED";
    public static final String CANCELLED = "CANCELLED";



    public static final String COMPANY_SUBSCRIBED = "CHOAM";
    public static final String COMPANY_NOT_SUBSCRIBED = "AMACHON";

}
