package dasturlash.uz.enums;

import java.util.LinkedList;
import java.util.List;

public enum Role{
    ROLE_USER, ROLE_ADMIN, ROLE_MODERATOR, ROLE_PUBLISH;

    public static List<Role> values(String[] roles) {
        if (roles == null || roles.length == 0) {
            return new LinkedList<>();
        }
        List<Role> roleList = new LinkedList<>();
        for (String role : roles) {
            roleList.add(Role.valueOf(role));
        }
        return roleList;
    }

    public static List<Role> valuesFromStr(String valuesStr) {
        if (valuesStr == null) {
            return new LinkedList<>();
        }
        return values(valuesStr.split(","));
    }
}
