package com.rdesouter.googleOauth;

public class ResponseBody {
    public String access_token;
    public int expires_in;
    public String scope;
    public String token_type;

    public String getAccess_token() {
        return access_token;
    }
    public int getExpires_in() {
        return expires_in;
    }
    public String getScope() {
        return scope;
    }
    public String getToken_type() {
        return token_type;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }
    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    @Override
    public String toString() {
        return "ResponseBody{\n\t" +
                "access_token='" + access_token + ";\n\t" +
                "expires_in=" + expires_in + ";\n\t" +
                "scope='" + scope + ";\n\t" +
                "token_type='" + token_type + ";\n" +
                '}';
    }
}
