package cz.cvut.kbss.ear.carservice.dto;

import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.model.Role;

public class RoleDto {
    String roleStr;
    Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(String roleStr) {
        this.roleStr = roleStr;
        switch (roleStr) {
            case "ROLE_ADMIN":
                this.role = Role.ADMIN;
                break;
            case "ROLE_CLIENT":
                this.role = Role.CLIENT;
                break;
            case "ROLE_EMPLOYEE":
                this.role = Role.EMPLOYEE;
                break;
            default:
                throw new NotFoundException("Role with name " + roleStr + " doesn't exist");
        }
    }
}
