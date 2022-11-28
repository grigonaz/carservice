package cz.cvut.kbss.ear.carservice.dto;

public class AuthorizationResponseDto {

    private String jwt;
    private String role;

    public AuthorizationResponseDto(String jwt, String role) {
        this.jwt = jwt;
        this.role = role;
    }

    public String getJwt() {
        return jwt;
    }

    public String getRole() {
        return role;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
